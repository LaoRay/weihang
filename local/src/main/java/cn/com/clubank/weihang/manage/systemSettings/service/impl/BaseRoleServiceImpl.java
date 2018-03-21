package cn.com.clubank.weihang.manage.systemSettings.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.systemSettings.dao.BaseRoleMapper;
import cn.com.clubank.weihang.manage.systemSettings.dao.BaseRoleModuleButtonMapper;
import cn.com.clubank.weihang.manage.systemSettings.dao.BaseUserRoleMapper;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseRole;
import cn.com.clubank.weihang.manage.systemSettings.service.IBaseRoleService;
import cn.com.clubank.weihang.manage.user.dao.BaseUserMapper;

@Service
public class BaseRoleServiceImpl implements IBaseRoleService {

	@Resource
	private BaseRoleMapper baseRoleMapper;
	@Resource
	private BaseUserRoleMapper baseUserRoleMapper;
	@Resource
	private BaseRoleModuleButtonMapper baseRoleModuleButtonMapper;
	@Autowired
	private BaseUserMapper baseUserMapper;

	@Override
	public String selectRoleListByNameAndPaged(String roleName, Integer pageIndex, Integer pageSize) {
		PageObject<BaseRole> page = new PageObject<>(pageIndex, pageSize);

		int total = baseRoleMapper.selectCountByName(roleName);
		List<BaseRole> list = baseRoleMapper.selectRoleListAndPaged(roleName, page.getStart(), pageSize);
		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String insertOrUpdateRole(BaseRole role) {
		JSONObject json = new JSONObject();

		if (StringUtils.isBlank(role.getRoleId())) {// 角色主键为空执行新增
			if (baseRoleMapper.insert(role) > Constants.INT_0) {
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "新增成功");
			} else {
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "新增失败");
			}
		} else {
			if (baseRoleMapper.updateByPrimaryKeySelective(role) > Constants.INT_0) {
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "编辑成功");
			} else {
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "编辑失败");
			}
		}
		return json.toString();
	}

	@Override
	public String selectRoleDetail(String roleId) {
		JSONObject json = new JSONObject();

		BaseRole role = baseRoleMapper.selectByPrimaryKey(roleId);
		if (role != null) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", role);
		} else {
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", role);
		}
		return json.toString();
	}

	@Override
	public String selectRoleUser(String roleId) {
		JSONObject json = new JSONObject();

		List<Map<String, Object>> list = baseRoleMapper.selectRoleUserByRoleId(roleId);
		if (!list.isEmpty()) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", list);
		} else {
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", list);
		}
		return json.toString();
	}

	/**
	 * 获取角色列表
	 */
	@Override
	public CommonResult clientFindRoleList(String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<BaseRole> roleList = baseRoleMapper.selectRoleList();
		map.put("roleList", roleList);
		List<Map<String, String>> userRoleList = baseUserMapper.selectUserRoleByUserId(userId);
		map.put("userRoleList", userRoleList);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", map);
	}

	@Override
	public String deleteRole(String roleId) {
		JSONObject json = new JSONObject();

		// 删除角色
		baseRoleMapper.deleteByPrimaryKey(roleId);
		// 删除人员角色表中数据
		baseUserRoleMapper.deleteByRoleId(roleId);
		// 删除角色模块按钮表中数据
		baseRoleModuleButtonMapper.deleteByRoleId(roleId);

		json.put("apiStatus", ResultCode.SUCC.getValue());
		json.put("msg", "删除成功");

		return json.toString();
	}

}
