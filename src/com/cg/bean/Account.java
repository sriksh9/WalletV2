package com.cg.bean;

public class Account {
	private long accNo;
	private String aName;
	private double aBalance;
	public Account() {
		// TODO Auto-generated constructor stub
	}
	public Account(Long accNo, String aName, double aBalance) {
		super();
		this.accNo = accNo;
		this.aName = aName;
		this.aBalance = aBalance;
	}
	public Long getAccNo() {
		return accNo;
	}
	public void setAccNo(Long accNo) {
		this.accNo = accNo;
	}
	public String getaName() {
		return aName;
	}
	public void setaName(String aName) {
		this.aName = aName;
	}
	public double getaBalance() {
		return aBalance;
	}
	public void setaBalance(double aBalance) {
		this.aBalance = aBalance;
	}
	@Override
	public String toString() {
		return "Account Details;\nAccount Number : " + accNo + "\nAccount Name : " + aName + "\nAccount Balance : " + aBalance;
	}
	
}
