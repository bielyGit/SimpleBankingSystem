package banking;

import java.util.*;

public class Accounts {

    ArrayList<Account> accounts = new ArrayList<>();
    int loggedAccountIndex = -1;
    String dataBaseName = "default";
    Scanner sc = new Scanner(System.in);
    DataBase db;

    public void getDataBaseNameAndConnect(String name){
        this.dataBaseName = name;
        db = new DataBase(dataBaseName);
        db.connect();
    }

    public void createNewAccount() {
        Account newAccount = new Account();
        String s = newAccount.getCardNumber();
        boolean isNumberInSet = false;
        for (Account elem: accounts) {
            if (s.equals(elem.getCardNumber())) {
                isNumberInSet = true;
            };
        }
        if (!isNumberInSet) {
            accounts.add(newAccount);
            System.out.printf("Your card has been created\n" +
                    "Your card number:\n" +
                    "%s\n" +
                    "Your card PIN:\n" +
                    "%s\n", newAccount.getCardNumber(), newAccount.getPin());
            db.addEntity(newAccount);
        } else {
            System.out.println("The card number is already in use!");
        }
    }

    public void print() {
        //old version
        /*        Iterator<Account> iterator = accounts.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().numberAndPinToString());
        }*/


    }

    public int login() {
        System.out.print("Enter your card number:\n>");
        String cardNumber = sc.nextLine();
        System.out.print("Enter your pin:\n>");
        String pin = sc.nextLine();

        if (accounts.size() == 0) {
            System.out.println("\nThere are no accounts!");
            return 1;
        }

        for (Account elem: accounts) {
            if (cardNumber.equals(elem.getCardNumber())) {
                loggedAccountIndex = accounts.indexOf(elem);
            };
        }

        if (loggedAccountIndex != -1 && accounts.get(loggedAccountIndex).getPin().equals(pin)) {
            System.out.println("\nYou have successfully logged in!");
            return loggedInMenu();
        } else {
            System.out.println("\nWrong card number or PIN!");
            return 1;
        }
    }

    public int loggedInMenu(){
        Scanner sc = new Scanner(System.in);
        int state = 1;
        while (state != 0) {
            System.out.print("\n1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit\n> ");
            state = sc.nextInt();
            System.out.println();

            switch (state) {
                case 1: {
                    // old version
//                    System.out.println("Balance: " + accounts.get(loggedAccountIndex).getBalance());

                    System.out.print("Balance: " + db.getBalance(accounts.get(loggedAccountIndex)));
                    break;
                }
                case 2: {
                    System.out.print("Enter income:\n>");
                    int addedIncome = sc.nextInt();
                    db.addIncome(accounts.get(loggedAccountIndex), addedIncome);
                    System.out.println("Income was added!");
                    break;
                }
                case 3: {
                    System.out.print("Transfer:\n>");
                    System.out.print("Enter card number:\n>");
                    String cardNumber = sc.next();

                    if (!accounts.get(loggedAccountIndex).isLuhnNumber(cardNumber)) {
                        System.out.println("Probably you made a mistake in the card number. Please try again!\n");
                        break;
                    }

                    switch (db.ifCardNumberExists(cardNumber, accounts.get(loggedAccountIndex))) {
                        case -1: {
                            System.out.print("Such a card does not exist.");
                            break;
                        }
                        case 1: {
                            System.out.print("Enter how much money you want to transfer:\n>");
                            int amountToTransfer = sc.nextInt();
                            if (amountToTransfer > db.getBalance(accounts.get(loggedAccountIndex))) {
                                System.out.print("Not enough money!\n");
                                break;
                            }
                            else {
                                System.out.print("Success!\n");
                                db.addIncome(accounts.get(loggedAccountIndex), - amountToTransfer);
                            }
                            break;
                        }
                        case 2: {
                            System.out.println("You can't transfer money to the same account!");
                            break;
                        }
                    }
                    break;
                }
                case 4: {
                    db.closeAccount(accounts.get(loggedAccountIndex));
                    System.out.println("The account has been closed!");
                    break;
                }


                case 5: {
                    System.out.println("You have successfully logged out!");
                    return 1;
                }
                case 0: {
                    System.out.println("Bye!");
                    return 0;
                }
            }
            System.out.println();
        }
        return -1;
    }
}
