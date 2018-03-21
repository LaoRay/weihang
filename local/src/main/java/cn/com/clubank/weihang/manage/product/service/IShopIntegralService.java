package cn.com.clubank.weihang.manage.product.service;

public interface IShopIntegralService {

	/**
	 * 查询积分商城商品列表
	 * @return
	 */
	String selectShopIntegralList();
	
	/**
	 * 兑换积分商城商品
	 * @param customerId 会员ID
	 * @param siId       积分商品ID
	 * @return
	 */
	String updateIntegralAccount(String customerId,String siId);
}
