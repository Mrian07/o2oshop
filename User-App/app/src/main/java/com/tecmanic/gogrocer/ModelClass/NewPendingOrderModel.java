package com.tecmanic.gogrocer.ModelClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewPendingOrderModel implements Serializable {


    String order_status;
    String delivery_date;
    String time_slot;
    String payment_method;
    String payment_status;
    String paid_by_wallet;
    String cart_id;
    String price;
    String del_charge;
    ArrayList<NewPendingDataModel> data;

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        if (payment_status==null){
            this.payment_status = "";
        }else {
            this.payment_status = payment_status;
        }

    }

    public String getPaid_by_wallet() {
        return paid_by_wallet;
    }

    public void setPaid_by_wallet(String paid_by_wallet) {
        this.paid_by_wallet = paid_by_wallet;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDel_charge() {
        return del_charge;
    }

    public void setDel_charge(String del_charge) {
        this.del_charge = del_charge;
    }

    public ArrayList<NewPendingDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<NewPendingDataModel> data) {
        this.data = data;
    }
}
