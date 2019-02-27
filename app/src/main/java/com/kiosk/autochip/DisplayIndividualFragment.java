package com.kiosk.autochip;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import app_utility.DataBaseHelper;
import app_utility.DatabaseHandler;
import app_utility.OnFragmentInteractionListener;
import app_utility.ZoomOutPageTransformer;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
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

    ImageView ibImage1;

    DatabaseHandler dbh;
    String sImagePath;
    String[] saImagePath;

    Dialog dialogViewPager;
    ViewPager mViewPagerSlideShow;
    ImageView ivLeftArrow;
    ImageView ivRightArrow;
    int imagePathPosition;
    TypedValue typedValue;

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
        typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_individual, container, false);
        TextView tvShowTechnicalImage = view.findViewById(R.id.tv_view_technical_image);

        saImagePath = dbh.getImagePathFromProducts(mParam1).split(",");
        //ArrayList<DataBaseHelper> alDB = new ArrayList<>(dbh.getImagePathFromProducts(mParam1));
        TextView tvHeading = view.findViewById(R.id.tv_heading);
        tvHeading.setText(mParam1);
        tvDescription = view.findViewById(R.id.tv_description);
        tvDescription.setText(mParam2);
        ibImage1 = view.findViewById(R.id.ib_image1);
        llImageParent = view.findViewById(R.id.ll_image_parent);

        ivDynamic = new ImageView[saImagePath.length];

        for(int i=0; i<ivDynamic.length; i++){
            addDynamicImagesAndContents(i);
            final int finalI = i;
            ivDynamic[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ibImage1.setImageDrawable(ivDynamic[finalI].getDrawable());
                    imagePathPosition = finalI;
                }
            });
        }

        Uri uri = Uri.fromFile(new File(saImagePath[0]));
        ibImage1.setImageURI(uri);
        //llImageParent.addView(ivDynamic[i]);

        ibImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MainActivity.onFragmentInteractionListener.onFragmentMessage("OPEN_DISPLAY_ENLARGE_PRODUCT_IMAGE", 0, mParam1, "");
                initReadMoreDialog();
                dialogViewPager.show();
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

    private void addDynamicImagesAndContents(int i) {
        ivDynamic[i] = new ImageView(getActivity());


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 90);
        //iv.setLayoutParams(layoutParams);

        Uri uri = Uri.fromFile(new File(saImagePath[i]));
        ivDynamic[i].setImageURI(uri);
        //ivDynamic[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ivDynamic[i].setLayoutParams(layoutParams);
        ivDynamic[i].setBackgroundResource(typedValue.resourceId);
        llImageParent.addView(ivDynamic[i]);
    }

    private void initReadMoreDialog() {
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_view_pager, null);
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        layout.setMinimumWidth((int)(displayRectangle.width() * 0.9f));
        layout.setMinimumHeight((int)(displayRectangle.height() * 0.9f));
        dialogViewPager = new Dialog(getActivity());
        dialogViewPager.setContentView(layout);
        dialogViewPager.setCancelable(true);

        TextView tvHeading = dialogViewPager.findViewById(R.id.tv_readmore_heading);
        mViewPagerSlideShow = dialogViewPager.findViewById(R.id.viewpager_image_dialog);
        mViewPagerSlideShow.setOffscreenPageLimit(3);



        ivLeftArrow = dialogViewPager.findViewById(R.id.iv_dialog_left_arrow);
        ivRightArrow = dialogViewPager.findViewById(R.id.iv_dialog_right_arrow);

        ivLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPagerSlideShow.setCurrentItem(mViewPagerSlideShow.getCurrentItem() - 1);
            }
        });

        ivRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPagerSlideShow.setCurrentItem(mViewPagerSlideShow.getCurrentItem() + 1);
            }
        });

        mViewPagerSlideShow.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               /* for (int i = 0; i < nALResources.size(); i++) {
                    if (i == position) {
                        imageViews[i].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.bubble_solid, null));
                        LayerDrawable bgDrawable = (LayerDrawable) imageViews[i].getDrawable();
                        final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.shape_bubble_solid_id);
                        shape.setColor(ResourcesCompat.getColor(getResources(), R.color.darkBlue, null));

                    } else {
                        imageViews[i].setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.bubble_holo, null));
                        LayerDrawable bgDrawable = (LayerDrawable) imageViews[i].getDrawable();
                        final GradientDrawable shape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.shape_bubble_holo_id);
                        shape.setColor(ResourcesCompat.getColor(getResources(), R.color.whiteBlue, null));
                    }
                }*/
                handleArrow(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPagerSlideShow.setPageTransformer(false, new ViewPager.PageTransformer()

        {
            @Override
            public void transformPage(View page, float position) {
                ZoomOutPageTransformer zoomOutPageTransformer = new ZoomOutPageTransformer();
                zoomOutPageTransformer.transformPage(page, position);
            }
        });

        final DialogImagePagerAdapter dialogImagePagerAdapter = new DialogImagePagerAdapter(getActivity(), saImagePath);
        mViewPagerSlideShow.setAdapter(dialogImagePagerAdapter);
        mViewPagerSlideShow.setCurrentItem(imagePathPosition);
        /*Typeface lightFace = Typeface.createFromAsset(getResources().getAssets(), "fonts/myriad_pro_light.ttf");
        Typeface regularFace = Typeface.createFromAsset(getResources().getAssets(), "fonts/myriad_pro_regular.ttf");
        tvHeading.setTypeface(regularFace);*/
        //tvSubHeading.setTypeface(lightFace);
        //tvDescription.setTypeface(lightFace);
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

    private void handleArrow(int position) {
        if (position == 0) {
            ivLeftArrow.setVisibility(View.GONE);
            ivRightArrow.setVisibility(View.VISIBLE);
        } else if (position == saImagePath.length - 1) {
            ivRightArrow.setVisibility(View.GONE);
            ivLeftArrow.setVisibility(View.VISIBLE);
        } else {
            ivLeftArrow.setVisibility(View.VISIBLE);
            ivRightArrow.setVisibility(View.VISIBLE);
        }
    }
}
