package com.cg.dao;

import java.sql.ResultSet;

import com.cg.bean.Account;
import com.cg.bean.Transaction;

public interface IWalletDAO {
	public void connect() throws Exception;
	public Account getAccount(long accNo) throws Exception;
	public ResultSet getTransactions(long accNo) throws Exception;
	public void printAccountsTable() throws Exception;
	public long addAccount(String name, double amt) throws Exception;
	public void addTransaction(Account acc, String transactionType, double amt, Transaction ts) throws Exception;
	public void printTransactionsTable() throws Exception;
	public Double deposit(long accountNumber, double amount) throws Exception;
	public Account balInc(long accountNumber, double amount) throws Exception;
	public Double withdraw(long accountNumber, double amount) throws Exception;
	public Account balDec(long accountNumber, double amount) throws Exception;
	public Double transfer(long accountNumber1, long accountNumber2, double amount) throws Exception;
	
}
