import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SubjectController implements Serializable{

    public static ArrayList<Subject> subjects = new ArrayList<Subject>();

    public static void main(String[] args) {
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
                addSubject();
            }
            catch (NumberFormatException ex){
                System.out.println("Incorrect input format");
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else if (userActionInput == 'D')
        {
            try {
                deleteSubject();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else {
            try {
                editSubject();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

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
        return new Subject(subjectName, subjectLevel);
    }
    public static void addSubject () throws Exception
    {
        System.out.println("Adding Subject");
        load();
        Subject subject = subjectInputs();
        subjects.add(subject);
        save();
        returnToMainMenuOrManagement();

    }
    public static void deleteSubject() throws Exception {
        System.out.println("-------------------------------");
        System.out.println("Deleting Subject");
        load();
        Subject subject = subjectInputs();
        if (searchSubject(subject)) {
            subjects.remove(searchSubjectReturnSubject(subject.getSubjectName(),subject.getSubjectLevel()));
        }
        else {
            System.out.println("Subject not found, cannot be deleted.");
        }
        save();
        returnToMainMenuOrManagement();
    }
    public static void editSubject() throws Exception {
        load();
        System.out.println("-------------------------------");
        System.out.println("Editing Subject");
        System.out.println("subjects list" + subjects.toString());
        Scanner sc = new Scanner(System.in);
        System.out.print("Subject name: ");
        String subjectName = sc.nextLine();
        System.out.print("Enter one character: H for HL, S for SL? ");
        char subjectLevel = sc.next().charAt(0);
        while (subjectLevel != 'H' && subjectLevel != 'S') {
            System.out.print("Wrong. Enter one character: H for HL, S for SL? ");
            subjectLevel = sc.next().charAt(0);
        }
        if (searchSubject(subjectName.trim(), subjectLevel)) {
            Subject newSubject = subjectInputs();
            if (!searchSubject(newSubject)) {
                subjects.set(subjects.indexOf(searchSubjectReturnSubject(subjectName.trim(), subjectLevel)), newSubject);
            }
            else {
                System.out.println("Subject already exists, cannot edit");
            }
        }
        else {
            System.out.println("Subject not found, cannot edit");
        }
        System.out.println(subjects);
        save();
        returnToMainMenuOrManagement();
    }

    public static boolean searchSubject (String subjectName, char subjectLevel) throws IOException {
        insertionSort();
        int low = 0;
        int high = subjects.size() - 1;
        while (low <= high) {
            int mid = (high + low +1) / 2;
            if (subjects.get(mid).getSubjectName().equals(subjectName) && subjects.get(mid).getSubjectLevel() == (subjectLevel)) {
                return true;
            }
            else if(subjects.get(mid).getSubjectName().compareTo(subjectName) - (subjects.get(mid).getSubjectLevel()-subjectLevel) <0 ) {
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
        }
        return false;
    }
    public static boolean searchSubject (Subject subject) throws IOException {
        if (subjects.size() > 2) {
            insertionSort();
            int low = 0;
            int high = subjects.size() - 1;
            while (low <= high) {
                int mid = low + (high - low) / 2;
                if (subjects.get(mid).getSubjectName().equals(subject.getSubjectName()) && subjects.get(mid).getSubjectLevel() == (subject.getSubjectLevel())) {
                    return true;
                } else if (subjects.get(mid).getSubjectName().compareTo(subject.getSubjectName()) < 0) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        else {
            for (Subject subjectKey: subjects) {
                if (subjectKey.getSubjectName().equals(subject.getSubjectName()) && subjectKey.getSubjectLevel() == subject.getSubjectLevel()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Subject searchSubjectReturnSubject (String subjectName, char subjectLevel) throws IOException {
        insertionSort();
        int low = 0;
        int high = subjects.size() - 1;
        while (low <= high) {
            int mid = (high + low + 1) / 2;
            if (subjects.get(mid).getSubjectName().equals(subjectName) && subjects.get(mid).getSubjectLevel() == (subjectLevel)) {
                return subjects.get(mid);
            }
            else if ((subjects.get(mid).getSubjectName().compareTo(subjectName)) - (subjects.get(mid).getSubjectLevel()-subjectLevel) <0) {
                low = mid + 1;
            }
            else {
                high = mid - 1;
            }
        }
        return null;
    }

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

    public static void save() throws IOException
    {
        System.out.println("-------------------------------");
        System.out.println("Saving changes to file");
        try {
            FileOutputStream f = new FileOutputStream(new File("subjects.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(subjects);
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
        ArrayList<Subject> t = null;
        try {
            FileInputStream fi = new FileInputStream("subjects.txt");
            ObjectInputStream oi = new ObjectInputStream(fi);
            subjects = (ArrayList<Subject>) oi.readObject();
            oi.close();
            fi.close();
            System.out.println("Data loaded");
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        catch (IOException i) {
            System.out.println("Error initializating the data stream");
        }
        catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }

}
