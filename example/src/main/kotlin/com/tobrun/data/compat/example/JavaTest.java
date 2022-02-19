package com.tobrun.data.compat.example;

public class JavaTest {

    public void test() {
        Person person = new Person.Builder()
                .setAge(31)
                .setName("Tobrun Van Nuland")
                .setNickname("Nurbot")
                .build();
    }
}
