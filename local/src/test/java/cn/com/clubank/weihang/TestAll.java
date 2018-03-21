package cn.com.clubank.weihang;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.clubank.weihang.manage.member.pojo.CarInfo;
import cn.com.clubank.weihang.manage.member.service.ICarInfoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext*.xml")
public class TestAll {

	@Autowired
	private ICarInfoService iCarInfoService;

	@Test
	public void testCarInfo() {

		CarInfo car = new CarInfo();
		car.setCarNo("sssddd");
		car.setDeleteMark(0);
		boolean b = iCarInfoService.insert(car);
		System.out.println(b);	
	}

	@Test
	public void testJson() {
		String[] s = { "123", "321" };
		Map<String, Object> map = new HashMap<>();
		map.put("s", s);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ss", s);
		String string = jsonObject.toJSONString();
		JSONObject jb = (JSONObject) JSONObject.parse(string);
		System.out.println(jb.get("ss").getClass());
	}

	@Test
	public void testJsonArray() {
		JSONArray array = new JSONArray();
		array.add("1");
		array.add("2");
		array.add("3");
		array.add("4");
		System.out.println(array.toJSONString());
		System.out.println(array.toString());
	}
	
	@Test
	public void testReg() {
		String str = "[1,2]";
		System.out.println(str.replaceAll("\\[|\\]", ""));
	}
}
