package cn.com.clubank.weihang.restful.news;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.manage.news.service.IBaseNewsService;

@Controller
public class BaseNewsController {

	@Resource
	private IBaseNewsService iBaseNewsService;
	
	/**
	 * 分页查询获得新闻图片、标题和主键
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/newsFindNewsPicTitle",method=RequestMethod.POST)
	@ResponseBody
	public String findNewsPicTitle(@RequestBody JSONObject param){
		
		return iBaseNewsService.selectNewsPicPaged(param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}
	
	/**
	 * 通过新闻主键获得新闻信息并更新浏览量
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/newsFindNewsContent",method=RequestMethod.POST)
	@ResponseBody
	public String findNewsContent(@RequestBody JSONObject param){
		
		return iBaseNewsService.selectNewsContent(param.getString("nId"));
	}
}
