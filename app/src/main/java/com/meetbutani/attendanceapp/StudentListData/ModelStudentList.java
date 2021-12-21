package com.meetbutani.attendanceapp.StudentListData;

import java.io.Serializable;

public class ModelStudentList implements Serializable {

    public String emailId, firstName, lastName, imageURL, rollNo, studentId, Class;

    public ModelStudentList() {
    }

    public ModelStudentList(String emailId, String firstName, String lastName, String imageURL, String rollNo, String studentId, String Class) {
        this.emailId = emailId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageURL = imageURL;
        this.rollNo = rollNo;
        this.studentId = studentId;
        this.Class = Class;
    }
}