package cn.com.clubank.weihang.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象
 * 
 * @author YangWei
 *
 */
public class PageObject<T> {

	// 用户输入的分页条件
	private int pageIndex = 0; // 页码下标
	private int pageSize = 10; // 每页行数
	private int rows; // 总行数
	private List<T> dataList = null; // 分页结果集
	
	// 根据总行数计算总页数，然后将总页数输出给页面
	private int totalPage;

	public PageObject(Integer pageIndex, Integer pageSize) {
		this.pageSize = pageSize != null ? pageSize : 10;
		this.pageIndex = pageIndex != null ? pageIndex : 0;
	}
	
	public PageObject(Integer pageIndex, Integer pageSize, Integer rows, List<T> dataList) {
		this.pageSize = pageSize != null ? pageSize : 10;
		this.pageIndex = pageIndex != null ? pageIndex : 0;
		this.rows = rows != null ? rows : 0;
		this.dataList = dataList;
	}

	public int getRows() {
		return rows;
	}
	
	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public int getTotalPage() { 
		// 根据总行数，计算总页数 
		if (rows % pageSize == 0) {
			totalPage = rows / pageSize;
		} else {
			totalPage = rows / pageSize + 1;
		}
		return totalPage; 
	}
	
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setpageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public List<T> getDataList() {
		return dataList == null ? new ArrayList<T>() : dataList;
	}

	public int getStart() {
		// 在mapper.xml使用begin属性时，对其进行计算
		return pageIndex * pageSize;
	}
	
	/**
	 * 分页获取起始下标
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public static Integer getStart(Integer pageIndex, Integer pageSize) {
		if (pageIndex == null || pageIndex < 0 || pageSize == null) {
			return -1;
		}
		return pageIndex * pageSize;
	}
}
