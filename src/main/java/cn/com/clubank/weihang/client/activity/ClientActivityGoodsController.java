package cn.com.clubank.weihang.client.activity;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.MallActivityGoods;
import cn.com.clubank.weihang.manage.product.service.MallActivityGoodsService;

/**
 * 后台：活动商品管理
 * 
 * @author Liangwl
 *
 */
@Controller
public class ClientActivityGoodsController {

	@Resource
	private MallActivityGoodsService mallActivityGoodsService;

	/**
	 * 后台：批量新增活动商品
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/clientAddActivityGoods", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult addActivityGoods(@RequestBody JSONObject param) {

		return mallActivityGoodsService.insertBatchActivityCoods(param.getJSONArray("array"));
	}

	/**
	 * 后台：通过活动商品ID获得活动商品信息
	 */
	@RequestMapping(value = "/clientFindActivityGoodsInfo", method = RequestMethod.POST)
	@ResponseBody
	public String findActivityGoodsInfo(@RequestBody JSONObject param) {

		return mallActivityGoodsService.selectActivityGoodsInfo(param.getString("id"));
	}

	/**
	 * 后台：编辑活动商品
	 * 
	 * @param goods
	 * @return
	 */
	@RequestMapping(value = "/clientEditActivityGoods", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult modifyActivityGoods(@RequestBody MallActivityGoods goods) {

		return mallActivityGoodsService.modifyActivityGoods(goods);
	}

	/**
	 * 后台：删除活动商品
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/clientDeleteActivityGoods", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult deleteActivityGoods(@RequestBody JSONObject json) {

		return mallActivityGoodsService.deleteActivityGoods(json.getString("id"));
	}
}
