package com.mindhub.homebanking.utils;

public final class CardUtils {

    public static int generateRandomCVV() {
        int randomCVV = getRandomNumber(1,999);
        return randomCVV;
    }

    public static String generateRandomCardNumber() {
        int firstDigits = getRandomNumber(1,999);
        int secondDigits = getRandomNumber(1,9999);
        int thirdDigits = getRandomNumber(1,9999);
        int fourthDigits = getRandomNumber(1,9999);

        String firstDigitsInString = String.format("%0" + 3 + "d",firstDigits);
        String secondDigitsInString = String.format("%0" + 4 + "d",secondDigits);
        String thirdDigitsInString = String.format("%0" + 4 + "d",thirdDigits);
        String fourthDigitsInString = String.format("%0" + 4 + "d",fourthDigits);

        String randomCardNumber = "4" + firstDigitsInString + "-" + secondDigitsInString + "-" +
                thirdDigitsInString + "-" + fourthDigitsInString;
        return randomCardNumber;
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
