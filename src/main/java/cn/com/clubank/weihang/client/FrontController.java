package cn.com.clubank.weihang.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 前端控制层：实现推荐注册页面的请求跳转
 * 
 * @author YangWei
 *
 */
@Controller
public class FrontController {
	
	@Value("${androidDownloadUrl}")
	private String androidDownloadUrl;
	
	@Value("${iosDownloadUrl}")
	private String iosDownloadUrl;
	
	/**
	 * 首页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/webindex", method=RequestMethod.GET)
	public String index(Model model) {
		return "index";
	}
	
	/**
	 * 推荐注册页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/webRegedit", method=RequestMethod.GET)
	public String regedit(Model model, String recommendCode) {
		model.addAttribute("recommendCode", recommendCode);
		return "recommend/regedit";
	}
	
	/**
	 * 注册成功
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/webRegeditSucess", method=RequestMethod.GET)
	public String productCategory(Model model) {
		model.addAttribute("androidDownloadUrl", androidDownloadUrl);
		model.addAttribute("iosDownloadUrl", iosDownloadUrl);
		return "recommend/regedit_sucess";
	}
	
	/**
	 * 下载页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/webAPPDownload", method=RequestMethod.GET)
	public String webAPPDownload(Model model) {
		model.addAttribute("androidDownloadUrl", androidDownloadUrl);
		model.addAttribute("iosDownloadUrl", iosDownloadUrl);
		return "recommend/regedit_sucess";
	}
	
	/**
	 * 威航协议
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/webUserRegistration", method=RequestMethod.GET)
	public String userRegistration(Model model) {
		return "recommend/userRegistration";
	}
	
}
