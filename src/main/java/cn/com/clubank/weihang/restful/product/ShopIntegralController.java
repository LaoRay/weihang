package cn.com.clubank.weihang.restful.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.manage.product.service.IShopIntegralService;

@Controller
public class ShopIntegralController {

	@Resource
	private IShopIntegralService iShopIntegralService;
	
	/**
	 * 查询积分商城商品列表
	 * @return
	 */
	@RequestMapping(value="/productGainShopIntegralList",method=RequestMethod.POST)
	@ResponseBody
	public String getShopIntegralList(){
		
		return iShopIntegralService.selectShopIntegralList();
	}
	
	/**
	 * 兑换积分商城商品
	 * @param customerId
	 * @param siId
	 * @return
	 */
	@RequestMapping(value="/productRedeemItems",method=RequestMethod.POST)
	@ResponseBody
	public String redeemItems(@RequestBody JSONObject param){
		
		return iShopIntegralService.updateIntegralAccount(param.getString("customerId"), param.getString("siId"));
	}
}
