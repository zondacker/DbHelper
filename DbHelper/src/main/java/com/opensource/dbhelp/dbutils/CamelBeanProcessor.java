package com.opensource.dbhelp.dbutils;

import java.beans.PropertyDescriptor;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.lang3.text.WordUtils;

/**
 * 记录集字段及bean属性之间的映射转换实现类。
 * <p/>
 * 重写dbutils中“beanProcessor”的"mapColumnsToProperties"方法， 使其可以实现数据表user_id到对象userId和user_id到对象属性user_id之间的自动转换。 注：引用时需要注意的是平台优先处理“user_id到对象userId”。
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
public class CamelBeanProcessor extends BeanProcessor {

	/**
	 * 对象属性和字段关系映射
	 *
	 * @param rsmd
	 *            元字符结果集
	 * @param props
	 *            对象属性数组
	 * @return 对象和数据表字段关系位置数组
	 */
	@Override
	protected int[] mapColumnsToProperties(ResultSetMetaData rsmd, PropertyDescriptor[] props) throws SQLException {

		int cols = rsmd.getColumnCount();
		int columnToProperty[] = new int[cols + 1];
		Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

		for (int col = 1; col <= cols; col++) {
			String columnName = rsmd.getColumnLabel(col);
			if (null == columnName || 0 == columnName.length()) {
				columnName = rsmd.getColumnName(col);
			}

			for (int i = 0; i < props.length; i++) {
				if (formatColName(columnName).equalsIgnoreCase(props[i].getName())) {
					columnToProperty[col] = i;
					break;
				}
				if (columnName.equalsIgnoreCase(props[i].getName())) {
					columnToProperty[col] = i;
					break;
				}
			}
		}

		return columnToProperty;
	}

	/**
	 * 格式化字符将user_id或USER_ID字段返回成userId
	 *
	 * @param name
	 *            字段名
	 * @return 转化后的属性名
	 */
	private String formatColName(String name) {
		if (name == null || "".equals(name)) {
			return "";
		}
		String rstr = name.toLowerCase();
		rstr = WordUtils.uncapitalize(WordUtils.capitalize(rstr, "_".toCharArray()));
		rstr = rstr.replaceAll("_", "");
		return rstr;
	}

}
