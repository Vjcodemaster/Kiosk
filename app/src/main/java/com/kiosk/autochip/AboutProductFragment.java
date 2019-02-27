package com.kiosk.autochip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import app_utility.OnFragmentInteractionListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link app_utility.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AboutProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutProductFragment.
     */

    public static AboutProductFragment newInstance(String param1, String param2) {
        AboutProductFragment fragment = new AboutProductFragment();
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
        View view = inflater.inflate(R.layout.fragment_about_product, container, false);
        TextView tvAbout = view.findViewById(R.id.tv_about);
        ImageView ivAbout = view.findViewById(R.id.iv_image_about);
        switch (Integer.valueOf(mParam1)){
            case 0:
                ivAbout.setImageResource(R.drawable.commercial_kitchen);
                tvAbout.setText(getActivity().getResources().getString(R.string.commercial_kitchens));
                break;
            case 1:
                ivAbout.setImageResource(R.drawable.bars_pubs);
                tvAbout.setText(getActivity().getResources().getString(R.string.bars_pubs));
                break;
            case 2:
                ivAbout.setImageResource(R.drawable.cake_sweet);
                tvAbout.setText(getActivity().getResources().getString(R.string.cake_sweet_shop));
                break;
            case 3:
                ivAbout.setImageResource(R.drawable.food_retail);
                tvAbout.setText(getActivity().getResources().getString(R.string.food_retail));
                break;
            case 4:
                ivAbout.setImageResource(R.drawable.cold_storage);
                tvAbout.setText(getActivity().getResources().getString(R.string.food_preservation));
                break;
            case 5:
                ivAbout.setImageResource(R.drawable.bio_medical);
                tvAbout.setText(getActivity().getResources().getString(R.string.bio_medical));
                break;
        }
        return view;
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
