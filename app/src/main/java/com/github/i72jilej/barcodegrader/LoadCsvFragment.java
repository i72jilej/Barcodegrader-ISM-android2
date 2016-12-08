package com.github.i72jilej.barcodegrader;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoadCsvFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoadCsvFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoadCsvFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // Codes for onActivityResult
    private static final int PICKFILE_RESULT_CODE = 1;

    //Widgets
    private TextView info_filename;
    private TextView info_nStudents;
    private TextView info_maxScore;

    private String info_filename_text = null;
    private String file_path = null;
    private String info_nStudents_text = null;
    private String info_maxScore_text = null;

    private Uri inputUri = null;
    Context applicationContext = null;
    private ArrayList<String[]> csvArray = new ArrayList<String[]>();

    public LoadCsvFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoadCsvFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoadCsvFragment newInstance(String param1, String param2) {
        LoadCsvFragment fragment = new LoadCsvFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //http://stackoverflow.com/questions/12026442/setretaininstance-not-retaining-the-instance

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_load_csv, container, false);

        //Loading info labels
        info_filename = (TextView) view.findViewById(R.id.info_filename);
        info_nStudents = (TextView) view.findViewById(R.id.info_nStudents);
        info_maxScore = (TextView) view.findViewById(R.id.info_maxScore);

        //Checking if it's a reload for rebuilding UI
        if (info_filename_text != null){
            info_filename.setText(info_filename_text);
            info_nStudents.setText(info_nStudents_text);
            info_maxScore.setText(info_maxScore_text);
        }

        //Assigning onClick for loadCsv button
        Button button_loadCsv = (Button) view.findViewById(R.id.button_loadCsv);
        button_loadCsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCsv(view);
            }
        });

        //Assign onClick for showCsv button
        Button button_showCsv = (Button) view.findViewById(R.id.button_showCsv);
        button_showCsv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                System.out.println("SECOND");
                System.out.println(info_filename_text);
            }
        });

        applicationContext = getActivity().getApplicationContext();



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //On Click para el botón de cargar csv
    public void loadCsv(View v) {
        // Note (eliminar cuando la leais): Esto daba problemas porque
        // Use ACTION_GET_CONTENT if you want your app to simply read/import data. With this approach, the app imports a _*_copy of the data_*_, such as an image file.
        // Use ACTION_OPEN_DOCUMENT if you want your app to have long term, persistent access to documents owned by a document provider. An example would be a photo-editing app that lets users edit images stored in a document provider.
        // puede que si se tiene cuidado al leer de cierta forma funcione bien pues lo que hacemos
        // es leerlo y olvidarnos pero no se si eso interfiere con los paths o algo.
        Intent fileintent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        fileintent.addCategory(Intent.CATEGORY_OPENABLE);
        fileintent.setType("*/*"); //Error con text/plain en Android 6 y con Android 5 tb intermitente?

        try {
            startActivityForResult(fileintent, PICKFILE_RESULT_CODE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            //Log.e("tag", "No activity can handle picking a file. Showing alternatives.");
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PICKFILE_RESULT_CODE:  //After choosing a file in the file explorer
                System.out.println("EXPLORER CLOSED");

                CSVReader csvFile = null;
                InputStream inputStream = null;
                BufferedReader reader = null;

                if(resultCode == RESULT_OK){ //If a file has been chosen
                    System.out.println("FILE CHOSEN");

                    if(data != null){
                        inputUri = data.getData();

                        try{
                            inputStream = applicationContext.getContentResolver().openInputStream(inputUri);
                            reader = new BufferedReader(new InputStreamReader(inputStream));
                            csvFile = new CSVReader(reader);

                            //Checking if it is a csv file
                            file_path = Uri.decode(inputUri.toString());

                            System.out.println(file_path);
                            System.out.println(file_path.substring(file_path.length()-4));

                            if(file_path.substring(file_path.length()-4).equals(".csv")){
                                System.out.println("FILE IS A CSV");

                                String[] aux = file_path.split("/");
                                for(String aux2 : aux) {
                                    info_filename_text = aux2;
                                }
                                info_filename.setText(info_filename_text);

                                csvArray.clear();
                                String[] nextLine;

                                try{
                                    while ((nextLine = csvFile.readNext()) != null) {
                                        // nextLine[] is an array of values from the line
                                        csvArray.add(nextLine);
                                        //System.out.println(csvArray.get(0)[0]); //(0) for the line, [0] for the element
                                    }

                                    info_maxScore_text = csvArray.get(1)[5];
                                    info_maxScore.setText(info_maxScore_text);
                                    info_nStudents_text = String.valueOf(csvArray.size() - 1);
                                    info_nStudents.setText(info_nStudents_text);

                                    //TODO Check if the csv file is in the correct format

                                }catch(IOException e){}
                            }
                            else{
                                System.out.println("FILE IS NOT A CSV");

                                Toast.makeText(applicationContext, R.string.alert_noCsv_text, Toast.LENGTH_LONG).show();
                                file_path = "";
                                info_filename_text = "";
                            }
                        }catch (FileNotFoundException e){
                            Toast.makeText(applicationContext, R.string.alert_fileNotFound, Toast.LENGTH_LONG).show();
                        }
                    }

                }
                else{
                    System.out.println("NO FILE CHOSEN");
                }
                break;
        }

    }

}
