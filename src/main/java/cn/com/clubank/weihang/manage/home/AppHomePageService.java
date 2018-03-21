package cn.com.clubank.weihang.manage.home;

import cn.com.clubank.weihang.common.util.CommonResult;

/**
 * app端首页数据
 * 
 * @author YangWei
 *
 */
public interface AppHomePageService {

	/**
	 * 构建app首页展示的所有数据
	 * @param customerId
	 * @param type			1-首页，2-汽车维修主页
	 * @return
	 */
	CommonResult appHomeData(String customerId, Integer type);
	
}
