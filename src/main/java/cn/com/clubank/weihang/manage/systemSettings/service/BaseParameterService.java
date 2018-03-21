package cn.com.clubank.weihang.manage.systemSettings.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseParameters;

/**
 * 基础参数 系统参数表（业务逻辑层接口）
 * 
 * @author LeiQY
 *
 */
public interface BaseParameterService {

	/**
	 * 根据keyCode获取参数
	 * 
	 * @param keyCode
	 * @return
	 */
	BaseParameters getParameterByKeyCode(String keyCode);
	
	/**
	 * 根据keyCode获取参数的值
	 * 
	 * @param keyCode
	 * @return
	 */
	String getValueByKeyCode(String keyCode);

	/**
	 * 获取地区列表
	 * 
	 * @param parentId
	 * @return
	 */
	CommonResult findAreaList(String parentId);
	
	/**
	 * 通过参数名称（模糊），参数编码（模糊）分页查询列表
	 * @param name 参数名称
	 * @param keyCode 参数编码
	 * @param pageIndex 页码下标
	 * @param pageSize 每页行数
	 * @return
	 */
	String selectParameterListAndPaged(String name,String keyCode,Integer pageIndex,Integer pageSize);
	
	/**
	 * 新增或编辑系统参数
	 * @param record
	 * @return
	 */
	String insertOrUpdateParameter(BaseParameters record);
	
	/**
	 * 删除参数
	 * @param pId 系统参数ID
	 * @return
	 */
	String deleteParameter(String pId);
}
