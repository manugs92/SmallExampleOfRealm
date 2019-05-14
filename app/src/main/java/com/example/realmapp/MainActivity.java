package com.example.realmapp;


import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializamos realm.
        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("default0.realm")
                .schemaVersion(1) // Must be bumped when the schema changes
                .migration(new MyMigration()) // Migration to run instead of throwing an exception
                .build();
        Realm.setDefaultConfiguration(config);

        //Creamos objeto realm.
        Realm realm = Realm.getDefaultInstance();

        //Indicamos los contactos que queremos cargar en la lista y los almacenamos en el adaptador.
        OrderedRealmCollection orderedRealmCollection = realm.where(Persona.class).findAll().sort("nombre");
        PersonaRealmAdapter personaRealmAdapter = new PersonaRealmAdapter(orderedRealmCollection,true);

        //Identificamos el recyclerview en la vista, y le seteamos una Layout.
        RecyclerView recyclerView = findViewById(R.id.contactos);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        //Seteamos el adaptador al recyclerView.
        recyclerView.setAdapter(personaRealmAdapter);

        //Identificamos el listener del FloatingActionButton y le añadimos un listener.
        FloatingActionButton floatingActionButton = findViewById(R.id.addContact);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddContactActivity.class);
                startActivity(intent);
            }
        });

        //Identificamos el botoón de restablecer filtro de búsqueda.
        Button buttonRestablecer = findViewById(R.id.restablecerBusqueda);

        //Identificamos campos del filtro de búsqueda.
        TextView TVEdad1 = findViewById(R.id.edad1);
        TextView TVEdad2 = findViewById(R.id.edad2);
        CheckBox cbHombre = findViewById(R.id.hombre);
        CheckBox cbMujer = findViewById(R.id.mujer);
        CheckBox cbAmbos = findViewById(R.id.ambos);


        /*Listener del botón de restablecerer, donde seteamos todos los campos de búsqueda a valores nulos/vacios
        y además, seteamos al recyclerView el adaptador por defecto.
         */
        buttonRestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TVEdad1.setText("");
                TVEdad2.setText("");
                cbHombre.setChecked(false);
                cbMujer.setChecked(false);
                cbAmbos.setChecked(false);
                recyclerView.setAdapter(personaRealmAdapter);
            }
        });

        //Identificamos el botón de filtrar búsqueda
        Button buttonFiltrar = findViewById(R.id.filtrarBusqueda);

        //Añadimos listener al CheckBox de hombre (Para evitar seleccionar dos a la vez)
        cbHombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbMujer.setChecked(false);
                cbAmbos.setChecked(false);
            }
        });

        //^Igual
        cbMujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbHombre.setChecked(false);
                cbAmbos.setChecked(false);
            }
        });

        //^Igual
        cbAmbos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbHombre.setChecked(false);
                cbMujer.setChecked(false);
            }
        });

        //Listener del botón filtrar.
        buttonFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creamos las variables para el filtro de la edad, e intentamos leerlas.
                int edad1,edad2;
                try {
                    edad1 = Integer.valueOf(TVEdad1.getText().toString());
                    edad2 = Integer.valueOf(TVEdad2.getText().toString());
                }
                //Si da algún error, pondremos valor 0.
                catch (NumberFormatException nfe) {
                    edad1 = 0;
                    edad2 = 0;
                }

                //Identificamos el estado de los checkBox
                boolean stateCbHombre = cbHombre.isChecked();
                boolean stateCbMujer = cbMujer.isChecked();

                //Creamos la query a realizar.
                RealmQuery<Persona>  query = null;

                //Si el rango de edades es> 0, buscaremos dentro de ese rango.
                if(edad1 > 0 && edad2 > 0) {
                    query = realm.where(Persona.class).between("edad",edad1,edad2);
                }
                //Si el Checkbox de hombre está seleccionado buscaremos por ese género.
                if(stateCbHombre) {
                    if(query != null) {
                        query = query.and().contains("genero","Hombre");
                    }else {
                        query = realm.where(Persona.class).contains("genero","Hombre");
                    }
                }else if(stateCbMujer) {
                    if(query != null) {
                        query = query.and().contains("genero","Mujer");
                    }else {
                        query = realm.where(Persona.class).contains("genero","Mujer");
                    }
                //Si no se ha marcado ningun Checkbox
                }else {
                    if(query!=null) {
                        query = query.sort("nombre");
                      }else {
                        query = realm.where(Persona.class).sort("nombre");
                      }
                  }

                  //Introducimos la query en el resultado, lo introducimos en el adaptador, y lo seteamos al recyclerView.
                  RealmResults<Persona> result = query.findAll();
                  PersonaRealmAdapter personaRealmAdapter = new PersonaRealmAdapter(result,true);
                  recyclerView.setAdapter(personaRealmAdapter);
            }
        });
    }
}
