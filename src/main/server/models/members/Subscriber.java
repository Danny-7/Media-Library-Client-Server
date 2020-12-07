package main.server.models.members;

import java.time.LocalDate;
import java.time.Period;

public class Subscriber {
    private int id;
    private String name;
    private int age;
    private LocalDate birthdayDate;

    public Subscriber(String name, LocalDate birthdayDate) {
        this.id = 0; //TODO à changer
        this.name = name;
        this.birthdayDate = birthdayDate;
        this.age = Period.between(birthdayDate, LocalDate.now()).getYears();
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

    @Override
    public String toString() {
        return "Subscriber{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", birthdayDate=" + birthdayDate +
                '}';
    }
}
