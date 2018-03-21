package cn.com.clubank.weihang.restful.systemSettings;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.systemSettings.service.ImageService;

/**
 * 图片处理
 * @author YangWei
 *
 */
@Controller
public class ImageController {

	@Resource
	private ImageService imageService;

	/**
	 * 图片上传
	 * 
	 * @return
	 */
	@RequestMapping(value = "/imageUpload", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult imageUpload(@RequestParam(value = "file") MultipartFile file, Integer fileType) {
		return imageService.uploadImage(file, fileType);
	}
	
	/**
	 * 图片下载
	 * 
	 * @return
	 */
	@RequestMapping(value = "/image/{type}/{date}/{id}", method = RequestMethod.GET)
	public void imageDown(HttpServletRequest request, HttpServletResponse response) {
		imageService.downImage(response, request.getServletPath());
	}
	
}
