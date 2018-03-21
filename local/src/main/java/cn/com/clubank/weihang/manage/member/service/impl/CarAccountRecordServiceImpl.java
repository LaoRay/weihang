package cn.com.clubank.weihang.manage.member.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.manage.member.dao.CarAccountRecordMapper;
import cn.com.clubank.weihang.manage.member.pojo.CarAccountRecord;
import cn.com.clubank.weihang.manage.member.service.ICarAccountRecordService;

/**
 * 券兑换信息
 * @author YangWei
 *
 */
@Service
public class CarAccountRecordServiceImpl implements ICarAccountRecordService {

	@Resource
	private CarAccountRecordMapper carAccountRecordMapper;

	@Override
	public int deleteByPrimaryKey(String recordId) {
		
		return carAccountRecordMapper.deleteByPrimaryKey(recordId);
	}

	@Override
	public int insert(CarAccountRecord record) {
		
		return carAccountRecordMapper.insert(record);
	}

	@Override
	public CarAccountRecord selectByPrimaryKey(String recordId) {
		
		return carAccountRecordMapper.selectByPrimaryKey(recordId);
	}

	@Override
	public List<CarAccountRecord> selectAll() {
		
		return carAccountRecordMapper.selectAll();
	}

	@Override
	public int updateByPrimaryKey(CarAccountRecord record) {
		
		return carAccountRecordMapper.updateByPrimaryKey(record);
	}

	

}
