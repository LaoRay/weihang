package cn.com.clubank.weihang.manage.product.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.product.dao.ProductReadLogMapper;
import cn.com.clubank.weihang.manage.product.pojo.ProductReadLog;
import cn.com.clubank.weihang.manage.product.service.IProductReadLogService;

@Service
public class ProductReadLogServiceImpl implements IProductReadLogService {

	@Resource
	private ProductReadLogMapper productReadLogMapper;
	
	@Override
	public String insertReadLog(ProductReadLog record) {
		JSONObject json=new JSONObject();
		//判断此商品是否浏览过
		ProductReadLog prl=productReadLogMapper.selectBySkuId(record.getSkuId());
		if(prl!=null){//此商品已存在浏览记录中
			String id=prl.getId();
			record.setId(id);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date createDate=null;
			try {
				createDate = sdf.parse(sdf.format(new Date()));
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			record.setCreateDate(createDate);
			if(productReadLogMapper.updateByPrimaryKeySelective(record)>Constants.INT_0){
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "更新成功");
			}else{
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "更新失败");
			}
		}else{
			if(productReadLogMapper.insert(record)>Constants.INT_0){
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "保存成功");
			}else{
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "保存失败");
			}
		}
		return json.toString();
	}

	@Override
	public String selectReadLog(String customerId,Integer pageIndex,Integer pageSize) {
		JSONObject json=new JSONObject();
		
		List<Map<String,Object>> list=productReadLogMapper.selectByCustomerId(customerId,PageObject.getStart(pageIndex, pageSize),pageSize);
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

}
