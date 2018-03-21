package cn.com.clubank.weihang.common.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MSValue {

	private String MoblieNo;

	private String[] MSText;

	private Integer MSType;
}
