package com.opensource.dbhelp.dialect;

import com.opensource.dbhelp.dialect.mysql.MySqlPageParser;
import com.opensource.dbhelp.dialect.oracle.OraclePageSqlParser;

/**
 * 获取翻页sql解析对象
 * <p/>
 * Copyright: Copyright (c) 13-1-15 上午9:27
 * <p/>
 * Company: GNU General Public License
 * <p/>
 * Author: GNU General Public License
 * <p/>
 * Version: 1.0
 * <p/>
 */
public class PageSqlParserFactory {

	/**
	 * 获取翻页解析对象
	 * 
	 * @param dialect
	 *            数据库类型
	 * @return PageSqlParser
	 */
	public static PageSqlParser getParser(String dialect) {
		if ("mysql".equals(dialect)) {
			return new MySqlPageParser();
		} else {
			return new OraclePageSqlParser();
		}
	}
}
