package com.example.analisissentimientos.Controlador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.analisissentimientos.R;

public class PantallaPrincipal extends AppCompatActivity {

    Button btnJugar;
    int idJugador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        btnJugar = (Button) findViewById(R.id.btnPantPrin_Jugar);

        Bundle datos = this.getIntent().getExtras();
        idJugador = datos.getInt("idUsuario");
    }

    public void jugar(View v) {

        Intent intent = new Intent(this, Juego.class);
        intent.putExtra("idUsuario", idJugador);
        startActivity(intent);
    }

    public void cerrarSesion(View v) {

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
