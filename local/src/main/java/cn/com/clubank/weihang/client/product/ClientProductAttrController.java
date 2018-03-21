package cn.com.clubank.weihang.client.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductAttr;
import cn.com.clubank.weihang.manage.product.pojo.ProductAttrValue;
import cn.com.clubank.weihang.manage.product.service.ProductAttributeService;

/**
 * 后台：规格管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class ClientProductAttrController {

	@Autowired
	private ProductAttributeService attributeService;

	/**
	 * 根据属性名查询（模糊查询）属性列表并分页
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientProductAttrFindPage", method = RequestMethod.POST)
	@ResponseBody
	public String productAttrFindPage(@RequestBody JSONObject param) {

		return attributeService.findPage(param.getString("attrName"), param.getInteger("pageIndex"),
				param.getInteger("pageSize"));
	}

	/**
	 * 增加或编辑属性
	 * 
	 * @param attr
	 * @return
	 */
	@RequestMapping(value = "/clientProductAddOrEditAttr", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult addOrEditAttribute(@RequestBody ProductAttr attr) {
		return attributeService.addOrEditAttribute(attr);
	}

	/**
	 * 根据属性id获取属性
	 * 
	 * @param attrId
	 * @return
	 */
	@RequestMapping(value = "/clientProductFindAttrByAttrId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findAttrByAttrId(@RequestBody JSONObject json) {
		return attributeService.findAttrByAttrId(json.getString("attrId"));
	}

	/**
	 * 根据属性id删除属性
	 * 
	 * @param attrId
	 * @return
	 */
	@RequestMapping(value = "/clientProductDeleteAttrByAttrId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult deleteAttrByAttrId(@RequestBody JSONObject json) {
		return attributeService.deleteAttrByAttrId(json.getString("attrId"));
	}

	/**
	 * 根据属性id获取属性值列表
	 * 
	 * @param attrId
	 * @return
	 */
	@RequestMapping(value = "/clientProductFindAttrValueListByAttrId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findAttrValueListByAttrId(@RequestBody JSONObject json) {
		return attributeService.findAttrValueListByAttrId(json.getString("attrId"));
	}

	/**
	 * 增加属性值
	 * 
	 * @param attrValue
	 * @return
	 */
	@RequestMapping(value = "/clientProductAddAttrValue", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult addAttributeValue(@RequestBody ProductAttrValue attrValue) {
		return attributeService.addAttributeValue(attrValue);
	}

	/**
	 * 删除属性值信息
	 * 
	 * @param vId
	 * @return
	 */
	@RequestMapping(value = "/clientProductDeleteAttrValue", method = RequestMethod.POST)
	@ResponseBody
	public String deleteAttrValue(@RequestBody JSONObject param) {
		return attributeService.deleteAttrValue(param.getString("attrValueId"));
	}
}
