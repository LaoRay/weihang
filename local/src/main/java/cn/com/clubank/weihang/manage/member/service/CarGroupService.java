package cn.com.clubank.weihang.manage.member.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.member.pojo.CarGroup;

/**
 * 集团组业务处理
 * 
 * @author YangWei
 *
 */
public interface CarGroupService {

	/**
	 * 保存、更新
	 * @param info
	 * @return
	 */
	CommonResult saveOrUpdateGroup(CarGroup info);
	
	/**
	 * 后台-分页查询
	 * @param groupName
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	String findPage(String groupName, Integer pageIndex, Integer pageSize);
	
	/**
	 * 添加集团组车辆
	 * @param groupId
	 * @param carIds
	 * @return
	 */
	CommonResult addGroupCars(String groupId, String carIds);
	
	/**
	 * 获取集团组详情
	 * @param groupId
	 * @return
	 */
	CommonResult memberGetGroupInfo(String groupId);
	
	/**
	 * 删除集团组车辆
	 * @param groupId
	 * @param carIds
	 * @return
	 */
	CommonResult deleteGroupCars(String carIds);
	
	/**
	 * 查询集团组车辆列表
	 * @param groupId
	 * @return
	 */
	CommonResult findGroupCarList(String groupId);
	
	/**
	 * 查询集团组帐号消费记录
	 * @param groupId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	CommonResult findGroupAccountRecord(String groupId, Integer pageIndex, Integer pageSize);
	
}
