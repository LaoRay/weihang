package cn.com.clubank.weihang.restful.pay;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.pay.service.WeihPayService;

/**
 * app端：支付接口
 * 
 * @author YangWei
 *
 */
@Controller
public class WeihPayController {
	
	@Resource
	private WeihPayService weihPayService;
	
	/**
	 * app支付签名
	 * 
	 * @param param
	 * @param objId		订单id（维修单no、洗车单no、商品订单no、保险订单no，集团组id）
	 * @param total		支付总金额
	 * @param type		类型：1维修单、2洗车单、3商品、4会员充值、5洗车打赏、6集团组充值 、7保险订单支付、8特殊订单支付
	 * @return
	 */
	@RequestMapping(value = "/alipaySignature", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult alipaySignature(HttpServletRequest request, @RequestBody JSONObject param) throws ServletException, IOException {
		return weihPayService.alipayAppSignature(param.getString("objId"), param.getString("total"), request.getHeader("customerId"), param.getInteger("type"));
	}
	
	/**
	 * 微信支付签名
	 * 
	 * @param param
	 * @param objId		订单id（维修单no、洗车单no、商品订单no、保险订单no，集团组id）
	 * @param total		支付总金额
	 * @param type		类型：1维修单、2洗车单、3商品、4会员充值、5洗车打赏、6集团组充值 、7保险订单支付、8特殊订单支付
	 * @return
	 */
	@RequestMapping(value = "/weixinpaySignature", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult weixinpaySignature(HttpServletRequest request, @RequestBody JSONObject param) throws ServletException, IOException {
		return weihPayService.weixinpaySignature(param.getString("objId"), param.getString("total"), request.getHeader("customerId"), request.getRemoteAddr(), param.getInteger("type"));
	}
	
	/**
	 * 支付宝支付成功-回调
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/alipayNotify", produces = "application/json;charset=utf-8")
	@ResponseBody
	public void notify(HttpServletRequest request) {
		weihPayService.alipayNotify(request);
	}
	
	/**
	 * 微信支付成功-回调
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/weixinNotify", produces = "application/json;charset=utf-8")
	@ResponseBody
	public void weixinNotify(HttpServletRequest request) {
		weihPayService.weixinNotify(request);
	}
	
	/**
	 * 会员帐号支付
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/memberAccountPay", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult memberAccountPay(HttpServletRequest request, @RequestBody JSONObject param) {
		Integer flatType = request.getIntHeader("flatType");
		String payPassword = param.getString("payPassword");
		return weihPayService.memberAccountPay(param.getString("objNo"), param.getBigDecimal("total"), request.getHeader("customerId"), param.getInteger("type"), param.getInteger("isGroup"), flatType, payPassword);
	}

	/**
	 * 支付宝网站支付
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/alipayWebsite", method = RequestMethod.GET)
	public void clientAliPay(HttpServletRequest request, HttpServletResponse httpResponse, String customerId, String objId, String total, Integer type) throws ServletException, IOException {
		String cid = StringUtil.isBlank(request.getHeader("customerId")) ? customerId : request.getHeader("customerId");
		httpResponse.setContentType("text/html;charset=utf-8");
		httpResponse.getWriter().write(weihPayService.alipayWebsiteSignature(objId, total, cid, type));//直接将完整的表单html输出到页面
		httpResponse.getWriter().flush();
		httpResponse.getWriter().close();
	}
	
	/**
	 * 支付宝网站支付
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/alipayReturn", method = RequestMethod.GET)
	public String alipayReturn(HttpServletRequest request, HttpServletResponse httpResponse) throws ServletException, IOException {
		/*httpResponse.setContentType("text/html;charset=utf-8");
		httpResponse.getWriter().write("支付宝支付成功!!");//直接将完整的表单html输出到页面
		httpResponse.getWriter().flush();
		httpResponse.getWriter().close();*/
		return "alipaySucess";
	}
	
}
