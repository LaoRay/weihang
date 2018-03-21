package cn.com.clubank.weihang.common.util;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口响应公共类
 * 
 * @author LeiQY
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult implements Serializable {

	private static final long serialVersionUID = 1L;

	// 响应业务状态
	private Integer apiStatus;

	// 响应提示消息
	private String msg;

	// 响应中的数据
	private Object data;
	
	public static CommonResult result(Object data) {
		return new CommonResult(data);
	}

	public static CommonResult result(Integer apiStatus, String msg) {
		return new CommonResult(apiStatus, msg);
	}

	public static CommonResult result(Integer apiStatus, String msg, Object data) {
		return new CommonResult(apiStatus, msg, data);
	}

	public CommonResult(Object data) {
		this.apiStatus = 1;
		this.msg = "OK";
		this.data = data;
	}

	public CommonResult(Integer apiStatus, String msg) {
		this.apiStatus = apiStatus;
		this.msg = msg;
	}
}
