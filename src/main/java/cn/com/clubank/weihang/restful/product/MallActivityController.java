package cn.com.clubank.weihang.restful.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.service.MallActivityService;

/**
 * 活动商城
 * 
 * @author LeiQY
 *
 */
@Controller
public class MallActivityController {

	@Autowired
	private MallActivityService mallActivityService;

	/**
	 * 查询活动列表及对应的活动商品信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/mallActivityFindActivityAndProduct", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findActivityAndProduct() {
		return mallActivityService.findActivityAndProduct();
	}

	/**
	 * 根据活动id查询活动商品列表
	 * 
	 * @param activityId
	 * @return
	 */
	@RequestMapping(value = "/mallActivityFindActivityProductListByActivityId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findActivityProductListByActivityId(@RequestBody JSONObject json) {
		return mallActivityService.findActivityProductListByActivityId(json.getString("activityId"),
				json.getInteger("pageIndex"), json.getInteger("pageSize"));
	}
}
