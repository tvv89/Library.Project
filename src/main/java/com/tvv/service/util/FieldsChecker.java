package com.tvv.service.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util: check string fields
 */
public class FieldsChecker {

    /**
     * Check user first name or last name
     * @param name
     * @return correct or not
     */
    public static boolean checkNameField(String name) {
        String regex = "^([A-Za-zА-Яа-яіІїЇёЁъЪ ]+)";
        return checkRegEx(name,regex);
    }

    /**
     * Check phone
     * @param phone String with email
     * @return correct or not
     */
    public static boolean checkPhone(String phone) {
        String regex = "^[+]{1}([0-9]){12}$";
        return checkRegEx(phone,regex);
    }

    /**
     * Check phone
     * @param isbn String with email
     * @return correct or not
     */
    public static boolean checkISBN(String isbn) {
        String regex = "^[1-9]{1}([0-9]){12}$";
        return checkRegEx(isbn,regex);
    }

    /**
     * Check password
     * @param password String with password
     * @return correct or not
     */
    public static boolean checkPasswordField (String password) {
        String regex = "^[A-Za-z0-9]+";
        return checkRegEx(password,regex);
    }

    /**
     * Check authors
     * @param authors String with password
     * @return correct or not
     */
    public static boolean checkAuthors (String authors) {
        String regex = "^([A-Za-zА-Яа-яіІїЇёЁъЪ, ]+)";
        return checkRegEx(authors,regex);
    }

    /**
     * Check book name
     * @param bookName String with password
     * @return correct or not
     */
    public static boolean checkBookName (String bookName) {
        String regex = "^([0-9A-Za-zА-Яа-яіІїЇёЁъЪ,?.!-:; ]+)";
        return checkRegEx(bookName,regex);
    }

    /**
     * Check publisher
     * @param publisher String with password
     * @return correct or not
     */
    public static boolean checkPublisher (String publisher) {
        String regex = "^([A-Za-zА-Яа-яіІїЇёЁъЪ0-9,. ]+)";
        return checkRegEx(publisher,regex);
    }

    /**
     * Check year
     * @param year String with password
     * @return correct or not
     */
    public static boolean checkYear (String year) {
        if (year==null || year.isEmpty() || Year.parse(year).compareTo(Year.now())>0) {
            return false;
        }
        String regex = "^([1-2]){1}([0-9]){3}";
        return checkRegEx(year,regex);
    }

    /**
     * Check genre
     * @param genre String with password
     * @return correct or not
     */
    public static boolean checkGenre (String genre) {
        String regex = "^([A-Za-zА-Яа-яіІїЇёЁъЪ, ]+)";
        return checkRegEx(genre,regex);
    }

    /**
     * Check count
     * @param count String with password
     * @return correct or not
     */
    public static boolean checkCount (String count) {
        String regex = "^[0-9]{1,4}";
        return checkRegEx(count,regex);
    }

    /**
     * Check 16 years old for user
     * @param date LocalDate date of birth
     * @return correct or not
     */
    public static boolean checkAge16YearsOld(LocalDate date) {
        if (date==null) return false;
        return (Period.between(date, LocalDate.now()).getYears()>=16);
    }

    /**
     * Function for checking regex
     * @param field string value
     * @param regex regex value
     * @return result of checking
     */
    private static boolean checkRegEx(String field, String regex){
        if (field==null) return false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(field);
        return matcher.matches();
    }
}
