package com.unipacifico.peluqueria.vistas.administrador.clientes;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unipacifico.peluqueria.R;
import com.unipacifico.peluqueria.clases.Cliente;
import com.unipacifico.peluqueria.costantes.Constantes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.unipacifico.peluqueria.costantes.Constantes.Estado_Eliminado;
import static com.unipacifico.peluqueria.costantes.Constantes.Estado_Modificar;

public class AdaptadorClientes_Admin extends RecyclerView.Adapter<AdaptadorClientes_Admin.Admin> {

    List<Cliente> listaClientes;
    Context context;

    public AdaptadorClientes_Admin(List<Cliente> listaClientes, Context context) {
        this.listaClientes = listaClientes;
        this.context = context;
    }

    @NonNull
    @Override
    public Admin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_clientes, null, false);
        return new Admin(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Admin holder, int position) {

        holder.telefono.setText(listaClientes.get(position).getTelefono());
        holder.nombre.setText(listaClientes.get(position).getNombre());
        holder.apellido.setText(listaClientes.get(position).getApellido());
        holder.direccion.setText(listaClientes.get(position).getDireccion());
        holder.contrasena.setText(listaClientes.get(position).getContraseña());
        holder.editar.setOnClickListener(v -> {
            DialogoEditar(listaClientes.get(position).getTelefono(),
                    listaClientes.get(position).getNombre(),
                    listaClientes.get(position).getApellido(),
                    listaClientes.get(position).getDireccion(),
                    listaClientes.get(position).getContraseña());
        });
        holder.eliminar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Esta seguro de que desea elimanar a " + listaClientes.get(position).getNombre())
                    .setPositiveButton("Si", (dialog, id) -> {
                        Eliminar(listaClientes.get(position).getTelefono());
                    }).setNegativeButton("No", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void Eliminar(String telefono) {
        StringRequest stringRequest;
        String url_cita = Constantes.DireccionServidor + "Administrador/wsJSONEliminarCliente.php?telefono=" + telefono;
        stringRequest = new StringRequest(Request.Method.GET, url_cita, response -> {
            Log.i("Estado", response.toString());

            if (response.equals(Estado_Eliminado)) {
                Toast.makeText(context, "Eliminado con Exito", Toast.LENGTH_LONG).show();
                notifyDataSetChanged();

            } else {
                Toast.makeText(context, "Ya lo eliminaste, reinicia la ventana para ver los cambios", Toast.LENGTH_LONG).show();
            }
        }, error -> {
            Log.i("Error", error.toString());
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    EditText telef, nom, ape, dire, cont;
    Button modi;

    private void DialogoEditar(String telefono, String nombre, String apellido, String direccion, String contraseña) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_modificar_cliente);

        telef = dialog.findViewById(R.id.telefono_cliente_alert);
        nom = dialog.findViewById(R.id.nombre_cliente_alert);
        ape = dialog.findViewById(R.id.apellido_cliente_alert);
        dire = dialog.findViewById(R.id.direccion_cliente_alert);
        cont = dialog.findViewById(R.id.contrasena_cliente_alert);
        modi = dialog.findViewById(R.id.editar_cliente_alert);

        telef.setText(telefono);
        nom.setText(nombre);
        ape.setText(apellido);
        dire.setText(direccion);
        cont.setText(contraseña);

        modi.setOnClickListener(v -> {
            if (telef.getText().toString().equals("") ||
                    nom.getText().toString().equals("") ||
                    ape.getText().toString().equals("") ||
                    dire.getText().toString().equals("") ||
                    cont.getText().toString().equals("")) {

                Toast.makeText(context, "Debes ingresar todos los datos", Toast.LENGTH_LONG).show();
            } else {

                Modificar(telefono, telef.getText().toString(),
                        nom.getText().toString(),
                        ape.getText().toString(),
                        dire.getText().toString(),
                        cont.getText().toString());
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    private void Modificar(String teleori, String telefono, String nombre, String apellido, String direccion, String contraseña) {

        StringRequest stringRequest_registro;
        String url_registro = Constantes.DireccionServidor + "Administrador/wsJSONModificarCliente.php?";
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
                usuario.put("original", teleori);
                usuario.put("telefono", telefono);
                usuario.put("nombre", nombre);
                usuario.put("apellido", apellido);
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
        return listaClientes.size();
    }

    public class Admin extends RecyclerView.ViewHolder {
        TextView telefono, nombre, apellido, direccion, contrasena;
        Button editar, eliminar;

        public Admin(@NonNull View itemView) {
            super(itemView);

            telefono = itemView.findViewById(R.id.telefono_row_clientes_administrador);
            nombre = itemView.findViewById(R.id.nombre_row_clientes_administrador);
            apellido = itemView.findViewById(R.id.apellido_row_clientes_administrador);
            direccion = itemView.findViewById(R.id.direccion_row_clientes_administrador);
            contrasena = itemView.findViewById(R.id.contrasena_row_clientes_administrador);
            editar = itemView.findViewById(R.id.editar_row_clientes_administrador);
            eliminar = itemView.findViewById(R.id.eliminar_row_clientes_administrador);
        }
    }
}
