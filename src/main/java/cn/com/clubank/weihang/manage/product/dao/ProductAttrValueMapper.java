package cn.com.clubank.weihang.manage.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.product.pojo.ProductAttrValue;

public interface ProductAttrValueMapper {
	int deleteByPrimaryKey(String vId);

	int insert(ProductAttrValue record);

	ProductAttrValue selectByPrimaryKey(String vId);

	List<ProductAttrValue> selectAll();

	int updateByPrimaryKey(ProductAttrValue record);

	List<ProductAttrValue> selectListByAttrId(String attrId);

	/**
	 * 分页查询：通过属性值模糊查询，进行分页
	 * 
	 * @param valueName
	 *            属性值
	 * @param startIndex
	 *            起始页
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	List<Map<String, Object>> findPage(@Param("valueName") String valueName, @Param("startIndex") Integer startIndex,
			@Param("pageSize") Integer pageSize);

	/**
	 * 按属性值名模糊查询到的总条数
	 * 
	 * @param valueName
	 *            属性值
	 * @return
	 */
	int findPageTotal(@Param("valueName") String valueName);

	/**
	 * 根据属性id删除属性值
	 * 
	 * @param attrId
	 * @return
	 */
	int deleteByAttrId(String attrId);
}