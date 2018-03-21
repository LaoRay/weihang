package cn.com.clubank.weihang.manage.systemSettings.service;

public interface IBaseLogService {

	/**
	 * 通过操作用户（模糊），操作时间查询日志列表并分页
	 * @param operateName 操作用户
	 * @param operateTimeOne 操作时间1
	 * @param operateTimeTwo 操作时间2
	 * @param pageIndex 页码下标
	 * @param pageSize 每页行数
	 * @return
	 */
	String selectLogListAndPaged(String operateName,String operateTimeOne,String operateTimeTwo,Integer pageIndex,Integer pageSize);
	
	/**
	 * 保存日志
	 * @param operateUserId	操作用户
	 * @param operateName	操作用户
	 * @param module		模块
	 * @param features		功能
	 * @param ipAddress		id地址
	 * @param flatType		平台类型：1-IOS，2-安卓，3-管理端，4-网站
	 */
	void saveLog(String operateUserId, String operateName, String module, String features, String ipAddress, Integer flatType);
}
