package cn.com.clubank.weihang.website.home;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.home.WebsiteHomeService;

/**
 * 网站首页
 * 
 * @author LeiQY
 *
 */
@Controller
public class WebsiteHomeController {

	@Autowired
	private WebsiteHomeService websiteHomeService;

	/**
	 * 构建pc首页展示的所有数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/websiteHomeData", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteHomeData(HttpServletRequest request) {
		return websiteHomeService.websiteHomeData(request.getHeader("customerId"));
	}

	/**
	 * 构建pc商城首页展示的所有数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "/websiteMallHomeData", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteMallHomeData(HttpServletRequest request) {
		return websiteHomeService.websiteMallHomeData(request.getHeader("customerId"));
	}
}
