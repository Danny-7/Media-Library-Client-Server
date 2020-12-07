package main.server;

public class ServerApp {
    //Peut être à bouger
    private static int lastDocumentNumber = 0;
    private static int lastMemberNumber = 0;

    public static int getNewDocNumber() {
        return lastDocumentNumber++;
    }
    public static int getNewMemberNumber() {
        return lastMemberNumber++;
    }

}
