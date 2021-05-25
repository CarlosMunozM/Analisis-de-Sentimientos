package com.example.analisissentimientos.Controlador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.analisissentimientos.Modelo.PersonaMOD;
import com.example.analisissentimientos.R;

import java.util.ArrayList;

public class AdaptadorJugador extends ArrayAdapter<PersonaMOD> {

    public AdaptadorJugador(Context context, ArrayList<PersonaMOD> datos)
    {
        super(context, R.layout.item, datos);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.item, null);

        TextView lblApellido = (TextView)item.findViewById(R.id.apellidos);
        lblApellido.setText(getItem(position).getApellidos());

        TextView lblApellidos = (TextView)item.findViewById(R.id.nombres);
        lblApellidos.setText(getItem(position).getNombres());

        TextView lblTelefono = (TextView)item.findViewById(R.id.telefono);
        lblTelefono.setText(getItem(position).getTelefono());

        return(item);
    }
}
