package com.example.peliculaspgv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Seleccion extends AppCompatActivity {

    private ImageView iv;
    private TextView tv1, tv2;
    private Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion);
        Bundle bundle = this.getIntent().getExtras();
        setTitle(bundle.getString("Titulo"));

        iv = findViewById(R.id.iv);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        b1 = findViewById(R.id.b1);

        //Creamos la ventana con la información pasada del MainActivity.
        Picasso.get()
                .load("https://image.tmdb.org/t/p/w185" + bundle.getString("Imagen"))
                .into(iv);
        tv1.setText(bundle.getString("Titulo"));
        tv2.setText(bundle.getString("Texto"));

        //Creamos el listener para acceder a los creditos de la película.
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Seleccion.this, Creditos.class);
                Bundle b = new Bundle();
                b.putInt("IDPelicula", bundle.getInt("ID"));
                b.putString("Titulo", bundle.getString("Titulo"));
                i.putExtras(b);
                startActivity(i);
            }
        });
    }
}