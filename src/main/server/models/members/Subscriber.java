package main.server.models.members;

import main.server.main.ServerApp;
import main.server.models.Document;
import main.server.models.ObserverLibrary;
import main.server.models.documents.GeneralDocument;
import main.server.utils.MailHandler;
import main.server.services.LibraryService;

import java.time.LocalDate;
import java.time.Period;

public class Subscriber implements ObserverLibrary {
    private final int id;
    private final String name;
    private final int age;
    private final LocalDate birthdayDate;
    private boolean suspended;
    private final String email;


    public Subscriber(String name, LocalDate birthdayDate) {
        this.id = ServerApp.getNewMemberNumber();
        this.name = name;
        this.birthdayDate = birthdayDate;
        this.age = Period.between(birthdayDate, LocalDate.now()).getYears();
        this.suspended = false;
        this.email = this.name+"@library.com";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public LocalDate getBirthdayDate() {
        return birthdayDate;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void suspend() {
        LibraryService.scheduleSuspension(this);
        suspended = true;
    }

    public void unSuspend() {
        suspended = false;
        LibraryService.cancelSuspension(id);
    }

    @Override
    public String toString() {
        return "Subscriber{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", birthdayDate=" + birthdayDate +
                '}';
    }

    @Override
    public void update(Document doc) {
        GeneralDocument generalDocument = (GeneralDocument)doc;
        MailHandler.sendMail("aguiardaniel271@gmail.com", generalDocument.getTitle());
    }
}
