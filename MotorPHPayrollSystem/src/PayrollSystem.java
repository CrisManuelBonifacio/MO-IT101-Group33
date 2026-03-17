import java.util.ArrayList;

public class PayrollSystem {

    ArrayList<Employee> employees = new ArrayList<>();

    public void addEmployee(Employee emp) {

        employees.add(emp);

    }

    public void processPayroll() {

        System.out.println("Processing Payroll...");

        for(Employee emp : employees) {

            emp.displayEmployeeInfo();
            System.out.println("----------------------");

        }

    }
}