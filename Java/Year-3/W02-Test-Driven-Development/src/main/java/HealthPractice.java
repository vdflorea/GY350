// Vlad Florea
// 22409144

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HealthPractice {
    private String practiceName;
    private String practiceAddress;
    private List<Booking> bookingList = new ArrayList<Booking>();

    public HealthPractice(String practiceName, String practiceAddress) {
        this.practiceName = practiceName;
        this.practiceAddress = practiceAddress;
    }

    public static void main(String[] args) {
        // Simple code to visualise/test a Booking's toString() output during runtime

        HealthPractice h = new HealthPractice("Galway Hospital", "Galway");
        h.createBooking("AB1234567", h);

        System.out.println(h.bookingList.get(0).toString());
    }

    // Case when date/time is provided
    public void createBooking(String patientNum, HealthPractice healthPractice, LocalDateTime appointmentDateTime) {
        bookingList.add(new Booking(patientNum, healthPractice, appointmentDateTime));
    }

    // Case when date/time not provided and must be set by external API
    // (Overloaded method)
    public void createBooking(String patientNum, HealthPractice healthPractice) {
        bookingList.add(new Booking(patientNum, healthPractice));
    }

    public String getPracticeName() {
        return practiceName;
    }

    public String getPracticeAddress() {
        return practiceAddress;
    }
}
