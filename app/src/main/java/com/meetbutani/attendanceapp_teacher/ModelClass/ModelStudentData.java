package com.meetbutani.attendanceapp_teacher.ModelClass;

import java.io.Serializable;

public class ModelStudentData implements Serializable {

    public String emailId, firstName, lastName, imageURL, rollNo, Class;

    public ModelStudentData() {
    }

    public ModelStudentData(String emailId, String firstName, String lastName, String imageURL, String rollNo, String Class) {
        this.emailId = emailId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageURL = imageURL;
        this.rollNo = rollNo;
        this.Class = Class;
    }
}