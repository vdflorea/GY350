// Vlad Florea
// 22409144

import java.time.LocalDateTime;
import java.util.Random;

public class Booking implements HealthPracticeAppointmentWebservice {
    private long bookingId;

    private String patientNum;
    private HealthPractice healthPractice;
    private LocalDateTime appointmentDateTime;

    public Booking(String patientNum, HealthPractice healthPractice, LocalDateTime appointmentDateTime) {
        try {
            // Bookings CANNOT be created with an invalid date/time
            isValidDate(appointmentDateTime);

            // Generate 8-digit Booking ID
            bookingId = new Random().nextLong(99999999);

            this.patientNum = patientNum;
            this.healthPractice = healthPractice;
            this.appointmentDateTime = appointmentDateTime;
        } catch (InvalidDateTimeException e) {
            // Rethrow up the stack once more
            throw e;
        }
    }

    // When date/time is not specified
    public Booking(String patientNum, HealthPractice healthPractice){
        try {
            LocalDateTime tempDateTime = createBookingDateTime(healthPractice);

            // "External API" may generate invalid date/time so we check:
            isValidDate(tempDateTime);

            this.appointmentDateTime = tempDateTime;

            // Generate 8-digit Booking ID
            bookingId = new Random().nextLong(99999999);

            this.patientNum = patientNum;
            this.healthPractice = healthPractice;
        } catch (InvalidDateTimeException e) {
            // Rethrow up the stack once more
            throw e;
        }
    }

    public void isValidDate(LocalDateTime date) throws InvalidDateTimeException {
        // Check if date provided is in the future (invalid)
        if (date.isBefore(LocalDateTime.now())) {
            throw new InvalidDateTimeException("Invalid date detected. Booking date cannot be in the past!");
        }
    }

    public LocalDateTime createBookingDateTime(HealthPractice healthPractice) {
        // Here the external API logic would be called/executed and the healthPractice variable would be used
        // -> For this example, just going to generate the booking date/time locally:
        return LocalDateTime.now().plusMonths(1);
    }

    @Override
    public String toString() {
        // Print out a Booking's information as per the format in the assignment brief

        String out = "";
        out += "Booking ID Number: " + bookingId + "\n";
        out += "Patient Number: " + patientNum + "\n";
        out += "Health Practice: " + healthPractice.getPracticeName() + "\n";
        out += "Address: " + healthPractice.getPracticeAddress() + "\n";
        out += "Date & Time: " + String.format("On %s, %d %s %d at %s:%s",
                appointmentDateTime.getDayOfWeek(),
                appointmentDateTime.getDayOfMonth(),
                appointmentDateTime.getMonth(),
                appointmentDateTime.getYear(),
                appointmentDateTime.getHour(),
                appointmentDateTime.getMinute()) + "\n";

        return out;
    }

    // Possible to edit the Patient's Number
    public void setPatientNumber(String patientNum) {
        this.patientNum = patientNum;
    }

    public String getPatientNumber() {
        return patientNum;
    }

    public HealthPractice getHealthPractice() {
        return healthPractice;
    }

    public LocalDateTime getBookingDateTime() {
        return appointmentDateTime;
    }

    public long getBookingId() {
        return bookingId;
    }
}
