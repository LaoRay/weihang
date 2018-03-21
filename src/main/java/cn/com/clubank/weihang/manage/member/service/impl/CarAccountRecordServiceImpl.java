package cn.com.clubank.weihang.manage.member.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.manage.member.dao.CarAccountRecordMapper;
import cn.com.clubank.weihang.manage.member.dao.CarInfoMapper;
import cn.com.clubank.weihang.manage.member.pojo.CarAccountRecord;
import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.member.service.ICarAccountRecordService;
import cn.com.clubank.weihang.manage.message.pojo.MsgList;
import cn.com.clubank.weihang.manage.message.service.IMessageService;

@Service
public class CarAccountRecordServiceImpl implements ICarAccountRecordService {

	@Resource
	private CarAccountRecordMapper carAccountRecordMapper;

	@Resource
	private IMessageService iMessageService;
	
	@Autowired
	private CarInfoMapper carInfoMapper;
	
	@Override
	public CarAccountRecord save(CarAccountRecord info) {
		carAccountRecordMapper.insert(info);
		//会员信息
		CarInfo car = carInfoMapper.selectByPrimaryKey(info.getCarId());
		if (car != null) {
			// 推送金额变动通知
			String title = "";
			String content = "";
			if (info.getAccountType() == Constants.INT_1) {//金额变化类型：1.增加 2.减少
				//增加
				title = String.format("账户余额增加通知");
				content = String.format("您的账户【%s】余额增加【%s】，金额来源【%s】，最终账户余额【%s】！", car.getCarNo(), info.getDealAmount(), 
						info.getAccountPayTypeStr(), info.getFinishAmount());
			} else {
				title = String.format("账户余额减少通知");
				content = String.format("您的账户【%s】消费支出【%s】，最终账户余额【%s】！", car.getCarNo(), info.getDealAmount(), info.getFinishAmount());
			}
			iMessageService.pushMessage(new MsgList(MsgList.MSGTYPE_STATION, title, content, info.getAccountSource()), car.getCustomerId());
		}
		return info;
	}

}
