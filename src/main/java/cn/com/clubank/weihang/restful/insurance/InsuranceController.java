package cn.com.clubank.weihang.restful.insurance;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.DataItemCode;
import cn.com.clubank.weihang.manage.dataItem.service.IDataItemDetailService;
import cn.com.clubank.weihang.manage.insurance.pojo.InsurancePic;
import cn.com.clubank.weihang.manage.insurance.service.InsuranceService;

/**
 * 保险管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class InsuranceController {

	@Autowired
	private InsuranceService insuranceService;
	@Resource
	private IDataItemDetailService iDataItemDetailService;

	/**
	 * 获取固定投保项
	 * 
	 * @return
	 */
	@RequestMapping(value = "/insuranceGainFixedInsuranceItem", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult gainFixedInsuranceItem() {
		return iDataItemDetailService.gainNameValue(DataItemCode.FIXED_INSURANCE_ITEM.getValue());
	}

	/**
	 * 获取保险公司列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/insuranceGainInsuranceCompanyList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult gainInsuranceCompany() {
		return iDataItemDetailService.gainNameValue(DataItemCode.INSURANCE_COMPANY.getValue());
	}

	/**
	 * 添加保险信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/memberAddInsuranceOrderInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberAddInsuranceOrderInfo(@RequestBody JSONObject json) {
		return insuranceService.memberAddInsuranceOrderInfo(json);
	}

	/**
	 * 查询保险订单列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/memberFindInsuranceOrderList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberFindInsuranceOrderList(@RequestBody JSONObject json) {
		return insuranceService.memberFindInsuranceOrderList(json.getString("customerId"), json.getInteger("status"),
				json.getInteger("pageIndex"), json.getInteger("pageSize"));
	}

	/**
	 * 保险订单取消
	 * 
	 * @return
	 */
	@RequestMapping(value = "/memberCancelInsuranceOrder", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberCancelInsuranceOrder(@RequestBody JSONObject json) {
		return insuranceService.memberCancelInsuranceOrder(json.getString("insuranceId"));
	}

	/**
	 * 保险订单确认收货
	 * 
	 * @return
	 */
	@RequestMapping(value = "/memberConfirmReceiptInsuranceOrder", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberConfirmReceiptInsuranceOrder(@RequestBody JSONObject json) {
		return insuranceService.memberConfirmReceiptInsuranceOrder(json.getString("insuranceId"));
	}

	/**
	 * 查询保险订单详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "/memberFindInsuranceOrderDetail", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberFindInsuranceOrderDetail(@RequestBody JSONObject json) {
		return insuranceService.memberFindInsuranceOrderDetail(json.getString("insuranceId"));
	}

	/**
	 * 添加保险图片
	 * 
	 * @return
	 */
	@RequestMapping(value = "/memberAddInsurancePicList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberAddInsurancePicList(@RequestBody JSONArray array) {
		return insuranceService.memberAddInsurancePicList(array);
	}

	/**
	 * 修改保险图片
	 * 
	 * @return
	 */
	@RequestMapping(value = "/memberModifyInsurancePic", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberModifyInsurancePic(@RequestBody InsurancePic pic) {
		return insuranceService.memberModifyInsurancePic(pic);
	}

	/**
	 * 查看保单相关图片 type 1-身份证正面 2-身份证反面 3-行驶证 4-车辆照片 5-保单照片
	 * 
	 * @return
	 */
	@RequestMapping(value = "/memberFindInsurancePicByType", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberFindInsurancePicByType(@RequestBody JSONObject param) {
		return insuranceService.memberFindInsurancePicByType(param.getString("insuranceId"), param.getInteger("type"));
	}
}
