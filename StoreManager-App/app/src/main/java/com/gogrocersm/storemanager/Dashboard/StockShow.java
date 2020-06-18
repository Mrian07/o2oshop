package com.gogrocersm.storemanager.Dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gogrocersm.storemanager.Adapter.AllProductsAdapter;
import com.gogrocersm.storemanager.Adapter.StockShowAdapter;
import com.gogrocersm.storemanager.AppController;
import com.gogrocersm.storemanager.Config.BaseURL;
import com.gogrocersm.storemanager.MainActivity;
import com.gogrocersm.storemanager.Model.AlllProductModel;
import com.gogrocersm.storemanager.R;
import com.gogrocersm.storemanager.util.CustomVolleyJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockShow extends AppCompatActivity {

    RecyclerView recyclerView ;
    private List<AlllProductModel> movieList;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String store_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_show);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Stock");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StockShow.this, MainActivity.class);
                startActivity(intent);
            }
        });
        movieList = new ArrayList<>();

        recyclerView = findViewById(R.id.stock_recyler_view);
        sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        store_id = sharedPreferences.getString("id", "");

        product();
    }


    private void product() {

        movieList.clear();
        //  category2_models.clear();
        // category3_models.clear();
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id", store_id);
        Log.d("dd",store_id);

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.storeproducts, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag", response.toString());
                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {

                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            AlllProductModel movie = new AlllProductModel();
                            movie.setCat_id(jsonObject.getString("cat_id"));
                            movie.setProduct_name(jsonObject.getString("product_name"));
                            movie.setMrp(jsonObject.getString("mrp"));
                            movie.setPrice(jsonObject.getString("price"));
                            movie.setQuantity(jsonObject.getString("quantity"));
                            movie.setUnit(jsonObject.getString("unit"));
                            movie.setP_id(jsonObject.getString("p_id"));

                            movie.setVarient_image(jsonObject.getString("varient_image"));

                            movieList.add(movie);

                        }

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        StockShowAdapter adapter = new StockShowAdapter(getApplicationContext(),movieList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.refreshDrawableState();
                        recyclerView.smoothScrollToPosition(0);

                        recyclerView.setAdapter(adapter);
                    }
                    else {

                        // Toast.makeText(select_city.this, "data not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

}
