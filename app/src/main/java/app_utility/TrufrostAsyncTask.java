package app_utility;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static app_utility.StaticReferenceClass.DB_NAME;
import static app_utility.StaticReferenceClass.NETWORK_ERROR_CODE;
import static app_utility.StaticReferenceClass.PASSWORD;
import static app_utility.StaticReferenceClass.PORT_NO;
import static app_utility.StaticReferenceClass.SERVER_URL;
import static app_utility.StaticReferenceClass.USER_ID;

public class TrufrostAsyncTask extends AsyncTask<String, Void, String> {

    private LinkedHashMap<String, ArrayList<String>> lhmProductsWithID = new LinkedHashMap<>();
    private CircularProgressBar circularProgressBar;
    //private Activity aActivity;
    //private OnAsyncTaskInterface onAsyncTaskInterface;
    private ArrayList<Integer> alPosition = new ArrayList<>();
    private Context context;
    private HashMap<String, Object> hmDataList = new HashMap<>();
    private int nOrderID = 191;
    private int odooID = StaticReferenceClass.DEFAULT_ODOO_ID;

    private int nTemporaryDBID;
    private ArrayList<String> alEmpID = new ArrayList<>();
    private ArrayList<String> alAmount = new ArrayList<>();
    private ArrayList<String> alTime = new ArrayList<>();
    //private ArrayList<String> alEmpID = new ArrayList<>();

    ArrayList<DataBaseHelper> alDBTemporaryData;
    DatabaseHandler db;

    public TrufrostAsyncTask(Context context) {
        this.context = context;
    }

    public TrufrostAsyncTask(Context context, ArrayList<DataBaseHelper> alDBTemporaryData, DatabaseHandler db) {
        this.context = context;
        this.alDBTemporaryData = alDBTemporaryData;
        this.db = db;
    }
    /*public BELAsyncTask(Activity aActivity, OnAsyncTaskInterface onAsyncTaskInterface,
                        HashMap<String, Object> hmDataList) {
        this.aActivity = aActivity;
        this.hmDataList = hmDataList;
        this.onAsyncTaskInterface = onAsyncTaskInterface;
    }*/

    private boolean isPresent = false;
    private Boolean isConnected = false;
    private int ERROR_CODE = 0;
    private String sMsgResult;
    private int type;
    private String sStallName;
    private int[] IDS = new int[2];
    String sEmpID;
    String sAmount;
    String sTime;
    String sScannedID;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //setProgressBar();
    }

    @Override
    protected String doInBackground(String... params) {
        type = Integer.parseInt(params[0]);
        switch (type) {
            case 1:
                loginTask();
                break;
            case 2:
                validateLogin();
                //getData();
                //updateTask();
                break;
            case 3:
                placeOrder();
                break;
            case 4:
                readProductAndImageTask();
                //snapRoadTask(params[1]);
                //readProducts();
                break;
            case 5:
                sStallName = params[1];
                validateLogin();
                break;
            case 6:
                break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (ERROR_CODE != 0) {
            switch (ERROR_CODE) {
                case NETWORK_ERROR_CODE:
                    unableToConnectServer(ERROR_CODE);
                    break;
            }
            ERROR_CODE = 0;
            return;
        }
        switch (type) {
            case 2:
                //AdminRegisterService.onAsyncInterfaceListener.onAsyncComplete("ODOO_ID_RETRIEVED", odooID, "");
                break;
            case 4:
                //onAsyncTaskInterface.onAsyncTaskComplete("READ_PRODUCTS", type, lhmProductsWithID, alPosition);
                break;
            case 5:
                break;
            case 6:
                break;

        }
        /*if (circularProgressBar != null && circularProgressBar.isShowing()) {
            circularProgressBar.dismiss();
        }*/
    }

    private void loginTask() {
        //if (isConnected) {
        try {
            isConnected = OdooConnect.testConnection(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            if (isConnected) {
                isConnected = true;
                //return true;
            } else {
                isConnected = false;
                sMsgResult = "Connection error";
            }
        } catch (Exception ex) {
            ERROR_CODE = NETWORK_ERROR_CODE;
            // Any other exception
            sMsgResult = "Error: " + ex;
        }
        // }
        //return isConnected;
    }

    private void validateLogin() {
        //240
        try {
            OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            /*Object[] conditions = new Object[104];
            //conditions[0] = new Object[]{"id", "!=", "0099009"};
            conditions[0] = new Object[]{"blood_bags_id", "!=", null};
            conditions[1] = new Object[]{"ambient_id", "!=", null};
            conditions[2] = new Object[]{"baking_plate_revolve_id", "!=", null};
            conditions[3] = new Object[]{"boiler_capacity_id", "!=", null};
            conditions[4] = new Object[]{"bottle_storage_id", "!=", null};
            conditions[5] = new Object[]{"bowl_capacity_id", "!=", null};
            conditions[6] = new Object[]{"bowl_speed_id", "!=", null};
            conditions[7] = new Object[]{"cabinet_case_id", "!=", null};
            conditions[8] = new Object[]{"capacity_in_cft_id", "!=", null};
            conditions[9] = new Object[]{"capacity_in_gallons_id", "!=", null};
            conditions[10] = new Object[]{"capacity_in_litre_id", "!=", null};
            conditions[11] = new Object[]{"cavity_dimension_id", "!=", null};
            conditions[12] = new Object[]{"cavity_voulme_id", "!=", null};
            conditions[13] = new Object[]{"compatible_storage_bin_id", "!=", null};
            conditions[14] = new Object[]{"consecutive_dispensing_id", "!=", null};
            conditions[15] = new Object[]{"cooking_surface_id", "!=", null};
            conditions[16] = new Object[]{"cooking_time_id", "!=", null};
            conditions[17] = new Object[]{"cooling_system_id", "!=", null};
            conditions[18] = new Object[]{"cube_shape_id", "!=", null};
            conditions[19] = new Object[]{"current_id", "!=", null};
            conditions[20] = new Object[]{"defrost_id", "!=", null};
            conditions[21] = new Object[]{"digital_temp_indicator_id", "!=", null};
            conditions[22] = new Object[]{"dimensions_id", "!=", null};
            conditions[23] = new Object[]{"door_handle_id", "!=", null};
            conditions[24] = new Object[]{"drive_motor_id", "!=", null};
            conditions[25] = new Object[]{"electricals_id", "!=", null};
            conditions[26] = new Object[]{"external_dimension_id", "!=", null};
            conditions[27] = new Object[]{"fans_id", "!=", null};
            conditions[28] = new Object[]{"first_dispensing_id", "!=", null};
            conditions[29] = new Object[]{"flavours_id", "!=", null};
            conditions[30] = new Object[]{"freezing_cylinder_id", "!=", null};
            conditions[31] = new Object[]{"freezing_cylinder_capacity_id", "!=", null};
            conditions[32] = new Object[]{"fry_basket_id", "!=", null};
            conditions[33] = new Object[]{"frypot_oil_id", "!=", null};
            conditions[34] = new Object[]{"gas_input_id", "!=", null};
            conditions[35] = new Object[]{"glass_door_id", "!=", null};
            conditions[36] = new Object[]{"gn_en_id", "!=", null};
            conditions[37] = new Object[]{"griddle_dimension_id", "!=", null};
            conditions[38] = new Object[]{"grinding_burrs_id", "!=", null};
            conditions[39] = new Object[]{"gross_volumels_id", "!=", null};
            conditions[40] = new Object[]{"heat_load_id", "!=", null};
            conditions[41] = new Object[]{"hopper_capacity_id", "!=", null};
            conditions[42] = new Object[]{"ice_storage_id", "!=", null};
            conditions[43] = new Object[]{"input_power_id", "!=", null};
            conditions[44] = new Object[]{"interior_dimensions_id", "!=", null};
            conditions[45] = new Object[]{"interior_light_id", "!=", null};
            conditions[46] = new Object[]{"jar_capacity_id", "!=", null};
            conditions[47] = new Object[]{"lighting_under_id", "!=", null};
            conditions[48] = new Object[]{"loading_temp_id", "!=", null};
            conditions[49] = new Object[]{"lock_id", "!=", null};
            conditions[50] = new Object[]{"max_kneading_id", "!=", null};
            conditions[51] = new Object[]{"max_loading_id", "!=", null};
            conditions[52] = new Object[]{"max_production_id", "!=", null};
            conditions[53] = new Object[]{"max_room_area_id", "!=", null};
            conditions[54] = new Object[]{"max_room_volume_id", "!=", null};
            conditions[55] = new Object[]{"mix_hopper_id", "!=", null};
            conditions[56] = new Object[]{"mixing_speed_id", "!=", null};
            conditions[57] = new Object[]{"motor_power_id", "!=", null};
            conditions[58] = new Object[]{"net_weight_id", "!=", null};
            conditions[59] = new Object[]{"no_of_baskets_id", "!=", null};
            conditions[60] = new Object[]{"no_of_beech_id", "!=", null};
            conditions[61] = new Object[]{"no_of_compressor_id", "!=", null};
            conditions[62] = new Object[]{"no_of_doors_id", "!=", null};
            conditions[63] = new Object[]{"no_of_layers_id", "!=", null};
            conditions[64] = new Object[]{"no_of_lids_id", "!=", null};
            conditions[65] = new Object[]{"no_of_shelves_id", "!=", null};
            conditions[66] = new Object[]{"power_id", "!=", null};
            conditions[67] = new Object[]{"power_supply_id", "!=", null};
            conditions[68] = new Object[]{"power_consumption_id", "!=", null};
            conditions[69] = new Object[]{"product_dimension_id", "!=", null};
            conditions[70] = new Object[]{"product_weight_id", "!=", null};
            conditions[71] = new Object[]{"rated_capacity_id", "!=", null};
            conditions[72] = new Object[]{"rated_input_ower_id", "!=", null};
            conditions[73] = new Object[]{"refrigerant_id", "!=", null};
            conditions[74] = new Object[]{"remarks_id", "!=", null};
            conditions[75] = new Object[]{"stabilizer_id ", "!=", null};
            conditions[76] = new Object[]{"temp_display_id", "!=", null};
            conditions[77] = new Object[]{"temp_range_id", "!=", null};
            conditions[78] = new Object[]{"temperature_range_id", "!=", null};
            conditions[79] = new Object[]{"time_control_id", "!=", null};
            conditions[80] = new Object[]{"volts_id", "!=", null};
            conditions[81] = new Object[]{"volume_id", "!=", null};
            conditions[82] = new Object[]{"wdh_inchs_id", "!=", null};
            conditions[83] = new Object[]{"wdh_mm_id", "!=", null};
            conditions[84] = new Object[]{"wheels_castors_id", "!=", null};
            conditions[85] = new Object[]{"output_id", "!=", null};
            conditions[86] = new Object[]{"quantity_id", "!=", null};
            conditions[87] = new Object[]{"ventilations_id", "!=", null};
            conditions[88] = new Object[]{"temp_con_panel_id", "!=", null};
            conditions[89] = new Object[]{"ref_cap_id ", "!=", null};
            conditions[90] = new Object[]{"defrosting_id", "!=", null};
            conditions[91] = new Object[]{"front_glass_id", "!=", null};
            conditions[92] = new Object[]{"doors_id", "!=", null};
            conditions[93] = new Object[]{"compressor_id", "!=", null};
            conditions[94] = new Object[]{"ref_freezer_id", "!=", null};
            conditions[95] = new Object[]{"certificate_id", "!=", null};
            conditions[96] = new Object[]{"temp_control_id", "!=", null};
            conditions[97] = new Object[]{"temp_con_pos_num_id", "!=", null};
            conditions[98] = new Object[]{"ice_maker_cham_id", "!=", null};
            conditions[99] = new Object[]{"adjustable_leg_id", "!=", null};
            conditions[100] = new Object[]{"handle_id", "!=", null};
            conditions[101] = new Object[]{"reversible_door_id", "!=", null};
            conditions[102] = new Object[]{"colour_id", "!=", null};
            conditions[103] = new Object[]{"id", "=", 371};*/

            Integer[] ids = new Integer[3];
            ids[0] = 371;
            ids[1] = 304;
            ids[2] = 303;

            //ids
            Object[] conditions1 = new Object[1];
            //conditions[0] = new Object[]{"id", "!=", "0099009"};
            conditions1[0] = new Object[]{"id", "!=", 0};

            //conditions1[1] = new Object[]{"id", "=", 304};

            String[] fields = new String[103];
            fields[0] = "blood_bags_id";
            fields[1] = "ambient_id";
            fields[2] = "baking_plate_revolve_id";
            fields[3] = "boiler_capacity_id";
            fields[4] = "bottle_storage_id";
            fields[5] = "bowl_capacity_id";
            fields[6] = "bowl_speed_id";
            fields[7] = "cabinet_case_id";
            fields[8] = "capacity_in_cft_id";
            fields[9] = "capacity_in_gallons_id";
            fields[10] = "capacity_in_litre_id";
            fields[11] = "cavity_dimension_id";
            fields[12] = "cavity_voulme_id";
            fields[13] = "compatible_storage_bin_id";
            fields[14] = "consecutive_dispensing_id";
            fields[15] = "cooking_surface_id";
            fields[16] = "cooking_time_id";
            fields[17] = "cooling_system_id";
            fields[18] = "cube_shape_id";
            fields[19] = "current_id";
            fields[20] = "defrost_id";
            fields[21] = "digital_temp_indicator_id";
            fields[22] = "dimensions_id";
            fields[23] = "door_handle_id";
            fields[24] = "drive_motor_id";
            fields[25] = "electricals_id";
            fields[26] = "external_dimension_id";
            fields[27] = "fans_id";
            fields[28] = "first_dispensing_id";
            fields[29] = "flavours_id";
            fields[30] = "freezing_cylinder_id";
            fields[31] = "freezing_cylinder_capacity_id";
            fields[32] = "fry_basket_id";
            fields[33] = "frypot_oil_id";
            fields[34] = "gas_input_id";
            fields[35] = "glass_door_id";
            fields[36] = "gn_en_id";
            fields[37] = "griddle_dimension_id";
            fields[38] = "grinding_burrs_id";
            fields[39] = "gross_volumels_id";

            fields[40] = "heat_load_id";
            fields[41] = "hopper_capacity_id";
            fields[42] = "ice_storage_id";
            fields[43] = "input_power_id";
            fields[44] = "interior_dimensions_id";
            fields[45] = "interior_light_id";
            fields[46] = "jar_capacity_id";
            fields[47] = "lighting_under_id";
            fields[48] = "loading_temp_id";
            fields[49] = "lock_id";
            fields[50] = "max_kneading_id";
            fields[51] = "max_loading_id";
            fields[52] = "max_production_id";
            fields[53] = "max_room_area_id";
            fields[54] = "max_room_volume_id";
            fields[55] = "mix_hopper_id";
            fields[56] = "mixing_speed_id";
            fields[57] = "motor_power_id";
            fields[58] = "net_weight_id";
            fields[59] = "no_of_baskets_id";
            fields[60] = "no_of_beech_id";
            fields[61] = "no_of_compressor_id";
            fields[62] = "no_of_doors_id";
            fields[63] = "no_of_layers_id";
            fields[64] = "no_of_lids_id";
            fields[65] = "no_of_shelves_id";
            fields[66] = "power_id";
            fields[67] = "power_supply_id";
            fields[68] = "power_consumption_id";
            fields[69] = "product_dimension_id";
            fields[70] = "product_weight_id";
            fields[71] = "rated_capacity_id";
            fields[72] = "rated_input_ower_id";
            fields[73] = "refrigerant_id";
            fields[74] = "remarks_id";
            fields[75] = "stabilizer_id";
            fields[76] = "temp_display_id";
            fields[77] = "temp_range_id";
            fields[78] = "temperature_range_id";
            fields[79] = "time_control_id";
            fields[80] = "volts_id";
            fields[81] = "volume_id";
            fields[82] = "wdh_inchs_id";
            fields[83] = "wdh_mm_id";
            fields[84] = "wheels_castors_id";
            fields[85] = "output_id";
            fields[86] = "quantity_id";
            fields[87] = "ventilations_id";
            fields[88] = "temp_con_panel_id";
            fields[89] = "ref_cap_id";
            fields[90] = "defrosting_id";
            fields[91] = "front_glass_id";
            fields[92] = "doors_id";
            fields[93] = "compressor_id";
            fields[94] = "ref_freezer_id";
            fields[95] = "certificate_id";
            fields[96] = "temp_control_id";

            fields[97] = "temp_con_pos_num_id";
            fields[98] = "ice_maker_cham_id";
            fields[99] = "adjustable_leg_id";
            fields[100] = "handle_id";
            fields[101] = "reversible_door_id";
            fields[102] = "colour_id";


            //conditions1[1] = new Object[]{"bowl_capacity_id", "!=", 0};

            //"technical.specification"

            //List<HashMap<String, Object>> stallData = oc.search_read("product.template", new Object[]{conditions1}, "name", "tech_spec_name");
            //List<HashMap<String, Object>> stallData = oc.search_read("product.template", new Object[]{conditions1}, "id", "bowl_capacity_id", "ambient_id");
            List<HashMap<String, Object>> stallData = oc.search_read("product.template", new Object[]{conditions1}, fields);

            //getData();
            /*if (stallData.size() == 1) {
                isPresent = true;
                odooID = Integer.valueOf(stallData.get(0).get("id").toString());
            }*/
            /*for (int i = 0; i < stallData.size(); ++i) {
                //int id = Integer.valueOf(data.get(i).get("id").toString());
                String sName = String.valueOf(stallData.get(i).get("name").toString());

                *//*if(sStallName.equals(sName)){
                    isPresent = true;

                    return;
                }*//*
                if (sStallName.equals(sName)) {
                    isPresent = true;
                    odooID = Integer.valueOf(stallData.get(i).get("id").toString());
                    return;
                }
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getData() {
        //240
        try {
            OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            Object[] conditions1 = new Object[1];
            //conditions[0] = new Object[]{"id", "!=", "0099009"};
            conditions1[0] = new Object[]{"id", "=", 371};
            List<HashMap<String, Object>> stallData = oc.search_read("technical.specification", new Object[]{conditions1}, "id","name", "tech_spec_name");
            //List<HashMap<String, Object>> data = oc.search_read("technical.specification", new Object[]{conditions1}, "name");
            if (stallData.size() == 1) {
                isPresent = true;
                odooID = Integer.valueOf(stallData.get(0).get("id").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void createOrder() {
        //240
        try {
            OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            Integer createCustomer = oc.create("sale.order", new HashMap() {{
                put("partner_id", 562);
                //put("state", ORDER_STATE[0]);
            }});
            IDS[0] = createCustomer;
            //createOne2Many(createCustomer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    private void placeOrder() {
        //240
        try {
            OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
            Boolean idC = oc.write("sale.order", new Object[]{nOrderID}, hmDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readImageTask() {
        OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
        Object[] conditions = new Object[2];
        conditions[0] = new Object[]{"res_model", "=", "product.template"};
        conditions[1] = new Object[]{"res_field", "=", "image_medium"};
        List<HashMap<String, Object>> data = oc.search_read("ir.attachment", new Object[]{conditions}, "id", "store_fname", "res_name");

        for (int i = 0; i < data.size(); ++i) {

        }
    }

    /*private void readProducts() {
        OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
        List<HashMap<String, Object>> data = oc.search_read("product.template", new Object[]{
                new Object[]{new Object[]{"type", "=", "product"}}}, "id", "name", "list_price");

        for (int i = 0; i < data.size(); ++i) {
            //int id = Integer.valueOf(data.get(i).get("id").toString());
            String sName = String.valueOf(data.get(i).get("name").toString());
            //String sUnitPrice = String.valueOf(data.get(i).get("list_price").toString());
            ArrayList<String> alData = new ArrayList<>();
            alData.add(data.get(i).get("id").toString());
            //alData.add(data.get(i).get("name").toString());
            alData.add(data.get(i).get("list_price").toString());
            lhmProductsWithID.put(sName, alData);
        }
    }*/
    private void readProductAndImageTask() {
        OdooConnect oc = OdooConnect.connect(SERVER_URL, PORT_NO, DB_NAME, USER_ID, PASSWORD);
        List<HashMap<String, Object>> productsData = oc.search_read("product.template", new Object[]{
                new Object[]{new Object[]{"type", "=", "product"}}}, "id", "name", "list_price");

        Object[] conditions = new Object[2];
        conditions[0] = new Object[]{"res_model", "=", "product.template"};
        conditions[1] = new Object[]{"res_field", "=", "image_medium"};
        List<HashMap<String, Object>> imageData = oc.search_read("ir.attachment", new Object[]{conditions},
                "id", "store_fname", "res_name");

        for (int i = 0; i < productsData.size(); ++i) {
            //int id = Integer.valueOf(data.get(i).get("id").toString());
            String sName = String.valueOf(productsData.get(i).get("name").toString());
            //String sUnitPrice = String.valueOf(data.get(i).get("list_price").toString());
            ArrayList<String> alData = new ArrayList<>();
            alData.add(productsData.get(i).get("id").toString());
            //alData.add(data.get(i).get("name").toString());
            alData.add(productsData.get(i).get("list_price").toString());
            for (int j = 0; j < imageData.size(); j++) {
                String base64 = imageData.get(j).get("store_fname").toString();
                if (imageData.get(j).get("res_name").toString().equals(sName)) {
                    alData.add(base64);
                    alPosition.add(i);
                    break;
                }
            }
            lhmProductsWithID.put(sName, alData);
        }
    }

    private void unableToConnectServer(int errorCode) {
        //OfflineTransferService.onAsyncInterfaceListener.onAsyncComplete("SERVER_ERROR", errorCode, "");
    }

    private void setProgressBar() {
        circularProgressBar = new CircularProgressBar(context);
        circularProgressBar.setCanceledOnTouchOutside(false);
        circularProgressBar.setCancelable(false);
        circularProgressBar.show();
    }

}
