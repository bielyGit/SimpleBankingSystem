package banking;

import java.util.Random;

import static java.lang.Character.digit;

public class Account {
    private String cardNumber;
    private String pin;
    private int balance;

    Account(){
        this.cardNumber = randomCartNumber();
        this.pin = randomPin();
        this.balance = 0;
    }


    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    private String randomCartNumber() {
        long bin = 4000000000000000L;
        long rightLimit = 999999999L;
        long random = (long) (Math.random() * rightLimit);
        String cardNumberWithoutCheck = Long.toString(bin + random * 10L);

        String cartNumberWithLunh = cardNumberWithoutCheck.substring(0, 15)
                .concat(String.valueOf(putLuhnNumber(cardNumberWithoutCheck)));

        return cartNumberWithLunh;
    }

    public int putLuhnNumber(String test){

        int sum = 0;
        for (int i = 0; i < 15; i++) {
            int digit = digit(test.charAt(i),10);
            if (i % 2 == 0){
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
        }

        int lunhDigit = 0;
        if (sum % 10 != 0) {
            lunhDigit = 10 - (sum % 10);
        }
        return lunhDigit;
    }

    public boolean isLuhnNumber(String test) {
        if (test.length() != 16) {
            return false;
        }

        int lastCardNumberDigit = Character.getNumericValue(test.charAt(15));
        int countLuhnLastDigit = putLuhnNumber(test);
        return lastCardNumberDigit == countLuhnLastDigit;
    }

    private String randomPin() {

//        int random = new Random().nextInt(10000);
        int random = 1000 + new Random().nextInt(9000);

        StringBuilder pin = new StringBuilder(Integer.toString(random));
        for (int i = pin.length(); i < 4; i++) {
            pin.insert(0,"0");
        }
        return pin.toString();
    }

    public String numberAndPinToString(){
        return "Card Number = " + cardNumber +" | Pin = " + pin;
}

}
