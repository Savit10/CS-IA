import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
public class TutorController {
    public static ArrayList <Tutor> tutors = new ArrayList<Tutor>();
    public static void main(String[] args) throws Exception {
        TutorController.load();
        System.out.println("-------------------------------");
        System.out.println("Tutor Management");
        Scanner sc = new Scanner(System.in);
        System.out.println("[A] to add tutors");
        System.out.println("[D] to delete tutors");
        System.out.println("[E] to edit tutors");
        char userActionInput = sc.next().charAt(0);
        while ((userActionInput) != 'A' && userActionInput != 'D' && userActionInput != 'E'){
            System.out.println("Incorrect input. Please try again");
            userActionInput = sc.next().charAt(0);
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
            try {
                deleteTutor();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                editTutor();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void returnToMainMenuOrManagement() throws Exception {
        System.out.println("-------------------------------");
        Scanner sc = new Scanner(System.in);
        char returnToWhat = ' ';
        do {
            System.out.println("[T] to return to Tutor Management");
            System.out.println("[M] to return to Main Menu");
            returnToWhat = sc.next().charAt(0);
        } while (returnToWhat != 'T' && returnToWhat != 'M');
        if (returnToWhat == 'T') {
            TutorController.main(null);
        } else {
            Main.main(null);
        }
    }

    public static void addTutor () throws Exception {
        load();
        ArrayList<Subject> subjectsTaught = new ArrayList<>();
        Tutor tutor = new Tutor(subjectsTaught);
        Scanner sc = new Scanner(System.in);
        System.out.println("-------------------------------");
        System.out.println("Adding Tutor ");
        System.out.println("-------------------------------");
        System.out.print("Enter the tutor's name: ");
        String name = sc.nextLine();
        tutor.setName(name);
        int grade = 0;
        boolean continues = true;
        do {
            System.out.print("Enter tutor's grade (between 11 and 12): ");
            try {
                grade = Integer.parseInt(sc.next());
                if (grade == 11 || grade == 12) {
                    break;
                }
                else {
                    System.out.println("Input outside given range");
                }
                continues = false;
            }
            catch (NumberFormatException ex) {
                System.out.println("Incorrect input format");
            }
        } while (continues);
        tutor.setGradeLevel(grade);
        boolean noMoreSubjects = false;
        System.out.println("Now we will be entering the subjects that the tutor plans to tutor!");
        do {
            Subject subject = SubjectController.subjectInputs();
            tutor.getSubjectsTaught().add(subject);
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
        System.out.println("Does the tutor have a tutee? Type Y if true, anything else if false: ");
        if (sc.next().charAt(0) == 'Y') {
            System.out.println("Enter the tutee's name");
            sc.next();
            name = sc.nextLine();
            if (TuteeController.searchByName(name) == null) {
                System.out.println("Tutee not found.");
            }
            else {
                tutor.setTutee(TuteeController.searchByName(name));
                TuteeController.searchByName(name).setTutor(tutor);
            }
        }
        System.out.println("-------------------------------");
        System.out.println("Now, the sessions that the tutor will be available for will be asked");
        boolean noMoreSessions = false;
        Session session = new Session();
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
                tutor.getSessionsAvailable().put(sessionDateAndTime, true);
                session.getTutors().add(tutor);
            }
        } while (!noMoreSessions);
        tutors.add(tutor);
        save();
        returnToMainMenuOrManagement();
    }

    public static void deleteTutor() throws Exception {
        load();
        System.out.println(tutors);
        Scanner sc = new Scanner(System.in);
        System.out.println("-------------------------------");
        System.out.println("Deleting Tutor");
        System.out.println("Are you sure you want to delete a Tutor? Press Y/N: ");
        char delete = sc.next().charAt(0);
        while (delete != 'Y' && delete != 'N')
        {
            System.out.println("Wrong input. Are you sure you want to delete a Tutor? Press Y/N: ");
            delete = sc.next().charAt(0);
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
        save();
        returnToMainMenuOrManagement();
    }

    public static void editTutor() throws Exception {
        load();
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
        returnToMainMenuOrManagement();
        save();
    }

    public static void editTutorInputs(Tutor tutor) throws Exception
    {
        Scanner sc = new Scanner(System.in);
        boolean continues = true;
        System.out.println("Found tutor with name " + tutor.getName());
        do {
            System.out.println("What would you like to edit?");
            System.out.println("[N] to edit Name");
            System.out.println("[G] to edit Grade");
            System.out.println("[S] to edit a Subject being Taught");
            System.out.println("[E] to edit a Session the Tutor is Available for");
            System.out.println("[T] to edit the Tutor's Tutee");
            System.out.println("[Q] to Quit Editing");
            char editInput = sc.next().charAt(0);
            if (editInput == 'N' || editInput == 'n')
            {
                System.out.println("What would you like to change the tutor's name to? ");
                sc.nextLine();
                String name = sc.nextLine();
                tutor.setName(name);
            }
            else if (editInput == 'G'|| editInput == 'g') {
                int grade = 0;
                while (grade < 11 || grade > 12) //
                {
                    System.out.print("Enter the tutor's grade(between 11 and 12): ");
                    try {
                        grade = Integer.parseInt(sc.next());
                    }
                    catch (NumberFormatException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                tutor.setGradeLevel(grade);
            }
            else if (editInput == 'S' || editInput == 's') {
                Subject subject = SubjectController.subjectInputs();
                boolean subjectTutorPresence = tutor.searchSubjectByNameAndLevel(subject.getSubjectName(), subject.getSubjectLevel()); // searches the tutee's list of subjects learning
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
                        }
                        else {
                            System.out.print("Enter subject's new level");
                            sc.nextLine();
                            tutor.searchSubjectByNameAndLevelAndReturnSubject(subject.getSubjectName(), subject.getSubjectLevel()).setSubjectLevel(sc.next().charAt(0));
                        }
                    }
                    else {
                        tutor.getSubjectsTaught().remove(tutor.searchSubjectByNameAndLevelAndReturnSubject(subject.getSubjectName(), subject.getSubjectLevel()));
                    }
                }
                else {
                    System.out.println("Subject not found ");
                }
            }
            else if (editInput == 'E' || editInput == 'e') {
                NewDate date = SessionController.addSessionDateInput();
                NewTime time = SessionController.addSessionTimeInput();
                Session sessionPresence = SessionController.searchByDateAndTime(time, date);
                String sessionKey = date.toString() + " " + time.toString();
                try {
                    if (tutor.searchSession(date, time)) {
                        System.out.println("Session found. Would you like to remove this tutor from this session? Press Y");
                        char yesOrNo = sc.next().charAt(0);
                        if (yesOrNo == 'Y') {
                            try {
                                tutor.getSessionsAvailable().replace(sessionKey, false);
                                SessionController.searchByDateAndTime(time, date).getTutors().remove(tutor);
                            } catch (RuntimeException r) {
                                System.out.println("Session doesn't have any tutors. No removing is needed.");
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Session doesn't exist");
                }
                if (sessionPresence != null) {
                    System.out.println("Session doesn't exist in tutor list. Would you like to have the tutor in this session? Press Y");
                    if (sc.next().charAt(0) == 'Y') {
                        tutor.getSessionsAvailable().put(sessionKey, true);
                        sessionPresence.getTutors().add(tutor);
                    }
                }
            }
            else if (editInput == 'T' || editInput == 't') {
                int switchInput = 0;
                boolean correctInput = false;
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
                        String name = sc.nextLine();
                        if (TuteeController.searchByName(name) == null) {
                            System.out.println("Tutee doesn't exist");
                        } else {
                            tutor.setTutee(TuteeController.searchByName(name));
                        }
                        break;
                    case 2:
                        tutor.setTutee(null);
                        break;
                    default:
                        System.out.println("Incorrect input");
                        break;
                }
            }
            else if (editInput == 'Q' || editInput == 'q') {
                continues = false;
                break;
            }
            else {
                System.out.println("Incorrect input");
            }
            System.out.print("Would you like to edit anything alse about this tutor? Press Y: ");
            if (sc.next().charAt(0) != 'Y') {
                continues = false;
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
    public static void insertionSortByName(ArrayList<Tutor> tutors)
    {
        int c = 0;
        int len = tutors.size();
        for(int i = 1; i < len; i++)
        {
            Tutor temp = tutors.get(i);
            int j = i-1;
            while(j >= 0 && tutors.get(j).getName().compareTo(temp.getName()) > 0)
            {
                c++;
                tutors.set(j+1, tutors.get(j));
                j--;
            }
            tutors.set(j+1, temp);
        }
    }
    public static void save() throws IOException
    {
        System.out.println("-------------------------------");
        System.out.println("Saving changes");
        try {
            FileOutputStream f = new FileOutputStream(new File ("tutors.txt")); //creates new File
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(tutors); //writes tutors object to file
            o.close();
            f.close();
            System.out.println("Changes saved to file");
        } catch (IOException e) {
            System.out.println("Error intializing stream");
        }
    }
    public static void load() throws Exception
    {
        System.out.println("-------------------------------");
        System.out.println("Loading data");
        try {
            FileInputStream fi = new FileInputStream("tutors.txt"); //attempts to open file
            ObjectInputStream oi = new ObjectInputStream(fi);
            tutors = (ArrayList<Tutor>) oi.readObject(); //reads object from file and casts it to arraylist
            oi.close();
            fi.close();
            System.out.println("Data Loaded");
        }
        catch (FileNotFoundException e) {System.out.println("File not found");}
        catch (IOException i) {System.out.println("Error initializating the data stream");}
        catch (ClassNotFoundException c) {c.printStackTrace();}
        System.out.println("To-do loading");
    }


}