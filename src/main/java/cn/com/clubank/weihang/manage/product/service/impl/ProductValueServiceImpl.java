package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.product.dao.ProductAttrMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductAttrValueMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductValueMapper;
import cn.com.clubank.weihang.manage.product.pojo.ProductAttr;
import cn.com.clubank.weihang.manage.product.pojo.ProductAttrValue;
import cn.com.clubank.weihang.manage.product.pojo.ProductValue;
import cn.com.clubank.weihang.manage.product.service.ProductValueService;

/**
 * 产品属性管理
 * 
 * @author LeiQY
 *
 */
@Service
public class ProductValueServiceImpl implements ProductValueService {

	@Autowired
	private ProductValueMapper productValueMapper;
	
	@Autowired
	private ProductAttrValueMapper productAttrValueMapper;
	
	@Autowired
	private ProductAttrMapper productAttrMapper;

	/**
	 * 增加产品基本属性
	 */
	@Override
	public CommonResult addProductValue(ProductValue productValue) {
		productValueMapper.insert(productValue);
		return CommonResult.result(ResultCode.SUCC.getValue(), "增加产品属性成功");
	}

	/**
	 * 根据产品id查询产品基本属性列表
	 */
	@Override
	public CommonResult findProductValueListByProductId(String productId) {
		List<ProductValue> productValueList = productValueMapper.selectListByProductId(productId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询产品基本属性成功", productValueList);
	}

	@Override
	public void addProductValue(String productId, String attrValueId) {
		List<ProductValue> list = productValueMapper.selectByProductAndAttrValue(productId, attrValueId);
		//判断商品是否存在这个属性，如果不存在则保存
		if (list.size() == 0) {
			//获取属性及属性值对象
			ProductAttrValue attrValue = productAttrValueMapper.selectByPrimaryKey(attrValueId);
			ProductAttr attr = productAttrMapper.selectByPrimaryKey(attrValue == null ? "" : attrValue.getAttrId());
			if (attr != null) {
				ProductValue info = new ProductValue();
				info.setProductId(productId);
				info.setProductAttr(attr.getAttrName());
				info.setProductAttrId(attr.getAttrId());
				info.setProductAttrValue(attrValue.getValueName());
				info.setProductAttrValueId(attrValue.getVId());
				productValueMapper.insert(info);
			}
			
		}
	}

}
