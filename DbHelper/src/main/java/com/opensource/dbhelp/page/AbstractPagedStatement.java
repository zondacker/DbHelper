package com.opensource.dbhelp.page;

import java.sql.SQLException;

import com.opensource.dbhelp.dialect.PageSqlParser;
import com.opensource.dbhelp.dialect.PageSqlParserFactory;

/**
 * 分页查询。
 * <p>
 * 根据查询语句和页码查询出当页数据。
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
public abstract class AbstractPagedStatement<T> {

	/** 每页允许的最大记录数 */
	public final static int MAX_PAGESIZE = Page.MAX_PAGESIZE;

	/** 查询总记录数的SQL */
	protected String countSql;

	/** 查询SQL */
	protected String querySql;

	/** 当前显示页 */
	protected int currentPage;

	/** 每页记录数 */
	protected int pageSize;

	/** 当前显示页从记录集开始的位置 */
	protected int startIndex;

	/** 总记录数 */
	protected int totalCount;

	/** 显示当前页是否需要偏移 */
	protected boolean hasOffset;

	protected PageSqlParser pageSqlParser;

	/**
	 * 构造一查询出当页数据的PageStatement，并指定每页显示记录条数
	 *
	 * @param dialect
	 *            database dialect
	 * @param sql
	 *            query sql
	 * @param currentPage
	 *            页码
	 * @param pageSize
	 *            每页容量
	 */
	public AbstractPagedStatement(String dialect, String sql, int currentPage, int pageSize) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.startIndex = Page.getStartOfAnyPage(currentPage, pageSize);
		this.hasOffset = currentPage > 1;

		pageSqlParser = PageSqlParserFactory.getParser(dialect);
		this.countSql = pageSqlParser.getCountingSql(sql);
		this.querySql = pageSqlParser.getPageSql(sql, hasOffset);
	}

	/**
	 * 执行查询
	 * 
	 * @param type
	 *            预定义的查询类型，会返回不同类型分页对象
	 * @return 分页对象
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	protected abstract Page<T> executeQuery(int type) throws SQLException;

}
