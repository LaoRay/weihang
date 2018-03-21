package cn.com.clubank.weihang.manage.product.service;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.Comment;

/**
 * 评价管理
 * 
 * @author LeiQY
 *
 */
public interface CommentService {

	/**
	 * 保存评价信息
	 * 
	 * @param comment
	 * @return
	 */
	CommonResult addComment(Comment comment);

	/**
	 * 根据产品id查询评论列表
	 * 
	 * @param productId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	CommonResult findCommentListByProductId(String productId, Integer pageIndex, Integer pageSize);

	/**
	 * 审核评价
	 * 
	 * @param commentId
	 * @param adminId
	 * @param reviewResult
	 * @return
	 */
	CommonResult reviewComment(String commentId, String adminId, Integer reviewResult);

	/**
	 * 根据订单详情id查询评论
	 * 
	 * @param orderDetailId
	 * @return
	 */
	CommonResult findCommentByOrderDetailId(String orderDetailId);

	/**
	 * 定时任务：超过7天未评论订单自动5星好评
	 */
	void automaticComment();

	/**
	 * pc：根据产品id查询评论列表
	 * 
	 * @param productId
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	CommonResult websiteFindCommentListByProductId(String productId, Integer pageIndex, Integer pageSize);

	/**
	 * pc：根据产品id查询评价统计
	 * 
	 * @param productId
	 * @return
	 */
	CommonResult websiteFindCommentCountByProductId(String productId);
}
