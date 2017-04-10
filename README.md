# DbHelper
DbHelper是支持定制化SQL、存储过程以及高级映射的高效性持久层框架，其主要就完成2件事情：封装JDBC操作；利用反射打通Java类与SQL语句之间的相互转换。DbHelper的主要设计目的是让我们对执行SQL语句时对输入输出的数据管理更加方便，高效快捷地写出SQL和方便地获取SQL的执行结果。
# 使用方法
1. maven引入jar包： 
```
<dependency>
	<groupId>com.opensource</groupId>
	<artifactId>dbhelper</artifactId>
	<version>1.0.0</version>
</dependency>
```
2. 在Spring配置文件里配置DbHelper：  
```
<!-- 数据源配置,推荐使用Druid数据库连接池 -->
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
	<property name="url" value="${db.jdbcUrl}" />
	<property name="username" value="${db.user}" />
	<property name="password" value="${db.password}" />
	<property name="driverClassName" value="${db.driverClass}" />
	<!-- 配置初始化大小、最小、最大 -->
	<property name="initialSize" value="${db.initialSize}" />
	<property name="minIdle" value="${db.minIdle}" />
	<property name="maxActive" value="${db.maxActive}" />
	<!-- 配置获取连接等待超时的时间 -->
	<property name="maxWait" value="${db.maxWait}" />
	<!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
	<property name="timeBetweenEvictionRunsMillis" value="${db.timeBetweenEvictionRunsMillis}" />
	<!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
	<property name="minEvictableIdleTimeMillis" value="${db.minEvictableIdleTimeMillis}" />
	<property name="validationQuery" value="SELECT 'x' from dual" />
	<property name="testWhileIdle" value="true" />
	<property name="testOnBorrow" value="false" />
	<property name="testOnReturn" value="false" />
	<!-- 打开PSCache，并且指定每个连接上PSCache的大小 -->
	<property name="poolPreparedStatements" value="${db.poolPreparedStatements}" />
	<property name="maxPoolPreparedStatementPerConnectionSize" value="${db.maxPoolPreparedStatementPerConnectionSize}" />
	<!-- 配置监控统计拦截的filters -->
	<property name="filters" value="stat,log4j" />
	<property name="proxyFilters">
	    <list>
		<ref bean="log-filter" />
	    </list>
	</property>
</bean>

<bean id="transactionAwareDataSourceProxy" class="org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy">
	<constructor-arg ref="dataSource" />
</bean>
 <!-- 配置DbHelper -->
<bean id="dbHelper" class="com.opensource.dbhelp.DbHelper">
	<constructor-arg ref="transactionAwareDataSourceProxy" />
</bean>
```
3. 在DAO层注入DbHelper并使用：  
```
public class TeseDao {

	@Autowired
	private DbHelper dbHelper;

	/**
	 * 查询用户列表
	 * 
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public List<User> getUserList(User user) throws SQLException {
	  StringBuilder sql = new StringBuilder();
	  List<Object> params = new ArrayList<Object>();
	  sql.append("SELECT TU.* FROM T_USER TU\n");
	  sql.append(" WHERE 1 = 1\n");
	     if (user != null && StringUtils.isNotEmpty(user.getName)) {
		sql.append(" AND TU.NAME LIKE ?\n");
		params.add("%" + param.getName().trim() + "%");
	  }
	  return dbHelper.getBeanList(sql.toString(), User.class, params.toArray());
	}

}
```
