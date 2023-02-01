import java.io.Serializable;
import java.util.ArrayList;

public class Session implements Serializable {

    private NewDate sessionDate;
    private NewTime sessionTime;
    private ArrayList <Tutor> tutors;
    private ArrayList <Tutee> tutees;

    public Session(NewDate sessionDate, NewTime sessionTime, ArrayList <Tutor> tutors, ArrayList <Tutee> tutees)
    {
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
        this.tutors = tutors;
        this.tutees = tutees;
    }

    public Session() {

    }

    public NewDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(NewDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public NewTime getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(NewTime sessionTime) {
        this.sessionTime = sessionTime;
    }

    public ArrayList<Tutor> getTutors() {
        return tutors;
    }

    public void setTutors(ArrayList<Tutor> tutors) {
        this.tutors = tutors;
    }

    public ArrayList<Tutee> getTutees() {
        return tutees;
    }

    public void setTutees(ArrayList<Tutee> tutees) {
        this.tutees = tutees;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Session{" +
                "sessionDate=" + sessionDate +
                ", sessionTime=" + sessionTime +
                ", tutors=" + tutors +
                ", tutees=" + tutees +
                '}';
    }

    public int compareTo(Session session) {
        int comparison = this.getSessionDate().compareTo(session.getSessionDate());
        if (comparison == 0) {
            return this.getSessionTime().compareTo(session.getSessionTime());
        }
        else {
            return comparison;
        }
    }

}