package com.example.analisissentimientos.Controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.analisissentimientos.Clases.Conexion;
import com.example.analisissentimientos.R;
import com.example.analisissentimientos.WebServices.Asynchtask;
import com.example.analisissentimientos.WebServices.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Juego extends AppCompatActivity implements Asynchtask {

    ImageView imgAnalisis;
    EditText txtOpinion;
    Button btnSiguiente;

    String metodo = "", texto = "", resAnalisis = "";
    String[] imaBD, imagenes, opiniones;
    int[] aleatorios, posiciones, idImaBD, idImagenses;
    int iteracion = 0, total_imgBD = 0, idPersona = 0, idJuego = 0, imag_mostrar = 6;
    ProgressDialog dialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        imgAnalisis = (ImageView) findViewById(R.id.imgJuego);
        txtOpinion = (EditText) findViewById(R.id.txtJug_Opinion);
        btnSiguiente = (Button) findViewById(R.id.btnJug_Siguiente);

        txtOpinion.setEnabled(true);
        btnSiguiente.setEnabled(true);

        inicializar();
        opiniones = new String[imag_mostrar];
    }

    private void inicializar() {

        idPersona = this.getIntent().getExtras().getInt("idUsuario");

        imagenes = new String[imag_mostrar];
        idImagenses = new int[imag_mostrar];

        Map<String, String> datos = new HashMap<String, String>();
        datos.put("accion", "obtenerImg");

        WebService ws = new WebService("http://"+ Conexion.getServidor() +":8080/WS_AnalisisSentimientos/wsJuego",
                datos, this, this);

        metodo = "obtenerImg";
        ws.execute("");
    }

    public void cambiar(View view) {
        try {
            if(!txtOpinion.getText().toString().equals("")) {

                txtOpinion.setError(null);
                txtOpinion.setCompoundDrawables(null, null, null, null);

                opiniones[iteracion - 1] = txtOpinion.getText().toString();
                txtOpinion.setText("");

                if (iteracion == imag_mostrar - 1)
                    btnSiguiente.setText("Finalize");

                if (iteracion == imag_mostrar) {
                    //realizarAnalisis();
                    btnSiguiente.setText("Finalize");
                    registarJuegos();
                }
                else
                    colocarImagen();
            }
            else {
                txtOpinion.setError("Fill in this field");
                Toast.makeText(this, "Enter an opinion", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void colocarImagen() {

        Glide.with(this)
                .load(imagenes[iteracion])
                .error(R.drawable.img_error1)
                .into(imgAnalisis);

        iteracion ++;
    }

    public void registarJuegos()
    {
        for(int i = 0; i< opiniones.length;i++)
        {
            if(i == 0)
                texto = opiniones[i] + ". ";
            else
                texto += opiniones[i] + ". ";
        }

        Map<String, String> datos = new HashMap<String, String>();
        datos.put("accion", "registrarJuego");
        datos.put("idPersona", String.valueOf(idPersona));
        datos.put("frase", texto);
        WebService ws = new WebService("http://"+ Conexion.getServidor() +":8080/WS_AnalisisSentimientos/wsJuego",
                datos, this, this);

        metodo = "registrarJuego";
        mostrarDialogo("Performing analysis...");
        ws.execute("");
    }

    public void registrarJuegoImagen(int idJuego, int[] idImagenses, String[] opiniones) {

        Map<String, String> datos;
        WebService ws = null;

        for (int i = 0; i < imag_mostrar; i++)
        {
            datos = new HashMap<String, String>();
            datos.put("accion", "registrarJuegoImagen");
            datos.put("idJuego", String.valueOf(idJuego));
            datos.put("idImagen", String.valueOf(idImagenses[i]));
            datos.put("opinion", opiniones[i]);
            ws = new WebService("http://"+ Conexion.getServidor() +":8080/WS_AnalisisSentimientos/wsJuego",
                    datos, this, this);

            metodo = "registrarJuegoImagen";
            ws.execute("");
        }
    }
    @Override
    public void processFinish(String result) throws JSONException {

        if(metodo.equals("obtenerImg"))
        {
            imaBD = parsearWSimagenes(result);
            posiciones = obtenerRandom(imag_mostrar, imaBD.length - 1);

            for (int i = 0; i < imag_mostrar; i++) {
                imagenes[i] = imaBD[posiciones[i]];
                idImagenses[i] = idImaBD[posiciones[i]];
            }

            colocarImagen();
        }
        if(metodo.equals("realizarAnalisis"))
        {
            JSONObject objSentimientos = new JSONObject(result);
            resAnalisis = objSentimientos.getString("score_tag");
            iteracion = 0;
            result = "";
            Toast.makeText(this, resAnalisis, Toast.LENGTH_SHORT).show();
        }
        if(metodo.equals("registrarJuego"))
        {
          idJuego = Integer.parseInt(result);
            if(idJuego != 0)
            {
                registrarJuegoImagen(idJuego, idImagenses, opiniones);
            }
            else
                Toast.makeText(this, "Error registering game", Toast.LENGTH_SHORT).show();
        }
        if(metodo.equals("registrarJuegoImagen"))
        {
            if(result.equals("correcto"))
            {
                cerrarDialogo();
                txtOpinion.setEnabled(false);
                btnSiguiente.setEnabled(false);

                Intent intent = new Intent(this, Resultado.class);
                intent.putExtra("idUsuario", idPersona);
                intent.putExtra("idJuego", idJuego);
                startActivity(intent);

            }
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

    private String[] parsearWSimagenes(String result) throws JSONException {

        JSONArray parsear = new JSONArray(result);
        String[] lista = new String[parsear.length()];
        idImaBD = new int[parsear.length()];
        JSONObject c;

        for(int i = 0; i< parsear.length(); i++)
        {
            c = parsear.getJSONObject(i);
            lista[i] = c.getString("ruta");
            idImaBD[i] = c.getInt("id_imagen");
        }
        return lista;
    }

    public void realizarAnalisis() {

        for(int i = 0; i< opiniones.length;i++)
        {
            if(i == 0)
                texto = opiniones[i] + ". ";
            else
                texto += opiniones[i] + ". ";
        }

        Map<String, String> datos = new HashMap<String, String>();
        datos.put("accion", "analizar");
        datos.put("frase", texto);
        WebService ws = new WebService("http://"+ Conexion.getServidor() +":8080/WS_AnalisisSentimientos/wsJuego",
                datos, this, this);

        metodo = "realizarAnalisis";
        ws.execute("");


    }

    private int[] obtenerRandom(int numero, int max) {
        Random rnd = new Random();
        int n, i = 0;
        int[] a = new int[numero];
        while (i < numero) {
            //n = rnd.nextInt(max);
            n = rnd.nextInt((max - 0) + 1) + 0;

            if (!buscarNumero(a, n)) {
                a[i] = n;
                i++;
            }
        }
        return a;
    }

    public Boolean buscarNumero(int[] vector, int numero) {
        Boolean b = false;

        for (int i = 0; i < vector.length; i++) {
            if (numero == vector[i])
                b = true;
        }
        return b;
    }
}
