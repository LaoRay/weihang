package cn.com.clubank.weihang.restful.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.manage.product.pojo.ProductReadLog;
import cn.com.clubank.weihang.manage.product.service.IProductReadLogService;

/**
 * 产品浏览记录管理
 * 
 * @author Liangwl
 *
 */
@Controller
public class ProductReadLogController {

	@Resource
	private IProductReadLogService iProductReadLogService;

	/**
	 * 保存浏览记录
	 * 
	 * @param record
	 * @return
	 */
	@RequestMapping(value = "/productSaveReadLog", method = RequestMethod.POST)
	@ResponseBody
	public String saveReadLog(@RequestBody ProductReadLog record) {

		return iProductReadLogService.insertReadLog(record);
	}

	/**
	 * 查询客户浏览记录并分页
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/productFindCustomerReadLog", method = RequestMethod.POST)
	@ResponseBody
	public String findCustomerReadLog(@RequestBody JSONObject param) {

		return iProductReadLogService.selectReadLog(param.getString("customerId"), param.getInteger("pageIndex"),
				param.getInteger("pageSize"));
	}
}
