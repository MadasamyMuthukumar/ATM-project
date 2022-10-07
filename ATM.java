package ATMproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class ATM {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			int accno=0,pin=0,flag3=0,witham=0,curbal=0,x=0,y=0,calc=0,flag4=0;
			boolean flag=true;
			Scanner sc=new Scanner(System.in); 
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			Connection c =DriverManager.getConnection("jdbc:mysql://localhost:3306/mybase","root","root");
			Statement st=c.createStatement(); 
			ResultSet r;
			ResultSet R;
			while(flag) {
				System.out.println("\n1.Load Cash to ATM\n2.Show Customer Details\n3.Show ATM Operations\n4.Check ATM balance\n5.Exit");
				int choice=sc.nextInt();
				switch(choice) {
				case 1:
					System.out.println("Count of Rs.2000 : ");
					int r1=sc.nextInt();
					System.out.println("Count of Rs.500 : ");
					int r2=sc.nextInt();
					System.out.println("Count of Rs.100 : ");
					int r3=sc.nextInt();
					st.executeUpdate("update atm set Number=Number+"+r1+",Value=Value+"+r1*2000+" where Denomination=2000");
					st.executeUpdate("update atm set Number=Number+"+r2+",Value=Value+"+r2*500+"  where Denomination=500");
					st.executeUpdate("update atm set Number=Number+"+r3+",Value=Value+"+r3*100+"  where Denomination=100");
					break;
				case 2:
					r=st.executeQuery("select * from custd");
					System.out.println("Acc No   Account Holder   pin Number   Account Balance");
					while(r.next()) {
						System.out.println(r.getInt(1)+"       "+r.getString(2)+"           "+r.getInt(3)+"              "+r.getInt(4));
						
					}
					break;
				case 3:
					System.out.println("1.Check Balance\n2.Withdraw Money\n3.Transfer Money");
					int choice2=sc.nextInt();
					r=st.executeQuery("select acc,pin from custd");
					switch(choice2) {
					case 1:
						flag3=0;
						System.out.println("Enter Account no and pin ");
						accno=sc.nextInt();
						pin=sc.nextInt();
						while(r.next()) {
							if(r.getInt(1)==accno && r.getInt(2)==pin) {
								flag3=1;}					}
						if(flag3==1) {
							R=st.executeQuery("select * from custd where acc="+accno);
							System.out.println("Acc No   Account Holder   pin Number   Account Balance");
							while(R.next()) {
								System.out.println(R.getInt(1)+"       "+R.getString(2)+"           "+R.getInt(3)+"              "+R.getInt(4));
							}
						}
						else {
							System.out.println("Invalid AccountNumber and Pin");
						}
						break;
					case 2:
						flag3=0;
						System.out.println("Enter Account no and pin ");
						accno=sc.nextInt();
						pin=sc.nextInt();
						while(r.next()) {
							if(r.getInt(1)==accno && r.getInt(2)==pin) {
								flag3=1;
								}	
							}
						if(flag3==1) {
							
							System.out.println("Enter the amount to withdraw ");
							witham=sc.nextInt();
							int forc=witham;
							int sum=0;
							r=st.executeQuery("select Value from atm");
							while(r.next()) {
								sum+=r.getInt(1);
							}
							if((witham<=10000 && witham>=100)&&witham%100==0) {
								if( witham<sum) {
									R=st.executeQuery("select bal from custd where acc="+accno);
								while(R.next()) {
									curbal=R.getInt(1);
								}
								if(witham<curbal) {
									while(witham>=3000) {
										calc+=2000;
										st.executeUpdate("update atm set Number=Number-1,Value=Value-2000 where Denomination=2000");
										st.executeUpdate("update custd set bal=bal-2000 where acc="+accno);
										witham-=2000;
									}
									while(witham>=1000) {
										calc+=500;
										st.executeUpdate("update atm set Number=Number-1,Value=Value-500 where Denomination=500");
										st.executeUpdate("update custd set bal=bal-500 where acc="+accno);
										witham-=500;
									}
									while(witham>0) {
										calc+=100;
										st.executeUpdate("update atm set Number=Number-1,Value=Value-100 where Denomination=100");
										st.executeUpdate("update custd set bal=bal-100 where acc="+accno);
										witham-=100;
									}
									if(calc!=calc)
										System.out.println("Denomination not available in ATM");
									
									
								}else {
									System.out.println("Amount greater than balance!");
								}
								}else {
									System.out.println("Amount not available in ATM");
								}
							}else {
								System.out.println("Amount not in range of 100 to 10,000");
							}
						}else {
							System.out.println("Wrong Account Number and pin");
						}
						break;
					case 3:
						flag3=0;curbal=0;
						System.out.println("Enter Account no and pin ");
						accno=sc.nextInt();
						pin=sc.nextInt();
						while(r.next()) {
							if(r.getInt(1)==accno && r.getInt(2)==pin) {
								flag3=1;
								}	
							}
						r=st.executeQuery("select bal from custd where acc="+accno);
						while(r.next()) {
							curbal=r.getInt(1);
						}
						if(flag3==1) {
							flag4=0;
							R=st.executeQuery("select acc from custd");
							witham=0;
							System.out.println("Enter Amount to transfer");
							witham=sc.nextInt();
							if(witham<10000 && witham>1000) {
							System.out.println("Enter the Account Number to Transfer");
							int transac=sc.nextInt();
							while(R.next()) {
							if(R.getInt(1)==transac) {
								flag4=1;
							}
							}
							if(flag4==1) {
								if(witham<curbal) {
									st.executeUpdate("update custd set bal=bal+"+witham+" where acc="+transac);
									st.executeUpdate("update custd set bal=bal-"+witham+" where acc="+accno);
									
								}
								else {
									System.out.println("Transfer amount exceed current balance!");
								}
							}else {
								System.out.println("Incorrect Account Number!");
							}
							
							
							
						}else {
							System.out.println("Amount not in range of 1000 to 10,000");
						}
						}else {
							System.out.println("Incorrect Pin or Acount Number!");
						}
						break;
						
					}
					break;
				case 4:
					int sum=0;
					r=st.executeQuery("select * from atm");
					System.out.println("Denomination   Number   Value");
					while(r.next()) {
						System.out.println(r.getInt(1)+"\t\t"+r.getInt(2)+"\t"+r.getInt(3));
						sum+=r.getInt(3);
						
					}
					System.out.println("Total amount available in ATM= "+sum);
					break;
				case 5:
					flag=false;
					break;
					
				}
				
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}

	}

}
