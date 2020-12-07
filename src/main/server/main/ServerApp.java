package main.server.main;

import main.server.BorrowServer;
import main.server.ReservationServer;
import main.server.ReturnServer;

public class ServerApp {
    //On lance ici les 3 threads réservation / emprunt / retour
    public static void main(String[] args) {
        ReservationServer reservationServer = new ReservationServer();
        BorrowServer borrowServer = new BorrowServer();
        ReturnServer returnServer = new ReturnServer();

        new Thread(reservationServer).start();
        new Thread(borrowServer).start();
        new Thread(returnServer).start();
    }
}
