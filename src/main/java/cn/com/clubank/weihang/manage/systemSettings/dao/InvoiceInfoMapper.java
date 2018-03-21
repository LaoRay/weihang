package cn.com.clubank.weihang.manage.systemSettings.dao;

import cn.com.clubank.weihang.manage.systemSettings.pojo.InvoiceInfo;
import java.util.List;

public interface InvoiceInfoMapper {
    int deleteByPrimaryKey(String invoiceId);

    int insert(InvoiceInfo record);

    InvoiceInfo selectByPrimaryKey(String invoiceId);

    List<InvoiceInfo> selectAll();

    int updateByPrimaryKey(InvoiceInfo record);
}