package com.example.demo.config;



import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class Datasource {

	
	@Value("${spring.datasource.url:jdbc:mysql://localhost:3306/crudusers}")
    private String main_url;
	
	@Value("${spring.datasource.username:root}")
    private String main_username;
	
	@Value("${spring.datasource.password:Dimdim@01}")
    private String main_password;
	
	
	
	@Bean
	public AppRoutingDataSource dataSource() {
		AppRoutingDataSource multiTenantDataSource = new AppRoutingDataSource();
		
		multiTenantDataSource.setTargetDataSources(new ConcurrentHashMap<>());
		multiTenantDataSource.setDefaultTargetDataSource(defaultDataSource());
		multiTenantDataSource.afterPropertiesSet();
		
		return multiTenantDataSource;
	}
	
	
	private DriverManagerDataSource defaultDataSource() {
		DriverManagerDataSource defaultDataSource = new DriverManagerDataSource();
		defaultDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		defaultDataSource.setUrl(main_url);
		defaultDataSource.setUsername(main_username);
		defaultDataSource.setPassword(main_password);
		return defaultDataSource;
	}
}
