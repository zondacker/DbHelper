/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.opensource.dbhelp.dbutils.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.opensource.dbhelp.dbutils.StringRowProcessor;

/**
 * <code>ResultSetHandler</code> implementation that converts the <code>ResultSet</code> into a <code>List</code> of <code>String[]</code>s. This class is thread safe.
 * 
 * @see com.opensource.dbhelp.dbutils.ResultSetHandler
 */
public class StringArrayListHandler extends AbstractListHandler<String[]> {

	/**
	 * The StringRowProcessor implementation to use when converting rows into String[]s.
	 */
	private final StringRowProcessor convert;

	/**
	 * Creates a new instance of ArrayListHandler using a <code>BasicStringRowProcessor</code> for conversions.
	 */
	public StringArrayListHandler() {
		this(StringArrayHandler.ROW_PROCESSOR);
	}

	/**
	 * Creates a new instance of ArrayListHandler.
	 * 
	 * @param convert
	 *            The <code>StringRowProcessor</code> implementation to use when converting rows into String[]s.
	 */
	public StringArrayListHandler(StringRowProcessor convert) {
		super();
		this.convert = convert;
	}

	/**
	 * Convert row's columns into an <code>Object[]</code>.
	 * 
	 * @return <code>Object[]</code>, never <code>null</code>.
	 * @throws java.sql.SQLException
	 *             if a database access error occurs
	 * @see com.opensource.dbhelp.dbutils.handlers.AbstractListHandler#handle(java.sql.ResultSet)
	 */
	@Override
	protected String[] handleRow(ResultSet rs) throws SQLException {
		return this.convert.toArray(rs);
	}

}
