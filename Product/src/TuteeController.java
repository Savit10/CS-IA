import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.ServiceLoader;

public class TuteeController implements Serializable{
        public static ArrayList<Tutee> tutees = new ArrayList<Tutee>();
        public static void main(String[] args) throws Exception {
            Scanner sc = new Scanner(System.in);
            System.out.println("Press A to add tutees");
            System.out.println("Press D to delete tutees");
            System.out.println("Press E to edit tutees");
            char userActionInput = sc.next().charAt(0);
            while (userActionInput != 'A' && userActionInput != 'D' && userActionInput != 'E'){
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
                try {
                    deleteTutee();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                try {
                    editTutee();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private static void returnToMainMenuOrManagement() throws Exception {
            Scanner sc = new Scanner(System.in);
            char returnToWhat = ' ';
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
            load();
            ArrayList<Subject> subjectsLearning = new ArrayList<>();
            Tutee tutee = new Tutee(subjectsLearning);
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter the student's name: ");
            String name = sc.nextLine();
            tutee.setName(name);
            int grade = 0;
            while (grade < 11 || grade > 12) // for high school students
            {
                System.out.print("Enter student grade (between 11 and 12): ");
                try {
                    grade = Integer.parseInt(sc.next());
                }
                catch (NumberFormatException ex) {
                    System.out.println("Incorrect input format");
                }
            }
            boolean noMoreSubjects = false;
            System.out.println("Now we will be entering the subjects that the tutee plans to learn.");
            do {
                Subject subject = SubjectController.subjectInputs();
                tutee.getSubjectsLearning().add(subject);
                if (!SubjectController.searchSubject(subject))
                {
                    SubjectController.subjects.add(subject);
                }
                System.out.println("More subjects? Press Y / N: ");
                char moreSubjects = sc.next().charAt(0);
                while (moreSubjects != 'Y' && moreSubjects != 'N') {
                    System.out.println("Incorrect input. More subjects? Press Y / N: ");
                    moreSubjects = sc.next().charAt(0);
                }
                if (moreSubjects == 'N') {
                    noMoreSubjects = true;
                }
            } while (!noMoreSubjects);

            System.out.println("Enter the tutee's tutor's name: ");
            sc.next();
            name = sc.nextLine();
            if (TutorController.searchByName(name) == null) {
                System.out.println("Tutor not found");
            }
            else {
                tutee.setTutor(TutorController.searchByName(name));
                TutorController.searchByName(name).setTutee(tutee);
            }
            System.out.println("Now, the sessions that the tutee will be available for will be asked");
            boolean noMoreSessions = false;
            Session session = null;
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
                    tutee.getSessionsAvailable().put(sessionDateAndTime, true);
                    session.getTutees().add(tutee);
                }
            } while (!noMoreSessions);
            tutees.add(tutee);
            save();
            returnToMainMenuOrManagement();
        }

        public static void deleteTutee() throws Exception {
            load();
            Scanner sc = new Scanner(System.in);
            System.out.println("Are you sure you want to delete a Tutee? Press Y/N: ");
            char delete = sc.next().charAt(0);
            while (delete != 'Y' && delete != 'N')
            {
                System.out.println("Wrong input. Are you sure you want to delete a Tutee? Press Y/N: ");
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
                    System.out.print("What is the ID? (integer): ");
                    boolean correctInput = false;
                    while (!correctInput) {
                        try {
                            id = Integer.parseInt(sc.next());
                            correctInput = true;
                        } catch (Exception ex) {
                            System.out.println("Incorrect input.");
                        }
                    }
                    boolean tuteePresence = searchIndexById(id);
                    if (!tuteePresence){
                        System.out.println("No tutee found with that ID");
                    }
                    else {
                        tutees.remove(id);
                    }
                }
                else {
                    System.out.println("What is the name?: ");
                    String name = sc.nextLine();
                    String tuteeId = searchByNameAndReturnId(name);
                    if (tuteeId == null){
                        System.out.println("No tutee found with that name.");
                    }
                    else {tutees.remove(tutees.get(Integer.parseInt(tuteeId)));}
                }
            }
            save();
            returnToMainMenuOrManagement();
        }

        public static void editTutee() throws Exception {
            load();
            Scanner sc = new Scanner(System.in);
            System.out.println("Are you sure you would like to edit a tutee? Press Y/N");
            char input = sc.next().charAt(0);
            while (input != 'Y' && input != 'N'){
                System.out.println("Wrong input. Are you sure you would like to edit a tutee? Press Y/N ");
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
                    System.out.print("What is the ID?: ");
                    int id = sc.nextInt();
                    boolean tuteePresence = searchIndexById(id);
                    if (!tuteePresence){
                        System.out.println("No tutee found with that ID.");
                    }
                    else {editTuteeInputs(id);}
                }
                else {
                    System.out.println("What is the name?: ");
                    String name = sc.next() + " " + sc.next();
                    System.out.println(name);
                    String tuteeId = searchByNameAndReturnId(name);
                    if (tuteeId == null){
                        System.out.println("No tutee found with that name");
                    }
                    else {editTuteeInputs(Integer.parseInt(tuteeId));}
                }
            }
            returnToMainMenuOrManagement();
            save();
        }

        public static void editTuteeInputs(int id) throws Exception
        {
            Scanner sc = new Scanner(System.in);
            boolean continues = true;
            Tutee tutee = tutees.get(id);
            do {
                System.out.println("Found tutee with name " + tutee.getName() + ". What would you like to edit?" );
                System.out.println("Press N to edit Name");
                System.out.println("Press G to edit Grade");
                System.out.println("Press S to edit a Subject being learnt");
                System.out.println("Press E to edit a Session the Tutee is Available for");
                System.out.println("Press T to edit the Tutee's Tutor");
                System.out.println("Press Q to Quit Editing");
                char editInput = sc.next().charAt(0);
                if (editInput == 'N' || editInput == 'n')
                {
                    System.out.println("What would you like to change the tutee's name to? ");
                    String name = sc.nextLine();
                    tutee.setName(name);
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
                    tutee.setGradeLevel(grade);
                }
                else if (editInput == 'S' || editInput == 's') {
                    Subject subject = SubjectController.subjectInputs();
                    boolean subjectTuteePresence = tutee.searchSubject(subject.getSubjectName(), subject.getSubjectLevel()) != null; // searches the tutee's list of subjects learning
                    boolean subjectOverallPresence = SubjectController.searchSubject(subject); // searches the overall list of subjects
                    if (subjectTuteePresence) {
                        System.out.println("Subject found for tutee. Press E for editing subject and D for deleting subject");
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
                                tutee.searchSubject(subject.getSubjectName(), subject.getSubjectLevel()).setSubjectName(sc.next());
                            }
                            else {
                                System.out.println("Enter subject's new level");
                                tutee.searchSubject(subject.getSubjectName(), subject.getSubjectLevel()).setSubjectLevel(sc.next().charAt(0));
                            }
                        }
                        else {
                            tutee.getSubjectsLearning().remove(tutee.searchSubject(subject.getSubjectName(), subject.getSubjectLevel()));
                        }
                    }
                    else if (subjectOverallPresence){
                        System.out.println("Subject not found for tutee but found in overall list. Would you like to add it to the tutee's list? Press Y");
                        char addingChar = sc.next().charAt(0);
                        if (addingChar == 'Y')
                        {
                            tutee.getSubjectsLearning().add(SubjectController.searchSubjectReturnSubject(subject.getSubjectName(), subject.getSubjectLevel()));
                        }
                    }
                    else {
                        System.out.println("Subject not found at all");
                    }
                }
                else if (editInput == 'E' || editInput == 'e') {
                    NewDate date = SessionController.addSessionDateInput();
                    NewTime time = SessionController.addSessionTimeInput();
                    Session sessionPresence = SessionController.searchByTime(time);
                    String sessionKey = date.toString() + " " + time.toString();
                    if (tutee.searchSession(date, time)) {
                        System.out.println("Session found. Would you like to remove this tutee from this session? Press Y");
                        char yesOrNo = sc.next().charAt(0);
                        if (yesOrNo == 'Y') {
                            try {
                                tutee.getSessionsAvailable().replace(sessionKey, false);
                                SessionController.searchByTime(time).getTutees().remove(tutee);
                            } catch (RuntimeException r) {
                                System.out.println("Session doesn't have any tutees. No removing is needed.");
                            }
                        }
                    }
                    else if (sessionPresence != null) {
                        System.out.println("Session doesn't exist in tutee list. Would you like to have the tutee in this session? Press Y");
                        if (sc.next().charAt(0) == 'Y') {
                            tutee.getSessionsAvailable().put(sessionKey, true);
                            sessionPresence.getTutees().add(tutee);
                        }
                    }
                    else {
                        System.out.println("Session doesn't exist at all.");
                    }
                }
                else if (editInput == 'T' || editInput == 't') {
                    boolean inputInt = false;
                    int integerInput = 0;
                    while (!inputInt) {
                        try {
                            System.out.println("[1] To change tutor");
                            System.out.println("[2] To not have a tutor right now");
                            integerInput = Integer.parseInt(sc.next());
                            inputInt = true;
                        }
                        catch (Exception e) {
                            System.out.print("Incorrect input");
                        }
                    }
                    switch (integerInput) {
                        case 1:
                            System.out.println("What name does this tutor have?");
                            String name = sc.nextLine();
                            if (TutorController.searchByName(name) == null) {
                                System.out.println("Tutor doesn't exist");
                            } else {
                                tutee.setTutor(TutorController.searchByName(name));
                            }
                        case 2:
                            tutee.setTutor(null);
                        default:
                            System.out.println("Incorrect input");
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
        public static boolean searchIndexById (int ID) { //complete
            int low = 0;
            int high = tutees.size() - 1;
            while (low <= high) {
                int mid = low + (high - low) / 2;
                if (Integer.parseInt(tutees.get(mid).getId()) == ID) {
                    return true;
                }
                else if (Integer.parseInt(tutees.get(mid).getId()) < ID) {
                    low = mid + 1;
                }
                else {
                    high = mid - 1;
                }
            }
            return false;
        }

        public static Tutee searchByName(String name) { //complete
            insertionSort(tutees);
            int low = 0;
            int high = tutees.size() - 1;
            while (low <= high) {
                int mid = low + (high - low) / 2;
                if (tutees.get(mid).getName().equals(name)) {
                    return tutees.get(mid);
                }
                else if (tutees.get(mid).getId().compareTo(name)<0) {
                    low = mid + 1;
                }
                else {
                    high = mid - 1;
                }
            }
            return null;
        }
        public static String searchByNameAndReturnId(String name) throws Exception {
            load();
            if (tutees.size() > 2) {
                insertionSort(tutees);
                int low = 0;
                int high = tutees.size() - 1;
                while (low <= high) {
                    int mid = low + (high - low) / 2;
                    if (tutees.get(mid).getName().equals(name)) {
                        return tutees.get(mid).getId();
                    } else if (tutees.get(mid).getId().compareTo(name) < 0) {
                        low = mid + 1;
                    } else {
                        high = mid - 1;
                    }
                }
            }
            else {
                System.out.println("Tutee list: " + tutees.toString());
                for (Tutee tutee : tutees) {
                    System.out.println("    tutee name: " + tutee.getName());
                    if (tutee.getName().equals(name)) {
                        return tutee.getId();
                    }
                }
            }
            save();
            return null;
        }
        public static void save() throws IOException
        {
            System.out.println("Saving changes");
            try {
                FileOutputStream f = new FileOutputStream(new File("tutees.txt"));
                ObjectOutputStream o = new ObjectOutputStream(f);
                o.writeObject(tutees);
                System.out.println("Changes saved to file");
            } catch (IOException e) {
                System.out.println("Error intializing stream");
            }
        }

        public static void load() throws Exception //
        {
            ArrayList<Tutee> t = null;
            try {
                FileInputStream fi = new FileInputStream("tutees.txt");
                ObjectInputStream oi = new ObjectInputStream(fi);
                tutees = (ArrayList<Tutee>) oi.readObject();
                System.out.println("LOADED tutees from load(): " + tutees.toString());
            }
            catch (FileNotFoundException e) {
                System.out.println("File not found");
            }
            catch (ClassNotFoundException c) {
                c.printStackTrace();
            }
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

    public static boolean searchById(int ID) { //binary search by id since id is unique
        insertionSortByID(tutees);
        int low = 0;
        int high = tutees.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (Integer.parseInt(tutees.get(mid).getId()) == ID) {
                return true;
            }
            else if (Integer.parseInt(tutees.get(mid).getId()) < ID) {
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
        }
        return false;
    }

    public static void insertionSortByID(ArrayList<Tutee> tutees)
    {
        int c = 0;
        int len = tutees.size();
        for(int i = 1; i < len; i++)
        {
            Tutee temp = tutees.get(i);
            int j = i-1;
            while(j >= 0 && Integer.parseInt(tutees.get(j).getId()) - Integer.parseInt(temp.getId()) > 0)
            {
                c++;
                tutees.set(j+1, tutees.get(j));
                j--;
            }
            tutees.set(j+1, temp);
        }
    }
    }
