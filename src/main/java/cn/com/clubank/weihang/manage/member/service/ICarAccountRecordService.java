package cn.com.clubank.weihang.manage.member.service;

import cn.com.clubank.weihang.manage.member.pojo.CarAccountRecord;

/**
 * 车辆账户流水表（业务逻辑层接口）
 * @author YangWei
 *
 */
public interface ICarAccountRecordService {

	/**
	 * 保存账户流水记录并处理其他业务
	 * @param info
	 * @return
	 */
    CarAccountRecord save(CarAccountRecord info);

}
