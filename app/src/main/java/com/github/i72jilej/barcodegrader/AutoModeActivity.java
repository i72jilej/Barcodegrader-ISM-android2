package com.github.i72jilej.barcodegrader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class AutoModeActivity extends AppCompatActivity {

    private TextView nStudentsLabel;
    private TextView maxScoreLabel;

    private ListView scannedStudents;
    private ListView notFoundCodes;

    private ArrayAdapter<String> scannedStudents_adapter;
    private ArrayAdapter<String> notFoundCodes_adapter;

    private ArrayList<String> scannedStudents_array = new ArrayList<String>();
    private ArrayList<String> notFoundCodes_array = new ArrayList<String>();
    private ArrayList<String> readedCodes = new ArrayList<String>();

    IntentIntegrator autoModeIntegrator;

    //String[] scannedStudents_array = {"(Ningún alumno escaneado)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_mode);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         **/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nStudentsLabel = (TextView) findViewById(R.id.info_nStudents);
        maxScoreLabel = (TextView) findViewById(R.id.info_maxScore);

        nStudentsLabel.setText(GlobalVars.getInstance().getInfo_nStudents());
        maxScoreLabel.setText(GlobalVars.getInstance().getInfo_maxScore());

        scannedStudents = (ListView) findViewById(R.id.list_scanned);
        notFoundCodes = (ListView) findViewById(R.id.list_not_found);

        scannedStudents_array.add(getResources().getString(R.string.info_default_student_name));
        notFoundCodes_array.add(getResources().getString(R.string.info_default_codes_not_found));

        scannedStudents_adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, scannedStudents_array);
        notFoundCodes_adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, notFoundCodes_array);

        scannedStudents.setAdapter(scannedStudents_adapter);
        notFoundCodes.setAdapter(notFoundCodes_adapter);

        autoModeIntegrator = new IntentIntegrator(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        boolean first = true;
        switch(requestCode){
            case IntentIntegrator.REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    //When returning from each scan
                    System.out.println("SCANNING");
                    IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    String code = intentResult.getContents(); //Pescando el código escaneado en esta vuelta
                    System.out.println(code);


                    //Avoiding duplicates
                    if(!readedCodes.contains(code)){
                        readedCodes.add(code);
                    }

                    autoModeIntegrator.initiateScan(); //scanning again


                }
                else if(resultCode == RESULT_CANCELED){
                    //When pushing back durign scan
                    String code;
                    int pos;

                    System.out.println("READED CODES");
                    for(int i = 0; i < readedCodes.size(); i++){
                        System.out.print(readedCodes.get(i));
                    }

                    //

                    System.out.println("ANALYZING");
                    for(int i = 1; i < GlobalVars.getInstance().getCsvArray().size(); i++){
                        code = GlobalVars.getInstance().getCsvArray().get(i)[0];

                        System.out.println("-> " + code);
                        if((pos = readedCodes.indexOf(code)) != -1){
                            System.out.println("-> FOUND");

                            if(first)
                                scannedStudents_adapter.clear();

                            scannedStudents_adapter.add(GlobalVars.getInstance().getCsvArray().get(i)[1]);
                            first = false;

                            readedCodes.remove(pos);
                        }


                    }
                    System.out.println("END ANALYZING");

                    //Adding not found codes
                    if(!readedCodes.isEmpty()){
                        System.out.println("ADDING NOTFOUNDCODES");
                        notFoundCodes_adapter.clear();
                        Toast.makeText(getApplicationContext(), R.string.alert_codes_not_found, Toast.LENGTH_LONG).show();

                        for(int j = 0; j < readedCodes.size(); j++){
                            notFoundCodes_adapter.add(readedCodes.get(j));
                        }
                    }
                }
        }
    }

    public void startAutoMode(View v){
        autoModeIntegrator.initiateScan();
    }

}
