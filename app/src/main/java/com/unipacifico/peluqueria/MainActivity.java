package com.unipacifico.peluqueria;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unipacifico.peluqueria.clases.Administrador;
import com.unipacifico.peluqueria.clases.Cliente;
import com.unipacifico.peluqueria.clases.Trabajador;
import com.unipacifico.peluqueria.costantes.Constantes;
import com.unipacifico.peluqueria.vistas.administrador.Administrador_V;
import com.unipacifico.peluqueria.vistas.cliente.Cliente_V;
import com.unipacifico.peluqueria.vistas.trabajador.Trabajador_V;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.unipacifico.peluqueria.costantes.Constantes.*;

public class MainActivity extends AppCompatActivity {

    TextView ir_registro, ir_sesion;
    ScrollView ir_registro_v, ir_sesion_v;
    TextView telefono_inicio_sesion, contrasena_inicio_sesion;
    TextView telefono_registro_cliente, nombre_registro_cliente, apellido_registro_cliente, direccion_registro_cliente, contrasena_registro_cliente;
    Button registrar_cliente, inicio_sesion;
    ArrayList<Cliente> clientes = new ArrayList<>();
    ArrayList<Trabajador> trabaja = new ArrayList<>();
    ArrayList<Administrador> admin = new ArrayList<>();

    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ir_registro = findViewById(R.id.registro_ir);
        ir_sesion = findViewById(R.id.inicio_ir);
        ir_registro_v = findViewById(R.id.regis);
        ir_sesion_v = findViewById(R.id.inici);
        telefono_inicio_sesion = findViewById(R.id.telefono_inicio_sesion);
        contrasena_inicio_sesion = findViewById(R.id.contraseña_inicio_sesion);
        telefono_registro_cliente = findViewById(R.id.telefono_registro_cliente);
        nombre_registro_cliente = findViewById(R.id.nombre_registro_cliente);
        apellido_registro_cliente = findViewById(R.id.apellido_registro_cliente);
        direccion_registro_cliente = findViewById(R.id.direccion_registro_cliente);
        contrasena_registro_cliente = findViewById(R.id.contraseña_registro_cliente);

        progressDoalog = new ProgressDialog(this);
        progressDoalog.setMessage("Espere un momento");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setIndeterminate(true);
        progressDoalog.setCancelable(false);

        ir_registro.setOnClickListener(v -> {
            ir_sesion_v.setVisibility(View.GONE);
            ir_registro_v.setVisibility(View.VISIBLE);
        });
        ir_sesion.setOnClickListener(v -> {
            ir_sesion_v.setVisibility(View.VISIBLE);
            ir_registro_v.setVisibility(View.GONE);
        });

        registrar_cliente = findViewById(R.id.registrar_cliente);

        registrar_cliente.setOnClickListener(v -> {
            if (!(telefono_registro_cliente.length() == 10) || telefono_registro_cliente.equals("") || nombre_registro_cliente.equals("") || apellido_registro_cliente.equals("") || direccion_registro_cliente.equals("") || contrasena_registro_cliente.equals("")) {
                Toast.makeText(getApplicationContext(), "Debes ingresar todos los datos", Toast.LENGTH_LONG).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(aviso_registro)
                        .setPositiveButton("Acepto", (dialog, id) -> {
                            RegistroCliente(telefono_registro_cliente.getText().toString(), nombre_registro_cliente.getText().toString(), apellido_registro_cliente.getText().toString(), direccion_registro_cliente.getText().toString(), contrasena_registro_cliente.getText().toString());
                        }).setNegativeButton("Cancelar", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        inicio_sesion = findViewById(R.id.iniciarsesion);
        inicio_sesion.setOnClickListener(v -> {
            if (!(telefono_inicio_sesion.length() == 10) || telefono_inicio_sesion.equals("") || contrasena_inicio_sesion.equals("")) {
                Toast.makeText(getApplicationContext(), "Debes ingresar todos los datos", Toast.LENGTH_LONG).show();
            } else {
                InicioCliente(telefono_inicio_sesion.getText().toString(), contrasena_inicio_sesion.getText().toString());
            }
        });


    }

    private void RegistroCliente(String telefono, String nombre, String apellido, String direccion, String contrasena) {

        progressDoalog.show();

        clientes.clear();

        StringRequest stringRequest_registro;
        String url_registro = Constantes.DireccionServidor + "Clientes/wsJSONRegistro.php?";
        stringRequest_registro = new StringRequest(Request.Method.POST, url_registro, response -> {

            progressDoalog.cancel();

            if (response.equals(Estado_Duplicado)) {
                Toast.makeText(getApplicationContext(), "El número que trata de registrar ya se encuentra", Toast.LENGTH_LONG).show();
            }
            if (response.equals(Estado_Registra)) {
                clientes.add(new Cliente(telefono, nombre, apellido, direccion, contrasena));
                Toast.makeText(getApplicationContext(), "Registrado con exito", Toast.LENGTH_LONG).show();
                ir_sesion_v.setVisibility(View.VISIBLE);
                ir_registro_v.setVisibility(View.GONE);
                telefono_registro_cliente.setText("");
                nombre_registro_cliente.setText("");
                apellido_registro_cliente.setText("");
                direccion_registro_cliente.setText("");
                contrasena_registro_cliente.setText("");
            }
        }, error -> {
            Toast.makeText(getApplicationContext(), "Ocurrio un error en nuestro sistema, intentelo luego", Toast.LENGTH_LONG).show();
            progressDoalog.cancel();
        }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> usuario = new HashMap<>();
                usuario.put("telefono", telefono);
                usuario.put("nombre", nombre);
                usuario.put("apellido", apellido);
                usuario.put("direccion", direccion);
                usuario.put("contraseña", contrasena);

                return usuario;
            }
        };
        RequestQueue request_registro = Volley.newRequestQueue(getApplicationContext());
        request_registro.add(stringRequest_registro);
    }

    private void InicioCliente(String telefono, String contrasena) {

        progressDoalog.show();
        JsonObjectRequest stringRequest_inicio;
        String url_registro = Constantes.DireccionServidor + "Clientes/wsJSONInicioCliente.php?telefono=" + telefono + "&&contraseña=" + contrasena;
        stringRequest_inicio = new JsonObjectRequest(Request.Method.GET, url_registro, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

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

                        progressDoalog.cancel();

                        Intent intentCliente = new Intent(getBaseContext(), Cliente_V.class);
                        intentCliente.putExtra("cliente", clientes);
                        startActivity(intentCliente);
                        finish();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, error -> {
            Log.i("Error", error.toString());

            String resul = "Noexiste";
            Pattern regex = Pattern.compile("\\b" + Pattern.quote(resul) + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher match = regex.matcher(error.toString());
            if (match.find()) {
                InicioTrabajador(telefono, contrasena);
            }
            progressDoalog.cancel();
        }
        );
        RequestQueue request_inicio = Volley.newRequestQueue(getApplicationContext());
        request_inicio.add(stringRequest_inicio);
    }

    private void InicioTrabajador(String telefono, String contrasena) {

        progressDoalog.show();
        JsonObjectRequest stringRequest_inicio;
        String url_registro = Constantes.DireccionServidor + "Trabajadores/wsJSONInicioTrabajador.php?telefono=" + telefono + "&&contraseña=" + contrasena;
        stringRequest_inicio = new JsonObjectRequest(Request.Method.GET, url_registro, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i("Trabadores", response.toString());

                Trabajador trabajador = null;
                JSONArray jsonArrayTrabajador = response.optJSONArray("trabajador");
                trabaja.clear();

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

                        trabaja.add(trabajador);

                        progressDoalog.cancel();

                        Intent intentTrabajadores = new Intent(getBaseContext(), Trabajador_V.class);
                        intentTrabajadores.putExtra("trabajador", trabaja);
                        startActivity(intentTrabajadores);
                        finish();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, error -> {
            Log.i("Error", error.toString());

            String resul = "Noexiste";
            Pattern regex = Pattern.compile("\\b" + Pattern.quote(resul) + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher match = regex.matcher(error.toString());
            if (match.find()) {
                InicioAdmin(telefono, contrasena);
            }
            progressDoalog.cancel();
        }
        );
        RequestQueue request_inicio = Volley.newRequestQueue(getApplicationContext());
        request_inicio.add(stringRequest_inicio);
    }

    private void InicioAdmin(String telefono, String contrasena) {

        progressDoalog.show();
        JsonObjectRequest stringRequest_inicio;
        String url_registro = Constantes.DireccionServidor + "Administrador/wsJSONInicioAdministrador.php?telefono=" + telefono + "&&contraseña=" + contrasena;
        stringRequest_inicio = new JsonObjectRequest(Request.Method.GET, url_registro, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i("Administrador", response.toString());

                Administrador administrador = null;
                JSONArray jsonArrayAdministrador = response.optJSONArray("administrador");
                trabaja.clear();

                try {
                    for (int i = 0; i < jsonArrayAdministrador.length(); i++) {
                        administrador = new Administrador();
                        JSONObject jsonObject = null;
                        jsonObject = jsonArrayAdministrador.getJSONObject(i);

                        administrador.setIdentificacion(jsonObject.getString("identificacion"));
                        administrador.setTelefono(jsonObject.getString("telefono"));
                        administrador.setContrasena(jsonObject.getString("contrasena"));
                        administrador.setNombre(jsonObject.getString("nombre"));

                        admin.add(administrador);

                        progressDoalog.cancel();

                        Intent intentAdmin = new Intent(getBaseContext(), Administrador_V.class);
                        intentAdmin.putExtra("administrador", trabaja);
                        startActivity(intentAdmin);
                        finish();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, error -> {
            Log.i("Error", error.toString());

            String resul = "Noexiste";
            Pattern regex = Pattern.compile("\\b" + Pattern.quote(resul) + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher match = regex.matcher(error.toString());
            if (match.find()) {

                Toast.makeText(this, "El usuario no se encuentra registrado", Toast.LENGTH_LONG).show();
            }
            progressDoalog.cancel();
        }
        );
        RequestQueue request_inicio = Volley.newRequestQueue(getApplicationContext());
        request_inicio.add(stringRequest_inicio);
    }

}