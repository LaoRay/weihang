package cn.com.clubank.weihang.manage.news.service;

import cn.com.clubank.weihang.manage.news.pojo.BaseNews;

/**
 * 新闻中心表（数据访问层接口）
 * @author Liangwl
 *
 */
public interface IBaseNewsService {

	/**
	 * 分页查询获得新闻图片、标题和主键
	 * @param pageIndex 页码下标
	 * @param pageSize  每页行数
	 * @return
	 */
	String selectNewsPicPaged(Integer pageIndex, Integer pageSize);
	
	/**
	 * 通过新闻主键获得新闻信息
	 * @param nId 新闻主键
	 * @return
	 */
	String selectNewsContent(String nId);
	
	/**
	 * 后台：通过新闻标题模糊查询,类型查询并分页
	 * @param title     新闻标题
	 * @param type      类型 1新闻 2公告
	 * @param pageIndex 页码下标
	 * @param pageSize  每页行数
	 * @return
	 */
	String selectByNewsTitlePaged(String title,Integer type,Integer pageIndex,Integer pageSize);
	
	/**
	 * 后台：通过新闻主键获得新闻信息
	 * @param nId 新闻ID
	 * @return
	 */
	String selectByNewsKey(String nId);
	
	/**
	 * 后台：新增或编辑新闻信息
	 * @param record 新闻中心表对象
	 * @return
	 */
	String insertOrEdit(BaseNews record);
	
	/**
	 * 后台：删除资讯
	 * @param nId
	 * @return
	 */
	String deleteNewsInfo(String nId);
}
