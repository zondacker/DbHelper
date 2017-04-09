package com.opensource.dbhelp.page;

/**
 * 定义根据封装的分页对象获取页面展示分页导航html的接口。
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
public interface PageShow {

	/**
	 * 获得分页导航HTML
	 * 
	 * @param page
	 *            分页对象
	 * @return HTML字符，用于在前台展示分页导航
	 */
	public String getPageIndex(ListPage<?> page);

}
