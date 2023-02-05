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

    // constructor
    public Tutor(ArrayList<Subject> subjectsTaught) {
        this.subjectsTaught = subjectsTaught;
    }

    // accessors and mutators
    public int getGradeLevel() {
        return gradeLevel;
    }
    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }
    public ArrayList<Subject> getSubjectsTaught() {
        return subjectsTaught;
    }
    public HashMap <String, Boolean> getSessionsAvailable() {
        return sessionsAvailable;
    }
    public void setSessionsAvailable(HashMap <String, Boolean> sessionsAvailable) { this.sessionsAvailable = sessionsAvailable; }
    public Tutee getTutee() {
        return tutee;
    }
    public void setTutee(Tutee tutee) {
        this.tutee = tutee;
    }

    // toString - polymorphism

    //toString just to print sessionsAvailable in a specific format
    public String toStringSessionsAvailable()
    {
        String sessionsavailable = "";
        if (sessionsAvailable != null) {
            for (String string: sessionsAvailable.keySet()) // iterates through hashmap's keys
            {
                sessionsavailable += "[" + string + " = " + sessionsAvailable.get(string) + "],";
            }
        }
        return "{"+sessionsavailable+"}" ;
    }
    public String toString() {
        if (this.getTutee() == null) { // if the tutor's tutee is null
            return "Tutor {" +
                    "Name = " + this.getName()+
                    ", gradeLevel =" + this.getGradeLevel() +
                    ", subjectsTaught =" + this.getSubjectsTaught() +
                    ", sessionsAvailable =" + this.toStringSessionsAvailable() +
                    ", tutee = null}"; // print tutor is null
        }
        else { // if tutor's tutee is not null
            return "Tutor {" +
                    "Name = " + this.getName() +
                    ", gradeLevel =" + this.getGradeLevel() +
                    ", subjectsTaught =" + this.getSubjectsTaught() +
                    ", sessionsAvailable =" + this.toStringSessionsAvailable() +
                    ", tutee =" + this.getTutee().getName() + // printing just the name as if tutee is printed entirely, tutee calls tutor and infinite recursive error would occur
                    " }";
        }
    }

    // searches the tutor's list of subjects by name and level to see if it is present or not
    public boolean searchSubjectByNameAndLevel (String subjectName, char subjectLevel) throws IOException {
        insertionSort(this.subjectsTaught); // sorts subjects by name and level
        int low = 0;
        int high = this.subjectsTaught.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2; // middle index of sub-array
            if (this.subjectsTaught.get(mid).getSubjectName().equals(subjectName) && this.subjectsTaught.get(mid).getSubjectLevel() == (subjectLevel)) { // if this subject is the one looked for
                return true;
            }
            else if (this.subjectsTaught.get(mid).getSubjectName().compareTo(subjectName)<0) {
                low = mid + 1; // subject in higher sub-array
            }
            else {
                high = mid - 1; // subject in lower sub-array
            }
        }
        return false; // subject not found
    }
    // searches the tutor's list of subjects by name and level, returning the subject if found and null if not
    public Subject searchSubjectByNameAndLevelAndReturnSubject (String subjectName, char subjectLevel) throws IOException {
        insertionSort(this.subjectsTaught); // sorts subjects by name and level
        int low = 0;
        int high = this.subjectsTaught.size() - 1;
        while (low <= high) {
            int mid = (high + low) / 2; // middle index of sub-array
            if (this.subjectsTaught.get(mid).getSubjectName().equals(subjectName) && this.subjectsTaught.get(mid).getSubjectLevel() == (subjectLevel)) { // if this subject is the one looked for
                return subjectsTaught.get(mid); // return subject
            }
            else if (this.subjectsTaught.get(mid).getSubjectName().compareTo(subjectName)<0) {
                low = mid + 1; // subject in higher sub-array
            }
            else {
                high = mid - 1; // subject in lower sub-array
            }
        }
        return null; // subject not found
    }
    // sorts subjects by name and level
    public static void insertionSort(ArrayList<Subject> subjects)
    {	int c = 0;
        int len = subjects.size();
        for(int i = 1; i < len; i++)
        {
            Subject temp = subjects.get(i);
            int j = i-1;
            while(j >= 0 && subjects.get(j).compareTo(temp) > 0)
            {
                c++;
                subjects.set(j+1, subjects.get(j));
                j--;
            }
            subjects.set(j+1, temp);
        }
    }
    // searches tutor's sessions available to see if it is present
    public boolean searchSession (NewDate date, NewTime time) {
        String dateAndTime = date.toString() + " " + time.toString();
        for (HashMap.Entry <String, Boolean> entry: this.sessionsAvailable.entrySet()) { // iterates through hashmap's keys
            if (dateAndTime.equals(entry.getKey())) { //  if hashmap's key equals key entered
                return true; // session found
            }
        }
        return false; // session not found
    }
}