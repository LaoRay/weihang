package cn.com.clubank.weihang.poiExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.com.clubank.weihang.common.util.ImportExcelUtil;
import cn.com.clubank.weihang.manage.member.dao.CustomerInfoMapper;
import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.member.pojo.CustomerInfo;
import cn.com.clubank.weihang.manage.member.service.ICarInfoService;
import cn.com.clubank.weihang.manage.member.service.ICustomerInfoService;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext*.xml")
public class ImportExcel {
	
	@Resource
	private ICustomerInfoService iCustomerInfoService;
	@Resource
	private ICarInfoService iCarInfoService;
	@Resource
	private CustomerInfoMapper customerInfoMapper;
	
	@Test
	public void excelImportMysql() throws Exception{
		String path="E:/威航/会员.xls";
		File file=new File(path);
		List<List<Object>> listob = null; 
		InputStream in = new FileInputStream(path);
		listob = new ImportExcelUtil().getBankListByExcel(in, file.getName());
		// 该处调用service相应方法进行数据保存到数据库中
        for (int i = 0; i < listob.size(); i++) {  
            List<Object> lo = listob.get(i);  
            CustomerInfo customer = new CustomerInfo();  
            customer.setMobile(String.valueOf(lo.get(4)));//手机号
            customer.setPassword(String.valueOf(lo.get(4)).substring(5));//密码
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            customer.setCreateDate(sdf.parse((String) lo.get(3)));//注册时间
        
            iCustomerInfoService.insertOrUpdateCustomer(customer);
            //System.out.println("打印信息-->会员手机:" + customer.getMobile() + "   登录密码：" + customer.getPassword() + "   注册时间：" + customer.getCreateDate());
            
            CustomerInfo customerInfo=customerInfoMapper.selectByMobile(String.valueOf(lo.get(4)));
            
            CarInfo car=new CarInfo();
            car.setCustomerId(customerInfo.getCId());//会员ID
            car.setCarNo((String) lo.get(1));//车牌号
            car.setAccount(new BigDecimal((String) lo.get(2)));//账户
            car.setGiveCount(Integer.parseInt((String) lo.get(5)));//接送次数
            car.setHelpCount(Integer.parseInt((String) lo.get(6)));//救援次数
            //通过会员等级确定账户权益ID
            
            iCarInfoService.insertOrUpdateCar(car);
            //System.out.println("车牌号："+car.getCarNo()+"  账户："+car.getAccount()+"   接送次数："+car.getGiveCount()+"   救援次数："+car.getHelpCount());
        }
		
	}
}
