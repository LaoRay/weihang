package cn.com.clubank.weihang.common.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import cn.com.clubank.weihang.common.util.DateUtil;
import cn.com.clubank.weihang.manage.bespeak.service.IReservationService;
import cn.com.clubank.weihang.manage.insurance.service.InsuranceService;
import cn.com.clubank.weihang.manage.product.dao.MallAdvertMapper;
import cn.com.clubank.weihang.manage.product.service.MallActivityService;
import cn.com.clubank.weihang.manage.product.service.OrderService;
import cn.com.clubank.weihang.manage.special.service.ISpecialOrderService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EachHourTask extends QuartzJobBean {

	@Autowired
	private OrderService orderService;
	@Autowired
	private MallActivityService activityService;
	@Autowired
	private MallAdvertMapper advertMapper;
	@Autowired
	private IReservationService iReservationService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private ISpecialOrderService iSpecialOrderService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("每小时执行定时任务【eachOneHour】，时间：{}", DateUtil.getCurrentTime());

		// 处理活动及活动商品
		activityService.handleActivityMall();

		// 定时开启、关闭广告
		advertMapper.handleMallAdvert();

		// 订单处理，三天内未支付订单置为已失效，已发货订单十天内未确认收货系统自动确认收货
		orderService.updateOrderListStatus();

		// 处理保单状态(待付款状态三天内未支付，订单状态修改为已取消，已发货保单十天内未确认，订单状态变更为已完成)
		insuranceService.handleInsuranceOrderStatus();
		
		// 处理特殊订单状态(待付款状态三天内未支付，订单状态修改为已取消，已发货保单十天内未确认，订单状态变更为已完成)
		iSpecialOrderService.handleSpecialOrderStatus();

		// 处理过期的预约单
		iReservationService.handleTimeOut();

		// 处理商品订单的退款（申请退款24小时候之后自动原路退款）
		orderService.timedOrderConfirmRefund();
	}
}
