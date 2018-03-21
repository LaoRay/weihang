package cn.com.clubank.weihang.manage.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.product.pojo.ProductAttr;

public interface ProductAttrMapper {
    int deleteByPrimaryKey(String attrId);

    int insert(ProductAttr record);

    ProductAttr selectByPrimaryKey(String attrId);

    List<ProductAttr> selectAll();

    int updateByPrimaryKey(ProductAttr record);
    
    /**
     * 选择更新
     * @param record
     * @return
     */
    int updateSelectiveByPrimaryKey(ProductAttr record);

	List<ProductAttr> selectListByCategoryId(String categoryId);
	
	/**
	 * 分页查询：通过属性名模糊查询，进行分页
	 * @param attrName    属性名
	 * @param startIndex  起始页
	 * @param pageSize    每页行数
	 * @return
	 */
    List<Map<String, Object>> findPage(@Param("attrName") String attrName, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);
    
    /**
     * 按属性名模糊查询到的总条数
     * @param attrName  属性名
     * @return
     */
    int findPageTotal(@Param("attrName") String attrName);
}