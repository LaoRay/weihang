package cn.com.clubank.weihang.client.marketing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductIntegral;
import cn.com.clubank.weihang.manage.product.service.ProductIntegralService;

/**
 * 商品积分管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class ClientProductIntegralController {

	@Autowired
	private ProductIntegralService productIntegralService;

	/**
	 * 添加或修改商品积分
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientModifyProductIntegral", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientModifyProductIntegral(@RequestBody ProductIntegral productIntegral) {
		return productIntegralService.clientModifyProductIntegral(productIntegral);
	}

	/**
	 * 删除商品积分
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientDeleteProductIntegral", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientDeleteProductIntegral(@RequestBody JSONObject json) {
		return productIntegralService.clientDeleteProductIntegral(json.getString("productIntegralId"));
	}

	/**
	 * 查询商品积分列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/clientFindProductIntegralList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult clientFindProductIntegralList(@RequestBody JSONObject json) {
		return productIntegralService.clientFindProductIntegralList(json.getInteger("pageIndex"),
				json.getInteger("pageSize"), json.getInteger("status"), json.getString("skuName"));
	}
}
