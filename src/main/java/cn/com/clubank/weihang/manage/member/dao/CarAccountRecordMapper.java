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
	
	/**
     * 根据第三方流水号查询-理论上只有一条
     * @param transactionId
     * @return
     */
    List<CarAccountRecord> selectByTransactionId(String transactionId);

	List<CarAccountRecord> selectRecordListByCarId(@Param("carId") String carId, @Param("start") Integer start,
			@Param("pageSize") Integer pageSize);
	
	/**
	 * 根据集团组查询减少（消费）的记录
	 * @param carId
	 * @return
	 */
	List<Map<String, Object>> findGroupReduceRecord(@Param("groupId") String groupId, @Param("start") Integer start,
			@Param("pageSize") Integer pageSize);
	
	/**
	 * 根据集团组查询减少（消费）的记录
	 * @param carId
	 * @return
	 */
	List<CarAccountRecord> clientFindGroupReduceRecord(@Param("groupId") String groupId, @Param("start") Integer start,
			@Param("pageSize") Integer pageSize);
	
	/**
	 * 根据集团组查询减少（消费）的记录总数
	 * @param carId
	 * @return
	 */
	int clientFindGroupReduceRecordCount(@Param("groupId") String groupId);
	
	/**
	 * 查询集团组充值记录
	 * @param carId
	 * @return
	 */
	List<Map<String, Object>> findGroupRechargeRecord(@Param("groupId") String groupId, @Param("start") Integer start,
			@Param("pageSize") Integer pageSize);
}