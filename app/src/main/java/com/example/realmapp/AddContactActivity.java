package com.example.realmapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.realm.Realm;

public class AddContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        final EditText ETnombre = findViewById(R.id.nombrePersona);
        final EditText ETedad = findViewById(R.id.edadPersona);
        final EditText ETgenero = findViewById(R.id.generoPersona);
        final EditText ETAltura = findViewById(R.id.alturaPersona);

        Realm realm = Realm.getDefaultInstance();

        Button addButton = findViewById(R.id.anadirPersona);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = ETnombre.getText().toString();
                int edad = Integer.valueOf(ETedad.getText().toString());
                String genero = ETgenero.getText().toString();
                int altura = Integer.valueOf(ETAltura.getText().toString());

                Persona persona = new Persona(nombre,edad,genero,altura);

                realm.beginTransaction();
                realm.copyToRealm(persona);
                realm.commitTransaction();

                finish();
            }
        });
    }
}
