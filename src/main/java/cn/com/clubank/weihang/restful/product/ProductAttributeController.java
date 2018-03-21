package cn.com.clubank.weihang.restful.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.service.ProductAttributeService;

/**
 * 属性管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class ProductAttributeController {

	@Autowired
	private ProductAttributeService attributeService;

	/**
	 * 根据类别id获取属性列表
	 * 
	 * @param categoryId
	 * @return
	 */
	@RequestMapping(value = "/productFindAttrListByCategoryId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findAttrListByCategoryId(@RequestBody JSONObject json) {
		return attributeService.findAttrListByCategoryId(json.getString("categoryId"));
	}

	/**
	 * 根据属性值id获取属性值
	 * 
	 * @param attrValueId
	 * @return
	 */
	@RequestMapping(value = "/productFindAttrValueByAttrValueId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findAttrValueByAttrValueId(@RequestBody JSONObject json) {
		return attributeService.findAttrValueByAttrValueId(json.getString("attrValueId"));
	}

}
