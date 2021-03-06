package cn.com.clubank.weihang.manage.product.service;

import com.alibaba.fastjson.JSONArray;
import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.MallActivityGoods;

/**
 * 活动商品
 * 
 * @author LeiQY
 *
 */
public interface MallActivityGoodsService {

	/**
	 * 通过活动商品ID获得活动商品信息
	 * 
	 * @param id
	 * @return
	 */
	String selectActivityGoodsInfo(String id);

	/**
	 * 修改活动商品
	 * 
	 * @param goods
	 * @return
	 */
	CommonResult modifyActivityGoods(MallActivityGoods goods);

	/**
	 * 删除活动商品
	 * 
	 * @param goodsId
	 * @return
	 */
	CommonResult deleteActivityGoods(String goodsId);

	/**
	 * 批量新增活动商品
	 * 
	 * @param array
	 * @return
	 */
	CommonResult insertBatchActivityCoods(JSONArray array);
}
