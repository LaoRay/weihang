package cn.com.clubank.weihang.manage.systemSettings.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import cn.com.clubank.weihang.common.util.CommonResult;

/**
 * 图片处理
 * 
 * @author YangWei
 *
 */
public interface ImageService {

	/**
	 * 上传图片
	 * @param file
	 * @param fileType
	 * @return
	 */
	public CommonResult uploadImage(MultipartFile file, Integer fileType);
	
	/**
	 * 下载图片
	 * @param file
	 * @param fileType
	 * @return
	 */
	public void downImage(HttpServletResponse response, String path);
}
