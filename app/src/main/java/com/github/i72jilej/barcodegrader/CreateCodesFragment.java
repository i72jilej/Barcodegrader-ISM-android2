package com.github.i72jilej.barcodegrader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateCodesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateCodesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCodesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Context applicationContext = null;

    public CreateCodesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateCodesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateCodesFragment newInstance(String param1, String param2) {
        CreateCodesFragment fragment = new CreateCodesFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_create_codes, container, false);

        Button button_createCodes = (Button) view.findViewById(R.id.button_createCodes);
        button_createCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCodes(view);
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

    public void createCodes(View view){
        //Preparing codes to be encoded
        ArrayList<String> codes = new ArrayList<>();
        int codeW = 720;
        int codeH = 120;

        codes.clear();
        for(int i = 1; i < GlobalVars.getInstance().getCsvArray().size(); i++){
            codes.add(GlobalVars.getInstance().getCsvArrayValue(i, 0));
        }

        //Generating barcodes
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix matrix;
        Bitmap bitmap = null;

        //TODO loop here
        try {
            matrix = writer.encode(codes.get(0), BarcodeFormat.CODE_128, codeW, codeH);
            System.out.println("CODE: " + codes.get(0));
            bitmap = generateBitmap(matrix, codeW, codeH);

        }
        catch (WriterException e){
            e.printStackTrace();
        }


        //TESTING
        if(bitmap != null){
            ImageView test = (ImageView) view.findViewById(R.id.testImageView);
            test.setImageBitmap(bitmap);
        }
        else{
            Toast.makeText(applicationContext, R.string.alert_fileNotFound, Toast.LENGTH_LONG).show();
        }
        //END TESTING

        //http://tutorials.jenkov.com/java-itext/index.html


    }

    private Bitmap generateBitmap(BitMatrix matrix, int codeW, int codeH){
        Bitmap bitmap = Bitmap.createBitmap(codeW, codeH, Bitmap.Config.ARGB_8888);

        for(int i = 0; i < codeW; i++){
            for(int j = 0; j < codeH; j++){
                bitmap.setPixel(i, j, matrix.get(i, j) ? Color.BLACK: Color.WHITE);
            }
        }

        return bitmap;
    }
}
