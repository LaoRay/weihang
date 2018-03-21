package cn.com.clubank.weihang.common.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SMSModel {

	// 短信模板序列号
	public String serialCode;

	// 短信类型 1.验证码 2.通知
	public Integer type;
}
