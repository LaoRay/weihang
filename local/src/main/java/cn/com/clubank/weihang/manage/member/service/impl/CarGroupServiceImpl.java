package cn.com.clubank.weihang.manage.member.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.member.dao.CarAccountRecordMapper;
import cn.com.clubank.weihang.manage.member.dao.CarGroupMapper;
import cn.com.clubank.weihang.manage.member.dao.CarInfoMapper;
import cn.com.clubank.weihang.manage.member.pojo.CarGroup;
import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.member.service.CarGroupService;

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

	@Override
	public CommonResult saveOrUpdateGroup(CarGroup info) {
		if (StringUtil.isBlank(info.getAccount()) || StringUtil.isBlank(info.getPassword()) || StringUtil.isBlank(info.getGroupName())) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数不能为空");
		}
		CarGroup group = carGroupMapper.selectByAccount(info.getAccount());
		if (group != null && !group.getGroupId().equals(info.getGroupId())) {
			return new CommonResult(ResultCode.DATA_EXIST.getValue(), "帐号已存在");
		}
		
		CarGroup record = carGroupMapper.selectByPrimaryKey(info.getGroupId());
		if (record != null) {
			//更新操作
			record.setAccount(info.getAccount());
			record.setPassword(info.getPassword());
			record.setGroupName(info.getGroupName());
			record.setGroupPic(info.getGroupPic());
			carGroupMapper.updateByPrimaryKey(record);
		} else {
			//新增时初始化一辆集团组的虚拟车辆
			CarInfo car = new CarInfo();
			car.setCarNo(StringUtil.getVirtualCarNo()); //TODO 判断是否存在虚拟车牌号
			car.setIsDefault(true);
			car.setAccount(new BigDecimal(Constants.INT_0));
			car.setIsVirtual(true);
			carInfoMapper.insert(car);
			
			info.setGroupCar(car.getCarId());
			carGroupMapper.insert(info);
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "操作成功");
	}

	@Override
	public String findPage(String groupName, Integer pageIndex, Integer pageSize) {
		PageObject<CarGroup> page = new PageObject<CarGroup>(pageIndex, pageSize);

		int total = carGroupMapper.findPageTotal(groupName);
		List<CarGroup> list = carGroupMapper.findPage(groupName, page.getStart(), pageSize);

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
		if (StringUtil.isBlank(groupId)) {
			return new CommonResult(ResultCode.PARAM_ERROR.getValue(), "参数不能为空");
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "查询成功", carAccountRecordMapper
				.selectReduceByUserCar(groupId, PageObject.getStart(pageIndex, pageSize), pageSize));
	}

	@Override
	public CommonResult memberGetGroupInfo(String groupId) {
		Map<String, Object> info = carGroupMapper.selectDetailByPrimaryKey(groupId);
		if (info == null) {
			return new CommonResult(ResultCode.DATA_NOEXIST.getValue(), "集团组信息不存在或参数错误");
		}
		return new CommonResult(ResultCode.SUCC.getValue(), "查询成功", info);
	}

}
