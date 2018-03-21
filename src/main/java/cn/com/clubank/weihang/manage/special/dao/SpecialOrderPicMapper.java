package cn.com.clubank.weihang.manage.special.dao;

import java.util.List;

import cn.com.clubank.weihang.manage.special.pojo.SpecialOrderPic;

public interface SpecialOrderPicMapper {
    int deleteByPrimaryKey(String picId);

    int insert(SpecialOrderPic record);
    
    int insertBatch(List<SpecialOrderPic> list);

    int insertSelective(SpecialOrderPic record);

    SpecialOrderPic selectByPrimaryKey(String picId);

    int updateByPrimaryKeySelective(SpecialOrderPic record);

    int updateByPrimaryKey(SpecialOrderPic record);
    
    /**
     * 通过特殊订单id获得商品附件图
     * @param specialId
     * @return
     */
    List<SpecialOrderPic> selectBySpecialId(String specialId);
}