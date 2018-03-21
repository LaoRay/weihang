package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.common.util.StringUtil;
import cn.com.clubank.weihang.manage.product.dao.ProductBrandMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductBrandRelationMapper;
import cn.com.clubank.weihang.manage.product.pojo.ProductBrand;
import cn.com.clubank.weihang.manage.product.pojo.ProductBrandRelation;
import cn.com.clubank.weihang.manage.product.service.ProductBrandService;

/**
 * 产品品牌管理
 * 
 * @author YangWei
 *
 */
@Service
public class ProductBrandServiceImpl implements ProductBrandService {

	@Resource
	private ProductBrandMapper productBrandMapper;
	@Autowired
	private ProductBrandRelationMapper brandRelationMapper;

	@Override
	public CommonResult saveOrUpdate(ProductBrand info, String categoryIds) {
		String msg = "";
		if (StringUtil.isBlank(info.getBrandId())) {
			productBrandMapper.insert(info);
			msg = "新增成功";
		} else {
			productBrandMapper.updateByPrimaryKey(info);
			msg = "修改成功";
		}
		brandRelationMapper.deleteByBrandId(info.getBrandId());
		if (!StringUtils.isBlank(categoryIds)) {
			for (String categoryId : categoryIds.split(",")) {
				ProductBrandRelation brandRelation = new ProductBrandRelation();
				brandRelation.setCategoryId(categoryId);
				brandRelation.setBrandId(info.getBrandId());
				brandRelationMapper.insert(brandRelation);
			}
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), msg);
	}

	/**
	 * 根据品牌id查询品牌信息
	 */
	@Override
	public CommonResult clientFindProductBrandByBrandId(String brandId) {
		Map<String, Object> map = new HashMap<String, Object>();
		ProductBrand productBrand = productBrandMapper.selectByPrimaryKey(brandId);
		map.put("productBrand", productBrand);
		List<ProductBrandRelation> brandRelationList = brandRelationMapper.selectByBrandId(brandId);
		map.put("brandRelationList", brandRelationList);
		String categoryIds = "";
		for (ProductBrandRelation rel : brandRelationList) {
			categoryIds += rel.getCategoryId() + ",";
		}
		map.put("categoryIds", categoryIds.length() > 0 ? categoryIds.substring(0, categoryIds.length() - 1) : "");
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询品牌信息成功", map);
	}

	/**
	 * 根据品牌id删除品牌
	 */
	@Override
	public CommonResult clientDeleteProductBrand(String brandId) {
		ProductBrand brand = productBrandMapper.selectByPrimaryKey(brandId);
		brand.setBrandStatus(2);
		productBrandMapper.updateByPrimaryKey(brand);
		brandRelationMapper.deleteByBrandId(brandId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "删除成功");
	}

	@Override
	public CommonResult delete(String brandIds) {
		for (String brandId : brandIds.split(",")) {
			// TODO 删除之前判断是否有产品已经使用
			ProductBrand info = productBrandMapper.selectByPrimaryKey(brandId);
			if (info != null) {
				info.setBrandStatus(2); // 无效
				productBrandMapper.updateByPrimaryKey(info);
			}
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "删除成功！");
	}

	@Override
	public String findPage(String brandName, Integer pageIndex, Integer pageSize) {
		PageObject<ProductBrand> page = new PageObject<ProductBrand>(pageIndex, pageSize);

		int total = productBrandMapper.findPageTotal(brandName);
		List<ProductBrand> list = productBrandMapper.findPage(brandName, page.getStart(), page.getPageSize());

		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String findAll() {
		return JSON.toJSONStringWithDateFormat(productBrandMapper.selectAll(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 根据分类id查询品牌
	 */
	@Override
	public CommonResult clientFindProductBrandByCategoryId(String categoryId) {
		List<Map<String, String>> brandList = productBrandMapper.selectByCategoryId(categoryId);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", brandList);
	}
}
