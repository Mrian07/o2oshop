package com.gogrocersm.storemanager.Fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gogrocersm.storemanager.Adapter.My_Nextday_Order_Adapter;
import com.gogrocersm.storemanager.Adapter.My_Today_Order_Adapter;
import com.gogrocersm.storemanager.AppController;
import com.gogrocersm.storemanager.Config.BaseURL;
import com.gogrocersm.storemanager.Config.SharedPref;
import com.gogrocersm.storemanager.MainActivity;
import com.gogrocersm.storemanager.Model.NextdayOrderModel;
import com.gogrocersm.storemanager.Model.TodayOrderModel;
import com.gogrocersm.storemanager.R;
import com.gogrocersm.storemanager.util.CustomVolleyJsonArrayRequest;
import com.gogrocersm.storemanager.util.CustomVolleyJsonRequest;
import com.gogrocersm.storemanager.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Home_fragment extends Fragment {

    private static String TAG = Home_fragment.class.getSimpleName();
    private RecyclerView rv_today_orders,rv_next_day_orders;
    private My_Today_Order_Adapter my_today_order_adapter;
    private My_Nextday_Order_Adapter my_nextday_order_adapter;
    private List<TodayOrderModel> movieList = new ArrayList<>();
    private List<NextdayOrderModel> nextdayOrderModels = new ArrayList<>();
    ProgressDialog pd;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
String store_id;
    private LinearLayout linearLayout;

    public Home_fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
        rv_today_orders = (RecyclerView) view.findViewById(R.id.rv_today_order);
        rv_next_day_orders=(RecyclerView)view.findViewById(R.id.rv_next_order);
        rv_today_orders.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_next_day_orders.setLayoutManager(new LinearLayoutManager(getActivity()));


        sharedPreferences = getActivity().getSharedPreferences("logindata", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        store_id = sharedPreferences.getString("id", "");
        Log.d("dd",store_id);

        getTodayOrders();
        getNextDayOrders();
        return view;


    }


    private void getTodayOrders() {
        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id", store_id);
        Log.d("dd",store_id);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseURL.storeassigned_url, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                            final List<TodayOrderModel> data = new ArrayList<>();
                            Log.i("eclipse", "Response=" + response);
                            // We set the response data in the TextView
                            try {
//                                JSONObject object = new JSONObject(response.toString());
//                                JSONArray Jarray = object.getJSONArray("today_orders");
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject json_data = response.getJSONObject(i);
                                    TodayOrderModel brandModel = new TodayOrderModel();
                                    brandModel.cart_id = json_data.getString("cart_id");
                                    final String sal=brandModel.cart_id;
                                    SharedPref.putString(getActivity(), BaseURL.KEY_ORDER_ID, sal);
                                    brandModel.user_name = json_data.getString("user_name");
                                    brandModel.user_phone = json_data.getString("user_phone");
                                    brandModel.order_price = json_data.getString("order_price");
                                    brandModel.time_slot = json_data.getString("time_slot");
                                    brandModel.payment_mode = json_data.getString("payment_mode");
                                    brandModel.order_status = json_data.getString("order_status");
                                    brandModel.delivery_boy_name = json_data.getString("delivery_boy_name");
                                    brandModel.delivery_boy_phone = json_data.getString("delivery_boy_phone");
                                    brandModel.delivery_date = json_data.getString("delivery_date");
                                    brandModel.setJsonArray(json_data.getJSONArray("order_details"));
                                    data.add(brandModel);
                                }
                                my_today_order_adapter = new My_Today_Order_Adapter(getActivity(), data);
                                rv_today_orders.setAdapter(my_today_order_adapter);
                                rv_today_orders.refreshDrawableState();
                                rv_today_orders.smoothScrollToPosition(0);
                            } catch (JSONException e) {
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error [" + error + "]");
                }
            });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void getNextDayOrders() {
        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id", store_id);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                BaseURL.storeunassigned_url, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                final List<NextdayOrderModel> data = new ArrayList<>();
                Log.i("hdhdjfd", "Response=" + response);
                // We set the response data in the TextView
                try {
//                                JSONObject object = new JSONObject(response.toString());
//                                JSONArray Jarray = object.getJSONArray("today_orders");
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject json_data = response.getJSONObject(i);
                        NextdayOrderModel brandModel = new NextdayOrderModel();
                        brandModel.cart_id = json_data.getString("cart_id");
                        final String sal = brandModel.cart_id;
                        SharedPref.putString(getActivity(), BaseURL.KEY_ORDER_ID, sal);
                        brandModel.user_name = json_data.getString("user_name");
                        brandModel.user_phone = json_data.getString("user_phone");
                        brandModel.order_price = json_data.getString("order_price");
                        brandModel.payment_mode = json_data.getString("payment_mode");
                        brandModel.order_status = json_data.getString("order_status");

                        brandModel.cart_id = json_data.getString("cart_id");
//                        brandModel.delivery_boy_phone = json_data.getString("delivery_boy_phone");
                        brandModel.delivery_date = json_data.getString("delivery_date");
                        brandModel.setJsonArray(json_data.getJSONArray("order_details"));
                        data.add(brandModel);

                        my_nextday_order_adapter = new My_Nextday_Order_Adapter(getActivity(), data);
                        rv_next_day_orders.setAdapter(my_nextday_order_adapter);
                        rv_next_day_orders.refreshDrawableState();
                        rv_next_day_orders.smoothScrollToPosition(0);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error [" + error + "]");
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}

