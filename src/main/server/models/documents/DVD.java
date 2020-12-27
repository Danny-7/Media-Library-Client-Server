package main.server.models.documents;

import main.server.models.exceptions.BorrowException;
import main.server.models.exceptions.ReservationException;
import main.server.models.members.Subscriber;

public class DVD extends GeneralDocument{
    private int recommendedAge;
    private boolean forAdults;
    private static final int ADULTS_AGE = 16;


    public DVD(String title, int recommendedAge) {
        super(title);
        this.recommendedAge = recommendedAge;
        this.forAdults = recommendedAge >= ADULTS_AGE;
    }

    @Override
    public void reservationFor(Subscriber sb) throws ReservationException {
        if(sb.getAge() < this.recommendedAge)
            throw new ReservationException("You don't have the recommended age !");
        super.reservationFor(sb);
    }

    @Override
    public void borrowBy(Subscriber sb) throws BorrowException {
        if(sb.getAge() < this.recommendedAge)
            throw new BorrowException("You don't have the recommended age !");
        super.borrowBy(sb);
    }

    @Override
    public String toString() {
        return super.toString() + "\n\t DVD{" +
                "recommendedAge=" + recommendedAge +
                ", forAdults=" + forAdults +
                '}';
    }
}
