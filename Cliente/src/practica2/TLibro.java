/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practica2;

import java.io.Serializable;

/**
 *
 * @author usuario
 */
public class TLibro implements Serializable{
    public String Isbn;
    public String Titulo;
    public String Autor;
    public String Pais;
    public String Idioma;
    public int anio;
    public int NoLibros;
    public int NoPrestados;
    public int NoListaEspera;

    public TLibro(String Isbn, String Titulo, String Autor, String Pais, String Idioma, int anio, int NoLibros, int NoPrestados, int NoListaEspera) {
        this.Isbn = Isbn;
        this.Titulo = Titulo;
        this.Autor = Autor;
        this.Pais = Pais;
        this.Idioma = Idioma;
        this.anio = anio;
        this.NoLibros = NoLibros;
        this.NoPrestados = NoPrestados;
        this.NoListaEspera = NoListaEspera;
    }
    
    public TLibro() {
        this.Isbn = "";
        this.Titulo = "";
        this.Autor = "";
        this.Pais = "";
        this.Idioma = "";
        this.anio = 0;
        this.NoLibros = 0;
        this.NoPrestados = 0;
        this.NoListaEspera = 0;
    }
    
    public TLibro(TLibro l) {
        this.Isbn = l.Isbn;
        this.Titulo = l.Titulo;
        this.Autor = l.Autor;
        this.Pais = l.Pais;
        this.Idioma = l.Idioma;
        this.anio = l.anio;
        this.NoLibros = l.NoLibros;
        this.NoPrestados = l.NoPrestados;
        this.NoListaEspera = l.NoListaEspera;
    }
    

}
