package cn.com.clubank.weihang.manage.news.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.clubank.weihang.manage.news.pojo.BaseNews;

public interface BaseNewsMapper {

    int deleteByPrimaryKey(String nId);

    int insert(BaseNews record);

    int insertSelective(BaseNews record);

    BaseNews selectByPrimaryKey(String nId);

    int updateByPrimaryKeySelective(BaseNews record);

    int updateByPrimaryKey(BaseNews record);
    
    /**
     * 分页查询获得新闻图片、标题和主键
     * @param start     起始下标
     * @param pageSize  每页行数
     * @return
     */
    List<BaseNews> selectNewsPaged(@Param("start") Integer start, @Param("pageSize") Integer pageSize);
    
    /**
     * 后台：通过新闻标题模糊查询,类型查询到的总条数
     * @param title 新闻标题
     * @param typeId 类型
     * @return
     */
    int selectByNewsTitleSum(@Param("title") String title,@Param("typeId") Integer type);
    
    /**
     * 后台：通过新闻标题模糊查询,类型查询并分页
     * @param title       新闻标题
     * @param typeId      类型
     * @param startIndex  起始索引
     * @param pageSize    每页行数
     * @return
     */
    List<BaseNews> selectByNewsTitlePaged(@Param("title") String title,@Param("typeId") Integer type,@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize);
}