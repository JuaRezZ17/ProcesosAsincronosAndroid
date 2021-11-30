package com.example.peliculaspgv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Configuration extends AppCompatActivity {

    private EditText et1, et2, et3;
    private Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        setTitle("Peliculas");

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        b1 = findViewById(R.id.b1);

        //Creamos el listener para guardar la nueva configuración de API.
        b1.setOnClickListener(e-> {

            MainActivity.API_KEY = et1.getText().toString();
            MainActivity.endPointPeliculas = et2.getText().toString();
            System.out.println(MainActivity.endPointPeliculas);
            Creditos.urlCreditos = et3.getText().toString();

            Toast t = Toast.makeText(getApplicationContext(), "Configuración guardada correctamente.", Toast.LENGTH_SHORT);
            t.show();

            finish();
        });
    }
}