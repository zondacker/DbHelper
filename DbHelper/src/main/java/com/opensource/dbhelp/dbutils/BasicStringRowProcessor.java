/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.opensource.dbhelp.dbutils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Basic implementation of the <code>StringRowProcessor</code> interface.
 * <p>
 * This class is thread-safe.
 * </p>
 *
 * @see StringRowProcessor
 */
public class BasicStringRowProcessor implements StringRowProcessor {

	/**
	 * The Singleton instance of this class.
	 */
	private static final BasicStringRowProcessor instance = new BasicStringRowProcessor();

	/**
	 * Returns the Singleton instance of this class.
	 *
	 * @return The single instance of this class.
	 * @deprecated Create instances with the constructors instead. This will be removed after DbUtils 1.1.
	 */
	@Deprecated
	public static BasicStringRowProcessor instance() {
		return instance;
	}

	/**
	 * BasicRowProcessor constructor.
	 */
	public BasicStringRowProcessor() {
	}

	/**
	 * Convert a <code>ResultSet</code> row into an <code>Object[]</code>. This implementation copies column values into the array in the same order they're returned from the <code>ResultSet</code>. Array elements will be set to <code>null</code> if the column was SQL NULL.
	 *
	 * @see com.cplatform.dbhelp.dbutils.StringRowProcessor#toArray(java.sql.ResultSet)
	 * @param rs
	 *            ResultSet that supplies the array data
	 * @throws java.sql.SQLException
	 *             if a database access error occurs
	 * @return the newly created array
	 */
	@Override
	public String[] toArray(ResultSet rs) throws SQLException {

		ResultSetMetaData meta = rs.getMetaData();
		int cols = meta.getColumnCount();
		String[] result = new String[cols];

		for (int i = 0; i < cols; i++) {
			result[i] = trimToEmpty(rs.getString(i + 1));
		}

		return result;
	}

	/**
	 * Convert a <code>ResultSet</code> row into a <code>Map</code>. This implementation returns a <code>Map</code> with case insensitive column names as keys. Calls to <code>map.get("COL")</code> and <code>map.get("col")</code> return the same value.
	 * 
	 * @see com.cplatform.dbhelp.dbutils.StringRowProcessor#toMap(java.sql.ResultSet)
	 * @param rs
	 *            ResultSet that supplies the map data
	 * @throws java.sql.SQLException
	 *             if a database access error occurs
	 * @return the newly created Map
	 */
	@Override
	public Map<String, String> toMap(ResultSet rs) throws SQLException {
		Map<String, String> result = new CaseInsensitiveHashMap();
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();

		for (int i = 1; i <= cols; i++) {
			result.put(rsmd.getColumnName(i), trimToEmpty(rs.getString(i)));
		}

		return result;
	}

	private static String trimToEmpty(String str) {
		return str != null ? str.trim() : "";
	}

	/**
	 * A Map that converts all keys to lowercase Strings for case insensitive lookups. This is needed for the toMap() implementation because databases don't consistently handle the casing of column names.
	 * <p>
	 * The keys are stored as they are given [BUG #DBUTILS-34], so we maintain an internal mapping from lowercase keys to the real keys in order to achieve the case insensitive lookup.
	 * <p>
	 * Note: This implementation does not allow <tt>null</tt> for key, whereas {@link java.util.HashMap} does, because of the code:
	 * 
	 * <pre>
	 * key.toString().toLowerCase()
	 * </pre>
	 */
	private static class CaseInsensitiveHashMap extends HashMap<String, String> {

		/**
		 * The internal mapping from lowercase keys to the real keys.
		 * <p>
		 * Any query operation using the key ({@link #get(Object)}, {@link #containsKey(Object)}) is done in three steps:
		 * <ul>
		 * <li>convert the parameter key to lower case</li>
		 * <li>get the actual key that corresponds to the lower case key</li>
		 * <li>query the map with the actual key</li>
		 * </ul>
		 * </p>
		 */
		private final Map<String, String> lowerCaseMap = new HashMap<String, String>();

		/**
		 * Required for serialization support.
		 *
		 * @see java.io.Serializable
		 */
		private static final long serialVersionUID = -2848101435296897392L;

		/** {@inheritDoc} */
		@Override
		public boolean containsKey(Object key) {
			Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
			return super.containsKey(realKey);
			// Possible optimisation here:
			// Since the lowerCaseMap contains a mapping for all the keys,
			// we could just do this:
			// return lowerCaseMap.containsKey(key.toString().toLowerCase());
		}

		/** {@inheritDoc} */
		@Override
		public String get(Object key) {
			Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
			return super.get(realKey);
		}

		/** {@inheritDoc} */
		@Override
		public String put(String key, String value) {
			/*
			 * In order to keep the map and lowerCaseMap synchronized, we have to remove the old mapping before putting the new one. Indeed, oldKey and key are not necessaliry equals. (That's why we call super.remove(oldKey) and not just super.put(key, value))
			 */
			Object oldKey = lowerCaseMap.put(key.toLowerCase(Locale.ENGLISH), key);
			String oldValue = super.remove(oldKey);
			super.put(key, value);
			return oldValue;
		}

		/** {@inheritDoc} */
		@Override
		public void putAll(Map<? extends String, ? extends String> m) {
			for (Map.Entry<? extends String, ? extends String> entry : m.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				this.put(key, value);
			}
		}

		/** {@inheritDoc} */
		@Override
		public String remove(Object key) {
			Object realKey = lowerCaseMap.remove(key.toString().toLowerCase(Locale.ENGLISH));
			return super.remove(realKey);
		}
	}

}