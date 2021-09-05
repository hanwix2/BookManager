package com.learning.jpa.bookmanager.domain;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void test() {
        User user = new User("hanwix", "hanwix@email.com");

        System.out.println(">>> " + user);
    }

}