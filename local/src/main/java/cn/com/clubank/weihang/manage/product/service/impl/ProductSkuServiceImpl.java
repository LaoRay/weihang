package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.product.dao.ProductPicMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductSkuMapper;
import cn.com.clubank.weihang.manage.product.pojo.ProductSku;
import cn.com.clubank.weihang.manage.product.service.ProductSkuService;

/**
 * 产品sku管理
 * 
 * @author LeiQY
 *
 */
@Service
public class ProductSkuServiceImpl implements ProductSkuService {

	@Autowired
	private ProductSkuMapper productSkuMapper;
	
	@Autowired
	private ProductPicMapper productPicMapper;

	/**
	 * 增加产品sku
	 */
	@Override
	public CommonResult addProductSku(ProductSku productSku) {
		productSkuMapper.insert(productSku);
		return CommonResult.result(ResultCode.SUCC.getValue(), "增加产品sku成功");
	}

	/**
	 * 根据skuId查询产品sku
	 */
	@Override
	public CommonResult FindProductSkuBySkuId(String skuId) {
		ProductSku productSku = productSkuMapper.selectByPrimaryKey(skuId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询产品sku成功", productSku);
	}

	/**
	 * 根据产品id查询产品sku列表
	 */
	@Override
	public CommonResult findProductSkuListByProductId(String productId) {
		List<ProductSku> productSkuList = productSkuMapper.selectSkuListByProductId(productId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询产品sku成功", productSkuList);
	}

	/**
	 * 根据skuId删除sku
	 */
	@Override
	public CommonResult deleteSkuBySkuId(String skuId) {
		ProductSku sku = productSkuMapper.selectByPrimaryKey(skuId);
		if (sku == null) {
			return CommonResult.result(ResultCode.DATA_NOEXIST.getValue(), "sku信息不存在");
		}
		productPicMapper.deleteByPrimaryKey(sku.getProductPicId()); //删除sku的图片
		productSkuMapper.deleteByPrimaryKey(skuId); //删除sku信息
		return CommonResult.result(ResultCode.SUCC.getValue(), "删除产品sku成功");
	}
}
