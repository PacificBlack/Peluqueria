package com.unipacifico.peluqueria.vistas.trabajador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.unipacifico.peluqueria.R;
import com.unipacifico.peluqueria.clases.Reservas;
import com.unipacifico.peluqueria.clases.Trabajador;
import com.unipacifico.peluqueria.costantes.Constantes;
import com.unipacifico.peluqueria.vistas.cliente.AdapatadorReservaClientes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Trabajador_V extends AppCompatActivity {



    ArrayList<Trabajador> trabajadores = new ArrayList<>();
    ArrayList<Reservas> reservas = new ArrayList<>();
    AdaptadorCitasTrabajador adaptador;
    RecyclerView recyclerViewTrabajador;
    AdaptadorCitasTrabajador adapatador;

    TextView nombre_trabajador,telefono_trabajador;

    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabajador__v);

        nombre_trabajador = findViewById(R.id.nombre_v_trabajador);
        telefono_trabajador = findViewById(R.id.telefono_v_trabajador);
        recyclerViewTrabajador = findViewById(R.id.recyclerTrabajador);

        recyclerViewTrabajador.setLayoutManager(new LinearLayoutManager(this));

        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMessage("Espere un momento");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setIndeterminate(true);
        progressDoalog.setCancelable(false);

        ArrayList<Trabajador> worker = (ArrayList<Trabajador>) getIntent().getSerializableExtra("trabajador");
        trabajadores.addAll(worker);

        Cargar_Citas(trabajadores.get(0).getIdentificacion());
        nombre_trabajador.setText(trabajadores.get(0).getNombre());
        telefono_trabajador.setText(trabajadores.get(0).getTelefono());

    }

    private void Cargar_Citas(String identificacion) {

        JsonObjectRequest objectRequest_traer;
        String url_traer = Constantes.DireccionServidor + "Reservas/wsJSONReservasTrabajador.php?identificacion="+identificacion;
        objectRequest_traer = new JsonObjectRequest(Request.Method.GET, url_traer, null, response -> {

            Log.i("Trajo",response.toString());
            reservas.clear();
            Reservas reserv = null;
            JSONArray jsonArrayReservas = response.optJSONArray("reservas");
            try {
                for (int i = 0; i < jsonArrayReservas.length(); i++) {
                    reserv = new Reservas();
                    JSONObject jsonObject = null;
                    jsonObject = jsonArrayReservas.getJSONObject(i);

                    reserv.setId_reserva(jsonObject.getString("id_reserva"));
                    reserv.setNumero_cliente(jsonObject.getString("telefono_cliente"));
                    reserv.setIdententificacion_peluquero(jsonObject.getString("identificacion_peluquero"));
                    reserv.setDetalle_servicio(jsonObject.getString("detalle_servicio"));
                    reserv.setPrecio_servicio(jsonObject.getString("precio_servicio"));
                    reserv.setPago_servicio(jsonObject.getString("pago_servicio"));
                    reserv.setEstado_servicio(jsonObject.getString("estado_servicio"));
                    reserv.setFecha_servico(jsonObject.getString("fecha_servicio"));

                    reservas.add(reserv);
                }

                adapatador = new AdaptadorCitasTrabajador(reservas,this);
                recyclerViewTrabajador.setAdapter(adapatador);
                adapatador.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.i("ERROR",response.toString());
                e.printStackTrace();
            }

        }, error -> {
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(objectRequest_traer);
    }
}