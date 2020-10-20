package com.company;

import java.util.*;

import java.io.*;
import java.io.IOException;
import java.time.LocalDateTime;  // Import the LocalDateTime class
import java.time.format.DateTimeFormatter;  // Import the DateTimeFormatter
class Bank implements Serializable
{
    static int customercount = 0;
    String bankName;
    String bankAddress;
    String adminUserName;
    String adminPassWord;


    Bank()
    {
        adminUserName = "admin";
        adminPassWord = "admin";
    }
}

class Account extends Bank implements Serializable
{
    String accountID;
    float balance = 0;
    float amt;


    void scanAccountDetails()
    {
        /* Scan the account details such as initial balance while opening the account */
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the Opening Balance greater than 10,000 : ");
        while (true)
        {
            balance = sc.nextFloat();
            if (balance>=10000) // validate the balance if it is greater than 10,000
            {
                break;
            }
            else // if initial balance is less than the it will ask again
            {
                System.out.print("Enter the amount greater than or equal to 10,000 : ");
            }
        }


    }

    void displayAccountDetails()
    {
        /*It displays the account detail like account id and balance of account holder */
        System.out.format("%10s  |  %.1f\n",accountID,balance);

    }



    float deposit(float balance1)
    {
        /* returns the float value of updated balance after depositing the amount in customer account */
        Scanner sc = new Scanner(System.in);
        System.out.println("Your Account Balance is "+balance1);
        System.out.print("Enter Amount U Want to Deposit : ");
        amt = sc.nextFloat();
        if(amt<0)  //it does not allow to deposit a negative amount
        {
            System.out.println("\nAmount entered is negative.......... so you can not withdraw \nPRESS ENTER FOR MORE OPTION");
        }
        else
        {
            balance1 = balance1+amt;
            System.out.println("\nTransaction successful!........\nFinal Balance is "+balance1+"\nPRESS ENTER FOR MORE OPTION");
            return balance1;
        }
        return balance1 ;//return the updated balance
    }


    float withdrawal(float balance1)
    {
        // return s the float value of updated value after withdrawing money from customer account
        Scanner sc = new Scanner(System.in);
        while(true)
        {

            System.out.println("Your Account Balance is "+balance1);
            System.out.print("Enter Amount U Want to withdraw : ");
            amt = sc.nextFloat();
            if(amt<0) // does not allow to withdraw in negative value
            {
                System.out.println("\nAmount entered is negative.......... so you can not withdraw \nPRESS ENTER FOR MORE OPTION");
                break;
            }
            else if(balance1==1000)// if the balance is 1,000 you can not withdraw as minimum balance should be 1,000
            {
                System.out.println("\nyour balance is 1,000.......... so you can not withdraw \nPRESS ENTER FOR MORE OPTION");
                break;
            }
            else if(balance1>=amt && balance1-amt>=1000)
            {
                balance1 = balance1-amt;
                System.out.println("\nTransaction successful!........\nFinal Balance is "+balance1+"\nPRESS ENTER FOR MORE OPTION");
                return balance1; // returns the updated the value
            }


            else
            {
                System.out.println("\nLess Balance or minimuim balance should be 1000..Transaction Failed.......\nPRESS ENTER FOR MORE OPTION");
                break;
            }

        }
        return balance1;
    }


}

class Customer extends Account implements Serializable
{
    String name ;
    // String[] address = new String[100];
    String customerUser;
    String customerPass;
    String dateCreated;
    String types;
    Customer()
    {}
    public Customer(Customer obj)
    {
        this.accountID = obj.accountID;
        this.name = obj.name;
        this.customerUser = obj.customerUser;
        this.customerPass = obj. customerPass;
        this.balance = obj.balance;
        this.dateCreated = obj.dateCreated;
        this.types=obj.types;
    }

    void scanCustomerDetails(String year) throws IOException
    {
        /* It scans all realated fields to the customer such as name, username, password, datecreated */
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj3 = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
        String fulldate = myDateObj.format(myFormatObj3); //It generates the current time at which the account is created
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Account Types: \n 1.Saving Account \n 2.Current Account");
        int s=sc.nextInt();
        if (s==1){
            types="Saving Account";
        }else {
            types="Current Account";
        }
        sc.nextLine();
        System.out.print("Enter the Customer Name : ");
        name = sc.nextLine().toUpperCase();
        // System.out.print("Enter the Address : ");
        // address[st] = sc.nextLine();
        System.out.print("Enter the Customer Username : ");
        customerUser = sc.nextLine();
        System.out.print("Enter the Customer Password : ");
        customerPass = sc.nextLine();
        scanAccountDetails();
        //it auto-generate the account id using the year + first two character in uppercase + customer count
        accountID = year + name.substring(0,2).toUpperCase() + Integer.toString(customercount +101);
        System.out.println("\nYour Account ID  : "+accountID);
        System.out.println("Account Created on : "+fulldate);
        dateCreated = fulldate;
        File objfile = new File("C:\\Users\\HP\\IdeaProjects\\Classroom1\\src\\com\\company\\Bank\\"+accountID);//make a file of customer using accountid of customer id
        FileOutputStream fos = new FileOutputStream(objfile);
        ObjectOutputStream objOs=new ObjectOutputStream(fos);
        objOs.writeObject(this);// write whole object in the file created of name accountID
        fos.close();
        objOs.close();
    }

    void displaycustomerDetails(String identity) throws IOException, ClassNotFoundException
    {
        // displayAccountDetails();
        /*It display all the customer details in atabular form  */
        // opening the file of name identity which is the account id of the customer
        FileInputStream fis = new FileInputStream("C:\\Users\\HP\\IdeaProjects\\Classroom1\\src\\com\\company\\Bank\\"+identity);
        ObjectInputStream objIS = new ObjectInputStream(fis);
        Customer newObject = (Customer)objIS.readObject(); //cast object to customer class and readthe object from the file
        System.out.printf("%10s  |%10s  |%9s    |  %10s   |  %.1f  \n", newObject.accountID, newObject.customerUser, newObject.name, newObject.dateCreated, newObject.balance);
        fis.close();
        objIS.close();
    }


    boolean checkcustomer(String username, String password, String acc ) throws IOException,ClassNotFoundException
    {
        /*It check the username and password while we login into the customer login */
        FileInputStream fis = new FileInputStream("C:\\Users\\HP\\IdeaProjects\\Classroom1\\src\\com\\company\\Bank\\"+acc);
        ObjectInputStream objIS = new ObjectInputStream(fis);
        Customer newObject = (Customer)objIS.readObject();
        String tempuser = newObject.customerUser;
        String temppass = newObject.customerPass;
        if(tempuser.equals(username) && temppass.equals(password)) //If the username and password matches it returns true otherwise false
        {
            return  true;
        }

        return false;
    }

}


class Myexception extends Exception
{
    Myexception(String msg)
    {
        super(msg);
    }
}

public class FileBankTranscation implements Serializable
{
    public static void main(String[] args) throws IOException, ClassNotFoundException,FileNotFoundException
    {
        clrscr();
        Customer[] obj = new Customer[50];// making the object of customer class
        Bank bk = new Bank();// making object of bank class
        File objFile = new File("C:\\Users\\HP\\IdeaProjects\\Classroom1\\src\\com\\company\\Bank");
        if(!objFile.exists())// if the bank folder does not exists it will create it
        {
            objFile.mkdir();
        }
        String name[] = objFile.list();
        Bank.customercount = name.length;
        // using the date and time library for the validation of the saturdays and sundays
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E");
        DateTimeFormatter myFormatObj1 = DateTimeFormatter.ofPattern("dd");
        DateTimeFormatter myFormatObj2 = DateTimeFormatter.ofPattern("yyyy");
        int date = Integer.parseInt(myDateObj.format(myFormatObj1));
        String day = myDateObj.format(myFormatObj);
        String year = myDateObj.format(myFormatObj2);
        Scanner sc = new Scanner(System.in);

        while(true)
        {
            int n,n1,n2,n3;
            boolean rep = true;
            clrscr();
            System.out.println("$$$$$$$$$$$$$$  Bank Application  $$$$$$$$$$$$$$");
            System.out.println("1. Admin Login\n2. Customer Login\n3. Exit");
            n = sc.nextInt();
            switch (n)
            {
                case 1:
                    // Admin Login Menu
                    // It is a admin login in which we can make a new account and check balance and details of customer
                    // using the admin rights if the admin username and password is correct
                    clrscr();
                    System.out.println("***************** Admin Login *****************");
                    System.out.print("Enter the admin username : ");
                    String username = sc.next();
                    System.out.print("Enter the admin password : ");
                    String password = sc.next();

                    if(username.equals(bk.adminUserName) && password.equals(bk.adminPassWord))
                    {
                        // after checking the username and password, admin menu can be accessed

                        while(rep)
                        {
                            // while loop is to repeat the the admin menu until the admin press 3 for exit
                            clrscr();// it clear the command prompt
                            System.out.println("############## Admin Access ###############");
                            System.out.println("1. Create New User\n2. Display User Details\n3. Go to Main Menu");
                            System.out.println("###########################################");
                            n1 = sc.nextInt();
                            switch (n1)
                            {
                                case 1:
                                    // Create New User
                                    // it create new user using the scancustomerdetails method of class customer
                                    clrscr();
                                    obj[Bank.customercount] = new Customer();
                                    obj[Bank.customercount].scanCustomerDetails(year);
                                    Bank.customercount++;
                                    System.out.println("\nNEW USER CREATED SUCCESSFULLY!!!!............\nPRESS ENTER FOR MORE OPTION");
                                    sc.nextLine();
                                    sc.nextLine();
                                    break;


                                case 2:
                                    // Display User Details
                                    // it display the customer details in ascending and descending order
                                    if(Bank.customercount==0)// if there are no account then it will display no data found
                                    {
                                        System.out.println("\nNO DATA FOUND!!!!!!...........\nPRESS ENTER FOR MORE OPTION");
                                        sc.nextLine();
                                        sc.nextLine();
                                        break;
                                    }
                                    clrscr();
                                    name = objFile.list();
                                    System.out.println("###########################################");
                                    System.out.println("Display User In Order");
                                    System.out.println("1. Ascending\n2. Descending\n3. Go to Main Menu");
                                    System.out.println("###########################################");
                                    n3 = sc.nextInt();
                                    switch (n3)
                                    {
                                        case 1://Sorts the account in ascending order
                                            for(int i=0;i<name.length;i++)
                                            {
                                                for(int j=0;j<name.length-1;j++)
                                                {
                                                    if (Integer.parseInt(name[j].substring(7))>Integer.parseInt(name[j+1].substring(7)))
                                                    {
                                                        String temp;
                                                        temp = name[j];
                                                        name[j] = name[j+1];
                                                        name[j+1] = temp;

                                                    }
                                                }
                                            }
                                            break;

                                        case 2://Sorts the account in descending order
                                            for(int i=0;i<name.length;i++)
                                            {
                                                for(int j=0;j<name.length-1;j++)
                                                {
                                                    if (Integer.parseInt(name[j].substring(7))<Integer.parseInt(name[j+1].substring(7)))
                                                    {
                                                        String temp;
                                                        temp = name[j];
                                                        name[j] = name[j+1];
                                                        name[j+1] = temp;

                                                    }

                                                }
                                            }
                                            break;

                                        default:
                                            break;
                                    }
                                    clrscr();
                                    // below code is for displaying the customer details in tabular form
                                    System.out.println("-----------------------------------------------------------------------------------");
                                    System.out.printf("%10s  |%10s  |%9s    |  %20s   |  %6s", "ACCOUNT ID", "USERNAME", "NAME","Account Created","BALANCE");
                                    System.out.println();
                                    System.out.println("-----------------------------------------------------------------------------------");
                                    // display the sorted account in selected manner opted by admin
                                    for(int i=0;i<name.length;i++)
                                    {
                                        // it display the customer details using the file name of the customer
                                        Customer obj1 = new Customer();
                                        obj1.displaycustomerDetails(name[i]);
                                    }
                                    System.out.println("\nPRESS ENTER FOR MORE OPTION");
                                    sc.nextLine();
                                    sc.nextLine();
                                    break;

                                case 3:
                                    rep = false;
                                    break;

                                default:
                                    System.out.println("INVALID INPUT");
                                    break;

                            } // end of admin switch case
                        }  // while loop for repeating the admin menu
                    }  // end of if for checking the admin username and password
                    else
                    {
                        System.out.print("\nInvalid Username or Password!!!....\nGO TO MAIN MENU  PRESS ENTER");
                        sc.nextLine();
                        sc.nextLine();
                    }
                    break;

                case 2:
                    clrscr();
                    System.out.println("*****************Customer Login*****************");
                    System.out.print("Enter Account Id : ");
                    String acc = sc.next();
                    System.out.print("Enter customer username : ");
                    username = sc.next();
                    System.out.print("Enter customer password : ");
                    password = sc.next();
                    boolean check = false;
                    Customer custobj = new Customer();
                    File checkFile = new File("C:\\Users\\HP\\IdeaProjects\\Classroom1\\src\\com\\company" +
                            "\\Bank\\"+acc);
                    if(checkFile.exists())// it check whether the file of customer using the account id
                    {
                        check = custobj.checkcustomer(username, password,acc);
                    }
                    else
                    {
                        System.out.print("\nEnter Valid Account Id");
                    }

                    if(check)
                    {
                        // open file of the customer's account after validating username and password
                        FileInputStream fis = new FileInputStream("C:\\Users\\HP\\IdeaProjects\\Classroom1\\src\\com\\company\\Bank\\"+acc);
                        ObjectInputStream objIS = new ObjectInputStream(fis);
                        Customer newObject = (Customer)objIS.readObject(); // read the whole object from the file and cast to customer class
                        rep = true;
                        while(rep)
                        {
                            // while loop repeats the customer login menu until the customer press 5 for main menu
                            clrscr();
                            System.out.println("############## Customer Access ###############");
                            System.out.println("1. Account Details\n2. Withdraw Money\n3. Deposit Money\n4. Transfer Money\n5. Go To Main Menu");
                            System.out.println("##############################################");
                            n2 = sc.nextInt();
                            switch (n2)
                            {
                                case 1:
                                    // Check balance
                                    clrscr();
                                    System.out.println("-----------------------------------------------------------------------------------");
                                    System.out.printf("%10s  |%10s  |%9s    |  %20s   |  %6s", "ACCOUNT ID", "USERNAME", "NAME","Account Created","BALANCE");
                                    System.out.println();
                                    System.out.println("-----------------------------------------------------------------------------------");
                                    custobj.displaycustomerDetails(acc);
                                    System.out.println("\nPRESS ENTER FOR MORE OPTION");
                                    sc.nextLine();
                                    sc.nextLine();
                                    break;

                                case 2:
                                    // Withdraw Money
                                    clrscr();
                                    newObject.balance = custobj.withdrawal(newObject.balance);
                                    custobj = newObject;
                                    fis.close();
                                    objIS.close();
                                    File objfile = new File("C:\\Users\\HP\\IdeaProjects\\Classroom1\\src\\com\\company\\Bank\\"+acc);
                                    FileOutputStream fos = new FileOutputStream(objfile);
                                    ObjectOutputStream objOs=new ObjectOutputStream(fos);
                                    objOs.writeObject(custobj);
                                    fos.close();
                                    objOs.close();
                                    sc.nextLine();
                                    sc.nextLine();
                                    break;

                                case 3:
                                    // Deposit Money
                                    clrscr();
                                    newObject.balance = custobj.deposit(newObject.balance);
                                    custobj = newObject;
                                    fis.close();
                                    objIS.close();
                                    File objfile1 = new File("C:\\Users\\HP\\IdeaProjects\\Classroom1\\src\\com\\company\\Bank\\"+acc);//make a file of customer using accountid
                                    FileOutputStream fos1 = new FileOutputStream(objfile1);
                                    ObjectOutputStream objOs1=new ObjectOutputStream(fos1);
                                    objOs1.writeObject(custobj);
                                    fos1.close();
                                    objOs1.close();
                                    sc.nextLine();
                                    sc.nextLine();
                                    break;

                                case 4:
                                    // Transfer Money
                                    /*It transfer the money from current account in which customer is logged in to another account  */
                                    clrscr();
                                    System.out.print("Enter the Account ID to Which U want to transfer : ");
                                    String acc1 = sc.next();
                                    boolean tf = false;
                                    File checkFile1 = new File("C:\\Users\\HP\\IdeaProjects\\Classroom1\\src\\com\\company\\Bank\\"+acc1);
                                    if(checkFile1.exists())// It check whether the account exists or not in the
                                    {
                                        tf = true;
                                    }
                                    float amt = 0;

                                    if(tf)
                                    {
                                        FileInputStream fis1 = new FileInputStream("C:\\Users\\HP\\IdeaProjects\\Classroom1\\src\\com\\company\\Bank\\"+acc1);
                                        ObjectInputStream objIS1 = new ObjectInputStream(fis1);
                                        Customer newObject1 = (Customer)objIS1.readObject();
                                        System.out.print("Enter the amount U want transfer : ");
                                        amt = sc.nextFloat();
                                        // It validate the entered amount using the if condition
                                        if(amt<0)
                                        {
                                            System.out.println("\nAmount entered is negative.......... so you can not withdraw \nPRESS ENTER FOR MORE OPTION");
                                        }
                                        else if(amt < newObject.balance && newObject.balance-amt>=1000)
                                        {
                                            newObject.balance -= amt;
                                            newObject1.balance += amt;
                                            System.out.println("\nTransfer successful!.........\nFinal Balance is "+newObject.balance+"\nPRESS ENTER FOR MORE OPTION" );
                                        }
                                        else if (amt > newObject.balance)
                                        {
                                            System.out.println("\nTransfer failed, not enough balance!!!!......\nPRESS ENTER FOR MORE OPTION");
                                        }
                                        else
                                        {
                                            System.out.println("\nLess Balance or minimuim balance should be 1000..Transaction Failed..\nPRESS ENTER FOR MORE OPTION");

                                        }
                                        custobj = newObject;
                                        Customer tempobj = new Customer();
                                        tempobj = newObject1;
                                        fis.close();
                                        objIS.close();
                                        fis1.close();
                                        objIS1.close();
                                        File objfile2 = new File("C:\\Users\\HP\\IdeaProjects\\Classroom1\\src\\com\\company\\Bank\\"+acc);//make a file of customer using accountid
                                        FileOutputStream fos2 = new FileOutputStream(objfile2);
                                        ObjectOutputStream objOs2=new ObjectOutputStream(fos2);
                                        objOs2.writeObject(custobj);
                                        File objfile3 = new File("C:\\Users\\HP\\IdeaProjects\\Classroom1\\src\\com\\company\\Bank\\"+acc1);//make a file of customer using accountid
                                        FileOutputStream fos3 = new FileOutputStream(objfile3);
                                        ObjectOutputStream objOs3=new ObjectOutputStream(fos3);
                                        objOs3.writeObject(tempobj);
                                        fos2.close();  objOs2.close();  fos3.close();  objOs3.close();
                                        sc.nextLine();
                                        sc.nextLine();
                                    }
                                    else
                                    {
                                        System.out.println("\nINVALID ACCOUNT ID !!..........\nPRESS ENTER FOR MORE OPTION");
                                        sc.nextLine();
                                        sc.nextLine();
                                    }
                                    break;

                                case 5:
                                    rep = false;
                                    break;

                                default:
                                    System.out.println("INVALID INPUT");
                                    break;
                            }// end of switch for customer menu
                        }// while loop for repeating the customer menu
                    }// end of if after checking the username and password of the customer login
                    else
                    {
                        System.out.print("\nInvalid Username or Password!!!....\nGO TO MAIN MENU  PRESS ENTER");
                        sc.nextLine();
                        sc.nextLine();

                    }
                    break;

                case 3:
                    System.exit(0);

                default:
                    System.out.println("INVALID INPUT");
                    break;

            }

        }
    }
    static void clrscr()
    {
        /* This function is for clearing the command prompt */
        try
        {
            new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}