package com.github.i72jilej.barcodegrader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import com.github.i72jilej.barcodegrader.CsvListItem;

import java.util.List;

/**
 * Created by ralka on 28/01/2017.
 */

public class CsvListAdapter extends ArrayAdapter<CsvListItem> {
    public CsvListAdapter(Context context, List<CsvListItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.csv_list_item,
                    parent,
                    false);
        }

        TextView studentName = (TextView) convertView.findViewById(R.id.list_student_name);
        TextView studentEmail = (TextView) convertView.findViewById(R.id.list_student_email);
        TextView gradeDate = (TextView) convertView.findViewById(R.id.info_grade_date);

        CsvListItem listitem = getItem(position);

        studentName.setText(listitem.getStudentName());
        studentEmail.setText(listitem.getStudentEmail());
        gradeDate.setText(listitem.getGradeDate());

        return convertView;
    }

}
