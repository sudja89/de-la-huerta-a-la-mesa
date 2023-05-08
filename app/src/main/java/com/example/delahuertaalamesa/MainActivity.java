package com.example.delahuertaalamesa;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.delahuertaalamesa.databinding.ActivityMainBinding;
import com.example.delahuertaalamesa.propertiesproducts.PropertiesProducts;
import com.example.delahuertaalamesa.recyclerviewMainActivity.ListAdapterMainActivity;
import com.example.delahuertaalamesa.recyclerviewMainActivity.ListProductsMainActivity;
import com.example.delahuertaalamesa.register.Login;
import com.example.delahuertaalamesa.sortViewsProducts.SortViewsProducts;
import com.example.delahuertaalamesa.tools.ItemClickSupport;
import com.example.delahuertaalamesa.tools.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/*
 * FALTA => limpar codigo, organizar, traducir
 *       => administrador movil o web?¿
 *       => adaptar a diferentes tamaños
 */


/**
 * De la huerta a la mesa
 * diseño de la app: tema, ocultar botones, pantalla completa
 * <p>
 * facilidad de trabajo: blinding, acceso a internet y glide o picasso
 * <p>
 * ScreenLoad: se trata de la primera pantalla que aparece creando una animacion y da paso al main mediante un intent
 * comprueba si hay internet si no es asi reconecta hasta que haya internet
 * <p>
 * MainActivity:
 * recylerView contiene los *productos del mes,
 * menu toolBar contiene contactar y registrar,
 * menu botones con el calendario, frutas, verduras y favoritos,
 * el spinner funciona cambia los productos del mes seleccionado por defecto mes actual
 * <p>
 * PropertiesProducts: al hacer click pasar el id del producto y sacar sus propiedades *producto por id
 * el calendario cambia de color segun los meses que son del producto
 * <p>
 * SortViewProducts: al hacer click pasa un string clasificando los *productos por frutas verduras y favoritos(para acceder hay que hacer login)
 * <p>
 * Login: te permite crear o acceder/desconectar
 * admin - ¿?
 * users - pueden añadir a favoritos, aparece la estrella en propiedades
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityMainBinding binding;
    private List<ListProductsMainActivity> productsMonth;
    private ListAdapterMainActivity listAdapterMainActivity;
    private RecyclerView recyclerView;
    private int indexmonth;
    private RequestQueue requestQueue;
    private String channel;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // hide buttons
        // View decorView = getWindow().getDecorView();
        // int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        // decorView.setSystemUiVisibility(uiOptions);

        try {
            // load components
            getMonth_LoadSpinner();
            loadMenuToolBar();

            // declare the menu buttons for onClick()
            binding.imgSortFuits.setOnClickListener(this);
            binding.imgSortVegetables.setOnClickListener(this);
            binding.imgSortFavorites.setOnClickListener(this);

            // gets the selected cardView from the recyclerView and sends the id_product to PropertiesProducts
            RecyclerView context = findViewById(R.id.recyclerView);
            ItemClickSupport.addTo(context).setOnItemClickListener((recyclerView, position, v) -> {

                int id = productsMonth.get(position).getId_product();
                Intent intent = new Intent(MainActivity.this, PropertiesProducts.class);
                intent.putExtra("id", id);
                startActivity(intent);

            });

            // receive the string with the classification fruits/vegetables/favorites
            Bundle extras = getIntent().getExtras();
            channel = extras.getString("channel");
            //if received from login shows alertGifFavorite
            if (channel.equalsIgnoreCase("login")) alertGifFavorite();

        } catch (Exception e) {
            Log.d("canalERROR", "Se ha producido una excepción genérica");
            Log.d("canalERROR", Util.PrintEx(e));
        }
    }

    /**
     * Starts arrayList and queries the products of the selected month in the spinner,
     * to load the reciblerView,
     * by default it shows the products of the current month
     */
    private void loadProducts() {
        try {
            productsMonth = new ArrayList<>();

            loadRecycler();

            requestQueue = Volley.newRequestQueue(this);
            JsonArrayRequest jsonArrayRequest = getProductsMonthID(indexmonth);
            requestQueue.add(jsonArrayRequest);
        } catch (Exception e) {
            Log.d("canalERROR", "Se ha producido una excepción genérica");
            Log.d("canalERROR", Util.PrintEx(e));
        }
    }

    /**
     * Load recyclerView
     */
    private void loadRecycler() {
        try {
            listAdapterMainActivity = new ListAdapterMainActivity(productsMonth, this);
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(listAdapterMainActivity);
        } catch (Exception e) {
            Log.d("canalERROR", "Se ha producido una excepción genérica");
            Log.d("canalERROR", Util.PrintEx(e));
        }
    }

    /**
     * Load menuToolBar
     */
    private void loadMenuToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);// hide title to menuToolbar
    }

    /**
     * Options to menuToolBar
     *
     * @param menu The options menu in which you place your items.
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    /**
     * Selection of menuToolBar options
     * Handle item selection
     *
     * @param item The menu item that was selected.
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.register:
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                return true;
            case R.id.contact:
                acceptContact();
                return true;
                case R.id.web:
                acceptWeb();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Displays an AlertDialog to confirm or cancel,
     * going to WhatsApp channel after pressing contact
     */
    private void acceptContact() {
        AlertDialog alertDialog = new AlertDialog
                .Builder(this)
                .setPositiveButton("Sí, continuar", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://chat.whatsapp.com/K0CD4DshuaUJGKtSPOMyDD"));
                    startActivity(intent);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setTitle("Confirmar")
                .setMessage("¿Quieres ir al canal de WhatsApp?")
                .create();
        alertDialog.show();
    }
    /**
     * Displays an AlertDialog to confirm or cancel,
     * going to web "De la huerta a la mesa" after pressing web
     */
    private void acceptWeb() {
        AlertDialog alertDialog = new AlertDialog
                .Builder(this)
                .setPositiveButton("Sí, continuar", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://granped.es/webhuertamesa"));
                    startActivity(intent);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setTitle("Confirmar")
                .setMessage("¿Quieres ir a la web \n\"De la huerta a la mesa\"?")
                .create();
        alertDialog.show();
    }

    /**
     * Collects the options performed by the menuToolBar buttons
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        ImageView imageView = (ImageView) v;
        switch (imageView.getId()) {
            case R.id.img_sortFuits: {
                Intent intent = new Intent(this, SortViewsProducts.class);
                intent.putExtra("channel", "fruits");
                startActivity(intent);
                break;
            }
            case R.id.img_sortVegetables: {
                Intent intent = new Intent(this, SortViewsProducts.class);
                intent.putExtra("channel", "vegetables");
                startActivity(intent);
                break;
            }
            case R.id.img_sortFavorites: {
                if (Login.login) {
                    Intent intent = new Intent(this, SortViewsProducts.class);
                    intent.putExtra("channel", "favorites");
                    startActivity(intent);
                } else {
                    if (toast != null) toast.cancel();
                    toast = Toast.makeText(this, "Registro necesario", Toast.LENGTH_SHORT);
                    toast.show();
                }
                break;
            }
        }
    }

    /**
     * Load the spinner with the months of the year and select the current month,
     * passing the month loads the recyclerView products,
     * by default loads the current month
     *
     * @return
     */
    private void getMonth_LoadSpinner() {
        // monthsList is a list containing the names of the months
        List<String> monthsList = new ArrayList<>();
        String[] months = new DateFormatSymbols().getMonths();
        for (int i = 0; i < months.length; i++) {
            monthsList.add(months[i]);
        }
        // creation of adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthsList);

        // style
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);

        // attach adapter data to the spinner
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        String monthCurrent = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        // get the current month
        indexmonth = 0;
        for (int i = 0; i < monthsList.size(); i++) {
            // compares the current month with the texts displayed in the spinner
            if (monthCurrent.equals(String.valueOf(adapter.getItem(i)))) {
                // gets the index of the match
                indexmonth = i;
            }

        }
        // moves spinner to position
        spinner.setSelection(indexmonth);

        // *** puts the products of the selected month on the spinner
        // SPINNER WITH A NORMAL ONCLICK IS NOT VALID
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexmonth = spinner.getSelectedItemPosition() + 1;
                //calls the method to load the month's products in the recyclerView
                loadProducts();
            }

            // predefined method in case nothing is selected,
            // in our case we always have something loaded, 
            // it cannot be deleted.
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Make the query with the server,
     * get the months of the month by passing the id_month
     *
     * @param id_month
     * @return
     */
    private JsonArrayRequest getProductsMonthID(int id_month) {
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "https://granped.es/huertamesa/products/ProductsMonth.php?id_month=" + id_month,
                null,
                response -> {
                    if (response != null) {
                        response.toString();
                        try {
                            int length = response.length();
                            for (int i = 0; i < length; i++) {
                                JSONObject product = response.getJSONObject(i);

                                int id_product = Integer.parseInt(product.getString("id_product"));
                                String name_picture = product.getString("name_picture");
                                String name_product = product.getString("name_product");
                                String submit = product.getString("submit");
                                String properties = product.getString("properties");
                                String production = product.getString("production");
                                String curiosities = product.getString("curiosities");

                                ListProductsMainActivity element = new ListProductsMainActivity(
                                        id_product,
                                        name_picture,
                                        name_product,
                                        submit,
                                        properties,
                                        production,
                                        curiosities,
                                        getResources().getIdentifier(name_picture + "", "drawable", getApplicationContext().getPackageName())
                                );

                                productsMonth.add(element);
                                listAdapterMainActivity.notifyItemInserted(productsMonth.size());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> Log.d("canalError", "Error Respuesta en JSON: " + error.getMessage())
        ) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                int mStatusCode = response.statusCode;
                Log.d("VolleyResponseCode", String.valueOf(mStatusCode));
                return super.parseNetworkResponse(response);
            }
        };
        return jsArrayRequest;
    }

    /**
     * If you login show gifFavorites in alert form
     * Web View is a view of a web page is the solution I found to show the gif in the alert.
     * The only bad thing is that it shakes sometimes I don't know why.
     */
    private void alertGifFavorite() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Ahora puedes guardar\ntus productos favoritos");

        WebView wv = new WebView(this);
        wv.loadDataWithBaseURL("", "<img style= width:300px;  src='https://granped.es/huertamesa/pictures/gifFavorite.gif'/>", "text/html", "utf-8", null);

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        alert.setView(wv);
        alert.setNegativeButton("Aceptar", (dialog, id) -> dialog.dismiss());
        alert.show();
    }
}