package cn.com.clubank.weihang.client.activity;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.MallActivity;
import cn.com.clubank.weihang.manage.product.service.MallActivityService;

/**
 * 后台：活动管理
 * @author Liangwl
 *
 */
@Controller
public class ActivityController {

	@Resource
	private MallActivityService mallActivityService;
	
	/**
	 * 后台：通过活动状态查询、活动名称模糊查询并分页
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientFindByActivityNamePaged",method=RequestMethod.POST)
	@ResponseBody
	public String findByActivityNamePaged(@RequestBody JSONObject param){
		
		return mallActivityService.selectByActivityNamePaged(param.getString("title"), param.getInteger("status"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}
	
	/**
	 * 后台：通过活动主键获得活动信息
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientFindActivityInfoByKey",method=RequestMethod.POST)
	@ResponseBody
	public String findActivityInfoByKey(@RequestBody JSONObject param){
		
		return mallActivityService.selectInfoByKey(param.getString("id"));
	}
	
	/**
	 * 后台：新增或编辑活动
	 * @param mallActivity 活动表对象
	 * @return
	 */
	@RequestMapping(value = "/clientAddOrEditActivity", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult modifyActivity(@RequestBody MallActivity mallActivity) {
		return mallActivityService.modifyActivity(mallActivity);
	}
	
	/**
	 * 后台：通过活动ID获得活动详情
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientFindActivityInfoByActivityId",method=RequestMethod.POST)
	@ResponseBody
	public String findActivityInfoByActivityId(@RequestBody JSONObject param){
		
		return mallActivityService.selectActivityInfoByActivityId(param.getString("activityId"));
	}
	
	/**
	 * 后台：通过活动ID获得活动商品列表
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientFindActivityGoodsListByActivityId",method=RequestMethod.POST)
	@ResponseBody
	public String findActivityGoodsListByActivityId(@RequestBody JSONObject param){
		
		return mallActivityService.selectActivityGoodsListByActivityId(param.getString("activityId"));
	}
	
	/**
	 * 后台：删除活动
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientDeleteActivity", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult deleteActivity(@RequestBody JSONObject param) {
		return mallActivityService.deleteActivity(param.getString("activityId"));
	}

}
