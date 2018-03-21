package cn.com.clubank.weihang.common.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MSKey {

	private String UserId;
	
	private String USerName;
	
	private String ClientID;
	
	private String SerialNumber;
	
	private String Key;
}
