package com.opensource.dbhelp;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 管理DbHelper实例。
 * <p/>
 * Copyright: Copyright (c) 13-1-14 下午2:08
 * <p/>
 * Company: GNU General Public License
 * <p/>
 * Author: GNU General Public License
 * <p/>
 * Version: 1.0
 * <p/>
 */
public class DbHelperManager {

	private DbHelperManager() {
		// disable construct
	}

	private final static DbHelperManager INSTANCE = new DbHelperManager();

	/**
	 * construct
	 * 
	 * @return DbHelperManager实例
	 */
	public static DbHelperManager getInstance() {
		return INSTANCE;
	}

	private final Map<String, DbHelper> helpers = new HashMap<String, DbHelper>();

	/**
	 * 获得DbHelper实例
	 * 
	 * @param configName
	 *            数据源名
	 * @return DbHelper
	 */
	public DbHelper getHelper(String configName, String dialect) {
		synchronized (helpers) {
			if (helpers.containsKey(configName)) {
				return helpers.get(configName);
			} else {
				DataSource dataSource = new ComboPooledDataSource(configName);
				DbHelper dbHelper = new DbHelper(dataSource);
				if ("mysql".equals(dialect)) {
					dbHelper.setDialect("mysql");
				} else {
					dbHelper.setDialect("oracle");
				}
				helpers.put(configName, dbHelper);
				return dbHelper;
			}
		}
	}

	/**
	 * 获取默认数据库helper对象
	 * 
	 * @return DbHelper
	 */
	public static DbHelper getHelper() {
		return DbHelperManager.getInstance().getHelper(null, null);
	}

}
