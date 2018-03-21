package cn.com.clubank.weihang.restful.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.product.pojo.Comment;
import cn.com.clubank.weihang.manage.product.service.CommentService;

/**
 * 评价管理
 * 
 * @author LeiQY
 *
 */
@Controller
public class CommentController {

	@Autowired
	private CommentService commentService;

	/**
	 * 保存评价信息
	 * 
	 * @param comment
	 * @return
	 */
	@RequestMapping(value = "/productAddComment", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult addComment(@RequestBody Comment comment) {
		return commentService.addComment(comment);
	}

	/**
	 * 根据产品id查询评论列表
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/productFindCommentListByProductId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findCommentListByProductId(@RequestBody JSONObject json) {
		return commentService.findCommentListByProductId(json.getString("productId"), json.getInteger("pageIndex"),
				json.getInteger("pageSize"));
	}

	/**
	 * 审核评价
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/productReviewComment", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult reviewComment(@RequestBody JSONObject json) {
		return commentService.reviewComment(json.getString("commentId"), json.getString("adminId"),
				json.getInteger("reviewResult"));
	}
}
