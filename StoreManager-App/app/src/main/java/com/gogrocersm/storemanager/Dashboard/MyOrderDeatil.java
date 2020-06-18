package com.gogrocersm.storemanager.Dashboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.franmontiel.localechanger.LocaleChanger;
import com.gogrocersm.storemanager.util.CustomVolleyJsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gogrocersm.storemanager.AppController;
import com.gogrocersm.storemanager.Config.BaseURL;
import com.gogrocersm.storemanager.Model.My_order_detail_model;
import com.gogrocersm.storemanager.MainActivity;
import com.gogrocersm.storemanager.R;
import com.gogrocersm.storemanager.util.ConnectivityReceiver;
import com.gogrocersm.storemanager.util.CustomVolleyJsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrderDeatil extends AppCompatActivity {
    TextView status, order_id, customer_name, order_socity, customer_phone, order_date, order_time, ammount;
    ImageView Phone;
    String phone_number = "";
    private RecyclerView rv_detail_order;
    private String sale_id ,store_id , cartid;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView back_button;
    Button reject_btn , confirm_order;
    private List<My_order_detail_model> my_order_detail_modelList = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        store_id = sharedPreferences.getString("id", "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.order_detail));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyOrderDeatil.this, MainActivity.class);
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
        reject_btn = findViewById(R.id.reject_order);
        confirm_order = findViewById(R.id.confirm_order);

        sale_id = getIntent().getStringExtra("sale_id");

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
        cartid = getIntent().getStringExtra("cart_id");

        reject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_reject();

            }
        });

        confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmorder();
            }
        });

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


//        Phone = (ImageView) findViewById(R.id.make_call);
//        Phone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isPermissionGranted()) {
//                    call_action();
//                }
//
//            }
//
//        });


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
        jsonArray = BaseURL.order_detail;

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject object = jsonArray.getJSONObject(i);

                My_order_detail_model my_order_detail_model1 = new My_order_detail_model();
                my_order_detail_model1.setProduct_name(object.getString("product_name"));
                my_order_detail_model1.setPrice(object.getString("price"));
                my_order_detail_model1.setProduct_image(object.getString("varient_image"));
                my_order_detail_model1.setQty(object.getString("qty"));
                my_order_detail_model1.setStore_order_id(object.getString("store_order_id"));


                my_order_detail_modelList.add(my_order_detail_model1);
                My_order_detail_adapter adapter = new My_order_detail_adapter(my_order_detail_modelList);
                rv_detail_order.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        if (my_order_detail_modelList.isEmpty()) {
            Toast.makeText(MyOrderDeatil.this, getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
        }
    }

        private void order_reject() {

            String tag_json_obj = "json store req";
            Map<String, String> params = new HashMap<String, String>();
            params.put("store_id", store_id);
            params.put("cart_id",cartid);
            Log.d("xx",store_id);

            Log.d("xx",cartid);


            CustomVolleyJsonArrayRequest jsonObjectRequest = new CustomVolleyJsonArrayRequest(Request.Method.POST, BaseURL.order_rejected, params
                    , new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("Tag", response.toString());

                    try {

                        JSONObject object = response.getJSONObject(0);
                        String result = object.getString("result");

                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        Toast.makeText(MyOrderDeatil.this, ""+result, Toast.LENGTH_SHORT).show();

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

    private void confirmorder () {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id", store_id);
        params.put("cart_id",cartid);
        Log.d("xx",store_id);

        Log.d("xx",cartid);


        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.storeconfirm, params
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag", response.toString());

                try {

                    String status = response.getString("status");
                    String message = response.getString("message");
                    if (status.contains("1")){

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        Toast.makeText(MyOrderDeatil.this, ""+message, Toast.LENGTH_SHORT).show();

                    }


                }

           catch (JSONException e) {
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

    class My_order_detail_adapter extends RecyclerView.Adapter<My_order_detail_adapter.MyViewHolder> {

        private List<My_order_detail_model> modelList;
        private List<My_order_detail_model> itemList;
        private Context context;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_title, tv_price, tv_qty;
            public ImageView iv_img ,delete;
            String store_order_id;

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
        public My_order_detail_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_my_order_detail_rv, parent, false);

            context = parent.getContext();

            return new My_order_detail_adapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final My_order_detail_model mList = modelList.get(position);

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

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    delete(mList.getStore_order_id());

                }
            });

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
//                    call_action();
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

    private void delete(String store_order_id) {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("store_order_id", store_order_id);
        Log.d("xx",store_order_id);


        CustomVolleyJsonArrayRequest jsonObjectRequest = new CustomVolleyJsonArrayRequest(Request.Method.POST, BaseURL.productcancelled, params
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Tag", response.toString());

                try {

                    JSONObject object = response.getJSONObject(0);
                    String result = object.getString("result");


                    Toast.makeText(MyOrderDeatil.this, ""+result, Toast.LENGTH_SHORT).show();

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




