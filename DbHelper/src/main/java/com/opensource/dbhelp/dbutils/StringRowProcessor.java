/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.opensource.dbhelp.dbutils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * <code>StringRowProcessor</code> implementations convert <code>ResultSet</code> rows into various other objects. Implementations can extend <code>BasicStringRowProcessor</code> to protect themselves from changes to this interface.
 *
 * @see BasicStringRowProcessor
 */
public interface StringRowProcessor {

	/**
	 * Create an <code>String[]</code> from the column values in one <code>ResultSet</code> row. The <code>ResultSet</code> should be positioned on a valid row before passing it to this method. Implementations of this method must not alter the row position of the <code>ResultSet</code>.
	 *
	 * @param rs
	 *            ResultSet that supplies the array data
	 * @throws java.sql.SQLException
	 *             if a database access error occurs
	 * @return the newly created array
	 */
	String[] toArray(ResultSet rs) throws SQLException;

	/**
	 * Create a <code>Map</code> from the column values in one <code>ResultSet</code> row. The <code>ResultSet</code> should be positioned on a valid row before passing it to this method. Implementations of this method must not alter the row position of the <code>ResultSet</code>.
	 *
	 * @param rs
	 *            ResultSet that supplies the map data
	 * @throws java.sql.SQLException
	 *             if a database access error occurs
	 * @return the newly created Map
	 */
	Map<String, String> toMap(ResultSet rs) throws SQLException;

}