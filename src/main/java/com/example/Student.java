package com.example;

public class Student {
    private String firstName;
    private String lastName;
    private int course;

    public Student(String firstName, String lastName, int course) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.course = course;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getCourse() {
        return course;
    }
}