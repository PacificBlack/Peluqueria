package com.unipacifico.peluqueria.vistas.administrador.empleados;

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
import com.unipacifico.peluqueria.vistas.administrador.clientes.AdaptadorClientes_Admin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class Empleados_V_Administrador extends Fragment {


    public Empleados_V_Administrador() {

    }


    ArrayList<Trabajador> empleados = new ArrayList<>();
    AdaptadorEmpleados_Admin adaptador;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_empleados__v__administrador, container, false);
        recyclerView = vista.findViewById(R.id.empleado_v_admin);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TraerEmpleados();
        return vista;
    }

    private void TraerEmpleados() {
        JsonObjectRequest objectRequest_peluqueros;
        String url_peluqueros = Constantes.DireccionServidor + "Administrador/wsJSONEmpleados.php?";
        objectRequest_peluqueros = new JsonObjectRequest(Request.Method.GET, url_peluqueros, null, response -> {

            Log.i("Cliente", response.toString());

            Trabajador trabajador = null;
            JSONArray jsonArrayCliente = response.optJSONArray("empleados");
                empleados.clear();

            try {
                for (int i = 0; i < jsonArrayCliente.length(); i++) {
                    trabajador = new Trabajador();
                    JSONObject jsonObject = null;
                    jsonObject = jsonArrayCliente.getJSONObject(i);

                    trabajador.setTelefono(jsonObject.getString("telefono"));
                    trabajador.setNombre(jsonObject.getString("nombre"));
                    trabajador.setIdentificacion(jsonObject.getString("identificacion"));
                    trabajador.setDireccion(jsonObject.getString("direccion"));
                    trabajador.setContraseña(jsonObject.getString("contrasena"));

                    empleados.add(trabajador);

                }

                adaptador = new AdaptadorEmpleados_Admin(empleados,getContext());
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