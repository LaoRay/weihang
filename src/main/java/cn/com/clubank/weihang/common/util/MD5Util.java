package cn.com.clubank.weihang.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.PrivilegedAction;

/**
 * MD5加密解密util
 * 
 * @author YangWei
 *
 */
public class MD5Util {
	// 对文件进行MD5处理时，文件长度的最大允许值
	private static int maxMD5FileLength = 100000000;
	
	// 文件超过最大长度的默认MD5
	private static String defaultBigFileMD5 = "00000000000000000000000000000000";
	// 文件的默认初始MD5
	private static String defaultInitMD5 = "00000000000000000000000000000001";
	
	public static int getMaxMD5FileLength() {
		return maxMD5FileLength;
	}

	public static void setMaxMD5FileLength(int maxMD5FileLength) {
		MD5Util.maxMD5FileLength = maxMD5FileLength;
	}

	public static String getDefaultBigFileMD5() {
		return defaultBigFileMD5;
	}

	public static void setDefaultBigFileMD5(String defaultBigFileMD5) {
		MD5Util.defaultBigFileMD5 = defaultBigFileMD5;
	}

	public static String getDefaultInitMD5() {
		return defaultInitMD5;
	}

	public static void setDefaultInitMD5(String defaultInitMD5) {
		MD5Util.defaultInitMD5 = defaultInitMD5;
	}

	/***
	 * MD5加码 生成32位md5码
	 */
	public static String string2MD5(String inStr){
		MessageDigest md5 = null;
		try{
			md5 = MessageDigest.getInstance("MD5");
		}catch (Exception e){
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++){
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	/**
	 * 加密解密算法 执行一次加密，两次解密
	 */ 
	public static String convertMD5(String inStr){

		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++){
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;

	}
	
	/**
	 * 对文件进行MD5 
	 * by taolm
	 * @return
	 */
	public static String convertFileToMD5(String path) {
		
		String md5Value = null;  		
		FileInputStream in = null;
		MappedByteBuffer byteBuffer = null;
		
        try {
        	File file = new File(path);
        	if ( file.length() > maxMD5FileLength ) {
        		md5Value = defaultBigFileMD5;
        	} else {
	        	in = new FileInputStream(path);  
	        	byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());  
	        	
	        	MessageDigest md5 = MessageDigest.getInstance("MD5");  
	        	md5.update(byteBuffer);  
	        	BigInteger bi = new BigInteger(1, md5.digest());  
	        	md5Value = bi.toString(16); 
        	} 
        } catch ( Exception e ) {
        	md5Value = defaultInitMD5;  		
        	e.printStackTrace();
        } finally {
        	if ( byteBuffer.capacity() > 0 ) {
        		try
        		{
        			clean(byteBuffer);        			
        		}catch(Exception e)
        		{
        			e.printStackTrace();
        		}
        	}

        	if ( in != null )
        	{
        		try {
        			in.close(); 
        		} catch ( Exception e ) {
        			e.printStackTrace();
        		}
        	}
        }
    
        return md5Value;  
	}
	
	// 强制删除缓冲区
	public static void clean(final Object buffer) throws Exception {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			@SuppressWarnings("restriction")
			public Object run() {
				try {
					Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
					getCleanerMethod.setAccessible(true);
					sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
					cleaner.clean();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}

	 
	// 测试主函数
	public static void main(String args[]) {
		String s = convertFileToMD5("C:/test.txt");
		System.out.println(s);
	/*	FileInputStream in = null;
		try {
			in = new FileInputStream("C:/test.txt");
			in.close();
		} catch ( Exception e) {
			e.printStackTrace();
		}*/
	}
}
