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

    @Override
    public String toString() {
        return "Tutee{" +
                "gradeLevel=" + gradeLevel +
                ", subjectsLearning=" + toStringSubjectsLearning() +
                ", sessionsAvailable=" + toStringSessionsAvailable() +
                ", tutor=" + tutor +
                '}';
    }

    public Tutee (String name)
    {
        super(name);
    }

    public void setName(String name) {
        super.setName(name);
    }

    public String getName() {return super.getName();}
    public Tutee(ArrayList<Subject> subjectsLearning) {
        this.subjectsLearning = subjectsLearning;
    }
    public int getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(int gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public ArrayList<Subject> getSubjectsLearning() {
        return subjectsLearning;
    }

    public void setSubjectsLearning(ArrayList<Subject> subjectsLearning) {
        this.subjectsLearning = subjectsLearning;
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

    public String toStringSubjectsLearning ()
    {
        String toString = "";
        for (Subject subject : subjectsLearning) {
            toString = toString + subject.toString() + ", ";
        }
        return toString;
    }

    public String toStringSessionsAvailable()
    {
        String sessionsavailable = "";
        if (sessionsAvailable!= null) {
            for (HashMap.Entry<String, Boolean> entry : sessionsAvailable.entrySet()) // adapted from https://www.geeksforgeeks.org/iterate-map-java/
            {
                if (entry.getValue()) {
                    sessionsavailable = sessionsavailable + entry.getKey() + " ,";
                }
            }
        }
        return "Sessions available: " + sessionsavailable ;
    }

    public Subject searchSubject (String subjectName, char subjectLevel) throws IOException {
        int low = 0;
        int high = this.subjectsLearning.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (this.subjectsLearning.get(mid).getSubjectName().equals(subjectName) && this.subjectsLearning.get(mid).getSubjectLevel() == (subjectLevel)) {
                return subjectsLearning.get(mid);
            }
            else if (this.subjectsLearning.get(mid).getSubjectName().compareTo(subjectName)<0) {
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

    public boolean searchSubjectByNameAndLevel (String subjectName, char subjectLevel) throws IOException {
        int low = 0;
        int high = this.subjectsLearning.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (this.subjectsLearning.get(mid).getSubjectName().equals(subjectName) && this.subjectsLearning.get(mid).getSubjectLevel() == (subjectLevel)) {
                return true;
            }
            else if (this.subjectsLearning.get(mid).getSubjectName().compareTo(subjectName)<0) {
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
        }
        return false;
    }

    public Subject searchSubjectByNameAndLevelAndReturnSubject(String subjectName, char subjectLevel) {
        int low = 0;
        int high = this.subjectsLearning.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (this.subjectsLearning.get(mid).getSubjectName().equals(subjectName) && this.subjectsLearning.get(mid).getSubjectLevel() == (subjectLevel)) {
                return subjectsLearning.get(mid);
            }
            else if (this.subjectsLearning.get(mid).getSubjectName().compareTo(subjectName)<0) {
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
        }
        return null;
    }
}
