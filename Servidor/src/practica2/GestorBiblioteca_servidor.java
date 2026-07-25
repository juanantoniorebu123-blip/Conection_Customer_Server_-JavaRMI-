package practica2;

import IRemota.GestorBibliotecaIntf;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;
import java.io.RandomAccessFile;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author usuario
 */
public class GestorBiblioteca_servidor extends UnicastRemoteObject implements GestorBibliotecaIntf {//FALTA EL IMPLEMENTS UNICASTREMOTEOBJECT

    private int idAdmin = -1;
    private ArrayList<TLibro> Biblioteca;
    private String path;
    private int nLibros = 0;
    private ArrayList<TRepositorio> repositorio;
    private int campoOrdenacion;

    /**
     *
     */
    public GestorBiblioteca_servidor() throws RemoteException {
        try {
            path = new File(".").getCanonicalPath();
            path += "\\";
            Biblioteca = new ArrayList<TLibro>();
            repositorio = new ArrayList<TRepositorio>();
            
        } catch (IOException ex) {
            Logger.getLogger(GestorBiblioteca_servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int Conexion(String Passwd) throws RemoteException {
        if (idAdmin != -1) {
            return -1;
        }
        if (Passwd.equals("5634989")) {
            Random r = new Random();
            idAdmin = r.nextInt(1, 1000001);
            return idAdmin;
        } else {
            return -2;
        }
    }
    
    public boolean Desconexion(int Ida) throws RemoteException {
        if (Ida == idAdmin) {
            idAdmin = -1;
            return true;
        } else {
            return false;
        }
    }
    
    public int AbrirRepositorio(int Ida, String NomFichero) throws RemoteException {
        if (Ida == idAdmin && idAdmin > 0) {
            
            boolean esta = false;
            int pos = 0;
            while (!esta && pos < repositorio.size()) {
                if (NomFichero.equalsIgnoreCase(repositorio.get(pos).nombreFich)) {
                    esta = true;
                    break;
                } else {
                    pos++;
                }
            }
            
            if (esta) {
                return -2;
            }
            
            try {
                RandomAccessFile fich = new RandomAccessFile(path + NomFichero, "r");
                if (fich == null) {
                    return 0;
                }
                int librosFich = fich.readInt();
                nLibros += librosFich;
                
                TRepositorio tr = new TRepositorio();
                tr.nombreFich = NomFichero;
                TDatosRepositorio rep = new TDatosRepositorio();
                
                rep.NoLibros = librosFich;
                rep.nombre = fich.readUTF();
                rep.direccion = fich.readUTF();
                
                tr.repositorio = rep;
                
                for (int i = 0; i < librosFich; i++) {
                    TLibro libro = new TLibro();
                    libro.Isbn = fich.readUTF();
                    libro.Titulo = fich.readUTF();
                    libro.Autor = fich.readUTF();
                    libro.anio = fich.readInt();
                    libro.Pais = fich.readUTF();
                    libro.Idioma = fich.readUTF();
                    libro.NoLibros = fich.readInt();
                    libro.NoPrestados = fich.readInt();
                    libro.NoListaEspera = fich.readInt();
                    tr.libros.add(libro);
                    Biblioteca.add(libro);
                }
                repositorio.add(tr);
                Ordenar(Ida, campoOrdenacion);
                fich.close();
                return 1;
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(GestorBiblioteca_servidor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GestorBiblioteca_servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return -1;
    }
    
    public int GuardarRepositorio(int Ida, int Repo) throws RemoteException {
        if (Ida == idAdmin && idAdmin > 0) {
            if (Repo == -1) {
                for (int i = 0; i < repositorio.size(); i++) {
                    try {
                        guardarRepositorio(repositorio.get(i));
                        
                    } catch (Exception ex) {
                        Logger.getLogger(GestorBiblioteca_servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                return 1;
            } else if (Repo < 0 || Repo >= repositorio.size()) {
                return -2;
            } else { //El repositorio existe (es uno de los 3)
                try {
                    boolean guarda = guardarRepositorio(repositorio.get(Repo));
                    if (guarda) {
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GestorBiblioteca_servidor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GestorBiblioteca_servidor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(GestorBiblioteca_servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return -1;
        
    }

    //Metodo q usaremos para gardar un repositorio
    private boolean guardarRepositorio(TRepositorio rep) throws Exception {
        RandomAccessFile fich = new RandomAccessFile(path + rep.nombreFich, "rw");
        
        if (fich == null) {
            return false;
        }
        
        fich.writeInt(rep.libros.size());
        fich.writeUTF(rep.repositorio.nombre);
        fich.writeUTF(rep.repositorio.direccion);
        
        for (int j = 0; j < rep.libros.size(); j++) {
            fich.writeUTF(rep.libros.get(j).Isbn);
            fich.writeUTF(rep.libros.get(j).Titulo);
            fich.writeUTF(rep.libros.get(j).Autor);
            fich.writeInt(rep.libros.get(j).anio);
            fich.writeUTF(rep.libros.get(j).Pais);
            fich.writeUTF(rep.libros.get(j).Idioma);
            fich.writeInt(rep.libros.get(j).NoLibros);
            fich.writeInt(rep.libros.get(j).NoPrestados);
            fich.writeInt(rep.libros.get(j).NoListaEspera);
            
        }
        fich.close();
        return true;
    }
    
    public int NRepositorios(int Ida) throws RemoteException {
        
        if (Ida == idAdmin && idAdmin > 0) {
            return repositorio.size();
        } else {
            return -1;
        }
        
    }
    
    public TDatosRepositorio DatosRepositorio(int Ida, int Repo) throws RemoteException {
        if (Ida == idAdmin && idAdmin > 0) {
            if (Repo < 0 || Repo >= repositorio.size()) {
                TDatosRepositorio noExiste = null;
                
                noExiste.NoLibros = 0;
                noExiste.direccion = "";
                noExiste.nombre = "???";
                
                return noExiste;
                
            } else {
                return repositorio.get(Repo).repositorio;
            }
        } else {
            return null;
        }
    }
    
    public int NuevoLibro(int Ida, TLibro Libro, int Repo) throws RemoteException {
        if (Ida == idAdmin && idAdmin > 0) {
            if (Repo >= 0 && Repo < repositorio.size()) {
                boolean iguales = false;
                int i = 0;
                while (!iguales && i < Biblioteca.size()) {
                    if (Libro.Isbn.trim().equalsIgnoreCase(Biblioteca.get(i).Isbn.trim())) {
                        iguales = true;
                    } else {
                        i++;
                    }
                }
                
                if (iguales == true) {
                    return 0;
                } else {
                    repositorio.get(Repo).libros.add(Libro);
                    repositorio.get(Repo).repositorio.NoLibros++;
                    Biblioteca.add(Libro);
                    Ordenar(Ida, campoOrdenacion);
                    return 1;
                }
            } else {
                return -2;
            }
            
        } else {
            return -1;
        }
    }
    
    public int Comprar(int Ida, String Isbn, int NoLibros) throws RemoteException {
        if (Ida == idAdmin && idAdmin > 0) {
            int i = 0, j = 0;
            boolean iguales = false;
            while (iguales == false && i < repositorio.size()) {
                j = 0;
                while (iguales == false && j < repositorio.get(i).libros.size()) {
                    if (repositorio.get(i).libros.get(j).Isbn.equalsIgnoreCase(Isbn)) {
                        iguales = true;
                    } else {
                        j++;
                    }
                }
                if (!iguales) {
                    i++;
                }
            }
            
            if (iguales == false) {
                return 0;
            } else {
                repositorio.get(i).libros.get(j).NoLibros += NoLibros;
                while (repositorio.get(i).libros.get(j).NoListaEspera > 0 && repositorio.get(i).libros.get(j).NoLibros > 0) {
                    repositorio.get(i).libros.get(j).NoListaEspera--;
                    repositorio.get(i).libros.get(j).NoLibros--;
                }
                Ordenar(Ida, campoOrdenacion);
                return 1;
            }
        } else {
            return -1;
        }
    }
    
    public int Retirar(int Ida, String Isbn, int NoLibros) throws RemoteException {
        if (Ida == idAdmin && idAdmin > 0) {
            int i = 0, j = 0;
            boolean iguales = false;
            while (iguales == false && i < repositorio.size()) {
                j = 0;
                while (iguales == false && j < repositorio.get(i).libros.size()) {
                    if (repositorio.get(i).libros.get(j).Isbn.equalsIgnoreCase(Isbn)) {
                        iguales = true;
                    } else {
                        j++;
                    }
                }
                if (!iguales) {
                    i++;
                }
            }
            
            if (iguales == false) {
                return 0;
            } else {
                if (repositorio.get(i).libros.get(j).NoLibros >= NoLibros) {
                    repositorio.get(i).libros.get(j).NoLibros -= NoLibros;
                    
                    Ordenar(Ida, campoOrdenacion);
                    return 1;
                } else {
                    return 2;
                }
                
            }
        } else {
            return -1;
        }
    }
    
    public boolean Ordenar(int Ida, int Campo) throws RemoteException {
        if (Ida == idAdmin && idAdmin > 0) {
            campoOrdenacion = Campo;
            
            for (int i = 0; i < repositorio.size(); i++) {
                for (int j = 0; j < repositorio.get(i).libros.size() - 1; j++) { // Bubble Sort
                    for (int k = 0; k < repositorio.get(i).libros.size() - 1 - j; k++) {
                        if (libroPorCampo(Campo, i, k, k + 1)) {
                            TLibro temp = repositorio.get(i).libros.get(k);
                            repositorio.get(i).libros.set(k, repositorio.get(i).libros.get(k + 1));
                            repositorio.get(i).libros.set(k + 1, temp);
                        }
                    }
                }
            }
            for (int j = 0; j < Biblioteca.size() - 1; j++) { // Bubble Sort
                for (int k = 0; k < Biblioteca.size() - 1 - j; k++) {
                    if (libroPorCampo(Campo, k, k + 1)) {
                        TLibro temp = Biblioteca.get(k);
                        Biblioteca.set(k, Biblioteca.get(k + 1));
                        Biblioteca.set(k + 1, temp);
                    }
                }
            }
            
            return true;
        } else {
            return false;
        }
    }
    
    public boolean libroPorCampo(int Campo, int rep, int l2, int l1) { // compara los campos de los libros
        boolean salida = false;
        TLibro L1 = repositorio.get(rep).libros.get(l1);
        TLibro L2 = repositorio.get(rep).libros.get(l2);
        switch (Campo) {
            case 0:
                if (L1.Isbn.compareToIgnoreCase(L2.Isbn) < 0) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
            case 1:
                if (L1.Titulo.compareToIgnoreCase(L2.Titulo) < 0) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
            case 2:
                if (L1.Autor.compareToIgnoreCase(L2.Autor) < 0) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
            case 3:
                if (L1.anio < L2.anio) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
            case 4:
                if (L1.Pais.compareToIgnoreCase(L2.Pais) < 0) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
            case 5:
                if (L1.Idioma.compareToIgnoreCase(L2.Idioma) < 0) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
            case 6:
                if (L1.NoLibros < L2.NoLibros) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
            case 7:
                if (L1.NoPrestados < L2.NoPrestados) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
            case 8:
                if (L1.NoListaEspera < L2.NoListaEspera) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
        }
        return salida;
    }
    
    public boolean libroPorCampo(int Campo, int l2, int l1) { // compara los campos de los libros
        boolean salida = false;
        TLibro L1 = Biblioteca.get(l1);
        TLibro L2 = Biblioteca.get(l2);
        switch (Campo) {
            case 0:
                if (L1.Isbn.compareToIgnoreCase(L2.Isbn) < 0) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
            case 1:
                if (L1.Titulo.compareToIgnoreCase(L2.Titulo) < 0) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
            case 2:
                if (L1.Autor.compareToIgnoreCase(L2.Autor) < 0) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
            case 3:
                if (L1.anio < L2.anio) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
            case 4:
                if (L1.Pais.compareToIgnoreCase(L2.Pais) < 0) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
            case 5:
                if (L1.Idioma.compareToIgnoreCase(L2.Idioma) < 0) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
            case 6:
                if (L1.NoLibros < L2.NoLibros) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
            case 7:
                if (L1.NoPrestados < L2.NoPrestados) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
            case 8:
                if (L1.NoListaEspera < L2.NoListaEspera) {
                    salida = true;
                } else {
                    salida = false;
                }
                break;
        }
        return salida;
    }
    
    public int NLibros(int Repo) throws RemoteException {
        if (Repo >= 0 && Repo < repositorio.size()) {
            return repositorio.get(Repo).repositorio.NoLibros;
        } else if (Repo == -1) {
            return Biblioteca.size();
        } else {
            return -1;
        }
    }
    
    public int Buscar(int Ida, String Isbn) throws RemoteException {
        if (Ida == idAdmin && idAdmin > 0) {
            int i = 0, j = 0;
            boolean iguales = false;
            while (iguales == false && i < Biblioteca.size()) {
                if (Isbn.equalsIgnoreCase(Biblioteca.get(i).Isbn)) {
                    iguales = true;
                } else {
                    i++;
                }
            }
            if (iguales == false) {
                return -2;
            } else {
                return i;
            }
        } else {
            return -1;
        }
    }
    
    public TLibro Descargar(int Ida, int Repo, int Pos) throws RemoteException {
        if (Ida == idAdmin && idAdmin > 0) {
            if (Repo >= 0 && Repo < repositorio.size()) {
                if (Pos <= repositorio.get(Repo).libros.size() && Pos >= 0) {
                    return repositorio.get(Repo).libros.get(Pos);
                } else {
                    return null;
                }
            } else {
                if (Repo == -1 && Pos <= Biblioteca.size() && Pos >= 0) {
                    return Biblioteca.get(Pos);
                } else {
                    return null;
                }
            }
        } else {//El ida no esta en el servidor

            if (Repo >= 0 && Repo < repositorio.size()) {
                if (Pos <= repositorio.get(Repo).libros.size() && Pos >= 0) {
                    TLibro libro = new TLibro(repositorio.get(Repo).libros.get(Pos));
                    libro.NoListaEspera = 0;
                    libro.NoPrestados = 0;
                    return libro;
                } else {
                    return null;
                }
            } else {
                if (Repo == -1 && Pos <= Biblioteca.size() && Pos >= 0) {
                    TLibro libro = new TLibro(Biblioteca.get(Pos));
                    libro.NoListaEspera = 0;
                    libro.NoPrestados = 0;
                    return libro;
                } else {
                    return null;
                }
            }
        }
    }
    
    public int Prestar(int Pos) throws RemoteException {
        
        int i = 0, j = 0, k = 0;
        boolean iguales = false;
        
        if (Biblioteca.size() > Pos && Pos >= 0) {
            
            if (Biblioteca.get(Pos).NoLibros == 0) { 
            //esto es con repositorios, con biblioteca seria Biblioteca.get(Pos).NoListaEspera++; y llamamos al ordenar
                while (i < repositorio.size() && !iguales) { //miramos si la pos de biblioteca coincide con algun isbn de algun libro de un repositorio (trabajamos con repositorio ya que al hacer ++ automaticamente se hace en biblioteca pq son el mismo objeto libro)
                    j = 0;
                    while (j < repositorio.get(i).libros.size() - 1 && !iguales) { // Bubble Sort
                        k = 0;
                        while (k < repositorio.get(i).libros.size() - 1 - j && !iguales) {
                            if (Biblioteca.get(Pos).Isbn.equalsIgnoreCase(repositorio.get(i).libros.get(k).Isbn)) {
                                iguales = true;
                                repositorio.get(i).libros.get(k).NoListaEspera++;
                                Ordenar(idAdmin, campoOrdenacion);
                            } else {
                                k++;
                            }
                        }
                        j++;
                    }
                    i++;
                }
                
                return 0;
            } else {
                while (i < repositorio.size() && !iguales) { //igual q arriba
                    j = 0;
                    while (j < repositorio.get(i).libros.size() - 1 && !iguales) {
                        k = 0;
                        while (k < repositorio.get(i).libros.size() - 1 - j && !iguales) {
                            if (Biblioteca.get(Pos).Isbn.equalsIgnoreCase(repositorio.get(i).libros.get(k).Isbn)) {
                                iguales = true;
                                repositorio.get(i).libros.get(k).NoLibros--;
                                repositorio.get(i).libros.get(k).NoPrestados++;
                                Ordenar(idAdmin, campoOrdenacion);
                            } else {
                                k++;
                            }
                        }
                        j++;
                    }
                    i++;
                }
                
                return 1;
            }
        } else {
            return -1;
        }
    }
    
    public int Devolver(int Pos) throws RemoteException {
        
        int i = 0, j = 0, k = 0;
        boolean iguales = false;
        
        if (Biblioteca.size() > Pos && Pos >= 0) {
            if (Biblioteca.get(Pos).NoPrestados == 0) {
                return 2;
            } else if (Biblioteca.get(Pos).NoListaEspera > 0) {
                Biblioteca.get(Pos).NoListaEspera--;
                Ordenar(idAdmin, campoOrdenacion);
                
                return 0;
            } else {
                Biblioteca.get(Pos).NoLibros++;
                Biblioteca.get(Pos).NoPrestados--;
                Ordenar(idAdmin, campoOrdenacion);
                
                return 1;
            }
        } else {
            return -1;
        }
    }
}
