package com.unipacifico.peluqueria.vistas.administrador.reservas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unipacifico.peluqueria.R;
import com.unipacifico.peluqueria.clases.Reservas;

import java.util.List;

public class AdaptadorReservas_Administrador extends RecyclerView.Adapter<AdaptadorReservas_Administrador.Reservados> {

    List<Reservas> listareservas;

    public AdaptadorReservas_Administrador(List<Reservas> listareservas) {
        this.listareservas = listareservas;
    }

    @NonNull
    @Override
    public AdaptadorReservas_Administrador.Reservados onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reservas_admin, null, false);
        return new Reservados(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorReservas_Administrador.Reservados holder, int position) {

        holder.telefono.setText("Telefono Cliente: "+listareservas.get(position).getNumero_cliente());
        holder.identificacion.setText("Identificacion Peluquero: "+listareservas.get(position).getIdententificacion_peluquero());
        holder.detalles.setText(listareservas.get(position).getDetalle_servicio());
        holder.precio.setText("Precio: "+listareservas.get(position).getPrecio_servicio());
        holder.pago.setText("Cobro: "+listareservas.get(position).getPago_servicio());
        holder.estado.setText("Estado: "+listareservas.get(position).getEstado_servicio());
        holder.fecha.setText(listareservas.get(position).getFecha_servico());

    }

    @Override
    public int getItemCount() {
        return listareservas.size();
    }

    public class Reservados extends RecyclerView.ViewHolder {

        TextView telefono, identificacion, detalles, precio, pago, estado, fecha;

        public Reservados(@NonNull View itemView) {
            super(itemView);

            telefono = itemView.findViewById(R.id.telefono_cliente_reservas_admin);
            identificacion = itemView.findViewById(R.id.identificacionpeluquero_reservas_admin);
            detalles = itemView.findViewById(R.id.detalleservicio_reservas_admin);
            precio = itemView.findViewById(R.id.precioservicio_reservas_admin);
            pago = itemView.findViewById(R.id.pagoservicio_reservas_admin);
            estado = itemView.findViewById(R.id.estado_reservas_admin);
            fecha = itemView.findViewById(R.id.fecha_reservas_admin);
        }
    }
}
