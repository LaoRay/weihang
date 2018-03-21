package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.List;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.product.dao.MallAdvertPositionMapper;
import cn.com.clubank.weihang.manage.product.pojo.MallAdvertPosition;
import cn.com.clubank.weihang.manage.product.service.MallAdvertPositionService;

@Service
public class MallAdvertPositionServiceImpl implements MallAdvertPositionService {

	@Resource
	private MallAdvertPositionMapper mallAdvertPositionMapper;

	@Override
	public String selectByAdPositionIdNamePaged(String positionName, Integer positionType, Integer pageIndex,
			Integer pageSize) {
		PageObject<MallAdvertPosition> page = new PageObject<>(pageIndex, pageSize);

		int total = mallAdvertPositionMapper.selectSumByPositionName(positionName,positionType);
		List<MallAdvertPosition> list = mallAdvertPositionMapper.selectByAdPositionIdNamePaged(positionName,
				positionType, page.getStart(), pageSize);
		page.setRows(total);
		page.setDataList(list);

		return JSON.toJSONStringWithDateFormat(page, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String insertOrUpdateAdvertPosition(MallAdvertPosition record) {
		JSONObject json = new JSONObject();

		// 判断是执行新增还是更新。主键id为空执行新增，不为空执行更新
		if (StringUtils.isBlank(record.getId())) {
			if (mallAdvertPositionMapper.insert(record) > Constants.INT_0) {
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "新增成功");
			} else {
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "新增失败");
			}
		} else {
			if (mallAdvertPositionMapper.updateSelectiveByPrimaryKey(record) > Constants.INT_0) {
				json.put("apiStatus", ResultCode.SUCC.getValue());
				json.put("msg", "更新成功");
			} else {
				json.put("apiStatus", ResultCode.FAIL.getValue());
				json.put("msg", "更新失败");
			}
		}
		return json.toString();
	}

	@Override
	public String deleteAdvertPosition(String id) {
		JSONObject json = new JSONObject();

		if (mallAdvertPositionMapper.deleteByPrimaryKey(id) > Constants.INT_0) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "删除成功");
		} else {
			json.put("apiStatus", ResultCode.FAIL.getValue());
			json.put("msg", "删除失败");
		}
		return json.toString();
	}

	@Override
	public String selectAllAdvertPositionInfo() {
		JSONObject json = new JSONObject();

		List<MallAdvertPosition> list = mallAdvertPositionMapper.selectAll();
		if (list != null && !list.isEmpty()) {
			json.put("apiStatus", ResultCode.SUCC.getValue());
			json.put("msg", "查询成功");
			json.put("data", list);
		} else {
			json.put("apiStatus", ResultCode.DB_QUERY_EMPTY.getValue());
			json.put("msg", "查询为空");
			json.put("data", list);
		}
		return json.toString();
	}

	@Override
	public String selectAdvertPositionInfo(String id) {
		JSONObject json = new JSONObject();

		MallAdvertPosition advertPosition = mallAdvertPositionMapper.selectByPrimaryKey(id);

		json.put("apiStatus", ResultCode.SUCC.getValue());
		json.put("msg", "查询成功");
		json.put("data", advertPosition);
		return json.toString();
	}

}
