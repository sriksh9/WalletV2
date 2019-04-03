package com.cg.bean;

public class Transaction {
	private long transactionID;
	private long accountNo;
	private String transactionType;
	private double amount;
	private double balance;
	private String dateTime;
	
	public Transaction() {
		// TODO Auto-generated constructor stub
	}

	public Transaction(long transactionID, long accountNo, String transactionType, double amount, double balance,
			String dateTime) {
		super();
		this.transactionID = transactionID;
		this.accountNo = accountNo;
		this.transactionType = transactionType;
		this.amount = amount;
		this.balance = balance;
		this.dateTime = dateTime;
	}

	public long getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(long transactionID) {
		this.transactionID = transactionID;
	}

	public long getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	@Override
	public String toString() {
		return "---------------------------------------------------------------------------------------------"
				+"\nTransaction Details;\nTransactionID : " + transactionID + "\nAssociated Account Number : " + accountNo + "\nTransaction Type : "
				+ transactionType + "\nAmount=" + amount + "\nAccount Balance=" + balance + "Date & Time=" + dateTime;
	}
	
	
}
