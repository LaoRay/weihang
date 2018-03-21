package cn.com.clubank.weihang.manage.news.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.news.dao.BaseNewsMapper;
import cn.com.clubank.weihang.manage.news.pojo.BaseNews;
import cn.com.clubank.weihang.manage.news.service.IBaseNewsService;

/**
 * 资讯管理
 * @author Liangwl
 *
 */
@Service
public class BaseNewsServiceImpl implements IBaseNewsService {

	@Resource
	private BaseNewsMapper baseNewsMapper;
	
	@Override
	public String selectNewsPicPaged(Integer pageIndex, Integer pageSize) {
		JSONObject json=new JSONObject();
		
		List<BaseNews> list=baseNewsMapper.selectNewsPaged(PageObject.getStart(pageIndex, pageSize), pageSize);
		if(list==null){
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", list);
		}else{
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", list);
		}
		return json.toString();
	}

	@Override
	public String selectNewsContent(String nId) {
		JSONObject json=new JSONObject();
		
		Map<String,Object> map=new HashMap<String,Object>();
		BaseNews bn=baseNewsMapper.selectByPrimaryKey(nId);
		if(bn==null){
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
		}else{
			map.put("title", bn.getTitle());//新闻标题
			map.put("content", bn.getContent());//新闻内容
			map.put("releaseTime", bn.getReleaseTime());//新闻发布时间
			
			//更新浏览量
			int pv=bn.getPv();
			bn.setPv(pv+1);
			baseNewsMapper.updateByPrimaryKeySelective(bn);
			
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", map);
		}
		return JSON.toJSONStringWithDateFormat(json, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String selectByNewsTitlePaged(String title, Integer type, Integer pageIndex, Integer pageSize) {
		PageObject<BaseNews> page=new PageObject<>(pageIndex, pageSize);
		
		int total=baseNewsMapper.selectByNewsTitleSum(title,type);
		List<BaseNews> list=baseNewsMapper.selectByNewsTitlePaged(title, type, page.getStart(), pageSize);
		
		page.setRows(total);//总行数
		page.setDataList(list);//分页结果集
		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String selectByNewsKey(String nId) {
		JSONObject json=new JSONObject();
		
		BaseNews bn=baseNewsMapper.selectByPrimaryKey(nId);
		if(bn==null){
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", bn);
		}else{
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", bn);
		}
		return json.toString();
	}

	@Override
	public String insertOrEdit(BaseNews record) {
		JSONObject json=new JSONObject();
		
		//判断是执行新增还是更新。nId(新闻主键)为空执行新增，不为空执行更新
		if(StringUtils.isBlank(record.getNId())){
			if(baseNewsMapper.insert(record)>Constants.INT_0){
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "新增成功");
			}else{
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "新增失败");
			}
		}else{
			if(baseNewsMapper.updateByPrimaryKeySelective(record)>Constants.INT_0){
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "更新成功");
			}else{
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "更新失败");
			}
		}
		return json.toString();
	}

	@Override
	public String deleteNewsInfo(String nId) {
		JSONObject json=new JSONObject();
		
		if(baseNewsMapper.deleteByPrimaryKey(nId)>Constants.INT_0){
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "删除成功");
		}else{
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "删除失败");
		}
		return json.toString();
	}

}
