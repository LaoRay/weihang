package cn.com.clubank.weihang.common.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import cn.com.clubank.weihang.common.util.DateUtil;
import cn.com.clubank.weihang.manage.member.dao.CouponListMapper;
import cn.com.clubank.weihang.manage.product.service.CommentService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EachDayZeroTask extends QuartzJobBean {

	@Autowired
	private CouponListMapper couponListMapper;
	@Autowired
	private CommentService commentService;

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		log.info("每天零点执行定时任务【everyDayZeroPoint】，时间：{}", DateUtil.getCurrentTime());

		// 检查优惠券是否过期
		couponListMapper.updateCouponStatus();

		// 超过7天未评论订单自动5星好评
		commentService.automaticComment();
	}
}
