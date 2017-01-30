package com.github.i72jilej.barcodegrader;

/**
 * Created by ralka on 28/01/2017.
 */

public class CsvListItem {
    private String studentName;
    private String studentEmail;
    private String gradeDate;

    public CsvListItem(String name, String email, String grade){
        this.studentName = name;
        this.studentEmail = email;
        this.gradeDate = grade;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getGradeDate() {
        return gradeDate;
    }

    public void setGradeDate(String gradeDate) {
        this.gradeDate = gradeDate;
    }

    @Override
    public String toString(){
        return "Student{" +  studentName + " - " + studentEmail + " - " + gradeDate + "}";
    }
}
