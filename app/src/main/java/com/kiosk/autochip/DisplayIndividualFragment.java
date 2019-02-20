package com.kiosk.autochip;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import app_utility.DataBaseHelper;
import app_utility.DatabaseHandler;
import app_utility.OnFragmentInteractionListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link app_utility.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DisplayIndividualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayIndividualFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout llImageParent;

    private OnFragmentInteractionListener mListener;

    private ImageView[] ivDynamic;

    TextView tvDescription;

    DatabaseHandler dbh;
    String sImagePath;

    public DisplayIndividualFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DisplayIndividualFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayIndividualFragment newInstance(String param1, String param2) {
        DisplayIndividualFragment fragment = new DisplayIndividualFragment();
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
        dbh = new DatabaseHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_individual, container, false);
        TextView tvShowTechnicalImage = view.findViewById(R.id.tv_view_technical_image);

        sImagePath = dbh.getImagePathFromProducts(mParam1);
        //ArrayList<DataBaseHelper> alDB = new ArrayList<>(dbh.getImagePathFromProducts(mParam1));
        TextView tvHeading = view.findViewById(R.id.tv_heading);
        tvHeading.setText(mParam1);
        tvDescription = view.findViewById(R.id.tv_description);
        tvDescription.setText(mParam2);
        ImageView ibImage1 = view.findViewById(R.id.ib_image1);
        llImageParent = view.findViewById(R.id.ll_image_parent);

        ivDynamic = new ImageView[2];

        for(int i=0; i<ivDynamic.length; i++){
            addDynamicImagesAndContents(i);
        }

        Uri uri = Uri.fromFile(new File(sImagePath));
        ibImage1.setImageURI(uri);
        //llImageParent.addView(ivDynamic[i]);

        ibImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.onFragmentInteractionListener.onFragmentMessage("OPEN_DISPLAY_ENLARGE_PRODUCT_IMAGE", 0, "", "");
            }
        });
        tvShowTechnicalImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.onFragmentInteractionListener.onFragmentMessage("OPEN_TECHNICAL_FRAGMENT", 0, "", "");
            }
        });
        return view;
    }

    public void addDynamicImagesAndContents(int i) {
        //Button btnDynamic = new Button(MainActivity.this);

        ivDynamic[i] = new ImageView(getActivity());

        //ivDynamic[i].setTag(alMainProductName.get(i));

        /*if (Build.VERSION.SDK_INT < 23) {
            //noinspection deprecation
            btnMenuOne[i].setTextAppearance(MainActivity.this, R.style.TextAppearance_AppCompat_Medium);
        } else {
            btnMenuOne[i].setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        }*/
        //btnMenuOne[i].setText(alMainProductName.get(i));
        //btnMenuOne[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        //btnMenuOne[i].setBackground(drawableFromTheme);
        //btnMenuOne[i].setClickable(true);
        //btnMenuOne[i].setBackgroundResource(typedValue.resourceId);
        //btnMenuOne[i].setAllCaps(false);
        Uri uri = Uri.fromFile(new File(sImagePath));
        ivDynamic[i].setImageURI(uri);
        llImageParent.addView(ivDynamic[i]);
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
}
