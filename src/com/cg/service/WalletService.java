package com.cg.service;

import java.sql.ResultSet;

import com.cg.bean.Account;
import com.cg.dao.IWalletDAO;
import com.cg.dao.WalletDAO;

public class WalletService implements IWalletService {
	
	IWalletDAO dao = new WalletDAO();
	
	public void connect() throws Exception {
		dao.connect();
	}
	
	public Account getAccount(long accNo) throws Exception{
		return dao.getAccount(accNo);
	}
	
	public long addAccount(String name, double amt) throws Exception {
		return dao.addAccount(name, amt);
	}
	
	public ResultSet getTransactions(long accNo) throws Exception{
		return dao.getTransactions(accNo);
	}
	
	public Double deposit(long accountNumber, double amount) throws Exception{
		return dao.deposit(accountNumber, amount);
	}
	
	public Double withdraw(long accountNumber, double amount) throws Exception{
		return dao.withdraw(accountNumber, amount);
	}
	
	public Double transfer(long accountNumber1, long accountNumber2, double amount) throws Exception{
		return dao.transfer(accountNumber1, accountNumber2, amount);
	}
}
