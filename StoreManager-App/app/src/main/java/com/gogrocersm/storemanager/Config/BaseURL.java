package com.gogrocersm.storemanager.Config;


import org.json.JSONArray;

public class BaseURL {

    static final String APP_NAME = "StoreManager";
    public static final String PREFS_NAME = "GroceryLoginPrefs";
    public static final String PREFS_NAME2 = "GroceryLoginPrefs2";
    public static final String IS_LOGIN = "isLogin";
    public static final String KEY_NAME = "user_fullname";
    public static final String KEY_EMAIL = "user_email";
    public static final String KEY_ID = "user_id";
    public static JSONArray order_detail = new JSONArray();
    public static final String KEY_MOBILE = "user_phone";
    public static final String KEY_IMAGE = "user_image";
    public static final String KEY_PINCODE = "pincode";
    public static final String KEY_SOCITY_ID = "Socity_id";
    public static final String KEY_SOCITY_NAME = "socity_name";
    public static final String KEY_HOUSE = "house_no";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_DELIVERY_BOY_ID = "BOY_ID";
    public static final String KEY_STOCK_PRODUCTS_ID = "p_id";
    public static final String stock = "stock";

    public static final String KEY_DELIVERY_BOY_NAME = "BOY_NAME";
    public static final String KEY_ORDER_ID = "ORDER_ID";

    public static String BASE_URL = "https://thecodecafe.in/gogrocer-ver2.0/api/store/";


    public static String BASE_URLGOGrocer = "https://thecodecafe.in/gogrocer-ver2.0/api/";

    public static String LOGIN_URL = BASE_URL + "store_login";
    public static String storeassigned_url = BASE_URL + "storeassigned";
    public static String storeunassigned_url = BASE_URL + "storeunassigned";
    public static String productcancelled = BASE_URL + "productcancelled";
    public static String order_rejected = BASE_URL + "order_rejected";
    public static String storeconfirm = BASE_URL + "storeconfirm";
    public static String productselect = BASE_URL + "productselect";
    public static String store_stock_update = BASE_URL + "store_stock_update";
    public static String store_delete_product = BASE_URL + "store_delete_product";
    public static String storeproducts = BASE_URL + "storeproducts";


    public static String currency = BASE_URLGOGrocer + "currency";

    public static String IMG_PROFILE_URL = BASE_URL + "uploads/profile/";
    public static String FORGOT_URL = BASE_URL + "index.php/api/forgot_password";
    public static String GET_STOCK = BASE_URLGOGrocer+"index.php/api/stock";
//    public static String STOCK_UPDATE = BASE_URLGOGrocer+"index.php/api/stock_insert";
    public static String Update_user=BASE_URL+"store_profile";
    public static String ASSIGN_ORDER = BASE_URLGOGrocer + "index.php/api/assign_order";
    public static String OrderDetail = BASE_URLGOGrocer + "index.php/api/order_details";
    public static String IMG_PRODUCT_URL ="https://thecodecafe.in/gogrocer-ver2.0/";


    //Dashboard Items
//    public static String ALL_PRODUCTS_URL = BASE_URL + "index.php/api/all_products";

    public static String APP_USER_URL = BASE_URL + "index.php/api/all_users";

    public static String STOCK_LIST = BASE_URL + "index.php/api/get_leftstock";


}
