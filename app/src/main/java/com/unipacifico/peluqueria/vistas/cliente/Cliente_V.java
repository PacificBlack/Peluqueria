package com.unipacifico.peluqueria.vistas.cliente;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.unipacifico.peluqueria.R;
import com.unipacifico.peluqueria.clases.Cliente;
import com.unipacifico.peluqueria.clases.Reservas;
import com.unipacifico.peluqueria.clases.Trabajador;
import com.unipacifico.peluqueria.costantes.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.unipacifico.peluqueria.costantes.Constantes.*;

public class Cliente_V extends AppCompatActivity {

    TextView nombre_clientes, numero_clientes, direccion_clientes;
    MaterialButton boton_reservar;
    int servicio_detalle = 0;
    double costo = 0;
    String detalle_servicio;

    //--Alert
    RadioButton peluquero1, peluquero2, peluquero3;
    RadioButton horario_dia, horario_tarde, horario_noche;
    Spinner horario;
    TextView dia_reserva;
    EditText dia_calendario;
    MaterialButton completar_reserva;

    //Reserva
    ArrayList<Trabajador> trabajadores = new ArrayList<>();
    ArrayList<Cliente> clientes = new ArrayList<>();
    ArrayList<Reservas> reservas = new ArrayList<>();
    RecyclerView recyclerViewReservas;
    AdapatadorReservaClientes adapatador;


    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente__v);

        boton_reservar = findViewById(R.id.reservar);
        nombre_clientes = findViewById(R.id.nombre_v_clientes);
        numero_clientes = findViewById(R.id.telefono_v_clientes);
        direccion_clientes = findViewById(R.id.direccion_v_clientes);
        dia_reserva = findViewById(R.id.dia_reserva);
        recyclerViewReservas = findViewById(R.id.reservas_v_clientes);
        recyclerViewReservas.setLayoutManager(new LinearLayoutManager(this));

        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMessage("Espere un momento");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setIndeterminate(true);
        progressDoalog.setCancelable(false);

        ArrayList<Cliente> cliente = (ArrayList<Cliente>) getIntent().getSerializableExtra("cliente");
        clientes.addAll(cliente);

        Cargar_Peluqueros();
        TraerReservas(clientes.get(0).getTelefono());
        nombre_clientes.setText(clientes.get(0).getNombre());
        numero_clientes.setText(clientes.get(0).getTelefono());
        direccion_clientes.setText(clientes.get(0).getDireccion());


        boton_reservar.setOnClickListener(v -> {

            if (servicio_detalle == 0) {
                Toast.makeText(this, "Debes seleccionar un servicio", Toast.LENGTH_LONG).show();
            } else {

                ArrayAdapter<CharSequence> dia = null, tarde = null, noche = null;

                if (servicio_detalle == 10) {
                    costo = 3000;
                    detalle_servicio = "Mickey";
                    dia = ArrayAdapter.createFromResource(this, R.array.Turnos_Mañana_Diez, android.R.layout.simple_spinner_item);
                    dia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    tarde = ArrayAdapter.createFromResource(this, R.array.Turnos_Tarde_Diez, android.R.layout.simple_spinner_item);
                    tarde.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    noche = ArrayAdapter.createFromResource(this, R.array.Turnos_Noche_Diez, android.R.layout.simple_spinner_item);
                    noche.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                if (servicio_detalle == 11) {
                    costo = 5000;
                    detalle_servicio = "Barba";
                    dia = ArrayAdapter.createFromResource(this, R.array.Turnos_Mañana_Diez, android.R.layout.simple_spinner_item);
                    dia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    tarde = ArrayAdapter.createFromResource(this, R.array.Turnos_Tarde_Diez, android.R.layout.simple_spinner_item);
                    tarde.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    noche = ArrayAdapter.createFromResource(this, R.array.Turnos_Noche_Diez, android.R.layout.simple_spinner_item);
                    noche.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                if (servicio_detalle == 21) {
                    costo = 8000;
                    detalle_servicio = "Barba, Mickey";
                    dia = ArrayAdapter.createFromResource(this, R.array.Turnos_Mañana_Completo, android.R.layout.simple_spinner_item);
                    dia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    tarde = ArrayAdapter.createFromResource(this, R.array.Turnos_Tarde_Completo, android.R.layout.simple_spinner_item);
                    tarde.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    noche = ArrayAdapter.createFromResource(this, R.array.Turnos_Noche_Completo, android.R.layout.simple_spinner_item);
                    noche.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                if (servicio_detalle == 40) {
                    costo = 10000;
                    detalle_servicio = "Corte de cabello";
                    dia = ArrayAdapter.createFromResource(this, R.array.Turnos_Mañana_Completo, android.R.layout.simple_spinner_item);
                    dia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    tarde = ArrayAdapter.createFromResource(this, R.array.Turnos_Tarde_Completo, android.R.layout.simple_spinner_item);
                    tarde.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    noche = ArrayAdapter.createFromResource(this, R.array.Turnos_Noche_Completo, android.R.layout.simple_spinner_item);
                    noche.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                }
                if (servicio_detalle == 50) {
                    costo = 13000;
                    detalle_servicio = "Corte de cabello, Mickey";
                    dia = ArrayAdapter.createFromResource(this, R.array.Turnos_Mañana_Completo, android.R.layout.simple_spinner_item);
                    dia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    tarde = ArrayAdapter.createFromResource(this, R.array.Turnos_Tarde_Completo, android.R.layout.simple_spinner_item);
                    tarde.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    noche = ArrayAdapter.createFromResource(this, R.array.Turnos_Noche_Completo, android.R.layout.simple_spinner_item);
                    noche.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                if (servicio_detalle == 51) {
                    costo = 15000;
                    detalle_servicio = "Corte de cabello, Barba";
                    dia = ArrayAdapter.createFromResource(this, R.array.Turnos_Mañana_Completo, android.R.layout.simple_spinner_item);
                    dia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    tarde = ArrayAdapter.createFromResource(this, R.array.Turnos_Tarde_Completo, android.R.layout.simple_spinner_item);
                    tarde.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    noche = ArrayAdapter.createFromResource(this, R.array.Turnos_Noche_Completo, android.R.layout.simple_spinner_item);
                    noche.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }
                if (servicio_detalle == 61) {
                    costo = 12000;
                    detalle_servicio = "Corte de cabello, Barba, Mickey";
                    dia = ArrayAdapter.createFromResource(this, R.array.Turnos_Mañana_Completo, android.R.layout.simple_spinner_item);
                    dia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    tarde = ArrayAdapter.createFromResource(this, R.array.Turnos_Tarde_Completo, android.R.layout.simple_spinner_item);
                    tarde.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    noche = ArrayAdapter.createFromResource(this, R.array.Turnos_Noche_Completo, android.R.layout.simple_spinner_item);
                    noche.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                }

                Dialogo(costo, detalle_servicio, dia, tarde, noche);

            }
        });


    }

    public void verificados(View view) {

        boolean chequeado = ((CheckBox) view).isChecked();

        switch (view.getId()) {

            case R.id.mickey_v_clientes:
                if (chequeado) {
                    servicio_detalle += 10;
                    Log.i("Check", String.valueOf(servicio_detalle));

                } else
                    servicio_detalle -= 10;
                Log.i("Check", String.valueOf(servicio_detalle));

                break;

            case R.id.barba_v_clientes:
                if (chequeado) {
                    servicio_detalle += 11;
                    Log.i("Check", String.valueOf(servicio_detalle));

                } else {
                    servicio_detalle -= 11;
                    Log.i("Check", String.valueOf(servicio_detalle));

                }

                break;

            case R.id.corte_v_clientes:
                if (chequeado) {
                    servicio_detalle += 40;
                    Log.i("Check", String.valueOf(servicio_detalle));

                } else {
                    servicio_detalle -= 40;
                    Log.i("Check", String.valueOf(servicio_detalle));

                }
                break;

        }
    }

    public void Cargar_Peluqueros() {
        JsonObjectRequest objectRequest_peluqueros;
        String url_peluqueros = Constantes.DireccionServidor + "Trabajadores/wsJSONTrabajadores.php?";
        objectRequest_peluqueros = new JsonObjectRequest(Request.Method.GET, url_peluqueros, null, response -> {
            Log.i("Peluquero", response.toString());
            Trabajador trabajador = null;
            JSONArray jsonArrayTrabajador = response.optJSONArray("peluquero");
            trabajadores.clear();

            try {
                for (int i = 0; i < jsonArrayTrabajador.length(); i++) {
                    trabajador = new Trabajador();
                    JSONObject jsonObject = null;
                    jsonObject = jsonArrayTrabajador.getJSONObject(i);

                    trabajador.setIdentificacion(jsonObject.getString("identificacion"));
                    trabajador.setNombre(jsonObject.getString("nombre"));
                    trabajador.setDireccion(jsonObject.getString("direccion"));
                    trabajador.setTelefono(jsonObject.getString("telefono"));
                    trabajador.setContraseña(jsonObject.getString("contrasena"));

                    trabajadores.add(trabajador);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, error -> {
            Log.i("Error", error.toString());
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(objectRequest_peluqueros);
    }

    String identificacion_peluquero,
            fecha_servicio, hora;
    boolean uno = false, dos = false, tres = false;

    public void Dialogo(Double costo, String detalle, ArrayAdapter<CharSequence> dia, ArrayAdapter<CharSequence> tarde, ArrayAdapter<CharSequence> noche) {
        String telefono_cliente = clientes.get(0).getTelefono(),
                detalle_servicio = detalle;
        Double precio_servicio = costo,
                pago_servicio = 0.0;
        String estado_servicio = Estado_Servicio_Espera;

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alert_reserva);

        peluquero1 = dialog.findViewById(R.id.peluquero_v_1);
        peluquero2 = dialog.findViewById(R.id.peluquero_v_2);
        peluquero3 = dialog.findViewById(R.id.peluquero_v_3);

        horario_dia = dialog.findViewById(R.id.horario_dia);
        horario_tarde = dialog.findViewById(R.id.horario_tarde);
        horario_noche = dialog.findViewById(R.id.horario_noche);
        dia_calendario = dialog.findViewById(R.id.dia_reserva);
        horario = dialog.findViewById(R.id.spinner_horario);
        completar_reserva = dialog.findViewById(R.id.completar_reserva);

        peluquero1.setText(trabajadores.get(0).getNombre());
        peluquero2.setText(trabajadores.get(1).getNombre());
        peluquero3.setText(trabajadores.get(2).getNombre());

        peluquero1.setOnClickListener(v -> {
            identificacion_peluquero = String.valueOf(trabajadores.get(0).getIdentificacion());
            uno = true;
        });
        peluquero2.setOnClickListener(v -> {
            identificacion_peluquero = String.valueOf(trabajadores.get(1).getIdentificacion());
            uno = true;

        });
        peluquero3.setOnClickListener(v -> {
            identificacion_peluquero = String.valueOf(trabajadores.get(2).getIdentificacion());
            uno = true;

        });

        //-------------------------------------------------------------------------------------------

        horario_dia.setOnClickListener(v -> {
            horario.setVisibility(View.VISIBLE);
            horario.setAdapter(dia);
            dos = true;
        });
        horario_tarde.setOnClickListener(v -> {
            horario.setVisibility(View.VISIBLE);
            horario.setAdapter(tarde);
            dos = true;

        });
        horario_noche.setOnClickListener(v -> {
            horario.setVisibility(View.VISIBLE);
            horario.setAdapter(noche);
            dos = true;

        });

        dia_calendario.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int año = c.get(Calendar.YEAR);
            int mes = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    fecha_servicio = "" + dayOfMonth + "/" + month + "/" + year;
                    dia_calendario.setText(fecha_servicio);
                    tres = true;
                }
            }, año, mes, d);

            pickerDialog.show();

        });


        horario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hora = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        completar_reserva.setOnClickListener(v -> {
            if (uno && dos && tres) {
                String timepo = fecha_servicio + " - " + hora;
                RegistroReserva(telefono_cliente, identificacion_peluquero, detalle_servicio, String.valueOf(precio_servicio), String.valueOf(pago_servicio), estado_servicio, timepo);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Debes Completar todos los datos", Toast.LENGTH_LONG).show();
            }
        });

        dialog.show();
    }

    public void RegistroReserva(String telefono_cliente, String identificacion, String detalle, String precio, String pago, String estado, String fecha) {
        progressDoalog.show();

        StringRequest stringRequest_registro;
        String url = Constantes.DireccionServidor + "Reservas/wsJSONRegistro.php?";
        stringRequest_registro = new StringRequest(Request.Method.POST, url, response -> {

            progressDoalog.cancel();

            if (response.equals(Estado_Duplicado)) {
                Toast.makeText(getApplicationContext(), "Ya hay una reserva programada para ese dia con esa misma, por favor pruebe con otra hora o con otro dia", Toast.LENGTH_LONG).show();
            }
            if (response.equals(Estado_Registra)) {
                Toast.makeText(getApplicationContext(), "Registrado con exito", Toast.LENGTH_LONG).show();

                TraerReservas(clientes.get(0).getTelefono());
            }
        }, error -> {
            Toast.makeText(getApplicationContext(), "Ocurrio un error en nuestro sistema, intentelo luego", Toast.LENGTH_LONG).show();
            progressDoalog.cancel();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> reserva = new HashMap<>();

                reserva.put("telefono_cliente", telefono_cliente);
                reserva.put("identificacion_peluquero", identificacion);
                reserva.put("detalle_servicio", detalle);
                reserva.put("precio_servicio", precio);
                reserva.put("pago_servicio", pago);
                reserva.put("estado_servicio", estado);
                reserva.put("fecha_servicio", fecha);

                return reserva;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest_registro);
    }

    public void TraerReservas(String telefono) {
        JsonObjectRequest objectRequest_traer;
        String url_traer = Constantes.DireccionServidor + "Reservas/wsJSONReservasCliente.php?telefono="+telefono;
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

                    reserv.setNumero_cliente(jsonObject.getString("telefono_cliente"));
                    reserv.setIdententificacion_peluquero(jsonObject.getString("nombre"));
                    reserv.setDetalle_servicio(jsonObject.getString("detalle_servicio"));
                    reserv.setPrecio_servicio(jsonObject.getString("precio_servicio"));
                    reserv.setPago_servicio(jsonObject.getString("pago_servicio"));
                    reserv.setEstado_servicio(jsonObject.getString("estado_servicio"));
                    reserv.setFecha_servico(jsonObject.getString("fecha_servicio"));

                    reservas.add(reserv);
                }

                adapatador = new AdapatadorReservaClientes(this,reservas);
                recyclerViewReservas.setAdapter(adapatador);
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