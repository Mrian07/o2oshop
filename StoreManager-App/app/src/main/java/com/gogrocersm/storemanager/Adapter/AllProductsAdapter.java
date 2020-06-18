package com.gogrocersm.storemanager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogrocersm.storemanager.AppController;
import com.gogrocersm.storemanager.Config.BaseURL;
import com.gogrocersm.storemanager.Dashboard.MyOrderDeatil;
import com.gogrocersm.storemanager.MainActivity;
import com.gogrocersm.storemanager.Model.AlllProductModel;
import com.gogrocersm.storemanager.R;
import com.gogrocersm.storemanager.util.CustomVolleyJsonArrayRequest;
import com.gogrocersm.storemanager.util.CustomVolleyJsonRequest;
import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.ViewHolder>implements Filterable {

    private Context context;
    private List<AlllProductModel> list;
    private List<AlllProductModel> productListFiltered;
    private AllProductsAdapterListener listener;



    public AllProductsAdapter(Context applicationContext, List<AlllProductModel> movieList) {
        this.context = applicationContext;
        this.productListFiltered = movieList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_all_products, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AlllProductModel movie = productListFiltered.get(position);
//        if (movie.getStock().equals("1")){
//            holder.stock.setText(context.getResources().getString(R.string.in_st));
//        }else if (movie.getStock().equals("2")){
//            holder.stock.setText(context.getResources().getString(R.string.out_st));
//            holder.stock.setTextColor(context.getResources().getColor(R.color.color_3));
//        }
        String sign= MainActivity.currency_sign;
//        holder.product_Id.setText(movie.getCat_id());
        holder.product_name.setText(movie.getProduct_name());
//        holder.catogary_id.setText(movie.getMrp());
//        holder.increment.setText(movie.getIncrement());
        holder.price.setText(sign + movie.getPrice());
        holder.qty.setText(movie.getQuantity());
        holder.unit_value.setText(movie.getUnit());
//        holder.reward.setText(movie.getReward());
        Glide.with(context)
                .load(movie.getVarient_image())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.productimage);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
Log.d("a",movie.getP_id());
                delete(movie.getP_id());

            }
        });

    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = list;
                } else {
                    List<AlllProductModel> filteredList = new ArrayList<>();
                    for (AlllProductModel row : list) {
                        if (row.getProduct_name().toLowerCase().contains(charString.toLowerCase()) || row.getCat_id().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (ArrayList<AlllProductModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name,price,unit_value,qty,mrp;
        public ImageView productimage , delete;

        public ViewHolder(View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.name_product);
            price = itemView.findViewById(R.id.price_product);
            unit_value = itemView.findViewById(R.id.unit_product);
            qty = itemView.findViewById(R.id.qty_product);
            productimage=itemView.findViewById(R.id.imageview_product);
            mrp = itemView.findViewById(R.id.mrp_product);
            delete = (ImageView) itemView.findViewById(R.id.delete);


        }
    }
    public interface AllProductsAdapterListener {
        void onContactSelected(AlllProductModel contact);
    }
    private void delete(String p_id) {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("p_id", p_id);
        Log.d("xx",p_id);


        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.store_delete_product, params
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag", response.toString());

                try {
                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")){

                        context.startActivity(new Intent(context.getApplicationContext(),MainActivity.class));
                        Toast.makeText(context.getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //    Toast.makeText(context.getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }
}