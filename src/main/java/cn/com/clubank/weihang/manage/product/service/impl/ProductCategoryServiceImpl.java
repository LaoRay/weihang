package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.product.dao.ProductAttrMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductCategoryMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductInfoMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductPicMapper;
import cn.com.clubank.weihang.manage.product.pojo.ProductAttr;
import cn.com.clubank.weihang.manage.product.pojo.ProductCategory;
import cn.com.clubank.weihang.manage.product.pojo.ProductPic;
import cn.com.clubank.weihang.manage.product.service.IProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements IProductCategoryService {

	@Resource
	private ProductCategoryMapper productCategoryMapper;

	@Resource
	private ProductAttrMapper productAttrMapper;

	@Autowired
	private ProductInfoMapper productInfoMapper;

	@Autowired
	private ProductPicMapper productPicMapper;

	@Override
	public String insertSortInfo(ProductCategory record) {
		JSONObject json = new JSONObject();

		if(StringUtils.isBlank(record.getParentId())){//当前分类无上级分类，为父分类。
			//限制父分类个数
			List<ProductCategory> list=productCategoryMapper.selectAllRootCategory();
			if(list.size()>12){
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "父类型已满12个，不能再添加父类型");
				return json.toString();
			}
		}
		if (productCategoryMapper.insert(record) > Constants.INT_0) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "保存成功");
		} else {
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "保存失败");
		}
		return json.toString();

	}

	@Override
	public String deleteSortInfo(String categoryId) {
		JSONObject json = new JSONObject();

		// 判断此分类下是否有商品
		if (productInfoMapper.selectSumByCategoryId(categoryId) > Constants.INT_0) {
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "此分类下有商品，不能删除");
			return json.toString();
		}

		// 判断此分类下是否有商品规格
		List<ProductAttr> list = productAttrMapper.selectListByCategoryId(categoryId);
		if (!list.isEmpty()) {
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "此分类下有规格，不能删除");
			return json.toString();
		}

		if (productCategoryMapper.deleteByPrimaryKey(categoryId) > Constants.INT_0) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "删除成功");
		} else {
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "删除失败");
		}
		return json.toString();
	}

	@Override
	public String selectAllRootCategory() {
		JSONObject json = new JSONObject();

		List<ProductCategory> list = productCategoryMapper.selectAllRootCategory();
		if (list != null) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "成功");
			json.put("data", list);
		} else {
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "失败");
		}
		return json.toString();
	}

	@Override
	public String selectAllLeafCategory() {
		JSONObject json = new JSONObject();

		List<ProductCategory> list = productCategoryMapper.selectAllLeafCategory();
		if (list != null) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "成功");
			json.put("data", list);
		} else {
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "失败");
		}
		return json.toString();
	}

	@Override
	public String selectSubclassification(String parentId) {
		JSONObject json = new JSONObject();

		List<ProductCategory> list = productCategoryMapper.selectSubclassification(parentId);
		if (list != null) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "成功");
			json.put("data", list);
		} else {
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "失败");
		}
		return json.toString();
	}

	@Override
	public String findPage(String cname, Integer pageIndex, Integer pageSize) {
		PageObject<ProductCategory> page = new PageObject<ProductCategory>(pageIndex, pageSize);

		int total = productCategoryMapper.findPageTotal(cname);
		List<ProductCategory> list = productCategoryMapper.findPage(cname, page.getStart(), page.getPageSize());

		page.setRows(total);
		page.setDataList(list);
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 根据父分类ID获取子分类列表及其商品信息
	 */
	@Override
	public CommonResult selectSubclassificationListAndProductList(String parentId) {
		// 根据父id查询子分类信息
		List<ProductCategory> productCategoryList = productCategoryMapper.selectSubclassification(parentId);
		Map<String, Object> categoryProductMap = new HashMap<>();
		List<Object> list = new ArrayList<>();
		for (ProductCategory productCategory : productCategoryList) {
			String categoryId = productCategory.getCategoryId();
			// 根据子分类查询该分类下的商品信息
			List<Map<String, String>> productInfoList = new ArrayList<Map<String, String>>();
			for (Map<String, String> map : productInfoMapper.selectProductInfoListByCategoryId(categoryId)) {
				String productId = map.get("productId");
				// 根据商品id查询图片
				List<ProductPic> productPicList = productPicMapper.selectByProductId(productId);
				if (productPicList != null && productPicList.size() > 0) {
					map.put("picSmall", productPicList.get(0).getPicSmall());
				}
				productInfoList.add(map);
			}
			Map<String, Object> productMap = new HashMap<String, Object>();
			productMap.put("categoryId", categoryId);
			productMap.put("cname", productCategory.getCname());
			productMap.put("productInfoList", productInfoList);
			list.add(productMap);
		}
		categoryProductMap.put("productList", list);
		// TODO
		// 查询轮播图
		categoryProductMap.put("bannerList", new ArrayList<>());
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询分类及商品信息成功", categoryProductMap);
	}

	@Override
	public String clientProductCategoryTreeData() {
		// 所有根节点
		List<ProductCategory> rootList = productCategoryMapper.selectAllRootCategory();
		return buildCategoryTreeDataByRoots(rootList);
	}

	/**
	 * 后台：编辑分类信息
	 */
	@Override
	public String modifySortInfo(ProductCategory category) {
		JSONObject json = new JSONObject();
		productCategoryMapper.updateSelectiveByPrimaryKey(category);
		json.put("apiStatus", ResultCode.SUCC.getValue());
		json.put("msg", "编辑成功");
		return json.toString();
	}

	/**
	 * 后台：根据分类id查询分类信息
	 */
	@Override
	public String findSortInfoByCategoryId(String categoryId) {
		JSONObject json = new JSONObject();
		ProductCategory category = productCategoryMapper.selectByPrimaryKey(categoryId);
		if (category != null) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", category);
		} else {
			json.put("apiStatus", ResultCode.DATA_NOEXIST.getValue());
			json.put("msg", "数据不存在，查询失败");
		}
		return json.toString();
	}

	@Override
	public String clientProductCategoryTreeDataByType(Integer type) {
		List<ProductCategory> rootList = null;
		if (type == 0) {
			// 所有根节点
			rootList = productCategoryMapper.selectAllRootCategory();
		} else if (type == 1) {
			// 服务类根节点
			rootList = productCategoryMapper.selectServiceRootCategory();
		} else if (type == 2) {
			// 实体类根节点
			rootList = productCategoryMapper.selectEntityRootCategory();
		} else {
			rootList = new ArrayList<ProductCategory>();
		}
		return buildCategoryTreeDataByRoots(rootList);
	}

	/**
	 * 根据根分类构建树结构数据
	 * 
	 * @param rootList
	 * @return
	 */
	private String buildCategoryTreeDataByRoots(List<ProductCategory> rootList) {
		JSONArray result = new JSONArray();
		JSONObject root = null;
		JSONArray childrens = null;
		JSONObject children = null;
		// 所有根节点
		for (ProductCategory rootCate : rootList) {
			root = new JSONObject();
			root.put("id", rootCate.getCategoryId());
			root.put("label", rootCate.getCname());
			root.put("isLeaf", 0);
			// 子节点
			childrens = new JSONArray();
			List<ProductCategory> childrensList = productCategoryMapper
					.selectSubclassification(rootCate.getCategoryId());
			for (ProductCategory childrenCate : childrensList) {
				children = new JSONObject();
				children.put("id", childrenCate.getCategoryId());
				children.put("label", childrenCate.getCname());
				children.put("isLeaf", 1);
				childrens.add(children);
			}
			root.put("children", childrens);
			result.add(root);
		}
		return result.toString();
	}
}
