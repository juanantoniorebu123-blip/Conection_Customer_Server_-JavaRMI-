package practica2;

import java.io.Serializable;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author usuario
 */
public class TDatosRepositorio implements Serializable{
    public String nombre;
    public String direccion;
    public int NoLibros;

    public TDatosRepositorio(String nombre, String direccion, int NoLibros) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.NoLibros = NoLibros;
    }
    
    public TDatosRepositorio() {
        this.nombre = "";
        this.direccion = "";
        this.NoLibros = 0;
    }
}
