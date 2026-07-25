/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package practica2;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author usuario
 */
public class Practica2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        try {
            GestorBiblioteca_servidor b = new GestorBiblioteca_servidor();
            Registry reg;
            reg = LocateRegistry.createRegistry(2026);

            reg.rebind("biblioteca", b);

            System.out.println("Servidor Funcionando ....");
            System.out.println("pulsa una tecla para finalizar");

            System.in.read();

            System.out.println("Saliendo del servidor ...");
            System.exit(0);

        } catch (RemoteException ex) {
            Logger.getLogger(Practica2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Practica2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
