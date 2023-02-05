import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
public class TuteeController{
        public static ArrayList <Tutee> tutees = new ArrayList<>();
        public static void main(String[] args) throws Exception {
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
                    addTutee();
                }
                catch (NumberFormatException ex){
                    System.out.println("Incorrect input format");
                }
            }
            else if (userActionInput == 'D')
            {
                deleteTutee();
            }
            else {
                try {
                    editTutee();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            save();
            TutorController.save();
            SessionController.save();
            SubjectController.save();
            returnToMainMenuOrManagement();
        }

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
            Tutee tutee = new Tutee(subjectsLearning);
            Scanner sc = new Scanner(System.in);
            System.out.println("-------------------------------");
            System.out.println("Adding Tutee");
            System.out.println("-------------------------------");
            System.out.print("Enter the tutee's name: ");
            String name = sc.nextLine().trim();
            tutee.setName(name);
            System.out.println("Tutee's name is set.");
            int grade = 0;
            boolean continues = true;
            do {
                System.out.print("Enter tutee's grade (between 11 and 12): ");
                try {
                    grade = Integer.parseInt(sc.next());
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
            tutee.setGradeLevel(grade);
            System.out.println("Tutee's grade is set");
            boolean noMoreSubjects = false;
            System.out.println("Adding Subjects Tutee will learn");
            do {
                Subject subject = SubjectController.subjectInputs();
                tutee.getSubjectsLearning().add(subject);
                if (!SubjectController.searchSubject(subject))
                {
                    SubjectController.subjects.add(subject);
                }
                System.out.println("More subjects? Press Y / N: ");
                char moreSubjects = sc.next().charAt(0);
                while (moreSubjects != 'Y' && moreSubjects != 'N')
                {
                    System.out.print("Incorrect input. More subjects? Press Y / N: ");
                    moreSubjects = sc.next().charAt(0);
                }
                if (moreSubjects == 'N')
                {
                    noMoreSubjects = true;
                }
            } while (!noMoreSubjects);
            System.out.println("Tutee's Subjects to learn is set");
            System.out.println("Does the tutee have a tutor? Type Y if true, anything else if false: ");
            if (sc.next().charAt(0) == 'Y') {
                System.out.println("Enter the tutor's name");
                sc.nextLine();
                name = sc.nextLine();
                if (TutorController.searchByName(name) == null) {
                    System.out.println("Tutor not found.");
                }
                else {
                    tutee.setTutor(TutorController.searchByName(name));
                    TutorController.searchByName(name).setTutee(tutee);
                    System.out.println("Tutor and tutee paired");
                }
            }
            System.out.println("-------------------------------");
            System.out.println("Adding Sessions Tutee is available for");
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
                    tutee.setSessionsAvailable(sessionsAvailable);
                    ArrayList<Tutee> tutees = new ArrayList<>();
                    tutees.add(tutee);
                    SessionController.searchByDateAndTime(sessionTime, sessionDate).setTutees(tutees);
                    System.out.println("Session Added to Tutee's list of available sessions");
                }
            } while (!noMoreSessions);
            tutees.add(tutee);
            System.out.println("Tutee Added");
        }

        public static void deleteTutee() {
            System.out.println(tutees);
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
                if (searchByName(name) == null){
                    System.out.println("No tutee found with that name");
                }
                else {
                    tutees.remove(searchByName(name));
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
                if (searchByName(name) == null){
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
                    tutee.setGradeLevel(grade);
                    System.out.println("Tutee's Grade Edited");
                }
                else if (editInput == 'S' || editInput == 's') {
                    Subject subject = SubjectController.subjectInputs();
                    boolean subjectTuteePresence = tutee.searchSubjectByNameAndLevel(subject.getSubjectName(), subject.getSubjectLevel());
                    if (subjectTuteePresence) {
                        System.out.println("Subject found for tutee. Press E for editing subject and D for deleting subject");
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
                                tutee.searchSubjectByNameAndLevelAndReturnSubject(subject.getSubjectName(), subject.getSubjectLevel()).setSubjectName(sc.nextLine());
                                System.out.println("Subject Name Edited");
                            }
                            else {
                                System.out.print("Enter subject's new level");
                                sc.nextLine();
                                tutee.searchSubjectByNameAndLevelAndReturnSubject(subject.getSubjectName(), subject.getSubjectLevel()).setSubjectLevel(sc.next().charAt(0));
                                System.out.println("Subject Level Edited");
                            }
                        }
                        else {
                            tutee.getSubjectsLearning().remove(tutee.searchSubjectByNameAndLevelAndReturnSubject(subject.getSubjectName(), subject.getSubjectLevel()));
                            System.out.println("Subject Removed");
                        }
                    }
                    else {
                        System.out.println("Subject not found.");
                    }
                }
                else if (editInput == 'E' || editInput == 'e') {
                    NewDate date = SessionController.addSessionDateInput();
                    NewTime time = SessionController.addSessionTimeInput();
                    Session session = SessionController.searchByDateAndTime(time, date);
                    String sessionKey = date.toString() + " " + time.toString();
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
                        else if (session != null) {
                            System.out.println("Session doesn't exist in tutee's list. Would you like to have the tutee in this session? Press Y");
                            char yesOrNo = sc.next().charAt(0);
                            if (yesOrNo == 'Y') {
                                try {
                                    if (!tutee.getSessionsAvailable().get(sessionKey)) {
                                        tutee.getSessionsAvailable().replace(sessionKey, true);
                                        session.getTutees().add(tutee);
                                    }
                                } catch (NullPointerException ex) {
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
                    } catch (Exception e) {
                        if (session != null) {
                            System.out.println("Session doesn't exist in tutee's list. Would you like to have the tutor in this session? Press Y");
                            char yesOrNo = sc.next().charAt(0);
                            if (yesOrNo == 'Y') {
                                try {
                                    if (!tutee.getSessionsAvailable().get(sessionKey)) {
                                        tutee.getSessionsAvailable().replace(sessionKey, true);
                                        session.getTutees().add(tutee);
                                    }
                                } catch (NullPointerException ex) {
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
                    if (TutorController.searchByName(name) == null) {
                        System.out.println("Tutor doesn't exist");
                    }
                    else {
                        while (!correctInput) {
                            try {
                                System.out.println("[1] To change tutor");
                                System.out.println("[2] To not have a tutor right now");
                                switchInput = Integer.parseInt(sc.next());
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
                                if (TutorController.searchByName(newTutorName) == null) {
                                    System.out.println("New Tutor doesn't exist");
                                }
                                else {
                                    tutee.setTutor(TutorController.searchByName(newTutorName));
                                    TutorController.searchByName(name).setTutee(null);
                                    TutorController.searchByName(newTutorName).setTutee(tutee);
                                    System.out.println("New Tutor and Tutee paired");
                                }
                                break;
                            case 2:
                                tutee.setTutor(null);
                                TutorController.searchByName(name).setTutee(null);
                                System.out.println("Tutee and Tutor Unpaired");
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
                System.out.print("Would you like to edit anything else about this tutee? Press Y: ");
                if (sc.next().charAt(0) == 'Y') {
                    continues = true;
                }
            } while(continues);
        }

        public static Tutee searchByName(String name){ // linear search by name since name is not unique
            for (Tutee tutee : tutees) {
                if (tutee.getName().equals(name)) {
                    return tutee;
                }
            }
            return null;
        }
        public static void insertionSort(ArrayList<Tutee> tutees)
        {
            int c = 0;
            int len = tutees.size();
            for(int i = 1; i < len; i++)
            {
                Tutee temp = tutees.get(i);
                int j = i-1;
                while(j >= 0 && tutees.get(j).getName().compareTo(temp.getName()) > 0)
                {
                    c++;
                    tutees.set(j+1, tutees.get(j));
                    j--;
                }
                tutees.set(j+1, temp);
            }
        }
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
