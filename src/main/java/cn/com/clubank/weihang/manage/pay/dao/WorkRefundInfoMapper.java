package cn.com.clubank.weihang.manage.pay.dao;

import cn.com.clubank.weihang.manage.pay.pojo.WorkRefundInfo;
import java.util.List;

public interface WorkRefundInfoMapper {
    int deleteByPrimaryKey(String wriId);

    int insert(WorkRefundInfo record);

    WorkRefundInfo selectByPrimaryKey(String wriId);

    List<WorkRefundInfo> selectAll();

    int updateByPrimaryKey(WorkRefundInfo record);
}