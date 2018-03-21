package cn.com.clubank.weihang.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 异常捕获
 * @author YangWei
 *
 */
@Controller
@RequestMapping("/error")
public class ErrorController {

	@RequestMapping(value="/404", method = RequestMethod.GET)
	public ModelAndView resolveException(HttpServletRequest request,   
            HttpServletResponse response, Object handler, Exception ex) {
		return new ModelAndView("error/404");
	}
	
}
