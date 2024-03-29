package com.github.i72jilej.barcodegrader;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by ralka on 14/12/2016.
 */

public class GlobalVars {
    private static GlobalVars global;

    //LoadCsv
    private String info_filename;
    private String info_maxScore;
    private String info_nStudents;
    private ArrayList<String[]> csvArray = new ArrayList<String[]>();
    GradeFragment gradeFragment = null;
    private Uri inputUri = null;

    public static GlobalVars getInstance(){
        if(global==null){
            global = new GlobalVars();
        }
        return global;
    }

    public String getInfo_filename() {
        return info_filename;
    }

    public void setInfo_filename(String info_filename) {
        this.info_filename = info_filename;
    }

    public String getInfo_maxScore() {
        return info_maxScore;
    }

    public void setInfo_maxScore(String info_maxScore) {
        this.info_maxScore = info_maxScore;
    }

    public String getInfo_nStudents() {
        return info_nStudents;
    }

    public void setInfo_nStudents(String info_nStudents) {
        this.info_nStudents = info_nStudents;
    }

    public ArrayList<String[]> getCsvArray() {
        return csvArray;
    }

    public void setCsvArray(ArrayList<String[]> csvArray) {
        this.csvArray = csvArray;
    }

    public void setCsvArrayValue(int row, int column, String value){
        this.csvArray.get(row)[column] = value;
    }

    public String getCsvArrayValue(int row, int column){
        return this.csvArray.get(row)[column];
    }

    public GradeFragment getGradeFragment() {
        return gradeFragment;
    }

    public void setGradeFragment(GradeFragment gradeFragment) {
        this.gradeFragment = gradeFragment;
    }

    public Uri getInputUri() {
        return inputUri;
    }

    public void setInputUri(Uri inputUri) {
        this.inputUri = inputUri;
    }
}
