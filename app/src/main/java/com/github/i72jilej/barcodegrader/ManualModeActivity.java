package com.github.i72jilej.barcodegrader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class ManualModeActivity extends AppCompatActivity {

    int studentRow = 0;
    int prev_studentRow = 0;
    float maxGrade;
    float currGrade;

    TextView studentName;
    TextView studentEmail;
    EditText studentGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_mode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });**/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        studentName = (TextView) findViewById(R.id.info_student_name);
        studentEmail = (TextView) findViewById(R.id.info_student_email);
        studentGrade = (EditText) findViewById(R.id.edit_grade);

        //Building UI
        TextView gradeLabel = (TextView) findViewById(R.id.info_label_grade);
        gradeLabel.setText(getResources().getString(R.string.info_label_default_grade) + GlobalVars.getInstance().getInfo_maxScore() + ")");

        ArrayList<Button> buttonArrayList = new ArrayList<>();
        buttonArrayList.add((Button) findViewById(R.id.button_grade_0));
        buttonArrayList.add((Button) findViewById(R.id.button_grade_1));
        buttonArrayList.add((Button) findViewById(R.id.button_grade_2));
        buttonArrayList.add((Button) findViewById(R.id.button_grade_3));
        buttonArrayList.add((Button) findViewById(R.id.button_grade_4));
        buttonArrayList.add((Button) findViewById(R.id.button_grade_5));
        buttonArrayList.add((Button) findViewById(R.id.button_grade_6));
        buttonArrayList.add((Button) findViewById(R.id.button_grade_7));
        buttonArrayList.add((Button) findViewById(R.id.button_grade_8));
        buttonArrayList.add((Button) findViewById(R.id.button_grade_9));
        buttonArrayList.add((Button) findViewById(R.id.button_grade_10));

        maxGrade = Float.valueOf(GlobalVars.getInstance().getInfo_maxScore().replaceAll(",","."));
        float step = maxGrade / 10;

        buttonArrayList.get(0).setText("0");

        for(int i = 1; i <= 10; i++){
            buttonArrayList.get(i).setText(String.valueOf(step * i));
        }

        //Assigning onClickListeners
        for(int i = 0; i < 11; i++) {
            buttonArrayList.get(i).setOnClickListener(mGlobal_OnClickListener);
        }

        studentGrade.addTextChangedListener(textWatcher);

    }

    public void manualScan (View v){
        System.out.println("SCANNING");
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);

        scanIntegrator.initiateScan(); //Result is catched by onActivityResult
        System.out.println("SCANNED");
    }

    public void deleteGrade(View v){
        if(studentRow != 0){
            studentGrade.setText("");
            GlobalVars.getInstance().getCsvArray().get(studentRow)[3] = "";
        }
        else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_student_not_loaded), Toast.LENGTH_LONG).show();
        }
    }

    public int searchStudent(String code){
        //Searchs for a student in the array and returns it possition. Returns 0 if it is not found
        int row = 0;

        while (row < GlobalVars.getInstance().getCsvArray().size() && !GlobalVars.getInstance().getCsvArray().get(row)[0].equals(code)) {
            //System.out.println("ROW: " + row + "CODE: " + code + " -> " + GlobalVars.getInstance().getCsvArray().get(row)[0]);
            row++;
        }

        if (row == GlobalVars.getInstance().getCsvArray().size()){
            //System.out.println("NOT FOUND");
            row = 0;
        }

        //System.out.println("RETURN: " + row);
        return row;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try{
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

            if (scanningResult != null){
                //Receiving string
                String scanContent = scanningResult.getContents();

                //System.out.println(scanContent);
                studentRow = searchStudent(scanContent);

                if (studentRow == 0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_student_not_found), Toast.LENGTH_LONG).show();
                    studentRow = prev_studentRow;
                }
                else{
                    //Loading student's actual data
                    prev_studentRow = studentRow;

                    studentName.setText(GlobalVars.getInstance().getCsvArray().get(studentRow)[1]);
                    studentEmail.setText(GlobalVars.getInstance().getCsvArray().get(studentRow)[2]);
                    studentGrade.setText(GlobalVars.getInstance().getCsvArray().get(studentRow)[3]);
                }

            } else{
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_no_code), Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    //OnClick listeners for the grading buttons
    final View.OnClickListener mGlobal_OnClickListener = new View.OnClickListener() {
        public void onClick(final View v) {
            Button button = (Button) findViewById(v.getId());

            if (studentRow != 0){
                studentGrade.setText(button.getText().toString());
                GlobalVars.getInstance().getCsvArray().get(studentRow)[3] = button.getText().toString();
            }
            else{
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_student_not_loaded), Toast.LENGTH_LONG).show();
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(studentRow != 0){
                if(!studentGrade.getText().toString().equals("")) {
                    if (Float.valueOf(studentGrade.getText().toString()) > maxGrade) {
                        System.out.println("NOTA MAYOR");
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_max_grade) + String.valueOf(maxGrade), Toast.LENGTH_LONG).show();
                        GlobalVars.getInstance().getCsvArray().get(studentRow)[3] = "";


                        studentGrade.setText("");
                    } else if (Float.valueOf(studentGrade.getText().toString()) <= maxGrade) {
                        System.out.println("NOTA OK");

                        GlobalVars.getInstance().getCsvArray().get(studentRow)[3] = studentGrade.getText().toString();
                    }
                }
                else{
                  System.out.println("NOTA EMPTY");
                    GlobalVars.getInstance().getCsvArray().get(studentRow)[3] = "";
                }
            }
            else{
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.alert_student_not_loaded), Toast.LENGTH_LONG).show();
            }
        }
    };


}
