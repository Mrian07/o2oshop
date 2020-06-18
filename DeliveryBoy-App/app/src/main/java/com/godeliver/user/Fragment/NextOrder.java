package com.godeliver.user.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.godeliver.user.Activity.HomeActivity;
import com.godeliver.user.Activity.OrderDetail;
import com.godeliver.user.Adapter.My_Past_Order_adapter;
import com.godeliver.user.Adapter.TodayOrder_adapter;
import com.godeliver.user.AppController;
import com.godeliver.user.Config.BaseURL;
import com.godeliver.user.MainActivity;
import com.godeliver.user.Model.My_Past_order_model;
import com.godeliver.user.Model.My_order_model;
import com.godeliver.user.R;
import com.godeliver.user.util.ConnectivityReceiver;
import com.godeliver.user.util.CustomVolleyJsonArrayRequest;
import com.godeliver.user.util.CustomVolleyJsonRequest;
import com.godeliver.user.util.Session_management;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.godeliver.user.Config.BaseURL.dCompleteDelievry;
import static com.godeliver.user.Config.BaseURL.dOutforDelivery;
import static com.godeliver.user.Config.BaseURL.todayOrder;
import static com.google.android.exoplayer2.mediacodec.MediaCodecInfo.TAG;

public class NextOrder  extends Fragment {
    private RecyclerView rv_myorder;
    private List<My_order_model> my_order_modelList = new ArrayList<>();
    TabHost tHost;
    private Session_management sessionManagement;
    String get_id;

    public NextOrder() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_next, container, false);

        sessionManagement = new Session_management(getActivity());
        get_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // check user can press back button or not
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

//                    Fragment fm = new Home_fragment();
//                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });

        rv_myorder = (RecyclerView) view.findViewById(R.id.rv_myorder);
        rv_myorder.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (ConnectivityReceiver.isConnected())

        {
            makeGetOrderRequest();
        } else

        {
//            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        // recyclerview item click listener
//        rv_myorder.addOnItemTouchListener(new
//
//                RecyclerTouchListener(getActivity(), rv_myorder, new RecyclerTouchListener.OnItemClickListener()
//
//        {
//            @Override
//            public void onItemClick(View view, int position) {
//                String sale_id = my_order_modelList.get(position).getSale_id();
//                String date = my_order_modelList.get(position).getOn_date();
//                String time = my_order_modelList.get(position).getDelivery_time_from() + "-" + my_order_modelList.get(position).getDelivery_time_to();
//                String total = my_order_modelList.get(position).getTotal_amount();
//                String status = my_order_modelList.get(position).getStatus();
//                String deli_charge = my_order_modelList.get(position).getDelivery_charge();
//                Intent intent=new Intent(getContext(), MyOrderDetail.class);
//                intent.putExtra("sale_id", sale_id);
//                intent.putExtra("date", date);
//                intent.putExtra("time", time);
//                intent.putExtra("total", total);
//                intent.putExtra("status", status);
//                intent.putExtra("deli_charge", deli_charge);
//                startActivity(intent);
//
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));

        return view;
    }

    private void makeGetOrderRequest() {
        String tag_json_obj = "json_socity_req";
        HashMap<String,String> param = new HashMap<>();
        param.put("dboy_id",get_id);
        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                todayOrder, param, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                Log.d("rdtfyghj",response.toString());
                try {

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject obj = response.getJSONObject(i);
                        String saleid = obj.getString("cart_id");
                        String placedon = obj.getString("delivery_date");
                        String timefrom = obj.getString("time_slot");
                        //  String timeto=obj.getString("delivery_time_from");
                        //String item = obj.getString("total_items");
                        String ammount = obj.getString("remaining_price");
                        String status = obj.getString("order_status");

                        String user_address = obj.getString("user_address");
                        String store_Address = obj.getString("store_address");
                        //  String house = obj.getString("house_no");
                        String rename = obj.getString("user_name");
                        String renumber = obj.getString("user_phone");
                        String store_name = obj.getString("store_name");
                        String total_items = obj.getString("total_items");

                        String store_lat = obj.getString("store_lat");
                        String store_lng = obj.getString("store_lng");
                        String user_lat = obj.getString("user_lat");
                        String user_lng = obj.getString("user_lng");
                        String dboy_lat = obj.getString("dboy_lat");
                        String dboy_lng = obj.getString("dboy_lng");

                        My_order_model my_order_model = new My_order_model();
                        // my_order_model.setSocityname(society);
                        my_order_model.setHouse(user_address);
                        my_order_model.setTotal_items(total_items);
                        my_order_model.setRecivername(rename);
                        my_order_model.setRecivermobile(renumber);
                        my_order_model.setDelivery_time_from(timefrom);
                        my_order_model.setSale_id(saleid);
                        my_order_model.setOn_date(placedon);
                        my_order_model.setTotal_amount(ammount);
                        my_order_model.setStatus(status);
                        my_order_model.setStore_name(store_name);

                        my_order_model.setLat(store_lat);
                        my_order_model.setLng(store_lng);

                        my_order_model.setUserLat(user_lat);
                        my_order_model.setUserLong(user_lng);

                        my_order_model.setDbLat(dboy_lat);
                        my_order_model.setDbuserLong(dboy_lng);


                        //   my_order_model.setTotal_items(item);
                        // my_order_model.setDelivery_time_to(timefrom);

                        my_order_modelList.add(my_order_model);
                    }
                   My_Order_Adapter myPendingOrderAdapter = new My_Order_Adapter(my_order_modelList);
                    rv_myorder.setAdapter(myPendingOrderAdapter);
                    myPendingOrderAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }




                if (my_order_modelList.isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param = new HashMap<>();
                param.put("dboy_id",get_id);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);

    }

    public class My_Order_Adapter extends RecyclerView.Adapter<My_Order_Adapter.MyViewHolder> {

        private List<My_order_model> modelList;
        private LayoutInflater inflater;
        private android.support.v4.app.Fragment currentFragment;
        ProgressDialog progressDialog;
        private Context context;

        public My_Order_Adapter(Context context, List<My_order_model> modemodelList, final android.support.v4.app.Fragment currentFragment) {

            this.context = context;
            this.modelList = modemodelList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.currentFragment = currentFragment;

        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_orderno, tv_status, tv_date, tv_time, tv_price, tv_item, relativetextstatus, tv_tracking_date, tv_socity,
                    tv_recivername, tv_house,tv_storename;

            Button get_dirc, pickorder;
            ImageView call;
            CardView cardView;

            public MyViewHolder(View view) {
                super(view);

                tv_storename=view.findViewById(R.id.store_name);
                get_dirc = view.findViewById(R.id.get_dirc);
                tv_orderno = (TextView) view.findViewById(R.id.tv_order_no);
                tv_status = (TextView) view.findViewById(R.id.tv_order_status);
                relativetextstatus = (TextView) view.findViewById(R.id.status);
                tv_tracking_date = (TextView) view.findViewById(R.id.tracking_date);
                tv_date = (TextView) view.findViewById(R.id.tv_order_date);
                tv_time = (TextView) view.findViewById(R.id.tv_order_time);
                tv_price = (TextView) view.findViewById(R.id.tv_order_price);
                tv_item = (TextView) view.findViewById(R.id.tv_order_item);
                tv_socity = view.findViewById(R.id.tv_societyname);
                tv_house = view.findViewById(R.id.tv_house);
                tv_recivername = view.findViewById(R.id.tv_recivername);
                call = view.findViewById(R.id.call);
                pickorder = view.findViewById(R.id.order_picked);
                cardView = view.findViewById(R.id.card_view);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        String saleid = modelList.get(position).getSale_id();
//                        String placedon = modelList.get(position).getOn_date();
//                        String time = modelList.get(position).getDelivery_time_from() + "-" + modelList.get(position).getDelivery_time_to();
//                        String item = modelList.get(position).getTotal_items();
//                        String ammount = modelList.get(position).getTotal_amount();
//                        String status = modelList.get(position).getStatus();
//                        String society = modelList.get(position).getSocityname();
//                        String house = modelList.get(position).getHouse();
//                        String recivername = modelList.get(position).getRecivername();
//                        String recivermobile = modelList.get(position).getRecivermobile();
//                        Intent intent = new Intent(context, OrderDetail.class);
//                        intent.putExtra("sale_id", saleid);
//                        intent.putExtra("placedon", placedon);
//                        intent.putExtra("time", time);
//                        intent.putExtra("item", item);
//                        intent.putExtra("ammount", ammount);
//                        intent.putExtra("status", status);
//                        intent.putExtra("socity_name", society);
//                        intent.putExtra("house_no", house);
//                        intent.putExtra("receiver_name", recivername);
//                        intent.putExtra("receiver_mobile", recivermobile);
//                        context.startActivity(intent);
////
//                    }
//                }
//            });


            }
        }

        public My_Order_Adapter(List<My_order_model> modelList) {
            this.modelList = modelList;
        }

        @Override
        public My_Order_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_rv, parent, false);
            context = parent.getContext();
            return new My_Order_Adapter.MyViewHolder(itemView);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(My_Order_Adapter.MyViewHolder holder, final int position) {
            final My_order_model mList = modelList.get(position);
            progressDialog=new ProgressDialog(context);
            progressDialog.setMessage("Loading");

            try {
                holder.tv_orderno.setText(mList.getSale_id());

               /* if (mList.getStatus().equals("0")) {
                    holder.pickorder.setVisibility(View.GONE);
                    holder.relativetextstatus.setEnabled(false);

                    holder.tv_status.setText(context.getResources().getString(R.string.pending));
                    holder.relativetextstatus.setText(context.getResources().getString(R.string.pending));
                } else*/
                if (mList.getStatus().equals("Confirmed")) {
                    // holder.tv_status.setText(context.getResources().getString(R.string.confirm));
                    holder.pickorder.setVisibility(View.GONE);
                    holder.relativetextstatus.setEnabled(true);
                    //  holder.relativetextstatus.setText(context.getResources().getString(R.string.confirm));
                    holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
                    holder.tv_status.setText(context.getResources().getString(R.string.confirm));
                    holder.relativetextstatus.setText(context.getResources().getString(R.string.outfordeliverd));
                    holder.get_dirc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + mList.getLat() + "," + mList.getLng()));
                            context.startActivity(intent);
                        }
                    });
                    holder.relativetextstatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            order_OutDelivery(mList.getSale_id());

                        }
                    });
                } else if (mList.getStatus().equals("Out_For_Delivery")) {
                    holder.get_dirc.setVisibility(View.VISIBLE);
                    holder.pickorder.setVisibility(View.GONE);
                    holder.relativetextstatus.setEnabled(true);
                    holder.tv_status.setText(context.getResources().getString(R.string.outfordeliverd));
                    holder.relativetextstatus.setText(context.getResources().getString(R.string.delivered));
                    holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
                    holder.get_dirc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + mList.getDbLat() + "," + mList.getDbuserLong()));
                            context.startActivity(intent);
                        }
                    });
                    holder.relativetextstatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String saleid = modelList.get(position).getSale_id();
                            String placedon = modelList.get(position).getOn_date();
                            String time = modelList.get(position).getDelivery_time_from();
                            String item = modelList.get(position).getTotal_items();
                            String ammount = modelList.get(position).getTotal_amount();
                            String ssstatus = modelList.get(position).getStatus();
                            String society = modelList.get(position).getSocityname();
                            String house = modelList.get(position).getHouse();
                            String recivername = modelList.get(position).getRecivername();
                            String recivermobile = modelList.get(position).getRecivermobile();
                            String stotreAddr = modelList.get(position).getStore_name();
                            Intent intent = new Intent(context, OrderDetail.class);
                            intent.putExtra("sale_id", saleid);
                            intent.putExtra("placedon", placedon);
                            intent.putExtra("time", time);
                            intent.putExtra("item", item);
                            intent.putExtra("ammount", ammount);
                            intent.putExtra("status", ssstatus);
                            intent.putExtra("socity_name", society);
                            intent.putExtra("house_no", house);
                            intent.putExtra("receiver_name", recivername);
                            intent.putExtra("receiver_mobile", recivermobile);
                            intent.putExtra("storeAddr", stotreAddr);
                            context.startActivity(intent);
                        }

/*
                        private void order_picked(final String sale_id) {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST,dCompleteDelievry, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("fgh",response);

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        String msg = jsonObject.getString("message");
                                        if (status.equals("1")) {
                                            String saleid = modelList.get(position).getSale_id();
                                            String placedon = modelList.get(position).getOn_date();
                                            String time = modelList.get(position).getDelivery_time_from() + "-" + modelList.get(position).getDelivery_time_to();
                                            String item = modelList.get(position).getTotal_items();
                                            String ammount = modelList.get(position).getTotal_amount();
                                            String ssstatus = modelList.get(position).getStatus();
                                            String society = modelList.get(position).getSocityname();
                                            String house = modelList.get(position).getHouse();
                                            String recivername = modelList.get(position).getRecivername();
                                            String recivermobile = modelList.get(position).getRecivermobile();
                                            String stotreAddr = modelList.get(position).getStore_name();
                                            Intent intent = new Intent(context, OrderDetail.class);
                                            intent.putExtra("sale_id", saleid);
                                            intent.putExtra("placedon", placedon);
                                            intent.putExtra("time", time);
                                            intent.putExtra("item", item);
                                            intent.putExtra("ammount", ammount);
                                            intent.putExtra("status", ssstatus);
                                            intent.putExtra("socity_name", society);
                                            intent.putExtra("house_no", house);
                                            intent.putExtra("receiver_name", recivername);
                                            intent.putExtra("receiver_mobile", recivermobile);
                                            intent.putExtra("storeAddr", stotreAddr);
                                            context.startActivity(intent);

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new com.android.volley.Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("Error [" + error + "]");

                                }
                            }) {

                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("cart_id",sale_id);
                                    Log.d("asdf",sale_id);

                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                            requestQueue.getCache().clear();
                            requestQueue.add(stringRequest);
                        }
*/
                    });
                }

                /* else if (mList.getStatus().equals("4")) {
                    holder.tv_status.setText(context.getResources().getString(R.string.delivered));
                    holder.pickorder.setVisibility(View.GONE);
                    holder.relativetextstatus.setEnabled(false);

                    holder.relativetextstatus.setText(context.getResources().getString(R.string.delivered));
                    holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
                } else if (mList.getStatus().equals("3")) {
                    holder.tv_status.setText("Cancel");
                    holder.relativetextstatus.setEnabled(false);
                    holder.pickorder.setVisibility(View.GONE);
                    holder.relativetextstatus.setText("Cancel");
                    holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
                }
                else if (mList.getStatus().equals("5")) {

                    holder.tv_status.setText("Ready to Pickup");
                    holder.relativetextstatus.setEnabled(false);
                    holder.pickorder.setVisibility(View.VISIBLE);
                    holder.relativetextstatus.setText("Ready to Pickup");
                    holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));

                }
*/
/*
                holder.relativetextstatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String saleid = modelList.get(position).getSale_id();
                        String placedon = modelList.get(position).getOn_date();
                        String time = modelList.get(position).getDelivery_time_from() + "-" + modelList.get(position).getDelivery_time_to();
                        String item = modelList.get(position).getTotal_items();
                        String ammount = modelList.get(position).getTotal_amount();
                        String status = modelList.get(position).getStatus();
                        String society = modelList.get(position).getSocityname();
                        String house = modelList.get(position).getHouse();
                        String recivername = modelList.get(position).getRecivername();
                        String recivermobile = modelList.get(position).getRecivermobile();
                        Intent intent = new Intent(context, OrderDetail.class);
                        intent.putExtra("sale_id", saleid);
                        intent.putExtra("placedon", placedon);
                        intent.putExtra("time", time);
                        intent.putExtra("item", item);
                        intent.putExtra("ammount", ammount);
                        intent.putExtra("status", status);
                        intent.putExtra("socity_name", society);
                        intent.putExtra("house_no", house);
                        intent.putExtra("receiver_name", recivername);
                        intent.putExtra("receiver_mobile", recivermobile);
                        context.startActivity(intent);
//
                    }
                });*/
              /*  holder.get_dirc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + mList.getLat() + "," + mList.getLng()));
                        context.startActivity(intent);
                    }
                });*/


            } catch (Exception e) {

            }

            holder.tv_date.setText(mList.getOn_date());
            holder.tv_tracking_date.setText(mList.getOn_date());
          //  holder.tv_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
            holder.tv_time.setText(mList.getDelivery_time_from());
            holder.tv_price.setText(context.getResources().getString(R.string.currency) + mList.getTotal_amount());
            holder.tv_item.setText(context.getResources().getString(R.string.tv_cart_item) + mList.getTotal_items());
            holder.tv_socity.setText(mList.getSocityname());
            holder.tv_house.setText(mList.getHouse());
            holder.tv_storename.setText(mList.getStore_name());
            holder.tv_recivername.setText(modelList.get(position).getRecivername());
            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isPermissionGranted()) {
                        call_action(mList.getRecivermobile());
                    }

                }
            });

           /* holder.pickorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    order_picked(mList.getSale_id());

                }
            });*/
        }


        @Override
        public int getItemCount() {
            return modelList.size();

        }

        public boolean isPermissionGranted() {
            if (Build.VERSION.SDK_INT >= 23) {
                if (context.checkSelfPermission(Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.v("TAG", "Permission is granted");
                    return true;
                } else {

                    Log.v("TAG", "Permission is revoked");
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    return false;
                }
            } else { //permission is automatically granted on sdk<23 upon installation
                Log.v("TAG", "Permission is granted");
                return true;
            }
        }

        public void call_action(String number) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));

            context.startActivity(callIntent);
        }
        private void order_OutDelivery(final String saleid) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST,dOutforDelivery, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("fgh",response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");
                        if (status.equals("1")) {

                            TodayOrder tm = new TodayOrder();
                            android.support.v4.app.FragmentManager manager21 = getFragmentManager();
                            android.support.v4.app.FragmentTransaction transaction21 = manager21.beginTransaction();
                            transaction21.replace(R.id.contentPanel, tm);
                            transaction21.commit();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error [" + error + "]");

                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("cart_id",saleid);
                    Log.d("fgh",saleid);

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);



        }

    }

}
