package cn.com.clubank.weihang.manage.product.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductAttr;
import cn.com.clubank.weihang.manage.product.pojo.ProductAttrValue;

/**
 * 属性管理
 * 
 * @author LeiQY
 *
 */
public interface ProductAttributeService {

	/**
	 * 增加或编辑属性
	 * 
	 * @param attr
	 *            属性表对象
	 * @return
	 */
	CommonResult addOrEditAttribute(ProductAttr attr);

	/**
	 * 根据属性id获取属性
	 * 
	 * @param attrId
	 * @return
	 */
	CommonResult findAttrByAttrId(String attrId);

	/**
	 * 删除属性
	 * 
	 * @param attrId
	 * @return
	 */
	CommonResult deleteAttrByAttrId(String attrId);

	/**
	 * 根据类别id查询属性列表
	 * 
	 * @param categoryId
	 * @return
	 */
	CommonResult findAttrListByCategoryId(String categoryId);

	/**
	 * 增加属性值
	 * 
	 * @param attrValue
	 * @return
	 */
	CommonResult addAttributeValue(ProductAttrValue attrValue);

	/**
	 * 根据属性值id查询属性值
	 * 
	 * @param attrValueId
	 * @return
	 */
	CommonResult findAttrValueByAttrValueId(String attrValueId);

	/**
	 * 根据属性id获取属性值列表
	 * 
	 * @param attrId
	 * @return
	 */
	CommonResult findAttrValueListByAttrId(String attrId);

	/**
	 * 删除规格信息
	 * 
	 * @param vId
	 *            属性值ID
	 * @return
	 */
	String deleteAttrValue(String vId);

	/**
	 * 分页查询：通过规格名称(属性名)模糊查询
	 * 
	 * @param attrName
	 *            属性名
	 * @param pageIndex
	 *            页码下标
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	String findPage(String attrName, Integer pageIndex, Integer pageSize);

	/**
	 * 分页查询：通过属性值模糊查询并分页
	 * 
	 * @param valueName
	 *            属性值
	 * @param pageIndex
	 *            页码下标
	 * @param pageSize
	 *            每页行数
	 * @return
	 */
	String selectByAttrValuePage(String valueName, Integer pageIndex, Integer pageSize);
}
