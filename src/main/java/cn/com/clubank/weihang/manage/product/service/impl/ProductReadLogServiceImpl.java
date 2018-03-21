package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.product.dao.ProductPicMapper;
import cn.com.clubank.weihang.manage.product.dao.ProductReadLogMapper;
import cn.com.clubank.weihang.manage.product.pojo.ProductPic;
import cn.com.clubank.weihang.manage.product.pojo.ProductReadLog;
import cn.com.clubank.weihang.manage.product.service.IProductReadLogService;

@Service
public class ProductReadLogServiceImpl implements IProductReadLogService {

	@Resource
	private ProductReadLogMapper productReadLogMapper;

	@Resource
	private ProductPicMapper productPicMapper;

	@Override
	public String insertReadLog(ProductReadLog record) {
		JSONObject json = new JSONObject();
		if (record == null || StringUtils.isBlank(record.getCustomerId())
				|| StringUtils.isBlank(record.getProductId())) {
			json.put("apiStatus", ResultCode.PARAM_ERROR.getValue());
			json.put("msg", "参数错误");
			return json.toString();
		}
		// 判断此商品是否浏览过
		ProductReadLog prl = productReadLogMapper.selectByCustomerIdAndProductId(record.getCustomerId(),
				record.getProductId());
		if (prl != null) {// 此商品已存在浏览记录中
			prl.setCreateDate(new Date());
			if (productReadLogMapper.updateByPrimaryKeySelective(prl) > Constants.INT_0) {
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "更新成功");
			} else {
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "更新失败");
			}
		} else {
			if (productReadLogMapper.insert(record) > Constants.INT_0) {
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "保存成功");
			} else {
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "保存失败");
			}
		}
		return json.toString();
	}

	@Override
	public String selectReadLog(String customerId, Integer pageIndex, Integer pageSize) {
		JSONObject json = new JSONObject();

		List<Map<String, Object>> list = productReadLogMapper.selectByCustomerId(customerId,
				PageObject.getStart(pageIndex, pageSize), pageSize);
		for (Map<String, Object> map : list) {
			List<ProductPic> pics = productPicMapper.selectByProductId(map.get("productId").toString());
			map.put("picBig", pics.size() > 0 ? pics.get(0).getPicBig() : Constants.NULL);
			map.put("picSmall", pics.size() > 0 ? pics.get(0).getPicSmall() : Constants.NULL);
		}
		json.put("apiStatus", ResultCode.SUCC.getValue());
		json.put("msg", "查询成功");
		json.put("data", list);
		return json.toString();
	}

	/**
	 * pc:获得更多浏览记录
	 */
	@Override
	public CommonResult websiteFindMoreReadRecords(String customerId, Integer pageIndex, Integer pageSize) {
		PageObject<Map<String, Object>> page = new PageObject<>(pageIndex, pageSize);
		int total = productReadLogMapper.selectRecordCount(customerId);
		page.setRows(total);
		List<Map<String, Object>> list = productReadLogMapper.selectByCustomerId(customerId, page.getStart(),
				page.getPageSize());
		for (Map<String, Object> map : list) {
			List<ProductPic> pics = productPicMapper.selectByProductId(map.get("productId").toString());
			map.put("picBig", pics.size() > 0 ? pics.get(0).getPicBig() : Constants.NULL);
		}
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}

}
