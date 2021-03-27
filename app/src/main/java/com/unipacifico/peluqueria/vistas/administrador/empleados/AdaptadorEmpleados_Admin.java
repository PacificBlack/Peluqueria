package com.unipacifico.peluqueria.vistas.administrador.empleados;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unipacifico.peluqueria.R;
import com.unipacifico.peluqueria.clases.Trabajador;
import com.unipacifico.peluqueria.costantes.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.unipacifico.peluqueria.costantes.Constantes.Estado_Modificar;
import static com.unipacifico.peluqueria.costantes.Constantes.Estado_Servicio_Cancel;
import static com.unipacifico.peluqueria.costantes.Constantes.aviso_registro;

public class AdaptadorEmpleados_Admin extends RecyclerView.Adapter<AdaptadorEmpleados_Admin.Empleados> {

    List<Trabajador> listaTrabajador;
    Context context;

    public AdaptadorEmpleados_Admin(List<Trabajador> listaTrabajador, Context context) {
        this.listaTrabajador = listaTrabajador;
        this.context = context;
    }


    @NonNull
    @Override
    public Empleados onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_clientes, null, false);
        return new Empleados(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Empleados holder, int position) {

        holder.telefono.setText(listaTrabajador.get(position).getTelefono());
        holder.nombre.setText(listaTrabajador.get(position).getNombre());
        holder.identificacion.setText(listaTrabajador.get(position).getIdentificacion());
        holder.direccion.setText(listaTrabajador.get(position).getDireccion());
        holder.contrasena.setText(listaTrabajador.get(position).getContraseña());
        holder.titulo.setText("Empleado");
        holder.editar.setOnClickListener(v -> {
            DialogoEditar(listaTrabajador.get(position).getTelefono(),
                    listaTrabajador.get(position).getNombre(),
                    listaTrabajador.get(position).getIdentificacion(),
                    listaTrabajador.get(position).getDireccion(),
                    listaTrabajador.get(position).getContraseña());
        });
        holder.eliminar.setText("Dinero Generado");
        holder.eliminar.setOnClickListener(v -> {
            Ganancias(listaTrabajador.get(position).getIdentificacion());
        });
    }

    private void Ganancias(String identificacion) {
        JsonObjectRequest objectRequest_peluqueros;
        String url_peluqueros = Constantes.DireccionServidor + "Administrador/wsJSONGananciaE.php?id="+identificacion;
        objectRequest_peluqueros = new JsonObjectRequest(Request.Method.GET, url_peluqueros, null,response -> {

            JSONArray jsonArray = response.optJSONArray("ganancia");
            try {

                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(0);

                String precio = jsonObject.getString("SUM(precio_servicio)");
                String pago = jsonObject.getString("SUM(pago_servicio)");


                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                if (precio.equals("null")){
                    builder.setMessage("Ganancias segun el sistema:"+0+"\n"+
                            "Ganancias entregadas por el empleado:"+0)
                            .setPositiveButton("Acepto", (dialog, id) -> {
                                dialog.dismiss();
                            });
                }else {
                    builder.setMessage("Ganancias segun el sistema:"+precio+"\n"+
                            "Ganancias entregadas por el empleado:"+pago)
                            .setPositiveButton("Acepto", (dialog, id) -> {
                                dialog.dismiss();
                            });
                }


                AlertDialog dialog = builder.create();
                dialog.show();

            }catch (Exception e){
                e.printStackTrace();
            }

        }, error -> {
            Log.i("Error", error.toString());
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(objectRequest_peluqueros);
    }

    EditText telef, nom, iden, dire, cont;
    Button modi;

    private void DialogoEditar(String telefono, String nombre, String identificacion, String direccion, String contraseña) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_modificar_cliente);

        telef = dialog.findViewById(R.id.telefono_cliente_alert);
        nom = dialog.findViewById(R.id.nombre_cliente_alert);
        iden = dialog.findViewById(R.id.apellido_cliente_alert);
        dire = dialog.findViewById(R.id.direccion_cliente_alert);
        cont = dialog.findViewById(R.id.contrasena_cliente_alert);
        modi = dialog.findViewById(R.id.editar_cliente_alert);

        telef.setText(telefono);
        nom.setText(nombre);
        iden.setText(identificacion);
        dire.setText(direccion);
        cont.setText(contraseña);

        modi.setOnClickListener(v -> {
            if (telef.getText().toString().equals("") ||
                    nom.getText().toString().equals("") ||
                    iden.getText().toString().equals("") ||
                    dire.getText().toString().equals("") ||
                    cont.getText().toString().equals("")) {

                Toast.makeText(context, "Debes ingresar todos los datos", Toast.LENGTH_LONG).show();
            } else {

                Modificar(identificacion, telef.getText().toString(),
                        nom.getText().toString(),
                        iden.getText().toString(),
                        dire.getText().toString(),
                        cont.getText().toString());
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    private void Modificar(String identiori, String telefono, String nombre, String identificacion, String direccion, String contraseña) {

        StringRequest stringRequest_registro;
        String url_registro = Constantes.DireccionServidor + "Administrador/wsJSONModificarEmpleado.php?";
        stringRequest_registro = new StringRequest(Request.Method.POST, url_registro, response -> {

            if (response.equals(Estado_Modificar)) {
                Toast.makeText(context, "Modificado con exito", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "No se pudo modificar, intentalo de nuevo", Toast.LENGTH_LONG).show();
            }
        }, error -> {
            Toast.makeText(context, "Ocurrio un error en nuestro sistema, intentelo luego", Toast.LENGTH_LONG).show();
        }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> usuario = new HashMap<>();
                usuario.put("original", identiori);
                usuario.put("telefono", telefono);
                usuario.put("nombre", nombre);
                usuario.put("identificacion", identificacion);
                usuario.put("direccion", direccion);
                usuario.put("contraseña", contraseña);

                return usuario;
            }
        };
        RequestQueue request_registro = Volley.newRequestQueue(context);
        request_registro.add(stringRequest_registro);
    }

    @Override
    public int getItemCount() {
        return listaTrabajador.size();
    }

    public class Empleados extends RecyclerView.ViewHolder {
        TextView telefono, nombre, identificacion, direccion, contrasena, titulo;
        Button editar, eliminar;

        public Empleados(@NonNull View itemView) {
            super(itemView);
            telefono = itemView.findViewById(R.id.telefono_row_clientes_administrador);
            nombre = itemView.findViewById(R.id.nombre_row_clientes_administrador);
            identificacion = itemView.findViewById(R.id.apellido_row_clientes_administrador);
            direccion = itemView.findViewById(R.id.direccion_row_clientes_administrador);
            contrasena = itemView.findViewById(R.id.contrasena_row_clientes_administrador);
            editar = itemView.findViewById(R.id.editar_row_clientes_administrador);
            eliminar = itemView.findViewById(R.id.eliminar_row_clientes_administrador);
            titulo = itemView.findViewById(R.id.titolo);
        }
    }
}
