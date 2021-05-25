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
import com.example.analisissentimientos.Controlador.Jugadores;
import com.example.analisissentimientos.Controlador.Login;
import com.example.analisissentimientos.R;
import com.example.analisissentimientos.WebServices.Asynchtask;
import com.example.analisissentimientos.WebServices.WebService;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class ModificarJugador extends AppCompatActivity implements Asynchtask, View.OnClickListener, TextWatcher {

    EditText txtNombres, txtApellidos, txtTelefono, txtFechaNacimiento;
    RadioGroup rbtGenero;
    RadioButton radioButton, rbtMasculino, rbtFemenino;
    Boolean b_radiobutton = false, camposLlenos = false;
    String error, nombres, apellidos, telefono, fechaNacimiento, genero, id_jugador;

    int pantalla = 0;

    Bundle datos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_jugador);

         datos = this.getIntent().getExtras();

        getSupportActionBar().setTitle("Modify Player");

        txtNombres = (EditText) findViewById(R.id.txtModJug_Nombres);
        txtApellidos = (EditText) findViewById(R.id.txtModJug_Apellidos);
        txtTelefono = (EditText) findViewById(R.id.txtModJug_Telefono);
        txtFechaNacimiento = (EditText) findViewById(R.id.txtModJug_FechaNacimiento);
        rbtGenero = (RadioGroup) findViewById(R.id.rbtModJug_Genero);
        rbtMasculino = (RadioButton) findViewById(R.id.rbtModJug_Masculino);
        rbtFemenino = (RadioButton) findViewById(R.id.rbtModJug_Femenino);

        txtFechaNacimiento.setOnClickListener(this);
        txtFechaNacimiento.addTextChangedListener(this);

        llenarCampos();


    }

    public void llenarCampos()
    {
        pantalla = datos.getInt("pantalla");

        id_jugador = String.valueOf(datos.getInt("id_persona"));
        txtNombres.setText(datos.getString("nombres"));
        txtApellidos.setText(datos.getString("apellidos"));
        txtTelefono.setText(datos.getString("telefono"));
        txtFechaNacimiento.setText(datos.getString("fechaNacimiento"));
        if(datos.getString("genero").equals("M"))
            rbtMasculino.setChecked(true);
        else
            rbtFemenino.setChecked(true);
    }


    public void modificarJugador(View view)
    {

        if(validarCamposVacios()) {
            ejecutarWS_modificarJugador();
        }
        else
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
    }

    public void ejecutarWS_modificarJugador()
    {
        Map<String, String> datos = new HashMap<String, String>();

        datos.put("accion","modificarJugador");

        datos.put("id_persona",id_jugador);
        datos.put("nombres",txtNombres.getText().toString());
        datos.put("apellidos",txtApellidos.getText().toString());
        datos.put("telefono",txtTelefono.getText().toString());
        datos.put("fecha_nacimiento",txtFechaNacimiento.getText().toString());
        datos.put("genero",obtenerGenero());

        WebService ws = new WebService("http://"+ Conexion.getServidor() +":8080/WS_AnalisisSentimientos/wsJugador"
                , datos, this, this);

        ws.execute("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtModJug_FechaNacimiento:
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
            Toast.makeText(this, "Connection errorError de conexión", Toast.LENGTH_SHORT).show();
        else {
            if (result.equals("correcto")) {
                Toast.makeText(getApplicationContext(), "Modified Player", Toast.LENGTH_LONG).show();
                if(pantalla == 0)
                    startActivity(new Intent(getApplicationContext(), Jugadores.class));
            } else if (result.equals("error"))
                Toast.makeText(getApplicationContext(), "Error Modifying Player", Toast.LENGTH_LONG).show();

        }
    }



    private Boolean validarCamposVacios() {

        Boolean a = false, b = false, c = false, d = false, e = false;
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

        if(rbtGenero.getCheckedRadioButtonId() == -1) {
            //Toast.makeText(this, "Escoja un género", Toast.LENGTH_SHORT).show();
            //rbtGenero.requestFocus();

            rbtMasculino.setError("Choose a gender");
        }
        else{
            e = true;
            rbtMasculino.setError(null);
            rbtMasculino.setCompoundDrawables(null, null, null, null);
        }

        if(a && b && c && d && e)
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

        if(!txtFechaNacimiento.getText().toString().equals(""))
        {
            txtFechaNacimiento.setError(null);
        }

    }
}
