package com.kiosk.autochip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsRVAdapter extends RecyclerView.Adapter<ProductsRVAdapter.ProductItemTabHolder> {

    Context context;
    private RecyclerView recyclerView;
    TextView tvPrevious;

    public ProductsRVAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ProductsRVAdapter.ProductItemTabHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_grid_layout, parent, false);

        return new ProductsRVAdapter.ProductItemTabHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsRVAdapter.ProductItemTabHolder holder, final int position) {

        switch (position){
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
        }

        holder.ivProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            MainActivity.onFragmentInteractionListener.onFragmentMessage("OPEN_DISPLAY_FRAGMENT", position, "");
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
        return 8; //alBeaconInfo.size()
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
