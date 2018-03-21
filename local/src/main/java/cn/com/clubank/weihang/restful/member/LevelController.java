package cn.com.clubank.weihang.restful.member;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.member.pojo.AccountBenefit;
import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.member.pojo.CustomerInfo;
import cn.com.clubank.weihang.manage.member.service.IAccountBenefitService;
import cn.com.clubank.weihang.manage.member.service.ICarAccountRecordService;
import cn.com.clubank.weihang.manage.member.service.ICarInfoService;
import cn.com.clubank.weihang.manage.member.service.ICouponListService;
import cn.com.clubank.weihang.manage.member.service.ICustomerInfoService;

/**
 * 等级管理
 * @author YangWei
 *
 */
@Controller
public class LevelController {
	
	@Resource
	private ICustomerInfoService iCustomerInfoService;
	
	@Resource
	private ICarInfoService iCarInfoService;
	
	@Resource
	private ICouponListService icouponService;
	
	@Resource
	private ICarAccountRecordService iCarAccountRecordService;
	
	@Resource
	private IAccountBenefitService iAccountBenefitService;
	
	/**
	 * 获取会员卡现存金额、优惠券、积分、会员等级
	 * 
	 * @param carId 车辆id
	 * @return
	 */
	@RequestMapping(value="/memberFindMoneyInfo", method=RequestMethod.POST)
	@ResponseBody
	public String getMoneyInfo(@RequestBody JSONObject param) {
 		JSONObject json = new JSONObject();
		//获取会员信息对象（积分、等级）
		CarInfo info = iCarInfoService.selectByPrimaryKey(param.getString("carId"));
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "会员信息不存在");
			return json.toString();
		}
		//客户信息
		CustomerInfo cus = iCustomerInfoService.selectByPrimaryKey(info.getCustomerId());
		
		//账户等级信息
		AccountBenefit abInfo = iAccountBenefitService.selectByPrimaryKey(info.getAbId());
		
		json.put("grade", abInfo == null ? "" : abInfo.getBenefitName()); // 账户等级(查询)
		json.put("integralAccount", cus.getIntegralAccount() == null ? 0 : cus.getIntegralAccount()); // 积分
		json.put("acount", info.getAccount()); // 账户金额
		json.put("coupons", icouponService.selectByCustomerId(cus.getCId())); // 优惠券
		
		json.put("apiStatus", ResultCode.SUCC.getValue());
		return json.toString();
	}

	/**
	 * 获取会员级别列表及其说明数据
	 * 
	 * @param memberNO 会员编号
	 * @return
	 */
	@RequestMapping(value="/memberFindAccountBenefitList", method=RequestMethod.POST)
	@ResponseBody
	public String getAccountBenefitList() {
		JSONObject json = new JSONObject();
		json.put("apiStatus", ResultCode.SUCC.getValue());
		// 级别的定义说明获取
		json.put("data", iAccountBenefitService.selectAll());
		return json.toString();
	}
	
	/**
	 * 获取会员级别说明数据
	 * 
	 * @param ab_id 账户权益id
	 * @return
	 */
	@RequestMapping(value="/memberFindAccountBenefitInfo", method=RequestMethod.POST)
	@ResponseBody
	public String getAccountBenefit(@RequestBody JSONObject param) {
		JSONObject json = new JSONObject();
		AccountBenefit info = iAccountBenefitService.selectByPrimaryKey(param.getString("abId"));
		if (info == null) {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "数据不存在！");
		} else {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			// 级别的定义说明获取
			json.put("data", iAccountBenefitService.selectByPrimaryKey(param.getString("abId")));
		}
		return json.toString();
	}

}
