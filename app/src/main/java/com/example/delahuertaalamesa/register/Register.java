package com.example.delahuertaalamesa.register;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.delahuertaalamesa.R;
import com.example.delahuertaalamesa.tools.Util;

import java.util.Hashtable;
import java.util.Map;

public class Register extends AppCompatActivity {
    private EditText et_nameUser, et_passwordUser, et_name;
    private Button btn_registerRegister;
    private RequestQueue requestQueue;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // hide buttons
        // View decorView = getWindow().getDecorView();
        // int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        // decorView.setSystemUiVisibility(uiOptions);

        try {
            // declare layout elements
            et_nameUser = findViewById(R.id.et_nameUser);
            et_passwordUser = findViewById(R.id.et_passwordUser);
            et_name = findViewById(R.id.et_name);
            btn_registerRegister = findViewById(R.id.btn_registerRegister);

            // if btn_registerRegister is pressed calls registerUser() method
            btn_registerRegister.setOnClickListener(v -> registerUser());

        } catch (Exception e) {
            Log.d("canalERROR", "Se ha producido una excepción genérica");
            Log.d("canalERROR", Util.PrintEx(e));
        }
    }

    /**
     * Method in charge of registering users,
     * checks that the fields are filled in,
     * and that there is no record with the same name
     */
    private void registerUser() {
        StringRequest stringRequest;

        stringRequest = new StringRequest(Request.Method.POST,
                "https://granped.es/huertamesa/users/register.php",
                response -> {
                    // this section we program what we want to do if there are no errors
                    switch (response) {
                        case "ERROR 1":
                            if (toast != null) toast.cancel();
                            toast = Toast.makeText(Register.this, "Se deben de llenar todos los campos", Toast.LENGTH_SHORT);
                            toast.show();
                            break;
                        case "ERROR 2":
                            if (toast != null) toast.cancel();
                            toast = Toast.makeText(Register.this, "Usuario ya existe", Toast.LENGTH_SHORT);
                            toast.show();
                            et_nameUser.requestFocus();
                            break;
                        case "MENSAJE":
                            if (toast != null) toast.cancel();
                            toast = Toast.makeText(Register.this, "Registro exitoso", Toast.LENGTH_SHORT);
                            toast.show();

                            finish();// if the registration is accepted, end the Register activity and return to Login.
                            break;
                    }
                }, error -> {
            // if case of any error in the data collection
            if (toast != null) toast.cancel();
            toast = Toast.makeText(Register.this, "Error de conexión", Toast.LENGTH_SHORT);
            toast.show();
        }) {
            /**
             * This method the values of the application are sent to the server
             *
             * @return
             */
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameter = new Hashtable<>();

                parameter.put("name_user", et_nameUser.getText().toString().trim());
                parameter.put("password_user", et_passwordUser.getText().toString().trim());
                parameter.put("name", et_name.getText().toString().trim());

                return parameter;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}