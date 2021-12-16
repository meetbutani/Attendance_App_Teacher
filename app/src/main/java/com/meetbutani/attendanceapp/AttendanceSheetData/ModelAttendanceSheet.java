package com.meetbutani.attendanceapp.AttendanceSheetData;

import java.io.Serializable;

public class ModelAttendanceSheet implements Serializable {

    public String date, startTime, endTime, sheetId, status, type, timestamp;

    public ModelAttendanceSheet() {
    }

    public ModelAttendanceSheet(String date, String startTime, String endTime, String sheetId, String status, String type, String timestamp) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sheetId = sheetId;
        this.status = status;
        this.type = type;
        this.timestamp = timestamp;
    }
}