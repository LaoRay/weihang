package cn.com.clubank.weihang.common.weihpay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 支付宝支付util
 * @author YangWei
 *
 */
@Slf4j
public class WeihAlipayUtil {

	protected static WeihAlipayUtil instance = null;
	
	private AlipayClient alipayClient = null;
	
	private String appId; //支付宝分配给开发者的应用ID
	
	private String appPrivateKey; //开发者应用私钥，由开发者自己生成
	
	private String alipayPublicKey; //支付宝公钥，由支付宝生成
	
	private String signType = "RSA2"; //商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
	
	private String charset = "UTF-8"; //请求和签名使用的字符编码格式，支持GBK和UTF-8
	
	private String notifyUrl;  // 支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https
	
	private String returnUrl; // 网站支付成功之后的跳转地址
	
	/**
	 * app签名
	 * @param outTradeNo
	 * @param subject
	 * @param totalAmount
	 * @param body
	 * @param passbackParams
	 * @return
	 */
	public String sdkExecute(String outTradeNo, String subject, String totalAmount, String passbackParams) {
		String params = "";
		try {
			params = URLEncoder.encode(passbackParams, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			log.info("公共参数encode出错：" + passbackParams);
			e1.printStackTrace();
		}
		
		AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();// 创建API对应的request
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setPassbackParams(params);
		model.setSubject(subject); // 商品标题
		model.setOutTradeNo(outTradeNo); // 商家订单编号
		model.setTotalAmount(totalAmount); // 订单总金额
		model.setProductCode("QUICK_MSECURITY_PAY"); // 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
		alipayRequest.setBizModel(model);
		alipayRequest.setNotifyUrl(notifyUrl); // 回调地址

		String orderStr = "";
		try {
			// 这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeAppPayResponse response = getalipayClient().sdkExecute(alipayRequest);
			orderStr = response.getBody();
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return null;
		}
		return orderStr;
	}
	
	/**
	 * 支付宝退款
	 * @param outTradeNo
	 * @param subject
	 * @param totalAmount
	 * @param body
	 * @param passbackParams
	 * @param outRequestNo		退款的唯一标识码
	 * @return
	 */
	public boolean refund(String outTradeNo, String refundTotal, String passbackParams, String outRequestNo) {
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizContent("{" +
			/*"\"out_trade_no\":\""+orderNo+"\"," +*/
			"\"trade_no\":\""+outTradeNo+"\"," +
			"\"refund_amount\":"+refundTotal+"," +
			"\"refund_reason\":\""+passbackParams+"\"," +
			"\"out_request_no\":\""+outRequestNo+"\"," +
			"\"operator_id\":\"OP001\"," +
			"\"store_id\":\"NJ_S_001\"," +
			"\"terminal_id\":\"NJ_T_001\"" +
			"  }");
		try {
			AlipayTradeRefundResponse response = getalipayClient().execute(request);
			log.info("支付宝退款结果：" + response.getBody());
			if(response.isSuccess()){
				log.info("支付宝退款调用成功");
				return true;
			} else {
				log.info("支付宝退款调用失败");
				return false;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 查询订单信息
	 * @param outTradeNo
	 * @return
	 */
	public String query(String outTradeNo) {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizContent("{" +
				/*"\"out_trade_no\":\""+orderNo+"\"," +*/
				"\"trade_no\":\""+outTradeNo+"\"" +
				"  }");
		try {
			AlipayTradeQueryResponse response = getalipayClient().execute(request);
			log.info("查询结果：" + response.getBody());
			if(response.isSuccess()){
				log.info("调用成功");
			} else {
				log.info("调用失败");
			}
			return response.getBody();
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 网页支付
	 * @param outTradeNo
	 * @param subject
	 * @param totalAmount
	 * @return form		html标签字符串
	 */
	public String pageExecute(String outTradeNo, String subject, String totalAmount, String passbackParams) {
		return pageExecute(outTradeNo, subject, totalAmount, passbackParams, "");
	}
	
	/**
	 * 网页支付
	 * @param outTradeNo
	 * @param subject
	 * @param totalAmount
	 * @param body
	 * @param passbackParams
	 * @return form		html标签字符串
	 */
	public String pageExecute(String outTradeNo, String subject, String totalAmount,
			String passbackParams, String body) {
		
		String params = "";
		try {
			params = URLEncoder.encode(passbackParams, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
	    alipayRequest.setReturnUrl(returnUrl);
	    alipayRequest.setNotifyUrl(notifyUrl);//在公共参数中设置回跳和通知地址
	    alipayRequest.setBizContent("{" +
	        "    \"out_trade_no\":\""+outTradeNo+"\"," +
	        "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
	        "    \"total_amount\":"+totalAmount+"," +
	        "    \"subject\":\""+subject+"\"," +
	        "    \"body\":\""+body+"\"," +
	        "    \"passback_params\":\""+params+"\"" +
	        "  }");//填充业务参数
	    
	    String form="";
	    try {
	        form = getalipayClient().pageExecute(alipayRequest).getBody(); //调用SDK生成表单
	    } catch (AlipayApiException e) {
	        e.printStackTrace();
	        return null;
	    }
		return form;
	}
	
	/**
	 * 验证回调参数正确性
	 * @param params
	 * @return
	 */
	public boolean rsaCheckV1(Map<String, String> params) {
		try {
			return AlipaySignature.rsaCheckV1(params, alipayPublicKey, charset, signType);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private AlipayClient getalipayClient() {
		if (alipayClient == null) {
			alipayClient =  new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId,
					appPrivateKey, "json", charset, alipayPublicKey, signType);
		}
		return alipayClient;
	}
	
	public static WeihAlipayUtil getInstance() {
		if (instance == null) {
			instance = new WeihAlipayUtil();
		}
		return instance;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppPrivateKey() {
		return appPrivateKey;
	}

	public void setAppPrivateKey(String appPrivateKey) {
		this.appPrivateKey = appPrivateKey;
	}

	public String getAlipayPublicKey() {
		return alipayPublicKey;
	}

	public void setAlipayPublicKey(String alipayPublicKey) {
		this.alipayPublicKey = alipayPublicKey;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}
	
}
