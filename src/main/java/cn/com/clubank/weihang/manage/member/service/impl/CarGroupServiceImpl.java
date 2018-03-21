package cn.com.clubank.weihang.manage.member.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.DESUtils;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.member.dao.CarAccountRecordMapper;
import cn.com.clubank.weihang.manage.member.dao.CarGroupMapper;
import cn.com.clubank.weihang.manage.member.dao.CarInfoMapper;
import cn.com.clubank.weihang.manage.member.pojo.CarAccountRecord;
import cn.com.clubank.weihang.manage.member.pojo.CarGroup;
import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.member.service.CarGroupService;
import cn.com.clubank.weihang.manage.pay.service.WeihPayService;

/**
 * 集团组业务处理
 * 
 * @author YangWei
 *
 */
@Service
public class CarGroupServiceImpl implements CarGroupService {

	@Resource
	private CarInfoMapper carInfoMapper;

	@Resource
	private CarGroupMapper carGroupMapper;

	@Resource
	private CarAccountRecordMapper carAccountRecordMapper;
	
	@Resource
	private WeihPayService weihPayService;

	@Override
	public CommonResult saveOrUpdateGroup(CarGroup info) {
		if (StringUtil.isBlank(info.getAccount()) || StringUtil.isBlank(info.getGroupName())) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数不能为空");
		}
		/*if (StringUtil.isBlank(info.getGroupCar())) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "请选择关联账户车辆");
		}*/
		CarGroup group = carGroupMapper.selectByAccount(info.getAccount());
		if (group != null && !group.getGroupId().equals(info.getGroupId())) {
			return new CommonResult(ResultCode.DATA_EXIST.getValue(), "帐号已存在");
		}
		/*CarInfo car = carInfoMapper.selectByPrimaryKey(info.getGroupCar());
		if (car == null) {
			return new CommonResult(ResultCode.DATA_NOEXIST.getValue(), "帐号车辆数据不存在");
		}*/
		List<CarGroup> cars = carGroupMapper.selectByGroupCar(info.getGroupCar());
		// 判断账户车辆的唯一性
		if (cars.size() > 0 && !cars.get(0).getGroupId().equals(info.getGroupId())) {
			return new CommonResult(ResultCode.DATA_EXIST.getValue(), "此账户车辆已被其他企业用户绑定，请重新选择！");
		}
		//密码加密
		info.setPassword(DESUtils.weihEncode(info.getPassword()));
		if (StringUtil.isNotBlank(info.getGroupId())) {
			carGroupMapper.updateByPrimaryKey(info);
			//先删除集团组下的公车
			List<CarInfo> publicCars = carInfoMapper.selectByGroupId(info.getGroupId());
			for (CarInfo publicCar : publicCars) {
				//删除集团组车辆-将车辆表的group_id更新成null
				carInfoMapper.updateGroupByPrimaryKey(publicCar.getCarId(), null);
			}
		} else {
			carGroupMapper.insert(info);
		}
		//更新公车数据
		if (StringUtil.isNotBlank(info.getPublicCarIds())) {
			for (String carId : info.getPublicCarIds().split(",")) {
				CarInfo publicCar = carInfoMapper.selectByPrimaryKey(carId);
				if (publicCar != null) {
					publicCar.setGroupId(info.getGroupId());
					carInfoMapper.updateByPrimaryKey(publicCar);
				}
			}
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "操作成功");
	}

	@Override
	public String findPage(String groupName, Integer pageIndex, Integer pageSize) {
		PageObject<Map<String, Object>> page = new PageObject<Map<String, Object>>(pageIndex, pageSize);

		int total = carGroupMapper.findPageTotal(groupName);
		List<Map<String, Object>> list = carGroupMapper.findPage(groupName, page.getStart(), pageSize);
		for (Map<String, Object> map : list) {
			// 公车数量
			map.put("publicCarNum", carInfoMapper.selectCountByGroupId(map.get("groupId").toString()));
		}
		
		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public CommonResult addGroupCars(String groupId, String carIds) {
		if (StringUtil.isBlank(groupId) || StringUtil.isBlank(carIds)) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数不能为空");
		}
		for (String carId : carIds.split(",")) {
			carInfoMapper.updateGroupByPrimaryKey(carId, groupId);
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "添加成功");
	}

	@Override
	public CommonResult deleteGroupCars(String carIds) {
		if (StringUtil.isBlank(carIds)) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数不能为空");
		}
		for (String carId : carIds.split(",")) {
			carInfoMapper.updateGroupByPrimaryKey(carId, null);
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "删除成功");
	}

	@Override
	public CommonResult findGroupCarList(String groupId) {
		if (StringUtil.isBlank(groupId)) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数不能为空");
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "查询成功", carInfoMapper.selectByGroupId(groupId));
	}

	@Override
	public CommonResult findGroupAccountRecord(String groupId, Integer pageIndex, Integer pageSize) {
		if (StringUtil.isBlank(groupId) || pageIndex == null || pageSize == null) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数不能为空");
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "查询成功", carAccountRecordMapper
				.findGroupReduceRecord(groupId, PageObject.getStart(pageIndex, pageSize), pageSize));
	}

	@Override
	public CommonResult memberGetGroupInfo(String groupId) {
		Map<String, Object> info = carGroupMapper.selectDetailByPrimaryKey(groupId);
		if (info == null) {
			return new CommonResult(ResultCode.DATA_NOEXIST.getValue(), "集团组信息不存在或参数错误");
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "查询成功", info);
	}

	@Override
	public CommonResult memberFindGroupRechargeRecord(String groupId, Integer pageIndex, Integer pageSize) {
		if (StringUtil.isBlank(groupId) || pageIndex == null || pageSize == null) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数不能为空");
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "查询成功", carAccountRecordMapper
				.findGroupRechargeRecord(groupId, PageObject.getStart(pageIndex, pageSize), pageSize));
	}

	@Override
	public CommonResult clientGroupDelete(String groupId) {
		CarGroup group = carGroupMapper.selectByPrimaryKey(groupId);
		if (group == null) {
			return new CommonResult(ResultCode.DATA_NOEXIST.getValue(), "集团组信息不存在");
		}
		CarInfo car = carInfoMapper.selectByPrimaryKey(group.getGroupCar());
		if (car != null && car.getAccount().compareTo(new BigDecimal("0.0")) == -1) {
			return new CommonResult(ResultCode.DATA_NOEXIST.getValue(), "帐号赊账，不能删除");
		}
		carGroupMapper.deleteByPrimaryKey(groupId);
		if (car != null) {
			carInfoMapper.deleteCar(car.getCarId());
		}
		// 删除集团组下的公车
		List<CarInfo> publicCars = carInfoMapper.selectByGroupId(groupId);
		for (CarInfo publicCar : publicCars) {
			//删除集团组车辆-将车辆表的group_id更新成null
			carInfoMapper.updateGroupByPrimaryKey(publicCar.getCarId(), null);
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "删除成功");
	}

	@Override
	public CommonResult clientMemberGetGroupInfo(String groupId) {
		Map<String, Object> info = carGroupMapper.selectDetailByPrimaryKey(groupId);
		if (info == null) {
			return new CommonResult(ResultCode.DATA_NOEXIST.getValue(), "集团组信息不存在或参数错误");
		}
		JSONObject result = new JSONObject();
		//公车列表
		result.put("publicCars", carInfoMapper.selectByGroupId(groupId));
		//密码解密
		info.put("password", DESUtils.weihDecode(info.get("password").toString()));
		result.put("info", info);
		return new CommonResult(ResultCode.SUCC.getValue(), "查询成功", result);
	}

	@Override
	public CommonResult clientGroupRecharge(String groupId, BigDecimal total ,Integer accountPayType, String desc) {
		return weihPayService.handleGroupRecharge(groupId, "weihclientRecharge", total, 1, accountPayType, desc);
	}

	@Override
	public String clientFindGroupAccountRecord(String groupId, Integer pageIndex, Integer pageSize) {
		if (StringUtil.isBlank(groupId) || pageIndex == null || pageSize == null) {
			JSONObject result = new JSONObject();
			result.put("msg", "参数不能为空");
			result.put("apiStatus", ResultCode.SUCC.getValue());
			return result.toString();
		}
		PageObject<CarAccountRecord> page = new PageObject<CarAccountRecord>(pageIndex, pageSize);
		int total = carAccountRecordMapper.clientFindGroupReduceRecordCount(groupId);
		List<CarAccountRecord> list = carAccountRecordMapper.clientFindGroupReduceRecord(groupId, page.getStart(), pageSize);
		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

}
