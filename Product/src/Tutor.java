import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
public class Tutor extends Person implements Serializable {
    // attributes
    private int gradeLevel;
    private ArrayList <Subject> subjectsTaught;
    private HashMap <String, Boolean> sessionsAvailable;
    private Tutee tutee;
    public Tutor(ArrayList<Subject> subjectsTaught) {
        this.subjectsTaught = subjectsTaught;
    }

    public Tutor() {

    }

    public int getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public ArrayList<Subject> getSubjectsTaught() {
        return subjectsTaught;
    }

    public void setSubjectsTaught(ArrayList<Subject> subjectsTaught) {
        this.subjectsTaught = subjectsTaught;
    }

    public HashMap <String, Boolean> getSessionsAvailable() {
        return sessionsAvailable;
    }
    public String toStringSessionsAvailable()
    {
        String sessionsavailable = "";
        if (sessionsAvailable != null) {
            for (String string: sessionsAvailable.keySet()) // adapted from https://www.geeksforgeeks.org/iterate-map-java/
            {
                sessionsavailable += string + " ,";
            }
        }

        return "Sessions available: " + sessionsavailable ;
    }

    public void setSessionsAvailable(HashMap <String, Boolean> sessionsAvailable) {
        this.sessionsAvailable = sessionsAvailable;
    }

    public Tutee getTutee() {
        return tutee;
    }

    public void setTutee(Tutee tutee) {
        this.tutee = tutee;
    }
    public java.lang.String toString() {
        return "Tutor {" +
                "Student Name = " + this.getName()+
                ", gradeLevel =" + gradeLevel +
                ", subjectsTaught =" + subjectsTaught +
                ", sessionsAvailable =" + toStringSessionsAvailable()+
                " }";
    }

    public boolean searchSubjectByNameAndLevel (String subjectName, char subjectLevel) throws IOException {
        int low = 0;
        int high = this.subjectsTaught.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (this.subjectsTaught.get(mid).getSubjectName().equals(subjectName) && this.subjectsTaught.get(mid).getSubjectLevel() == (subjectLevel)) {
                return true;
            }
            else if (this.subjectsTaught.get(mid).getSubjectName().compareTo(subjectName)<0) {
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
        }
        return false;
    }

    public Subject searchSubjectByNameAndLevelAndReturnSubject (String subjectName, char subjectLevel) throws IOException {
        int low = 0;
        int high = this.subjectsTaught.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (this.subjectsTaught.get(mid).getSubjectName().equals(subjectName) && this.subjectsTaught.get(mid).getSubjectLevel() == (subjectLevel)) {
                return subjectsTaught.get(mid);
            }
            else if (this.subjectsTaught.get(mid).getSubjectName().compareTo(subjectName)<0) {
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
        }
        return null;
    }

    public boolean searchSession (NewDate date, NewTime time) {
        String dateAndTime = date.toString() + " " + time.toString();
        for (HashMap.Entry <String, Boolean> entry: this.sessionsAvailable.entrySet()) {
            if (dateAndTime.equals(entry.getKey())) {
                return true;
            }
        }
        return false;
    }
}