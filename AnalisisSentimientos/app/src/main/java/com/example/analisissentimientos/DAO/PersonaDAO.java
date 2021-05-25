package com.example.analisissentimientos.DAO;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.analisissentimientos.Controlador.RegistrarJugador;
import com.example.analisissentimientos.Modelo.PersonaMOD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PersonaDAO {


    public PersonaMOD loginWS(final Context context, final PersonaMOD personaMOD) {

        RequestQueue rq = Volley.newRequestQueue(context);
        final PersonaMOD personaMOD2 = new PersonaMOD();

        StringRequest str = new StringRequest(Request.Method.POST,
                "http://192.168.100.232:8080/WS_AnalisisSentimientos/wsLogin",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context, response, Toast.LENGTH_LONG).show();

                        try {

                            JSONObject  jsonObject = new JSONObject(response);

                            String id, nombres, apellidos;

                            id = jsonObject.getString("id_persona");
                            nombres = jsonObject.getString("nombres");
                            apellidos = jsonObject.getString("apellidos");

                            personaMOD2.setId_persona(Integer.parseInt(id));
                            personaMOD2.setNombres(nombres);
                            personaMOD2.setApellidos(apellidos);

                            Intent intent = new Intent(context, RegistrarJugador.class);
                            context.startActivity(intent);

                        } catch (JSONException e) {
                            Log.d("Error: ", e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: ", error.toString());
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<>();
                parametros.put("accion","login");
                parametros.put("usr", personaMOD.getUsuario());
                parametros.put("clave", personaMOD.getClave());
                return  parametros;
            }
        };
        rq.add(str);

        return personaMOD2;
    }

    public PersonaMOD parsearWSlogin(String json) throws JSONException {

        PersonaMOD personaMOD = new PersonaMOD();
        JSONObject  jsonObject = new JSONObject(json);

        String id, nombres, apellidos;

        id = jsonObject.getString("id_persona");
        nombres = jsonObject.getString("nombres");
        apellidos = jsonObject.getString("apellidos");

        personaMOD.setId_persona(Integer.parseInt(id));
        personaMOD.setNombres(nombres);
        personaMOD.setApellidos(apellidos);

        JSONObject opciones = (JSONObject) jsonObject.get("tipoUsuario");
        personaMOD.getTipoUsuarioMOD().setId_tipousuario(Integer.parseInt(opciones.getString("id_tipoUsuario")));

        return personaMOD;
    }

}
