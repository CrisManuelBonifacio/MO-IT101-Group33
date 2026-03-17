import java.util.Scanner;

public class LoginSystem {

    public boolean login() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("MotorPH Payroll System Login");

        System.out.print("Username: ");
        String user = scanner.nextLine();

        System.out.print("Password: ");
        String pass = scanner.nextLine();

        if(user.equals("admin") && pass.equals("1234")) {

            System.out.println("Login Successful");
            return true;

        } else {

            System.out.println("Login Failed");
            return false;

        }

    }
}