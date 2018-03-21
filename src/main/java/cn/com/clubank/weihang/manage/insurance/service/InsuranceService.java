package cn.com.clubank.weihang.manage.insurance.service;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.insurance.pojo.InsurancePic;

/**
 * 保险管理
 * 
 * @author LeiQY
 *
 */
public interface InsuranceService {

	/**
	 * 添加保险信息
	 * 
	 * @param json
	 * @return
	 */
	CommonResult memberAddInsuranceOrderInfo(JSONObject json);

	/**
	 * 查询保险订单列表
	 * 
	 * @param customerId
	 * @param status
	 * @return
	 */
	CommonResult memberFindInsuranceOrderList(String customerId, Integer status, Integer pageIndex, Integer pageSize);

	/**
	 * 保险订单取消
	 * 
	 * @param insuranceId
	 * @return
	 */
	CommonResult memberCancelInsuranceOrder(String insuranceId);

	/**
	 * 保险订单确认收货
	 * 
	 * @param insuranceId
	 * @return
	 */
	CommonResult memberConfirmReceiptInsuranceOrder(String insuranceId);

	/**
	 * 查询保险订单详情
	 * 
	 * @param insuranceId
	 * @return
	 */
	CommonResult memberFindInsuranceOrderDetail(String insuranceId);

	/**
	 * 添加保险图片
	 * 
	 * @param array
	 * @return
	 */
	CommonResult memberAddInsurancePicList(JSONArray array);

	/**
	 * 修改保险照片
	 * 
	 * @param pic
	 * @return
	 */
	CommonResult memberModifyInsurancePic(InsurancePic pic);

	/**
	 * 查看保单相关图片
	 * 
	 * @param insuranceId
	 * @param type
	 * @return
	 */
	CommonResult memberFindInsurancePicByType(String insuranceId, Integer type);

	/**
	 * 后台 ：查询保单列表
	 * 
	 * @param subTimeStart
	 * @param subTimeEnd
	 * @param companyName
	 * @param status
	 * @param subName
	 * @param carNo
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	CommonResult clinetFindInsuranceOrderList(String subTimeStart, String subTimeEnd, String companyName,
			Integer status, String subName, String carNo, Integer pageIndex, Integer pageSize);

	/**
	 * 后台：保险订单验车
	 * 
	 * @param insuranceId
	 * @return
	 */
	CommonResult clinetInsuranceOrderCheckCar(String insuranceId);

	/**
	 * 后台：保险订单驳回
	 * 
	 * @param insuranceId
	 * @param description
	 * @return
	 */
	CommonResult clinetRejectInsuranceOrder(String insuranceId, String description);

	/**
	 * 后台：添加保单图片
	 * 
	 * @param pic
	 * @return
	 */
	CommonResult clinetAddInsuranceOrderPic(InsurancePic pic);

	/**
	 * 后台：编辑保单价格并确认
	 * 
	 * @param insuranceId
	 * @param amount1
	 * @param amount2
	 * @param amount3
	 * @return
	 */
	CommonResult clinetModifyInsuranceOrderPrice(String insuranceId, BigDecimal amount1, BigDecimal amount2,
			BigDecimal amount3);

	/**
	 * 后台：发货
	 * 
	 * @param insuranceId
	 * @return
	 */
	CommonResult clinetDeliveryInsuranceOrder(String insuranceId);

	/**
	 * 定时任务：处理保单状态(待付款状态三天内未支付，订单状态修改为已取消，已发货保单十天内未确认，订单状态变更为已完成)
	 */
	void handleInsuranceOrderStatus();
}
