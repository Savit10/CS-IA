import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
public class Tutee extends Person implements Serializable{
    // attributes
    private int gradeLevel;
    private ArrayList<Subject> subjectsLearning;
    private HashMap<String, Boolean> sessionsAvailable;
    private Tutor tutor;

    // constructor
    public Tutee(ArrayList<Subject> subjectsLearning) {
        this.subjectsLearning = subjectsLearning;
    }

    //toString - polymorphism
    @Override
    public String toString() {
        if (this.getTutor() == null) { // if the tutor of the tutee is null
            return "Tutee {" +
                    "Name = " + this.getName()+
                    ", gradeLevel =" + this.getGradeLevel() +
                    ", subjectsLearning =" + this.getSubjectsLearning() +
                    ", sessionsAvailable =" + this.toStringSessionsAvailable() +
                    ", tutor = null}"; // print null for tutor
        }
        else { // if tutor is not null
            return "Tutor {" +
                    "Name = " + this.getName() +
                    ", gradeLevel =" + this.getGradeLevel() +
                    ", subjectsLearning =" + this.getSubjectsLearning() +
                    ", sessionsAvailable =" + this.toStringSessionsAvailable() +
                    ", tutee =" + this.getTutor().getName() + // print just the name of the tutee to prevent an infinite call of tutor and tutee in String form
                    " }";
        }
    }

    // to String method to specifically print out sessions available in a format
    public String toStringSessionsAvailable()
    {
        String sessionsavailable = "";
        if (sessionsAvailable != null) {
            for (String string: sessionsAvailable.keySet()) // iterates through keys of the hashmap
            {
                sessionsavailable += "[" + string + " = " + sessionsAvailable.get(string) + "],";
            }
        }
        return "{"+sessionsavailable+"}" ;
    }

    // accessors and mutators of Tutee
    public void setName(String name) {
        super.setName(name);
    }
    public String getName() {return super.getName();}
    public int getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public ArrayList<Subject> getSubjectsLearning() {
        return subjectsLearning;
    }

    public HashMap<String, Boolean> getSessionsAvailable() {
        return sessionsAvailable;
    }

    public void setSessionsAvailable(HashMap<String, Boolean> sessionsAvailable) {
        this.sessionsAvailable = sessionsAvailable;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }


    // searches the tutee's list of sessions available for date and time to see if that session is present
    public boolean searchSession (NewDate date, NewTime time) {
        String dateAndTime = date.toString() + " " + time.toString();
        for (HashMap.Entry <String, Boolean> entry: this.sessionsAvailable.entrySet()) { // iterates through HashMap's keys
            if (dateAndTime.equals(entry.getKey())) { // if the key of date+time equals the entry's key
                return true; // session is found
            }
        }
        return false; // session is not found
    }

    // searches the tutee's list of subjects by name and level to see if it is present
    public boolean searchSubjectByNameAndLevel (String subjectName, char subjectLevel) throws IOException {
        this.insertionSort();
        int low = 0;
        int high = this.subjectsLearning.size() - 1;
        while (low <= high) {
            int mid = (high + low) / 2; // index of middle of sub-array
            if (this.subjectsLearning.get(mid).getSubjectName().equals(subjectName) && this.subjectsLearning.get(mid).getSubjectLevel() == (subjectLevel)) { // if middle index's subject is the subject looking for
                return true; // subject is found
            }
            else if (this.subjectsLearning.get(mid).getSubjectName().compareTo(subjectName)<0) {
                low = mid + 1; // subject is in higher sub-array
            }
            else {
                high = mid - 1; // subject is in lower sub-array
            }
        }
        return false; // subject not found
    }

    // searches the tutee's list of subjects by name and level, returning the subject if found and null if not
    public Subject searchSubjectByNameAndLevelAndReturnSubject(String subjectName, char subjectLevel) {
        this.insertionSort();
        int low = 0;
        int high = this.subjectsLearning.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2; // middle index of sub-array
            if (this.subjectsLearning.get(mid).getSubjectName().equals(subjectName) && this.subjectsLearning.get(mid).getSubjectLevel() == (subjectLevel)) { // if this subject is the one looked for
                return subjectsLearning.get(mid); // subject found
            }
            else if (this.subjectsLearning.get(mid).getSubjectName().compareTo(subjectName)<0) {
                low = mid + 1; // subject in higher sub-array
            }
            else {
                high = mid - 1; // subject in lower sub-array
            }
        }
        return null; // subject not found
    }

    // sorting subject by name and level
    public ArrayList<Subject> insertionSort()
    {	int c = 0;
        int len = this.subjectsLearning.size();
        for(int i = 1; i < len; i++)
        {
            Subject temp = this.subjectsLearning.get(i);
            int j = i-1;
            while(j >= 0 && this.subjectsLearning.get(j).compareTo(temp) > 0)
            {
                c++;
                this.subjectsLearning.set(j+1, this.subjectsLearning.get(j));
                j--;
            }
            this.subjectsLearning.set(j+1, temp);
        }
        return this.subjectsLearning;
    }
}
