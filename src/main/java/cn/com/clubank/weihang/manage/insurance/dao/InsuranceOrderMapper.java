package cn.com.clubank.weihang.manage.insurance.dao;

import cn.com.clubank.weihang.manage.insurance.pojo.InsuranceOrder;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface InsuranceOrderMapper {
	int deleteByPrimaryKey(String insuranceId);

	int insert(InsuranceOrder record);

	InsuranceOrder selectByPrimaryKey(String insuranceId);

	InsuranceOrder selectByNo(String insuranceNo);

	List<InsuranceOrder> selectAll();

	int updateByPrimaryKey(InsuranceOrder record);

	int updateSelectiveByPrimaryKey(InsuranceOrder record);

	int selectInsuranceOrderCount(@Param("customerId") String customerId, @Param("status") Integer status);

	List<InsuranceOrder> selectInsuranceOrderList(@Param("customerId") String customerId,
			@Param("status") Integer status, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

	int clientSelectInsuranceOrderCount(@Param("subTimeStart") String subTimeStart,
			@Param("subTimeEnd") String subTimeEnd, @Param("companyName") String companyName,
			@Param("status") Integer status, @Param("subName") String subName, @Param("carNo") String carNo);

	List<InsuranceOrder> clientSelectInsuranceOrderList(@Param("subTimeStart") String subTimeStart,
			@Param("subTimeEnd") String subTimeEnd, @Param("companyName") String companyName,
			@Param("status") Integer status, @Param("subName") String subName, @Param("carNo") String carNo,
			@Param("start") Integer start, @Param("pageSize") Integer pageSize);

	void handleDelayedPayOrderStatus();

	void handleUnconfirmedReceiptOrderStatus();
}