package cn.com.clubank.weihang.client.advert;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.MallAdvert;
import cn.com.clubank.weihang.manage.product.service.MallAdvertService;

/**
 * 后台：广告列表
 * @author Liangwl
 *
 */
@Controller
public class AdvertController {

	@Resource
	private MallAdvertService mallAdvertService;
	
	/**
	 * 后台：通过广告名称模糊查询，广告状态、广告位置类型查询并分页
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientFindAdvertListPaged",method=RequestMethod.POST)
	@ResponseBody
	public String findAdListByNameOrStatusOrType(@RequestBody JSONObject param){
		
		return mallAdvertService.selectAdListByNameOrStatusOrType(param.getString("adName"), param.getInteger("status"), param.getInteger("positionType"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}
	
	/**
	 * 后台：通过广告ID获得广告信息和广告位列表
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientFindAdvertInfoAndPositionList",method=RequestMethod.POST)
	@ResponseBody
	public String findAdvertInfoAndPositionList(@RequestBody JSONObject param){
		
		return mallAdvertService.selectAdvertInfoAndPositionList(param.getString("id"));
	}
	
	/**
	 * 后台：通过广告ID获得广告详情
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientFindAdvertDetail",method=RequestMethod.POST)
	@ResponseBody
	public String findAdvertDetail(@RequestBody JSONObject param){
		
		return mallAdvertService.selectAdvertDetail(param.getString("id"));
	}
	
	/**
	 * 后台：新增或编辑广告
	 * @param record
	 * @return
	 */
	@RequestMapping(value="/clientAddOrEditAdvert",method=RequestMethod.POST)
	@ResponseBody
	 public CommonResult addOrEditAdvert(@RequestBody MallAdvert record){
		 
		 return mallAdvertService.modifyAdvert(record);
	 }

	/**
	 * 后台：删除广告
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientDeleteAdvert", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult deleteAdvert(@RequestBody JSONObject param) {
		
		return mallAdvertService.deleteAdvert(param.getString("id"));
	}
	
	/**
	 * 后台：关闭广告
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientCloseAdvert", method=RequestMethod.POST)
	@ResponseBody
	public String closeAdvert(@RequestBody JSONObject param){
		
		return mallAdvertService.updateAdvertStatus(param.getString("id"));
	}
	
}
