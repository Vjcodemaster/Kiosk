package com.kiosk.autochip;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import app_utility.DataBaseHelper;
import app_utility.DatabaseHandler;

public class ProductsRVAdapter extends RecyclerView.Adapter<ProductsRVAdapter.ProductItemTabHolder> {

    Context context;
    private RecyclerView recyclerView;
    TextView tvPrevious;
    String sTag;
    ArrayList<DataBaseHelper> alDb;
    DatabaseHandler dbh;
    ArrayList<String> alName = new ArrayList<>();
    ArrayList<String> alDescription = new ArrayList<>();
    ArrayList<String> alImagePath = new ArrayList<>();

    public ProductsRVAdapter(Context context, RecyclerView recyclerView, String sTag) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.sTag = sTag;
        dbh = new DatabaseHandler(context);
        alDb = new ArrayList<>(dbh.getAllProductsData1());
        for(int i=0; i<alDb.size(); i++){
            if(alDb.get(i).get_product_category_names().equals(sTag)){
                alName.add(alDb.get(i).get_individual_product_names());
                alDescription.add(alDb.get(i).get_individual_product_description());
                alImagePath.add(alDb.get(i).get_individual_product_images_path());
            }
        }
    }

    @NonNull
    @Override
    public ProductsRVAdapter.ProductItemTabHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_grid_layout, parent, false);

        return new ProductsRVAdapter.ProductItemTabHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsRVAdapter.ProductItemTabHolder holder, final int position) {

        holder.tvProductName.setText(alName.get(position));
        /*if(alImagePath.get(position)!=null) {
            Uri sPath = Uri.fromFile(new File(alImagePath.get(position)));
            Glide.with(context)
                    .load(sPath)
                    .into(holder.ivProducts);
        }*/
        /*switch (position) {
            case 0:
                holder.ivProducts.setImageResource(R.drawable.reach_in_cabinets);
                holder.tvProductName.setText("Reach in Cabinets");
                break;
            case 1:
                holder.ivProducts.setImageResource(R.drawable.chef_counters);
                holder.tvProductName.setText("Chef Counters");
                break;
            case 2:
                holder.ivProducts.setImageResource(R.drawable.ventilated_saladettes);
                holder.tvProductName.setText("Ventilated Saladettes");
                break;
            case 3:
                holder.ivProducts.setImageResource(R.drawable.ventilated_undercounters);
                holder.tvProductName.setText("Ventilated UnderCounters");
                break;
            case 4:
                holder.ivProducts.setImageResource(R.drawable.countertop_cold_display);
                holder.tvProductName.setText("Countertop Cold Display");
                break;
            case 5:
                holder.ivProducts.setImageResource(R.drawable.countertop_sushi_display);
                holder.tvProductName.setText("Countertop Sushi Display");
                break;
            case 6:
                holder.ivProducts.setImageResource(R.drawable.preparation_counters);
                holder.tvProductName.setText("Reach in Cabinets");
                break;
            case 7:
                holder.ivProducts.setImageResource(R.drawable.blast_chillers_freezer);
                holder.tvProductName.setText("Reach in Cabinets");
                break;
        }*/

        holder.ivProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.onFragmentInteractionListener.onFragmentMessage("OPEN_DISPLAY_FRAGMENT", position, alName.get(position),alDescription.get(position));
            }
        });


       /* holder.tvNumber.setText(String.valueOf(position+1));

        holder.tvNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tvPrevious!=null)
                    tvPrevious.setBackgroundColor(context.getResources().getColor((R.color.colorPrimaryDark)));
                holder.tvNumber.setBackgroundColor(context.getResources().getColor((R.color.colorGold)));
                String sValue = holder.tvNumber.getText().toString();
                LoginActivity.onAsyncInterfaceListener.onResultReceived("NUMBER_RECEIVED", 1, sValue, null);
                tvPrevious = holder.tvNumber;
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return alName.size(); //alBeaconInfo.size()
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ProductItemTabHolder extends RecyclerView.ViewHolder {
        //TextView tvNumber;
        ImageView ivProducts;
        TextView tvProductName;

        ProductItemTabHolder(View itemView) {
            super(itemView);
            ivProducts = itemView.findViewById(R.id.iv_products);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            //tvNumber = itemView.findViewById(R.id.tv_number);
        }
    }

}
