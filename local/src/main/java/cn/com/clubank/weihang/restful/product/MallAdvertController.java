package cn.com.clubank.weihang.restful.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.service.MallAdvertService;

/**
 * 广告管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class MallAdvertController {

	@Autowired
	private MallAdvertService mallAdvertService;

	/**
	 * 根据广告位编号查询广告列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/mallAdvertFindAdvertListByPositionCode", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findAdvertListByPositionCode(@RequestBody JSONObject json) {
		return mallAdvertService.findAdvertListByPositionCode(json.getString("positionCode"));
	}

	/**
	 * 点击广告
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/mallAdvertClickAdvert", method = RequestMethod.POST)
	@ResponseBody
	public String clickAdvert(@RequestBody JSONObject param) {
		return mallAdvertService.updateAdvertClickCount(param.getString("id"), param.getString("customerId"));
	}
}
