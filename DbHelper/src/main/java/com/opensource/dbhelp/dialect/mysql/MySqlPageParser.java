package com.opensource.dbhelp.dialect.mysql;

import com.opensource.dbhelp.dialect.PageSqlParser;
import com.opensource.dbhelp.util.ArrayUtils;

/**
 * Mysql 翻页接口实现
 * <p/>
 * Copyright: Copyright (c) 13-1-14 下午4:01
 * <p/>
 * Company: GNU General Public License
 * <p/>
 * Author: GNU General Public License
 * <p/>
 * Version: 1.0
 * <p/>
 */
public class MySqlPageParser implements PageSqlParser {

	@Override
	public String getPageSql(String sql, boolean hasOffset) {
		if (hasOffset) {
			return sql + " limit ?, ?";
		} else {
			return sql + " limit ?";
		}
	}

	@Override
	public String getCountingSql(String sql) {
		return "select count(1) from ( " + sql + ") as __tc";
	}

	@Override
	public Object[] attachPageParam(Object[] params, boolean hasOffset, int startIndex, int pageSize) {
		if (hasOffset) {
			params = ArrayUtils.add(params, startIndex - 1);
			params = ArrayUtils.add(params, pageSize);
		} else {
			params = ArrayUtils.add(params, pageSize);
		}
		return params;
	}
}
