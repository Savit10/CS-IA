import java.io.Serializable;

public class Subject implements Serializable{
    private String subjectName;
    private char subjectLevel;
    public Subject(String name, char level)
    {
        this.subjectName = name;
        this.subjectLevel = level;
    }

    public Subject ()
    {
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public char getSubjectLevel() {
        return subjectLevel;
    }

    public void setSubjectLevel(char subjectLevel) {
        this.subjectLevel = subjectLevel;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return subjectName + " " + subjectLevel;
    }


    public int compareTo(Subject subject) {
        int comparisonSubject = this.subjectName.compareTo(subject.subjectName);
        if (comparisonSubject==0) {
            return this.subjectLevel - subject.subjectLevel;
        }
        else {
            return comparisonSubject;
        }
    }
}