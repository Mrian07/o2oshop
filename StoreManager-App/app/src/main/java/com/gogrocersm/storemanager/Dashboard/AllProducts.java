package com.gogrocersm.storemanager.Dashboard;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.franmontiel.localechanger.LocaleChanger;
import com.gogrocersm.storemanager.Adapter.AllProductsAdapter;
import com.gogrocersm.storemanager.Adapter.Select_Product_Adapter;
import com.gogrocersm.storemanager.AppController;
import com.gogrocersm.storemanager.Config.BaseURL;
import com.gogrocersm.storemanager.MainActivity;
import com.gogrocersm.storemanager.Model.AlllProductModel;
import com.gogrocersm.storemanager.R;
import com.gogrocersm.storemanager.util.ConnectivityReceiver;
import com.gogrocersm.storemanager.util.CustomVolleyJsonRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllProducts extends AppCompatActivity implements AllProductsAdapter.AllProductsAdapterListener {
    private RecyclerView mList;
    private List<AlllProductModel> movieList;
    private AllProductsAdapter adapter;
    private SearchView searchView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String store_id;
    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        sharedPreferences = getSharedPreferences("logindata", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        store_id = sharedPreferences.getString("id", "");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.all_product));
        mList = findViewById(R.id.main_list);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllProducts.this, MainActivity.class);
                startActivity(intent);
            }
        });
        movieList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mList.setLayoutManager(mLayoutManager);
        mList.setItemAnimator(new DefaultItemAnimator());
        mList.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        mList.setAdapter(adapter);

        if (ConnectivityReceiver.isConnected()) {
            product();
        } else {
            Toast.makeText(getApplicationContext(), "Network Issue", Toast.LENGTH_SHORT).show();
        }
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

                        AllProductsAdapter adapter = new AllProductsAdapter(getApplicationContext(),movieList);
                        mList.setAdapter(adapter);
                        mList.refreshDrawableState();
                        mList.smoothScrollToPosition(0);

                        mList.setAdapter(adapter);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onContactSelected(AlllProductModel contact) {
        //   Toast.makeText(getApplicationContext(), "Selected: " + contact.getCatogary_id() + ", " + contact.getTitle(), Toast.LENGTH_LONG).show();

    }
}
