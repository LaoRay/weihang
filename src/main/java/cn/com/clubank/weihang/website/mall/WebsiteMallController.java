package cn.com.clubank.weihang.website.mall;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.common.util.CommonResult;
import cn.com.clubank.weihang.manage.bespeak.service.IReservationService;
import cn.com.clubank.weihang.manage.member.service.ICouponService;
import cn.com.clubank.weihang.manage.member.service.IntegralService;
import cn.com.clubank.weihang.manage.news.service.IBaseNewsService;
import cn.com.clubank.weihang.manage.product.service.CommentService;
import cn.com.clubank.weihang.manage.product.service.IProductReadLogService;
import cn.com.clubank.weihang.manage.product.service.MallActivityGoodsService;
import cn.com.clubank.weihang.manage.product.service.MallActivityService;
import cn.com.clubank.weihang.manage.product.service.OrderService;
import cn.com.clubank.weihang.manage.product.service.ProductFavouriteService;
import cn.com.clubank.weihang.manage.product.service.ProductInfoService;
import cn.com.clubank.weihang.manage.product.service.ProductService;
import cn.com.clubank.weihang.manage.product.service.ShoppingCartService;
import cn.com.clubank.weihang.manage.repair.service.IWorkRepairService;

/**
 * pc商城
 * 
 * @author LeiQY
 *
 */
@Controller
public class WebsiteMallController {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductInfoService productInfoService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private MallActivityGoodsService mallActivityGoodsService;
	@Autowired
	private MallActivityService mallActivityService;
	@Autowired
	private IProductReadLogService productReadLogService;
	@Autowired
	private IntegralService integralService;
	@Autowired
	private ICouponService couponService;
	@Autowired
	private IBaseNewsService baseNewsService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private IWorkRepairService iWorkRepairService;
	@Autowired
	private IReservationService reservationService;
	@Autowired
	private ShoppingCartService shoppingCartService;
	@Autowired
	private ProductFavouriteService productFavouriteService;

	/**
	 * 首页搜索--按照产品名称模糊查询产品列表--pc
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/websiteFindListByProductName", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteFindListByProductName(@RequestBody JSONObject param) {
		return productService.websiteFindListByProductName(param.getInteger("pageIndex"), param.getInteger("pageSize"),
				param.getString("productName"));
	}

	/**
	 * 根据分类查询商品列表
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/websiteFindProductListByCategoryId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteFindProductListByCategoryId(@RequestBody JSONObject param) {
		return productInfoService.websiteFindProductListByCategoryId(param.getString("categoryId"),
				param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 根据产品id查询评价列表
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/websiteFindCommentListByProductId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteFindCommentListByProductId(@RequestBody JSONObject param) {
		return commentService.websiteFindCommentListByProductId(param.getString("productId"),
				param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 根据产品id查询评价统计
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/websiteFindCommentCountByProductId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteFindCommentCountByProductId(@RequestBody JSONObject param) {
		return commentService.websiteFindCommentCountByProductId(param.getString("productId"));
	}

	/**
	 * 获得当前活动的所有商品并分页（限时抢购点击更多商品）
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/websiteFindActivityUnderAllGoods", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findActivityUnderAllGoods(@RequestBody JSONObject param) {
		return mallActivityGoodsService.selectActivityGoodsAndPagedByActivityId(param.getString("activityId"),
				param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 获得所有活动及商品（活动商品点击更多）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/websiteFindAllActivityAndGoods", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult findAllActivityGoods() {
		return mallActivityService.findActivityAndProduct();
	}

	/**
	 * 获得更多浏览记录
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/websiteFindMoreReadRecords", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteFindMoreReadRecords(@RequestBody JSONObject param) {
		return productReadLogService.websiteFindMoreReadRecords(param.getString("customerId"),
				param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 获得四种优惠券
	 * 
	 * @return
	 */
	@RequestMapping(value = "/websiteFindCouponList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteFindCouponList() {
		return integralService.websiteFindCouponList();
	}

	/**
	 * 获得更多优惠券
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/websiteFindMoreCouponListByType", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteFindCouponList(@RequestBody JSONObject param) {
		return integralService.websiteFindCouponList(param.getInteger("couponType"), param.getInteger("pageIndex"),
				param.getInteger("pageSize"));
	}

	/**
	 * 获得优惠券详情
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/websiteFindCouponDetail", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteFindCouponDetail(@RequestBody JSONObject param) {
		return couponService.websiteFindCouponDetail(param.getString("crId"));
	}

	/**
	 * 获得更多新闻资讯
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/websiteFindMoreNews", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteFindMoreNews(@RequestBody JSONObject param) {
		return baseNewsService.websiteFindMoreNews(param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 根据订单状态查询订单列表
	 * 
	 * @param customerId
	 * @param orderStatus
	 * @param orderCategory
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/websiteFindOrderListByOrderStatus", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteFindOrderListByOrderStatus(@RequestBody JSONObject param) {
		return orderService.websiteFindOrderListByOrderStatus(param.getString("customerId"),
				param.getInteger("orderStatus"), param.getInteger("orderCategory"), param.getInteger("pageIndex"),
				param.getInteger("pageSize"));
	}

	/**
	 * 查询预约列表
	 * 
	 * @param customerId
	 * @param reservationStatus
	 *            -1全部 2已预约(已确认) 4已完成 3已取消
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/websiteBespeakViewRecord", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteBespeakViewRecord(@RequestBody JSONObject param) {
		return reservationService.websiteBespeakViewRecord(param.getString("customerId"),
				param.getInteger("reservationStatus"), param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 客户查询维修单
	 * 
	 * @param type
	 *            全部-1，待支付1，待确认2，已完成3
	 * @return
	 */
	@RequestMapping(value = "/websiteFindRepairOrderList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteFindRepairOrderList(HttpServletRequest request, @RequestBody JSONObject param) {
		return iWorkRepairService.websiteFindRepairOrderList(request.getHeader("customerId"), param.getInteger("type"),
				param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 客户查询洗车单
	 * 
	 * @param type
	 *            全部-1，待支付1，待确认2，已完成3
	 * @return
	 */
	@RequestMapping(value = "/websiteFindWashOrderList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteFindWashOrderList(HttpServletRequest request, @RequestBody JSONObject param) {
		return iWorkRepairService.websiteFindWashOrderList(request.getHeader("customerId"), param.getInteger("type"),
				param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 查询购物车列表
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/websiteFindShoppingCartListByCustomerId", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteFindShoppingCartListByCustomerId(@RequestBody JSONObject param) {
		return shoppingCartService.websiteFindShoppingCartListByCustomerId(param.getString("customerId"),
				param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}

	/**
	 * 查询收藏列表
	 * 
	 * @param customerId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/websiteFindCollectionList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult websiteFindCollectionList(@RequestBody JSONObject param) {
		return productFavouriteService.websiteFindCollectionList(param.getString("customerId"),
				param.getInteger("pageIndex"), param.getInteger("pageSize"));
	}
}