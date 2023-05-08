package com.example.delahuertaalamesa.register;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.delahuertaalamesa.MainActivity;
import com.example.delahuertaalamesa.R;
import com.example.delahuertaalamesa.admin.AdminActivity;
import com.example.delahuertaalamesa.databinding.ActivityLoginBinding;
import com.example.delahuertaalamesa.sortViewsProducts.SortViewsProducts;
import com.example.delahuertaalamesa.tools.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private ActivityLoginBinding binding;
    public static int intent_id_user;
    private String name_user;
    public static boolean login = false;
    private EditText et_nameUser, et_passwordUser;
    private Button btn_login, btn_register, btn_closedSession;
    private TextView tv_textHeadRegister, tv_textBodyRegister, tv_startLogin;
    private RequestQueue requestQueue;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_register);
        // binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // hide buttons
        // View decorView = getWindow().getDecorView();
        // int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        // decorView.setSystemUiVisibility(uiOptions);

        try {
            // load components
            loadMenuToolBar();

            // declare the menu buttons for onClick() and buttons layout
            binding.imgSortReturn.setOnClickListener(this);
            binding.imgSortFuits.setOnClickListener(this);
            binding.imgSortVegetables.setOnClickListener(this);
            binding.imgSortFavorites.setOnClickListener(this);

            btn_login = findViewById(R.id.btn_login);
            btn_closedSession = findViewById(R.id.btn_closedSession);
            btn_register = findViewById(R.id.btn_register);

            et_nameUser = findViewById(R.id.et_nameUser);
            et_passwordUser = findViewById(R.id.et_passwordUser);

            tv_textHeadRegister = findViewById(R.id.tv_textHeadRegister);
            tv_textBodyRegister = findViewById(R.id.tv_textBodyRegister);
            tv_startLogin = findViewById(R.id.tv_startLogin);

            // change the layout if the user is logged in
            if (login) {
                et_nameUser.setVisibility(View.INVISIBLE);
                et_passwordUser.setVisibility(View.INVISIBLE);
                btn_login.setVisibility(View.INVISIBLE);
                btn_register.setVisibility(View.INVISIBLE);
                tv_textHeadRegister.setVisibility(View.INVISIBLE);
                tv_textBodyRegister.setVisibility(View.INVISIBLE);

                tv_startLogin.setText("Sesión iniciada");
                tv_startLogin.setVisibility(View.VISIBLE);
                btn_closedSession.setVisibility(View.VISIBLE);
            } else {
                et_nameUser.setVisibility(View.VISIBLE);
                et_passwordUser.setVisibility(View.VISIBLE);
                btn_login.setVisibility(View.VISIBLE);
                btn_register.setVisibility(View.VISIBLE);
                tv_textHeadRegister.setVisibility(View.VISIBLE);
                tv_textBodyRegister.setVisibility(View.VISIBLE);
                tv_startLogin.setVisibility(View.INVISIBLE);
                btn_closedSession.setVisibility(View.INVISIBLE);
            }

            // login button action
            btn_login.setOnClickListener(view -> {
                getUser();
                login();
            });

            // register button action
            btn_register.setOnClickListener(view -> {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            });

            // closedSession button action
            btn_closedSession.setOnClickListener(v -> {
                if (login) {
                    login = false;

                    if (toast != null) toast.cancel();
                    toast = Toast.makeText(Login.this, "Sesión cerrada", Toast.LENGTH_SHORT);
                    toast.show();

                    finish();
                    startActivity(getIntent());

                } else {
                    if (toast != null) toast.cancel();
                    toast = Toast.makeText(Login.this, "Aún no has iniciado sesión", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        } catch (Exception e) {
            Log.d("canalERROR", "Se ha producido una excepción genérica");
            Log.d("canalERROR", Util.PrintEx(e));
        }
    }

    /**
     * Method in charge of user login,
     * checks that the fields are filled in,
     * and that there is a record with the same name,
     *
     * !IMPORTANT also checks if the login is the administrator,
     * if it matches, it is redirected to the AdminActivity view.
     */
    private void login() {
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, "https://granped.es/huertamesa/users/login.php",
                response -> {
                    if (response.equals("ERROR 1")) {
                        if (toast != null) toast.cancel();
                        toast = Toast.makeText(Login.this, "Se deben de llenar todos los campos", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (response.equals("ERROR 2")) {
                        if (toast != null) toast.cancel();
                        toast = Toast.makeText(Login.this, "No existe ese registro", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        if (toast != null) toast.cancel();
                        toast = Toast.makeText(Login.this, "Inicio de Sesion exitoso", Toast.LENGTH_SHORT);
                        toast.show();

                        login = true;

                        Intent intent;
                        if (name_user.equalsIgnoreCase("danie")){
                            intent = new Intent(this, AdminActivity.class);
                        }else {
                            intent = new Intent(this, MainActivity.class);
                            intent.putExtra("channel", "login");
                        }
                        startActivity(intent);

                    }
                }, error -> {
            if (toast != null) toast.cancel();
            toast = Toast.makeText(Login.this, "Error de conexión", Toast.LENGTH_SHORT);
            toast.show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new Hashtable<>();

                parametros.put("name_user", et_nameUser.getText().toString().trim());
                parametros.put("password_user", et_passwordUser.getText().toString().trim());

                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(stringRequest);
    }

    /**
     * Performs the query to the web service
     */
    private void getUser() {
        name_user = et_nameUser.getText().toString().trim();

        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = getIdUser(name_user);
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Load menuToolBar
     */
    private void loadMenuToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//hide title to menuToolbar
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
            case R.id.img_sortReturn: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            }
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
                    Intent intent = new Intent(Login.this, SortViewsProducts.class);
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
     * Performs the query to the web service,
     * get the id_user, passing the name_user
     */
    private JsonObjectRequest getIdUser(String name_user) {
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://granped.es/huertamesa/users/UserNameALL.php?name_user=" + name_user,
                null,
                response -> {
                    if (response != null) {
                        response.toString();
                        try {
                            intent_id_user = response.getInt("id_user");
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
        return jsArrayRequest;
    }
}