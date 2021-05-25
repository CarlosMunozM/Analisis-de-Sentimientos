package com.example.analisissentimientos.Controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.analisissentimientos.Clases.Conexion;
import com.example.analisissentimientos.Clases.DatePickerFragment;
import com.example.analisissentimientos.R;
import com.example.analisissentimientos.WebServices.Asynchtask;
import com.example.analisissentimientos.WebServices.WebService;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class RegistrarJugador extends AppCompatActivity implements Asynchtask, View.OnClickListener, TextWatcher {

    EditText txtNombres, txtApellidos, txtTelefono, txtFechaNacimiento, txtUsuario, txtClave, txtRepClave;
    TextInputLayout txtlyClave,txtlyRepClave;
    RadioGroup rbtGenero;
    RadioButton radioButton, rbtMasculino;
    Boolean b_radiobutton = false, camposLlenos = false;
    String clave, repetirClave, error;

    Boolean claves = false;
    int pantalla = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_jugador);

        Bundle datos = this.getIntent().getExtras();
        pantalla = datos.getInt("pantalla");
        getSupportActionBar().setTitle("Register Player");

        txtNombres = (EditText) findViewById(R.id.txtRegJug_Nombres);
        txtApellidos = (EditText) findViewById(R.id.txtRegJug_Apellidos);
        txtTelefono = (EditText) findViewById(R.id.txtRegJug_Telefono);
        txtFechaNacimiento = (EditText) findViewById(R.id.txtRegJug_FechaNacimiento);
        txtUsuario = (EditText) findViewById(R.id.txtRegJug_Usuario);
        txtClave = (EditText) findViewById(R.id.txtRegJug_Clave);
        txtRepClave = (EditText) findViewById(R.id.txtRegJug_RepClave);
        rbtGenero = (RadioGroup) findViewById(R.id.rbtRegJug_Genero);
        rbtMasculino = (RadioButton) findViewById(R.id.rbtRegJug_Masculino);

        txtlyClave = (TextInputLayout) findViewById(R.id.txtlyClave);
        txtlyRepClave = (TextInputLayout) findViewById(R.id.txtlyRepClave);

        txtFechaNacimiento.setOnClickListener(this);
        txtClave.addTextChangedListener(this);
        txtRepClave.addTextChangedListener(this);
        txtFechaNacimiento.addTextChangedListener(this);

    }


    public void registrarJugador(View view)
    {
        clave = txtClave.getText().toString();
        repetirClave = txtRepClave.getText().toString();

        if(validarCamposVacios()) {

            if(validarClave())
            {
                ejecutarWS_buscarUsuario();
            }
            else
                Toast.makeText(this, "The passwords do not match", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
    }

    public void ejecutarWS_registrarJugador()
    {
        Map<String, String> datos = new HashMap<String, String>();

        datos.put("accion","registrarJugador");
        datos.put("nombres",txtNombres.getText().toString());
        datos.put("apellidos",txtApellidos.getText().toString());
        datos.put("telefono",txtTelefono.getText().toString());
        datos.put("fecha_nacimiento",txtFechaNacimiento.getText().toString());
        datos.put("usuario",txtUsuario.getText().toString());
        datos.put("clave",txtClave.getText().toString());
        datos.put("genero",obtenerGenero());

        WebService ws = new WebService("http://"+ Conexion.getServidor() +":8080/WS_AnalisisSentimientos/wsLogin"
                , datos, this, this);

        ws.execute("");
    }

    public void ejecutarWS_buscarUsuario()
    {
        Map<String, String> datos = new HashMap<String, String>();

        datos.put("accion","buscarUsuario");
        datos.put("usuario",txtUsuario.getText().toString());

        WebService ws = new WebService("http://"+ Conexion.getServidor() +":8080/WS_AnalisisSentimientos/wsLogin"
                , datos, this, this);

        ws.execute("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtRegJug_FechaNacimiento:
                showDatePickerDialog();
                break;
        }
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 porque Enero es 0
                final String selectedDate = year + "-" + (month+1) + "-" + day;
                txtFechaNacimiento.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public String obtenerGenero()
    {
        String genero = "";

        if ( !(rbtGenero.getCheckedRadioButtonId() == -1) )
        {
            b_radiobutton = true;
            int selectedId = rbtGenero.getCheckedRadioButtonId();
            radioButton = (RadioButton)findViewById(selectedId);
            genero = radioButton.getText().toString();

            if(genero.equals("Male"))
                genero = "M";
            else if(genero.equals("Female"))
                genero = "F";
        }
        else
            b_radiobutton = false;

        return genero;

    }


    @Override
    public void processFinish(String result) throws JSONException {
        if(result.equals("") || result.equals(null))
            Toast.makeText(this, "Connection error", Toast.LENGTH_SHORT).show();
        else {
            if (result.equals("correcto")) {
                Toast.makeText(getApplicationContext(), "Registered player", Toast.LENGTH_LONG).show();
                if(pantalla == 1)
                    startActivity(new Intent(getApplicationContext(), Jugadores.class));
                else
                    startActivity(new Intent(getApplicationContext(), Login.class));
            } else if (result.equals("error"))
                Toast.makeText(getApplicationContext(), "Error registering player", Toast.LENGTH_LONG).show();

            if (result.equals("usuarioOcupado")) {
                error = "This user is already busy";
                Toast.makeText(this, "User not available", Toast.LENGTH_SHORT).show();
                txtUsuario.setError(error);
            } else if (result.equals("usuarioLibre")) {
                txtUsuario.setError(null);
                txtUsuario.setCompoundDrawables(null, null, null, null);
                //Toast.makeText(this, "Jugador Registrado", Toast.LENGTH_SHORT).show();
                ejecutarWS_registrarJugador();
            }
        }
    }


    private Boolean validarClave() {
        Boolean b = false;
        String error = "The keys do not match";

        if(!txtClave.getText().toString().equals(txtRepClave.getText().toString())) {

            txtClave.setError(error);
            txtlyClave.setPasswordVisibilityToggleEnabled(false);

            txtRepClave.setError(error);
            txtlyRepClave.setPasswordVisibilityToggleEnabled(false);
        }
        else
        {
            txtClave.setError(null);
            txtlyClave.setPasswordVisibilityToggleEnabled(true);

            txtRepClave.setError(null);
            txtlyRepClave.setPasswordVisibilityToggleEnabled(true);
            b = true;
        }

        return b;
    }

    private Boolean validarCamposVacios() {

        Boolean a = false, b = false, c = false, d = false, e = false, f = false, g = false, h = false;
        error = "Fill this field";

        if(txtNombres.getText().toString().equals("")) {
            //Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
            //txtNombres.requestFocus();
            txtNombres.setError(error);
        }
        else {
            a = true;
            txtNombres.setError(null);
            txtNombres.setCompoundDrawables(null, null, null, null);
        }

        if(txtApellidos.getText().toString().equals("")) {
            //Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
            //txtApellidos.requestFocus();
            txtApellidos.setError(error);
        }
        else {
            b = true;
            txtApellidos.setError(null);
            txtApellidos.setCompoundDrawables(null, null, null, null);
        }

        if(txtTelefono.getText().toString().equals("")) {
            //Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
            //txtTelefono.requestFocus();
            txtTelefono.setError(error);
        }
        else{
            c = true;
            txtTelefono.setError(null);
            txtTelefono.setCompoundDrawables(null, null, null, null);
        }

        if(txtFechaNacimiento.getText().toString().equals("")) {
            //Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
            //txtFechaNacimiento.requestFocus();
            txtFechaNacimiento.setError(error);
        }
        else{
            d = true;
            txtFechaNacimiento.setError(null);
            txtFechaNacimiento.setCompoundDrawables(null, null, null, null);
        }

        if(txtUsuario.getText().toString().equals("")) {
            //Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
            //txtUsuario.requestFocus();
            txtUsuario.setError(error);
        }
        else{
            e = true;
            txtUsuario.setError(null);
            txtUsuario.setCompoundDrawables(null, null, null, null);
        }

        if(txtClave.getText().toString().equals("")) {
            //Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
            //txtClave.requestFocus();
            txtClave.setError(error);
            txtlyClave.setPasswordVisibilityToggleEnabled(false);
        }
        else{
            f = true;
            txtClave.setError(null);
            txtClave.setCompoundDrawables(null, null, null, null);
            txtlyClave.setPasswordVisibilityToggleEnabled(true);
        }

        if(txtRepClave.getText().toString().equals("")) {
            //Toast.makeText(this, "Llene todos los campos", Toast.LENGTH_SHORT).show();
            //txtRepClave.requestFocus();
            txtRepClave.setError(error);
            txtlyRepClave.setPasswordVisibilityToggleEnabled(false);
        }
        else{
            g = true;
            txtRepClave.setError(null);
            txtRepClave.setCompoundDrawables(null, null, null, null);
            txtlyRepClave.setPasswordVisibilityToggleEnabled(true);
        }

        if(rbtGenero.getCheckedRadioButtonId() == -1) {
            //Toast.makeText(this, "Escoja un género", Toast.LENGTH_SHORT).show();
            //rbtGenero.requestFocus();

            rbtMasculino.setError("Escoja un género");
        }
        else{
            h = true;
            rbtMasculino.setError(null);
            rbtMasculino.setCompoundDrawables(null, null, null, null);
        }

        if(a && b && c && d && e && f && g && h)
            return true;
        else
            return  false;

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(!txtClave.getText().toString().equals(""))
        {
            txtlyClave.setPasswordVisibilityToggleEnabled(true);
        }
        if(!txtRepClave.getText().toString().equals(""))
        {
            txtlyRepClave.setPasswordVisibilityToggleEnabled(true);
        }
        if(!txtFechaNacimiento.getText().toString().equals(""))
        {
            txtFechaNacimiento.setError(null);
        }

    }
}
