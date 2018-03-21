package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.product.dao.ProductAttrMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductAttrValueMapper;
import cn.com.clubank.weihang.manage.product.pojo.ProductAttr;
import cn.com.clubank.weihang.manage.product.pojo.ProductAttrValue;
import cn.com.clubank.weihang.manage.product.service.ProductAttributeService;

/**
 * 属性管理
 * 
 * @author LeiQY
 *
 */
@Service
public class ProductAttributeServiceImpl implements ProductAttributeService {

	@Autowired
	private ProductAttrMapper attrMapper;
	@Autowired
	private ProductAttrValueMapper attrValueMapper;

	@Override
	public CommonResult addOrEditAttribute(ProductAttr attr) {
		if (attr.getAttrId() == null) {
			attrMapper.insert(attr);// 新增
			return CommonResult.result(ResultCode.SUCC.getValue(), "属性添加成功");
		} else {
			attrMapper.updateSelectiveByPrimaryKey(attr);// 编辑
			return CommonResult.result(ResultCode.SUCC.getValue(), "编辑成功");
		}
	}

	/**
	 * 删除属性
	 */
	@Override
	public CommonResult deleteAttrByAttrId(String attrId) {
		attrValueMapper.deleteByAttrId(attrId);
		attrMapper.deleteByPrimaryKey(attrId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "删除成功");
	}

	/**
	 * 根据属性id获取属性
	 */
	@Override
	public CommonResult findAttrByAttrId(String attrId) {
		ProductAttr attr = attrMapper.selectByPrimaryKey(attrId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "属性查询成功", attr);
	}

	/**
	 * 根据类别id查询属性列表
	 */
	@Override
	public CommonResult findAttrListByCategoryId(String categoryId) {
		List<ProductAttr> productAttrList = attrMapper.selectListByCategoryId(categoryId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "属性查询成功", productAttrList);
	}

	/**
	 * 增加属性值
	 */
	@Override
	public CommonResult addAttributeValue(ProductAttrValue attrValue) {
		attrValueMapper.insert(attrValue);
		return CommonResult.result(ResultCode.SUCC.getValue(), "属性值添加成功");
	}

	/**
	 * 根据属性值id查询属性值
	 */
	@Override
	public CommonResult findAttrValueByAttrValueId(String attrValueId) {
		ProductAttrValue attrValue = attrValueMapper.selectByPrimaryKey(attrValueId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "属性值查询成功", attrValue);
	}

	/**
	 * 根据属性id获取属性值列表
	 */
	@Override
	public CommonResult findAttrValueListByAttrId(String attrId) {
		List<ProductAttrValue> attrValueList = attrValueMapper.selectListByAttrId(attrId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "属性值查询成功", attrValueList);
	}

	@Override
	public String deleteAttrValue(String vId) {
		JSONObject json = new JSONObject();

		if (attrValueMapper.deleteByPrimaryKey(vId) > Constants.INT_0) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "删除成功");
		} else {
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "删除失败");
		}
		return json.toString();
	}

	@Override
	public String findPage(String attrName, Integer pageIndex, Integer pageSize) {
		PageObject<Map<String, Object>> page = new PageObject<Map<String, Object>>(pageIndex, pageSize);

		int total = attrMapper.findPageTotal(attrName);
		List<Map<String, Object>> list = attrMapper.findPage(attrName, page.getStart(), page.getPageSize());

		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String selectByAttrValuePage(String valueName, Integer pageIndex, Integer pageSize) {
		PageObject<Map<String, Object>> page = new PageObject<Map<String, Object>>(pageIndex, pageSize);

		int total = attrValueMapper.findPageTotal(valueName);
		List<Map<String, Object>> list = attrValueMapper.findPage(valueName, page.getStart(), page.getPageSize());

		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

}
