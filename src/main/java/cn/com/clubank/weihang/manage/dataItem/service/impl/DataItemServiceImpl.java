package cn.com.clubank.weihang.manage.dataItem.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.dataItem.dao.BaseDataitemMapper;
import cn.com.clubank.weihang.manage.dataItem.pojo.BaseDataitem;
import cn.com.clubank.weihang.manage.dataItem.service.IDataItemService;

/**
 * 数据字典分类表业务逻辑层
 * @author Liangwl
 *
 */
@Service
public class DataItemServiceImpl implements IDataItemService {

	@Resource
	private BaseDataitemMapper baseDataitemMapper;
	
	@Override
	public CommonResult selectDataItemList(Integer pageIndex, Integer pageSize) {
		
		PageObject<BaseDataitem> page = new PageObject<>(pageIndex, pageSize);
		int total=baseDataitemMapper.selectCount();
		List<BaseDataitem> list=baseDataitemMapper.selectAll(page.getStart(), pageSize);
		page.setRows(total);
		page.setDataList(list);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", page);
	}


}
