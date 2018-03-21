package cn.com.clubank.weihang.client.advert;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.manage.product.pojo.MallAdvertPosition;
import cn.com.clubank.weihang.manage.product.service.MallAdvertPositionService;

/**
 * 后台：广告位管理
 * @author Liangwl
 *
 */
@Controller
public class AdvertPositionController {
	
	@Resource
	private MallAdvertPositionService mallAdvertPositionService;

	/**
	 * 后台：通过广告位位置类型查询，广告位名称模糊查询并分页
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientFindByAdPositionIdNamePaged",method=RequestMethod.POST)
	@ResponseBody
	public String findByAdPositionIdNamePaged(@RequestBody JSONObject param){
		
		return mallAdvertPositionService.selectByAdPositionIdNamePaged(param.getString("positionName"), param.getInteger("positionType"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}
	
	/**
	 * 后台：通过广告位ID获得广告位信息
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientFindAdvertPositionInfo",method=RequestMethod.POST)
	@ResponseBody
	public String findAdvertPositionInfo(@RequestBody JSONObject param){
		
		return mallAdvertPositionService.selectAdvertPositionInfo(param.getString("id"));
	}
	
	/**
	 * 后台：新增或编辑广告位
	 * @param record
	 * @return
	 */
	@RequestMapping(value="/clientAddOrEditAdvertPosition",method=RequestMethod.POST)
	@ResponseBody
	public String addOrEditAdvertPosition(@RequestBody MallAdvertPosition record){
		
		return mallAdvertPositionService.insertOrUpdateAdvertPosition(record);
	}
	
	/**
	 * 后台：删除广告位
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/clientDeleteAdvertPosition",method=RequestMethod.POST)
	@ResponseBody
	public String deleteAdvertPosition(@RequestBody JSONObject param){
		
		return mallAdvertPositionService.deleteAdvertPosition(param.getString("id"));
	}
	
	/**
	 * 后台：获得所有广告位信息
	 * @return
	 */
	@RequestMapping(value="/clientFindAllAdvertPositionInfo",method=RequestMethod.POST)
	@ResponseBody
	public String findAllAdvertPositionInfo(){
		
		return mallAdvertPositionService.selectAllAdvertPositionInfo();
	}
}
