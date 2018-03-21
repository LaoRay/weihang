package cn.com.clubank.weihang.manage.systemSettings.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.clubank.weihang.common.redis.JedisClient;
import cn.com.clubank.weihang.manage.systemSettings.dao.BaseCodeRuleMapper;
import cn.com.clubank.weihang.manage.systemSettings.service.BaseCodeRuleService;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * 编号规则项目
 * 
 * @author YangWei
 *
 */
@Slf4j
@Service
public class BaseCodeRuleServiceImpl implements BaseCodeRuleService {

	@Resource
	private BaseCodeRuleMapper baseCodeRuleMapper;
	@Autowired
	private JedisClient jedisClient;

	@Override
	public synchronized String getCode(String itemCode) {
		// TODO 统一生成
		String today = "BASECODE:" + new SimpleDateFormat("yyyyMMdd").format(new Date());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = "BASECODE:" + new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
		String result = itemCode + new SimpleDateFormat("yyMMddHHmm").format(new Date());
		Boolean exists = jedisClient.exists(today);
		Jedis jedis = null;
		try {
			if (!exists) {
				jedisClient.del(yesterday);
				jedisClient.setnx(today, "0");
			}
			jedis = jedisClient.getJedis();
			jedisClient.watch(today);
			Transaction tx = jedis.multi(); // 开启事务
			tx.incr(today);
			List<Object> list = tx.exec(); // 提交事务
			String code = jedisClient.get(today);
			if (list != null && list.size() > 0 && StringUtils.isNotBlank(code)) {
				if (code.length() < 4) {
					for (int i = 0; i < 4 - code.length(); i++) {
						result += "0";
					}
				}
				result += code;
			} else {
				result = itemCode + new SimpleDateFormat("yyDDDHHmmssSSS").format(new Date());
			}
		} catch (Exception e) {
			log.error("生成订单号异常", e);
			result = itemCode + new SimpleDateFormat("yyDDDHHmmssSSS").format(new Date());
		} finally {
			jedis.close();
		}
		log.info("生成单号：{}", result);
		return result;
	}

}
