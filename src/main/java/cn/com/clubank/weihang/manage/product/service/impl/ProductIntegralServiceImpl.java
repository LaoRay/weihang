package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.product.dao.ProductIntegralMapper;
import cn.com.clubank.weihang.manage.product.pojo.ProductIntegral;
import cn.com.clubank.weihang.manage.product.service.ProductIntegralService;

/**
 * 商品积分管理
 * 
 * @author LeiQY
 *
 */
@Service
public class ProductIntegralServiceImpl implements ProductIntegralService {

	@Autowired
	private ProductIntegralMapper productIntegralMapper;

	/**
	 * 添加或修改商品积分
	 */
	@Override
	public CommonResult clientModifyProductIntegral(ProductIntegral productIntegral) {
		if (productIntegral == null || StringUtils.isBlank(productIntegral.getSkuId())) {
			return CommonResult.result(ResultCode.PARAM_ERROR.getValue(), "参数为空");
		}
		ProductIntegral pi = productIntegralMapper.selectBySkuId(productIntegral.getSkuId());
		if (pi != null) {
			return CommonResult.result(ResultCode.DATA_EXIST.getValue(), "该商品已存在，您可前去编辑");
		}
		String msg = "";
		if (StringUtils.isBlank(productIntegral.getProductIntegralId())) {
			productIntegralMapper.insert(productIntegral);
			msg = "添加成功";
		} else {
			productIntegralMapper.updateSelectiveByPrimaryKey(productIntegral);
			msg = "修改成功";
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), msg);
	}

	/**
	 * 查询商品积分列表
	 */
	@Override
	public CommonResult clientFindProductIntegralList(Integer pageIndex, Integer pageSize, Integer status,
			String skuName) {
		PageObject<Map<String, String>> page = new PageObject<>(pageIndex, pageSize);
		int total = productIntegralMapper.selectProductIntegralCount(status, skuName);
		page.setRows(total);
		List<Map<String, String>> list = productIntegralMapper.selectProductIntegralList(page.getStart(),
				page.getPageSize(), status, skuName);
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}

	/**
	 * 删除
	 */
	@Override
	public CommonResult clientDeleteProductIntegral(String productIntegralId) {
		productIntegralMapper.deleteByPrimaryKey(productIntegralId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "删除成功");
	}

}
