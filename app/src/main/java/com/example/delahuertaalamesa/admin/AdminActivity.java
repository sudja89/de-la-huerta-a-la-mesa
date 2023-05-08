package com.example.delahuertaalamesa.admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.delahuertaalamesa.R;
import com.example.delahuertaalamesa.recyclerviewMainActivity.ListProductsMainActivity;
import com.example.delahuertaalamesa.tools.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private Button btn_search,btn_crud_create,btn_crud_update,btn_crud_delete;
    private EditText edt_name;
    private TextView tv_idProduct,tv_commonname,tv_submit,tv_properties,tv_production, tv_curiosities;
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btn_search = findViewById(R.id.btn_crud_read);
        btn_crud_create = findViewById(R.id.btn_crud_create);
        btn_crud_update = findViewById(R.id.btn_crud_update);
        btn_crud_delete = findViewById(R.id.btn_crud_delete);

        edt_name = findViewById(R.id.edt_name);
        tv_idProduct = findViewById(R.id.tv_idProduct);
        tv_commonname = findViewById(R.id.tv_commonname);
        tv_submit = findViewById(R.id.tv_submit);
        tv_properties = findViewById(R.id.tv_properties);
        tv_production = findViewById(R.id.tv_production);
        tv_curiosities = findViewById(R.id.tv_curiosities);

        btn_search.setOnClickListener(v -> {
            String name = edt_name.getText().toString().trim();
            requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = getProductName(name);
            requestQueue.add(jsonObjectRequest);
        });

        btn_crud_create.setOnClickListener(v -> {
            createProduct();
        });

        btn_crud_update.setOnClickListener(v -> {
            requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest;
            try {
                jsonObjectRequest = updateProduct(new ListProductsMainActivity(
                        Integer.parseInt(tv_idProduct.getText().toString().trim()),
                        tv_commonname.getText().toString().trim(),
                        tv_commonname.getText().toString().trim(),
                        tv_submit.getText().toString().trim(),
                        tv_properties.getText().toString().trim(),
                        tv_production.getText().toString().trim(),
                        tv_curiosities.getText().toString().trim(),
                        0));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            requestQueue.add(jsonObjectRequest);
        });

        btn_crud_delete.setOnClickListener(v -> {
            int id_product = Integer.parseInt(tv_idProduct.getText().toString().trim());
            requestQueue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = deleteProduct(id_product);
            requestQueue.add(jsonObjectRequest);
        });

    }

    /**
     * Performs the query to the web service,
     * the complete product by passing your id
     */
    private JsonObjectRequest getProductName(String name) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://granped.es/huertamesa/products/ProductsLogic.php?name_picture=" + name,
                null,
                response -> {
                    if (response != null) {
                        response.toString();
                        try {
                            String id_product = response.getString("id_product");
                            String name_picture = response.getString("name_picture");
                            String name_product = response.getString("name_product");
                            String submit = response.getString("submit");
                            String properties = response.getString("properties");
                            String production = response.getString("production");
                            String curiosities = response.getString("curiosities");

                            getProduct(id_product, name_product, submit, properties, production, curiosities);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> Log.d("canalError", "Error Respuesta en JSON: " + error.getMessage())
        ) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("VolleyResponseCode", String.valueOf(mStatusCode));
                return super.parseNetworkResponse(response);
            }
        };
        return jsonObjectRequest;
    }

    /**
     * Gets the product id and prints it in AdminActivity view
     */
    private void getProduct(String id_product, String name_product, String submit, String properties, String production, String curiosities) {
        TextView tvIdProduct = findViewById(R.id.tv_idProduct);
        TextView tvCommonName = findViewById(R.id.tv_commonname);
        TextView tvSubmit = findViewById(R.id.tv_submit);
        TextView tvProperties = findViewById(R.id.tv_properties);
        TextView tvProduction = findViewById(R.id.tv_production);
        TextView tvCuriosities = findViewById(R.id.tv_curiosities);

        try {
            tvIdProduct.setText(id_product);
            tvCommonName.setText(name_product);
            tvSubmit.setText(submit);
            tvProperties.setText(properties);
            tvProduction.setText(production);
            tvCuriosities.setText(curiosities);

        } catch (Exception e) {
            Log.d("canalERROR", "Se ha producido una excepción genérica");
            Log.d("canalERROR", Util.PrintEx(e));
        }
    }

    /**
     * Create a product
     */
    private void createProduct() {
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, "https://granped.es/huertamesa/products/ProductsLogic.php",
                response -> {
                    if (toast != null) toast.cancel();
                    toast = Toast.makeText(this, "Producto creado", Toast.LENGTH_SHORT);
                    toast.show();
                }, error -> {
            if (toast != null) toast.cancel();
            toast = Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT);
            toast.show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new Hashtable<>();

                parameters.put("id_product", tv_idProduct.getText().toString().trim());
                parameters.put("name_picture", tv_commonname.getText().toString().toLowerCase().trim());
                parameters.put("name_product", tv_commonname.getText().toString().trim());
                parameters.put("submit", tv_submit.getText().toString().trim());
                parameters.put("properties", tv_properties.getText().toString().trim());
                parameters.put("production", tv_production.getText().toString().trim());
                parameters.put("curiosities", tv_curiosities.getText().toString().trim());

                return parameters;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Update a product
     */
    private JsonObjectRequest updateProduct(ListProductsMainActivity element) throws JSONException {
        JSONObject object = new JSONObject();
        try {
            object.put("id_product", element.getId_product());
            object.put("name_picture", element.getCommon_name().toLowerCase().trim());
            object.put("name_product", element.getCommon_name());
            object.put("submit", element.getSubmit());
            object.put("properties", element.getProperties());
            object.put("production", element.getProduction());
            object.put("curiosities", element.getCuriosities());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                "https://granped.es/huertamesa/products/ProductsLogic.php?id_product=" + object.get("id_product"),
                object,
                response -> {
                    if (toast != null) toast.cancel();
                    toast = Toast.makeText(this, "Producto modificado", Toast.LENGTH_SHORT);
                    toast.show();
                },
                error -> {
                    if (toast != null) toast.cancel();
                    toast = Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT);
                    toast.show();
                }
        ) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("VolleyResponseCode", String.valueOf(mStatusCode));
                return super.parseNetworkResponse(response);
            }
        };
        return jsObjectRequest;
    }

    /**
     * Delete a product
     */
    private JsonObjectRequest deleteProduct(int id_product) {
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                "https://granped.es/huertamesa/products/ProductsLogic.php?id_product=" + id_product,
                null,
                response -> {
                    if (toast != null) toast.cancel();
                    toast = Toast.makeText(this, "Producto borrado", Toast.LENGTH_SHORT);
                    toast.show();
                },
                error -> {
                    if (toast != null) toast.cancel();
                    toast = Toast.makeText(this, "Producto borrado", Toast.LENGTH_SHORT);
                    toast.show();
                }
        ) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("VolleyResponseCode", String.valueOf(mStatusCode));
                return super.parseNetworkResponse(response);
            }
        };
        return jsArrayRequest;
    }
}