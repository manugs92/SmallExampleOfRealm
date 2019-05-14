package com.example.realmapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.realm.Realm;
import io.realm.RealmQuery;

public class EditContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        final EditText ETnombre = findViewById(R.id.nombrePersona);
        final EditText ETedad = findViewById(R.id.edadPersona);
        final EditText ETgenero = findViewById(R.id.generoPersona);
        final EditText ETaltura = findViewById(R.id.alturaPersona);

        Realm realm = Realm.getDefaultInstance();

        Intent intent = getIntent();
        String nombre = intent.getStringExtra("Nombre");
        RealmQuery<Persona> query = realm.where(Persona.class).contains("nombre",nombre);
        Persona persona = query.findFirst();

        if(persona != null) {
            int edad = persona.getEdad();
            String genero = persona.getGenero();
            int altura = persona.getAltura();

            ETnombre.setText(nombre);
            ETedad.setText(String.valueOf(edad));
            ETgenero.setText(genero);
            ETaltura.setText(String.valueOf(altura));
        }

        Button guardarCambios = findViewById(R.id.guardarCambios);

        guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int edad = Integer.valueOf(ETedad.getText().toString());
                int altura = Integer.valueOf(ETaltura.getText().toString());
                Persona persona = new Persona(ETnombre.getText().toString(),edad,ETgenero.getText().toString(),altura);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(persona);
                realm.commitTransaction();
                finish();
            }
        });

        Button volverAtras = findViewById(R.id.volverAtras);
        volverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
