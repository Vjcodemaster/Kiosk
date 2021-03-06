package app_utility;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kiosk.autochip.MainActivity;
import com.kiosk.autochip.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

import static app_utility.StaticReferenceClass.DB_NAME;
import static app_utility.StaticReferenceClass.PASSWORD;
import static app_utility.StaticReferenceClass.PORT_NO;
import static app_utility.StaticReferenceClass.SERVER_URL;
import static app_utility.StaticReferenceClass.USER_ID;

public class VolleyTask {

    private Context context;
    private int mStatusCode = 0;
    //private JSONObject jsonObject = new JSONObject();
    private HashMap<String, String> params;
    //private int position;
    String msg;
    String sDescription;

    private int ERROR_CODE = 0;

    ArrayList<String> alProducts;
    ArrayList<String> alSubCategory;
    ArrayList<String> alMainCategory;
    //ArrayList<Integer> alID;
    ArrayList<Integer> alProductName;
    ArrayList<Integer> alProductSubCategory;

    private HashMap<Integer, String> hmImageAddressWithDBID = new HashMap<>();

    private LinkedHashMap<String, HashMap<String, ArrayList<String>>> lhm = new LinkedHashMap<>();
    int stockFlag;
    String URL;
    JSONObject jsonObject = new JSONObject();
    DatabaseHandler dbh;

    /*public VolleyTask(Context context, JSONObject jsonObject, String sCase, int stockFlag, String URL) {
        this.context = context;
        this.jsonObject = jsonObject;
        this.stockFlag = stockFlag;
        this.URL = URL;
        Volley(sCase);
    }*/

    public VolleyTask(Context context, HashMap<String, String> params, String sCase, String URL) {
        this.context = context;
        this.params = params;
        this.URL = URL;
        Volley(sCase);
        dbh = new DatabaseHandler(context);
    }

    private void Volley(String sCase) {
        switch (sCase) {
            case "REQUEST_PRODUCTS":
                requestProducts(URL);
                break;
            case "ODOO_LOGIN":
                loginOdoo();
                break;
        }
    }

    private void requestProducts(String URL) {

        StringRequest request = new StringRequest(
                Request.Method.POST, URL, //BASE_URL + Endpoint.USER
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // Success
                        onPostProductsReceived(mStatusCode, response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg = "No response from Server";
                    }
                }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                mStatusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return new JSONObject(params).toString().getBytes();
                //return params.toString().getBytes();
                //return params.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS));

        /*request.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        // add the request object to the queue to be executed
        ApplicationController.getInstance().addToRequestQueue(request);

    }

    private void loginOdoo() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            }
        });

    }


    private void onPostProductsReceived(int mStatusCode, String response) {
        if (mStatusCode == 200) {
            JSONObject jsonObject = null;
            int sResponseCode = 0;
            //Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
            try {
                jsonObject = new JSONObject(response);
                String sResult = jsonObject.getString("result");
                jsonObject = new JSONObject(sResult);
                sResponseCode = jsonObject.getInt("response_code");
            } catch (Exception e) {
                e.printStackTrace();
                /*ERROR_CODE = 900;
                msg = "No IDS matched";
                e.printStackTrace();
                sendMsgToActivity();*/
                return;
            }
            if (sResponseCode == 0) {
                msg = "Unable to connect to server, please try again later";
                sendMsgToActivity();
                return;
            }

            switch (sResponseCode) {
                case 201: //success
                    ERROR_CODE = 201;


                    try {
                        //sDescription = jsonObject.getString("description");
                        msg = jsonObject.getString("message");
                        JSONArray jsonArray = new JSONArray(msg);

                        /*String sMainCategory;
                        String sSubCategory;
                        alProducts = new ArrayList<>();*/

                        //alID = new ArrayList<>();
                        HashMap<String, ArrayList<String>> hm;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            //String product = jsonArray.getJSONObject(i).getString("product");
                            //String quantity = jsonArray.getJSONObject(i).getString("quantity_received");

                            String sMainCategory = jsonArray.getJSONObject(i).get("maincategory").toString();
                            String sSubCategory = jsonArray.getJSONObject(i).get("subcategory").toString();
                            StringBuilder sb = new StringBuilder();
                            sb.append(jsonArray.getJSONObject(i).get("name"));
                            sb.append("##");
                            sb.append(jsonArray.getJSONObject(i).get("image"));
                            sb.append("##");
                            sb.append(jsonArray.getJSONObject(i).get("description"));
                            sb.append("##");
                            sb.append(jsonArray.getJSONObject(i).get("techspecheader"));
                            sb.append("##");
                            sb.append(jsonArray.getJSONObject(i).get("techspecvalue"));
                            sb.append("##");
                            sb.append(sSubCategory);
                            sb.append("##");
                            sb.append(sMainCategory);
                            alProducts = new ArrayList<>();

                            if (lhm.get(sMainCategory) != null) {
                                hm = new HashMap<>(Objects.requireNonNull(lhm.get(sMainCategory)));
                                if (hm.get(sSubCategory) != null) {
                                    alProducts.addAll(Objects.requireNonNull(hm.get(sSubCategory)));
                                    alProducts.add(sb.toString());
                                    hm = new HashMap<>();
                                    hm.put(sSubCategory, alProducts);
                                    lhm.put(sMainCategory, hm);
                                }
                            } else {
                                hm = new HashMap<>();
                                alProducts.add(sb.toString());
                                hm.put(sSubCategory, alProducts);
                                lhm.put(sMainCategory, hm);
                            }
                        }
                        ArrayList<String> alMainMenuKey = new ArrayList<>(lhm.keySet());


                        //ArrayList<String> alSubCategoryKey = new ArrayList<>(hm.keySet());

                        for (int i = 0; i < alMainMenuKey.size(); i++) {
                            hm = new HashMap<>(Objects.requireNonNull(lhm.get(alMainMenuKey.get(i))));//lhm.get(alMainMenuKey.get(i));

                            ArrayList<String> alKeySet = new ArrayList<>(hm.keySet());
                            String sSubCategory = android.text.TextUtils.join("##", alKeySet);
                            dbh.addDataToMainProducts(new DataBaseHelper(alMainMenuKey.get(i), switchDescription(alMainMenuKey.get(i)), sSubCategory));
                            int mainID = dbh.getIdForStringTablePermanent(alMainMenuKey.get(i));
                            for (int j = 0; j < alKeySet.size(); j++) {
                                hm = new HashMap<>(Objects.requireNonNull(lhm.get(alMainMenuKey.get(i))));
                                ArrayList<String> alTmp = new ArrayList<>(hm.get(alKeySet.get(j)));
                                for (int k = 0; k < alTmp.size(); k++) {
                                    //ArrayList<String> alProducts = (ArrayList<String>) Arrays.asList(alTmp.get(k).split(","));
                                    ArrayList<String> alProducts = new ArrayList<>(Arrays.asList(alTmp.get(k).split("##")));
                                    dbh.addDataToIndividualProducts(new DataBaseHelper(mainID, alMainMenuKey.get(i), alKeySet.get(j), alProducts.get(0), alProducts.get(2), alProducts.get(1), "", alProducts.get(3), alProducts.get(4)));

                                    /*if (DataReceiverService.refOfService != null){
                                        String sData = String.valueOf(dbh.lastID()) + "##" + alProducts.get(1);
                                        DataReceiverService.refOfService.dataStorage.alDBIDWithAddress.add(sData);
                                    }*/
                                    int id = dbh.getRecordsCount();
                                    if (DataReceiverService.refOfService != null) {

                                        String sData = String.valueOf(id) + "##" + alProducts.get(1);
                                        String[] sSplitData = sData.split("##");
                                        ArrayList<String> alMultipleUrl = new ArrayList<>(Arrays.asList(sSplitData[1].split(",")));
                                        if (alMultipleUrl.size() > 1) {
                                            for (int l = 0; l < alMultipleUrl.size(); l++) {
                                                String sMultiple = String.valueOf(id) + "##" + alMultipleUrl.get(l);
                                                DataReceiverService.refOfService.dataStorage.alDBIDWithAddress.add(sMultiple);
                                                DataReceiverService.refOfService.dataStorage.isDataUpdatedAtleastOnce = true;
                                            }
                                        } else {
                                            DataReceiverService.refOfService.dataStorage.alDBIDWithAddress.add(sData);
                                            DataReceiverService.refOfService.dataStorage.isDataUpdatedAtleastOnce = true;
                                        }
                                    }
                                    //DataReceiverService.refOfService.dataStorage.hmImageAddressWithDBID.put(dbh.lastID(), alProducts.get(1));
                                    else
                                        hmImageAddressWithDBID.put(dbh.lastID(), alProducts.get(1));
                                }
                            }
                        }
                        //ArrayList<DataBaseHelper> dbData = new ArrayList<>(dbh.getAllProductsData());
                        ArrayList<DataBaseHelper> dbData = new ArrayList<>(dbh.getAllProductsData1());
                        ArrayList<String> alProducts = new ArrayList<>();
                        for (int i=0; i<dbData.size(); i++){
                            alProducts.add(dbData.get(i).get_individual_product_names());
                        }
                        sendMsgToActivity();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ERROR_CODE = 901;
                        msg = "Unable to reach server, please try again";
                        sendMsgToActivity();
                    }
                    break;
                /*case 201:
                    ERROR_CODE = 201;
                    try {
                        msg = jsonObject.getString("message");
                        JSONArray jsonArray = new JSONArray(msg);
                        //JSONObject jsonObject1;
                        alData = new ArrayList<>();
                        alID = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String product = jsonArray.getJSONObject(i).getString("name");
                            String quantity = jsonArray.getJSONObject(i).getString("quantity_done");

                            alData.add(product);
                            alID.add(Integer.valueOf(quantity));
                        }
                        sendMsgToActivity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 202:
                    try {
                        ERROR_CODE = 202;
                        msg = jsonObject.getString("message");
                        sendMsgToActivity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 203:
                    try {
                        ERROR_CODE = 203;
                        msg = jsonObject.getString("message");
                        sendMsgToActivity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 300: //RFID not exist
                    try {
                        ERROR_CODE = 300;
                        msg = jsonObject.getString("message");
                        sendMsgToActivity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 204: //authentication failed(wrong password)
                    //snackBarToast = new SnackBarToast(aActivity, aActivity.getResources().getString(R.string.wrong_password));
                    break;
                case 402:
                    try {
                        ERROR_CODE = 402;
                        msg = jsonObject.getString("message");
                        sendMsgToActivity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;*/
            }
        }
        /*if (circularProgressBar != null && circularProgressBar.isShowing()) {
            circularProgressBar.dismiss();
        }*/
    }

    private String switchDescription(String sKey) {
        String sResult = null;
        switch (sKey) {
            case "Commercial Kitchens":
                sResult = context.getResources().getString(R.string.commercial_kitchens);
                break;
            case "Bars & Pubs":
                sResult = context.getResources().getString(R.string.bars_pubs);
                break;
            case "Cake & Sweet Shops":
                sResult = context.getResources().getString(R.string.cake_sweet_shop);
                break;
            case "Food Retail":
                sResult = context.getResources().getString(R.string.food_retail);
                break;
            case "Food Preservation":
                sResult = context.getResources().getString(R.string.food_preservation);
                break;
            case "Biomedical":
                sResult = context.getResources().getString(R.string.bio_medical);
                break;

        }
        return sResult;
    }

    private void sendMsgToActivity() {
        try {
            MainActivity.onFragmentInteractionListener.onFragmentMessage("UPDATE_HOME_BUTTON", 0, "", "");
            //onServiceInterface.onServiceCall("RFID", ERROR_CODE, String.valueOf(this.jsonObject.get("rfids")), msg, alID, alData);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    /*try {
        //sDescription = jsonObject.getString("description");
        msg = jsonObject.getString("message");
        JSONArray jsonArray = new JSONArray(msg);

                        *//*String sMainCategory;
                        String sSubCategory;
                        alProducts = new ArrayList<>();*//*

        //alID = new ArrayList<>();
        HashMap<String, ArrayList<String>> hm;
        for (int i = 0; i < jsonArray.length(); i++) {
            //String product = jsonArray.getJSONObject(i).getString("product");
            //String quantity = jsonArray.getJSONObject(i).getString("quantity_received");

            String sMainCategory = jsonArray.getJSONObject(i).get("maincategory").toString();
            String sSubCategory = jsonArray.getJSONObject(i).get("subcategory").toString();
            StringBuilder sb = new StringBuilder();
            sb.append(jsonArray.getJSONObject(i).get("name"));
            sb.append("##");
            sb.append(jsonArray.getJSONObject(i).get("image"));
            sb.append("##");
            sb.append(jsonArray.getJSONObject(i).get("description"));
            sb.append("##");
            sb.append(sSubCategory);
            sb.append("##");
            sb.append(sMainCategory);
            alProducts = new ArrayList<>();

            if (lhm.get(sMainCategory) != null) {
                hm = new HashMap<>(Objects.requireNonNull(lhm.get(sMainCategory)));
                if (hm.get(sSubCategory) != null) {
                    alProducts.addAll(Objects.requireNonNull(hm.get(sSubCategory)));
                    alProducts.add(sb.toString());
                    hm = new HashMap<>();
                    hm.put(sSubCategory, alProducts);
                    lhm.put(sMainCategory, hm);
                }
            } else {
                hm = new HashMap<>();
                alProducts.add(sb.toString());
                hm.put(sSubCategory, alProducts);
                lhm.put(sMainCategory, hm);
            }
        }
        ArrayList<String> alMainMenuKey = new ArrayList<>(lhm.keySet());


        //ArrayList<String> alSubCategoryKey = new ArrayList<>(hm.keySet());

        for (int i = 0; i < alMainMenuKey.size(); i++) {
            hm = new HashMap<>(Objects.requireNonNull(lhm.get(alMainMenuKey.get(i))));//lhm.get(alMainMenuKey.get(i));

            ArrayList<String> alKeySet = new ArrayList<>(hm.keySet());
            String sSubCategory = android.text.TextUtils.join("##", alKeySet);
            dbh.addDataToMainProducts(new DataBaseHelper(alMainMenuKey.get(i), switchDescription(alMainMenuKey.get(i)), sSubCategory));
            int mainID = dbh.getIdForStringTablePermanent(alMainMenuKey.get(i));
            for (int j = 0; j < alKeySet.size(); j++) {
                hm = new HashMap<>(Objects.requireNonNull(lhm.get(alMainMenuKey.get(i))));
                ArrayList<String> alTmp = new ArrayList<>(hm.get(alKeySet.get(j)));
                for (int k = 0; k < alTmp.size(); k++) {
                    //ArrayList<String> alProducts = (ArrayList<String>) Arrays.asList(alTmp.get(k).split(","));
                    ArrayList<String> alProducts = new ArrayList<>(Arrays.asList(alTmp.get(k).split("##")));
                    dbh.addDataToIndividualProducts(new DataBaseHelper(mainID, alMainMenuKey.get(i), alKeySet.get(j), alProducts.get(0), alProducts.get(2), alProducts.get(1), ""));

                                    *//*if (DataReceiverService.refOfService != null){
                                        String sData = String.valueOf(dbh.lastID()) + "##" + alProducts.get(1);
                                        DataReceiverService.refOfService.dataStorage.alDBIDWithAddress.add(sData);
                                    }*//*
                    int id = dbh.getRecordsCount();
                    if (DataReceiverService.refOfService != null) {

                        String sData = String.valueOf(id) + "##" + alProducts.get(1);
                        String[] sSplitData = sData.split("##");
                        ArrayList<String> alMultipleUrl = new ArrayList<>(Arrays.asList(sSplitData[1].split(",")));
                        if (alMultipleUrl.size() > 1) {
                            for (int l = 0; l < alMultipleUrl.size(); l++) {
                                String sMultiple = String.valueOf(id) + "##" + alMultipleUrl.get(l);
                                DataReceiverService.refOfService.dataStorage.alDBIDWithAddress.add(sMultiple);
                            }
                        } else
                            DataReceiverService.refOfService.dataStorage.alDBIDWithAddress.add(sData);
                    }
                    //DataReceiverService.refOfService.dataStorage.hmImageAddressWithDBID.put(dbh.lastID(), alProducts.get(1));
                    else
                        hmImageAddressWithDBID.put(dbh.lastID(), alProducts.get(1));
                }
            }
        }
        ArrayList<DataBaseHelper> dbData = new ArrayList<>(dbh.getAllProductsData());
        sendMsgToActivity();
    } catch (JSONException e) {
        e.printStackTrace();
        ERROR_CODE = 901;
        msg = "Unable to reach server, please try again";
        sendMsgToActivity();
    }*/
}
