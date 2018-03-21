package cn.com.clubank.weihang.manage.insurance.dao;

import cn.com.clubank.weihang.manage.insurance.pojo.InsurancePic;
import java.util.List;

public interface InsurancePicMapper {
	int deleteByPrimaryKey(String picId);

	int insert(InsurancePic record);

	InsurancePic selectByPrimaryKey(String picId);

	List<InsurancePic> selectAll();

	int updateByPrimaryKey(InsurancePic record);

	List<InsurancePic> selectByInsuranceId(String insuranceId);

	int insertBatch(List<InsurancePic> list);
}