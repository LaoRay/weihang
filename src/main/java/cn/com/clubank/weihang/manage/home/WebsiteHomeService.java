package cn.com.clubank.weihang.manage.home;

import cn.com.clubank.weihang.common.util.CommonResult;

/**
 * pc端首页
 * 
 * @author LeiQY
 *
 */
public interface WebsiteHomeService {

	/**
	 * 构建pc首页展示的所有数据
	 * 
	 * @param customerId
	 * @return
	 */
	CommonResult websiteHomeData(String customerId);

	/**
	 * 构建pc商城首页展示的所有数据
	 * 
	 * @param customerId
	 * @return
	 */
	CommonResult websiteMallHomeData(String customerId);
}
