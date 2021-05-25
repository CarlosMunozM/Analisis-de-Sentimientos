package com.example.analisissentimientos.Controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.analisissentimientos.Clases.Conexion;
import com.example.analisissentimientos.DAO.PersonaDAO;
import com.example.analisissentimientos.Modelo.PersonaMOD;
import com.example.analisissentimientos.R;
import com.example.analisissentimientos.WebServices.Asynchtask;
import com.example.analisissentimientos.WebServices.WebService;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity implements Asynchtask {

    //RequestQueue rq;
    EditText txtUsuario, txtClave;
    TextInputLayout txtly_clave;
    String respuesta;
    PersonaDAO personaDAO;
    PersonaMOD personaMOD;
    ProgressDialog dialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Sentiment Analysis");

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtClave = (EditText) findViewById(R.id.txtClave);
        txtly_clave = (TextInputLayout) findViewById(R.id.txtloginly_clave);
    }


    public void iniciarSesion(View view)
    {

        if(!txtUsuario.getText().toString().equals("") && !txtClave.getText().toString().equals("")) {

            txtUsuario.setError(null);
            txtUsuario.setCompoundDrawables(null, null, null, null);
            txtClave.setError(null);
            txtClave.setCompoundDrawables(null, null, null, null);
            txtly_clave.setPasswordVisibilityToggleEnabled(true);

            personaDAO = new PersonaDAO();
            personaMOD = new PersonaMOD();

            mostrarDialogo("Logging in");

            Map<String, String> datos = new HashMap<String, String>();
            WebService ws = new WebService("http://"+ Conexion.getServidor() +":8080/WS_AnalisisSentimientos/wsLogin" +
                    "?accion=login" +
                    "&usr=" + txtUsuario.getText().toString() + "" +
                    "&clave=" + txtClave.getText().toString(), datos, this, this);
            ws.execute("");


            /*
            personaMOD.setUsuario(txtUsuario.getText().toString());
            personaMOD.setClave(txtClave.getText().toString());
            personaDAO.loginWS(getApplicationContext(), personaMOD);
            */
        }
        else
        {
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
            if(txtUsuario.getText().toString().equals("")) {
                txtUsuario.setError("Fill this fields");
            }
            if(txtClave.getText().toString().equals("")) {
                txtClave.setError("Fill this fields");
                txtly_clave.setPasswordVisibilityToggleEnabled(false);
            }
        }

    }

    public void registrarUsuario(View view) {
        Intent intent = new Intent(this, RegistrarJugador.class);
        intent.putExtra("pantalla", 0);
        startActivity(intent);

    }

    @Override
    public void processFinish(String result) throws JSONException {

        if(!result.equals("error")) {
            personaDAO = new PersonaDAO();
            personaMOD = new PersonaMOD();

            personaMOD = personaDAO.parsearWSlogin(result);
            //Toast.makeText(getApplicationContext(),personaMOD.getId_persona() + " "+ personaMOD.getNombres() + " "+ personaMOD.getApellidos() + " "+ personaMOD.getTipoUsuarioMOD().getId_tipousuario(), Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "Hello, " + personaMOD.getNombres(), Toast.LENGTH_SHORT).show();

            if(personaMOD.getTipoUsuarioMOD().getId_tipousuario() == 1)
            {
                cerrarDialogo();
                Intent intent = new Intent(this, Jugadores.class);
                intent.putExtra("idUsuario", personaMOD.getId_persona());
                startActivity(intent);
            }
            else
            {
                cerrarDialogo();
                Intent intent = new Intent(this, PantallaPrincipal.class);
                intent.putExtra("idUsuario", personaMOD.getId_persona());
                startActivity(intent);
            }


        }
        else
        {
            cerrarDialogo();
            Toast.makeText(this, "Failed to login", Toast.LENGTH_SHORT).show();
        }

    }

    public void mostrarDialogo(String texto)
    {
        dialogo = new ProgressDialog(this);
        dialogo.setMessage(texto);
        dialogo.setIndeterminate(false);
        dialogo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //dialogo.setCancelable(true);
        dialogo.show();
    }

    public void cerrarDialogo()
    {
        dialogo.dismiss();
    }
}