package com.opensource.dbhelp.dialect;

/**
 * 翻页SQL接口
 * <p/>
 * Copyright: Copyright (c) 13-1-14 下午3:57
 * <p/>
 * Company: GNU General Public License
 * <p/>
 * Author: GNU General Public License
 * <p/>
 * Version: 1.0
 * <p/>
 */
public interface PageSqlParser {

	/**
	 * 生成查询一页数据的sql语句
	 *
	 * @param sql
	 *            原查询语句
	 * @param hasOffset
	 *            true 不是第一页 false 第一页
	 * @return 查询当前页的SQL
	 */
	public String getPageSql(String sql, boolean hasOffset);

	/**
	 * 获取查询记录数的sql语句
	 *
	 * @param sql
	 *            原查询语句
	 * @return 查询记录数的SQL
	 */
	public String getCountingSql(String sql);

	/**
	 * 附加翻页参数
	 * 
	 * @param params
	 *            原有参数
	 * @param hasOffset
	 *            true 不是第一页 false 第一页
	 * @param startIndex
	 *            开始索引
	 * @param pageSize
	 *            每页记录数
	 * @return 附加翻页后的传入参数
	 */
	public Object[] attachPageParam(Object[] params, boolean hasOffset, int startIndex, int pageSize);
}
