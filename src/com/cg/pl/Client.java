
package com.cg.pl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

import com.cg.bean.Transaction;
import com.cg.service.IWalletService;
import com.cg.service.WalletService;

public class Client {

	private static Scanner sc;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		int choice = 0;
		//creating the object of service layer
		IWalletService ser = new WalletService();
		ser.connect();
		int op = 0; //Variable to check if the program to should iterate
		do {
			System.out.print("\n1->Register new account\n2->Account Details\n3->Deposit\n4->Withdraw\n5->Transfer\n6->MiniStatement");
			System.out.print("\nEnter your choice :");
			choice = sc.nextInt();
			switch (choice) {
			case 1: {
				System.out.println("\n---------------Register a new Acccount---------------\n");
				System.out.print("\nEnter the name : ");
				String name = sc.next();
				System.out.print("\nEnter the opening balance : ");
				double balance = sc.nextDouble();
				System.out.print("\nYour Account Number is :"+ser.addAccount(name, balance));
				break;
			}
			case 2: {
				System.out.println("\n---------------Account Details---------------");
				System.out.print("\nEnter the account number : ");
				long accountNumber = sc.nextLong();
				System.out.println(ser.getAccount(accountNumber));
				break;
			}
			case 3: {
				System.out.println("\n---------------Money Deposit---------------");
				System.out.print("\nEnter Ac Number : ");
				long accountNumber = sc.nextLong();
				System.out.print("\nAmount : ");
				double amount = sc.nextDouble();
				System.out.println("Your updated balance is : "+ser.deposit(accountNumber, amount));
				break;
			}
			case 4: {
				System.out.println("\n---------------Money Withdrawl---------------");
				System.out.print("\nEnter Ac Number : ");
				long accountNumber = sc.nextLong();
				System.out.print("\nAmount : ");
				double amount = sc.nextDouble();
				System.out.println("Your updated balance is : "+ser.withdraw(accountNumber, amount));
				break;

			}
			case 5: {
				System.out.println("\n---------------Money Transfer---------------");
				System.out.print("\nEnter your A/c Number :");
				long accountNumber1 = sc.nextLong();
				System.out.print("\nEnter the A/c to be credited : ");
				long accountNumber2 = sc.nextLong();
				System.out.print("\nAmount : ");
				double amount = sc.nextDouble();
				System.out.println("Your updated balance is : "+ser.transfer(accountNumber1, accountNumber2, amount));
				break;
			}
			case 6: {
				System.out.println("\n------------------------------Show Transactions------------------------------");
				Transaction tc=new Transaction();
				System.out.print("\nEnter the account no. :");
				long accountNumber = sc.nextLong();
				System.out.println();
				ResultSet rs = ser.getTransactions(accountNumber);//Obtain the entire result set from the database.
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnsNumber = rsmd.getColumnCount();//Obtaining the column count, for the proper formatted display of the entire table
				while(rs.next()) {
					
					//Printing with the Transaction "toString() Method. However, this mode of printing is not recommended."
					
					/*tc.setTransactionID(rs.getLong(1));
					tc.setAccountNo(rs.getLong(2));
					tc.setTransactionType(rs.getString(3));
					tc.setAmount(rs.getDouble(4));
					tc.setBalance(rs.getDouble(5));
					tc.setDateTime(rs.getString(6));
					System.out.println(tc);*/
					
					//iterating the entire row for obtaining the column
					for(int i = 1 ; i <= columnsNumber; i++){
					      System.out.print(rs.getString(i) + "	"); //Print one element of a row
					}
					System.out.println();
				}
				break;
			}

			}
			System.out.print("\nDo you wish to continue? 0->No, any other number->yes");
			op = sc.nextInt();

		} while (op != 0);
		System.out.println("------------------------Thank you for banking with us------------------------");

	}

}

