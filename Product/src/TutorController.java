import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
public class TutorController {
    public static ArrayList <Tutor> tutors = new ArrayList<>();
    public static void main(String[] args) throws Exception {
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
            catch (NumberFormatException ex){
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        save();
        TuteeController.save();
        SubjectController.save();
        SessionController.save();
        returnToMainMenuOrManagement();
    }

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

    public static void addTutor () throws Exception {
        ArrayList<Subject> subjectsTaught = new ArrayList<>();
        Tutor tutor = new Tutor(subjectsTaught);
        Scanner sc = new Scanner(System.in);
        System.out.println("-------------------------------");
        System.out.println("Adding Tutor ");
        System.out.println("-------------------------------");
        System.out.print("Enter the tutor's name: ");
        String name = sc.nextLine().trim();
        tutor.setName(name);
        int grade = 0;
        boolean continues = true;
        do {
            System.out.print("Enter tutor's grade (between 11 and 12): ");
            try {
                grade = Integer.parseInt(sc.next().trim());
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
        tutor.setGradeLevel(grade);
        System.out.println("Tutor Grade Set");
        boolean noMoreSubjects = false;
        System.out.println("Adding Subjects Tutor can teach");
        do {
            Subject subject = SubjectController.subjectInputs();
            tutor.getSubjectsTaught().add(subject);
            if (!SubjectController.searchSubject(subject))
            {
                SubjectController.subjects.add(subject);
                System.out.print("Subject added to overall Subject list: ");
                System.out.println(SubjectController.subjects);
            }
            System.out.println("More subjects? Press Y / N: ");
            char moreSubjects = sc.next().trim().charAt(0);
            while (moreSubjects != 'Y' && moreSubjects != 'N')
            {
                System.out.print("Incorrect input. More subjects? Press Y / N: ");
                moreSubjects = sc.next().trim().charAt(0);
            }
            if (moreSubjects == 'N')
            {
                noMoreSubjects = true;
            }
        } while (!noMoreSubjects);
        System.out.println("Subjects Added To Tutor List");
        System.out.println("Does the tutor have a tutee? Type Y if true, anything else if false: ");
        if (sc.next().trim().charAt(0) == 'Y') {
            System.out.println("Enter the tutee's name");
            sc.nextLine();
            name = sc.nextLine().trim();
            if (TuteeController.searchByName(name) == null) {
                System.out.println("Tutee not found.");
            }
            else {
                tutor.setTutee(TuteeController.searchByName(name));
                TuteeController.searchByName(name).setTutor(tutor);
                System.out.println("Tutee Paired");
            }
        }
        System.out.println("-------------------------------");
        System.out.println("Adding Sessions Tutor is Available for");
        boolean noMoreSessions = false;
        do {
            NewDate sessionDate = SessionController.addSessionDateInput();
            NewTime sessionTime = SessionController.addSessionTimeInput();
            String sessionDateAndTime = sessionDate.toString() + " " + sessionTime.toString();
            if (SessionController.searchByDateAndTime(sessionTime, sessionDate) == null)
            {
                System.out.println("Session not found");
                noMoreSessions = true;
            }
            else {
                HashMap<String, Boolean> sessionsAvailable = new HashMap<>();
                sessionsAvailable.put(sessionDateAndTime, true);
                tutor.setSessionsAvailable(sessionsAvailable);
                ArrayList<Tutor> tutors = new ArrayList<>();
                tutors.add(tutor);
                SessionController.searchByDateAndTime(sessionTime, sessionDate).setTutors(tutors);
                System.out.println("Session Added to Tutor");
            }
        } while (!noMoreSessions);
        tutors.add(tutor);
        System.out.println("Tutor Created and Added");
        System.out.println(tutor);
    }

    public static void deleteTutor() {
        System.out.println(tutors);
        Scanner sc = new Scanner(System.in);
        System.out.println("-------------------------------");
        System.out.println("Deleting Tutor");
        System.out.println("Are you sure you want to delete a Tutor? Press Y/N: ");
        char delete = sc.next().trim().charAt(0);
        while (delete != 'Y' && delete != 'N')
        {
            System.out.println("Wrong input. Are you sure you want to delete a Tutor? Press Y/N: ");
            delete = sc.next().trim().charAt(0);
        }
        if (delete == 'Y')
        {
            System.out.print("What is the tutor's name?: ");
            sc.nextLine();
            String name = sc.nextLine();
            if (searchByName(name) == null){
                System.out.println("No tutor found with that name");
            }
            else {
                tutors.remove(searchByName(name));
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
        while (input != 'Y' && input != 'N'){
            System.out.print("Wrong input. Are you sure you would like to edit a tutor? Press Y/N ");
            sc.nextLine();
            input = sc.next().charAt(0);
        }
        if (input == 'Y')
        {
            System.out.print("What is the tutor's name?: ");
            sc.nextLine();
            String name = sc.nextLine();
            if (searchByName(name) == null){
                System.out.println("No tutor found with that name");
            }
            else {
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
                        grade = Integer.parseInt(sc.next().trim());
                        if (grade == 11 || grade == 12) {
                            tutor.setGradeLevel(grade);
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
                tutor.setGradeLevel(grade);
                System.out.println("Grade Level Edited");
            }
            else if (editInput == 'S' || editInput == 's') {
                Subject subject = SubjectController.subjectInputs();
                boolean subjectTutorPresence = tutor.searchSubjectByNameAndLevel(subject.getSubjectName(), subject.getSubjectLevel());
                if (subjectTutorPresence) {
                    System.out.println("Subject found for tutor. Press E for editing subject and D for deleting subject");
                    char editOrDelete = sc.next().charAt(0);
                    while (editOrDelete != 'E' && editOrDelete != 'D') {
                        System.out.println("Incorrect input. Press E for editing subject and D for deleting subject");
                        editOrDelete = sc.next().charAt(0);
                    }
                    if (editOrDelete == 'E')
                    {
                        System.out.print("What would you like to edit? Press S for subject name and L for subject Level");
                        char subjectEdit = sc.next().charAt(0);
                        while(subjectEdit != 'S' && subjectEdit != 'L'){
                            System.out.println("Incorrect input. What would you like to edit? Press S for subject name and L for subject Level: ");
                            subjectEdit = sc.next().charAt(0);
                        }
                        if (subjectEdit == 'S')
                        {
                            System.out.print("Enter subject's new name: ");
                            sc.nextLine();
                            tutor.searchSubjectByNameAndLevelAndReturnSubject(subject.getSubjectName(), subject.getSubjectLevel()).setSubjectName(sc.nextLine());
                            System.out.println("Subject's Name Edited");
                        }
                        else {
                            System.out.print("Enter subject's new level");
                            sc.nextLine();
                            tutor.searchSubjectByNameAndLevelAndReturnSubject(subject.getSubjectName(), subject.getSubjectLevel()).setSubjectLevel(sc.next().charAt(0));
                            System.out.println("Subject's Level Edited");
                        }
                    }
                    else {
                        tutor.getSubjectsTaught().remove(tutor.searchSubjectByNameAndLevelAndReturnSubject(subject.getSubjectName(), subject.getSubjectLevel()));
                        System.out.println("Subject remove from Tutor's List");
                    }
                }
                else {
                    System.out.println("Subject not found ");
                }
            }
            else if (editInput == 'E' || editInput == 'e') {
                NewDate date = SessionController.addSessionDateInput();
                NewTime time = SessionController.addSessionTimeInput();
                Session session = SessionController.searchByDateAndTime(time, date);
                String sessionKey = date.toString() + " " + time.toString();
                try {
                    if (tutor.searchSession(date, time) && tutor.getSessionsAvailable().get(sessionKey)) { // searches if the session is in the tutor's list and if it is set to "true", indicating they are actively tutoring in that session
                        System.out.println("Session found. Would you like to remove this tutor from this session? Press Y");
                        char yesOrNo = sc.next().charAt(0);
                        if (yesOrNo == 'Y') {
                            tutor.getSessionsAvailable().replace(sessionKey, false); // doesn't permanently remove the tutor from the session but instead sets its value to "False", indicating they are not tutoring in this session anymore but may do so later
                            System.out.println("Session availability set to false");
                        }
                    }
                    else if (session != null) {
                        System.out.println("Session doesn't exist in tutor's list. Would you like to have the tutor in this session? Press Y");
                        char yesOrNo = sc.next().charAt(0);
                        if (yesOrNo == 'Y') {
                            try {
                                if (!tutor.getSessionsAvailable().get(sessionKey)) {
                                    tutor.getSessionsAvailable().replace(sessionKey, true);
                                    session.getTutors().add(tutor);
                                }
                            } catch (NullPointerException ex) {
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
                } catch (Exception e) {
                    if (session != null) {
                        System.out.println("Session doesn't exist in tutor's list. Would you like to have the tutor in this session? Press Y");
                        char yesOrNo = sc.next().charAt(0);
                        if (yesOrNo == 'Y') {
                            try {
                                if (!tutor.getSessionsAvailable().get(sessionKey)) {
                                    tutor.getSessionsAvailable().replace(sessionKey, true);
                                    session.getTutors().add(tutor);
                                }
                            } catch (NullPointerException ex) {
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
                if (TuteeController.searchByName(name) == null) {
                    System.out.println("Tutee doesn't exist");
                }
                else {
                    while (!correctInput) {
                        try {
                            System.out.println("[1] To change tutee");
                            System.out.println("[2] To not have a tutee right now");
                            switchInput = Integer.parseInt(sc.next());
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
                            if (TuteeController.searchByName(newTuteeName) == null) {
                                System.out.println("New Tutee doesn't exist");
                            }
                            else {
                                tutor.setTutee(TuteeController.searchByName(newTuteeName));
                                TuteeController.searchByName(name).setTutor(null);
                                TuteeController.searchByName(newTuteeName).setTutor(tutor);
                                System.out.println("New Tutee paired with Tutor");
                            }
                            break;
                        case 2:
                            tutor.setTutee(null);
                            TuteeController.searchByName(name).setTutor(null);
                            System.out.println("Tutor and Tutee unpaired");
                            break;
                        default:
                            System.out.println("Incorrect input");
                            break;
                    }
                }
            }
            else if (editInput == 'Q' || editInput == 'q') {
                continues = false;
                break;
            }
            else {
                System.out.println("Incorrect input");
            }
            System.out.print("Would you like to edit anything else about this tutor? Press Y: ");
            if (sc.next().charAt(0) == 'Y') {
                continues = true;
            }
        } while(continues);
    }

    public static Tutor searchByName(String name){ // linear search by name since name is not unique
        for (Tutor tutor : tutors) {
            if (tutor.getName().equals(name)) {
                return tutor;
            }
        }
        return null;
    }
    public static void save()
    {
        System.out.println("-------------------------------");
        System.out.println("Saving changes to Tutors");
        try {
            FileOutputStream f = new FileOutputStream("tutors.txt"); //creates new File
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(tutors); //writes tutors object to file
            System.out.println("Changes saved to file");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }
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