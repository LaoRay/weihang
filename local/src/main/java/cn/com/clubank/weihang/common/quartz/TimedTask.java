package cn.com.clubank.weihang.common.quartz;

import org.springframework.beans.factory.annotation.Autowired;

import cn.com.clubank.weihang.common.util.DateUtil;
import cn.com.clubank.weihang.manage.bespeak.service.IReservationService;
import cn.com.clubank.weihang.manage.member.dao.CouponListMapper;
import cn.com.clubank.weihang.manage.product.dao.MallActivityMapper;
import cn.com.clubank.weihang.manage.product.dao.MallAdvertMapper;
import cn.com.clubank.weihang.manage.product.service.CommentService;
import cn.com.clubank.weihang.manage.product.service.OrderService;
import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务
 * 
 * @author LeiQY
 *
 */
@Slf4j
public class TimedTask {

	@Autowired
	private CouponListMapper couponListMapper;
	@Autowired
	private OrderService orderService;
	@Autowired
	private MallActivityMapper activityMapper;
	@Autowired
	private MallAdvertMapper advertMapper;
	@Autowired
	private IReservationService iReservationService;
	@Autowired
	private CommentService commentService;

	/**
	 * 每天零点定时任务
	 */
	public void everyDayZeroPoint() {
		log.info("每天零点执行定时任务【everyDayZeroPoint】，时间：{}", DateUtil.getCurrentTime());

		// 检查优惠券是否过期
		couponListMapper.updateCouponStatus();

		// 定时开启、关闭广告
		advertMapper.handleMallAdvert();

		// 超过7天未评论订单自动5星好评
		commentService.automaticComment();
	}

	/**
	 * 每小时定时任务
	 */
	public void eachOneHour() {
		log.info("每小时执行定时任务【eachOneHour】，时间：{}", DateUtil.getCurrentTime());

		// 定期检查活动是否开始、结束
		activityMapper.handleActivityMall();

		// 检查订单是否支付，三天内未支付将订单置为已失效
		orderService.updateOrderListStatus();

		// 处理过期的预约单
		iReservationService.handleTimeOut();
	}
}
