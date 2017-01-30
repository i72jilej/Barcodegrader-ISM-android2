package com.github.i72jilej.barcodegrader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import com.github.i72jilej.barcodegrader.CsvListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ralka on 28/01/2017.
 * http://www.journaldev.com/10416/android-listview-with-custom-adapter-example-tutorial
 */

public class CsvListAdapter extends ArrayAdapter<CsvListItem> {

    private ArrayList<CsvListItem> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView studentName;
        TextView studentEmail;
        TextView gradeDate;
    }

    public CsvListAdapter(ArrayList<CsvListItem> data, Context context) {
        super(context, R.layout.csv_list_item, data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CsvListItem listItem = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.csv_list_item, parent, false);

            viewHolder.studentName = (TextView) convertView.findViewById(R.id.list_student_name);
            viewHolder.studentEmail = (TextView) convertView.findViewById(R.id.list_student_email);
            viewHolder.gradeDate = (TextView) convertView.findViewById(R.id.info_grade_date);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.studentName.setText(listItem.getStudentName());
        viewHolder.studentEmail.setText(listItem.getStudentEmail());
        viewHolder.gradeDate.setText(listItem.getGradeDate());

        return convertView;
    }

}
