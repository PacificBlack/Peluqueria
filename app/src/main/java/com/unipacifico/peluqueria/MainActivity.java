package com.unipacifico.peluqueria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView ir_registro, ir_sesion;
    ScrollView ir_registro_v, ir_sesion_v;
    TextView telefono_inicio_sesion, contraseña_inicio_sesion;
    TextView telefono_registro_cliente, nombre_registro_cliente, apellido_registro_cliente, direccion_registro_cliente, contraseña_registro_cliente;
    Button registrar_cliente, inicio_sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ir_registro = findViewById(R.id.registro_ir);
        ir_sesion = findViewById(R.id.inicio_ir);
        ir_registro_v = findViewById(R.id.regis);
        ir_sesion_v = findViewById(R.id.inici);
        telefono_inicio_sesion = findViewById(R.id.telefono_inicio_sesion);
        contraseña_inicio_sesion = findViewById(R.id.contraseña_inicio_sesion);
        telefono_registro_cliente = findViewById(R.id.telefono_registro_cliente);
        nombre_registro_cliente = findViewById(R.id.nombre_registro_cliente);
        apellido_registro_cliente = findViewById(R.id.apellido_registro_cliente);
        direccion_registro_cliente = findViewById(R.id.direccion_registro_cliente);
        contraseña_registro_cliente = findViewById(R.id.contraseña_registro_cliente);

        ir_registro.setOnClickListener(v -> {
            ir_sesion_v.setVisibility(View.GONE);
            ir_registro_v.setVisibility(View.VISIBLE);
        });
        ir_sesion.setOnClickListener(v -> {
            ir_sesion_v.setVisibility(View.VISIBLE);
            ir_registro_v.setVisibility(View.GONE);
        });
    }
}