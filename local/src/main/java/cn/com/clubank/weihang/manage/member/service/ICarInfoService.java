package cn.com.clubank.weihang.manage.member.service;

import java.math.BigDecimal;
import java.util.List;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.member.pojo.CarInfo;

/**
 * 车辆信息表（业务逻辑层接口）
 * 
 * @author Liangwl
 *
 */
public interface ICarInfoService {
	/**
	 * 通过主键ID删除数据是否成功
	 * 
	 * @param carId
	 * @return
	 */
	boolean deleteByPrimaryKey(String carId);

	/**
	 * 删除会员车辆信息
	 * 
	 * @param carId
	 *            车辆ID
	 * @return
	 */
	public String deleteCar(String carId);

	/**
	 * 添加数据是否成功
	 * 
	 * @param record
	 * @return
	 */
	boolean insert(CarInfo record);

	/**
	 * 通过车牌号查询数据
	 * 
	 * @param carNo
	 *            车牌号
	 * @return
	 */
	public String selectByCarNo(String carNo);

	/**
	 * 通过主键ID查询数据
	 * 
	 * @param carId
	 * @return
	 */
	CarInfo selectByPrimaryKey(String carId);

	/**
	 * 根据会员编号，获取会员车辆信息
	 * 
	 * @param customerId
	 *            客户ID
	 * @return
	 */
	public String selectByCustomerId(String customerId);

	/**
	 * 查询所有数据
	 * 
	 * @return
	 */
	List<CarInfo> selectAll();

	/**
	 * 更新数据是否成功
	 * 
	 * @param record
	 * @return
	 */
	boolean updateByPrimaryKey(CarInfo record);

	/**
	 * 获取会员的账户金额
	 * 
	 * @param customerId
	 * @return
	 */
	BigDecimal getAccount(String customerId);

	/**
	 * 设置默认车辆
	 */
	CommonResult setDefaultCar(CarInfo record);

	/**
	 * 添加车辆
	 * 
	 * @param carNo
	 * @return
	 */
	CommonResult addNewCar(CarInfo car);

	/**
	 * 查询车辆账户流水
	 * 
	 * @param customerId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	CommonResult findCarAccountRecodList(String customerId, Integer pageIndex, Integer pageSize);

	/**
	 * 获得会员车辆列表
	 * 
	 * @param customerId
	 * @return
	 */
	String selectCustomerCarList(String customerId);

	/**
	 * 获得车辆品牌ID、车辆品牌，集团ID、集团名，账户权益ID、权益名
	 * 
	 * @return
	 */
	String selectCarbrandAndGroup();

	/**
	 * 新增或编辑车辆
	 * 
	 * @param car
	 * @return
	 */
	String insertOrUpdateCar(CarInfo car);

	/**
	 * 后台：获得车辆详情
	 * 
	 * @param carId
	 * @return
	 */
	String selectCarDetail(String carId);
}
