package cn.com.clubank.weihang.restful.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.ProductAftersaleApply;
import cn.com.clubank.weihang.manage.product.service.IProductAftersaleApplyService;

/**
 * 售后服务
 * 
 * @author LeiQY
 *
 */
@Controller
public class ProductAftersaleApplyController {

	@Autowired
	private IProductAftersaleApplyService iProductAftersaleApplyService;

	/**
	 * 保存售后服务申请信息
	 * 
	 * @param apply
	 * @return
	 */
	@RequestMapping(value = "/productSaveApplyInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult saveApplyInfo(@RequestBody ProductAftersaleApply apply) {
		return iProductAftersaleApplyService.insertApplyInfo(apply);
	}

	/**
	 * 查询售后申请列表
	 * 
	 * @param customerId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/productFindApplyInfoList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findApplyInfoList(@RequestBody JSONObject json) {
		return iProductAftersaleApplyService.findApplyInfoList(json.getString("customerId"),
				json.getInteger("pageIndex"), json.getInteger("pageSize"));
	}
}
