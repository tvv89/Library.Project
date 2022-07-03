package com.tvv.service.util;

import com.tvv.db.util.Fields;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FieldsCheckerTest {

    @Test
    void testCheckNameField() {
        assertTrue(FieldsChecker.checkNameField("Vova"));
        assertTrue(FieldsChecker.checkNameField("Вова"));
        assertTrue(FieldsChecker.checkNameField("Едгар По"));
        assertTrue(!FieldsChecker.checkNameField("Vova123"));
        assertTrue(!FieldsChecker.checkNameField("Sergey,"));
    }

    @Test
    void testCheckPhone() {
        assertTrue(FieldsChecker.checkPhone("+380631234567"));
        assertTrue(FieldsChecker.checkPhone("+380991234567"));
        assertTrue(!FieldsChecker.checkPhone("380991234567"));
        assertTrue(!FieldsChecker.checkPhone("+380 99 1234567"));
        assertTrue(!FieldsChecker.checkPhone("+38099123456"));
        assertTrue(!FieldsChecker.checkPhone("+38099123456a"));
    }

    @Test
    void testCheckISBN() {
        assertTrue(FieldsChecker.checkISBN("1234567890123"));
        assertTrue(FieldsChecker.checkISBN("5555555555555"));
        assertTrue(!FieldsChecker.checkISBN("555555555555"));
        assertTrue(!FieldsChecker.checkISBN("555555555555a"));
        assertTrue(!FieldsChecker.checkISBN("555 555 555 5555"));
        assertTrue(!FieldsChecker.checkISBN("555555555555!"));
        assertTrue(!FieldsChecker.checkISBN(null));
    }

    @Test
    void testCheckPasswordField() {
        assertTrue(FieldsChecker.checkPasswordField("1"));
        assertTrue(FieldsChecker.checkPasswordField("password1"));
        assertTrue(FieldsChecker.checkPasswordField("Password1"));
        assertTrue(FieldsChecker.checkPasswordField("Passw0rd1"));
        assertTrue(!FieldsChecker.checkPasswordField(""));
        assertTrue(!FieldsChecker.checkPasswordField("Passw0rd1."));
        assertTrue(!FieldsChecker.checkPasswordField("Passw0rd1!"));

    }

    @Test
    void testCheckAuthors() {
        assertTrue(FieldsChecker.checkAuthors("Vova"));
        assertTrue(FieldsChecker.checkAuthors("Вова"));
        assertTrue(FieldsChecker.checkAuthors("Едгар По"));
        assertTrue(!FieldsChecker.checkAuthors("Vova123"));
        assertTrue(!FieldsChecker.checkAuthors("Sergey."));
    }

    @Test
    void testCheckBookName() {
        assertTrue(FieldsChecker.checkBookName("The best book"));
        assertTrue(FieldsChecker.checkBookName("The best book of 1984"));
        assertTrue(FieldsChecker.checkBookName("The best book of 1984!"));
        assertTrue(FieldsChecker.checkBookName("The best book книга"));
        assertTrue(!FieldsChecker.checkBookName("The best book _ number"));
    }

    @Test
    void testCheckPublisher() {
        assertTrue(FieldsChecker.checkPublisher("Publisher"));
        assertTrue(FieldsChecker.checkPublisher("Publisher, 123"));
        assertTrue(FieldsChecker.checkPublisher("Видання 25"));
        assertTrue(!FieldsChecker.checkPublisher("Publisher 123!!!"));
        assertTrue(!FieldsChecker.checkPublisher(""));
        assertTrue(!FieldsChecker.checkPublisher(null));
    }

    @Test
    void testCheckYear() {
        assertTrue(FieldsChecker.checkYear("1999"));
        assertTrue(FieldsChecker.checkYear("1899"));
        assertTrue(FieldsChecker.checkYear("2001"));
        assertTrue(FieldsChecker.checkYear("2022"));
        assertTrue(!FieldsChecker.checkYear("2023"));
        assertTrue(!FieldsChecker.checkYear("3023"));
        assertTrue(!FieldsChecker.checkYear(""));
        assertTrue(!FieldsChecker.checkYear(null));
    }

    @Test
    void testCheckGenre() {
        assertTrue(FieldsChecker.checkGenre("Genre"));
        assertTrue(!FieldsChecker.checkGenre("Genre, 123"));
        assertTrue(FieldsChecker.checkGenre("Пригоди"));
        assertTrue(FieldsChecker.checkGenre("Пригоди, Історичні"));
        assertTrue(!FieldsChecker.checkGenre("Genre!!!"));
        assertTrue(!FieldsChecker.checkGenre(""));
        assertTrue(!FieldsChecker.checkGenre(null));
    }

    @Test
    void testCheckCount() {
        assertTrue(FieldsChecker.checkCount("1"));
        assertTrue(FieldsChecker.checkCount("1000"));
        assertTrue(!FieldsChecker.checkCount("10000"));
        assertTrue(FieldsChecker.checkCount("0"));
        assertTrue(!FieldsChecker.checkCount(""));
        assertTrue(!FieldsChecker.checkCount(null));
        assertTrue(!FieldsChecker.checkCount("5 шт."));
    }

    @Test
    void testCheckAge16YearsOld() {
        assertTrue(!FieldsChecker.checkAge16YearsOld(null));
        assertTrue(FieldsChecker.checkAge16YearsOld(LocalDate.MIN));
        assertTrue(!FieldsChecker.checkAge16YearsOld(LocalDate.MAX));
        assertTrue(!FieldsChecker.checkAge16YearsOld(LocalDate.now()));
        assertTrue(FieldsChecker.checkAge16YearsOld(LocalDate.parse("1990-01-01")));
    }
}