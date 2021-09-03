package DBAcess;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Appointment;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * This class is used to validate numerous sections of the program that requires validation
 */
public class Validation {
    /**
     * The check for overlapping appointments in the appointment add controller
     * @param id appointment id
     * @param start start time of appointment
     * @param end end time of appointment
     * @return boolean where true means there is an overlapping appointment
     */
    public static Boolean appointmentExistsUpdate(int id,LocalDateTime start, LocalDateTime end) {
        Boolean returnValue = false;
        ObservableList<Appointment> allAppointments = DBAppointments.getAppointmentsExcept(id);

        for (Appointment a : allAppointments) {

            LocalDateTime apptStart = a.getStart();
            LocalDateTime apptEnd = a.getEnd();

            if (start.isAfter(apptStart) && start.isBefore(apptEnd) || end.isAfter(apptStart) && end.isBefore(apptEnd) || start.equals(apptStart) || end.equals(apptEnd)) {
                returnValue = true;
            }
        }

        return returnValue;
    }
    /**
     * The check for overlapping appointments in the update appointment controller
     * @param start start time of appointment
     * @param end end time of appointment
     * @return boolean where true means there is an overlapping appointment
     */
    public static Boolean appointmentExistsAdd(LocalDateTime start, LocalDateTime end){
        Boolean returnValue = false;
        ObservableList<Appointment> allAppointments = DBAppointments.getAllAppointments();

        for(Appointment a : allAppointments){

            LocalDateTime apptStart = a.getStart();
            LocalDateTime apptEnd = a.getEnd();

            if(start.isAfter(apptStart) && start.isBefore(apptEnd) || end.isAfter(apptStart) && end.isBefore(apptEnd) || start.equals(apptStart) || end.equals(apptEnd)){
                returnValue = true;
            }
        }

        return returnValue;
    }

    /**
     * Provides a check if there are appointments within 15 minutes
     * @return true if an appointment is within 15 minutes, false if there is none
     */
    public static Appointment appointmentInFifteenMinutes(){
        Appointment appointment = null;
        ObservableList<Appointment> allAppointments = DBAppointments.getAllAppointments();
        LocalDateTime currentTime = LocalDateTime.now();

        for(Appointment a : allAppointments){
            LocalDateTime apptStart = a.getStart();

            long timeDifference = ChronoUnit.MINUTES.between(currentTime,apptStart);

            if(timeDifference > 0 && timeDifference <= 15){
                appointment = a;
            }
        }
        return appointment;
    }

}
