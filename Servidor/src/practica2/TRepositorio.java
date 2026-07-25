/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practica2;

import java.util.ArrayList;

/**
 *
 * @author Manuel Jesus Salas
 */
public class TRepositorio {
    public TDatosRepositorio repositorio;
    public ArrayList<TLibro> libros;
    public String nombreFich;

    public TRepositorio() {
        libros = new ArrayList<TLibro>();
    }
}
