package com.cg.service;

import java.sql.ResultSet;

import com.cg.bean.Account;
import com.cg.dao.IWalletDAO;
import com.cg.dao.WalletDAO;

public class WalletService implements IWalletService {
	
	IWalletDAO dao = new WalletDAO();
	//Calling the Connect() method form DAO layer
	public void connect() throws Exception {
		dao.connect();
	}
	
	//Calling the getAccount method form DAO layer
	public Account getAccount(long accNo) throws Exception{
		return dao.getAccount(accNo);
	}
	
	//Calling the addAccount() method form DAO layer
	public long addAccount(String name, double amt) throws Exception {
		return dao.addAccount(name, amt);
	}
	
	//Calling the getTransactions() method form DAO layer
	public ResultSet getTransactions(long accNo) throws Exception{
		return dao.getTransactions(accNo);
	}
	
	//Calling the deposit() method form DAO layer
	public Double deposit(long accountNumber, double amount) throws Exception{
		return dao.deposit(accountNumber, amount);
	}
	
	//Calling the withdraw() method form DAO layer
	public Double withdraw(long accountNumber, double amount) throws Exception{
		return dao.withdraw(accountNumber, amount);
	}
	
	//Calling the transfer() method form DAO layer
	public Double transfer(long accountNumber1, long accountNumber2, double amount) throws Exception{
		return dao.transfer(accountNumber1, accountNumber2, amount);
	}
}
