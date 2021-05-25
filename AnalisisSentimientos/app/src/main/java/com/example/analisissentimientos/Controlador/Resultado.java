package com.example.analisissentimientos.Controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.analisissentimientos.Clases.Conexion;
import com.example.analisissentimientos.R;
import com.example.analisissentimientos.WebServices.Asynchtask;
import com.example.analisissentimientos.WebServices.WebService;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class Resultado extends AppCompatActivity implements Asynchtask, View.OnClickListener {

    TextView lblResultado;
    Button btnPantallaPrincipal;

    int idJuego, idPersona;
    String metodo = "", resAnalisis = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        lblResultado = (TextView) findViewById(R.id.lblResul_resultado);
        btnPantallaPrincipal = (Button) findViewById(R.id.btnResul_PantPrincipal);

        btnPantallaPrincipal.setOnClickListener(this);

        idJuego = this.getIntent().getExtras().getInt("idJuego");
        idPersona = this.getIntent().getExtras().getInt("idUsuario");

        obtenerResultados(idPersona, idJuego);
    }

    private void obtenerResultados(int idPersona, int idJuego) {

        Map<String, String> datos = new HashMap<String, String>();
        datos.put("accion", "obtenerAnalisis");
        datos.put("idPersona", String.valueOf(idPersona));
        datos.put("idJuego", String.valueOf(idJuego));

        WebService ws = new WebService("http://"+ Conexion.getServidor() +":8080/WS_AnalisisSentimientos/wsJuego",
                datos, this, this);

        metodo = "obtenerResultados";
        ws.execute("");
    }

    @Override
    public void processFinish(String result) throws JSONException {

        if(metodo.equals("obtenerResultados")) {

            if(!result.equals("error")) {

                resAnalisis = result;

                if (resAnalisis.equals("P+")) {
                    resAnalisis = "You have very positive feelings";
                    //resAnalisis = "Usted posee sentimiento muy positivos";
                } else if (resAnalisis.equals("P")) {
                    resAnalisis = "You have positive feelings";
                    //resAnalisis = "Usted posee sentimiento positivos";
                } else if (resAnalisis.equals("NEU")) {
                    resAnalisis = "You have neutral feelings";
                    //resAnalisis = "Usted posee sentimiento neutrales";
                } else if (resAnalisis.equals("N")) {
                    resAnalisis = "You have negative feelings";
                    //resAnalisis = "Usted posee sentimiento negativos";
                } else if (resAnalisis.equals("N+")) {
                    resAnalisis = "You have very negative feelings";
                    //resAnalisis = "Usted posee sentimiento muy negativos";
                } else if (resAnalisis.equals("NONE")) {
                    resAnalisis = "No feelings detected";
                    //resAnalisis = "No se han detectado sentimientos";
                }

                lblResultado.setText(resAnalisis);
            }
            else
                Toast.makeText(this, "Error getting analysis", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PantallaPrincipal.class);
        intent.putExtra("idUsuario", idPersona);
        startActivity(intent);
    }
}
