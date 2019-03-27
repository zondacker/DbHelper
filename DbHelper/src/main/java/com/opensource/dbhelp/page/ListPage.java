package com.opensource.dbhelp.page;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用List封装数据的分页对象。
 * <p>
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
public class ListPage<T> extends Page<T> {

	/** serialVersionUID */
	private static final long serialVersionUID = 3066553667648785439L;

	/** 包含记录的List对象 */
	private List<T> list;

	/**
	 * 默认构造方法，创建空页
	 */
	public ListPage() {
		this(new ArrayList<T>(), 0, 0);
	}

	/**
	 * 构造分页对象
	 *
	 * @param data
	 *            包含一页数据的OracleCachedRowSet
	 * @param start
	 *            该页数据在数据库中的起始位置
	 * @param totalSize
	 *            数据库中包含的记录总数
	 */
	public ListPage(List<T> data, int start, int totalSize) {
		this(data, start, totalSize, Page.DEFAULT_PAGESIZE);
	}

	/**
	 * 构造分页对象
	 *
	 * @param data
	 *            包含一页数据的OracleCachedRowSet
	 * @param start
	 *            该页数据在数据库中的起始位置
	 * @param totalSize
	 *            数据库中包含的记录总数
	 * @param pageSize
	 *            本页能容纳的记录数
	 */
	public ListPage(List<T> data, int start, int totalSize, int pageSize) {
		this.list = data;
		super.init(start, (data == null) ? 0 : data.size(), totalSize, pageSize, this.list);
	}

	/**
	 * 取分页对象中的记录数据
	 *
	 * @return 包含记录的List对象
	 */
	public List<T> getList() {
		return this.list;
	}

}
