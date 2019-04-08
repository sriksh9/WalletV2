package com.cg.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.cg.bean.Account;
import com.cg.bean.Transaction;

public class WalletDAO implements IWalletDAO {
	//DateTimeFormatter object, for formatting the obtained date in the given format.
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	
	Connection con = null;
	
	//To establish the connection between the application and the Oracle database
	public void connect() throws Exception {
		Class.forName("oracle.jdbc.OracleDriver");
		con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "hr");
	}
	
	//Method to return the Account object when ever required.
	public Account getAccount(long accNo) throws Exception {
		String query = "select * from bank_accounts where account_Number=" + accNo;
		Account acc = new Account();
		acc.setAccNo(accNo);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		rs.next();
		acc.setaName(rs.getString(2));
		acc.setaBalance(rs.getDouble(3));
		return acc;
	}
	
	//Method to obtain the transaction details of the given account number.
	public ResultSet getTransactions(long accNo) throws Exception {
		String query = "select * from bank_transactions where account_Number=" + accNo;
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		return rs;
	}
	
	//Method to print the Accounts registered with the bank. 
	//This is not mandatory, created just for verifying the database in testing phase
	public void printAccountsTable() throws Exception {
		String query = "select * from bank_accounts";
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			String userData = rs.getLong(1) + " : " + rs.getString(2) + " : " + rs.getDouble(3);
			System.out.println(userData);
		}
		st.close();
	}
	
	//Method to Add new account to the database
	public long addAccount(String name, double amt) throws Exception {
		String transactionType = "Opening Balance";
		Account acc = new Account();
		//To generate unique account number for the new user.
		String query1 = "select max(Account_NUMBER) from bank_accounts";
		Statement st1 = con.createStatement();
		ResultSet rs1 = st1.executeQuery(query1);
		long accountNumber = 0;
		if (rs1.next())
			accountNumber = rs1.getLong(1);
		//assigning values to the dummy object of Account Class 
		acc.setAccNo(accountNumber + 1);
		acc.setaName(name);
		acc.setaBalance(amt);
		st1.close();
		
		
		//Generating Dummy Transaction object for the making the transaction log
		Transaction ts = new Transaction();
		String query2 = "select max(TRANSACTION_ID) from bank_transactions";
		Statement st2 = con.createStatement();
		ResultSet rs2 = st2.executeQuery(query2);
		long transactionId = 0;
		if (rs2.next())
			transactionId = rs2.getLong(1);
		ts.setTransactionID(transactionId + 1);
		ts.setAccountNo(acc.getAccNo());
		ts.setTransactionType(transactionType);
		ts.setAmount(amt);
		ts.setBalance(acc.getaBalance());
		LocalDateTime now = LocalDateTime.now();
		ts.setDateTime(dtf.format(now));
		st2.close();
		
		//passing the elements of this transaction for logging into transaction table
		addTransaction(acc, transactionType, amt, ts);
		
		//logging new account into account table
		String query = "insert into bank_accounts values (?,?,?)";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1, "" + acc.getAccNo());
		pst.setString(2, acc.getaName());
		pst.setString(3, "" + acc.getaBalance());
		pst.executeUpdate();

		pst.close();
		return acc.getAccNo();
	}
	
	//Method to add the transaction to the database when ever a new transaction is generated.
	public void addTransaction(Account acc, String transactionType, double amt, Transaction ts) throws Exception {
		String query = "insert into bank_transactions values (?,?,?,?,?,?)";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setLong(1, ts.getTransactionID());
		pst.setLong(2, ts.getAccountNo());
		pst.setString(3, ts.getTransactionType());
		pst.setDouble(4, ts.getAmount());
		pst.setDouble(5, ts.getBalance());
		pst.setString(6, ts.getDateTime());
		pst.executeUpdate();
		pst.close();

	}

	//Method to print the Accounts registered with the bank. 
	//This is not mandatory, created just for verifying the database in testing phase
	public void printTransactionsTable() throws Exception {
		String query = "select * from bank_transactions";
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		while (rs.next()) {
			String userData = rs.getLong(1) + " : " + rs.getLong(2) + " : " + rs.getString(3) + " : " + rs.getDouble(4)
					+ " : " + rs.getDouble(5) + " : " + rs.getString(6);
			System.out.println(userData);
		}
		st.close();
	}
	
	//Method to deposit the amount to account balance and record the transaction
	public Double deposit(long accountNumber, double amount) throws Exception {
		String transactionType = "Deposit";
		//updating the account balance
		Account acc = balInc(accountNumber, amount);
		
		//Generating unique transaction ID
		Transaction ts = new Transaction();
		String query1 = "select max(TRANSACTION_ID) from bank_transactions";
		Statement st1 = con.createStatement();
		ResultSet rs1 = st1.executeQuery(query1);
		long transactionId = 0;
		if (rs1.next())
			transactionId = rs1.getLong(1);
		ts.setTransactionID(transactionId + 1);
		ts.setAccountNo(acc.getAccNo());
		ts.setTransactionType(transactionType);
		ts.setAmount(amount);
		ts.setBalance(acc.getaBalance());
		LocalDateTime now = LocalDateTime.now();
		ts.setDateTime(dtf.format(now));
		st1.close();
		//passing the elements of this transaction for logging into transaction table
		addTransaction(acc, transactionType, amount, ts);
		//updating account details in account table
		updateAccount(acc);
		return acc.getaBalance();
	}
	
	//Method to increment the balance of the account by given amount 
	public Account balInc(long accountNumber, double amount) throws Exception {
		String query = "select * from bank_accounts where account_Number=" + accountNumber;
		Account acc = new Account();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		rs.next();
		acc.setAccNo(rs.getLong(1));
		acc.setaBalance(rs.getDouble(3) + amount);
		st.close();
		return acc;

	}
	
	//Method to withdraw the amount from account balance and record the transaction
	public Double withdraw(long accountNumber, double amount) throws Exception {
		String transactionType = "Withdraw";
		//updating the account balance
		Account acc = balDec(accountNumber, amount);
		
		//Generating unique transaction ID
		Transaction ts = new Transaction();
		String query1 = "select max(TRANSACTION_ID) from bank_transactions";
		Statement st1 = con.createStatement();
		ResultSet rs1 = st1.executeQuery(query1);
		long transactionId = 0;
		if (rs1.next())
			transactionId = rs1.getLong(1);
		ts.setTransactionID(transactionId + 1);
		ts.setAccountNo(acc.getAccNo());
		ts.setTransactionType(transactionType);
		ts.setAmount(amount);
		ts.setBalance(acc.getaBalance());
		LocalDateTime now = LocalDateTime.now();
		ts.setDateTime(dtf.format(now));
		st1.close();
		
		//passing the elements of this transaction for logging into transaction table
		addTransaction(acc, transactionType, amount, ts);
		//updating account details in account table
		updateAccount(acc);
		return acc.getaBalance();
	}
	
	//Method to update the Accounts table
	public void updateAccount(Account acc) throws Exception {
		String query2 = "update bank_accounts set account_balance = " + acc.getaBalance() + " where Account_number = "
				+ acc.getAccNo();
		PreparedStatement pst = con.prepareStatement(query2);
		pst.executeUpdate();
		pst.close();
	}
	
	//Method to decrement the balance of the account by given amount
	public Account balDec(long accountNumber, double amount) throws Exception {
		String query = "select * from bank_accounts where account_Number=" + accountNumber;
		Account acc = new Account();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		rs.next();
		acc.setAccNo(rs.getLong(1));
		acc.setaBalance(rs.getDouble(3) - amount);
		st.close();
		return acc;
	}
	
	//Method to transfer the amount from account1 to account2 and record the transaction
	public Double transfer(long accountNumber1, long accountNumber2, double amount) throws Exception {
		String transactionType = "Transfer-Out";
		//Deducting the amount from Account1's balance
		Account acc1 = balDec(accountNumber1, amount);

		Transaction ts = new Transaction();
		String query1 = "select max(TRANSACTION_ID) from bank_transactions";
		Statement st1 = con.createStatement();
		ResultSet rs1 = st1.executeQuery(query1);
		long transactionId = 0;
		if (rs1.next())
			transactionId = rs1.getLong(1);
		ts.setTransactionID(transactionId + 1);
		ts.setAccountNo(acc1.getAccNo());
		ts.setTransactionType(transactionType);
		ts.setAmount(amount);
		ts.setBalance(acc1.getaBalance());
		LocalDateTime now = LocalDateTime.now();
		ts.setDateTime(dtf.format(now));
		st1.close();
		addTransaction(acc1, transactionType, amount, ts);
		updateAccount(acc1);
		//incrementing the amount to Account2's balance
		transactionType = "Transfer-In";
		Account acc2 = balInc(accountNumber2, amount);
		ts.setTransactionID(transactionId + 1);
		ts.setAccountNo(acc2.getAccNo());
		ts.setTransactionType(transactionType);
		ts.setAmount(amount);
		ts.setBalance(acc2.getaBalance());
		ts.setDateTime(dtf.format(now));
		addTransaction(acc2, transactionType, amount, ts);
		updateAccount(acc2);
		System.out.println("Transfer Successfull");
		
		return acc1.getaBalance();

	}
}
