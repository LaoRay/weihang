package cn.com.clubank.weihang.common.util;

/**
 * 结果代码
 * @author YangWei
 *
 */
public enum ResultCode {
	
	/**
	 * 成功
	 */
	SUCC(1),
	
	/**
	 * 失败
	 */
	FAIL(-1),
	
	/**
	 * 验证码已过期
	 */
	CODE_DUE(-2),
	
	/**
	 * 验证码错误
	 */
	CODE_ERROR(-3),
	
	/**
	 * 用户已登录
	 */
	USER_HAS_LOGIN(-10),
	
	/**
	 * 鉴权失败:用户名或密码错误
	 */
	AUTH_FAIL(-100),
	/**
	 * token已失效
	 */
	AUTH_TOKEN_INVALID(-110),
	
	/**
	 * 参数错误
	 */
	PARAM_ERROR(-200),
	/**
	 * 密码错误
	 */
	PASSWORD_ERROR(-201),
	
	/**
	 * 数据已存在
	 */
	DATA_EXIST(-260),
	/**
	 * 数据不存在
	 */
	DATA_NOEXIST(-261),
	
	/**
	 * 数据库SQL执行错误
	 */
	DB_EXECUTE_ERROR(-300),
	/**
	 * 数据库查询为空
	 */
	DB_QUERY_EMPTY(-301),
	
	/**
	 * 服务器内部错误
	 */
	SERVER_ERROR(-500),
	
	/**
	 * 未知
	 */
	UNKOWN(-999);
	
	private ResultCode(int value) {
		this.value = value;
	}
	
	public static boolean isSucc(String code) {
		return SUCC.getValueS().equals(code);
	}
	
	public int getValue() {
		return this.value;
	}
	
	public String getValueS() {
		return String.valueOf(this.value);
	}
	
	private int value;
}
