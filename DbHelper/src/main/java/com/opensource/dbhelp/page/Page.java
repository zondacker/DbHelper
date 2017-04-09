package com.opensource.dbhelp.page;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象。
 * <p>
 * 用于包含数据及分页信息的对象 <br>
 * Page类实现了用于显示分页信息的基本方法，但未指定所含数据的类型， 可根据需要实现以特定方式组织数据的子类， <br>
 * 如RowSetPage以RowSet封装数据，ListPage以List封装数据
 * <p>
 * Copyright: Copyright (c) Feb 6, 2009 3:39:21 PM
 * <p>
 * Company: GNU General Public License
 * <p>
 * Author: GNU General Public License
 * <p>
 * Version: 1.0
 * <p>
 */
public class Page<T> implements java.io.Serializable {

	/** sid */
	private static final long serialVersionUID = -8901550401275849961L;

	/** 每页允许的最大记录数 */
	public static final int MAX_PAGESIZE = Integer.MAX_VALUE;

	/** 空页对象 */
	public static final Page<Object> EMPTY_PAGE = new Page<Object>();

	/** 默认的每页记录数， 20 */
	public static final int DEFAULT_PAGESIZE = 20;

	/** 每页记录数 */
	private int myPageSize = DEFAULT_PAGESIZE;

	/***
	 * 本页数据在数据库中的起始位置
	 */
	private int start;

	/**
	 * 本页包含的数据
	 */
	private List<T> data;

	/**
	 * 当前显示页
	 */
	private int curPage;

	/**
	 * 本页包含的数据条数
	 */
	private int avaCount;

	/**
	 * 页数
	 */
	private int pageCount;

	/**
	 * 总记录数
	 */
	private int recordCount;

	/**
	 * 默认构造方法，只构造空页
	 */
	protected Page() {
		this.init(0, 0, 0, DEFAULT_PAGESIZE, new ArrayList<T>());
	}

	/**
	 * 分页数据初始方法，由子类调用
	 *
	 * @param start
	 *            本页数据在数据库中的起始位置
	 * @param avaCount
	 *            本页包含的数据条数
	 * @param totalSize
	 *            数据库中总记录条数
	 * @param pageSize
	 *            本页容量
	 * @param data
	 *            本页包含的数据
	 */
	protected void init(int start, int avaCount, int totalSize, int pageSize, List<T> data) {

		this.avaCount = avaCount;
		this.myPageSize = pageSize;

		this.start = start;
		this.recordCount = totalSize;

		this.data = data;

		if (avaCount > totalSize) {
			// throw new RuntimeException("记录条数大于总条数？！");
		}

		this.curPage = (start - 1) / pageSize + 1;
		this.pageCount = (totalSize + pageSize - 1) / pageSize;

		if (totalSize == 0 && avaCount == 0) {
			this.curPage = 1;
			this.pageCount = 1;
		}
	}

	/**
	 * 获得本页数据
	 * 
	 * @return 本页数据
	 */
	public List<T> getData() {
		return this.data;
	}

	/**
	 * 取本页数据容量（本页能包含的记录数）
	 *
	 * @return 本页能包含的记录数
	 */
	public int getPageSize() {
		return this.myPageSize;
	}

	/**
	 * 是否有下一页
	 *
	 * @return 是否有下一页
	 */
	public boolean hasNextPage() {
		return (this.getCurPage() < this.getPageCount());
	}

	/**
	 * 是否有上一页
	 *
	 * @return 是否有上一页
	 */
	public boolean hasPreviousPage() {
		return (this.getCurPage() > 1);
	}

	/**
	 * 获取当前页第一条数据在数据库中的位置
	 *
	 * @return 得到当页开始记录在记录集中位置
	 */
	public int getStart() {
		return start;
	}

	/**
	 * 获取当前页最后一条数据在数据库中的位置
	 *
	 * @return 得到当页结束记录在记录集中位置
	 */
	public int getEnd() {
		int end = this.getStart() + this.getAvaCount() - 1;
		if (end < 0) {
			end = 0;
		}
		return end;
	}

	/**
	 * 获取上一页第一条数据在数据库中的位置
	 *
	 * @return 记录对应的rownum
	 */
	public int getStartOfPreviousPage() {
		return Math.max(start - myPageSize, 1);
	}

	/**
	 * 获取下一页第一条数据在数据库中的位置
	 *
	 * @return 记录对应的rownum
	 */
	public int getStartOfNextPage() {
		return start + avaCount;
	}

	/**
	 * 获取任一页第一条数据在数据库中的位置，每页条数使用默认值
	 *
	 * @param pageNo
	 *            页号
	 * @return 记录对应的rownum
	 */
	public static int getStartOfAnyPage(int pageNo) {
		return getStartOfAnyPage(pageNo, DEFAULT_PAGESIZE);
	}

	/**
	 * 获取任一页第一条数据在数据库中的位置
	 *
	 * @param pageNo
	 *            页号
	 * @param pageSize
	 *            每页包含的记录数
	 * @return 记录对应的rownum
	 */
	public static int getStartOfAnyPage(int pageNo, int pageSize) {
		int startIndex = (pageNo - 1) * pageSize + 1;
		if (startIndex < 1)
			startIndex = 1;
		return startIndex;
	}

	/**
	 * 取本页包含的记录数
	 *
	 * @return 本页包含的记录数
	 */
	public int getAvaCount() {
		return avaCount;
	}

	/**
	 * 取数据库中包含的总记录数
	 *
	 * @return 数据库中包含的总记录数
	 */
	public int getRecordCount() {
		return this.recordCount;
	}

	/**
	 * 取当前页码
	 *
	 * @return 当前页码
	 */
	public int getCurPage() {
		return this.curPage;
	}

	/**
	 * 取总页码
	 *
	 * @return 总页码
	 */
	public int getPageCount() {
		return this.pageCount;
	}

}
