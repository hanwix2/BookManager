package com.learning.jpa.BookManager.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void test() {
        User user = new User("hanwix", "hanwix@email.com");

        System.out.println(">>> " + user);
    }

}