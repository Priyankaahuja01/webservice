package com.example.demo.response;

import java.time.LocalDateTime;

public class UserResponse {

	private String id;
	private String firstName;
	private String lastName;
	private String account_created;
	private String account_updated;
	private String username;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAccount_created() {
		return account_created;
	}
	public void setAccount_created(String account_created) {
		this.account_created = account_created;
	}
	public String getAccount_updated() {
		return account_updated;
	}
	public void setAccount_updated(String account_updated) {
		this.account_updated = account_updated;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
}
