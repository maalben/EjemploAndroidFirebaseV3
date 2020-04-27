package com.enterprise.firebaseandroidv_10;

public class PersonasDTO {

    private String id, nombre, apellido, edad, correo;

    public PersonasDTO(String id, String nombre, String apellido, String edad, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.correo = correo;
    }

    public PersonasDTO(){}

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEdad() {
        return edad;
    }

    public String getCorreo() {
        return correo;
    }
}
