package com.cg.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.cg.bean.Account;
import com.cg.bean.Transaction;

public class WalletDAO implements IWalletDAO {
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	Connection con = null;

	public void connect() throws Exception {
		Class.forName("oracle.jdbc.OracleDriver");
		con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "hr", "hr");
	}

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

	public ResultSet getTransactions(long accNo) throws Exception {
		String query = "select * from bank_transactions where account_Number=" + accNo;
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);

		return rs;
	}

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

	public long addAccount(String name, double amt) throws Exception {
		String transactionType = "Opening Balance";
		Account acc = new Account();
		String query1 = "select max(Account_NUMBER) from bank_accounts";
		Statement st1 = con.createStatement();
		ResultSet rs1 = st1.executeQuery(query1);
		long accountNumber = 0;
		if (rs1.next())
			accountNumber = rs1.getLong(1);
		acc.setAccNo(accountNumber + 1);
		acc.setaName(name);
		acc.setaBalance(amt);
		st1.close();

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

		addTransaction(acc, transactionType, amt, ts);

		String query = "insert into bank_accounts values (?,?,?)";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1, "" + acc.getAccNo());
		pst.setString(2, acc.getaName());
		pst.setString(3, "" + acc.getaBalance());
		pst.executeUpdate();

		pst.close();
		return acc.getAccNo();
	}

	public void addTransaction(Account acc, String transactionType, double amt, Transaction ts) throws Exception {

		// System.out.println(ts.getTransactionID()+" : "+ts.getAccountNo()+" :
		// "+ts.getTransactionType()+" : "+ts.getAmount()+" : "+ts.getBalance()+" :
		// "+ts.getDateTime());
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

	public Double deposit(long accountNumber, double amount) throws Exception {
		String transactionType = "Deposit";
		Account acc = balInc(accountNumber, amount);

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

		addTransaction(acc, transactionType, amount, ts);
		String query2 = "update bank_accounts set account_balance = " + acc.getaBalance() + " where Account_number = "
				+ acc.getAccNo();
		// String query1 = "update bank_accounts set account_balance = ? where
		// Account_number = ?";
		PreparedStatement pst = con.prepareStatement(query2);
		// pst.setDouble(3, acc.getaBalance());
		// pst.setLong(1, acc.getAccNo());
		pst.executeUpdate();
		System.out.println("Deposit Successful");
		pst.close();
		return acc.getaBalance();
	}

	public Account balInc(long accountNumber, double amount) throws Exception {
		String query = "select * from bank_accounts where account_Number=" + accountNumber;
		Account acc = new Account();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery(query);
		rs.next();
		acc.setAccNo(rs.getLong(1));
		;
		acc.setaBalance(rs.getDouble(3) + amount);
		st.close();
		return acc;

	}

	public Double withdraw(long accountNumber, double amount) throws Exception {
		String transactionType = "Withdraw";
		Account acc = balDec(accountNumber, amount);

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

		addTransaction(acc, transactionType, amount, ts);
		String query2 = "update bank_accounts set account_balance = " + acc.getaBalance() + " where Account_number = "
				+ acc.getAccNo();
		// String query1 = "update bank_accounts set account_balance = ? where
		// Account_number = ?";
		PreparedStatement pst = con.prepareStatement(query2);
		// pst.setDouble(3, acc.getaBalance());
		// pst.setLong(1, acc.getAccNo());
		pst.executeUpdate();
		System.out.println("Withdraw Successful");
		pst.close();
		return acc.getaBalance();
	}

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

	public Double transfer(long accountNumber1, long accountNumber2, double amount) throws Exception {
		String transactionType = "Transfer-Out";
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

		transactionType = "Transfer-In";
		Account acc2 = balInc(accountNumber2, amount);
		ts.setTransactionID(transactionId + 1);
		ts.setAccountNo(acc2.getAccNo());
		ts.setTransactionType(transactionType);
		ts.setAmount(amount);
		ts.setBalance(acc2.getaBalance());
		ts.setDateTime(dtf.format(now));
		addTransaction(acc2, transactionType, amount, ts);
		System.out.println("Transfer Successfull");
		
		return acc1.getaBalance();

	}
}
