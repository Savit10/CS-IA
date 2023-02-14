import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
public class TutorController {
    // list of all tutors in the system
    public static ArrayList <Tutor> tutors = new ArrayList<>();
    public static void main(String[] args) throws Exception {
        // loads data from all the files needed
        load();
        TuteeController.load();
        SubjectController.load();
        SessionController.load();
        System.out.println("-------------------------------");
        System.out.println("Tutor Management");
        Scanner sc = new Scanner(System.in);
        System.out.println("[A] to add tutors");
        System.out.println("[D] to delete tutors");
        System.out.println("[E] to edit tutors");
        char userActionInput = sc.next().trim().charAt(0);
        while ((userActionInput) != 'A' && userActionInput != 'D' && userActionInput != 'E'){
            System.out.println("Incorrect input. Please try again");
            userActionInput = sc.next().trim().charAt(0);
        }
        if (userActionInput == 'A')
        {
            try {
                addTutor();
            }
            catch (NumberFormatException ex){ // validates possible exceptions in the method
                System.out.println("Incorrect input format");
            }
        }
        else if (userActionInput == 'D')
        {
            deleteTutor();
        }
        else {
            try {
                editTutor();
            } catch (IOException e) { // catches possible exceptions in the method
                throw new RuntimeException(e);
            }
        }
        // saves the data onto file
        save();
        TuteeController.save();
        SubjectController.save();
        SessionController.save();
        // provides options to return to either the main method of this class or the main menu of the system
        returnToMainMenuOrManagement();
    }

    // provides options to return to either the main method of this class or the main menu of the system
    private static void returnToMainMenuOrManagement() throws Exception {
        System.out.println("-------------------------------");
        Scanner sc = new Scanner(System.in);
        char returnToWhat;
        do {
            System.out.println("[T] to return to Tutor Management");
            System.out.println("[M] to return to Main Menu");
            returnToWhat = sc.next().trim().charAt(0);
        } while (returnToWhat != 'T' && returnToWhat != 'M');
        if (returnToWhat == 'T') {
            TutorController.main(null);
        } else {
            Main.main(null);
        }
    }

    // adds tutor to the tutors list
    public static void addTutor () throws Exception {
        // instantiates tutor using a null subjects taught list
        ArrayList<Subject> subjectsTaught = new ArrayList<>();
        Tutor tutor = new Tutor(subjectsTaught);
        Scanner sc = new Scanner(System.in);
        System.out.println("-------------------------------");
        System.out.println("Adding Tutor ");
        System.out.println("-------------------------------");
        System.out.print("Enter the tutor's name: ");
        String name = sc.nextLine().trim();
        tutor.setName(name); // sets the tutor's name
        int grade = 0;
        boolean continues = true;
        do {
            System.out.print("Enter tutor's grade (between 11 and 12): ");
            try {
                grade = Integer.parseInt(sc.next().trim()); // validates the integer data type input
                if (grade == 11 || grade == 12) { // checks the range of the data (11-12)
                    continues = false;
                }
                else {
                    System.out.println("Input outside given range");
                }
            }
            catch (NumberFormatException ex) {
                System.out.println("Incorrect input format");
            }
        } while (continues);
        tutor.setGradeLevel(grade); // sets tutor's grade
        System.out.println("Tutor Grade Set");
        boolean noMoreSubjects = false;
        System.out.println("Adding Subjects Tutor can teach");
        do {
            Subject subject = SubjectController.subjectInputs(); // inputs a subject
            tutor.getSubjectsTaught().add(subject); // adds the subject to the tutor's subjects list
            if (!SubjectController.searchSubject(subject)) // if the subject doesn't exist in the overall subjects list
            {
                SubjectController.subjects.add(subject); // adds subject to overall list
                // prints updated subjects list for user convinence
                System.out.print("Subject added to overall Subject list: ");
                System.out.println(SubjectController.subjects);
            }
            System.out.println("More subjects? Press Y / N: ");
            char moreSubjects = sc.next().trim().charAt(0);
            while (moreSubjects != 'Y' && moreSubjects != 'N') // validates yes/no inputs
            {
                System.out.print("Incorrect input. More subjects? Press Y / N: ");
                moreSubjects = sc.next().trim().charAt(0);
            }
            if (moreSubjects == 'N') // if no more subjects to be added
            {
                noMoreSubjects = true; // exit loop
            }
        } while (!noMoreSubjects);
        System.out.println("Subjects Added To Tutor List");
        System.out.println("Does the tutor have a tutee? Type Y if true, anything else if false: ");
        if (sc.next().trim().charAt(0) == 'Y') {
            System.out.println("Enter the tutee's name");
            sc.nextLine();
            name = sc.nextLine().trim();
            if (TuteeController.searchByName(name) == null) { // if tutee is not found
                System.out.println("Tutee not found.");
            }
            else { // if tutee is found
                tutor.setTutee(TuteeController.searchByName(name)); // sets tutor's tutee to the tutee that is found
                TuteeController.searchByName(name).setTutor(tutor); // sets the found tutee's tutor to the tutor being added
                System.out.println("Tutee Paired");
            }
        }
        System.out.println("-------------------------------");
        System.out.println("Adding Sessions Tutor is Available for");
        boolean noMoreSessions = false;
        do {
            NewDate sessionDate = SessionController.addSessionDateInput(); // date input
            NewTime sessionTime = SessionController.addSessionTimeInput(); // time input
            String sessionDateAndTime = sessionDate.toString() + " " + sessionTime.toString(); // key to search with
            if (SessionController.searchByDateAndTime(sessionTime, sessionDate) == null) // if session is not found using date and time
            {
                System.out.println("Session not found");
                noMoreSessions = true;
            }
            else {
                // creates hashmap of sessionsAvailable and adds the session to the hashmap, setting tutor's sessions
                HashMap<String, Boolean> sessionsAvailable = new HashMap<>();
                sessionsAvailable.put(sessionDateAndTime, true);
                tutor.setSessionsAvailable(sessionsAvailable);
                // adds tutor to the session's tutors list
                ArrayList<Tutor> tutors = new ArrayList<>();
                tutors.add(tutor);
                SessionController.searchByDateAndTime(sessionTime, sessionDate).setTutors(tutors);
                System.out.println("Session Added to Tutor");
            }
        } while (!noMoreSessions);
        tutors.add(tutor); // adds tutor to the tutors list
        System.out.println("Tutor Created and Added");
        System.out.println(tutor);
    }

    public static void deleteTutor() {
        System.out.println(tutors); // prints tutors for user convenience
        Scanner sc = new Scanner(System.in);
        System.out.println("-------------------------------");
        System.out.println("Deleting Tutor");
        System.out.println("Are you sure you want to delete a Tutor? Press Y/N: ");
        char delete = sc.next().trim().charAt(0);
        while (delete != 'Y' && delete != 'N') // validates yes/No inputs
        {
            System.out.println("Wrong input. Are you sure you want to delete a Tutor? Press Y/N: ");
            delete = sc.next().trim().charAt(0);
        }
        if (delete == 'Y') // if deleting is true
        {
            System.out.print("What is the tutor's name?: ");
            sc.nextLine();
            String name = sc.nextLine();
            if (searchByName(name) == null){ // if the tutor is not found
                System.out.println("No tutor found with that name");
            }
            else { // if tutor is found
                tutors.remove(searchByName(name)); // tutor is removed
                System.out.println("Tutor removed");
            }
        }
    }

    public static void editTutor() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("-------------------------------");
        System.out.println("Editing Tutor");
        System.out.print("Are you sure you would like to edit a tutor? Press Y/N: ");
        char input = sc.next().charAt(0);
        while (input != 'Y' && input != 'N'){ // validates yes/No inputs
            System.out.print("Wrong input. Are you sure you would like to edit a tutor? Press Y/N ");
            sc.nextLine();
            input = sc.next().charAt(0);
        }
        if (input == 'Y') // if the user wants to edit the tutor
        {
            System.out.print("What is the tutor's name?: ");
            sc.nextLine();
            String name = sc.nextLine();
            if (searchByName(name) == null){ // if the tutor is not found
                System.out.println("No tutor found with that name");
            }
            else { // if tutor is found
                editTutorInputs(searchByName(name));
            }
        }
    }

    public static void editTutorInputs(Tutor tutor) throws Exception
    {
        Scanner sc = new Scanner(System.in);
        boolean continues = true;
        System.out.println("Found tutor with name " + tutor.getName());
        do {
            System.out.println("What would you like to edit?");
            System.out.println("[G] to edit Grade");
            System.out.println("[S] to edit a Subject being Taught");
            System.out.println("[E] to edit a Session the Tutor is Available for");
            System.out.println("[T] to edit the Tutor's Tutee");
            System.out.println("[Q] to Quit Editing");
            char editInput = sc.next().charAt(0);
            if (editInput == 'G'|| editInput == 'g') {
                int grade = 0;
                do {
                    System.out.print("Enter tutor's grade (between 11 and 12): ");
                    try {
                        grade = Integer.parseInt(sc.next().trim()); // validates input to ensure it is an integer
                        if (grade == 11 || grade == 12) { // validates range of input
                            continues = false; // exits loop
                        }
                        else {
                            System.out.println("Input outside given range");
                        }
                    }
                    catch (NumberFormatException ex) { // incorrect type of data (not integer)
                        System.out.println("Incorrect input format");
                    }
                } while (continues);
                tutor.setGradeLevel(grade); // sets tutor's grade
                System.out.println("Grade Level Edited");
            }
            else if (editInput == 'S' || editInput == 's') {
                Subject subject = SubjectController.subjectInputs(); // subject inputs
                boolean subjectTutorPresence = tutor.searchSubjectByNameAndLevel(subject.getSubjectName(), subject.getSubjectLevel()); // searches tutor's subjects list
                if (subjectTutorPresence) { // if subject is found
                    System.out.println("Subject found for tutor. Press E for editing subject and D for deleting subject");
                    char editOrDelete = sc.next().charAt(0);
                    while (editOrDelete != 'E' && editOrDelete != 'D') {
                        System.out.println("Incorrect input. Press E for editing subject and D for deleting subject");
                        editOrDelete = sc.next().charAt(0);
                    }
                    if (editOrDelete == 'E') // if user wants to edit subject
                    {
                        System.out.print("What would you like to edit? Press S for subject name and L for subject Level");
                        char subjectEdit = sc.next().charAt(0);
                        while(subjectEdit != 'S' && subjectEdit != 'L'){
                            System.out.println("Incorrect input. What would you like to edit? Press S for subject name and L for subject Level: ");
                            subjectEdit = sc.next().charAt(0);
                        }
                        if (subjectEdit == 'S') // if user wants to edit the subject's name
                        {
                            System.out.print("Enter subject's new name: ");
                            sc.nextLine();
                            tutor.searchSubjectByNameAndLevelAndReturnSubject(subject.getSubjectName(), subject.getSubjectLevel()).setSubjectName(sc.nextLine()); // searches for a subject in the tutor's subjects list and edits the name
                            System.out.println("Subject's Name Edited");
                        }
                        else {
                            System.out.print("Enter subject's new level");
                            sc.nextLine();
                            tutor.searchSubjectByNameAndLevelAndReturnSubject(subject.getSubjectName(), subject.getSubjectLevel()).setSubjectLevel(sc.next().charAt(0)); // searches for a subject in the tutor's subjects list and edits the level of the subject
                            System.out.println("Subject's Level Edited");
                        }
                    }
                    else {
                        tutor.getSubjectsTaught().remove(tutor.searchSubjectByNameAndLevelAndReturnSubject(subject.getSubjectName(), subject.getSubjectLevel())); // searches for a subject in the tutor's subjects list and removes it
                        System.out.println("Subject remove from Tutor's List");
                    }
                }
                else { // if subject is not found in the overall subjects list
                    System.out.println("Subject not found ");
                }
            }
            else if (editInput == 'E' || editInput == 'e') {
                NewDate date = SessionController.addSessionDateInput(); // date input
                NewTime time = SessionController.addSessionTimeInput(); // time input
                Session session = SessionController.searchByDateAndTime(time, date); // searches overall sessions list using time and date
                String sessionKey = date.toString() + " " + time.toString(); // date+ time key to search with
                try {
                    if (tutor.searchSession(date, time) && tutor.getSessionsAvailable().get(sessionKey)) { // searches if the session is in the tutor's list and if it is set to "true", indicating they are actively tutoring in that session
                        System.out.println("Session found. Would you like to remove this tutor from this session? Press Y");
                        char yesOrNo = sc.next().charAt(0);
                        if (yesOrNo == 'Y') {
                            tutor.getSessionsAvailable().replace(sessionKey, false); // doesn't permanently remove the tutor from the session but instead sets its value to "False", indicating they are not tutoring in this session anymore but may do so later
                            System.out.println("Session availability set to false");
                        }
                    }
                    else if (session != null) { // session is found in the overall sessions list
                        System.out.println("Session doesn't exist in tutor's list. Would you like to have the tutor in this session? Press Y");
                        char yesOrNo = sc.next().charAt(0);
                        if (yesOrNo == 'Y') {
                            try {
                                if (!tutor.getSessionsAvailable().get(sessionKey)) { // if tutor's session's key is false
                                    tutor.getSessionsAvailable().replace(sessionKey, true); // changes the tutor's sessions available to true, indicating the tutor not tuors in this session
                                    session.getTutors().add(tutor); // adds the tutor to the list of tutors in the session
                                }
                            } catch (NullPointerException ex) { // if the tutor's sessions available hashmap is null, this exception will be thrown
                                //instantiates a hashmap sessionsAvailable, adds the session, sets the tutor's sessionsAvailable to this hashmap, adds the tutor to the session
                                HashMap<String, Boolean> sessionsAvailable = new HashMap<>();
                                sessionsAvailable.put(sessionKey, true);
                                tutor.setSessionsAvailable(sessionsAvailable);
                                session.getTutors().add(tutor);
                            }
                            System.out.println("Tutor added to Session");
                        }
                        else {
                            System.out.println("Session doesn't exist");
                        }
                    }
                } catch (Exception e) { // exception that the tutor doesn't have a list of sessions
                    if (session != null) { // if session is found in overall list but not in tutor's list
                        System.out.println("Session doesn't exist in tutor's list. Would you like to have the tutor in this session? Press Y");
                        char yesOrNo = sc.next().charAt(0);
                        if (yesOrNo == 'Y') {
                            try {
                                if (!tutor.getSessionsAvailable().get(sessionKey)) { // if tutor's session's key is false
                                    tutor.getSessionsAvailable().replace(sessionKey, true); // changes the tutor's sessions available to true, indicating the tutor not tuors in this session
                                    session.getTutors().add(tutor); // adds the tutor to the list of tutors in the session
                                }
                            } catch (NullPointerException ex) {
                                //instantiates a hashmap sessionsAvailable, adds the session, sets the tutor's sessionsAvailable to this hashmap, adds the tutor to the session
                                HashMap<String, Boolean> sessionsAvailable = new HashMap<>();
                                sessionsAvailable.put(sessionKey, true);
                                tutor.setSessionsAvailable(sessionsAvailable);
                                session.getTutors().add(tutor);
                            }
                            System.out.println("Tutor added to session");
                        }
                        else {
                            System.out.println("Session doesn't exist");
                        }
                    }
                }
            }
            else if (editInput == 'T' || editInput == 't') {
                int switchInput = 0;
                boolean correctInput = false;
                System.out.print("What name does the current tutee have? ");
                sc.nextLine();
                String name = sc.nextLine();
                if (TuteeController.searchByName(name) == null) { // if tutee is not f
                    System.out.println("Tutee doesn't exist");
                }
                else { // if tutee is found
                    while (!correctInput) {
                        try {
                            System.out.println("[1] To change tutee");
                            System.out.println("[2] To not have a tutee right now");
                            switchInput = Integer.parseInt(sc.next()); // validates input type
                            correctInput = true;
                        } catch (Exception ex) {
                            System.out.println("Incorrect input.");
                        }
                    }
                    switch (switchInput) {
                        case 1:
                            System.out.print("What name does the new tutee have? ");
                            sc.nextLine();
                            String newTuteeName = sc.nextLine();
                            if (TuteeController.searchByName(newTuteeName) == null) { // if new tutee isn't found
                                System.out.println("New Tutee doesn't exist");
                            }
                            else {
                                tutor.setTutee(TuteeController.searchByName(newTuteeName)); // sets tutor's tutee to the new tutee
                                TuteeController.searchByName(name).setTutor(null); // sets old tutee's tutor to null
                                TuteeController.searchByName(newTuteeName).setTutor(tutor); // sets new tutee's tutor to the tutor, pairing them both
                                System.out.println("New Tutee paired with Tutor");
                            }
                            break;
                        case 2:
                            tutor.setTutee(null); // sets the tutor's tutee to null
                            TuteeController.searchByName(name).setTutor(null); // sets the tutee's tutor to null, unpairing them
                            System.out.println("Tutor and Tutee unpaired");
                            break;
                        default:
                            System.out.println("Incorrect input");
                            break;
                    }
                }
            }
            else if (editInput == 'Q' || editInput == 'q') {
                continues = false; // tries to exit loop
                break;
            }
            else {
                System.out.println("Incorrect input");
            }
            System.out.print("Would you like to edit anything else about this tutor? Press Y: ");
            if (sc.next().charAt(0) == 'Y') {
                continues = true; // continues loop
            }
        } while(continues);
    }

    // searches the tutors list by name, returning a tutor
    public static Tutor searchByName(String name){ // linear search by name since name is not unique
        for (Tutor tutor : tutors) {
            if (tutor.getName().equals(name)) {
                return tutor;
            }
        }
        return null;
    }
    // serializes the tutors object to file
    public static void save()
    {
        System.out.println("-------------------------------");
        System.out.println("Saving changes to Tutors");
        try {
            FileOutputStream f = new FileOutputStream("tutors.txt"); //creates new File
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(tutors); //Writes tutors object to file
            System.out.println("Changes saved to file");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }
    // deserializes the tutors object from file
    public static void load(){
        System.out.println("-------------------------------");
        System.out.println("Loading Tutors Data");
        try {
            FileInputStream fi = new FileInputStream("tutors.txt"); //attempts to open file
            ObjectInputStream oi = new ObjectInputStream(fi);
            tutors = (ArrayList<Tutor>) oi.readObject(); //reads object from file and casts it to arraylist
            System.out.println("Data Loaded");
        }
        catch (FileNotFoundException e) {System.out.println("File not found");}
        catch (IOException i) {System.out.println("Error initializing the data stream");}
        catch (ClassNotFoundException c) {c.printStackTrace();}
    }
}