package com.opensource.dbhelp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensource.dbhelp.dbutils.DbUtils;
import com.opensource.dbhelp.dbutils.QueryRunner;
import com.opensource.dbhelp.dbutils.handlers.BeanHandler;
import com.opensource.dbhelp.dbutils.handlers.BeanListHandler;
import com.opensource.dbhelp.dbutils.handlers.StringArrayHandler;
import com.opensource.dbhelp.dbutils.handlers.StringArrayListHandler;

/**
 * 数据库事务处理帮助类。
 * <p/>
 * 典型的调用方法如下： <br>
 * 
 * <pre>
 * DbTransaction dbc = DbHelperManager.getHelper().getDbTransaction();
 * try {
 *     dbc.connect();
 *     dbc.begin();
 * 
 *     dbc.execute("select ...", new Object[]{...});
 *     dbc.execute("insert into ...", ...);
 *     dbc.execute("insert into ...");
 * 
 *     String[] tmp1 = dbc.find("select ...", new Object[]{...});
 *     System.out.println( org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(tmp1) );
 * 
 *     List<Object[]> params = new ArrayList<Object[]>();
 *     params.add(new Object[]{3213, "20080920101111"});
 *     params.add(new Object[]{3214, "20080920101111"});
 *     params.add(new Object[]{3215, "20080920101111"});
 * 
 *     dbc.batch("insert into ... values (?, ?)", params);
 * 
 *     List<String[]> tmp2 = dbc.query("select * from ...");
 *     System.out.println( org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(tmp2) );
 * 
 *     dbc.execute("delete from ...", new Object[]{...});
 * 
 *     dbc.commit();
 * } catch (Exception e) {
 *     dbc.rollback();
 *     throw e;
 * } finally {
 *     dbc.close();
 * }
 * </pre>
 * <p/>
 * Copyright: Copyright (c) Feb 6, 2009 3:39:21 PM
 * <p/>
 * Company: GNU General Public License
 * <p/>
 * Author: GNU General Public License
 * <p/>
 * Version: 1.0
 * <p/>
 */
public class DbTransaction {

	/**
	 * logger
	 */
	private static final Log logger = LogFactory.getLog(DbTransaction.class);

	/**
	 * sql logger，用于把执行的SQL语句及参数进行单独记录
	 */
	private static final Log sqllogger = LogFactory.getLog("com.opensource.SQL");

	/**
	 * 数据库连接
	 */
	private Connection conn = null;

	/**
	 * 是否自动提交
	 */
	private boolean isAutoCommit;

	/**
	 * QueryRunner对象
	 */
	private QueryRunner run = null;

	/**
	 * 初始化时初始化QueryRunner对象
	 *
	 * @param dataSource
	 *            使用的数据源
	 */
	public DbTransaction(DataSource dataSource) {
		run = new QueryRunner(dataSource);
	}

	/**
	 * 建立数据库连接
	 *
	 * @throws SQLException
	 *             - 如果不能建立连接
	 */
	public void connect() throws SQLException {
		try {
			conn = run.getDataSource().getConnection();
		}
		catch (SQLException ex) {
			logger.error("get connection error!", ex);
			throw ex;
		}
	}

	/**
	 * 事务生命周期开始
	 *
	 * @throws SQLException
	 *             SQL异常
	 */
	public void begin() throws SQLException {
		try {
			isAutoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
		}
		catch (SQLException ex) {
			logger.error("begin transaction error!", ex);
			throw ex;
		}
	}

	/**
	 * 获取数据库连接
	 *
	 * @return 数据库连接对象
	 */
	public Connection getConnection() {
		return conn;
	}

	/**
	 * 结束事务生命周期，提交所有变更
	 *
	 * @throws SQLException
	 *             SQL异常
	 */
	public void commit() throws SQLException {
		try {
			conn.commit();
			conn.setAutoCommit(isAutoCommit);
		}
		catch (SQLException ex) {
			logger.error("commit transaction error!", ex);
			throw ex;
		}
	}

	/**
	 * 事务回滚
	 */
	public void rollback() {
		try {
			conn.rollback();
			conn.setAutoCommit(isAutoCommit);
		}
		catch (SQLException ex) {
			logger.error("rollback transaction error!", ex);
		}
	}

	/**
	 * 判断是否为自动加入数据模式
	 *
	 * @return 是否为自动提交
	 * @throws SQLException
	 *             SQL异常
	 */
	public boolean getAutoCommit() throws SQLException {
		boolean result;
		try {
			result = conn.getAutoCommit();
		}
		catch (SQLException ex) {
			logger.error("get auto commit status error!", ex);
			throw ex;
		}
		return result;
	}

	/**
	 * sql执行操作，用于数据更新，主要是Update，Insert，delete
	 *
	 * @param sql
	 *            要执行的sql语句
	 * @return 影响的行数
	 * @throws SQLException
	 *             捕捉错误
	 */
	public int execute(String sql) throws SQLException {
		return execute(sql, (Object[]) null);
	}

	/**
	 * sql执行操作，用于数据更新，主要是Update，Insert，delete
	 *
	 * @param sql
	 *            要执行的sql语句
	 * @param param
	 *            参数对象
	 * @return 影响的行数
	 * @throws SQLException
	 *             捕捉错误
	 */
	public int execute(String sql, Object param) throws SQLException {
		return execute(sql, new Object[] { param });
	}

	/**
	 * sql执行操作，用于数据更新，主要是Update，Insert，delete
	 *
	 * @param sql
	 *            要执行的sql语句
	 * @param params
	 *            参数数组对象
	 * @return 影响的行数
	 * @throws SQLException
	 *             捕捉错误
	 */
	public int execute(String sql, Object... params) throws SQLException {
		debug(sql, params);
		return run.update(conn, sql, params);
	}

	/**
	 * 批处理执行sql
	 *
	 * @param sql
	 *            要执行的SQL语句
	 * @param params
	 *            参数数组对象
	 * @return The number of rows updated per statement.
	 * @throws SQLException
	 *             SQL异常
	 */
	public int[] batch(String sql, List<Object[]> params) throws SQLException {
		debug(sql, "batch sql, count:" + ((params == null) ? -1 : params.size()));
		assert params != null;
		Object[][] p = new Object[params.size()][];
		for (int i = 0; i < params.size(); i++) {
			p[i] = params.get(i);
		}
		return run.batch(conn, sql, p);
	}

	/**
	 * 查找单条记录
	 *
	 * @param sql
	 *            要执行的SQL语句
	 * @return 如果查询不到记录，返回null
	 * @throws SQLException
	 *             SQL异常
	 */
	public String[] find(String sql) throws SQLException {
		return find(sql, (Object[]) null);
	}

	/**
	 * 查找单条记录
	 *
	 * @param sql
	 *            要执行的SQL语句
	 * @param param
	 *            参数对象
	 * @return 如果查询不到记录，返回null
	 * @throws SQLException
	 *             SQL异常
	 */
	public String[] find(String sql, Object param) throws SQLException {
		return find(sql, new Object[] { param });
	}

	/**
	 * 查找单条记录
	 *
	 * @param sql
	 *            要执行的SQL语句
	 * @param params
	 *            参数数组
	 * @return 如果查询不到记录，返回null
	 * @throws SQLException
	 *             SQL异常
	 */
	public String[] find(String sql, Object... params) throws SQLException {
		debug(sql, params);
		return run.query(conn, sql, new StringArrayHandler(), params);
	}

	/**
	 * 查找单条记录
	 *
	 * @param sql
	 *            要执行的SQL语句
	 * @param type
	 *            指定类
	 * @return 如果查询不到记录，返回null
	 * @throws SQLException
	 *             SQL异常
	 */
	public <T> T find(String sql, Class<T> type) throws SQLException {
		return find(sql, type, (Object[]) null);
	}

	/**
	 * 查找单条记录
	 *
	 * @param sql
	 *            要执行的SQL语句
	 * @param type
	 *            指定类
	 * @param param
	 *            参数对象
	 * @return 如果查询不到记录，返回null
	 * @throws SQLException
	 *             SQL异常
	 */
	public <T> T find(String sql, Class<T> type, Object param) throws SQLException {
		return find(sql, type, new Object[] { param });
	}

	/**
	 * 查找单条记录
	 *
	 * @param sql
	 *            要执行的SQL语句
	 * @param type
	 *            指定类
	 * @param params
	 *            参数数组
	 * @return 如果查询不到记录，返回null
	 * @throws SQLException
	 *             SQL异常
	 */
	public <T> T find(String sql, Class<T> type, Object... params) throws SQLException {
		debug(sql, params);
		return run.query(conn, sql, new BeanHandler<T>(type, DbHelper.BEAN_ROW_PROCESSOR), params);
	}

	/**
	 * 查询所有记录到list中
	 *
	 * @param sql
	 *            要执行的SQL语句
	 * @return 如果没有查询结果，返回容量为0的list
	 * @throws SQLException
	 *             SQL异常
	 */
	public List<String[]> query(String sql) throws SQLException {
		return query(sql, (Object[]) null);
	}

	/**
	 * 查询所有记录到list中
	 *
	 * @param sql
	 *            要执行的SQL语句
	 * @param param
	 *            参数对象
	 * @return 如果没有查询结果，返回容量为0的list
	 * @throws SQLException
	 *             SQL异常
	 */
	public List<String[]> query(String sql, Object param) throws SQLException {
		return query(sql, new Object[] { param });
	}

	/**
	 * 查询所有记录到list中
	 *
	 * @param sql
	 *            要执行的SQL语句
	 * @param params
	 *            参数数组对象
	 * @return 如果没有查询结果，返回容量为0的list
	 * @throws SQLException
	 *             SQL异常
	 */
	public List<String[]> query(String sql, Object... params) throws SQLException {
		debug(sql, params);
		return run.query(conn, sql, new StringArrayListHandler(), params);
	}

	/**
	 * 查询所有记录到bean list中
	 *
	 * @param sql
	 *            要执行的SQL语句
	 * @param type
	 *            指定类
	 * @return 如果没有查询结果，返回容量为0的list
	 * @throws SQLException
	 *             SQL异常
	 */
	public <T> List<T> query(String sql, Class<T> type) throws SQLException {
		return query(sql, type, (Object[]) null);
	}

	/**
	 * 查询所有记录到bean list中
	 *
	 * @param sql
	 *            要执行的SQL语句
	 * @param type
	 *            指定类
	 * @param param
	 *            参数对象
	 * @return 如果没有查询结果，返回容量为0的list
	 * @throws SQLException
	 *             SQL异常
	 */
	public <T> List<T> query(String sql, Class<T> type, Object param) throws SQLException {
		return query(sql, type, new Object[] { param });
	}

	/**
	 * 查询所有记录到bean list中
	 *
	 * @param sql
	 *            要执行的SQL语句
	 * @param type
	 *            指定类
	 * @param params
	 *            参数数组对象
	 * @return 如果没有查询结果，返回容量为0的list
	 * @throws SQLException
	 *             SQL异常
	 */
	public <T> List<T> query(String sql, Class<T> type, Object... params) throws SQLException {
		debug(sql, params);
		return run.query(conn, sql, new BeanListHandler<T>(type, DbHelper.BEAN_ROW_PROCESSOR), params);
	}

	/**
	 * 关闭连接
	 */
	public void close() {
		DbUtils.closeQuietly(conn);
		conn = null;
	}

	/**
	 * 输出debug信息
	 *
	 * @param sql
	 *            执行的SQL语句
	 * @param params
	 *            执行的参数
	 */
	private static void debug(String sql, Object... params) {
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

	/**
	 * 内部测试方法。。。
	 *
	 * @param args
	 *            参数
	 * @throws Exception
	 *             异常
	 */
	public static void main(String[] args) throws Exception {
		/*
		 * DbTransaction dbc = new DbTransaction(); String[] tmp1; List<String[]> tmp2; try { dbc.connect(); dbc.begin(); dbc.execute("insert into t_sns_session (user_id, last_activity) values (?, ?)", new Object[]{3210, "20080920101111"}); dbc.execute("insert into t_sns_session (user_id, last_activity) values (?, '20090102000000')", 3211); dbc.execute("insert into t_sns_session (user_id, last_activity) values (3212, '20090102000000')"); tmp1 = dbc.find("select * from t_sns_session where user_id=? and user_id=?", new Object[]{3210,3210}); System.out.println( org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(tmp1) ); tmp1 = dbc.find("select * from t_sns_session where user_id=?", 3211); System.out.println( org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(tmp1) ); tmp1 = dbc.find("select * from t_sns_session where user_id=3212");
		 * System.out.println( org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(tmp1) ); tmp2 = dbc.query("select * from t_sns_session"); System.out.println( org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(tmp2) ); List<Object[]> params = new ArrayList<Object[]>(); params.add(new Object[]{3213, "20080920101111"}); params.add(new Object[]{3214, "20080920101111"}); params.add(new Object[]{3215, "20080920101111"}); dbc.batch("insert into t_sns_session (user_id, last_activity) values (?, ?)", params); tmp2 = dbc.query("select * from t_sns_session"); System.out.println( org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(tmp2) ); dbc.execute("delete from t_sns_session where user_id=? or user_id=?", new Object[]{3210, 3211}); dbc.execute("delete from t_sns_session where user_id=?", 3212); dbc.execute("delete from t_sns_session1");
		 * dbc.commit(); } catch (Exception e) { dbc.rollback(); throw e; } finally { dbc.close(); }
		 */
	}

}
