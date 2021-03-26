package com.unipacifico.peluqueria.vistas.trabajador;

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
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.unipacifico.peluqueria.R;
import com.unipacifico.peluqueria.clases.Reservas;
import com.unipacifico.peluqueria.costantes.Constantes;

import java.util.List;

import static com.unipacifico.peluqueria.costantes.Constantes.Estado_Servicio_Cancel;

public class AdaptadorCitasTrabajador extends RecyclerView.Adapter<AdaptadorCitasTrabajador.Citas> {

    List<Reservas> listaReservaTrabajador;
    Context context;

    public AdaptadorCitasTrabajador(List<Reservas> listaReservaTrabajador, Context context) {
        this.listaReservaTrabajador = listaReservaTrabajador;
        this.context = context;
    }


    @NonNull
    @Override
    public AdaptadorCitasTrabajador.Citas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_citas,null,false);
        return new Citas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorCitasTrabajador.Citas holder, int position) {

        holder.nombrecliente_trabajador.setText("Numero del Cliente: "+listaReservaTrabajador.get(position).getNumero_cliente());
        holder.servicioscliente_trabajador.setText("Servicios: "+listaReservaTrabajador.get(position).getDetalle_servicio());
        holder.preciocliente_trabajador.setText("Valor a cobrar: "+listaReservaTrabajador.get(position).getPrecio_servicio());
        holder.estadocliente_trabajador.setText(listaReservaTrabajador.get(position).getEstado_servicio());
        holder.fechacliente_trabajador.setText(listaReservaTrabajador.get(position).getFecha_servico());

        String estado = listaReservaTrabajador.get(position).getEstado_servicio();
        String id = listaReservaTrabajador.get(position).getId_reserva();
        if (estado.equals("Esperando")){
            holder.terminar_trabajadores.setOnClickListener(v -> {
                Dialogo(id);
            });
        }else{
            holder.terminar_trabajadores.setVisibility(View.GONE);
        }

    }

    EditText cobra;
    Button despachar;
    private void Dialogo(String id) {

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_despachar);

        cobra = dialog.findViewById(R.id.precio_despachar_trabajador);
        despachar = dialog.findViewById(R.id.despachar_trabajador);

        despachar.setOnClickListener(v->{
            if (cobra.getText().toString().equals("")){
                Toast.makeText(context,"Debes ingresar el valor cobrado por el servicio",Toast.LENGTH_LONG).show();

            }else{
                Modificar(id,cobra.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    private void Modificar(String id, String total) {
        StringRequest stringRequest;
        String url_cita = Constantes.DireccionServidor + "Reservas/wsJSONCancelarCita.php?id="+id+"&&precio="+total;
        stringRequest = new StringRequest(Request.Method.GET,url_cita, response -> {
            Log.i("Estado",response.toString());
            if (response.equals(Estado_Servicio_Cancel)){
                Toast.makeText(context, "Cliente despachado con Exito", Toast.LENGTH_LONG).show();
                notifyDataSetChanged();

            }else {
                Toast.makeText(context, "Ya despachaste al cliente, por favor vuelve a iniciar sesion para ver los cambios", Toast.LENGTH_LONG).show();
            }
        }, error -> {
            Log.i("Error",error.toString());
        });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return listaReservaTrabajador.size();
    }

    public class Citas extends RecyclerView.ViewHolder {
        TextView nombrecliente_trabajador, servicioscliente_trabajador,preciocliente_trabajador,estadocliente_trabajador,fechacliente_trabajador;
        Button terminar_trabajadores;

        public Citas(@NonNull View itemView) {
            super(itemView);

            nombrecliente_trabajador = itemView.findViewById(R.id.cliente_row_trabajador);
            servicioscliente_trabajador = itemView.findViewById(R.id.servicios_row_trabajador);
            preciocliente_trabajador = itemView.findViewById(R.id.precio_row_trabajadores);
            estadocliente_trabajador = itemView.findViewById(R.id.estado_row_trabajadores);
            fechacliente_trabajador = itemView.findViewById(R.id.fecha_row_trabajadores);
            terminar_trabajadores = itemView.findViewById(R.id.terminar_row_trabajadores);
        }
    }
}
