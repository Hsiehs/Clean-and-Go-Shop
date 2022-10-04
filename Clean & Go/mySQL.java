import java.util.*;
import java.sql.*;
import java.sql.Date;
import java.text.NumberFormat;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;


public class mySQL extends MenuLogic{
	
    // ANNUAL CLEANING SUPPLIES EXPENSE REPORT/////////////////////////////////////////////
    public void annualCleaningSuppExp(Connection conn) throws SQLException, IOException {
        Scanner scanner = new Scanner (System.in);
        
        boolean done = false;
        
	  	while(!done) {
        System.out.println("Enter year: ");
        int year = scanner.nextInt();
        
        String query = "SELECT DESCRIPTION, CONCAT('$', FORMAT(SUM(AMOUNT_DUE),2)) AS TOTAL_EXPENSE"
                + " FROM INVOICE AS I, PO_RECORD AS PO"
                + " WHERE PO.PO_N = I.PO_N AND EXTRACT(year FROM DATE) =" + year +  " AND PRODUCT_CATEGORY =  'CLEANING SUPPLIES'"
                + " GROUP BY DESCRIPTION"
                + " ORDER BY DESCRIPTION";
        
        String query1 = " SELECT CONCAT('$', FORMAT(SUM(AMOUNT_DUE),2)) AS TOTAL_EXPENSE"
           + " FROM INVOICE AS I, PO_RECORD AS PO"
           + " WHERE PO.PO_N = I.PO_N AND EXTRACT(year FROM DATE) = " + year + " AND PRODUCT_CATEGORY = 'CLEANING SUPPLIES'";
     
        PreparedStatement stmt = conn.prepareStatement(query);
        PreparedStatement stmt1 = conn.prepareStatement(query1);
           
        ResultSet rset = stmt.executeQuery(query);
        ResultSet rset1 = stmt1.executeQuery(query1);
        
        if (!rset.next()) {
        	System.out.println( "No data available for the year " + year + " or invalid entry. Please try again.");
        	System.out.println();
        } else {
	        System.out.println("    CLEANING SUPPLIES EXPENSE REPORT YEAR: " + year);
	        System.out.println("--------------------------------------------------");
	
	        System.out.printf("%-20s","PRODUCT");
	        System.out.print("TOTAL AMOUNT \n");
			System.out.println();
			System.out.printf("%-20s", rset.getString(1));
            System.out.printf("%-20s", rset.getString(2));
			System.out.println();

	        // Write a loop to read all the returned rows from the query execution
	            while(rset.next()) {
	               for (int i = 0; i < 1; i++) {
	            	   System.out.printf("%-20s", rset.getString(1));
	                   System.out.printf("%-20s", rset.getString(2));
	                   System.out.println();
	               }
	           }
	            
	         System.out.println("--------------------------------------------------");
	         System.out.printf("%-20s", "TOTAL:");
	            
			rset1.next();
			System.out.printf("%-20s", rset1.getString(1));
			System.out.println();
			System.out.println();
			done = true;
        }
		  //Close the statement
          // scanner.close();
           stmt.close();
	  	}
           
    }
    
 // ANNUAL EXPENSE REPORT ///////////////////////////////////////////////////////////////
     public void annualExpReport(Connection conn) throws SQLException, IOException {
   	 Scanner scanner = new Scanner (System.in);
   	 boolean done = false;
   	 
  	 while(!done) {
   	 System.out.println("Enter year: ");
   	 int year = scanner.nextInt();
   	 
   	 // ACCOUNTS PAYABLE
   	 String query1 = "SELECT PRODUCT_CATEGORY, SUM(AMOUNT_DUE) AS TOTAL_EXPENSE"
   	 		+ "	FROM INVOICE AS I, PO_RECORD AS PO"
   	 		+ " WHERE PO.PO_N = I.PO_N AND EXTRACT(year FROM DATE) = " + year 
   	 		+ "	GROUP BY PRODUCT_CATEGORY"
   	 		+ "	ORDER BY PRODUCT_CATEGORY";
   	 
   	 // PAYROLL EXPENSE 
   	String query2 = "SELECT SUM(SALARY)*12 AS ANNUAL_SALARY" 
			+ " FROM EMPLOYEE"
			+ " WHERE EXTRACT(YEAR FROM HIRE_DATE) < " + year + " AND EXTRACT(YEAR FROM NOW()) <> " + year 
            + " UNION"
            + " SELECT SUM(SALARY) * (12 - EXTRACT(MONTH FROM NOW())) AS ANNUAL_SALARY "
			+ " FROM EMPLOYEE"
			+ " WHERE EXTRACT(YEAR FROM HIRE_DATE) < " + year + " AND EXTRACT(YEAR FROM NOW()) = " + year 
			+ " UNION"
			+ " SELECT SALARY * (12 - EXTRACT(MONTH FROM HIRE_DATE)) AS TOTAL_EARNED"
			+ " FROM EMPLOYEE"
			+ " WHERE EXTRACT(YEAR FROM HIRE_DATE) =  " + year + " AND EXTRACT(YEAR FROM NOW()) <> " + year
			+ " UNION"
			+ " SELECT SALARY * (EXTRACT(MONTH FROM NOW()) - EXTRACT(MONTH FROM HIRE_DATE)) AS TOTAL_EARNED"
			+ " FROM EMPLOYEE"
			+ " WHERE EXTRACT(YEAR FROM HIRE_DATE) =  "  + year + " AND EXTRACT(YEAR FROM NOW()) = " + year;
   	 
        PreparedStatement stmt1 = conn.prepareStatement(query1);
        ResultSet rset1 = stmt1.executeQuery(query1);
        
        PreparedStatement stmt2 = conn.prepareStatement(query2);
        ResultSet rset2 = stmt2.executeQuery(query2);
        
	  	NumberFormat formatter = NumberFormat.getCurrencyInstance();	
	  	double total1 = 0;
	  	
	  	if (!rset1.next() || !rset2.next()){
		  	System.out.println(" No data available for the year " + year + " or invalid entry. Please try again.");
		  	System.out.println();
		  	
	  	} else {
	  		System.out.println("    ANNUAL EXPENSE REPORT FOR YEAR: " + year);
	        System.out.println("--------------------------------------------------");

	        System.out.printf("%-30s","EXPENSE");
	        System.out.print("TOTAL AMOUNT \n");
			System.out.println();
			
			
			
		  		 // list of expenses and total amount for each
				System.out.printf("%-30s", rset1.getString(1));
				String amount = formatter.format(rset1.getDouble(2));
				System.out.println(amount);
				
			  		total1 += Double.valueOf(rset1.getDouble(2));
			  		
					 while(rset1.next()) {
						for (int i = 0; i < 1; i++) {
							System.out.printf("%-30s", rset1.getString(1));
							amount = formatter.format(rset1.getDouble(2));
							
							// total amount of all expense, salary not included
							total1 += Double.valueOf(rset1.getDouble(2));
							
							//System.out.println("total1: " + total1);
							
							System.out.println(amount);
						}
					}
			
	  	

				 // total salary expense
				 double total = 0;
			  	 //total += Double.valueOf(rset2.getDouble(1));
				 while(rset2.next()) {
					total += Double.valueOf(rset2.getDouble(1));
					//System.out.println(total);
				 }	
				 
				String moneyString = formatter.format(total);
				System.out.printf("%-30s","SALARY");
				System.out.println(moneyString);
	
				System.out.println("--------------------------------------------------");
		        System.out.printf("%-30s", "TOTAL:");
		        
		        // grand total of all annual expenses
		        System.out.println(moneyString = formatter.format(total + total1));
		        System.out.println();
	        
			done = true;
	  	}
		
	        //Close the statement
			//scanner.close();
			stmt1.close();
			stmt2.close();
	  	}
 
	 }


//  1.2 Supplies: 1.2.A Product Suppliers/////////////////////////////////////////////
public void productSuppliers(Connection conn) throws SQLException, IOException {
	// vendor names:
	// CLEANSTERS GOODIES
	// SUPPLIES FOR ALL
	boolean done = false;
	while(!done) {
	 System.out.println("Enter vendor: ");
	 
	 String vendor = readLine();
	 
	 String query = " SELECT PROD_NAME"
	 		+ " FROM PO_RECORD"
	 		+ " WHERE VENDOR_ID = (SELECT ID FROM VENDOR WHERE NAME = '" + vendor.toUpperCase() + "' )"
	 		+ " GROUP BY PROD_NAME ";
	 
	 PreparedStatement stmt = conn.prepareStatement(query);
	 ResultSet rset = stmt.executeQuery(query);
	 
	 // Write a loop to read all the returned rows from the query execution
			 if (!rset.next()) {
				 System.out.println("Vendor is not found or invalid entry. Please try again.");
				 System.out.println();
				 
			 } else {
				 
				 System.out.println("\n    PRODUCTS OFFERED BY THE VENDOR " + vendor.toUpperCase());
				 System.out.println("--------------------------------------------------------\n");
				 
				 System.out.println(rset.getString(1));
				 while(rset.next()) {
						System.out.println(rset.getString(1));
					}
				 System.out.println();
				 done = true;
				 stmt.close();
			 }
		}
//Close the statement
}

//-- 1.2 SupplieS/////////////////////
//-- 1.2.B Cleaning supplies alert ///////////////////

public void cleaningSuppAlert(Connection conn) throws SQLException, IOException {

	 String query = "SELECT PRODUCT_CATEGORY AS INVENTORY_LOW_IN_STOCK, SAFETY_STOCK_LEVEL, CURRENT_INVENTORY"
	 		+ " FROM CLEANING_SUPPLIES"
	 		+ " WHERE SAFETY_STOCK_LEVEL > CURRENT_INVENTORY";
	 
    PreparedStatement stmt = conn.prepareStatement(query);
    ResultSet rset = stmt.executeQuery(query);
    
    System.out.println();
    System.out.println("    		SUPPLIES BELOW SAFETY STOCK LEVEL ");
    System.out.println("-------------------------------------------------------------\n");

    // Write a loop to read all the returned rows from the query execution
    System.out.printf("%-20s", "SUPPLIES");
    System.out.printf("%-20s", "SAFETY STOCK LEVEL");
    System.out.println("CURRENT STOCK LEVEL");
		 while(rset.next()) {
			for (int i = 0; i < 1; i++) {
				System.out.printf("%-20s", rset.getString(1));
				System.out.printf("%-20s", rset.getString(2));
				System.out.printf("%-20s", rset.getString(3));
				System.out.println();
			}
		 }
		 System.out.println();
   //Close the statement
		stmt.close();
}


// -- 1.3 Equipment   /////////////////////////////
public void totalEquip(Connection conn) throws SQLException, IOException {

  	 String query = "SELECT MAINT_TYPE, COUNT(*) AS NUMBER_OF_UNITS"
  	 		+ " FROM EQUIPMENT"
  	 		+ " GROUP BY MAINT_TYPE";
  	 
       PreparedStatement stmt = conn.prepareStatement(query);
       ResultSet rset = stmt.executeQuery(query);
       
   	   System.out.println();
       System.out.println("    TOTAL NUMBER OF EQUIPMENT BY TYPE ");
       System.out.println("--------------------------------------------------");

       // Write a loop to read all the returned rows from the query execution
       System.out.printf("%-20s", "EQUIP TYPE");
       System.out.println("NUMBER OF UNITS");
   	   System.out.println();
   	   
  		 while(rset.next()) {
  			for (int i = 0; i < 1; i++) {
					System.out.printf("%-20s", rset.getString(1));
					System.out.printf("%-20s", rset.getInt(2));
					System.out.println();
  			}
  		 }
  		System.out.println();
      //Close the statement
  		stmt.close();
   }
   

// -- 1.3 Equipment   /////////////////////////////
	// -- 1.3.A Total number of equipment ///////////////////////////
public void equipMaintSchedule(Connection conn) throws SQLException, IOException {

   	 String query = "SELECT EQUIPMENT_ID, MAINT_TYPE, DATE, DAYOFWEEK(DATE) AS WEEK_DAY, START_TIME, END_TIME"
   	 		+ " FROM EQUIP_SCHEDULE, EQUIPMENT"
   	 		+ " WHERE INTERNAL_ID = EQUIPMENT_ID"
   	 		+ " ORDER BY DATE";
   	 
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rset = stmt.executeQuery(query);
        

        System.out.println("                        		    EQUIPMENT MAINTENANCE SCHEDULE       ");
        System.out.println("---------------------------------------------------------------------------------------------------------------");

        // Write a loop to read all the returned rows from the query execution
        System.out.printf("%-20s", "EQUIP ID");
        System.out.printf("%-20s", "EQUIP TYPE");
        System.out.printf("%-20s", "DATE");
        System.out.printf("%-20s", "WEEK DAY");
        System.out.printf("%-20s", "START TIME");
        System.out.println("END TIME");
		System.out.println();


   		 while(rset.next()) {
   			for (int i = 0; i < 1; i++) {
					System.out.printf("%-20s", rset.getString(1));
					System.out.printf("%-20s", rset.getString(2));
					System.out.printf("%-20s", rset.getDate(3));
					int date = rset.getInt(4);
					String day= "";
    					switch (date) {
                        case 1: day = "SUN";
                            break;
                        case 2: day = "MON";
                            break;
                        case 3: day = "TUE";
                         	break;
                        case 4: day = "WED";
                        	break;
                        case 5: day = "THU";
                        	break;
                        case 6: day = "FRI";
                            break;
                        case 7: day = "SAT";
    					}
					System.out.printf("%-20s", day);
					System.out.printf("%-20s", rset.getTime(5));
					System.out.printf("%-20s", rset.getTime(6));
					System.out.println();
   			}
   		 }
		System.out.println();
        //Close the statement
   		stmt.close();
    
 }

// -- 1.3 Equipment   /////////////////////////////
	// -- 1.3.B Average monthly usage of equipment ///////////////////////////
public void equipAvgMonthUsage(Connection conn) throws SQLException, IOException {
	String query = "SELECT INTERNAL_ID,  MAINT_TYPE, (EXTRACT(MONTH FROM USE_DATE)) AS MONTH, AVG(DURATION_IN_SERVICE) as AVG_USE_PER_MONTH "+
					"FROM EQUIPMENT, SERVICE_USES_EQUIPMENT "+
					"WHERE EQUIPMENT_INTERNAL_ID = INTERNAL_ID "+
					"GROUP BY INTERNAL_ID, MAINT_TYPE, "+
					"(EXTRACT(MONTH FROM USE_DATE));";

	PreparedStatement stmt = conn.prepareStatement(query);
	ResultSet rset = stmt.executeQuery(query);
										 
	System.out.println("                      EQUIPMENT MONTHLY AVERAGE USAGE       ");
	System.out.println("---------------------------------------------------------------------------------\n");  
	
	System.out.printf("%-20s", "INTERNAL_ID");
	System.out.printf("%-20s", "MAINT_TYPE");
	System.out.printf("%-20s", "MONTH");
	System.out.printf("%-20s", "AVG_USE");
	System.out.println();
	

	while (rset.next()){
		for (int i = 0; i < 1; i++){
			System.out.printf("%-20s", rset.getInt(1));
			System.out.printf("%-20s", rset.getString(2));
			int month = rset.getInt(3);
			String strMonth= "";
			switch (month) {
				case 1: strMonth = "JAN";
					break;
				case 2: strMonth = "FEB";
					break;
				case 3: strMonth = "MAR";
					 break;
				case 4: strMonth = "APR";
					break;
				case 5: strMonth = "MAY";
					break;
				case 6: strMonth = "JUN";
					break;
				case 7: strMonth = "JUL";
					break;
				case 8: strMonth = "AUG";
					break;
				case 9: strMonth = "SEP";
					break;
				case 10: strMonth = "OCT";
					break;
				case 11: strMonth = "NOV";
					break;
				case 12: strMonth = "DEC";
				}
			System.out.printf("%-20s", strMonth);
			System.out.printf("%-20s", rset.getDouble(4));
			System.out.println();
		}
	}
}

// -- Employee   /////////////////////////////
	// -- Schedule of employees///////////////////////////
public static void employeeSchedule(Connection conn) throws SQLException, IOException {

	Scanner scanner = new Scanner (System.in);
	System.out.println("Enter ssn: ");
	String id =  scanner.next();

	String query = "SELECT * FROM SCHEDULE WHERE EMPLOYEE_SSN = "+ id;

	PreparedStatement stmt = conn.prepareStatement(query);
	ResultSet rset = stmt.executeQuery(query);
					 
	System.out.println("                                      SHIFT SCHEDULE      ");
	System.out.println("--------------------------------------------------------------------------------------------\n");       
   
	System.out.printf("%-20s", "DATE");
	System.out.printf("%-20s", "DAY");
	System.out.printf("%-20s", "SHIFT");
	System.out.println();
	System.out.println("--------------------------------------------------------------------------------------------");
	
	// Write a loop to read all the returned rows from the query execution
	while (rset.next()){
		for (int i = 0; i < 1; i++){
			System.out.printf("%-20s", rset.getDate(2));
			System.out.printf("%-20s", rset.getString(4));
			System.out.printf("%-20s", rset.getString(5));
		}  
	}  
	System.out.println();
	//Close the statement
	stmt.close();
}

// -- Updates   /////////////////////////////
	// -- Inserting new equipment///////////////////////////
public static void addNewEquipment(Connection conn) throws SQLException, IOException, ParseException {
	System.out.println("                            ENTER INFORMATION FOR NEW EQUIPMENT      ");
	System.out.println("--------------------------------------------------------------------------------------------\n");
	Scanner scanner = new Scanner (System.in);
	System.out.print("Enter Internal ID: ");
	int intID = scanner.nextInt();

	System.out.print("Enter Transaction ID: ");
	int tranID = scanner.nextInt();

	System.out.print("Enter Brand: ");
	String brand = scanner.next();

	System.out.print("Enter Maintenance Type: ");
	String mainType= scanner.next();

	System.out.print("Enter Replacement Date (yyyy-mm-dd): ");
	String strDate = scanner.next();

		String query = "INSERT INTO EQUIPMENT "+
		"(INTERNAL_ID, TRANSACTION_ID, BRAND_NAME, AVAILABLE, MAINT_TYPE, REPLACEMENT_DATE) "+
		"VALUES ("+ intID +", "+ tranID +", '"+ brand +"', "+ 0 +", "+ mainType + ", '"+ strDate +"')";

		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.executeUpdate(query);

		System.out.println("                 The equipment has been succesfully inserted.");
		
		//Close the statement
		   stmt.close();     
 }

// -- Updates   /////////////////////////////
	// -- Inserting new service///////////////////////////
public static void addNewService(Connection conn) throws SQLException, IOException, ParseException{
	System.out.println("                            ENTER INFORMATION FOR NEW SERVICE      ");
	System.out.println("--------------------------------------------------------------------------------------------\n"); 
	Scanner scanner = new Scanner (System.in);

	System.out.print("Enter Service ID: ");
	int servID = scanner.nextInt();
	scanner.nextLine();
	System.out.print("Enter name of service: ");
	String name = scanner.nextLine();

	System.out.print("Enter description: ");
	String desc = scanner.nextLine();

	System.out.print("Enter rate charged: ");
	double rate = scanner.nextDouble();

	System.out.print("Enter duration (HH:MM:SS): ");
	String strDuration = scanner.next();

		String query = "INSERT INTO SERVICE (ID, NAME, DESCRIPTION, RATE_CHARGED, DURATION) "+
		"VALUES ("+ servID +", '"+ name +"', '"+ desc +"', "+ rate +", '"+ strDuration +"')";

		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.executeUpdate(query);

		System.out.println("                   The Service has been succesfully inserted.");
		//Close the statement
		stmt.close();           
 }

// -- Updates   /////////////////////////////
	// -- Inserting new customer///////////////////////////
	public static void addNewCustomer(Connection conn) throws SQLException, IOException, ParseException{
        System.out.println("                            ENTER INFORMATION FOR NEW CUSTOMER      ");
        System.out.println("--------------------------------------------------------------------------------------------\n"); 
        Scanner scanner = new Scanner (System.in);

        System.out.print("Enter Customer ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        System.out.print("Enter date (yyyy-mm-dd): ");
        String strDate = scanner.next();
        
        scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();  
        
        System.out.print("Enter phone number (with no - or spaces): ");
        String phone = scanner.next();
        scanner.nextLine();
        
        System.out.print("Enter 16 digit credit card number: ");
        String cc = scanner.nextLine();

        System.out.print("Enter customer's current store balance: ");
        double balance = scanner.nextDouble();

            String query = "INSERT INTO CUSTOMER (ID, NAME, DATE, ADDRESS, EMAIL, PHONE, CREDIT_CARD_N, CURRENT_BALANCE)"+
                            " VALuES ("+id+", '"+name+"', '"+strDate+"', '"+address+"', '"+email+"', '"+phone+"', '"+cc+"', "+balance+")";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate(query);   

            System.out.println("                   The Customer has been succesfully inserted.");
            //Close the statement
            stmt.close();            

     }

// -- Updates   /////////////////////////////
	// -- Inserting new employee///////////////////////////
	public static void addNewEmployee(Connection conn) throws SQLException, IOException, ParseException{
        System.out.println("                              ENTER NEW HIRE INFORMATION        ");
        System.out.println("--------------------------------------------------------------------------------------------\n");    
        
        Scanner scanner = new Scanner (System.in);

        System.out.print("Enter SSN (with no - or spaces): ");
        String ssn = scanner.next();

        System.out.print("Enter name: ");
        String name = scanner.next();
        scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        System.out.print("Enter gender: ");
        String gender = scanner.next();

        System.out.print("Hire date (YYYY-MM-DD): ");
        String strDate = scanner.next();

        System.out.print("Salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Position: ");
        String pos = scanner.nextLine();

            String query = "INSERT INTO EMPLOYEE (SSN, NAME, ADDRESS, GENDER, HIRE_DATE, SALARY, POSITION)"+
                           " VALUES('"+ssn+"', '"+name+"', '"+address+"', '"+gender+"', '"+strDate+"', "+salary+", '"+pos+"')";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.executeUpdate(query);   
               
            System.out.println("                   New Employee has been succesfully inserted.");
            //Close the statement
            stmt.close();                           

     }	 

	 private static String readLine() {
     InputStreamReader isr = new InputStreamReader(System.in);
     BufferedReader br = new BufferedReader(isr, 1);
     String line = "";

     try {
         line = br.readLine();
     } catch (IOException e) {
         System.out.println("Error in SimpleIO.readLine: " +
                 "IOException was thrown");
         System.exit(1);
     }
     return line;
 }
}
