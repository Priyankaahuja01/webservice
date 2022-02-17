package com.example.demo.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class Multitenantmanager implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(Multitenantmanager.class);
	
	private final Map<Object, Object> tenantDataSources = new ConcurrentHashMap<>();
	
	@Autowired
	private AppRoutingDataSource routingDataSource;
	

	@Value("${spring.datasource.url:jdbc:mysql://localhost:3306/crudusers}")
    private String main_url;
	
	@Value("${spring.datasource.username:root}")
    private String main_username;
	
	@Value("${spring.datasource.password:Dimdim@01}")
    private String main_password;
	
	

	public void setCurrentTenant(String tenantId)  {
		if (tenantIsAbsent(tenantId)) {
			throw new RuntimeException("No tenant with ID " + tenantId);
		}
		
		routingDataSource.setCurrentTenant(tenantId);
		logger.debug("Tenant '{}' set as current.", tenantId);
	}

	
	public void addTenant(String tenantId, String url, String username, String password) throws SQLException {
		DataSource dataSource = DataSourceBuilder.create()
				.driverClassName("com.mysql.cj.jdbc.Driver")
				.url(url)
				.username(username)
				.password(password)
				.build();

		try(Connection c = dataSource.getConnection()) {
			tenantDataSources.put(tenantId, dataSource);
			routingDataSource.setTargetDataSources(tenantDataSources);
			routingDataSource.afterPropertiesSet();
			logger.debug("Tenant '{}' added.", tenantId);
		}
	}

	public DataSource removeTenant(String tenantId) {
		Object removedDataSource = tenantDataSources.remove(tenantId);
		routingDataSource.setTargetDataSources(tenantDataSources);
		routingDataSource.afterPropertiesSet();
		return (DataSource) removedDataSource;
	}

	public boolean tenantIsAbsent(String tenantId) {
		return !tenantDataSources.containsKey(tenantId);
	}

	public Collection<Object> getTenantList() {
		return tenantDataSources.keySet();
	}	
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			addTenant("all", main_url, main_username, main_password);
			//addTenant("get", sec_url, sec_username, sec_password);
		} catch (Exception e ) {
			e.printStackTrace();
		}
	}
}
