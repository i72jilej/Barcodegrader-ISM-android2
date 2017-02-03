package com.github.i72jilej.barcodegrader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;

public class ShowCsvActivity extends AppCompatActivity {

    ArrayList<CsvListItem> listItemsArray;
    ListView csvList;
    private static CsvListAdapter csvList_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_csv);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        csvList = (ListView) findViewById(R.id.csv_all);
        listItemsArray = new ArrayList<>();

        for(int i = 1; i < GlobalVars.getInstance().getCsvArray().size(); i++){
            listItemsArray.add(new CsvListItem(GlobalVars.getInstance().getCsvArray().get(i)[1],
                                                GlobalVars.getInstance().getCsvArray().get(i)[2],
                                                GlobalVars.getInstance().getCsvArray().get(i)[3]));
        }

        csvList_adapter = new CsvListAdapter(listItemsArray, getApplicationContext());

        csvList.setAdapter(csvList_adapter);

    }

}
