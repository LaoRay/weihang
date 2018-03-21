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
}
