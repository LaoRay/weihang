package cn.com.clubank.weihang.manage.member.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车辆信息表
 * @author YangWei
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarInfo {

	/**
	 * 车辆主键
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String carId;

	/**
	 * 客户关联ID
	 */
    private String customerId;

    /**
     * 车牌号码
     */
    private String carNo;

    /**
     * 排序码
     */
    private Integer sort;

    /**
     * 车辆品牌ID
     */
    private String carBrandId;

    /**
     * 车型信息
     */
    private String carTypeInfo;

    /**
     * 是否默认车辆
     */
    private Boolean isDefault = false;
    
    /**
     * 是否虚拟车辆
     */
    private Boolean isVirtual = false;

	/**
     * 删除标记
     */
    private Integer deleteMark = 0;

    /**
     * 有效标志
     */
    private Integer enabledMark = 1;

    /**
     * 备注
     */
    private String description;

    /**
     * 创建日期
     */
    @WeihColumn(WeihColumnCode.CREATE_TIME)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
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
     * 修改日期
     */
    @WeihColumn(WeihColumnCode.UPDATE_TIME)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date modifyDate;

    /**
     * 修改用户主键
     */
    private String modifyUserId;

    /**
     * 修改用户名
     */
    private String modifyUserName;

    /**
     * 账户
     */
    private BigDecimal account = new BigDecimal(0.00);

    /**
     * 车架号
     */
    private String carVin;

    /**
     * 接送次数
     */
    private Integer giveCount=0;

    /**
     * 救援次数
     */
    private Integer helpCount=0;

    /**
     * 集团组关联ID
     */
    private String groupId;
  
    /**
     * 充值总额
     */
    private BigDecimal accountRechargeTotal=new BigDecimal(0.00);
  
    /**
     * 账户权益ID
     */
    private String abId;

    /**
     * 自定义属性：所属客户
     */
    private String customerName;
    
    /**
     * 自定义属性：所属客户手机
     */
    private String mobile;
    
    /**
     * 车辆品牌
     */
    private String carBrand;
    
    /**
     * 车辆年限
     */
    private String years;
    
    /**
     * 发动机排量
     */
    private String exhaustAmount;
    
    /**
     * 发动机号
     */
    private String engineNumber;
    
}