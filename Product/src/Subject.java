import java.io.Serializable;
public class Subject implements Serializable{
    // attributes
    private String subjectName;
    private char subjectLevel;
    // constructor
    public Subject(String name, char level)
    {   this.subjectName = name;
        this.subjectLevel = level;}
    // accessors and mutators
    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    public char getSubjectLevel() { return subjectLevel;}
    public void setSubjectLevel(char subjectLevel) {
        this.subjectLevel = subjectLevel;
    }
    // toString - polymorphism
    @java.lang.Override
    public java.lang.String toString() { return "[" + subjectName + ", " +  subjectLevel + "]";}
    // compareTo - polymorphism
    public int compareTo(Subject subject) {
        int comparisonSubject = this.subjectName.compareTo(subject.subjectName);
        if (comparisonSubject==0) { return this.subjectLevel - subject.subjectLevel; }
        else { return comparisonSubject; }
    }
}