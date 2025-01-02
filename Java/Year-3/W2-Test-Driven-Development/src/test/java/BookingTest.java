// Vlad Florea
// 22409144

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class BookingTest {
    @Test
    void testValidHealthPracticeAddedUponCreation() {
        HealthPractice h = new HealthPractice("Galway Hospital", "Galway");
        Booking b = new Booking("AB1234567", h);

        assertEquals(b.getHealthPractice(), h);
    }

    @Test
    void testValidPatientNumberAddedUponCreationAndAfterEdit() {
        String patientNum1 = "AB1234567";

        Booking b = new Booking(patientNum1, new HealthPractice("Galway Hospital", "Galway"));
        assertEquals(b.getPatientNumber(), patientNum1);

        // Edit patient number and ensure that returned value is valid
        String patientNum2 = "CD3456789";
        b.setPatientNumber(patientNum2);
        assertEquals(b.getPatientNumber(), patientNum2);
    }

    @Test
    void testValidBookingWithoutSpecifyingDateAndTime() {
        // Here we are testing the "external API"

        HealthPractice h = new HealthPractice("Galway Hospital", "Galway");
        Booking b = new Booking("AB1234567", h);

        // An exception would be thrown if the date is invalid
        // so we are instead ensuring that the returned date is set & exists
        assertNotNull(b.getBookingDateTime());
    }

    @Test
    void testValidBookingWithSpecifyingDateAndTime() {
        LocalDateTime tempDateTime = LocalDateTime.now().plusMonths(1); // Create appointment date one month from now

        HealthPractice h = new HealthPractice("Galway Hospital", "Galway");
        Booking b = new Booking("AB1234567", h, tempDateTime);

        assertEquals(b.getBookingDateTime(), tempDateTime);
    }

    @Test
    void testInvalidDateTime() {
        LocalDateTime tempDateTime = LocalDateTime.now().minusMonths(1); // Invalid date (in the past)

        HealthPractice h = new HealthPractice("Galway Hospital", "Galway");

        assertThrows(InvalidDateTimeException.class, () -> new Booking("AB1234567", h, tempDateTime));
    }

    @Test
    void testValidDateTimeWithSpecifyingDateTime() {
        LocalDateTime tempDateTime = LocalDateTime.now().plusMonths(1); // Valid date (in the future)

        HealthPractice h = new HealthPractice("Galway Hospital", "Galway");

        // Ensuring that NO exception is thrown
        assertDoesNotThrow(() -> new Booking("AB1234567", h, tempDateTime));
    }

    @Test
    void testValidDateTimeWithoutSpecifyingDateTime() {
        // i.e. testing that the "external API" is working correctly
        HealthPractice h = new HealthPractice("Galway Hospital", "Galway");

        // Ensuring that NO exception is thrown
        assertDoesNotThrow(() -> new Booking("AB1234567", h));
    }
}