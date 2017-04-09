package com.opensource.dbhelp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensource.dbhelp.dbutils.BasicRowProcessor;
import com.opensource.dbhelp.dbutils.CamelBeanProcessor;
import com.opensource.dbhelp.dbutils.DbUtils;
import com.opensource.dbhelp.dbutils.QueryRunner;
import com.opensource.dbhelp.dbutils.RowProcessor;
import com.opensource.dbhelp.dbutils.handlers.ArrayHandler;
import com.opensource.dbhelp.dbutils.handlers.ArrayListHandler;
import com.opensource.dbhelp.dbutils.handlers.BeanHandler;
import com.opensource.dbhelp.dbutils.handlers.BeanListHandler;
import com.opensource.dbhelp.dbutils.handlers.MapHandler;
import com.opensource.dbhelp.dbutils.handlers.MapListHandler;
import com.opensource.dbhelp.dbutils.handlers.ScalarHandler;
import com.opensource.dbhelp.dbutils.handlers.StringArrayHandler;
import com.opensource.dbhelp.dbutils.handlers.StringArrayListHandler;
import com.opensource.dbhelp.dbutils.handlers.StringMapHandler;
import com.opensource.dbhelp.dbutils.handlers.StringMapListHandler;
import com.opensource.dbhelp.page.ListPage;
import com.opensource.dbhelp.page.ListPagedStatement;
import com.opensource.dbhelp.page.Page;

/**
 * 数据库查询、执行处理帮助类。
 * <p/>
 * Copyright: Copyright (c) Feb 6, 2009 3:39:21 PM
 * <p/>
 * Company: GNU General Public License
 * <p/>
 * Author: GNU General Public License
 * <p/>
 * Version: 1.1
 * <p/>
 */
public class DbHelper {

	/**
	 * logger
	 */
	protected static final Log logger = LogFactory.getLog(DbHelper.class);

	/**
	 * sql logger，用于把执行的SQL语句及参数进行单独记录
	 */
	protected static final Log sqllogger = LogFactory.getLog("com.opensource.SQL");

	/**
	 * 数据源
	 */
	protected DataSource dataSource;

	/**
	 * 数据库类型
	 */
	protected String dialect;

	/**
	 * Singleton processor instance that handlers share to save memory.
	 */
	public static final RowProcessor BEAN_ROW_PROCESSOR = new BasicRowProcessor(new CamelBeanProcessor());

	/**
	 * 构造函数
	 *
	 * @param dataSource
	 *            数据源对象
	 */
	public DbHelper(DataSource dataSource) {
		setDataSource(dataSource);
	}

	/**
	 * 获取数据源
	 *
	 * @return 数据源对象
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * 设置数据源
	 *
	 * @param dataSource
	 *            数据源
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 获取数据库类型
	 *
	 * @return mysql or oracle
	 */
	public String getDialect() {
		return dialect;
	}

	/**
	 * 设置数据库类型
	 *
	 * @param dialect
	 *            mysql or oracle
	 */
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	/**
	 * 获得数据库事务处理帮助类
	 *
	 * @return DbTransaction
	 */
	public DbTransaction getDbTransaction() {
		return new DbTransaction(this.dataSource);
	}

	/**
	 * 取得数据库连接
	 *
	 * @return 数据库连接对象
	 */
	public Connection getConn() {
		try {
			return dataSource.getConnection();
		}
		catch (SQLException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 释放数据库连接
	 *
	 * @param conn
	 *            要释放的数据库连接
	 */
	public void closeConn(Connection conn) {
		DbUtils.closeQuietly(conn);
	}

	// ///////////////////////////////////////////////

	/**
	 * 根据SQL查询出结果集中的第一个字段的值。 如果结果集有多行，则只返回首行对应的结果。 如果结果集为空，则返回null。
	 *
	 * @param sql
	 *            执行的SQL语句
	 * @param params
	 *            参数数组
	 * @return 查询结果
	 * @throws SQLException
	 *             - if there is any problem executing the sql
	 */
	public String queryScalar(String sql, Object... params) throws SQLException {
		debug(sql, params);
		QueryRunner run = new QueryRunner(dataSource);
		Object obj = run.query(sql, new ScalarHandler<Object>(1), params);
		if (obj != null) {
			return obj.toString();
		}
		return null;
	}

	/**
	 * 批量执行 INSERT、UPDATE 或者 DELETE 语句。
	 *
	 * @param sql
	 *            即将被执行的sql语句
	 * @param params
	 *            参数数组
	 * @return 影响的行数
	 * @throws SQLException
	 *             - if there is any problem executing the sql
	 */
	public int[] batch(String sql, List<Object[]> params) throws SQLException {
		debug(sql, "batch sql, count:" + ((params == null) ? -1 : params.size()));
		QueryRunner run = new QueryRunner(dataSource);
		if (params == null) {
			throw new SQLException("Null parameters. If parameters aren't need, pass an empty array.");
		}
		Object[][] o = new Object[params.size()][];
		for (int i = 0; i < params.size(); i++) {
			o[i] = params.get(i);
		}
		return run.batch(sql, o);
	}

	/**
	 * 带入指定的参数数组，执行 SELECT 语句，返回Map&#60;String, String&#62;类型的单行结果。 如果结果集有多行，则只返回首行。 如果结果集为空，则返回null。 结果集中的null字段将转为空字符。
	 *
	 * @param sql
	 *            即将被执行的sql语句
	 * @param params
	 *            参数数组
	 * @return 查询结果
	 * @throws SQLException
	 *             - if there is any problem executing the sql
	 */
	public Map<String, String> getMap(String sql, Object... params) throws SQLException {
		debug(sql, params);
		QueryRunner run = new QueryRunner(dataSource);
		return run.query(sql, new StringMapHandler(), params);
	}

	/**
	 * 带入指定的参数数组，执行 SELECT 语句，返回Map&#60;String, Object&#62;类型的单行结果。 如果结果集有多行，则只返回首行。 如果结果集为空，则返回null。
	 *
	 * @param sql
	 *            即将被执行的sql语句
	 * @param params
	 *            参数数组
	 * @return 查询结果
	 * @throws SQLException
	 *             - if there is any problem executing the sql
	 */
	public Map<String, Object> getNativeMap(String sql, Object... params) throws SQLException {
		debug(sql, params);
		QueryRunner run = new QueryRunner(dataSource);
		return run.query(sql, new MapHandler(), params);
	}

	/**
	 * 带入指定的参数数组，执行 SELECT 语句，返回字符串数组类型的单行结果。 如果结果集有多行，则只返回首行。 如果结果集为空，则返回null。 结果集中的null字段将转为空字符。
	 *
	 * @param sql
	 *            即将被执行的sql语句
	 * @param params
	 *            参数数组
	 * @return 查询结果
	 * @throws SQLException
	 *             - if there is any problem executing the sql
	 */
	public String[] getArray(String sql, Object... params) throws SQLException {
		debug(sql, params);
		QueryRunner run = new QueryRunner(dataSource);
		return run.query(sql, new StringArrayHandler(), params);
	}

	/**
	 * 带入指定的参数数组，执行 SELECT 语句，返回对象数组类型的单行结果。 如果结果集有多行，则只返回首行。 如果结果集为空，则返回null。
	 *
	 * @param sql
	 *            即将被执行的sql语句
	 * @param params
	 *            参数数组
	 * @return 查询结果
	 * @throws SQLException
	 *             - if there is any problem executing the sql
	 */
	public Object[] getNativeArray(String sql, Object... params) throws SQLException {
		debug(sql, params);
		QueryRunner run = new QueryRunner(dataSource);
		return run.query(sql, new ArrayHandler(), params);
	}

	/**
	 * 给定SQL和指定的类，把字段映射到对应对象属性中，返回对象类型的单行结果。 如果结果集有多行，则只返回首行。 如果结果集为空，则返回null。 字段属性映射按照java属性命名原则，例如 update_time 映射到 updateTime 属性。
	 *
	 * @param sql
	 *            即将被执行的sql语句
	 * @param type
	 *            指定类
	 * @param params
	 *            参数数组
	 * @return 查询结果
	 * @throws SQLException
	 *             - if there is any problem executing the sql
	 */
	public <T> T getBean(String sql, Class<T> type, Object... params) throws SQLException {
		debug(sql, params);
		QueryRunner run = new QueryRunner(dataSource);
		return run.query(sql, new BeanHandler<T>(type, BEAN_ROW_PROCESSOR), params);
	}

	/**
	 * 带入指定的参数数组，执行 SELECT 语句，返回Map&#60;String, String&#62;列表。 如果结果集为空，则返回空列表。 结果集中的null字段将转为空字符。
	 *
	 * @param sql
	 *            即将被执行的sql语句
	 * @param params
	 *            参数数组
	 * @return 查询结果
	 * @throws SQLException
	 *             - if there is any problem executing the sql
	 */
	public List<Map<String, String>> getMapList(String sql, Object... params) throws SQLException {
		debug(sql, params);
		QueryRunner run = new QueryRunner(dataSource);
		return run.query(sql, new StringMapListHandler(), params);
	}

	/**
	 * 带入指定的参数数组，执行 SELECT 语句，返回Map&#60;String, Object&#62;列表。 如果结果集为空，则返回空列表。
	 *
	 * @param sql
	 *            即将被执行的sql语句
	 * @param params
	 *            参数数组
	 * @return 查询结果
	 * @throws SQLException
	 *             - if there is any problem executing the sql
	 */
	public List<Map<String, Object>> getNativeMapList(String sql, Object... params) throws SQLException {
		debug(sql, params);
		QueryRunner run = new QueryRunner(dataSource);
		return run.query(sql, new MapListHandler(), params);
	}

	/**
	 * 带入指定的参数数组，执行 SELECT 语句，返回字符串数组列表。 如果结果集为空，则返回空列表。 结果集中的null字段将转为空字符。
	 *
	 * @param sql
	 *            即将被执行的sql语句
	 * @param params
	 *            参数数组
	 * @return 查询结果
	 * @throws SQLException
	 *             - if there is any problem executing the sql
	 */
	public List<String[]> getArrayList(String sql, Object... params) throws SQLException {
		debug(sql, params);
		QueryRunner run = new QueryRunner(dataSource);
		return run.query(sql, new StringArrayListHandler(), params);
	}

	/**
	 * 带入指定的参数数组，执行 SELECT 语句，并把查询结果返回为字符串数组列表 如果结果集为空，则返回空列表。
	 *
	 * @param sql
	 *            即将被执行的sql语句
	 * @param params
	 *            参数数组
	 * @return 查询结果
	 * @throws SQLException
	 *             - if there is any problem executing the sql
	 */
	public List<Object[]> getNativeArrayList(String sql, Object... params) throws SQLException {
		debug(sql, params);
		QueryRunner run = new QueryRunner(dataSource);
		return run.query(sql, new ArrayListHandler(), params);
	}

	/**
	 * 给定SQL和指定的类，把字段映射到对应对象属性中，返回对象类型列表。 如果结果集为空，则返回空列表。 字段属性映射按照java属性命名原则，例如 update_time 映射到 updateTime 属性。
	 *
	 * @param sql
	 *            即将被执行的sql语句
	 * @param type
	 *            指定类
	 * @param params
	 *            参数数组
	 * @return 查询结果
	 * @throws SQLException
	 *             - if there is any problem executing the sql
	 */
	public <T> List<T> getBeanList(String sql, Class<T> type, Object... params) throws SQLException {
		debug(sql, params);
		QueryRunner run = new QueryRunner(dataSource);
		return run.query(sql, new BeanListHandler<T>(type, BEAN_ROW_PROCESSOR), params);
	}

	/**
	 * 执行 UPDATE 或者 DELETE 语句。
	 *
	 * @param sql
	 *            即将被执行的sql语句
	 * @param params
	 *            参数数组
	 * @return 影响的行数
	 * @throws SQLException
	 *             - if there is any problem executing the sql
	 */
	public int execute(String sql, Object... params) throws SQLException {
		debug(sql, params);
		QueryRunner run = new QueryRunner(dataSource);
		return run.update(sql, params);
	}

	// //////////////Page/////////////////////

	/**
	 * 根据传入的数据库相关的sql和页面信息返回数组格式的分页对象。
	 *
	 * @param sql
	 *            用于查询记录集的SQL
	 * @return 基于数据库相关的sql的分页实现类
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public ListPage<String[]> getArrayPage(String sql) throws SQLException {
		return getArrayPage(sql, 1, Page.DEFAULT_PAGESIZE, (Object[]) null);
	}

	/**
	 * 根据传入的数据库相关的sql和页面信息返回数组格式的分页对象。
	 *
	 * @param sql
	 *            用于查询记录集的SQL
	 * @param curPage
	 *            显示的页数
	 * @return 基于数据库相关的sql的分页实现类
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public ListPage<String[]> getArrayPage(String sql, int curPage) throws SQLException {
		return getArrayPage(sql, curPage, Page.DEFAULT_PAGESIZE, (Object[]) null);
	}

	/**
	 * 根据传入的数据库相关的sql和页面信息返回数组格式的分页对象。
	 *
	 * @param sql
	 *            用于查询记录集的SQL
	 * @param curPage
	 *            显示的页数
	 * @param pageSize
	 *            每页记录数
	 * @return 基于数据库相关的sql的分页实现类
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public ListPage<String[]> getArrayPage(String sql, int curPage, int pageSize) throws SQLException {
		return getArrayPage(sql, curPage, pageSize, (Object[]) null);
	}

	/**
	 * 根据传入的数据库相关的sql和页面信息返回数组格式的分页对象。
	 *
	 * @param sql
	 *            用于查询记录集的SQL
	 * @param params
	 *            查询参数
	 * @param curPage
	 *            显示的页数
	 * @param pageSize
	 *            每页记录数
	 * @return 基于数据库相关的sql的分页实现类
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public ListPage<String[]> getArrayPage(String sql, int curPage, int pageSize, Object... params) throws SQLException {
		ListPagedStatement<String[]> pst = new ListPagedStatement<String[]>(dataSource, dialect, sql, curPage, pageSize, params);
		return pst.executeQuery(0);
	}

	/**
	 * 根据传入的数据库相关的sql和页面信息返回Map格式的分页对象，Map的值为对象格式。
	 *
	 * @param sql
	 *            用于查询记录集的SQL
	 * @return 基于数据库相关的sql的分页实现类
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public ListPage<Map<String, Object>> getNativeMapPage(String sql) throws SQLException {
		return getNativeMapPage(sql, 1, Page.DEFAULT_PAGESIZE, (Object[]) null);
	}

	/**
	 * 根据传入的数据库相关的sql和页面信息返回Map格式的分页对象，Map的值为对象格式。
	 *
	 * @param sql
	 *            用于查询记录集的SQL
	 * @param curPage
	 *            显示的页数
	 * @return 基于数据库相关的sql的分页实现类
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public ListPage<Map<String, Object>> getNativeMapPage(String sql, int curPage) throws SQLException {
		return getNativeMapPage(sql, curPage, Page.DEFAULT_PAGESIZE, (Object[]) null);
	}

	/**
	 * 根据传入的数据库相关的sql和页面信息返回Map格式的分页对象，Map的值为对象格式。
	 *
	 * @param sql
	 *            用于查询记录集的SQL
	 * @param curPage
	 *            显示的页数
	 * @param pageSize
	 *            每页记录数
	 * @return 基于数据库相关的sql的分页实现类
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public ListPage<Map<String, Object>> getNativeMapPage(String sql, int curPage, int pageSize) throws SQLException {
		return getNativeMapPage(sql, curPage, pageSize, (Object[]) null);
	}

	/**
	 * 根据传入的数据库相关的sql和页面信息返回Map格式的分页对象，Map的值为对象格式。
	 *
	 * @param sql
	 *            用于查询记录集的SQL
	 * @param params
	 *            查询参数
	 * @param curPage
	 *            显示的页数
	 * @param pageSize
	 *            每页记录数
	 * @return 基于数据库相关的sql的分页实现类
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public ListPage<Map<String, Object>> getNativeMapPage(String sql, int curPage, int pageSize, Object... params) throws SQLException {
		ListPagedStatement<Map<String, Object>> pst = new ListPagedStatement<Map<String, Object>>(dataSource, dialect, sql, curPage, pageSize, params);
		return pst.executeQuery(2);
	}

	/**
	 * 根据传入的数据库相关的sql和页面信息返回Bean格式的分页对象。
	 *
	 * @param sql
	 *            用于查询记录集的SQL
	 * @param clazz
	 *            可以转化的bean类
	 * @return 基于数据库相关的sql的分页实现类
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public <T> ListPage<T> getPage(String sql, Class<T> clazz) throws SQLException {
		return getPage(sql, clazz, 1, Page.DEFAULT_PAGESIZE, (Object[]) null);
	}

	/**
	 * 根据传入的数据库相关的sql和页面信息返回Bean格式的分页对象。
	 *
	 * @param sql
	 *            用于查询记录集的SQL
	 * @param clazz
	 *            可以转化的bean类
	 * @param curPage
	 *            显示的页数
	 * @return 基于数据库相关的sql的分页实现类
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public <T> ListPage<T> getPage(String sql, Class<T> clazz, int curPage) throws SQLException {
		return getPage(sql, clazz, curPage, Page.DEFAULT_PAGESIZE, (Object[]) null);
	}

	/**
	 * 根据传入的数据库相关的sql和页面信息返回Bean格式的分页对象。
	 *
	 * @param sql
	 *            用于查询记录集的SQL
	 * @param clazz
	 *            可以转化的bean类
	 * @param curPage
	 *            显示的页数
	 * @param pageSize
	 *            每页记录数
	 * @return 基于数据库相关的sql的分页实现类
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public <T> ListPage<T> getPage(String sql, Class<T> clazz, int curPage, int pageSize) throws SQLException {
		return getPage(sql, clazz, curPage, pageSize, (Object[]) null);
	}

	/**
	 * 根据传入的数据库相关的sql和页面信息返回Bean格式的分页对象。
	 *
	 * @param sql
	 *            用于查询记录集的SQL
	 * @param params
	 *            查询参数
	 * @param clazz
	 *            可以转化的bean类
	 * @param curPage
	 *            显示的页数
	 * @param pageSize
	 *            每页记录数
	 * @return 基于数据库相关的sql的分页实现类
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public <T> ListPage<T> getPage(String sql, Class<T> clazz, int curPage, int pageSize, Object... params) throws SQLException {
		ListPagedStatement<T> pst = new ListPagedStatement<T>(dataSource, dialect, sql, curPage, pageSize, params);
		pst.setClazz(clazz);
		return pst.executeQuery(-1);
	}

	/**
	 * 根据传入的数据库相关的sql和页面信息返回Map格式的分页对象，Map的值为字符串。
	 *
	 * @param sql
	 *            用于查询记录集的SQL
	 * @return 基于数据库相关的sql的分页实现类
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public ListPage<Map<String, String>> getMapPage(String sql) throws SQLException {
		return getMapPage(sql, 1, Page.DEFAULT_PAGESIZE, (Object[]) null);
	}

	/**
	 * 根据传入的数据库相关的sql和页面信息返回Map格式的分页对象，Map的值为字符串。
	 *
	 * @param sql
	 *            用于查询记录集的SQL
	 * @param curPage
	 *            显示的页数
	 * @return 基于数据库相关的sql的分页实现类
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public ListPage<Map<String, String>> getMapPage(String sql, int curPage) throws SQLException {
		return getMapPage(sql, curPage, Page.DEFAULT_PAGESIZE, (Object[]) null);
	}

	/**
	 * 根据传入的数据库相关的sql和页面信息返回Map格式的分页对象，Map的值为字符串。
	 *
	 * @param sql
	 *            用于查询记录集的SQL
	 * @param curPage
	 *            显示的页数
	 * @param pageSize
	 *            每页记录数
	 * @return 基于数据库相关的sql的分页实现类
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public ListPage<Map<String, String>> getMapPage(String sql, int curPage, int pageSize) throws SQLException {
		return getMapPage(sql, curPage, pageSize, (Object[]) null);
	}

	/**
	 * 根据传入的数据库相关的sql和页面信息返回Map格式的分页对象，Map的值为字符串。
	 *
	 * @param sql
	 *            用于查询记录集的SQL
	 * @param params
	 *            查询参数
	 * @param curPage
	 *            显示的页数
	 * @param pageSize
	 *            每页记录数
	 * @return 基于数据库相关的sql的分页实现类
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public ListPage<Map<String, String>> getMapPage(String sql, int curPage, int pageSize, Object... params) throws SQLException {
		ListPagedStatement<Map<String, String>> pst = new ListPagedStatement<Map<String, String>>(dataSource, dialect, sql, curPage, pageSize, params);
		return pst.executeQuery(1);
	}

	/**
	 * 输出debug信息
	 *
	 * @param sql
	 *            执行的SQL语句
	 * @param params
	 *            执行的参数
	 */
	protected void debug(String sql, Object... params) {
		if (sqllogger.isDebugEnabled()) {
			if (params == null) {
				params = new Object[] {};
			}
			StringBuilder sb = new StringBuilder(400);
			sb.append(sql);
			if (params.length != 0) {
				sb.append(" [params:");
				for (Object obj : params) {
					sb.append(obj == null ? "<null>" : obj.toString()).append(", ");
				}
				sb.setLength(sb.length() - 2);
				sb.append("]");
			}
			sqllogger.debug(sb.toString());
		}
	}

}
