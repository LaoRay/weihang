package cn.com.clubank.weihang.manage.member.dao;

import java.util.List;

import cn.com.clubank.weihang.manage.member.pojo.InvoiceBasis;

public interface InvoiceBasisMapper {

    int deleteByPrimaryKey(String invoiceId);

    int insert(InvoiceBasis record);

    int insertSelective(InvoiceBasis record);

    InvoiceBasis selectByPrimaryKey(String invoiceId);
    
    /**
     * 通过客户ID获得客户的增票资质列表
     * @param customerId 客户ID
     * @return
     */
    List<InvoiceBasis> selectBycustomerId(String customerId);
    
    /**
     * 通过纳税人识别号查询到的条数
     * @param identificationCode 纳税人识别号
     * @return
     */
    int selectCountByIdentificationCode(String identificationCode);
    
    /**
     * 通过银行账户查询到的条数
     * @param bankId 银行账户
     * @return
     */
    int selectCountByBankId(String bankId);

    int updateByPrimaryKeySelective(InvoiceBasis record);

    int updateByPrimaryKey(InvoiceBasis record);
    
    /**
	 * 更新用户默认增票资质
	 * 
	 * @param record
	 * @return
	 */
	int updateIsDefault(InvoiceBasis record);
	
	/**
	 * 软删除
	 * @param invoiceId 增票资质主键
	 * @return
	 */
	int softDeleteByPrimaryKey(String invoiceId);
}