package cn.com.clubank.weihang.manage.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.product.pojo.ProductInfo;

public interface ProductInfoMapper {
	int insert(ProductInfo record);

	List<ProductInfo> selectAll();

	ProductInfo selectByPrimaryKey(String productId);

	int deleteByPrimaryKey(String productId);

	int updateSelectiveByPrimaryKey(ProductInfo productInfo);

	int updateByPrimaryKey(ProductInfo productInfo);

	List<Map<String, String>> selectListByProductName(@Param("start") Integer start,
			@Param("pageSize") Integer pageSize, @Param("productName") String productName);

	List<Map<String, String>> selectProductInfoListByCategoryId(String categoryId);

	List<Map<String, String>> selectMoreProductInfoListByCategoryId(@Param("categoryId") String categoryId,
			@Param("start") Integer start, @Param("pageSize") Integer pageSize);

	/**
	 * 后台：查询商品列表（多条件）
	 */
	List<Map<String, Object>> selectProductList(@Param("start") Integer start, @Param("pageSize") Integer pageSize,
			@Param("productName") String productName, @Param("productType") Integer productType,
			@Param("categoryId") String categoryId, @Param("reviewStatus") Integer reviewStatus);

	Integer selectProductCount(@Param("productName") String productName, @Param("productType") Integer productType,
			@Param("categoryId") String categoryId, @Param("reviewStatus") Integer reviewStatus);

	/**
	 * 通过分类ID获得产品列表并分页
	 * 
	 * @param categoryId
	 *            分类ID
	 * @param startIndex
	 *            起始索引
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	List<Map<String, Object>> selectProductListPaged(@Param("categoryId") String categoryId,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 根据商品类型查询：服务类、实体类
	 * 
	 * @param productType
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<Map<String, Object>> selectByProductType(@Param("productType") Integer productType,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 通过分类Id查询到的产品总条数
	 * 
	 * @param categoryId
	 *            分类ID
	 * @return
	 */
	int selectSumByCategoryId(@Param("categoryId") String categoryId);

	/**
	 * 根据推荐类型查询：精选商品、新品上市、低价超值...
	 * 
	 * @param categoryId
	 * @return
	 */
	List<ProductInfo> selectByRecommendType(@Param("recommendType") Integer recommendType,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	/**
	 * 查询前amount个热销产品（根据sku中的销售数量）
	 * 
	 * @param skuId
	 * @param amount
	 * @return
	 */
	List<ProductInfo> selectHotProduct(@Param("amount") Integer amount);

	Map<String, String> selectProductByProductId(String productId);

	List<Map<String, String>> selectByCategoryKey(@Param("start") Integer start, @Param("pageSize") Integer pageSize,
			@Param("categoryKey") String categoryKey);
}