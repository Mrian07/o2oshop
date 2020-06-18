package com.gogrocersm.storemanager.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogrocersm.storemanager.Dashboard.StockShow;
import com.gogrocersm.storemanager.Dashboard.StockUpdate;
import com.gogrocersm.storemanager.MainActivity;
import com.gogrocersm.storemanager.Model.AlllProductModel;
import com.gogrocersm.storemanager.R;

import java.util.List;

public class StockShowAdapter extends RecyclerView.Adapter<StockShowAdapter.ViewHolder> {

    private Context context;
    private List<AlllProductModel> list;

    public StockShowAdapter(Context context, List<AlllProductModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_stock_show_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AlllProductModel movie = list.get(position);

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
                Intent intent = new Intent(context, StockUpdate.class);
                intent.putExtra("po_id",movie.getP_id());
                intent.putExtra("product_name",movie.getProduct_name());
                intent.putExtra("stock",movie.getStock());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name,price,unit_value,qty,mrp,delete;
        public ImageView productimage ;

        public ViewHolder(View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.name_product);
            price = itemView.findViewById(R.id.price_product);
            unit_value = itemView.findViewById(R.id.unit_product);
            qty = itemView.findViewById(R.id.qty_product);
            productimage=itemView.findViewById(R.id.imageview_product);
            mrp = itemView.findViewById(R.id.mrp_product);
            delete = (TextView) itemView.findViewById(R.id.delete);


        }
    }

}

