package cn.com.clubank.weihang.client.news;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.manage.news.pojo.BaseNews;
import cn.com.clubank.weihang.manage.news.service.IBaseNewsService;

/**
 * 后台：资讯管理
 * @author Liangwl
 *
 */
@Controller
public class NewsController {

	@Resource
	private IBaseNewsService iBaseNewsService;
	
	/**
	 * 后台：通过新闻标题模糊查询,新闻类型查询新闻中心表并分页
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientFindNewsList",method=RequestMethod.POST)
	@ResponseBody
	public String findNewsList(@RequestBody JSONObject param){
		
		return iBaseNewsService.selectByNewsTitlePaged(param.getString("title"), param.getInteger("type"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}
	
	/**
	 * 后台：通过新闻主键获得新闻信息
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientFindNewsInfo",method=RequestMethod.POST)
	@ResponseBody
	public String findNewsInfo(@RequestBody JSONObject param){
		
		return iBaseNewsService.selectByNewsKey(param.getString("nId"));
	}
	
	/**
	 * 后台：保存新闻信息
	 * @param record 新闻中心表对象
	 * @return
	 */
	@RequestMapping(value="/clientSaveNewsInfo",method=RequestMethod.POST)
	@ResponseBody
	public String saveNewsInfo(@RequestBody BaseNews record){
		
		return iBaseNewsService.insertOrEdit(record);
	}
	
	/**
	 * 后台：删除资讯
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientDeleteNewsInfo",method=RequestMethod.POST)
	@ResponseBody
	public String deleteNewsInfo(@RequestBody JSONObject param){
		
		return iBaseNewsService.deleteNewsInfo(param.getString("nId"));
	}
}
