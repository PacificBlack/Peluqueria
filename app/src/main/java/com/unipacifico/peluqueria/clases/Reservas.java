package com.unipacifico.peluqueria.clases;

public class Reservas {
    String id_reserva, numero_cliente, idententificacion_peluquero, detalle_servicio, precio_servicio,
            pago_servicio, estado_servicio, fecha_servico;

    public Reservas() {
    }

    public Reservas(String id_reserva, String numero_cliente, String idententificacion_peluquero, String detalle_servicio, String precio_servicio, String pago_servicio, String estado_servicio, String fecha_servico) {
        this.id_reserva = id_reserva;
        this.numero_cliente = numero_cliente;
        this.idententificacion_peluquero = idententificacion_peluquero;
        this.detalle_servicio = detalle_servicio;
        this.precio_servicio = precio_servicio;
        this.pago_servicio = pago_servicio;
        this.estado_servicio = estado_servicio;
        this.fecha_servico = fecha_servico;
    }

    public String getId_reserva() {
        return id_reserva;
    }

    public String getNumero_cliente() {
        return numero_cliente;
    }

    public String getIdententificacion_peluquero() {
        return idententificacion_peluquero;
    }

    public String getDetalle_servicio() {
        return detalle_servicio;
    }

    public String getPrecio_servicio() {
        return precio_servicio;
    }

    public String getPago_servicio() {
        return pago_servicio;
    }

    public String getEstado_servicio() {
        return estado_servicio;
    }

    public String getFecha_servico() {
        return fecha_servico;
    }

    public void setId_reserva(String id_reserva) {
        this.id_reserva = id_reserva;
    }

    public void setNumero_cliente(String numero_cliente) {
        this.numero_cliente = numero_cliente;
    }

    public void setIdententificacion_peluquero(String idententificacion_peluquero) {
        this.idententificacion_peluquero = idententificacion_peluquero;
    }

    public void setDetalle_servicio(String detalle_servicio) {
        this.detalle_servicio = detalle_servicio;
    }

    public void setPrecio_servicio(String precio_servicio) {
        this.precio_servicio = precio_servicio;
    }

    public void setPago_servicio(String pago_servicio) {
        this.pago_servicio = pago_servicio;
    }

    public void setEstado_servicio(String estado_servicio) {
        this.estado_servicio = estado_servicio;
    }

    public void setFecha_servico(String fecha_servico) {
        this.fecha_servico = fecha_servico;
    }
}
