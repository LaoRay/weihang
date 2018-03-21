package cn.com.clubank.weihang.manage.member.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.member.pojo.CarInfo;

/**
 * 车辆信息表（数据访问层接口）
 * 
 * @author Liangwl
 *
 */
public interface CarInfoMapper {

	int deleteByPrimaryKey(String carId);

	int deleteCar(String carId);

	int insert(CarInfo record);

	/**
	 * 通过车牌号查询车辆信息
	 * 
	 * @param carNo
	 * @return
	 */
	CarInfo selectByCarNo(String carNo);

	CarInfo selectByPrimaryKey(String carId);

	/**
	 * 通过客户关联ID查询车辆信息(一个客户可能多辆车)
	 * 
	 * @param customerId
	 * @return
	 */
	List<CarInfo> selectByCustomerId(String customerId);
	
	/**
	 * 通过集团组查询车辆信息列表
	 * 
	 * @param customerId
	 * @return
	 */
	List<Map<String, Object>> selectByGroupId(String groupId);

	List<CarInfo> selectAll();

	int updateByPrimaryKey(CarInfo record);
	
	int updateSelectiveByPrimaryKey(CarInfo record);

	/**
	 * 获取会员的账户金额
	 * 
	 * @param customerId
	 * @return
	 */
	BigDecimal getAccount(String customerId);

	/**
	 * 更新用户默认车辆
	 * 
	 * @param record
	 * @return
	 */
	int updateIsDefault(CarInfo record);
	
	/**
	 * 根据id更新集团组字段
	 * @param carId
	 * @return
	 */
	int updateGroupByPrimaryKey(@Param("carId") String carId, @Param("value") String value);

	/**
	 * 根据客户id查询默认车辆信息
	 * 
	 * @param customerId
	 * @return
	 */
	CarInfo selectDefaultCarByCustomerId(String customerId);
	
	/**
	 * 通过会员ID获得会员车辆列表
	 * @param customerId 会员ID
	 * @return
	 */
	List<Map<String,Object>> selectCustomerCarList(String customerId);
	
	/**
	 * 获得车辆详情
	 * @param carId
	 * @return
	 */
	Map<String,Object> selectCarDetailByCarId(String carId);
}