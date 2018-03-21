package cn.com.clubank.weihang.manage.dataItem.service.impl;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.DataItemCode;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.dataItem.dao.BaseDataitemDetailMapper;
import cn.com.clubank.weihang.manage.dataItem.dao.BaseDataitemMapper;
import cn.com.clubank.weihang.manage.dataItem.pojo.BaseDataitem;
import cn.com.clubank.weihang.manage.dataItem.pojo.BaseDataitemDetail;
import cn.com.clubank.weihang.manage.dataItem.service.IDataItemDetailService;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据字典明细表业务逻辑层
 * @author Liangwl
 *
 */
@Slf4j
@Service
public class DataItemDetailServiceImpl implements IDataItemDetailService {

	@Resource
	private BaseDataitemMapper baseDataitemMapper;
	
	@Resource
	private BaseDataitemDetailMapper baseDataitemDetailMapper;

	/**
	 * 通过key(code)获得名称和值
	 */
	@Override
	public CommonResult gainNameValue(String code) {
		try {
			//先通过code获得分类表对象
			BaseDataitem baseDataitem=baseDataitemMapper.selectByCode(code);
			//通过分类ID获得明细表名称和值集合
			List<BaseDataitemDetail> list=baseDataitemDetailMapper.selectBySortKey(baseDataitem.getDataId());
			
			//如果是查询预约类型则返回图片路径，路径保存咋备注字段
			if (DataItemCode.BESPEAK_TYPE.getValue().equals(code)) {
				JSONArray jsons = new JSONArray();
				JSONObject json = null;
				for (BaseDataitemDetail info : list) {
					json = new JSONObject();
					json.put("bdId", info.getBdId());
					json.put("name", info.getName());
					json.put("value", info.getValue());
					json.put("imageUrl", info.getDescription());
					jsons.add(json);
				}
				return CommonResult.result(ResultCode.SUCC.getValue(), "成功", jsons);
			}
			return CommonResult.result(ResultCode.SUCC.getValue(), "成功", list);
		} catch (Exception e) {
			log.error("服务器异常",e);
			return CommonResult.result(ResultCode.SERVER_ERROR.getValue(), "服务器异常，查询失败");
		}

	}

	@Override
	public String selectDataItemDetailList(String dataId) {
		JSONObject json=new JSONObject();
		
		List<BaseDataitemDetail> list=baseDataitemDetailMapper.selectDetailByDataId(dataId);
		if(!list.isEmpty()){
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", list);
		}else{
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", list);
		}
		return json.toString();
	}

}
