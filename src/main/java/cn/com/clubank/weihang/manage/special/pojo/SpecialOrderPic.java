package cn.com.clubank.weihang.manage.special.pojo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

import cn.com.clubank.weihang.common.annotation.WeihColumn;
import cn.com.clubank.weihang.common.annotation.WeihColumnCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 特殊订单图片库
 * @author Liangwl
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialOrderPic {
	
	/**
	 * 特殊订单图片id
	 */
	@WeihColumn(WeihColumnCode.UUID)
    private String picId;

    /**
     * 特殊订单id
     */
    private String specialId;

    /**
     * 图片路径
     */
    private String picUrl;

    /**
     * 备注
     */
    private String description;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @WeihColumn(WeihColumnCode.CREATE_TIME)
    private Date createDate;

    private String createUserId;

    private String createUserName;

}