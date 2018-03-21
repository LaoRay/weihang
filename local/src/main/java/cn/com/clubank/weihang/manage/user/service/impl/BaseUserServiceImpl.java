package cn.com.clubank.weihang.manage.user.service.impl;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.redis.JedisClient;
import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.DESUtils;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.member.dao.JpushInfoMapper;
import cn.com.clubank.weihang.manage.member.pojo.JpushInfo;
import cn.com.clubank.weihang.manage.repair.dao.WorkEvaluateMapper;
import cn.com.clubank.weihang.manage.systemSettings.dao.BaseDepartmentMapper;
import cn.com.clubank.weihang.manage.systemSettings.dao.BaseRoleMapper;
import cn.com.clubank.weihang.manage.systemSettings.dao.BaseUserRoleMapper;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseDepartment;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseUserRole;
import cn.com.clubank.weihang.manage.user.dao.BaseUserMapper;
import cn.com.clubank.weihang.manage.user.pojo.BaseUser;
import cn.com.clubank.weihang.manage.user.service.IBaseUserService;

/**
 * 用户信息表业务逻辑层
 * 
 * @author Liangwl
 *
 */
@Service
public class BaseUserServiceImpl implements IBaseUserService {

	@Resource
	private BaseUserMapper baseUserMapper;
	@Autowired
	private JpushInfoMapper jpushInfoMapper;
	@Autowired
	private BaseDepartmentMapper baseDepartmentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private WorkEvaluateMapper workEvaluateMapper;
	@Autowired
	private BaseUserRoleMapper baseUserRoleMapper;
	@Autowired
	private BaseRoleMapper baseRoleMapper;

	@Override
	public CommonResult gainNameIcon(Integer duty) {
		List<BaseUser> list = baseUserMapper.selectByDuty(duty);
		if (list.size() > 0) {
			return CommonResult.result(ResultCode.SUCC.getValue(), "成功", list);
		}
		return CommonResult.result(ResultCode.FAIL.getValue(), "失败");
	}

	/**
	 * 员工登录
	 */
	@Override
	public CommonResult baseUserLogin(BaseUser baseUser, String phoneId) {
		BaseUser user = baseUserMapper.selectByAccount(baseUser.getAccount());
		if (user == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "账号尚未开通");
		}
		if (user.getPwd().equals(baseUser.getPwd())) {
			String token = UUID.randomUUID().toString();
			jedisClient.set("SESSION:" + token, JSONObject.toJSONString(user));
			// 设置上一次访问时间
			user.setPreviousVisit(user.getLastVisit());
			// 设置最后访问时间
			user.setLastVisit(new Date());
			// 修改访问时间
			baseUserMapper.updateByPrimaryKey(user);
			// 用户关联极光推送需要的唯一注册码
			JpushInfo jpushInfo = jpushInfoMapper.selectByPhoneId(phoneId);
			if (jpushInfo == null) {
				jpushInfo = new JpushInfo();
				jpushInfo.setPhoneId(phoneId);
				jpushInfo.setUserId(user.getUserId());
				jpushInfoMapper.insert(jpushInfo);
			}
			if (StringUtils.isBlank(jpushInfo.getUserId())) {
				jpushInfo.setUserId(user.getUserId());
				jpushInfoMapper.updateByPhoneId(jpushInfo);
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("userId", user.getUserId());
			map.put("token", token);
			map.put("duty", user.getDuty().toString());
			//  获取角色sorts
			map.put("roles", getUserRoleSorts(user.getUserId()));
			return CommonResult.result(ResultCode.SUCC.getValue(), "登陆成功", map);
		} else {
			return CommonResult.result(ResultCode.AUTH_FAIL.getValue(), "密码错误");
		}
	}

	@Override
	public CommonResult userResetPassword(String mobile, String password) {
		BaseUser info = baseUserMapper.selectByMobile(mobile);
		if (info == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "手机号码不存在");
		}
		// 用户新输入的密码覆盖原有密码
		info.setPwd(password);
		// 更新用户信息，重置密码
		baseUserMapper.updateByPrimaryKey(info);
		return CommonResult.result(ResultCode.SUCC.getValue(), "密码重置成功");
	}

	@Override
	public String getBaseUserInfo(String userId) {
		JSONObject json = new JSONObject();
		if (StringUtil.isBlank(userId)) {
			json.put("apiStatus", ResultCode.PARAM_ERROR.getValue());
			json.put("msg", "参数不能为空！");
			return json.toString();
		}
		BaseUser info = baseUserMapper.selectByPrimaryKey(userId);
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "用户不存在！");
			return json.toString();
		}
		info.setPwd(null); // 密码不返回

		JSONObject user = JSON.parseObject(JSON.toJSONString(info));
		BaseDepartment dep = baseDepartmentMapper.selectByPrimaryKey(info.getDepartmentId());

		Map<String, String> map = workEvaluateMapper.selectConsultantEvaluateTotalAndCount(userId);
		int total = map.containsKey("total") ? Integer.parseInt(map.get("total")) : 0;// 评价总数
		int sum = map.containsKey("sum") ? Integer.parseInt(map.get("sum")) : 0;// 评价总和

		user.put("evaluation", total == 0 ? 0 : new DecimalFormat("0.0").format((float) sum / total));// 客户评价

		user.put("dName", dep == null ? Constants.NULL : dep.getdName()); // 部门
		json.put("data", user);
		json.put("apiStatus", ResultCode.SUCC.getValue());
		json.put("msg", "成功");
		return json.toString();
	}

	@Override
	public CommonResult baseUserClientLogin(String account, String pwd) {
		BaseUser user = baseUserMapper.selectByAccount(account);
		if (user == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "账号尚未开通");
		}
		// pc端登录需要加密
		String password = DESUtils.weihEncode(pwd);
		if (user.getPwd().equals(password)) {
			String token = UUID.randomUUID().toString();
			jedisClient.set("SESSION:" + token, JSONObject.toJSONString(user));
			// 设置上一次访问时间
			user.setPreviousVisit(user.getLastVisit());
			// 设置最后访问时间
			user.setLastVisit(new Date());
			// 修改访问时间
			baseUserMapper.updateByPrimaryKey(user);

			Map<String, String> map = new HashMap<String, String>();
			map.put("userId", user.getUserId());
			map.put("token", token);
			map.put("duty", user.getDuty().toString());
			//  获取角色sorts
			map.put("roles", getUserRoleSorts(user.getUserId()));
			return CommonResult.result(ResultCode.SUCC.getValue(), "登陆成功", map);
		} else {
			return CommonResult.result(ResultCode.AUTH_FAIL.getValue(), "密码错误");
		}
	}

	/**
	 * 后台：根据帐号（模糊）、姓名（模糊）、部门主键、状态分页查询列表
	 */
	@Override
	public CommonResult clientFindBaseUserList(String account, String realname, String departmentId, Integer userStatus,
			Integer pageIndex, Integer pageSize) {
		PageObject<Map<String, String>> page = new PageObject<>(pageIndex, pageSize);
		int total = baseUserMapper.selectBaseUserCount(account, realname, departmentId, userStatus);
		page.setRows(total);
		List<Map<String, String>> list = baseUserMapper.selectBaseUserList(account, realname, departmentId, userStatus,
				page.getStart(), page.getPageSize());
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}

	/**
	 * 后台：根据用户主键查询用户信息
	 */
	@Override
	public CommonResult clientFindBaseUserInfoByUserId(String userId) {
		Map<String, String> userInfo = baseUserMapper.selectUserInfoByPrimaryKey(userId);
		List<Map<String, String>> roleList = baseUserMapper.selectUserRoleByUserId(userId);
		Map<String, Object> map = new HashMap<>();
		map.put("userInfo", userInfo);
		map.put("roleList", roleList);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", map);
	}

	/**
	 * 新增编辑用户
	 */
	@Override
	public CommonResult clientModifyBaseUser(BaseUser baseUser) {
		if (baseUser == null) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数错误");
		}
		String msg = "";
		if (StringUtils.isBlank(baseUser.getUserId())) {
			BaseUser user = baseUserMapper.selectByAccount(baseUser.getAccount());
			if (user != null) {
				return CommonResult.result(ResultCode.DATA_EXIST.getValue(), "账号已存在");
			}
			baseUserMapper.insert(baseUser);
			msg = "新增成功";
		} else {
			baseUserMapper.updateSelectiveByPrimaryKey(baseUser);
			msg = "修改成功";
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), msg);
	}

	/**
	 * 删除用户
	 */
	@Override
	public CommonResult clientDeleteBaseUser(String userId) {
		baseUserMapper.deleteByPrimaryKey(userId);
		baseUserRoleMapper.deleteByUserId(userId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "删除成功");
	}

	/**
	 * 设置用户状态
	 */
	@Override
	public CommonResult clientSetBaseUserStatus(String userId, Integer userStatus) {
		BaseUser user = baseUserMapper.selectByPrimaryKey(userId);
		user.setUserStatus(userStatus);
		baseUserMapper.updateSelectiveByPrimaryKey(user);
		return CommonResult.result(ResultCode.SUCC.getValue(), "状态设置成功");
	}

	/**
	 * 分配角色
	 */
	@Override
	public CommonResult clientSetBaseUserRole(JSONArray array) {
		if (array == null || array.size() <= 0) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数为空");
		}
		List<BaseUserRole> userRoleList = JSON.parseArray(array.toJSONString(), BaseUserRole.class);
		baseUserRoleMapper.deleteByUserId(userRoleList.get(0).getUserId());
		for (BaseUserRole baseUserRole : userRoleList) {
			baseUserRoleMapper.insert(baseUserRole);
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "分配成功");
	}
	
	/**
	 * 
	 * @return
	 */
	private String getUserRoleSorts(String userId) {
		String sorts = "";
		List<Map<String, Object>> list = baseRoleMapper.selectRoleUserByUserId(userId);
		for (Map<String, Object> map : list) {
			sorts += map.get("sort") + ",";
		}
		return sorts.length() > 0 ? sorts.substring(0, sorts.length() - 1) : "";
	}
}
