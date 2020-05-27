package com.my.securitytest;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class MyTest {

    @Test
    public void test(){
        System.out.println((int)TimeUnit.DAYS.toSeconds(30));
    }
}
