package com.example.demo.model;


import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

import java.time.Clock;
import java.time.OffsetDateTime;

//import javax.persistence.

@Entity
@Table(name = "user")
public class User  implements UserDetails{


	
		
		public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	
	
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	public String getVerified_on() {
		return verified_on;
	}
	public void setVerified_on(String verified_on) {
		this.verified_on = verified_on;
	}


	private boolean verified;
    private String verified_on;
    
		@Id
		@GeneratedValue(generator = "UUID")
	    @GenericGenerator(
	        name = "UUID",
	        strategy = "org.hibernate.id.UUIDGenerator"
	    )
		@Column(name = "id", unique = true, nullable = false, updatable = false)
		private String id;

		@NotEmpty @NotNull(message="First name cannot be missing or empty")
		@Column(name = "first_name")
		private String first_name;

		@NotEmpty @NotNull(message="Last name cannot be missing or empty")
		@Column(name = "last_name")
		private String last_name;

		@NotEmpty @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
		@Column(name = "password")
		private String password;
		
		@Email(message="Email not valid") @NotEmpty
		@Column(name = "username", unique = true, nullable = false)
	     @NotNull(message="Email cannot be missing or empty")
	    private String  username;
	    
	    
	    private String account_created;
	    private String account_updated;
	    
	    public User() {
	    	
	    }
	    
	    public User(String first_name2, String last_name2, String password2, String username2) {
			// TODO Auto-generated constructor stub
			this.id = UUID.randomUUID().toString();
			this.first_name = first_name2;
			this.last_name = last_name2;
			this.password = password2;
			this.username = username2;
			this.account_created = OffsetDateTime.now(Clock.systemUTC()).toString();
		    this.account_updated = OffsetDateTime.now(Clock.systemUTC()).toString();
		}
	    
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public boolean isAccountNonExpired() {
			// TODO Auto-generated method stub
			return true;
		}
		@Override
		public boolean isAccountNonLocked() {
			// TODO Auto-generated method stub
			return true;
		}
		@Override
		public boolean isCredentialsNonExpired() {
			// TODO Auto-generated method stub
			return true;
		}
		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return true;
		}
}

