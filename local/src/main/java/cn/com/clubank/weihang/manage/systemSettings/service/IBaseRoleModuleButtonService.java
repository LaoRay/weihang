package cn.com.clubank.weihang.manage.systemSettings.service;

import cn.com.clubank.weihang.manage.systemSettings.pojo.BaseRoleModuleButton;

public interface IBaseRoleModuleButtonService {

	/**
	 * 分配权限提交
	 * @param record
	 * @return
	 */
	String insertRoleModuleButton(BaseRoleModuleButton record);
}
