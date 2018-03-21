package cn.com.clubank.weihang.manage.member.service;

import java.util.List;

import cn.com.clubank.weihang.manage.member.pojo.CarAccountRecord;

/**
 * 车辆账户流水表（业务逻辑层接口）
 * @author YangWei
 *
 */
public interface ICarAccountRecordService {

    int deleteByPrimaryKey(String recordId);

    int insert(CarAccountRecord record);

    CarAccountRecord selectByPrimaryKey(String recordId);

    List<CarAccountRecord> selectAll();

    int updateByPrimaryKey(CarAccountRecord record);

}
