import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class SessionController {
    static ArrayList <Session> sessions = new ArrayList <Session> ();

    public static void main(String[] args) throws Exception {
        System.out.println("-------------------------------");
        System.out.println("Session Management");
        load();
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
    }

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
            NewDate sessionDate = addSessionDateInput();
            NewTime sessionTime = addSessionTimeInput();
            addSessionOtherInputs(sessionDate, sessionTime);
        }
        save();
        returnToMainOrManagement();

    }
    public static NewTime addSessionTimeInput () throws Exception {
        Scanner sc = new Scanner(System.in);
        boolean inputCorrect = false;
        NewTime sessionTime = null;
        while (!inputCorrect)
        {
            try {
                System.out.println("Enter the session's time in the format HH:MM");
                String time = sc.nextLine();
                sessionTime = new NewTime(time);
                inputCorrect = true;
            }
            catch (Exception e) { throw new RuntimeException ("Invalid input"); }
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
                System.out.println("Enter the session's date in the format DD/MM/YYYY");
                String date = sc.next();
                sessionDate = new NewDate(date);
                inputCorrect = true;
            }
            catch (Exception e){ throw new Exception("Invalid date format");}
        }
        return sessionDate;
    }

    private static void addSessionOtherInputs(NewDate sessionDate, NewTime sessionTime) throws Exception {
        Scanner sc = new Scanner(System.in);
        ArrayList<Tutor> tutors = new ArrayList<>();
        ArrayList<Tutee> tutees = new ArrayList<>();
        boolean exitLoop = true;
        do {
            TutorController.load();
            System.out.println("What is the name of a tutor in this session?");
            String name = sc.nextLine();
            if (TutorController.searchByName(name)==null){
                System.out.println("Tutor not found");
                exitLoop = false;
            }
            else {
                tutors.add(TutorController.searchByName(name));
                Objects.requireNonNull(TutorController.searchByName(name)).getSessionsAvailable().put(sessionDate.toString() + " " + sessionTime.toString(), true);
            }
        } while (exitLoop);
        do {
            System.out.println("What is the name of a tutee in this session?");
            String name = sc.next();
            if (TuteeController.searchByName(name)==null) {
                System.out.println("Tutee not found");
            }
            else {
                tutees.add(TuteeController.searchByName(name));
                exitLoop = true;
            }
        } while (exitLoop);
        Session session = new Session(sessionDate, sessionTime, tutors, tutees);
        System.out.println(session);
        sessions.add(session);
    }

    public static void deleteSession () throws Exception {//complete method, seems fine
        System.out.println("-------------------------------");
        System.out.println("Deleting Session");
        load();
        NewDate sessionDate = addSessionDateInput();
        NewTime sessionTime = addSessionTimeInput();
        if (searchByTime(sessionTime) == null) {
            System.out.println("Session not found. Cannot delete");
        }
        else {
            sessions.remove(searchByTime(sessionTime));
        }
        save();
        returnToMainOrManagement();
    }
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
    public static void editSession () throws Exception {
        System.out.println("-------------------------------");
        System.out.println("Editing Session");
        load();
        System.out.println(sessions.toString());
        Scanner sc = new Scanner(System.in);
        NewDate sessionDate = addSessionDateInput();
        NewTime sessionTime = addSessionTimeInput();
        if (searchByTime(sessionTime) == null) {
            System.out.println("Session not found. Cannot delete");
        }
        else {
            boolean continues = true;
            System.out.println("Found session. What would you like to edit?" );
            Session session = searchByTime(sessionTime);
            do {
                System.out.println("[1] to edit Date");
                System.out.println("[2] to edit Time");
                System.out.println("[3] to edit a Tutee");
                System.out.println("[4] to edit a Tutor");
                System.out.println("[5] to Quit Editing");
                int input = sc.nextInt();
                switch (input) {
                    case 1:
                        NewDate newSessionDate = addSessionDateInput();
                        session.setSessionDate(newSessionDate);
                        break;
                    case 2:
                        NewTime newSessionTime = addSessionTimeInput();
                        session.setSessionTime(newSessionTime);
                        break;
                    case 3:
                        System.out.println("Name of tutee you would like to edit?");
                        sc.next();
                        String name = sc.nextLine();
                        if (searchByNameTutee(name, session) !=null) {
                            System.out.println("Tutee found. Would you like to remove the tutee from the session? Press Y");
                            char booleanInput = sc.next().charAt(0);
                            if (booleanInput == 'Y') {
                                session.getTutees().remove(searchByNameTutee(name, session));
                            }
                        }
                        else if (TuteeController.searchByName(name) != null){
                            System.out.println("Tutee does exist outside the session. Would you like to add the tutee to the session? Press Y");
                            char yesOrNo = sc.next().charAt(0);
                            if(yesOrNo == 'Y') {
                                session.getTutees().add(TuteeController.searchByName(name));
                            }
                        }
                        else {
                            System.out.println("Tutee does not exist, cannot be deleted");
                        }
                        break;
                    case 4:
                        System.out.println("Name of tutor you would like to edit?");
                        sc.next();
                        name = sc.nextLine();
                        if (searchByNameTutor(name, session) !=null) {
                            System.out.println("Tutor found. Would you like to remove the tutor from the session? Press Y");
                            char booleanInput = sc.next().charAt(0);
                            if (booleanInput == 'Y') {
                                session.getTutors().remove(searchByNameTutor(name, session));
                            }
                        }
                        else if (TutorController.searchByName(name) != null){
                            System.out.println("Tutor does exist outside the session. Would you like to add the tutor to the session? Press Y");
                            char yesOrNo = sc.next().charAt(0);
                            if(yesOrNo == 'Y') {
                                session.getTutors().add(TutorController.searchByName(name));
                            }
                        }
                        else {
                            System.out.println("Tutee does not exist, cannot be deleted");
                        }
                        break;
                    case 5:
                        continues = false;
                        break;
                    default:
                        System.out.println("Incorrect input.");
                }
            } while (continues);
        }
        save();
        returnToMainOrManagement();
    }
    public static void save() throws IOException
    {
        System.out.print("Saving changes to file... ");
        try {
            FileOutputStream f = new FileOutputStream(new File("sessions.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(sessions);
            o.close();
            f.close();
            System.out.println("Changes file");
        } catch (IOException e) {
            System.out.println("Error intializing saving data stream");
        }
    }

    public static void load() throws Exception
    {
        System.out.println("Loading data from file");
        try {
            FileInputStream fi = new FileInputStream("sessions.txt");
            ObjectInputStream oi = new ObjectInputStream(fi);
            sessions = (ArrayList<Session>) oi.readObject();
            System.out.println("Data Loaded");
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }

    public static Tutee searchByNameTutee(String name, Session session) { //complete
        TuteeController.insertionSort(session.getTutees());
        int low = 0;
        int high = session.getTutees().size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (session.getTutees().get(mid).getName().equals(name)) {
                return session.getTutees().get(mid);
            }
            else if (session.getTutees().get(mid).getName().compareTo(name)<0) {
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
        }
        return null;
    }

    public static Tutor searchByNameTutor(String name, Session session) {
        TutorController.insertionSortByName(session.getTutors());
        int low = 0;
        int high = session.getTutors().size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (session.getTutors().get(mid).getName().equals(name)) {
                return session.getTutors().get(mid);
            }
            else if (session.getTutors().get(mid).getName().compareTo(name)<0) {
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
        }
        return null;
    }

    public static Session searchByTime (NewTime time) {
        insertionSort();
        int low = 0;
        int high = sessions.size() - 1;
        while (low <= high) {
            int mid = (high + low + 1) / 2;
            if (sessions.get(mid).getSessionTime().equals(time)) {
                return sessions.get(mid);
            }
            else if (sessions.get(mid).getSessionTime().compareTo(time)<0) {
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
        }
        return null;
    }

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
