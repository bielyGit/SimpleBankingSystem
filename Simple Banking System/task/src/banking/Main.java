package banking;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Accounts accounts = new Accounts();
        accounts.getDataBaseNameAndConnect(args[1]);

        Scanner sc = new Scanner(System.in);
        int state = 1;
        while (state != 0) {
            System.out.print("1. Create an account\n" +
                    "2. Log into account\n" +
                    "3. Show Accounts\n" +
                    "0. Exit\n> ");
            state = sc.nextInt();
            System.out.println();

            switch (state) {
                case 1: {
                    accounts.createNewAccount();
                    break;
                }
                case 2: {
                    state = accounts.login();
                    break;
                }
                case 3: {
                    accounts.print();
                    break;
                }
                case 0: {
                    break;
                }
            }
            System.out.println();
        }

    }
}