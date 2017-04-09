package com.opensource.dbhelp.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * PageShow的默认实现， 获取前台显示的分页导航。
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
public class DefaultPageShow implements PageShow {

	/**
	 * @see com.opensource.dbhelp.page.PageShow#getPageIndex(ListPage)
	 */
	@Override
	public String getPageIndex(ListPage<?> page) {
		int pageCount = page.getPageCount();
		int curPage = page.getCurPage();
		int recordCount = page.getRecordCount();
		StringBuilder ret = new StringBuilder();
		ret.append("<div id=\"wp_page_numbers\"><ul>");
		if (pageCount >= 2) {
			Set<Integer> set = new HashSet<Integer>();
			set.add(1);
			set.add(2);
			set.add(pageCount);
			set.add(curPage);
			if (curPage > 3) {
				set.add(curPage - 1);
			}
			if (curPage > 4) {
				set.add(curPage - 2);
			}
			if (curPage < pageCount - 2) {
				set.add(curPage + 1);
			}
			if (curPage < pageCount - 3) {
				set.add(curPage + 2);
			}
			if (pageCount > 3) {
				set.add(pageCount - 1);
			}
			List<Integer> list = new ArrayList<Integer>(set);
			Collections.sort(list);

			if (curPage > 1) {
				ret.append("<li><a href=\"javascript:np(").append(curPage - 1).append(");\">&lt;</a></li>");
			} else {
				ret.append("<li class=\"no_link\">&lt;</li>");
			}

			for (int i = 0; i < list.size(); i++) {
				int num = list.get(i);
				if (ret.length() > 0) {
					ret.append(" ");
				}
				if (i > 1 && list.get(i) - list.get(i - 1) > 1) {
					ret.append("<li class=\"space\">...</li>");
				}
				ret.append(renderSingleLink(num, curPage));
			}

			if (curPage < pageCount) {
				ret.append("<li><a href=\"javascript:np(").append(curPage + 1).append(");\">&gt;</a></li>");
			} else {
				ret.append("<li class=\"no_link\">&gt;</li>");
			}
		}
		ret.append("<li class=\"page_info\">共 ").append(recordCount).append(" 条记录</li>");
		ret.append("</ul><div style=\"float: none; clear: both;\"></div></div>");
		ret.append(getScript());
		return ret.toString();
	}

	/**
	 * 得到单个翻页显示的HTML代码
	 * 
	 * @param num
	 *            显示的跳转页数
	 * @param curPage
	 *            当前页
	 * @return HTML
	 */
	private String renderSingleLink(int num, int curPage) {
		StringBuilder sb = new StringBuilder();
		if (num == curPage) {
			sb.append("<li class=\"active_page\">").append(num).append("</li>");
		} else {
			sb.append("<li><a href=\"javascript:np(").append(num).append(");\">").append(num).append("</a></li>");
		}
		return sb.toString();
	}

	/**
	 * 得到翻页script脚本
	 * 
	 * @return script脚本
	 */
	private String getScript() {
		StringBuilder sb = new StringBuilder();
		sb.append("<script>function np(p)");
		sb.append("{window.location=getRedirectURL('pageIndex',(p==null?1:p));}</script>");
		return sb.toString();
	}
}
