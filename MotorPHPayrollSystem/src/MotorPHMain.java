import java.util.Scanner;

public class MotorPHMain{

    public static void main(String[] args) {

        LoginSystem login = new LoginSystem();

        if(!login.login()) {
            return;
        }

        PayrollSystem payroll = new PayrollSystem();

        Scanner scanner = new Scanner(System.in);

        int choice;

        do {

            System.out.println("\nMotorPH Payroll Menu");
            System.out.println("1 View Employee Information");
            System.out.println("2 Add Employee");
            System.out.println("3 Process Payroll");
            System.out.println("4 Exit");

            choice = scanner.nextInt();

            if(choice == 2) {

                System.out.print("Employee Number: ");
                int id = scanner.nextInt();

                scanner.nextLine();

                System.out.print("Name: ");
                String name = scanner.nextLine();

                System.out.print("Birthday: ");
                String birthday = scanner.nextLine();

                System.out.print("Hourly Rate: ");
                double rate = scanner.nextDouble();

                System.out.print("Hours Worked: ");
                int hours = scanner.nextInt();

                System.out.print("Late Hours: ");
                int late = scanner.nextInt();

                Employee emp = new Employee(id,name,birthday,rate,hours,late);

                payroll.addEmployee(emp);

                System.out.println("Employee Added Successfully");

            }

            if(choice == 3) {

                payroll.processPayroll();

            }

        } while(choice != 4);

    }

}