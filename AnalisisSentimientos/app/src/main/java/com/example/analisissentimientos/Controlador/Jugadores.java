package com.example.analisissentimientos.Controlador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.analisissentimientos.Clases.Conexion;
import com.example.analisissentimientos.Modelo.PersonaMOD;
import com.example.analisissentimientos.R;
import com.example.analisissentimientos.WebServices.Asynchtask;
import com.example.analisissentimientos.WebServices.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Jugadores extends AppCompatActivity implements Asynchtask, AdapterView.OnItemClickListener {

    SwipeRefreshLayout swipeRefreshLayout;
    String metodo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugadores);

        getSupportActionBar().setTitle("Players");

        actualizarLista();

        ListView lstJugadores  = findViewById(R.id.lstLista);
        lstJugadores.setOnItemClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                actualizarLista();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cerrar_sesion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemCerrarSesion:
                startActivity(new Intent(this, Login.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void actualizarLista()
    {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("accion", "mostrarJugadores");

        WebService ws = new WebService("http://"+ Conexion.getServidor() +":8080/WS_AnalisisSentimientos/wsJugador",
                datos, this, this);
        metodo = "listarJugadores";
        ws.execute("");
    }

    public void eliminarJugador(int idJugador)
    {
        Map<String, String> datos = new HashMap<String, String>();
        datos.put("accion", "eliminarJugador");
        datos.put("idJugador", String.valueOf(idJugador));

        WebService ws = new WebService("http://"+ Conexion.getServidor() +":8080/WS_AnalisisSentimientos/wsJugador",
                datos, this, this);
        metodo = "eliminarJugador";
        ws.execute("");
    }

    public void modificarJugador(PersonaMOD persona)
    {
        Intent intent = new Intent(this, ModificarJugador.class);
        intent.putExtra("pantalla", 0);

        intent.putExtra("id_persona", persona.getId_persona());
        intent.putExtra("nombres", persona.getNombres());
        intent.putExtra("apellidos", persona.getApellidos());
        intent.putExtra("telefono", persona.getTelefono());
        intent.putExtra("fechaNacimiento", persona.getFecha_nacimiento());
        intent.putExtra("genero", persona.getGenero());

        startActivity(intent);
    }


    public void abrir_registro_jugador(View view)
    {
        Intent intent = new Intent(this, RegistrarJugador.class);
        intent.putExtra("pantalla", 1);
        startActivity(intent);
    }

    @Override
    public void processFinish(String result) throws JSONException {

        if (metodo.equals("listarJugadores")) {
            ArrayList<PersonaMOD> personas = new ArrayList<>();

            JSONArray parsear = new JSONArray(result);
            JSONObject c;

            for (int i = 0; i < parsear.length(); i++) {
                c = parsear.getJSONObject(i);

                personas.add(new PersonaMOD(c.getInt("id_persona"),
                        c.getString("nombres"),
                        c.getString("apellidos"),
                        c.getString("telefono"),
                        c.getString("usuario"),
                        c.getString("genero"),
                        c.getString("fechaNacimiento")));
            }
            swipeRefreshLayout.setRefreshing(false);
            AdaptadorJugador adaptadorJugador = new AdaptadorJugador(this, personas);
            ListView lstJugadores = findViewById(R.id.lstLista);
            lstJugadores.setAdapter(adaptadorJugador);
        }
        if (metodo.equals("eliminarJugador")) {
            if(result.equals("correcto"))
                actualizarLista();
            else
                Toast.makeText(this, "Error deleting player", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        modalOpciones((PersonaMOD) (parent.getItemAtPosition(position)));
    }

    private void modalOpciones(final PersonaMOD persona) {

        final CharSequence[] opciones = {"Modify","Delete","Cancel"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(Jugadores.this);
        builder.setTitle("Choose an Option");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(opciones[which].equals("Modify"))
                    modificarJugador(persona);
                else if(opciones[which].equals("Delete"))
                    eliminarJugador(persona.getId_persona());
                else if(opciones[which].equals("Cancel"))
                    dialog.dismiss();
            }
        });

        builder.show();
    }
}
