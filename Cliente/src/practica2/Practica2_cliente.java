/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package practica2;

import IRemota.GestorBibliotecaIntf;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juana
 */
public class Practica2_cliente {

    /**
     * @param args the command line arguments
     */
    private static String Ajustar(String S, int Ancho) {
        byte v[] = S.getBytes();
        int c = 0;
        int len = 0;
        int uin;
        for (int i = 0; i < v.length; i++) {
            uin = Byte.toUnsignedInt(v[i]);
            if (uin > 128) {
                c++;
            }
        }

        len = c / 2;

        for (int i = 0; i < len; i++) {
            S = S + " ";
        }

        return S;
    }

    public static void Mostrar(int Pos, boolean Cabecera, TLibro l) {
        if (Cabecera) {
            System.out.println(String.format("%-5s%-58s%-18s%-4s%-4s%-4s", "POS", "TITULO", "ISBN", "DIS", "PRE", "RES"));
            System.out.println(String.format("     %-30s%-28s%-12s", "AUTOR", "PAIS (IDIOMA)", "AÑO"));
            for (int i = 0; i < 93; i++) {
                System.out.print("*");
            }
            System.out.print("\n");
        }

        String T = Ajustar(String.format("%-58s", l.Titulo), 58);
        String A = Ajustar(String.format("%-30s", l.Autor), 30);
        String PI = Ajustar(String.format("%-28s", l.Pais + " (" + l.Idioma + ")"), 28);

        System.out.println(String.format("%-5d%s%-18s%-4d%-4d%-4d", Pos + 1, T, l.Isbn, l.NoLibros, l.NoPrestados, l.NoListaEspera));
        System.out.println(String.format("     %s%s%-12d", A, PI, l.anio));
    }

    public static int mostrarRepositorio(GestorBibliotecaIntf biblioteca, int idAdmin) throws RemoteException {
        int n = biblioteca.NRepositorios(idAdmin);
        Scanner sc = new Scanner(System.in);
        System.out.println("Pos \t Nombre \t\tDireccion \t\t Nº Libros");
        TDatosRepositorio datos;
        for (int i = 1; i < n + 1; i++) {
            datos = biblioteca.DatosRepositorio(idAdmin, (i - 1));
            System.out.printf("%-3d %-15s %-20s %d\n", i, datos.nombre, datos.direccion, datos.NoLibros);
        }
        System.out.println("0 Todos los repositorios");
        System.out.println("\nElige un repositorio: ");
        int opc22 = sc.nextInt();

        return opc22;
    }

    //Función que devuelve true si el campo correspondiente del Libro l contiene el Texto
    public static boolean comprobar(TLibro l, String texto, String campo) {
        boolean encontrado;
        switch (campo.toLowerCase()) {
            case "i":
                encontrado = l.Isbn.contains(texto);
                break;
            case "t":
                encontrado = l.Titulo.contains(texto);
                break;
            case "a":
                encontrado = l.Autor.contains(texto);
                break;
            case "p":
                encontrado = l.Pais.contains(texto);
                break;
            case "d":
                encontrado = l.Idioma.contains(texto);
                break;
            case "*":
                encontrado = l.Isbn.contains(texto)
                        || l.Titulo.contains(texto)
                        || l.Autor.contains(texto)
                        || l.Pais.contains(texto)
                        || l.Idioma.contains(texto);
                break;
            default:
                encontrado = false;
                break;
        }

        return encontrado;
    }

    public static void menuPrincipal() {
        System.out.println("Gestor Biblioteca (menú Principal)");
        System.out.println("**********************************");
        System.out.println("1.- M. Administración");
        System.out.println("2.- Consulta de Libros");
        System.out.println("3.- Prestamo de Libros");
        System.out.println("4.- Devolución de Libros");
        System.out.println("0.- Salir");
        System.out.println("\nElige una opción: ");
    }

    public static void menuAdministracion() {
        System.out.println("Gestor Biblioteca (menú Administración)");
        System.out.println("**********************************");
        System.out.println("1.- Cargar Repositorio");
        System.out.println("2.- Guardar Repositorio");
        System.out.println("3.- Nuevo libro");
        System.out.println("4.- Comprar libros");
        System.out.println("5.- Retirar libros");
        System.out.println("6.- Ordenar libros");
        System.out.println("7.- Buscar libros");
        System.out.println("8.- Listar libros");
        System.out.println("0.- Salir");
    }

    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        Scanner scCadena = new Scanner(System.in);

        int idAdmin = -10;
        TLibro l;
        try {
            //Registry Registro = LocateRegistry.getRegistry("localhost", 2026);
            GestorBibliotecaIntf biblioteca = (GestorBibliotecaIntf) Naming.lookup("rmi://localhost:2026/biblioteca");
            int opc1;
            do {
                menuPrincipal();
                opc1 = sc.nextInt();

                switch (opc1) {
                    case 1:
                        System.out.println("Indique la contraseña de acceso: ");
                        String contra = scCadena.nextLine();

                        idAdmin = biblioteca.Conexion(contra);
                        if (idAdmin > 0) {
                            int opc2;
                            do {
                                menuAdministracion();
                                opc2 = sc.nextInt();

                                switch (opc2) {
                                    case 1:
                                        System.out.println("¿Que repositorio quieres cargar? (Indique el nombre)");
                                        String rep = scCadena.nextLine();
                                        int result1 = biblioteca.AbrirRepositorio(idAdmin, rep);
                                        switch (result1) {
                                            case -1:
                                                System.out.println("Ya hay un administrador conectado o el ida no coincide");
                                                break;
                                            case -2:
                                                System.out.println("Ya esta cargado ese repositorio");
                                                break;
                                            case 0:
                                                System.out.println("no se ha podido abrir el repositorio");
                                                break;
                                            case 1:
                                                System.out.println("El repositorio se ha abierto correctamente");
                                                break;
                                            default:
                                                break;
                                        }
                                        break;

                                    case 2:
                                        int opc22 = mostrarRepositorio(biblioteca, idAdmin);

                                        int guardar = biblioteca.GuardarRepositorio(idAdmin, --opc22);

                                        switch (guardar) {
                                            case -1:
                                                System.out.println("Ya hay un administrador conectado o el ida no coincide");
                                                break;
                                            case -2:
                                                System.out.println("El repositorio no existe");
                                                break;
                                            case 0:
                                                System.out.println("no se ha podido guardar el/los repositorio");
                                                break;
                                            case 1:
                                                System.out.println("El repositorio/s se ha/n guardado correctamente");
                                                break;
                                            default:
                                                break;
                                        }
                                        break;
                                    case 3:
                                        l = new TLibro();

                                        System.out.println("Introduce el ISBN: ");
                                        l.Isbn = scCadena.nextLine();

                                        System.out.println("Introduce el Autor: ");
                                        l.Autor = scCadena.nextLine();

                                        System.out.println("Introduce el Titulo: ");
                                        l.Titulo = scCadena.nextLine();

                                        System.out.println("Introduce el Año: ");
                                        l.anio = sc.nextInt();

                                        System.out.println("Introduce el Pais: ");
                                        l.Pais = scCadena.nextLine();

                                        System.out.println("Introduce el Idioma: ");
                                        l.Idioma = scCadena.nextLine();

                                        System.out.println("Introduce el Nº de Libros inicial: ");
                                        l.NoLibros = sc.nextInt();

                                        int opc3 = mostrarRepositorio(biblioteca, idAdmin);
                                        int resultado = biblioteca.NuevoLibro(idAdmin, l, --opc3);
                                        switch (resultado) {
                                            case -2:
                                                System.out.println("El repositorio indicado por el parámetro ‘Repo’ no existe o es -1.");
                                                break;
                                            case -1:
                                                System.out.println("No hay un usuario identificado como administrador o el ‘Ida’ no coincide con el almacenado en el servidor");
                                                break;
                                            case 0:
                                                System.out.println("Hay un libro en algún repositorio de la biblioteca que tiene el mismo Isbn.");
                                                break;
                                            case 1:
                                                System.out.println("Se ha añadido el nuevo libro al repositorio indicado por Repo.");
                                                break;
                                        }
                                        break;
                                    case 4:

                                        System.out.println("Introduce el ISBN a buscar: ");
                                        String Isbn = scCadena.nextLine();

                                        int pos = biblioteca.Buscar(idAdmin, Isbn);
                                        switch (pos) {
                                            case -1:
                                                System.out.println("No hay un usuario identificado como administrador o el ‘Ida’ no coincide con el almacenado en el servidor");
                                                break;
                                            case -2:
                                                System.out.println("No se ha encontrado ningún Libro con el Isbn indicado por parámetro");
                                                break;
                                        }
                                        if (pos >= 0) {
                                            l = biblioteca.Descargar(idAdmin, -1, pos);
                                            Mostrar(pos, true, l);
                                            System.out.println("¿Este es el libro del que deseas comprar mas unidades? (s/n)");
                                            String sn = scCadena.nextLine();

                                            if (sn.toLowerCase().equalsIgnoreCase("s")) {
                                                System.out.println("Introduce el numero de libros a comprar: ");
                                                int cant = sc.nextInt();
                                                int result = biblioteca.Comprar(idAdmin, l.Isbn, cant);
                                                switch (result) {
                                                    case -1:
                                                        System.out.println("No hay un usuario identificado como administrador o el ‘Ida’ no coincide con el almacenado en el Servidor");
                                                        break;
                                                    case 0:
                                                        System.out.println("No se ha encontrado ningún Libro con el Isbn indicado por parámetro");
                                                        break;
                                                    case 1:
                                                        System.out.println("Se han agregado los nuevos ejemplares del libro y el repositorio se ha ordenado.");
                                                        break;
                                                }
                                            } else if (sn.toLowerCase().equalsIgnoreCase("n")) {
                                                System.out.println("¡Vuelva pronto!");
                                            }
                                        }
                                        break;
                                    case 5:
                                        System.out.println("Introduce el ISBN a buscar: ");
                                        String Isbn5 = scCadena.nextLine();

                                        int pos5 = biblioteca.Buscar(idAdmin, Isbn5);
                                        switch (pos5) {
                                            case -1:
                                                System.out.println("No hay un usuario identificado como administrador o el ‘Ida’ no coincide con el almacenado en el servidor");
                                                break;
                                            case -2:
                                                System.out.println("No se ha encontrado ningún Libro con el Isbn indicado por parámetro");
                                                break;
                                        }
                                        if (pos5 >= 0) {
                                            l = biblioteca.Descargar(idAdmin, -1, pos5);
                                            Mostrar(pos5, true, l);
                                            System.out.println("¿Este es el libro del que deseas retirar unidades? (s/n)");
                                            String sn = scCadena.nextLine();

                                            if (sn.toLowerCase().equalsIgnoreCase("s")) {
                                                System.out.println("Introduce el numero de libros a retirar: ");
                                                int cant = sc.nextInt();
                                                int result = biblioteca.Retirar(idAdmin, l.Isbn, cant);
                                                switch (result) {
                                                    case -1:
                                                        System.out.println("No hay un usuario identificado como administrador o el ‘Ida’ no coincide con el almacenado en el Servidor");
                                                        break;
                                                    case 0:
                                                        System.out.println("No se ha encontrado ningún Libro con el Isbn indicado por parámetro");
                                                        break;
                                                    case 1:
                                                        System.out.println("Se han retiradp los ejemplares indicados del libro y el repositorio se ha ordenado.");
                                                        break;
                                                }
                                            } else if (sn.toLowerCase().equalsIgnoreCase("n")) {
                                                System.out.println("¡Vuelva pronto!");
                                            }
                                        }
                                        break;
                                    case 6:
                                        int codi;
                                        do {
                                            System.out.println("Código de ordenación \n0.- Por Isbn \n1.- Por Título \n2.- Por Autor \n3.- Por Año \n4.- Por País \n5.- Por Idioma \n6.- Por nº de libros Disponibles \n7.- Por nº de libros Prestados \n8.- Por nº de libros en espera ");
                                            System.out.println("Introduce el codigo");
                                            codi = scCadena.nextInt();
                                        } while (codi < 0 || codi > 8);

                                        if (biblioteca.Ordenar(idAdmin, codi)) {
                                            System.out.println("Se ha ordenado correctamente");
                                        } else {
                                            System.out.println("No hay un usuario identificado como administrador o el ‘Ida’ no coincide con el servidor. ");
                                        }
                                        break;
                                    case 7:
                                        System.out.println("Introduce el texto a buscar: ");
                                        String buscar = scCadena.nextLine();

                                        System.out.println("Código de búsqueda \nI.- Por Isbn \nT.- Por Título \nA.- Por Autor \nP.- Por País \nD.- Por Idioma \n*.- Por todos los campos.");
                                        System.out.println("Introduce el codigo: ");
                                        String cod = scCadena.nextLine();

                                        int opc7 = mostrarRepositorio(biblioteca, idAdmin);
                                        boolean cabe = true;
                                        int nlib = biblioteca.NLibros(--opc7);
                                        for (int i = 0; i < nlib; i++) {
                                            l = biblioteca.Descargar(idAdmin, opc7, i);
                                            if (comprobar(l, buscar, cod)) {
                                                Mostrar(i, cabe, l);
                                                cabe = false;
                                            }
                                        }
                                        break;
                                    case 8:
                                        int nLibros = biblioteca.NLibros(-1);
                                        boolean cab = true;
                                        for (int i = 0; i < nLibros; i++) {
                                            l = biblioteca.Descargar(idAdmin, -1, i);
                                            Mostrar(i, cab, l);
                                            cab = false;
                                        }
                                        break;
                                    case 0:
                                        boolean salir = biblioteca.Desconexion(idAdmin);
                                        if (salir) {
                                            idAdmin = -1;
                                            System.out.println("Saliendo de la aplicacion...");
                                        } else {
                                            System.out.println("Error al desconectarse (Ida incorrecto)");
                                        }
                                        break;
                                    default:
                                        throw new AssertionError();
                                }

                            } while (opc2 != 0);

                        } else if (idAdmin == -1) {
                            System.out.println("ya hay un admin conectado");
                        } else if (idAdmin == -2) {
                            System.out.println("la contraseña no coincide");
                        }

                        break;

                    case 2:
                        System.out.println("Introduce el texto a buscar: ");
                        String buscar = scCadena.nextLine();

                        System.out.println("Código de búsqueda \nI.- Por Isbn \nT.- Por Título \nA.- Por Autor \nP.- Por País \nD.- Por Idioma \n*.- Por todos los campos.");
                        System.out.println("Introduce el codigo: ");
                        String cod = scCadena.nextLine();

                        boolean cabe = true;
                        int nlib = biblioteca.NLibros(-1);
                        for (int i = 0; i < nlib; i++) {
                            l = biblioteca.Descargar(idAdmin, -1, i);
                            if (comprobar(l, buscar, cod)) {
                                Mostrar(i, cabe, l);
                                cabe = false;
                            }
                        }

                        break;
                    case 3:
                        System.out.println("Introduce el texto a buscar: ");
                        String buscar3 = scCadena.nextLine();

                        System.out.println("Código de búsqueda \nI.- Por Isbn \nT.- Por Título \nA.- Por Autor \nP.- Por País \nD.- Por Idioma \n*.- Por todos los campos.");
                        System.out.println("Introduce el codigo: ");
                        String cod3 = scCadena.nextLine();

                        boolean cabe3 = true;
                        int nlib3 = biblioteca.NLibros(-1);
                        for (int i = 0; i < nlib3; i++) {
                            l = biblioteca.Descargar(idAdmin, -1, i);
                            if (comprobar(l, buscar3, cod3)) {
                                Mostrar(i, cabe3, l);
                                cabe3 = false;
                            }
                        }
                        System.out.println("¿Quieres sacar algun libro de la biblioteca? (s/n)");
                        String sn = scCadena.nextLine();

                        if (sn.toLowerCase().equalsIgnoreCase("s")) {
                            System.out.println("Introduce la posicion del libro: ");
                            int pos3 = sc.nextInt();

                            int result = biblioteca.Prestar(--pos3);
                            switch (result) {
                                case -1:
                                    System.out.println("La posición indicada no está dentro de los límites del repositorio mezclado y ordenado.");
                                    break;
                                case 0:
                                    System.out.println("No ay libros disponibles, usted se encuentra en lista de espera ¡Disculpe las molestias!");
                                    break;
                                case 1:
                                    System.out.println("El libro se ha solicitado correctamente, pase a recogerlo");
                                    break;
                            }
                        } else {
                            System.out.println("¡Vuelva pronto!");
                        }
                        break;
                    case 4:
                        System.out.println("Introduce el Isbn a buscar: ");
                        String isbn = scCadena.nextLine();

                        boolean cabe4 = true;
                        int nlib4 = biblioteca.NLibros(-1);
                        for (int i = 0; i < nlib4; i++) {
                            l = biblioteca.Descargar(idAdmin, -1, i);
                            if (comprobar(l, isbn, "i")) {
                                Mostrar(i, cabe4, l);
                                cabe4 = false;
                            }
                        }

                        System.out.println("¿Quieres devolver algun libro a la biblioteca? (s/n)");
                        sn = scCadena.nextLine();
                        if (sn.toLowerCase().equalsIgnoreCase("s")) {
                            System.out.println("Introduce la posicion del libro a devolver: ");
                            int pos4 = sc.nextInt();
                            int result = biblioteca.Devolver(--pos4);
                            switch (result) {
                                case -1:
                                    System.out.println("La posición indicada no está dentro de los límites del repositorio mezclado y ordenado.");
                                    break;
                                case 0:
                                    System.out.println("Se ha devuelto el libro reduciendo el número de usuarios en espera");
                                    break;
                                case 1:
                                    System.out.println("Se ha devuelto aumentando el número de libros disponibles");
                                    break;
                                case 2:
                                    System.out.println(" El libro no se puede devolver, porque no hay ni usuarios en lista de espera ni libros prestados.");
                                    break;
                                default:
                                    System.out.println("Error inesperado");
                                    break;
                            }
                        } else {
                            System.out.println("El tiempo apremia moroso");
                        }
                        break;
                    case 0:
                        boolean salir = biblioteca.Desconexion(idAdmin);
                        if (salir) {
                            System.out.println("Saliendo de la aplicacion...");
                        } else {
                            System.out.println("Error al desconectarse (Ida incorrecto)");
                        }
                        break;
                }
            } while (opc1 != 0);

        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(Practica2_cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
