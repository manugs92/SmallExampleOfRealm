package com.example.realmapp;


import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Persona extends RealmObject {


    @PrimaryKey
    private String nombre;
    private int edad;
    private String genero;

    @Index
    private int altura;

    public Persona() {}

    public Persona(String nombre, int edad, String genero,int altura) {
        this.nombre=nombre;
        this.edad=edad;
        this.genero=genero;
        this.altura=altura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

}
