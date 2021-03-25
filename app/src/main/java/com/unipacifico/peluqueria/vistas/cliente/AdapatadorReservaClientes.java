package com.unipacifico.peluqueria.vistas.cliente;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unipacifico.peluqueria.R;
import com.unipacifico.peluqueria.clases.Cliente;
import com.unipacifico.peluqueria.clases.Reservas;
import com.unipacifico.peluqueria.clases.Trabajador;
import com.unipacifico.peluqueria.costantes.Constantes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static com.unipacifico.peluqueria.costantes.Constantes.Estado_Servicio_Cancel;

public class AdapatadorReservaClientes extends RecyclerView.Adapter<AdapatadorReservaClientes.ClientesHolder> {

    List<Reservas> listaReservaCLientes;
    Context context;

    public AdapatadorReservaClientes(Context context, List<Reservas> listaReservaCLientes) {
        this.listaReservaCLientes = listaReservaCLientes;
        this.context = context;
    }


    @NonNull
    @Override
    public AdapatadorReservaClientes.ClientesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reservas, null, false);

        return new ClientesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapatadorReservaClientes.ClientesHolder holder, int position) {
        holder.peluquero.setText(listaReservaCLientes.get(position).getIdententificacion_peluquero());
        holder.servicios.setText(listaReservaCLientes.get(position).getDetalle_servicio());
        holder.precio.setText(listaReservaCLientes.get(position).getPrecio_servicio());
        holder.estado.setText(listaReservaCLientes.get(position).getEstado_servicio());
        holder.fecha.setText(listaReservaCLientes.get(position).getFecha_servico());

        String estado = listaReservaCLientes.get(position).getEstado_servicio();
        String fecha = listaReservaCLientes.get(position).getFecha_servico();

        if (estado.equals("Esperando")) {
            holder.cancelar.setOnClickListener(v -> {
                Borrar(fecha);
            });
        } else {
            holder.cancelar.setVisibility(View.GONE);
        }
    }

    public void Borrar(String fecha) {
        StringRequest objectRequest_peluqueros;
        String url_peluqueros = Constantes.DireccionServidor + "Reservas/wsJSONCancelar.php?fecha="+fecha;
        objectRequest_peluqueros = new StringRequest(Request.Method.GET, url_peluqueros, response -> {
            Log.i("Respuesta",response);
            if (response.equals(Estado_Servicio_Cancel)) {
                Toast.makeText(context, "Peluqueada Cancelada con Exito", Toast.LENGTH_LONG).show();
                notifyDataSetChanged();
            } else if (response.equals("Noexiste")){
                Toast.makeText(context, "Ya cancelaste, por favor vuelve a iniciar sesion para ver los cambios", Toast.LENGTH_LONG).show();
            }

        }, error -> {
            Log.i("Error", error.toString());
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(objectRequest_peluqueros);
    }

    @Override
    public int getItemCount() {
        return listaReservaCLientes.size();
    }

    public class ClientesHolder extends RecyclerView.ViewHolder {
        TextView peluquero, servicios, precio, estado, fecha;
        Button cancelar;

        public ClientesHolder(@NonNull View itemView) {
            super(itemView);

            peluquero = itemView.findViewById(R.id.peluquero_row_clientes);
            servicios = itemView.findViewById(R.id.servicios_row_clientes);
            precio = itemView.findViewById(R.id.precio_row_clientes);
            estado = itemView.findViewById(R.id.estado_row_clientes);
            fecha = itemView.findViewById(R.id.fecha_row_clientes);
            cancelar = itemView.findViewById(R.id.cancelar_row_clientes);
        }
    }
}
