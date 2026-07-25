/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package IRemota;
import java.rmi.Remote;
import java.rmi.RemoteException;
import practica2.TDatosRepositorio;
import practica2.TLibro;
/**
 *
 * @author usuario
 */
public interface GestorBibliotecaIntf extends Remote {
    

    int Conexion(String Passwd) throws RemoteException;

    /*Verifica que la contraseña indicada por el parámetro ‘Passwd’ coincida con la del administrador (5634989)
    y devuelve un identificador de administrador (IDA). Las salidas de este servicio son:
    -1: Ya hay un usuario identificado como administrador, solo se permite uno.
    -2: La contraseña es errónea.
    N: Un número aleatorio generado en el intervalo [1, 1000000].
     Este número es el identificador del administrador (IDA), se almacena en el servidor y se envía
     al cliente para que sea utilizado en las llamadas al servicio. */
    boolean Desconexion(int Ida) throws RemoteException;

    /*Desconecta el cliente como administrador. Comprueba que el ‘Ida’ pasado por parámetro coincide con el
    almacenado en el servidor. Si no coincide devuelve false y caso contrario borra el Ida almacenado en el
    servidor (lo pone a -1) y devuelve true. */
    int AbrirRepositorio(int Ida, String NomFichero) throws RemoteException;

    /*Carga en memoria el fichero repositorio cuyo nombre es indicado por el parámetro ‘NomFichero’ y lo ordena
    por el campo de ordenación almacenado en el servidor. Las salidas de este servicio son:
    -1: No hay un usuario identificado como administrador o el ‘Ida’ no coincide con el almacenado en
     el servidor.
    -2: Ya existe un repositorio cargado con el mismo nombre de fichero.
    0: No se ha podido abrir el fichero repositorio.
    1: El fichero ha sido cargado al nuevo repositorio y ha sido ordenado. */

    int GuardarRepositorio(int Ida, int Repo) throws RemoteException;

    /*Guarda a fichero el repositorio que indica el parámetro ‘Repo’. En caso de ser -1 se guardarán todos los
repositorios cargados en el servidor en sus correspondientes ficheros. Las salidas de este servicio son:
-1: No hay un usuario identificado como administrador o el ‘Ida’ no coincide con el almacenado en
 el servidor.
-2: El repositorio no existe.
0: No se ha podido guardar a fichero el o los repositorios.
1: El o los repositorios han sido guardados a sus respectivos ficheros. */
    int NRepositorios(int Ida) throws RemoteException;

    /*Devuelve el número de repositorios almacenados en la biblioteca o -1 si el ‘Ida’ pasado por parámetro no
coincide con el almacenado en el servidor. */
    TDatosRepositorio DatosRepositorio(int Ida, int Repo) throws RemoteException;

    /*Devuelve la información del repositorio que indica el parámetro ‘Repo’. La clase TDatosRepositorio almacena
el nombre, dirección y número de libros que contiene el repositorio. Las salidas de este servicio son:
 null: No hay un usuario identificado como administrador o el ‘Ida’ no coincide con el almacenado
 en el servidor.
!=null: Un objeto TDatosRepositorio. Si el repositorio no existe en el servidor, el nombre del
repositorio contendrá “???”. */
    int NuevoLibro(int Ida, TLibro Libro, int Repo) throws RemoteException;

    /*Añade el libro del parámetro ‘Libro’ en el repositorio indicado por ‘Repo’. Una vez añadido, se ordenará
el repositorio por el campo por defecto almacenado en el servidor. Las salidas de este servicio son:
-1: No hay un usuario identificado como administrador o el ‘Ida’ no coincide con el almacenado en
 el servidor.
-2: El repositorio indicado por el parámetro ‘Repo’ no existe o es -1.
0: Hay un libro en algún repositorio de la biblioteca que tiene el mismo Isbn.
1: Se ha añadido el nuevo libro al repositorio indicado por Repo. */
    int Comprar(int Ida, String Isbn, int NoLibros) throws RemoteException;

    /*Añade un número de ejemplares indicados por el parámetro ‘NoLibros’ al libro cuyo Isbn coincida con el
pasado por parámetro. Una vez actualizado el número de ejemplares, todos los usuarios en espera de un
ejemplar lo recibirán reduciendo consecuentemente la cantidad de libros disponibles. Una vez terminada la
compra se ordenará el repositorio, que contiene el libro, por el campo por defecto almacenado en el servidor.
Las salidas de este servicio son:
-1: No hay un usuario identificado como administrador o el ‘Ida’ no coincide con el almacenado en
 el Servidor.
0: No se ha encontrado ningún Libro con el Isbn indicado por parámetro.
1: Se han agregado los nuevos ejemplares del libro y el repositorio se ha ordenado. */
    int Retirar(int Ida, String Isbn, int NoLibros) throws RemoteException;

    /*Elimina un número de ejemplares indicados por el parámetro ‘NoLibros’ al libro cuyo Isbn coincida con el
pasado por parámetro, siempre y cuando haya suficiente libros disponibles para retirar. Una vez retirados
los ejemplares, se ordenará el repositorio, que contiene el libro, por el campo por defecto almacenado en
el servidor. Las salidas de este servicio son:
-1: No hay un usuario identificado como administrador o el ‘Ida’ no coincide con el almacenado en
 el servidor.
0: No se ha encontrado ningún Libro con el Isbn indicado por parámetro.
1: Se han reducido el número de ejemplares disponibles y se ha ordenado el repositorio.
2: No hay suficientes ejemplares disponibles para ser retirados. */
    boolean Ordenar(int Ida, int Campo) throws RemoteException;

    /*Actualiza el campo de ordenación por defecto almacenado en el servidor con el indicado por el parámetro
‘Campo’ y ordena todos los repositorios almacenados en la biblioteca por dicho campo. Las salidas de este
servicio son:
false: No hay un usuario identificado como administrador o el ‘Ida’ no coincide con el almacenado
 en servidor.
true: Se han ordenado los repositorios. */
    int NLibros(int Repo) throws RemoteException;

    /*Devuelve el número de libros del repositorio indicado por parámetro ‘Repo’. Si ‘Repo’ es -1 devolverá el
número de libros almacenados en el servidor. Las salidas de este servicio son:
-1: Si no existe el repositorio en el servidor.
>=0: El número de libros del repositorio especificado o la suma del número de libros de todos los
 repositorios. */

    int Buscar(int Ida, String Isbn) throws RemoteException;

    /*Devuelve la posición del Libro cuyo Isbn coincide con el parámetro ‘Isbn’ en un repositorio que contenga
la mezcla ordenada, por el campo por defecto almacenado en el servidor, de todos los repositorios cargados
en el servidor. Las salidas de este servicio son:
-1: No hay un usuario identificado como administrador o el ‘Ida’ no coincide con el almacenado en
 servidor.
-2: No se ha encontrado ningún Libro con el Isbn indicado por parámetro.
>=0: La posición del libro dentro de la mezcla ordenada de todos los libros de todos los repositorios
 almacenados en el servidor. */
    TLibro Descargar(int Ida, int Repo, int Pos) throws RemoteException;

    /*Devuelve el libro que está en la posición indicada por el parámetro ‘Pos’ del repositorio indicado por el
parámetro ‘Repo’. Si el parámetro ‘Repo’ es -1, devolverá el libro de la posición del repositorio con la
mezcla ordenada de todos los repositorios. Las salidas de este servicio son:
 null: El repositorio indicado no existe o la posición del libro es incorrecta.
!=null: Un objeto TLibro. Si el campo ‘Ida’ no coincide con el almacenado en el servidor, los campos
‘NoPresentados’ y ‘NoListaEspera’ del Libro devuelto deben ser 0.*/

    int Prestar(int Pos) throws RemoteException;

    /*Presta un ejemplar del libro que está en la posición ‘Pos’ de la mezcla ordenada de todos los repositorios.
Si hay ejemplares disponibles se reducirá en una unidad y los prestados aumentarán en una unidad. Si no
hubiera ejemplares disponibles se aumentará el número de usuarios en la lista de espera. Una vez actualizado
se ordenará nuevamente el repositorio que contiene el libro por el campo de ordenación almacenado en el
servidor. Las salidas de este servicio son:
-1: La posición indicada no está dentro de los límites del repositorio mezclado y ordenado.
0: Se ha puesto el usuario en la lista de espera.
1: Se ha prestado el libro correctamente. */
    int Devolver(int Pos) throws RemoteException;
    /* Devolverá al Gestor Bibliotecario un ejemplar del libro cuya posición es indicada en el campo ‘Pos’, de
manera que si hay usuarios en espera se reducirán en una unidad y en caso contrario si hay libros prestados
se reducirá el número de libros prestados y se aumentará el número de libros disponibles. Una vez actualizado
los campos del libro, se ordenará el repositorio que contiene el libro por el campo de ordenación almacenado
en el servidor. Las salidas de este servicio son:
-1: La posición indicada no está dentro de los límites del repositorio mezclado y ordenado.
0: Se ha devuelto el libro reduciendo el número de usuarios en espera.
1: Se ha devuelto aumentando el número de libros disponibles.
2: El libro no se puede devolver, porque no hay ni usuarios en lista de espera ni libros
 prestados. */

}
