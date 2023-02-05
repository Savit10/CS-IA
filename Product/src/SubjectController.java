import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SubjectController{
    // list of subjects that exist in the system
    public static ArrayList<Subject> subjects = new ArrayList<Subject>();
    public static void main(String[] args) throws Exception {
        // loads subject data
        load();
        System.out.println("-------------------------------");
        System.out.println("Subject Management");
        Scanner sc = new Scanner(System.in);
        System.out.println("[A] to add Subject");
        System.out.println("[D] to delete Subject");
        System.out.println("[E] to edit Subject");
        char userActionInput = sc.next().charAt(0);
        while (userActionInput != 'A' && userActionInput != 'D' && userActionInput != 'E'){
            System.out.println("Incorrect input. Please try again");
            userActionInput = sc.next().charAt(0);
        }
        if (userActionInput == 'A')
        {
            try {
                addSubject(); // method to add a subject to the list
            }
            catch (NumberFormatException ex){ // catches a number format exception if entered in the method
                System.out.println("Incorrect input format");
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else if (userActionInput == 'D')
        {
            try {
                deleteSubject(); // method to delete a subject from the list
            } catch (Exception e) {
                throw new RuntimeException(e); // catches any runtime exception that is thrown by deleteSubject()
            }
        }
        else {
            try {
                editSubject(); // method to edit a subject from the list
            } catch (Exception e) {
                throw new RuntimeException(e); // catches any runtime exception that is thrown by editSubject()
            }
        }
        // saves subject's data
        save();
        // provides option to either go to the main menu of this class or return to the system's main menu
        returnToMainMenuOrManagement();
    }

    // provides option to either go to the main menu of this class or return to the system's main menu
    private static void returnToMainMenuOrManagement() throws Exception {
        System.out.println("-------------------------------");
        Scanner sc = new Scanner(System.in);
        System.out.println("[1] to return to Subject Management");
        System.out.println("[2] to return to Main Menu");
        int returnToWhat = sc.nextInt();
        while (returnToWhat != 1 && returnToWhat != 2) {
            System.out.println("Incorrect input.");
            System.out.println("[1] to return to Subject Management");
            System.out.println("[2] to return to Main Menu");
            returnToWhat = sc.nextInt();
        }
        if (returnToWhat == 1) {
            SubjectController.main(null);
        } else {
            Main.main(null);
        }
    }
    // method to input the subject's name and level
    public static Subject subjectInputs () {
        System.out.println("-------------------------------");
        Scanner sc = new Scanner(System.in);
        System.out.print("Subject name: ");
        String subjectName = sc.nextLine();
        System.out.print("Enter one character: H for HL, S for SL? ");
        char subjectLevel = sc.next().charAt(0);
        while (subjectLevel != 'H' && subjectLevel != 'S') {
            System.out.print("Wrong. Enter one character: H for HL, S for SL? ");
            subjectLevel = sc.next().charAt(0);
        }
        // returns an instantiated subject based on the inputs
        return new Subject(subjectName, subjectLevel);
    }
    // adds subject to overall list
    public static void addSubject () throws Exception
    {
        System.out.println("-------------------------------");
        subjects.add(subjectInputs()); // adds subject to overall list
        System.out.println("Subject Added");
    }
    // deletes subject from overall list
    public static void deleteSubject() throws Exception {
        System.out.println("-------------------------------");
        System.out.println("Deleting Subject");
        System.out.println(subjects);
        Subject subject = subjectInputs();
        if (searchSubject(subject)) { // if subject is found
            subjects.remove(searchSubjectReturnSubject(subject.getSubjectName(),subject.getSubjectLevel())); // remove subject from the list
            System.out.println("Subject Deleted");
        }
        else {
            System.out.println("Subject not found, cannot be deleted.");
        }
    }
    // edits a subject from the overall list
    public static void editSubject() throws Exception {
        System.out.println("-------------------------------");
        System.out.println("Editing Subject");
        Scanner sc = new Scanner(System.in);
        System.out.print("Subject name: ");
        String subjectName = sc.nextLine();
        System.out.print("Enter one character: H for HL, S for SL? ");
        char subjectLevel = sc.next().charAt(0);
        while (subjectLevel != 'H' && subjectLevel != 'S') {
            System.out.print("Wrong. Enter one character: H for HL, S for SL? ");
            subjectLevel = sc.next().charAt(0);
        }
        Subject subject = new Subject(subjectName, subjectLevel); // instantiates new subject to edit
        if (searchSubject(subject)) { // if the subject is found in the overall list
            Subject newSubject = subjectInputs(); // ask for new subject details
            if (!searchSubject(newSubject)) { // if the new subject is not found
                subjects.set(subjects.indexOf(searchSubjectReturnSubject(subjectName, subjectLevel)), newSubject); // set the existing subject's position to the new subject inputted
                System.out.println("Subject Edited");
            }
            else { // new subject is found
                System.out.println("Subject already exists, cannot edit");
            }
        }
        else { // subject doesn't exist
            System.out.println("Subject not found, cannot edit");
        }
    }
    // searches the overall list of subjects for a subject, returning true if found and false if not
    public static boolean searchSubject (Subject subject) throws IOException {
        insertionSort(); // sorts subjects list according to subject's name and level
        int low = 0;
        int high = subjects.size() - 1;
        while (low <= high) {
            int mid = (high + low) / 2; // middle of array
            if (subjects.get(mid).compareTo(subject) == 0) {
                return true;
            } else if (subjects.get(mid).compareTo(subject)< 0) { // higher sub-array
                low = mid + 1;
            } else { // lower sub-array
                high = mid - 1;
            }
        }
        return false;
    }

    // searches the list of subject's by name and level, returning the subject if found and null if not
    public static Subject searchSubjectReturnSubject (String subjectName, char subjectLevel) throws IOException {
        for (Subject subject: subjects) {
            if (subject.getSubjectName().equals(subjectName) && subject.getSubjectLevel() == (subjectLevel)) {
                return subject;
            }
        }
        return null;
    }

    // sorts subjects according to their names and levels
    public static ArrayList<Subject> insertionSort()
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
        return subjects;
    }

    // saves subjects data to file
    public static void save() throws IOException
    {
        System.out.println("-------------------------------");
        System.out.println("Saving changes to Subjects");
        try {
            FileOutputStream f = new FileOutputStream(new File("subjects.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(subjects); // writes subjects data to file
            o.close();
            f.close();
            System.out.println("Changes saved to file");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

    // loads subjects data from file
    public static void load() throws Exception
    {
        System.out.println("-------------------------------");
        System.out.println("Loading Subjects data");
        ArrayList<Subject> t = null;
        try {
            FileInputStream fi = new FileInputStream("subjects.txt");
            ObjectInputStream oi = new ObjectInputStream(fi);
            subjects = (ArrayList<Subject>) oi.readObject(); // reads subjects data from file
            oi.close();
            fi.close();
            System.out.println("Data loaded");
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        catch (IOException i) {
            System.out.println("Error initializing the data stream");
        }
        catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }

}
