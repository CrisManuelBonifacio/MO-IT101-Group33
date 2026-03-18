import java.util.*;
import java.io.*;

public class MotorPHSystem {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        File employeeFile = new File("C:\\Users\\bonif\\CSV Folder\\NetBeansProjects\\MotorPHSystem\\employees.csv");
        File attendanceFile = new File("C:\\Users\\bonif\\CSV Folder\\NetBeansProjects\\MotorPHSystem\\attendance.csv");

        if (!employeeFile.exists() || !attendanceFile.exists()) {
            System.out.println("CSV file not found.");
            return;
        }

        Map<String, String[]> employees = new LinkedHashMap<>();
        String line;

        BufferedReader empReader = new BufferedReader(new FileReader(employeeFile));
        empReader.readLine();
        while ((line = empReader.readLine()) != null) {
            String[] row = line.split(",", -1);
            if (row.length > 0) employees.put(row[0].trim(), row);
        }
        empReader.close();

        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        if ((!username.equals("employee") && !username.equals("payroll_staff")) || !password.equals("12345")) {
            System.out.println("Incorrect username and/or password.");
            System.out.println("Terminate the program.");
            return;
        }

        if (username.equals("employee")) {
            System.out.println("1. Enter your employee number");
            System.out.println("2. Exit the program");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim();

            if (choice.equals("2")) {
                System.out.println("Terminate the program.");
                return;
            }

            System.out.print("Enter employee number: ");
            String empNo = sc.nextLine().trim();

            if (!employees.containsKey(empNo)) {
                System.out.println("Employee number does not exist.");
                return;
            }

            String[] emp = employees.get(empNo);
            System.out.println("Employee Number: " + get(emp, 0));
            System.out.println("Employee Name: " + get(emp, 2) + " " + get(emp, 1));
            System.out.println("Birthday: " + get(emp, 3));
            return;
        }

        System.out.println("1. Process Payroll");
        System.out.println("2. Exit the program");
        System.out.print("Enter choice: ");
        String mainChoice = sc.nextLine().trim();

        if (mainChoice.equals("2")) {
            System.out.println("Terminate the program.");
            return;
        }

        System.out.println("1. One employee");
        System.out.println("2. All employees");
        System.out.println("3. Exit the program");
        System.out.print("Enter choice: ");
        String payrollChoice = sc.nextLine().trim();

        if (payrollChoice.equals("3")) {
            System.out.println("Terminate the program.");
            return;
        }

        String targetEmp = "";
        if (payrollChoice.equals("1")) {
            System.out.print("Enter employee number: ");
            targetEmp = sc.nextLine().trim();
            if (!employees.containsKey(targetEmp)) {
                System.out.println("Employee number does not exist.");
                return;
            }
        }

        Map<String, double[]> firstCutoffHours = new HashMap<>();
        Map<String, double[]> secondCutoffHours = new HashMap<>();

        BufferedReader attReader = new BufferedReader(new FileReader(attendanceFile));
        attReader.readLine();

        while ((line = attReader.readLine()) != null) {
            String[] row = line.split(",", -1);
            if (row.length < 6) continue;

            String empNo = row[0].trim();
            if (!employees.containsKey(empNo)) continue;
            if (payrollChoice.equals("1") && !empNo.equals(targetEmp)) continue;

            String dateText = row[3].trim();
            String loginText = row[4].trim();
            String logoutText = row[5].trim();

            if (dateText.equals("") || loginText.equals("") || logoutText.equals("")) continue;

            String[] dateParts = dateText.split("[/-]");
            int month, day;

            if (dateParts[0].length() == 4) {
                month = Integer.parseInt(dateParts[1]);
                day = Integer.parseInt(dateParts[2]);
            } else {
                month = Integer.parseInt(dateParts[0]);
                day = Integer.parseInt(dateParts[1]);
            }

            if (month < 6 || month > 12) continue;

            double inTime = toTime(loginText);
            double outTime = toTime(logoutText);

            double start = inTime <= (8 + 5.0 / 60.0) ? 8.0 : inTime;
            double end = outTime >= 17.0 ? 17.0 : outTime;
            double hours = end - start;

            if (hours < 0) hours = 0;

            if (!firstCutoffHours.containsKey(empNo)) firstCutoffHours.put(empNo, new double[13]);
            if (!secondCutoffHours.containsKey(empNo)) secondCutoffHours.put(empNo, new double[13]);

            if (day <= 15) firstCutoffHours.get(empNo)[month] += hours;
            else secondCutoffHours.get(empNo)[month] += hours;
        }
        attReader.close();

        String[] monthNames = {"", "January", "February", "March", "April", "May", "June", "July",
                               "August", "September", "October", "November", "December"};

        for (String empNo : employees.keySet()) {
            if (payrollChoice.equals("1") && !empNo.equals(targetEmp)) continue;

            String[] emp = employees.get(empNo);
            double rate = toDouble(emp[emp.length - 1]);

            for (int month = 6; month <= 12; month++) {
                double h1 = firstCutoffHours.containsKey(empNo) ? firstCutoffHours.get(empNo)[month] : 0.0;
                double h2 = secondCutoffHours.containsKey(empNo) ? secondCutoffHours.get(empNo)[month] : 0.0;

                if (h1 == 0.0 && h2 == 0.0) continue;

                double gross1 = h1 * rate;
                double gross2 = h2 * rate;
                double totalGross = gross1 + gross2;

                double sss = totalGross <= 3250 ? 135 : totalGross <= 3750 ? 157.5 : 180;
                double philHealth = (totalGross * 0.03) / 2.0;
                double pagIbig = totalGross <= 1500 ? totalGross * 0.01 : Math.min(totalGross * 0.02, 100);
                double tax = totalGross <= 20833 ? 0 : (totalGross - 20833) * 0.20;
                double totalDeductions = sss + philHealth + pagIbig + tax;

                System.out.println("\n========================================");
                System.out.println("Employee #: " + get(emp, 0));
                System.out.println("Employee Name: " + get(emp, 2) + " " + get(emp, 1));
                System.out.println("Birthday: " + get(emp, 3));

                System.out.println("\nCutoff Date: " + monthNames[month] + " 1 to " + monthNames[month] + " 15");
                System.out.println("Total Hours Worked: " + h1);
                System.out.println("Gross Salary: " + gross1);
                System.out.println("Net Salary: " + gross1);

                System.out.println("\nCutoff Date: " + monthNames[month] + " 16 to " + monthNames[month] + " 30");
                System.out.println("Total Hours Worked: " + h2);
                System.out.println("Gross Salary: " + gross2);
                System.out.println("SSS: " + sss);
                System.out.println("PhilHealth: " + philHealth);
                System.out.println("Pag-IBIG: " + pagIbig);
                System.out.println("Tax: " + tax);
                System.out.println("Total Deductions: " + totalDeductions);
                System.out.println("Net Salary: " + (gross2 - totalDeductions));
            }
        }
    }

    static String get(String[] row, int index) {
        return index >= 0 && index < row.length ? row[index].trim() : "";
    }

    static double toDouble(String s) {
        return Double.parseDouble(s.replace(",", "").trim());
    }

    static double toTime(String s) {
        String[] p = s.trim().split(":");
        return Integer.parseInt(p[0]) + Integer.parseInt(p[1]) / 60.0;
    }
}