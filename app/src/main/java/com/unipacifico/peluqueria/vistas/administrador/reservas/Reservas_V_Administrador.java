package com.unipacifico.peluqueria.vistas.administrador.reservas;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.unipacifico.peluqueria.R;
import com.unipacifico.peluqueria.clases.Reservas;
import com.unipacifico.peluqueria.clases.Trabajador;
import com.unipacifico.peluqueria.costantes.Constantes;
import com.unipacifico.peluqueria.vistas.administrador.empleados.AdaptadorEmpleados_Admin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class Reservas_V_Administrador extends Fragment {


    public Reservas_V_Administrador() {

    }

    EditText fecha;
    TextView ganacia, recaudo;
    RecyclerView recyclerView;
    AdaptadorReservas_Administrador adaptador;
    ArrayList<Reservas> reservas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_reservas__v__administrador, container, false);

        ganacia = vista.findViewById(R.id.ganancias_reservas_administrador);
        recaudo = vista.findViewById(R.id.recaudadas_reservas_administrador);
        recyclerView = vista.findViewById(R.id.reservas_v_admin);
        fecha = vista.findViewById(R.id.fecha_reservas_administrador);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fecha.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int año = c.get(Calendar.YEAR);
            int mes = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog pickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String fecha_servicio = "" + dayOfMonth + "/" + month + "/" + year;
                    fecha.setText(fecha_servicio);
                    Log.i("Tiempo",fecha_servicio);
                    TraerReservas(fecha_servicio);
                }
            }, año, mes, d);

            pickerDialog.show();
        });


        return vista;
    }

    private void TraerReservas(String fecha_servicio) {
        JsonObjectRequest objectRequest_peluqueros;
        String url_peluqueros = Constantes.DireccionServidor + "Administrador/wsJSONReservaciones.php?fecha=" + fecha_servicio;
        objectRequest_peluqueros = new JsonObjectRequest(Request.Method.GET, url_peluqueros, null, response -> {

            Log.i("Cliente", response.toString());

            Reservas reserv = null;
            JSONArray jsonArray = response.optJSONArray("reserva");
            reservas.clear();

            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    reserv = new Reservas();
                    JSONObject jsonObject = null;
                    jsonObject = jsonArray.getJSONObject(i);

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

                int precio = 0;
                int pago = 0;
                for (int i = 0; i < reservas.size() ; i++) {
                    precio += Integer.parseInt(reservas.get(i).getPrecio_servicio());
                    pago += Integer.parseInt(reservas.get(i).getPago_servicio());
                }

                ganacia.setText("Ganancias estimadas: "+precio);
                recaudo.setText("Ganancia obtenidas: "+pago);

                adaptador = new AdaptadorReservas_Administrador(reservas);
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