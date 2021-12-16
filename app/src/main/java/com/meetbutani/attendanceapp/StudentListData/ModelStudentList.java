package com.meetbutani.attendanceapp.StudentListData;

import java.io.Serializable;

public class ModelStudentList implements Serializable {

    public String emailId, firstName, lastName, imageURL, rollNo, studentId;

    public ModelStudentList() {
    }

    public ModelStudentList(String emailId, String firstName, String lastName, String imageURL, String rollNo, String studentId) {
        this.emailId = emailId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageURL = imageURL;
        this.rollNo = rollNo;
        this.studentId = studentId;
    }
}