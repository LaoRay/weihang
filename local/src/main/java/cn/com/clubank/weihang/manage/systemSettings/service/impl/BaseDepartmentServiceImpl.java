package cn.com.clubank.weihang.manage.systemSettings.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.systemSettings.dao.BaseDepartmentMapper;
import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseDepartment;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseDepartmentService;

/**
 * 行政部门表
 * 
 * @author LeiQY
 *
 */
@Service
public class BaseDepartmentServiceImpl implements BaseDepartmentService {

	@Autowired
	private BaseDepartmentMapper baseDepartmentMapper;

	/**
	 * 查询所有部门列表
	 */
	@Override
	public CommonResult clientFindBaseDepartmentList() {
		List<BaseDepartment> list = baseDepartmentMapper.selectAll();
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询成功", list);
	}

}
