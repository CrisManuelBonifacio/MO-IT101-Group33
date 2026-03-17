public class Employee {

    int employeeNumber;
    String name;
    String birthday;
    double hourlyRate;
    int hoursWorked;
    int lateHours;

    public Employee(int employeeNumber, String name, String birthday,
                    double hourlyRate, int hoursWorked, int lateHours) {

        this.employeeNumber = employeeNumber;
        this.name = name;
        this.birthday = birthday;
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
        this.lateHours = lateHours;
    }

    // STEP 1: Calculate Hours Worked
    public int calculateHoursWorked() {
        return hoursWorked;
    }

    // STEP 2: Calculate Gross Weekly Salary
    public double calculateGrossWeeklySalary() {

        double gross = hoursWorked * hourlyRate;

        // Late deduction included in gross salary
        double lateDeduction = lateHours * hourlyRate;

        return gross - lateDeduction;
    }

    // STEP 3: Calculate Net Weekly Salary
    public double calculateNetWeeklySalary() {

        double gross = calculateGrossWeeklySalary();

        // Government deductions
        double sss = gross * 0.05;
        double philhealth = gross * 0.03;
        double pagibig = 100;

        double totalDeductions = sss + philhealth + pagibig;

        return gross - totalDeductions;
    }

    // View Employee Information
    public void displayEmployeeInfo() {

        System.out.println("\nEmployee Information");
        System.out.println("Employee Number: " + employeeNumber);
        System.out.println("Name: " + name);
        System.out.println("Birthday: " + birthday);

        System.out.println("Hours Worked: " + calculateHoursWorked());

        System.out.println("Gross Weekly Salary: " + calculateGrossWeeklySalary());

        System.out.println("Net Weekly Salary: " + calculateNetWeeklySalary());
    }
}