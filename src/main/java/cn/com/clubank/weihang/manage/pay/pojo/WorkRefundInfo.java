package cn.com.clubank.weihang.manage.pay.pojo;

import java.math.BigDecimal;
import java.util.Date;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单退款流水表
 * 
 * @author YangWei
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkRefundInfo {
	
	@WeihColumn(WeihColumnCode.UUID)
    private String wriId;

	/**
	 * 支付流水表主键
	 */
    private String wpiId;

    @WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date refundDate;

    private BigDecimal refundTotal;

    private Integer refundWay;

    private String workId;

    private String transactionId;

    private Integer refundResult;

    private String description;

    @WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date createDate;

    private String createUserId;

    private String createUserName;

	public WorkRefundInfo(String wpiId, BigDecimal refundTotal, Integer refundWay, String workId, String transactionId) {
		super();
		this.wpiId = wpiId;
		this.refundTotal = refundTotal;
		this.refundWay = refundWay;
		this.workId = workId;
		this.transactionId = transactionId;
	}

}