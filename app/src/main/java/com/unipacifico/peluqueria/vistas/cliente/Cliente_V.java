package com.unipacifico.peluqueria.vistas.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.unipacifico.peluqueria.R;
import com.unipacifico.peluqueria.clases.Cliente;

import java.util.ArrayList;

public class Cliente_V extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente__v);

        ArrayList<Cliente> clientes = (ArrayList<Cliente>) getIntent().getSerializableExtra("cliente");


    }
}