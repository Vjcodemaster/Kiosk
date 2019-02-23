package com.kiosk.autochip;

/*
 * Created by Vj on 30-Mar-17.
 */

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;

import androidx.viewpager.widget.PagerAdapter;

class DialogImagePagerAdapter extends PagerAdapter {

    private Activity aActivity;
    private LayoutInflater mLayoutInflater;
    //private int[] mResources;
    //private OnFragmentInteractionListener mListener;
    private String[] saImagePath;
    //private CircularProgressBar circularProgressBar;

    /*private String[] sImageURL = {"https://s3.amazonaws.com/sohamsaabucket/01-min.jpg", "https://s3.amazonaws.com/sohamsaabucket/02-min.jpg",
            "https://s3.amazonaws.com/sohamsaabucket/03-min.jpg"};*/

    DialogImagePagerAdapter(Activity aActivity, String[] saImagePath) {
        this.aActivity = aActivity;
        this.saImagePath = saImagePath;
        mLayoutInflater = (LayoutInflater) aActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return saImagePath.length;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.dialog_image_pager_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        Uri uri = Uri.fromFile(new File(saImagePath[position]));
        imageView.setImageURI(uri);
        /*Glide.with(aActivity)
                .load(sImageURL[position])
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        circularProgressBar.dismiss();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mListener.onFragmentMessage("START_ANIMATION", aActivity.getString(R.string.app_name), false);
                        circularProgressBar.dismiss();
                        return false;
                    }
                })
                .into(imageView);*/


        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}
