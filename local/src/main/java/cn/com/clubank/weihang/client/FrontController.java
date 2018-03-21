package cn.com.clubank.weihang.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 前端控制层
 * 
 * @author YangWei
 *
 */
@Controller
@RequestMapping(value="/client")
public class FrontController {
	
	/**
	 * 首页
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/index", method=RequestMethod.GET)
	public String index(Model model) {
		return "home/index";
	}
	
	/**
	 * 商品分类-列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/category/index", method=RequestMethod.GET)
	public String productCategory(Model model) {
		return "product/category/index";
	}
	
	/**
	 * 商品分类-编辑
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/category/edit", method=RequestMethod.GET)
	public String categoryEdit(Model model) {
		return "product/category/edit";
	}
	
	/**
	 * 品牌管理-列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/brand/index", method=RequestMethod.GET)
	public String productBrand(Model model) {
		return "product/brand/index";
	}
	
	/**
	 * 品牌管理-编辑
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/brand/edit", method=RequestMethod.GET)
	public String brandEdit(Model model) {
		return "product/brand/edit";
	}
	
	/**
	 * 商品规格-列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/attribute/index", method=RequestMethod.GET)
	public String productAttribute(Model model){
		return "product/attribute/index";
	}
	
	/**
	 * 品牌规格-编辑
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/attribute/edit", method=RequestMethod.GET)
	public String attributeEdit(Model model) {
		return "product/attribute/edit";
	}
	
	/**
	 * 商品管理-列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/product/index", method=RequestMethod.GET)
	public String productIndex(Model model) {
		return "product/list/index";
	}
	
	/**
	 * 订单管理-预约订单
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/reserveOrder/index", method=RequestMethod.GET)
	public String reserveOrderIndex(Model model) {
		return "order/reservation/index";
	}
	
	/**
	 * 服务订单-列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/serviceOrder/index", method=RequestMethod.GET)
	public String serviceOrder(Model model, @RequestParam(defaultValue="wx") String type) {
		model.addAttribute("type", type);
		return "order/service/index";
	}
	
	/**
	 * 服务订单-编辑
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/serviceOrder/edit", method=RequestMethod.GET)
	public String serviceOrderEdit(Model model) {
		return "order/service/edit";
	}
	
	
	/**
	 * 服务订单-编辑价格
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/serviceOrder/editPrice", method=RequestMethod.GET)
	public String serviceOrdereditPrice(Model model, String repairId) {
		model.addAttribute("repairId", repairId);
		return "order/service/editPrice";
	}
	
	/**
	 * 物料管理-列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/materials/index", method=RequestMethod.GET)
	public String materials(Model model) {
		return "materials/list/index";
	}
	
	/**
	 * 物料管理-编辑
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/materials/edit", method=RequestMethod.GET)
	public String materialsEdit(Model model,String wiId) {
		model.addAttribute("wiId", wiId);
		return "materials/list/edit";
	}
	
	/**
	 * 物料分类-列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/classify/index", method=RequestMethod.GET)
	public String materialClassify(Model model) {
		return "materials/classify/index";
	}

	/**
	 * 物料分类-编辑
	 */
	@RequestMapping(value="/classify/edit", method=RequestMethod.GET)
	public String classifyEdit(Model model,String wicId) {
		model.addAttribute("wicId",wicId);
		return "materials/classify/edit";
	}
	
}
