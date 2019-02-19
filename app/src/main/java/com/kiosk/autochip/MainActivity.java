package com.kiosk.autochip;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import app_utility.DataReceiverService;
import app_utility.ImageViewRVAdapter;
import app_utility.OnFragmentInteractionListener;
import app_utility.StaticReferenceClass;
import app_utility.VolleyTask;

import static app_utility.StaticReferenceClass.PRODUCT_URL;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    RecyclerView recyclerView;
    FrameLayout flContainer;
    ViewStub stub;
    View inflated;

    Button btnRefridge;

    Toolbar toolbar;

    ViewStub stubSubMenu;
    View inflatedSubMenu;

    ViewStub stubSubMenu2;
    View inflatedSubMenu2;
    public static OnFragmentInteractionListener onFragmentInteractionListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onFragmentInteractionListener = this;
        HashMap<String, String> params = new HashMap<>();
        params.put("db", StaticReferenceClass.DB_NAME); //Trufrost-Testing
        params.put("user", StaticReferenceClass.USER_ID);
        params.put("password", StaticReferenceClass.PASSWORD);
        Intent in = new Intent(MainActivity.this, DataReceiverService.class);
        startService(in);

        VolleyTask volleyTask = new VolleyTask(getApplicationContext(), params, "REQUEST_PRODUCTS", PRODUCT_URL);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        stub = findViewById(R.id.fragment_menu);
        stub.setLayoutResource(R.layout.menu_layout);
        inflated = stub.inflate();

        stubSubMenu = findViewById(R.id.fragment_sub_menu);
        stubSubMenu.setLayoutResource(R.layout.sub_menu_layout);
        inflatedSubMenu = stubSubMenu.inflate();

        stubSubMenu2 = findViewById(R.id.fragment_sub_menu_2);
        stubSubMenu2.setLayoutResource(R.layout.sub_menu_layout_2);
        inflatedSubMenu2 = stubSubMenu2.inflate();

        btnRefridge = inflatedSubMenu.findViewById(R.id.btn_refrigde);

        btnRefridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductsFragment();
                stubSubMenu2.setVisibility(View.VISIBLE);
                stubSubMenu.setVisibility(View.GONE);
                stub.setVisibility(View.GONE);
            }
        });

        recyclerView = findViewById(R.id.rv_products);
        //flContainer = findViewById(R.id.fl_menu);

        LinearLayoutManager mLinearLayoutManager = new GridLayoutManager(MainActivity.this, 3);
        mLinearLayoutManager.setOrientation(RecyclerView.VERTICAL);


       /* int spanCount = 4; // 3 columns
        int spacing = 5; // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));*/

        /*recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position
                int spanCount = 3;
                int spacing = 10;//spacing between views in grid

                if (position >= 0) {
                    int column = position % spanCount; // item column

                    outRect.left = 0;
                    outRect.right = 0;
                    //outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                    //outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                    if (position < spanCount) { // top edge
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing; // item bottom
                } else {
                    outRect.left = 0;
                    outRect.right = 0;
                    outRect.top = 0;
                    outRect.bottom = 0;
                }
            }
        });*/
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));

        ImageViewRVAdapter imageViewRVAdapter = new ImageViewRVAdapter(MainActivity.this, recyclerView, getSupportFragmentManager());
        recyclerView.setAdapter(imageViewRVAdapter);
        //openMenuFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int size = getSupportFragmentManager().getBackStackEntryCount();
        if (size == 2) {
            //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            stub.setVisibility(View.GONE);
            stubSubMenu.setVisibility(View.VISIBLE);
            stubSubMenu2.setVisibility(View.GONE);
        }
        if (size == 1) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            stub.setVisibility(View.VISIBLE);
            stubSubMenu.setVisibility(View.GONE);
            stubSubMenu2.setVisibility(View.GONE);
        }
        super.onBackPressed();
    }

    private void openMenuFragment() {
        Fragment newFragment;
        FragmentTransaction transaction;
        //Bundle bundle = new Bundle();
        //bundle.putInt("index", 0);
        newFragment = AboutProductFragment.newInstance("", "");
        //newFragment.setArguments(bundle);

        //String sBackStackParent = newFragment.getClass().getName();
        transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
        transaction.replace(R.id.fl_menu, newFragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openProductsFragment() {
        Fragment newFragment;
        FragmentTransaction transaction;
        //Bundle bundle = new Bundle();
        //bundle.putInt("index", 0);
        newFragment = ProductsFragment.newInstance("", "");
        //newFragment.setArguments(bundle);

        //String sBackStackParent = newFragment.getClass().getName();
        transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
        transaction.replace(R.id.fl_menu, newFragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openDisplayIndividualFragment() {
        Fragment newFragment;
        FragmentTransaction transaction;
        //Bundle bundle = new Bundle();
        //bundle.putInt("index", 0);
        newFragment = DisplayIndividualFragment.newInstance("", "");
        //newFragment.setArguments(bundle);

        //String sBackStackParent = newFragment.getClass().getName();
        transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
        transaction.replace(R.id.fl_menu, newFragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openDisplayEnlargeProductFragment() {
        Fragment newFragment;
        FragmentTransaction transaction;
        //Bundle bundle = new Bundle();
        //bundle.putInt("index", 0);
        newFragment = EnlargeProductImage.newInstance("", "");
        //newFragment.setArguments(bundle);

        //String sBackStackParent = newFragment.getClass().getName();
        transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
        transaction.replace(R.id.fl_menu, newFragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openDisplayTechnicalFragment() {
        Fragment newFragment;
        FragmentTransaction transaction;
        //Bundle bundle = new Bundle();
        //bundle.putInt("index", 0);
        newFragment = DisplayTechnicalImage.newInstance("", "");
        //newFragment.setArguments(bundle);

        //String sBackStackParent = newFragment.getClass().getName();
        transaction = getSupportFragmentManager().beginTransaction();
        //transaction.setCustomAnimations(R.anim.t2b, R.anim.b2t);
        transaction.replace(R.id.fl_menu, newFragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentMessage(String sMsg, int type, String sResult) {
        switch (sMsg) {
            case "SHOW_BACK_BUTTON":
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case "OPEN_ABOUT_FRAGMENT":
                openMenuFragment();
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                stub.setVisibility(View.GONE);
                stubSubMenu.setVisibility(View.VISIBLE);
                //stub = findViewById(R.id.fragment_menu);
                /*stub.setLayoutResource(R.layout.sub_menu_layout);
                inflated = stub.inflate();*/
                //View inflated = stub.inflate();
                break;
            case "OPEN_DISPLAY_FRAGMENT":
                openDisplayIndividualFragment();
                break;
            case "OPEN_DISPLAY_ENLARGE_PRODUCT_IMAGE":
                openDisplayEnlargeProductFragment();
                break;
            case "HIDE_BACK_BUTTON":
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case "OPEN_TECHNICAL_FRAGMENT":
                openDisplayTechnicalFragment();
                break;
        }
    }
}
