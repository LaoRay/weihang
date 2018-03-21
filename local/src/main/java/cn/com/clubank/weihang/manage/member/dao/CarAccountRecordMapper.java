package cn.com.clubank.weihang.manage.member.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.member.pojo.CarAccountRecord;

public interface CarAccountRecordMapper {
	
	int deleteByPrimaryKey(String recordId);

	int insert(CarAccountRecord record);

	CarAccountRecord selectByPrimaryKey(String recordId);

	List<CarAccountRecord> selectAll();

	int updateByPrimaryKey(CarAccountRecord record);

	List<CarAccountRecord> selectRecordListByCarId(@Param("carId") String carId, @Param("start") Integer start,
			@Param("pageSize") Integer pageSize);
	
	/**
	 * 根据所用车辆id查询减少（消费）的支付记录
	 * @param carId
	 * @return
	 */
	List<Map<String, Object>> selectReduceByUserCar(@Param("groupId") String groupId, @Param("start") Integer start,
			@Param("pageSize") Integer pageSize);
}