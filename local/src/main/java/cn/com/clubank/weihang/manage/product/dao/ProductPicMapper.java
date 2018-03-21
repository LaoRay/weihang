package cn.com.clubank.weihang.manage.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.product.pojo.ProductPic;

public interface ProductPicMapper {
	int deleteByPrimaryKey(String productPicId);

	int insert(ProductPic record);

	ProductPic selectByPrimaryKey(String productPicId);

	List<ProductPic> selectAll();

	int updateByPrimaryKey(ProductPic record);

	void insertBatch(List<ProductPic> picList);

	List<ProductPic> selectByProductId(String productId);

	List<Map<String, String>> selectPicListByProductId(String productId);

	ProductPic selectBySmallAndBig(@Param("picSmall") String picSmall, @Param("picBig") String picBig);

	int updateSelectiveByPrimaryKey(ProductPic productPic);
}