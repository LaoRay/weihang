package cn.com.clubank.weihang.manage.product.dao;

import cn.com.clubank.weihang.manage.product.pojo.Comment;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface CommentMapper {
	int deleteByPrimaryKey(String commentId);

	int insert(Comment record);

	Comment selectByPrimaryKey(String commentId);

	List<Comment> selectAll();

	int updateByPrimaryKey(Comment record);

	int selectCommentCount(String productId);

	List<Comment> selectByOrderDetailId(String orderDetailId);

	List<Map<String, String>> selectByProductId(@Param("productId") String productId, @Param("start") Integer start,
			@Param("pageSize") Integer pageSize);

	Map<String, String> selectCountAssmentByProductId(String productId);
}