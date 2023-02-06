import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
public class TuteeController{
        // list of tutees in the system
        public static ArrayList <Tutee> tutees = new ArrayList<>();
        public static void main(String[] args) throws Exception {
            // loading data from the different classes
            load();
            TutorController.load();
            SubjectController.load();
            SessionController.load();
            System.out.println("-------------------------------");
            System.out.println("Tutee Management");
            Scanner sc = new Scanner(System.in);
            System.out.println("[A] to add tutees");
            System.out.println("[D] to delete tutees");
            System.out.println("[E] to edit tutees");
            char userActionInput = sc.next().charAt(0);
            while ((userActionInput) != 'A' && userActionInput != 'D' && userActionInput != 'E'){
                System.out.println("Incorrect input. Please try again");
                userActionInput = sc.next().charAt(0);
            }
            if (userActionInput == 'A')
            {
                try {
                    addTutee(); // method to call adding tutee
                }
                catch (NumberFormatException ex){
                    System.out.println("Incorrect input format");
                }
            }
            else if (userActionInput == 'D')
            {
                deleteTutee(); // method to call deleting tutee
            }
            else {
                try {
                    editTutee(); // method to call editing tutee
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            // saving data to all the files
            save();
            TutorController.save();
            SessionController.save();
            SubjectController.save();
            // provides options to return to either the main method of this class or the main menu of the system
            returnToMainMenuOrManagement();
        }
        // provides options to return to either the main method of this class or the main menu of the system
        private static void returnToMainMenuOrManagement() throws Exception {
            System.out.println("-------------------------------");
            Scanner sc = new Scanner(System.in);
            char returnToWhat;
            do {
                System.out.println("[T] to return to Tutee Management");
                System.out.println("[M] to return to Main Menu");
                returnToWhat = sc.next().charAt(0);
            } while (returnToWhat != 'T' && returnToWhat != 'M');
            if (returnToWhat == 'T') {
                TuteeController.main(null);
            } else {
                Main.main(null);
            }
        }

        public static void addTutee () throws Exception {
            ArrayList<Subject> subjectsLearning = new ArrayList<>();
            Tutee tutee = new Tutee(subjectsLearning); // instantiating tutee to add the other attributes to
            Scanner sc = new Scanner(System.in);
            System.out.println("-------------------------------");
            System.out.println("Adding Tutee");
            System.out.println("-------------------------------");
            System.out.print("Enter the tutee's name: ");
            String name = sc.nextLine().trim();
            tutee.setName(name); // setting name of tutee
            System.out.println("Tutee's name is set.");
            int grade = 0;
            boolean continues = true;
            do {
                System.out.print("Enter tutee's grade (between 11 and 12): ");
                try {
                    grade = Integer.parseInt(sc.next()); // validating grade's integer input
                    if (grade == 11 || grade == 12) {
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
            tutee.setGradeLevel(grade); // setting grade of tutee
            System.out.println("Tutee's grade is set");
            boolean noMoreSubjects = false;
            System.out.println("Adding Subjects Tutee will learn");
            do {
                Subject subject = SubjectController.subjectInputs(); // inputs subject
                tutee.getSubjectsLearning().add(subject); // adds subject to tutee's list of subject's learning
                if (!SubjectController.searchSubject(subject)) // if the subject is not found in the overall list of subjects
                {
                    SubjectController.subjects.add(subject); // adds subject to overall list
                }
                System.out.println("More subjects? Press Y / N: ");
                char moreSubjects = sc.next().charAt(0);
                while (moreSubjects != 'Y' && moreSubjects != 'N') // validates yes/no input, keeps asking if incorrect
                {
                    System.out.print("Incorrect input. More subjects? Press Y / N: ");
                    moreSubjects = sc.next().charAt(0);
                }
                if (moreSubjects == 'N') // if no more subjects,
                {
                    noMoreSubjects = true; // stop inputting subjects
                }
            } while (!noMoreSubjects);
            System.out.println("Tutee's Subjects to learn is set");
            System.out.println("Does the tutee have a tutor? Type Y if true, anything else if false: ");
            if (sc.next().charAt(0) == 'Y') {
                System.out.println("Enter the tutor's name");
                sc.nextLine();
                name = sc.nextLine();
                if (TutorController.searchByName(name) == null) { // if the tutor is not found in the tutors list
                    System.out.println("Tutor not found.");
                }
                else {
                    tutee.setTutor(TutorController.searchByName(name)); // set tutor of the tutee to the tutor that is found
                    TutorController.searchByName(name).setTutee(tutee); // set tutee of the tutor found to the tutee, pairing both of them together
                    System.out.println("Tutor and tutee paired");
                }
            }
            System.out.println("-------------------------------");
            System.out.println("Adding Sessions Tutee is available for");
            boolean noMoreSessions = false;
            do {
                NewDate sessionDate = SessionController.addSessionDateInput(); // date input
                NewTime sessionTime = SessionController.addSessionTimeInput(); // time input
                String sessionDateAndTime = sessionDate.toString() + " " + sessionTime.toString();
                if (SessionController.searchByDateAndTime(sessionTime, sessionDate) == null) // if not found in the overall sessions list
                {
                    System.out.println("Session not found");
                    noMoreSessions = true; // exit the loop and stop asking for sessions
                }
                else { // if found in overall sessions list
                    // create a Hashmap, add the session, and set the tutee's sessions available to the hashmap
                    HashMap<String, Boolean> sessionsAvailable = new HashMap<>();
                    sessionsAvailable.put(sessionDateAndTime, true);
                    tutee.setSessionsAvailable(sessionsAvailable);
                    // creating a tutees list, adding the tutee and setting the sessions list to this tutees list
                    ArrayList<Tutee> tutees = new ArrayList<>();
                    tutees.add(tutee);
                    SessionController.searchByDateAndTime(sessionTime, sessionDate).setTutees(tutees);
                    System.out.println("Session Added to Tutee's list of available sessions");
                }
            } while (!noMoreSessions);
            tutees.add(tutee); // adds tutee to overall tutees list
            System.out.println("Tutee Added");
        }

        public static void deleteTutee() {
            System.out.println(tutees); // prints tutees for user's reference
            Scanner sc = new Scanner(System.in);
            System.out.println("-------------------------------");
            System.out.println("Deleting Tutee");
            System.out.println("Are you sure you want to delete a Tutee? Press Y/N: ");
            char delete = sc.next().charAt(0);
            while (delete != 'Y' && delete != 'N')
            {
                System.out.println("Wrong input. Are you sure you want to delete a Tutee? Press Y/N: ");
                delete = sc.next().charAt(0);
            }
            if (delete == 'Y')
            {
                System.out.print("What is the tutee's name?: ");
                sc.nextLine();
                String name = sc.nextLine();
                if (searchByName(name) == null){ // if tutee is not found
                    System.out.println("No tutee found with that name");
                }
                else { // if tutee is found
                    tutees.remove(searchByName(name)); // remove the tutee from the list
                    System.out.println("Tutee removed");
                }
            }
        }

        public static void editTutee() throws Exception {
            Scanner sc = new Scanner(System.in);
            System.out.println("-------------------------------");
            System.out.println("Editing Tutee");
            System.out.print("Are you sure you would like to edit a tutee? Press Y/N: ");
            char input = sc.next().charAt(0);
            while (input != 'Y' && input != 'N'){
                System.out.print("Wrong input. Are you sure you would like to edit a tutee? Press Y/N ");
                sc.nextLine();
                input = sc.next().charAt(0);
            }
            if (input == 'Y')
            {
                System.out.print("What is the tutee's name?: ");
                sc.nextLine();
                String name = sc.nextLine();
                if (searchByName(name) == null){ // if tutee is not found in the list
                    System.out.println("No tutee found with that name");
                }
                else {
                    editTuteeInputs(searchByName(name));
                }
            }
        }

        public static void editTuteeInputs(Tutee tutee) throws Exception
        {
            Scanner sc = new Scanner(System.in);
            boolean continues = false;
            System.out.println("Found tutee with name " + tutee.getName());
            do {
                System.out.println("What would you like to edit?");
                System.out.println("[G] to edit Grade");
                System.out.println("[S] to edit a Subject being Learnt");
                System.out.println("[E] to edit a Session the Tutee is Available for");
                System.out.println("[T] to edit the Tutee's Tutor");
                System.out.println("[Q] to Quit Editing");
                char editInput = sc.next().charAt(0);
                if (editInput == 'G'|| editInput == 'g') {
                    int grade = 0;
                    do {
                        System.out.print("Enter tutee's grade (between 11 and 12): ");
                        try {
                            grade = Integer.parseInt(sc.next().trim()); // validates grade entry type and range
                            if (grade == 11 || grade == 12) {
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
                    tutee.setGradeLevel(grade); // edits tutee's grade level
                    System.out.println("Tutee's Grade Edited");
                }
                else if (editInput == 'S' || editInput == 's') {
                    Subject subject = SubjectController.subjectInputs(); // asks for a subject to edit
                    boolean subjectTuteePresence = tutee.searchSubjectByNameAndLevel(subject.getSubjectName(), subject.getSubjectLevel()); // checks whether subject is in tutee's subjects list or not
                    if (subjectTuteePresence) { // if subject is found in the tutee's list
                        System.out.println("Subject found for tutee. Press E for editing subject and D for deleting subject");
                        char editOrDelete = sc.next().charAt(0);
                        while (editOrDelete != 'E' && editOrDelete != 'D') {
                            System.out.println("Incorrect input. Press E for editing subject and D for deleting subject");
                            editOrDelete = sc.next().charAt(0);
                        }
                        if (editOrDelete == 'E') // if the user wants to edit the subject
                        {
                            System.out.print("What would you like to edit? Press S for subject name and L for subject Level");
                            char subjectEdit = sc.next().charAt(0);
                            while(subjectEdit != 'S' && subjectEdit != 'L'){
                                System.out.println("Incorrect input. What would you like to edit? Press S for subject name and L for subject Level: ");
                                subjectEdit = sc.next().charAt(0);
                            }
                            if (subjectEdit == 'S') // if the user wants to enter the subject's name
                            {
                                System.out.print("Enter subject's new name: ");
                                sc.nextLine();
                                tutee.searchSubjectByNameAndLevelAndReturnSubject(subject.getSubjectName(), subject.getSubjectLevel()).setSubjectName(sc.nextLine()); // sets the subject name as the next line's input
                                System.out.println("Subject Name Edited");
                            }
                            else { // if the user wants to enter the subject's level
                                System.out.print("Enter subject's new level");
                                sc.nextLine();
                                tutee.searchSubjectByNameAndLevelAndReturnSubject(subject.getSubjectName(), subject.getSubjectLevel()).setSubjectLevel(sc.next().charAt(0)); // sets the subject level as the next line's input
                                System.out.println("Subject Level Edited");
                            }
                        }
                        else { // the user wants to delete the subject
                            tutee.getSubjectsLearning().remove(tutee.searchSubjectByNameAndLevelAndReturnSubject(subject.getSubjectName(), subject.getSubjectLevel())); //searches the tutee's subjects list for the subject and removes it
                            System.out.println("Subject Removed");
                        }
                    }
                    else { // if subject is not found
                        System.out.println("Subject not found.");
                    }
                }
                else if (editInput == 'E' || editInput == 'e') {
                    NewDate date = SessionController.addSessionDateInput(); // date input
                    NewTime time = SessionController.addSessionTimeInput(); // time input
                    Session session = SessionController.searchByDateAndTime(time, date); // searches the overall list of sessions
                    String sessionKey = date.toString() + " " + time.toString(); // key to search the tutee's list of sessions available
                    try {
                        if (tutee.searchSession(date, time) && tutee.getSessionsAvailable().get(sessionKey)) { // searches if the session is in the tutor's list and if it is set to "true", indicating they are actively tutoring in that session
                            System.out.println("Session found. Would you like to remove this tutee from this session? Press Y");
                            char yesOrNo = sc.next().charAt(0);
                            if (yesOrNo == 'Y') {
                                try {
                                    tutee.getSessionsAvailable().replace(sessionKey, false); // doesn't permanently remove the tutor from the session but instead sets its value to "False", indicating they are not tutoring in this session anymore but may do so later
                                } catch (RuntimeException r) {
                                    System.out.println("Session doesn't have any tutees. No removing is needed.");
                                }
                            }
                        }
                        else if (session != null) { // session doesn't exist in the tutee's list but exists in the overall list of sessions
                            System.out.println("Session doesn't exist in tutee's list. Would you like to have the tutee in this session? Press Y");
                            char yesOrNo = sc.next().charAt(0);
                            if (yesOrNo == 'Y') {
                                try {
                                    if (!tutee.getSessionsAvailable().get(sessionKey)) { // if the boolean value of the session is false, but it is in the list of sessions
                                        tutee.getSessionsAvailable().replace(sessionKey, true); // set the boolean to true
                                        session.getTutees().add(tutee); // adds tutee to the list of tutees in the session
                                    }
                                } catch (NullPointerException ex) { // exception if the tutee doesn't;t have an existing sessions available Hashmap
                                    // instantiates a hashmap, adds the session, sets the tutee's sessionsAvailable to the hashmap, and adds tutee to the session's list of tutee
                                    HashMap<String, Boolean> sessionsAvailable = new HashMap<>();
                                    sessionsAvailable.put(sessionKey, true);
                                    tutee.setSessionsAvailable(sessionsAvailable);
                                    session.getTutees().add(tutee);
                                }
                                System.out.println("Tutee Added to Session");
                            }
                            else {
                                System.out.println("Session doesn't exist");
                            }
                        }
                    } catch (Exception e) { // catches exception that the tutee doesn't have a list of sessions
                        if (session != null) { // if session is in the overall list of sessions
                            System.out.println("Session doesn't exist in tutee's list. Would you like to have the tutor in this session? Press Y");
                            char yesOrNo = sc.next().charAt(0);
                            if (yesOrNo == 'Y') {
                                try {
                                    if (!tutee.getSessionsAvailable().get(sessionKey)) { // if the boolean value of the session is false, but it is in the list of sessions
                                        tutee.getSessionsAvailable().replace(sessionKey, true); // set the boolean to true
                                        session.getTutees().add(tutee); // adds tutee to the list of tutees in the session
                                    }
                                } catch (NullPointerException ex) { // exception if the tutee doesn;t have an existing sessions available Hashmap
                                    // instantiates a hashmap, adds the session, sets the tutee's sessionsAvailable to the hashmap, and adds tutee to the session's list of tutee
                                    HashMap<String, Boolean> sessionsAvailable = new HashMap<>();
                                    sessionsAvailable.put(sessionKey, true);
                                    tutee.setSessionsAvailable(sessionsAvailable);
                                    session.getTutees().add(tutee);
                                }
                                System.out.println("Tutee added to Session");
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
                    System.out.print("What name does the current tutor have? ");
                    sc.nextLine();
                    String name = sc.nextLine();
                    if (TutorController.searchByName(name) == null) { // if tutor is not found
                        System.out.println("Tutor doesn't exist");
                    }
                    else {
                        while (!correctInput) {
                            try {
                                System.out.println("[1] To change tutor");
                                System.out.println("[2] To not have a tutor right now");
                                switchInput = Integer.parseInt(sc.next()); // validates input of integer
                                correctInput = true;
                            } catch (Exception ex) {
                                System.out.println("Incorrect input format. Enter an integer");
                            }
                        }
                        switch (switchInput) {
                            case 1:
                                System.out.print("Enter the new tutor's name: ");
                                sc.nextLine();
                                String newTutorName = sc.nextLine();
                                if (TutorController.searchByName(newTutorName) == null) { // if new tutor is not found
                                    System.out.println("New Tutor doesn't exist");
                                }
                                else {
                                    tutee.setTutor(TutorController.searchByName(newTutorName)); // sets tutee's tutor to the new tutor
                                    TutorController.searchByName(name).setTutee(null); // sets the old tutor's tutee to null, unpairing them
                                    TutorController.searchByName(newTutorName).setTutee(tutee); // sets the new tutor's tutee to the tutee
                                    System.out.println("New Tutor and Tutee paired");
                                }
                                break;
                            case 2:
                                tutee.setTutor(null); // sets the tutee's tutor to null
                                TutorController.searchByName(name).setTutee(null); // sets the tutor's tutee to null, unpairing the two of them
                                System.out.println("Tutee and Tutor Unpaired");
                                break;
                            default:
                                System.out.println("Incorrect input");
                                break;
                        }
                    }
                }
                else if (editInput == 'Q' || editInput == 'q') {
                    continues = false; // breaks the loop
                    break;
                }
                else {
                    System.out.println("Incorrect input");
                }
                System.out.print("Would you like to edit anything else about this tutee? Press Y: ");
                if (sc.next().charAt(0) == 'Y') {
                    continues = true; // continues the loop
                }
            } while(continues);
        }

        // searches the tutees list by name, returning a tutee
        public static Tutee searchByName(String name){ // linear search by name since name is not unique
            for (Tutee tutee : tutees) {
                if (tutee.getName().equals(name)) {
                    return tutee;
                }
            }
            return null;
        }

        // serializes the data to files.
        public static void save()
        {
            System.out.println("-------------------------------");
            System.out.println("Saving changes to Tutees");
            try {
                FileOutputStream f = new FileOutputStream("tutees.txt"); //creates new File
                ObjectOutputStream o = new ObjectOutputStream(f);
                o.writeObject(tutees); //writes tutees object to file
                System.out.println("Changes saved to file");
            } catch (IOException e) {
                System.out.println("Error initializing stream");
            }
        }
        // deserializes data from .txt file,
        public static void load()
        {
            System.out.println("-------------------------------");
            System.out.println("Loading Tutees data");
            try {
                FileInputStream fi = new FileInputStream("tutees.txt"); //attempts to open file
                ObjectInputStream oi = new ObjectInputStream(fi);
                tutees = (ArrayList<Tutee>) oi.readObject(); //reads object from file and casts it to arraylist
                System.out.println("Data Loaded");
            }
            catch (FileNotFoundException e) {System.out.println("File not found");}
            catch (IOException i) {System.out.println("Error initializing the data stream");}
            catch (ClassNotFoundException c) {c.printStackTrace();}
        }
    }
