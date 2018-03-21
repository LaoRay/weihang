package cn.com.clubank.weihang.manage.product.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.common.util.Constants;
import cn.com.clubank.weihang.common.util.PageObject;
import cn.com.clubank.weihang.common.util.ResultCode;
import cn.com.clubank.weihang.manage.product.dao.CommentMapper;
import cn.com.clubank.weihang.manage.product.dao.OrderDetailMapper;
import cn.com.clubank.weihang.manage.product.dao.OrderListMapper;
import cn.com.clubank.weihang.manage.product.pojo.Comment;
import cn.com.clubank.weihang.manage.product.pojo.OrderDetail;
import cn.com.clubank.weihang.manage.product.pojo.OrderList;
import cn.com.clubank.weihang.manage.product.service.CommentService;

/**
 * 评价管理
 * 
 * @author LeiQY
 *
 */
@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private OrderListMapper orderListMapper;
	@Autowired
	private OrderDetailMapper orderDetailMapper;

	/**
	 * 保存评价信息
	 */
	@Override
	public CommonResult addComment(Comment comment) {
		// 评分为5则为好评
		if (comment.getScore() == Constants.INT_5) {
			comment.setAssessment(Comment.HIGH_COMMENT);
		}
		// 评分为1则为差评
		else if (comment.getScore() == Constants.INT_1) {
			comment.setAssessment(Comment.LOW_COMMENT);
		}
		// 介于1和5之间则为中评
		else {
			comment.setAssessment(Comment.MEDIUM_COMMENT);
		}
		// 保存评价信息
		commentMapper.insert(comment);
		// 不是追加评论才去更新订单详情和订单信息
		if (StringUtils.isBlank(comment.getParentId())) {
			// 设置订单详情表是否已评论为true
			String odiId = comment.getOrderDetailId();
			OrderDetail orderDetail = orderDetailMapper.selectByPrimaryKey(odiId);
			orderDetail.setIsComment(true);
			orderDetailMapper.updateByPrimaryKey(orderDetail);
			// 查看该订单下订单详情是否已全部已评价
			String orderId = orderDetail.getOrderId();
			List<OrderDetail> detailList = orderDetailMapper.selectListByOrderId(orderId);
			for (OrderDetail detail : detailList) {
				Boolean isComment = detail.getIsComment();
				// 如果有未评价的商品，则不更新订单状态
				if (!isComment) {
					return CommonResult.result(ResultCode.SUCC.getValue(), "保存评价信息成功");
				}
			}
			// 如果全部已评价，则更新订单为已完成
			OrderList orderList = orderListMapper.selectByPrimaryKey(orderId);
			orderList.setOrderStatus(Constants.INT_4);
			orderListMapper.updateByPrimaryKey(orderList);
		}
		return CommonResult.result(ResultCode.SUCC.getValue(), "保存评价信息成功");
	}

	/**
	 * 根据产品id查询评论列表及评论统计
	 */
	@Override
	public CommonResult findCommentListByProductId(String productId, Integer pageIndex, Integer pageSize) {
		// 根据产品id及时间倒序分页查询评论列表
		List<Map<String, String>> commentList = commentMapper.selectByProductId(productId,
				PageObject.getStart(pageIndex, pageSize), pageSize);
		// 获取评价统计信息
		Map<String, String> commentCount = commentMapper.selectCountAssmentByProductId(productId);
		Integer totalComment = commentCount.containsKey("totalComment") ? Integer.parseInt(commentCount.get("totalComment")) : 0;
		Integer totalScore = commentCount.containsKey("totalScore") ? Integer.parseInt(commentCount.get("totalScore")) : 0;
		commentCount.put("highCommentRate",
				totalComment == 0 ? "0" : String.format("%.0f", (double) totalScore * 100 / (totalComment * 5)) + "%");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("commentList", commentList);
		map.put("commentCount", commentCount);
		return CommonResult.result(ResultCode.SUCC.getValue(), "查询评价列表成功", map);
	}

	/**
	 * 审核评价
	 */
	@Override
	public CommonResult reviewComment(String commentId, String adminId, Integer reviewResult) {
		Comment comment = commentMapper.selectByPrimaryKey(commentId);
		// 审核人id
		comment.setAdminId(adminId);
		// 审核状态 1待审核 2已审核
		comment.setReviewStatus(Constants.INT_2);
		// 审核结果
		comment.setReviewResult(reviewResult);
		commentMapper.updateByPrimaryKey(comment);
		return CommonResult.result(ResultCode.SUCC.getValue(), "审核评价成功");
	}

	/**
	 * 定时任务：超过7天未评论订单自动5星好评
	 */
	@Override
	public void automaticComment() {
		List<Map<String, String>> list = orderDetailMapper.selectNotCommentOrder();
		if (list != null && list.size() > 0) {
			for (Map<String, String> map : list) {
				// 自动新增五星好评
				Comment comment = new Comment();
				comment.setOrderDetailId(map.get("orderDetailId"));
				comment.setCustomerId(map.get("customerId"));
				comment.setProductId(map.get("productId"));
				comment.setScore(Constants.INT_5);
				comment.setAssessment(Comment.HIGH_COMMENT);
				comment.setContent("好评");
				commentMapper.insert(comment);
				// 更新订单详情是否已评论为true
				OrderDetail orderDetail = orderDetailMapper.selectByPrimaryKey(map.get("orderDetailId"));
				orderDetail.setIsComment(true);
				orderDetailMapper.updateByPrimaryKey(orderDetail);
				// 查看该订单下订单详情是否已全部已评价
				String orderId = orderDetail.getOrderId();
				List<OrderDetail> detailList = orderDetailMapper.selectListByOrderId(orderId);
				for (OrderDetail detail : detailList) {
					Boolean isComment = detail.getIsComment();
					// 如果有未评价的商品，则不更新订单状态
					if (!isComment) {
						return;
					}
				}
				// 如果全部已评价，则更新订单为已完成
				OrderList orderList = orderListMapper.selectByPrimaryKey(orderId);
				orderList.setOrderStatus(Constants.INT_4);
				orderListMapper.updateByPrimaryKey(orderList);
			}
		}
	}
}
