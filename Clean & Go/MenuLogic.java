import java.util.*;
import java.sql.*;
import java.io.IOException;
import java.text.ParseException;

public class MenuLogic extends CleanAndGo {
    public void mainMenu(Connection conn) {
        Scanner in = new Scanner(System.in);
        printMenu obj = new printMenu();
        obj.printMainMenu();
        System.out.print("Type in your option: ");
        char ch = in.next().charAt(0);
        System.out.println();
        switch (ch) {
            case '1': // Equipment and Supplies
                equipmentAndSupplies(conn);
                break;
            case '2': // Customer and Service
                customersAndService(conn);
                break;
            case '3': // Employees
                employees(conn);
                break;
            case '4': // Updates
                updates(conn);
                break;
            case '5': // Quit
            	break;
            default:
                System.out.println("Not a valid option ");
                break;
        }
    }

    public void equipmentAndSupplies(Connection conn) { // Equipment and Supplies Menu
        try {

            boolean done = false;
            do {
                Scanner in = new Scanner(System.in);
                printMenu obj = new printMenu();
                mySQL mySQL = new mySQL();
                // do {
                obj.printEquipmentAndSuppliesMenu();
                System.out.print("Type in your option: ");
                char option = in.next().charAt(0);
                System.out.println();
                // Analyze Menu
                if (option == '1') {
                    obj.printAnalyzeMenu();
                    System.out.print("Type in your option: ");
                    option = in.next().charAt(0);
                    // Annual Cleaning Supplies
                    if (option == '1') {
                    	mySQL.annualCleaningSuppExp(conn);
                        mainMenu(conn);
                        break;
                        // Annual Expense Report
                    } else if (option == '2') {
                        mySQL.annualExpReport(conn);
                        mainMenu(conn);
                        break;
                        // Return to Equipment and Supplies
                    } else if (option == '3') {
                        equipmentAndSupplies(conn);
                    } else {
                        System.out.println("Invalid option!");
                        equipmentAndSupplies(conn);
                    }
                    // Supplies Menu
                } else if (option == '2') {
                    obj.printSuppliesMenu();
                    System.out.print("Type in your option: ");
                    option = in.next().charAt(0);
                    // Suppliers Products
                    if (option == '1') {
                        mySQL.productSuppliers(conn);
                        mainMenu(conn);
                        break;
                        // Cleaning Supplies Alert
                    } else if (option == '2') {
                        mySQL.cleaningSuppAlert(conn);
                        mainMenu(conn);
                        break;
                        // Return to Equipment and Supplies Menu
                    } else if (option == '3') {
                        equipmentAndSupplies(conn);
                    } else {
                        System.out.println("Invalid Option!");
                        equipmentAndSupplies(conn);
                    }
                    System.out.print("Type in your option: ");
                    // Equipment Menu
                } else if (option == '3') {
                    obj.printEquipmentMenu();
                    System.out.print("Type in your option: ");
                    option = in.next().charAt(0);
                    // Total Equipment
                    if (option == '1') {
                        mySQL.totalEquip(conn);
                        mainMenu(conn);
                        break;
                        // Equipment Maintenance Schedule
                    } else if (option == '2') {
                        mySQL.equipMaintSchedule(conn);
                        mainMenu(conn);
                        break;
                        // Average monthly Usage
                    } else if (option == '3') {
                        mySQL.equipAvgMonthUsage(conn);
                        mainMenu(conn);
                        break;
                        // Return to Equipment and Supplies
                    } else if (option == '4') {
                        equipmentAndSupplies(conn);
                    } else {
                        System.out.println("Invalid Option!");
                        equipmentAndSupplies(conn);
                    }
                    // Return to main menu
                } else if (option == '4') {
                    done = true;
                    mainMenu(conn);
                } else {
                    System.out.println("Invalid option!");
                    equipmentAndSupplies(conn);
                }
                // in.close();
            } while (!done);

        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (IOException e) {
            e.printStackTrace();
        } //finally {
           //if (conn != null) {
           //try {
           //conn.close();
          // } catch (SQLException e) {
           //System.out.println(e);
          // }
           //}
           //}
    }

    // Customer and Service Menu
    public void customersAndService(Connection conn) {
        System.out.println("Temporarily Unavailable");
        mainMenu(conn);
    }

    // Employees Work Schedule Search
    public void employees(Connection conn) {
        try {
		mySQL.employeeSchedule(conn);
        } catch (IOException ex) {
            ex.printStackTrace();
        }  catch (SQLException ex){
            System.out.println(ex);
        }
		mainMenu(conn);

    }

    // Updates Menu
    public void updates(Connection conn) {
        String masterUser = "student";
        String masterPassword = "password";
        Scanner in = new Scanner(System.in);
        printMenu obj = new printMenu();
        System.out.print("Username: ");
        String user = in.nextLine();
        System.out.print("Password: ");
        String pass = in.nextLine();
        if (user.equals(masterUser) && pass.equals(masterPassword)) {
            obj.printUpdatesMenu();
            System.out.print("Type in your option: ");
            char option = in.next().charAt(0);
            System.out.println();
            // Add Menu
            try {
	            if (option == '1') {
	                obj.printAddMenu();
	                System.out.print("Type in your option: ");
	                option = in.next().charAt(0);
	                // Add Equipment
	                if (option == '1') {
	                    System.out.println("Adding new equipment");
	                    mySQL.addNewEquipment(conn);
	                    mainMenu(conn);
	                    // Add Service
	                } else if (option == '2') {
	                    System.out.println("Adding new service");
	                    mySQL.addNewService(conn);
	                    mainMenu(conn);
	                    // Add Customer
	                } else if (option == '3') {
	                    System.out.println("Adding new customer");
	                    mySQL.addNewCustomer(conn);
	                    mainMenu(conn);
	                    // Add Employee
	                } else if (option == '4') {
	                    System.out.println("Adding new employee");
	                    mySQL.addNewEmployee(conn);
	                    mainMenu(conn);
	                    // Return to Updates Menu
	                } else if (option == '5') {
	                    updates(conn);
	                } else {
	                    System.out.println("Invalid option!");
	                }
	                // Delete Menu
	            } else if (option == '2') {
	                obj.printDeleteMenu();
	                System.out.print("Type in your option: ");
	                option = in.next().charAt(0);
	                // Delete Equipment
	                if (option == '1') {
	                    System.out.println("TEMPORARILY UNAVAILABLE");
	                    mainMenu(conn);
	                    // Delete Service
	                } else if (option == '2') {
	                    System.out.println("TEMPORARILY UNAVAILABLE");
	                    mainMenu(conn);
	                    // Delete Customer Information
	                } else if (option == '3') {
	                    System.out.println("TEMPORARILY UNAVAILABLE");
	                    mainMenu(conn);
	                    // Delete Employee Information
	                } else if (option == '4') {
	                    System.out.println("TEMPORARILY UNAVAILABLE");
	                    mainMenu(conn);
	                    // Return to Updates Menu
	                } else if (option == '5') {
	                    updates(conn);
	                } else {
	                    System.out.println("Invalid option!");
	                    updates(conn);
	                }
	                // Update Menu
	            } else if (option == '3') {
	                obj.printUpdateMenu();
	                System.out.print("Type in your option: ");
	                option = in.next().charAt(0);
	                // Update Equipment
	                if (option == '1') {
	                    System.out.println("TEMPORARILY UNAVAILABLE");
	                    mainMenu(conn);
	                    // Update Service
	                } else if (option == '2') {
	                    System.out.println("TEMPORARILY UNAVAILABLE");
	                    mainMenu(conn);
	                    // Update Customer Information
	                } else if (option == '3') {
	                    System.out.println("TEMPORARILY UNAVAILABLE");
	                    mainMenu(conn);
	                    // Update Employee Information
	                } else if (option == '4') {
	                    System.out.println("TEMPORARILY UNAVAILABLE");
	                    mainMenu(conn);
	                    // Return to Updates menu
	                } else if (option == '5') {
	                    updates(conn);
	                } else {
	                    System.out.println("Invalid option!");
	                    updates(conn);
	                }
	                // Return to Main Menu
	            } else if (option == '4') {
	                mainMenu(conn);
	            } else {
	                System.out.println("Invalid option!");
	                updates(conn);
	            }
            } catch (SQLException ex){
                System.out.println(ex);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ParseException ex){
                ex.printStackTrace();
            }
            in.close();
        } else {
            System.out.println("Incorrect Username or Password!");
            mainMenu(conn);
        }
    }
}