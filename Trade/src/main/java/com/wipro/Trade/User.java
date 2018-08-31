package com.wipro.Trade;

public class User {
	private String userid;
	private String password;
	private String email;
	private double balance;
	
	public User()
	{
		
	}

	public User(String userid, String password, String email) {
		super();
		this.userid = userid;
		this.password = password;
		this.email = email;
		this.balance=100000;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	

}
