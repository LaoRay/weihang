package cn.com.clubank.weihang.manage.news.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新闻中心表
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseNews {
	/**
	 * 新闻主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String nId;

	/**
	 * 类型 1：新闻、2：公告
	 */
    private Integer typeId;

    /**
     * 标题
     */
    private String title;

    /**
     * 浏览量
     */
    private Integer pv=0;

    /**
     * 发布时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date releaseTime;

    /**
     * 创建日期
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date createDate;

    /**
     * 创建用户主键
     */
    private String createUserId;

    /**
     * 创建用户名
     */
    private String createUserName;

    /**
     * 大图
     */
    private String bigImg;

    /**
     * 小图
     */
    private String smallImg;

    /**
     * 新闻内容
     */
    private String content;

}