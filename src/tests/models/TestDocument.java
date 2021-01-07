package tests.models;

import main.server.models.documents.DVD;
import main.server.models.documents.GeneralDocument;
import main.server.models.exceptions.BorrowException;
import main.server.models.exceptions.ReservationException;
import main.server.models.members.Subscriber;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/** DVDBorrowReservationTest : A class for test dvd operations
 *
 * @author Jules Doumèche - Daniel Aguiar - Gwénolé Martin
 * @version 1.0
 * @since 2021-01-04
 */
class TestDocument {

    private static Subscriber i_sb;
    private static Subscriber s_sb;
    private static GeneralDocument doc;

    @BeforeAll
    static void subscriber() {
        i_sb = new Subscriber("Initial Subscriber", LocalDate.of(2001,7,24));
        s_sb = new Subscriber("Second Subscriber", LocalDate.of(1997,6,2));
    }

    @BeforeEach
    public void document() {
        doc = new DVD("Le roi lion",3);
    }

    @Test
    public void testInitialDocState() {
        assertTrue(doc.isAvailable());
        assertNull(doc.getHolder());
    }

    @Test
    public void testReservation() {
        doc.reservationFor(i_sb);
        assertEquals(i_sb, doc.getHolder());
        assertTrue(doc.isReserved());
    }

    @Test
    public void testBorrow() {
        doc.borrowBy(i_sb);
        assertEquals(i_sb, doc.getHolder());
        assertTrue(doc.isBorrowed());
    }

    @Test
    public void testBorrowWithReservationBefore() {
        doc.reservationFor(i_sb);
        assertTrue(doc.isReserved());
        doc.borrowBy(i_sb);
        assertTrue(doc.isBorrowed());
    }

    @Test
    public void testReservationBorrowForDvdDate() {
        GeneralDocument adult_doc = new DVD("Le roi lion",20);
        // we can't reserve a dvd when our age is smaller than the recommended
        assertThrows(ReservationException.class, () -> {
            adult_doc.reservationFor(i_sb);
        });
        assertNull(adult_doc.getHolder());
        assertFalse(adult_doc.isReserved());

        assertThrows(BorrowException.class, () -> {
            adult_doc.borrowBy(i_sb);
        });
        // Doc still in initial state
        assertNull(adult_doc.getHolder());
        assertFalse(adult_doc.isBorrowed());
    }

    @Test
    public void testBorrowWithReservationBeforeWithDifferentSubscriber() {
        // we can't borrow if another subscriber reserved the doc
        doc.reservationFor(i_sb);
        assertTrue(doc.isReserved());
        assertThrows(BorrowException.class, () -> {
            doc.borrowBy(s_sb);
        });
        assertTrue(doc.isReserved());
    }

    @Test
    public void testBorrowBorrowedDoc() {
        doc.borrowBy(i_sb);
        assertThrows(BorrowException.class, () -> {
            doc.borrowBy(i_sb);
        });
        assertThrows(BorrowException.class, () -> {
            doc.borrowBy(s_sb);
        });
    }

    @Test
    public void testReservationBorrowedDoc() {
        doc.borrowBy(i_sb);
        assertThrows(ReservationException.class, () -> {
            doc.reservationFor(i_sb);
        });
        assertThrows(ReservationException.class, () -> {
            doc.reservationFor(s_sb);
        });
    }

    @Test
    public void testReturnBack() {
        doc.borrowBy(i_sb);
        doc.returnBack();
        // Doc still in initial state
        assertTrue(doc.isAvailable());
        assertNull(doc.getHolder());
    }

}