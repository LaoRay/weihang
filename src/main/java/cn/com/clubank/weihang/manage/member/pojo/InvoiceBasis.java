package cn.com.clubank.weihang.manage.member.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 增票资质表
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceBasis {
	
	/**
	 * 增票资质主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String invoiceId;

    /**
     * 客户ID
     */
    private String customerId;

    /**
     * 单位名称
     */
    private String companyAme;

    /**
     * 纳税人识别号
     */
    private String identificationCode;

    /**
     * 注册地址
     */
    private String registerAddress;

    /**
     * 注册电话
     */
    private String registerTelephone;

    /**
     * 开户银行
     */
    private String openingBank;

    /**
     * 银行账户
     */
    private String bankId;

    /**
     * 是否默认
     */
    private Boolean isDefault=false;

    /**
     * 删除标记
     */
    private Integer deleteMark=0;

    /**
     * 有效标志
     */
    private Integer enabledMark;

    /**
     * 备注
     */
    private String description;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date createDate;

    private String createUserId;

    private String createUserName;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @WeihColumn(WeihColumnCode.UPDATE_TIME)
    private Date modifyDate;

    private String modifyUserId;

    private String modifyUserName;
}