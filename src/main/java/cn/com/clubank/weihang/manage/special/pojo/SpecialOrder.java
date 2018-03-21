package cn.com.clubank.weihang.manage.special.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 特殊订单
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialOrder {
	
	/**
	 * 特殊订单id
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String specialId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 客户id
     */
    private String customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品规格
     */
    private String productAttr;

    /**
     * 商品描述
     */
    private String productDesc;

    /**
     * 商品厂商类型：1-原厂，2-第三方厂商
     */
    private Integer factoryType = 1;

    /**
     * 第三方厂商名称
     */
    private String factoryName;

    /**
     * 期望到货时间
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date expectTotime;

    /**
     * 预计到货时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date estimateTotime;

    /**
     * 状态：1已申请 2待付款 3待收货 4已完成 5已驳回 6已取消
     */
    private Integer status = 1;

    /**
     * 发货时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;

    /**
     * 配送地址
     */
    private String deliveryAddress;

    /**
     * 物流类型：1自提 2邮寄
     */
    private Integer deliveryType;

    /**
     * 物流状态：1待发货 2已发货 3已签收
     */
    private Integer deliveryStatus = 1;

    /**
     * 实付金额
     */
    private BigDecimal orderPayAmount = new BigDecimal(0);

    /**
     * 订单金额
     */
    private BigDecimal orderAmount = new BigDecimal(0);

    /**
     * 付款时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    /**
     * 支付类型：1账户 2支付宝 3微信
     */
    private Integer payType;

    /**
     * 驳回原因
     */
    private String turnDownDesc;

    /**
     * 备注
     */
    private String description;

    @WeihColumn(WeihColumnCode.CREATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    private String createUserId;

    private String createUserName;

    @WeihColumn(WeihColumnCode.UPDATE_TIME)
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date modifyDate;

    private String modifyUserId;

    private String modifyUserName;
    
    public static String getOrderStatus(Integer status){
    	if(status == 1){
    		return "已申请";    
    	}else if(status == 2){
    		return "待付款";
    	}else if(status == 3){
    		return "待收货";
    	}else if(status == 4){
    		return "已完成";
    	}else if(status == 5){
    		return "已驳回";
    	}else if(status == 6){
    		return "已取消";
    	}
    	return null;
    }
    
    public static String getPayType(Integer payType){
		if(payType==1){
			return "账户";
		}else if(payType==2){
			return "支付宝";
		}else if(payType==3){
			return "微信";
		}
		return null;
	}

}