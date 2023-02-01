import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
public class TutorController {
    public static ArrayList <Tutor> tutors = new ArrayList<Tutor>();
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("[A] to add tutors");
        System.out.println("[D] to delete tutors");
        System.out.println("[E] to edit tutors");
        char userActionInput = sc.next().charAt(0);
        while (userActionInput != 'A' && userActionInput != 'D' && userActionInput != 'E'){
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
        sc.close();
    }

    private static void returnToMainMenuOrManagement() throws Exception {
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
        sc.close();
    }

    public static void addTutor () throws Exception {
        load();
        ArrayList<Subject> subjectsTaught = new ArrayList<>();
        Tutor tutor = new Tutor(subjectsTaught);
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the student's name: ");
        String name = sc.nextLine().trim();
        tutor.setName(name);
        int grade = 0;
        while (grade < 11 || grade > 12)
        {
            System.out.print("Enter student grade (between 11 and 12): ");
            try {
                grade = Integer.parseInt(sc.next());
            }
            catch (NumberFormatException ex) {
                System.out.println("Incorrect input format");
            }
        }
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
                System.out.println("Incorrect input. More subjects? Press Y / N: ");
                moreSubjects = sc.next().charAt(0);
            }
            if (moreSubjects == 'N')
            {
                noMoreSubjects = true;
            }
        } while (!noMoreSubjects);
        System.out.println("Enter the tutor's tutee's name: ");
        sc.next();
        name = sc.nextLine();
        if (TuteeController.searchByName(name) == null) {
            System.out.println("Tutee not found.");
        }
        else {
            tutor.setTutee(TuteeController.searchByName(name));
            TuteeController.searchByName(name).setTutor(tutor);
        }
        System.out.println("Now, the sessions that the tutor will be available for will be asked");
        boolean noMoreSessions = false;
        Session session = new Session();
        do {
            NewDate sessionDate = SessionController.addSessionDateInput();
            NewTime sessionTime = SessionController.addSessionTimeInput();
            String sessionDateAndTime = sessionDate.toString() + " " + sessionTime.toString();
            if (SessionController.searchByTime(sessionTime) == null)
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
        Scanner sc = new Scanner(System.in);
        System.out.println("Are you sure you want to delete another Tutor? Press Y/N: ");
        char delete = sc.next().charAt(0);
        while (delete != 'Y' && delete != 'N')
        {
            System.out.println("Wrong input. Are you sure you want to delete a Tutor? Press Y/N: ");
            delete = sc.next().charAt(0);
        }
        if (delete == 'Y')
        {
            System.out.println("Do you want to delete based on ID or Name? Press I for ID and N for Name ");
            char idOrName = sc.next().charAt(0);
            while (idOrName != 'I' && idOrName != 'N')
            {
                System.out.println("Wrong input. Do you want to delete based on ID or Name? Press I for ID and N for Name: ");
                idOrName = sc.next().charAt(0);
            }
            if (idOrName == 'I'){
                int id = 0;
                boolean correctInput = false;
                while (!correctInput) {
                    System.out.print("What is the ID?: ");
                    try {
                        id = Integer.parseInt(sc.next());
                        correctInput = true;
                    } catch (Exception ex) {
                        System.out.println("Incorrect input.");
                    }
                }
                boolean tutorPresence = searchById(id);
                if (!tutorPresence){
                    System.out.println("No tutor found with that ID.");
                }
                else {tutors.remove(id);}
            }
            else {
                System.out.println("What is the name?: ");
                sc.next();
                String name = sc.nextLine();
                String tutorId = searchByName(name).getId();
                if (tutorId == null){
                    System.out.println("No tutee found with that name");
                }
                else {
                    tutors.remove(tutors.get(Integer.parseInt(tutorId)));
                }
            }
        }
        sc.close();
        save();
        returnToMainMenuOrManagement();
    }

    public static void editTutor() throws Exception {
        load();
        Scanner sc = new Scanner(System.in);
        System.out.println("Are you sure you would like to edit a tutor? Press Y/N");
        char input = sc.next().charAt(0);
        while (input != 'Y' && input != 'N'){
            System.out.println("Wrong input. Are you sure you would like to edit a tutor? Press Y/N ");
            input = sc.next().charAt(0);
        }
        if (input == 'Y')
        {
            System.out.println("Do you want to search based on ID or Name? Press I for ID and N for Name ");
            char idOrName = sc.next().charAt(0);
            while (idOrName != 'I' && idOrName != 'N')
            {
                System.out.println("Wrong input. Do you want to delete based on ID or Name? Press I for ID and N for Name: ");
                idOrName = sc.next().charAt(0);
            }
            if (idOrName == 'I'){
                int id = 0;
                boolean correctInput = false;
                while (!correctInput) {
                    System.out.print("What is the ID?: ");
                    try {
                        id = Integer.parseInt(sc.next());
                        correctInput = true;
                    } catch (Exception ex) {
                        System.out.println("Incorrect input.");
                    }
                }
                boolean tutorPresence = searchById(id);
                if (!tutorPresence){
                    System.out.println("No tutor found with that ID");
                }
                else {
                    editTutorInputs(id);
                }
            }
            else {
                System.out.println("What is the name?: ");
                sc.next();
                String name = sc.nextLine();
                if (searchByName(name) == null){ //error with searching algorithm
                    System.out.println("No tutor found with that name");
                }
                else {
                    editTutorInputs(Integer.parseInt(searchByName(name).getId()));
                }
            }
        }
        returnToMainMenuOrManagement();
        save();
    }

    public static void editTutorInputs(int id) throws Exception
    {
        Scanner sc = new Scanner(System.in);
        boolean continues = true;
        Tutor tutor = tutors.get(id);
        do {
            System.out.println("Found tutor with name " + tutor.getName() + ". What would you like to edit?" );
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
                String name = sc.nextLine();
                tutor.setName(name);
            }
            else if (editInput == 'G'|| editInput == 'g') {
                int grade = 0;
                while (grade < 11 || grade > 12) //
                {
                    System.out.println("Enter the student's actual grade (between 11 and 12) : ");
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
                        System.out.println("What would you like to edit? Press S for subject name and L for subject Level");
                        char subjectEdit = sc.next().charAt(0);
                        while(subjectEdit != 'S' && subjectEdit != 'L'){
                            System.out.println("Incorrect input. What would you like to edit? Press S for subject name and L for subject Level");
                            subjectEdit = sc.next().charAt(0);
                        }
                        if (subjectEdit == 'S')
                        {
                            System.out.println("Enter subject's new name");
                            tutor.searchSubjectByNameAndLevelAndReturnSubject(subject.getSubjectName(), subject.getSubjectLevel()).setSubjectName(sc.nextLine());
                        }
                        else {
                            System.out.println("Enter subject's new level");
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
                Session sessionPresence = SessionController.searchByTime(time);
                String sessionKey = date.toString() + " " + time.toString();
                if (tutor.searchSession(date, time)) {
                    System.out.println("Session found. Would you like to remove this tutor from this session? Press Y");
                    char yesOrNo = sc.next().charAt(0);
                    if (yesOrNo == 'Y') {
                        try {
                            tutor.getSessionsAvailable().replace(sessionKey, false);
                            SessionController.searchByTime(time).getTutors().remove(tutor);
                        } catch (RuntimeException r) {
                            System.out.println("Session doesn't have any tutors. No removing is needed.");
                        }
                    }
                }
                else if (sessionPresence != null) {
                    System.out.println("Session doesn't exist in tutor list. Would you like to have the tutor in this session? Press Y");
                    if (sc.next().charAt(0) == 'Y') {
                        tutor.getSessionsAvailable().put(sessionKey, true);
                        sessionPresence.getTutors().add(tutor);
                    }
                }
                else {
                    System.out.println("Session doesn't exist at all");
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
                        System.out.println("What name does the new tutee have?");
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
            }
            else {
                System.out.println("Incorrect input");
            }
        } while(continues);
    }

    public static Tutor searchByName(String name){ // linear search by name since name is not unique
        for (int i = 0; i < tutors.size(); i++) {
            if (tutors.get(i).getName().equals(name)) {
                return tutors.get(i);
            }
        }
        return null;
    }

    public static boolean searchById (int ID) { //binary search by id since id is unique
        insertionSortByID(tutors);
        int low = 0;
        int high = tutors.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (Integer.parseInt(tutors.get(mid).getId()) == ID) {
                return true;
            }
            else if (Integer.parseInt(tutors.get(mid).getId()) < ID) {
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
        }
        return false;
    }
    public static void save() throws IOException
    {
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
        System.out.println("Loading sources");
        try {
            FileInputStream fi = new FileInputStream("tutors.txt"); //attempts to open file
            ObjectInputStream oi = new ObjectInputStream(fi);
            tutors = (ArrayList<Tutor>) oi.readObject(); //reads object from file and casts it to arraylist
            oi.close();
            fi.close();
            System.out.println(tutors.toString());
        }
        catch (FileNotFoundException e) {System.out.println("File not found");}
        catch (IOException i) {System.out.println("Error initializating the data stream");}
        catch (ClassNotFoundException c) {c.printStackTrace();}
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

    public static void insertionSortByID(ArrayList<Tutor> tutors)
    {
        int c = 0;
        int len = tutors.size();
        for(int i = 1; i < len; i++)
        {
            Tutor temp = tutors.get(i);
            int j = i-1;
            while(j >= 0 && Integer.parseInt(tutors.get(j).getId()) - Integer.parseInt(temp.getId()) > 0)
            {
                c++;
                tutors.set(j+1, tutors.get(j));
                j--;
            }
            tutors.set(j+1, temp);
        }
    }




}