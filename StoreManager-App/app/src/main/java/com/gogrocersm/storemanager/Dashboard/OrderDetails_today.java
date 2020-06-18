package com.gogrocersm.storemanager.Dashboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gogrocersm.storemanager.Config.BaseURL;
import com.gogrocersm.storemanager.MainActivity;
import com.gogrocersm.storemanager.Model.My_order_detail_model;
import com.gogrocersm.storemanager.R;
import com.gogrocersm.storemanager.util.ConnectivityReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderDetails_today extends AppCompatActivity {

    TextView status, order_id, customer_name, order_socity, customer_phone, order_date, order_time, ammount;
    ImageView Phone;
    String phone_number = "";
    private RecyclerView rv_detail_order;
    private String sale_id , store_order_id;
    ImageView back_button;
    private List<My_order_detail_model> my_order_detail_modelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_today);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.order_detail));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetails_today.this, MainActivity.class);
                startActivity(intent);
            }
        });
        rv_detail_order = (RecyclerView) findViewById(R.id.product_recycler);
        rv_detail_order.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_detail_order.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 0));

        status = findViewById(R.id.status);
        order_id = findViewById(R.id.order_id);
        customer_name = findViewById(R.id.customer_name);
        order_socity = findViewById(R.id.order_socity);
        customer_phone = findViewById(R.id.customer_phone);
        order_date = findViewById(R.id.order_date);
        order_time = findViewById(R.id.order_time);
        ammount = findViewById(R.id.ammount);

        sale_id = getIntent().getStringExtra("sale_id");
        store_order_id = getIntent().getStringExtra("store_order_id");

        if (ConnectivityReceiver.isConnected()) {
            makeGetOrderDetailRequest(sale_id);
        } else {
            Toast.makeText(getApplicationContext(), "Network Issue", Toast.LENGTH_SHORT).show();
        }
        String user_fullname = getIntent().getStringExtra("user_fullname");
//        String socity = getIntent().getStringExtra("socity");
        String phone = getIntent().getStringExtra("customer_phone");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String amount = getIntent().getStringExtra("ammount");
        String stats = getIntent().getStringExtra("status");
//        if (stats.equals("0")) {
//            status.setText(getResources().getString(R.string.pending));
//        } else if (stats.equals("1")) {
//            status.setText(getResources().getString(R.string.confirm));
//        } else if (stats.equals("2")) {
//            status.setText(getResources().getString(R.string.outfordeliverd));
//        } else if (stats.equals("4")) {
//            status.setText(getResources().getString(R.string.delivered));
//        }

        order_id.setText(sale_id);
        customer_name.setText(user_fullname);
//        order_socity.setText(socity);
        customer_phone.setText(phone);
        order_date.setText(date);
        order_time.setText(time);
        ammount.setText(getResources().getString(R.string.currency) + amount);
        status.setText(stats);


        Phone = (ImageView) findViewById(R.id.make_call);
        Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPermissionGranted()) {
                    call_action();
                }

            }

        });


    }

    private void makeGetOrderDetailRequest(String sale_id) {

        // Tag used to cancel the request
//        String tag_json_obj = "json_order_detail_req";
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("sale_id", sale_id);
//
//        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
//                BaseURL.OrderDetail, params, new Response.Listener<JSONArray>() {
//
//            @Override
//            public void onResponse(JSONArray response) {
//                Gson gson = new Gson();
//                Type listType = new TypeToken<List<My_order_detail_model>>() {
//                }.getType();
//
//                my_order_detail_modelList = gson.fromJson(response.toString(), listType);


        JSONArray jsonArray = new JSONArray();
        jsonArray=    BaseURL.order_detail ;

        for (int i = 0 ; i <jsonArray.length();i++){

            try {
                JSONObject object = jsonArray.getJSONObject(i);

                My_order_detail_model my_order_detail_model1 = new My_order_detail_model();
                my_order_detail_model1. setProduct_name(object.getString("product_name"));
                my_order_detail_model1.setPrice(object.getString("price"));
                my_order_detail_model1.setProduct_image(object.getString("varient_image"));
                my_order_detail_model1.setQty(object.getString("qty"));
                my_order_detail_model1.setStore_order_id(object.getString("store_order_id"));


                my_order_detail_modelList.add(my_order_detail_model1);
                OrderDetails_today.My_order_detail_adapter adapter = new OrderDetails_today.My_order_detail_adapter(my_order_detail_modelList);
                rv_detail_order.setAdapter(adapter);
                adapter.notifyDataSetChanged();





            } catch (JSONException e) {
                e.printStackTrace();
            }


        }







        if (my_order_detail_modelList.isEmpty()) {
            Toast.makeText(OrderDetails_today.this, getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
        }
    }

    class My_order_detail_adapter extends RecyclerView.Adapter<OrderDetails_today.My_order_detail_adapter.MyViewHolder> {

        private List<My_order_detail_model> modelList;
        private List<My_order_detail_model> itemList;
        private Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_title, tv_price, tv_qty;
            public ImageView iv_img ,delete;

            public MyViewHolder(View view) {
                super(view);
                tv_title = (TextView) view.findViewById(R.id.tv_order_Detail_title);
                tv_price = (TextView) view.findViewById(R.id.tv_order_Detail_price);
                tv_qty = (TextView) view.findViewById(R.id.tv_order_Detail_qty);
                iv_img = (ImageView) view.findViewById(R.id.iv_order_detail_img);
                delete = (ImageView) view.findViewById(R.id.delete);


            }
        }

        public My_order_detail_adapter(List<My_order_detail_model> modelList) {
            this.modelList = modelList;
        }

        @Override
        public OrderDetails_today.My_order_detail_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.orderdetails_todayc_listitem, parent, false);

            context = parent.getContext();

            return new OrderDetails_today.My_order_detail_adapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull OrderDetails_today.My_order_detail_adapter.MyViewHolder holder, int position) {
            My_order_detail_model mList = modelList.get(position);

            Glide.with(context)
                    .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                    .centerCrop()
                    .placeholder(R.drawable.icons)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(holder.iv_img);

            holder.tv_title.setText(mList.getProduct_name());
            holder.tv_price.setText(mList.getPrice());
            holder.tv_qty.setText(mList.getQty());


        }

        @Override
        public int getItemCount() {
            return modelList.size();
        }

    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void call_action() {
        phone_number = customer_phone.getText().toString();
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_number));
        callIntent.setData(Uri.parse("tel:" + phone_number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }

}

