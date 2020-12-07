package tests.models;

import main.server.models.documents.DVD;
import main.server.models.documents.GeneralDocument;
import main.server.models.exception.BorrowException;
import main.server.models.exception.ReservationException;
import main.server.models.members.Subscriber;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DVDBorrowReservationTest {

    private static Subscriber sb;

    @BeforeAll
    static void subscriber() {
        sb = new Subscriber("Daniel", LocalDate.of(2001,7,24));
    }

    @Test
    public void testReservation() {
        GeneralDocument doc = new DVD("Le roi lion",3);
        assertTrue(doc.isAvailable());
        assertNull(doc.getHolder());
        doc.reservationFor(sb);
        assertEquals(sb, doc.getHolder());
        assertTrue(doc.isReserved());
    }

    @Test
    public void testBorrowWithReservationBefore() {
        GeneralDocument doc = new DVD("Le roi lion",20);
        assertTrue(doc.isAvailable());
        assertNull(doc.getHolder());
        assertThrows(ReservationException.class, () -> {
            doc.reservationFor(sb);
                });
        // we can't reserve a dvd when our age is smaller than the recommended
        assertNull(doc.getHolder());

        assertThrows(BorrowException.class, () -> {
            doc.borrowBy(sb);
        });

        // we can't borrow a dvd when our age is smaller than the recommended
        assertFalse(doc.isBorrowed());
    }

    @Test
    public void testReturnBack() {
        GeneralDocument doc = new DVD("Le petit chaperon rouge",3);
        doc.borrowBy(sb);

        // we can reserve a dvd when our age is higher than the recommended
        assertEquals(sb,doc.getHolder());
        assertFalse(doc.isBorrowed());
    }

    
}