package com.unipacifico.peluqueria.vistas.administrador.clientes;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.unipacifico.peluqueria.R;
import com.unipacifico.peluqueria.clases.Cliente;
import com.unipacifico.peluqueria.clases.Trabajador;
import com.unipacifico.peluqueria.costantes.Constantes;
import com.unipacifico.peluqueria.vistas.cliente.Cliente_V;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Clientes_V_Administrador extends Fragment {


    public Clientes_V_Administrador() {

    }


    ArrayList<Cliente> clientes = new ArrayList<>();
    AdaptadorClientes_Admin adaptador;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_clientes__v__administrador, container, false);
        recyclerView = vista.findViewById(R.id.cliente_v_admin);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TraerClientes();

        return vista;
    }

    private void TraerClientes() {
        JsonObjectRequest objectRequest_peluqueros;
        String url_peluqueros = Constantes.DireccionServidor + "Administrador/wsJSONClientes.php?";
        objectRequest_peluqueros = new JsonObjectRequest(Request.Method.GET, url_peluqueros, null, response -> {

            Log.i("Cliente", response.toString());

            Cliente cliente = null;
            JSONArray jsonArrayCliente = response.optJSONArray("cliente");
            clientes.clear();

            try {
                for (int i = 0; i < jsonArrayCliente.length(); i++) {
                    cliente = new Cliente();
                    JSONObject jsonObject = null;
                    jsonObject = jsonArrayCliente.getJSONObject(i);

                    cliente.setTelefono(jsonObject.getString("telefono"));
                    cliente.setNombre(jsonObject.getString("nombre"));
                    cliente.setApellido(jsonObject.getString("apellido"));
                    cliente.setDireccion(jsonObject.getString("direccion"));
                    cliente.setContraseña(jsonObject.getString("contrasena"));

                    clientes.add(cliente);

                }

                adaptador = new AdaptadorClientes_Admin(clientes,getContext());
                recyclerView.setAdapter(adaptador);
                adaptador.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, error -> {
            Log.i("Error", error.toString());
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(objectRequest_peluqueros);
    }
}