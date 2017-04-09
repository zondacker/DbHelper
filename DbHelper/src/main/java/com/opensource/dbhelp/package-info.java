/**
 * 提供数据访问的封装。
 * <p>
 *
 * <pre>
 * 
 * // 获取默认helper实例
 * DbHelper oracleDbHelper = DbHelperManager.getHelper();
 * 
 * // oracle 数据库
 * DbHelper oracleDbHelper = DbHelperManager.getInstance().getHelper(&quot;oracle&quot;, &quot;oracle&quot;);
 * 
 * // mysql 数据库
 * DbHelper mySqlDbHelper = DbHelperManager.getInstance().getHelper(&quot;mysql&quot;, &quot;mysql&quot;);
 * 
 * // 事务对象
 * DbTransaction transaction = mySqlDbHelper.getDbTransaction();
 *
 * </pre>
 */
package com.opensource.dbhelp;