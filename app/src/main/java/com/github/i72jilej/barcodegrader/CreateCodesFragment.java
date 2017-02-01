package com.github.i72jilej.barcodegrader;

import android.content.Context;
import android.content.Intent;
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
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.common.BitMatrix;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


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

    ArrayList<Bitmap> bitmapArray = new ArrayList<>();
    ArrayList<String> codesArray = new ArrayList<>();
    ArrayList<String> namesArray = new ArrayList<>();

    private static final int WRITE_REQUEST_CODE = 2;

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
        //bitmapArray = new ArrayList<>();



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
        int codeW = 500;
        int codeH = 60;

        codesArray.clear();
        namesArray.clear();
        for(int i = 1; i < GlobalVars.getInstance().getCsvArray().size(); i++){
            codesArray.add(GlobalVars.getInstance().getCsvArrayValue(i, 0));
            namesArray.add(GlobalVars.getInstance().getCsvArrayValue(i, 1));
        }

        //Generating barcodes in an array
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix matrix;

        bitmapArray.clear();
        for(int i = 0; i < codesArray.size(); i++){
            try {
                matrix = writer.encode(codesArray.get(i), BarcodeFormat.CODE_128, codeW, codeH);
                bitmapArray.add(generateBitmap(matrix, codeW, codeH));
                System.out.println("CODE: " + codesArray.get(i) + " BITMAPARRAY SIZE: " + bitmapArray.size());

                matrix.clear();
            }
            catch (WriterException e){
                e.printStackTrace();
            }
        }
        //TESTING
        /*
        if(bitmap != null){
            ImageView test = (ImageView) view.findViewById(R.id.testImageView);
            test.setImageBitmap(bitmap);
        }
        else{
            Toast.makeText(applicationContext, R.string.alert_fileNotFound, Toast.LENGTH_LONG).show();
        }
        */
        //END TESTING

        //http://tutorials.jenkov.com/java-itext/index.html
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        intent.setType("application/pdf");
        File file = new File(GlobalVars.getInstance().getInputUri().getPath());
        String new_filename = file.getName();
        new_filename = new_filename.substring(0, new_filename.length()-4) + "-codes.pdf";
        intent.putExtra(Intent.EXTRA_TITLE, new_filename);
        System.out.println(new_filename);

        startActivityForResult(intent, WRITE_REQUEST_CODE);

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case WRITE_REQUEST_CODE:
                if(resultCode == RESULT_OK && data != null) {
                    Uri outputUri = data.getData();
                    System.out.println("URI: " + outputUri.toString());
                    Document document = new Document();

                    try{
                        OutputStream outputStream = applicationContext.getContentResolver().openOutputStream(outputUri);
                        PdfWriter.getInstance(document, outputStream);

                        //Opening file
                        document.open();
                        document.add(new Paragraph(GlobalVars.getInstance().getInfo_filename()));

                        int nCols = 4;
                        int nRows = 9;
                        int nCells = nCols * nRows;
                        int nCodes = codesArray.size();

                        for(int j = 0; j < nCodes; j++){
                            //Creating table
                            PdfPTable table = new PdfPTable(nCols);
                            table.setWidthPercentage(90);

                            //Creating barcode temp file
                            File tempFile = new File(applicationContext.getCacheDir(), "tempCode.png");
                            OutputStream os = new BufferedOutputStream(new FileOutputStream(tempFile));
                            bitmapArray.get(j).compress(Bitmap.CompressFormat.PNG, 100, os);
                            os.close();
                            Image image = Image.getInstance(tempFile.getPath());
                            tempFile.delete();

                            //Creating cell
                            PdfPCell cell = new PdfPCell();
                            cell.addElement(image);
                            cell.addElement(new Paragraph(codesArray.get(j)));
                            cell.addElement(new Paragraph(namesArray.get(j)));

                            //cell.setBorder(Rectangle.NO_BORDER);
                            cell.setPadding(5);

                            //Adding cells
                            for(int i = 0; i < nCells; i++) {
                                table.addCell(cell);
                            }

                            //Adding table and new page
                            document.add(table);
                            document.newPage();
                        }

                        document.close();
                    }
                    catch (DocumentException | NullPointerException | IOException e){
                        e.printStackTrace();
                    }

                }

                break;

        }
    }
}
