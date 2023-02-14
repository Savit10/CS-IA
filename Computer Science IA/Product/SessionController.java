import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class SessionController {
    static ArrayList <Session> sessions = new ArrayList <Session> (); //sessions that exist in the system

    public static void main(String[] args) throws Exception {
        // loads data from classes that are needed
        load();
        TutorController.load();
        TuteeController.load();
        System.out.println("-------------------------------");
        System.out.println("Session Management");
        System.out.println("-------------------------------");
        Scanner sc = new Scanner(System.in);
        System.out.println("Press A to add Sessions");
        System.out.println("Press D to delete Sessions");
        System.out.println("Press E to edit Sessions");
        char userActionInput = sc.next().charAt(0);
        while (userActionInput != 'A' && userActionInput != 'D' && userActionInput != 'E'){
            System.out.println("Incorrect input. Please try again");
            userActionInput = sc.next().charAt(0);
        }
        if (userActionInput == 'A')
        {
            try {
                addSession();
            }
            catch (Exception ex){
                throw new RuntimeException(ex);
            }
        }
        else if (userActionInput == 'D')
        {
            try {
                deleteSession();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            try {
                editSession();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        // saves all data that may have been edited
        save();
        TutorController.save();
        TuteeController.save();
        // options to either return to this same main method or the main menu
        returnToMainOrManagement();
    }
    // adds session to list of sessions in the class
    public static void addSession () throws Exception {
        System.out.println("-------------------------------");
        System.out.println("Adding Session");
        Scanner sc = new Scanner(System.in);
        System.out.println("Are you sure you want to add a new session? Type Y/N");
        char booleanVariable = sc.next().charAt(0);
        while (booleanVariable!= 'Y' && booleanVariable != 'N'){
            System.out.println("Incorrect input. Are you sure you want to add a new session? Type Y/N");
            booleanVariable = sc.next().charAt(0);
        }
        if (booleanVariable == 'Y')
        {
            NewDate sessionDate = addSessionDateInput(); // allows user to input session's date
            NewTime sessionTime = addSessionTimeInput(); // allows user to input session's time
            addSessionOtherInputs(sessionDate, sessionTime); // allows user to create a new session and all other inputs.
        }
    }
    // adds the session's time
    public static NewTime addSessionTimeInput () throws Exception {
        Scanner sc = new Scanner(System.in);
        boolean inputCorrect = false;
        NewTime sessionTime = null;
        while (!inputCorrect)
        {
            try {
                System.out.print("Enter the session's time in the format HH:MM: ");
                String time = sc.nextLine();
                sessionTime = new NewTime(time); // this is in a try-catch block since this line can throw a format input error
                inputCorrect = true;
            }
            catch (Exception e) { System.out.println("Invalid input"); }
        }
        return sessionTime;
    }
    public static NewDate addSessionDateInput () throws Exception {
        NewDate sessionDate = null;
        Scanner sc = new Scanner(System.in);
        boolean inputCorrect = false;
        while (!inputCorrect)
        {
            try {
                System.out.print("Enter the session's date in the format DD/MM/YYYY: ");
                String date = sc.next();
                sessionDate = new NewDate(date); // this is in a try-catch block since this line can throw a format input error
                inputCorrect = true;
            }
            catch (Exception e){
                System.out.println("Invalid date format");
            }
        }
        return sessionDate;
    }

    private static void addSessionOtherInputs(NewDate sessionDate, NewTime sessionTime) throws Exception {
        Scanner sc = new Scanner(System.in);
        ArrayList<Tutor> tutors = new ArrayList<>(); //list of tutors in this session
        ArrayList<Tutee> tutees = new ArrayList<>(); //list of tutees in this session
        boolean exitLoop = true;
        do {
            System.out.print("What is the name of a tutor in this session? ");
            String name = sc.nextLine();
            if (TutorController.searchByName(name)==null){ // tutor not found in tutors list
                System.out.println("Tutor not found");
                exitLoop = false; // exits only when a tutor isn't found in the list
            }
            else {
                tutors.add(TutorController.searchByName(name)); // adds tutor to this sessions list
                String key = sessionDate.toString() + ", " + sessionTime.toString();
                if (TutorController.searchByName(name).getSessionsAvailable() == null) { // if tutor doesn't have any sessions that they're available for yet
                    HashMap<String, Boolean> sessionsAvailable = new HashMap<>();
                    sessionsAvailable.put(key, true);
                    TutorController.searchByName(name).setSessionsAvailable(sessionsAvailable); // creates new hashmap, and adds hashmap with this session to the tutor's HashMap
                }
                else {
                    TutorController.searchByName(name).getSessionsAvailable().put(key, true); // adds session to tutor's list of existing sessions
                }
                System.out.println("Tutor Added to Session");
            }
        } while (exitLoop);
        do {
            System.out.println("What is the name of a tutee in this session?");
            String name = sc.nextLine();
            if (TuteeController.searchByName(name)==null) { // tutee not found in tutees list
                System.out.println("Tutee not found");
                exitLoop = true; // exits only when a tutee isn't found in the list
            }
            else {
                tutees.add(TuteeController.searchByName(name)); // adds tutee to this sessions list
                String key = sessionDate.toString() + " " + sessionTime.toString();
                if (TuteeController.searchByName(name).getSessionsAvailable() == null) { // if tutee doesn't have any sessions that they're available for yet
                    HashMap<String, Boolean> sessionsAvailable = new HashMap<>();
                    sessionsAvailable.put(key, true);
                    TuteeController.searchByName(name).setSessionsAvailable(sessionsAvailable); // creates new hashmap, and adds hashmap with this session to the tutee's HashMap
                }
                else {
                    TuteeController.searchByName(name).getSessionsAvailable().put(key, true); // adds session to tutee's list of existing sessions
                }
                System.out.println("Tutee Added to Session");
            }
        } while (!exitLoop);
        Session session = new Session(sessionDate, sessionTime, tutors, tutees); // instantiates new session
        System.out.println(session);
        sessions.add(session); // adds it to sessions list
    }

    // deletes session from list
    public static void deleteSession () throws Exception {
        System.out.println("-------------------------------");
        System.out.println("Deleting Session");
        NewDate sessionDate = addSessionDateInput(); // date input
        NewTime sessionTime = addSessionTimeInput(); // time input
        if (searchByDateAndTime(sessionTime, sessionDate) == null) { // if it cannot find the session
            System.out.println("Session not found. Cannot delete");
        }
        else {
            sessions.remove(searchByDateAndTime(sessionTime, sessionDate)); // deletes session
            System.out.println("Session Deleted");
        }
    }

    // provides option to either return to the main method of this class or to the system's main menu
    private static void returnToMainOrManagement() throws Exception {
        System.out.println("-------------------------------");
        Scanner sc = new Scanner(System.in);
        char returnToWhat = ' ';
        do {
            System.out.println("[S] to return to Session Management");
            System.out.println("[M] to return to Main Menu");
            returnToWhat = sc.next().charAt(0);
        } while (returnToWhat!= 'S' && returnToWhat != 'M');
        if (returnToWhat == 'S') {
            SessionController.main(null);
        }
        else {
            Main.main(null);
        }
    }
    // editing a session's details
    public static void editSession () throws Exception {
        System.out.println("-------------------------------");
        System.out.println("Editing Session");
        Scanner sc = new Scanner(System.in);
        NewDate sessionDate = addSessionDateInput(); // date input
        NewTime sessionTime = addSessionTimeInput(); // time input
        if (searchByDateAndTime(sessionTime, sessionDate) == null) { // if there is no session of this data and time
            System.out.println("Session not found. Cannot delete");
        }
        else {
            boolean continues = true;
            System.out.println("Found session.");
            Session session = searchByDateAndTime(sessionTime, sessionDate); // finds session and sets it to this session
            do {
                boolean inputMisMatch = true;
                int input = 0;
                while(inputMisMatch) {
                    System.out.println("What would you like to edit?");
                    System.out.println("[1] to edit Date");
                    System.out.println("[2] to edit Time");
                    System.out.println("[3] to edit a Tutee");
                    System.out.println("[4] to edit a Tutor");
                    System.out.println("[5] to Quit Editing");
                    // validates input's data type and range
                    try {
                        input = Integer.parseInt(sc.next());
                        if (input != 1 && input != 2 && input != 3 && input != 4 && input != 5) {
                            System.out.println("Incorrect input.");
                        }
                        else {
                            inputMisMatch = false;
                        }
                    } catch (Exception e) {
                        System.out.println("Incorrect input format");
                    }
                }
                switch (input) {
                    case 1:
                        NewDate newSessionDate = addSessionDateInput(); // date input
                        session.setSessionDate(newSessionDate); // sets new date to session's date
                        System.out.println("Session Date Edited");
                        break;
                    case 2:
                        NewTime newSessionTime = addSessionTimeInput(); // time input
                        session.setSessionTime(newSessionTime); // sets new time to session's time
                        System.out.println("Session Time Edited");
                        break;
                    case 3:
                        System.out.print("Name of tutee you would like to edit?");
                        sc.nextLine();
                        String name = sc.nextLine();
                        if (searchByNameTutee(name, session)!=null) { // if tutee is found
                            System.out.println("Tutee found. Would you like to remove the tutee from the session? Press Y");
                            char booleanInput = sc.next().charAt(0);
                            if (booleanInput == 'Y') {
                                session.getTutees().remove(searchByNameTutee(name, session)); // removes tutee from session
                                System.out.println("Tutee Removed from Session");
                            }
                        }
                        // tutee is not found
                        else {
                            System.out.println("Tutee does not exist, cannot be deleted");
                        }
                        break;
                    case 4:
                        System.out.print("Name of tutor you would like to edit?");
                        sc.nextLine();
                        String tutorName = sc.nextLine();
                        if (searchByNameTutor(tutorName, session) !=null) { // if tutor is found
                            System.out.println("Tutor found. Would you like to remove the tutor from the session? Press Y/N");
                            char booleanInput = sc.next().charAt(0);
                            if (booleanInput == 'Y') {
                                session.getTutors().remove(searchByNameTutor(tutorName, session)); // removes tutee from session
                                System.out.println("Tutor Removed From Session");
                            }
                        }
                        // tutor is not found
                        else {
                            System.out.println("Tutee does not exist in session, cannot be deleted");
                        }
                        break;
                    case 5:
                        continues = false; // exits loop
                        break;
                    default:
                        System.out.println("Incorrect input.");
                }
                System.out.println("Session Edited.");
                System.out.println(session);
            } while (continues);
        }
    }
    //serializes sessions to file
    public static void save() throws IOException
    {
        System.out.println("----------------------");
        System.out.println("Saving changes to Sessions");
        try {
            FileOutputStream f = new FileOutputStream("sessions.txt");
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(sessions); // writes sessions to file
            o.close();
            f.close();
            System.out.println("Changes saved to file");
        } catch (IOException e) {
            System.out.println("Error initializing saving data stream");
        }
    }

    //deserializes sessions from file
    public static void load() throws Exception
    {
        System.out.println("----------------------");
        System.out.println("Loading Sessions Data");
        try {
            FileInputStream fi = new FileInputStream("sessions.txt");
            ObjectInputStream oi = new ObjectInputStream(fi);
            sessions = (ArrayList<Session>) oi.readObject(); // loads sessions from file
            System.out.println("Data Loaded");
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }

    // searches for a tutee from the session's list of tutee by name using linear search, returning a tutee
    public static Tutee searchByNameTutee(String name, Session session) {
        for (Tutee tutee: session.getTutees()) {
            if (tutee.getName().equals(name)) {
                return tutee;
            }
        }
        return null;
    }
    // searches for a tutor from the session's list of tutor by name using linear search, returning a tutor
    public static Tutor searchByNameTutor(String name, Session session) {
        for (Tutor tutor: session.getTutors()) {
            if (tutor.getName().equals(name)) {
                return tutor;
            }
        }
        return null;
    }

    // searches for a session from the list of sessions by data and time using binary search, returning a session
    public static Session searchByDateAndTime (NewTime time, NewDate date) {
        insertionSort();
        int low = 0;
        int high = sessions.size() - 1;
        while (low <= high) {
            int mid = (high+low)/2; // mid-point of sub-array
            if (sessions.get(mid).getSessionDate().equals(date) && sessions.get(mid).getSessionTime().equals(time)) {
                return sessions.get(mid);
            }
            else if (sessions.get(mid).getSessionTime().compareTo(time)< sessions.get(mid).getSessionDate().compareTo(date)) {
                low = mid + 1; // session is in higher sub-array
            }
            else {
                high = mid - 1; // session is in lower sub-array
            }
        }
        return null;
    }

    // sorts a list of sessions by date and time
    public static ArrayList<Session> insertionSort()
    {
        int c = 0;
        int len = sessions.size();
        for(int i = 1; i < len; i++)
        {
            Session temp = sessions.get(i);
            int j = i-1;
            while(j >= 0 && sessions.get(j).compareTo(temp) > 0)
            {
                c++;
                sessions.set(j+1, sessions.get(j));
                j--;
            }
            sessions.set(j+1, temp);
        }
        return sessions;
    }
}
