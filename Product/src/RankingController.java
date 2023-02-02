import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class RankingController {
    public static Ranking ranking;

    private static void returnToMainMenuOrManagement() throws Exception {
        System.out.println("-------------------------------");
        Scanner sc = new Scanner(System.in);
        char returnToWhat = ' ';
        do {
            System.out.println("[R] to return to Tutor Rankings");
            System.out.println("[M] to return to Main Menu");
            returnToWhat = sc.next().charAt(0);
        } while (returnToWhat != 'R' && returnToWhat != 'M');
        if (returnToWhat == 'R') {
            RankingController.main(null);
        } else {
            Main.main(null);
        }
    }
    public static void save() throws IOException
    {
        System.out.println("-------------------------------");
        System.out.println("Saving changes");
        try {
            FileOutputStream f = new FileOutputStream(new File("ranking.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(ranking);
            o.close();
            f.close();
            System.out.println("Changes saved to file");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

    public static void load() throws Exception //
    {
        System.out.println("-------------------------------");
        System.out.println("Loading data");
        ranking = null;
        try {
            FileInputStream fi = new FileInputStream("ranking.txt");
            ObjectInputStream oi = new ObjectInputStream(fi);
            ranking = (Ranking) oi.readObject();
            oi.close();
            fi.close();
            System.out.println("Data Loaded");
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        catch (IOException i) {
            System.out.println("Error initializing the data stream");
        }
        catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }

    public static void addRating() throws Exception {
        boolean continues = false;
        do {
            System.out.println("-------------------------------");
            System.out.println("Adding a rating to a tutor");
            Scanner sc = new Scanner(System.in);
            System.out.println("Name of tutor rating to add to: ");
            String name = sc.nextLine();
            if (TutorController.searchByName(name) == null) {
                System.out.println("Tutor not found.");
            }
            else {
                double newRating = 0;
                System.out.println("Rating: ");
                newRating = Double.parseDouble(sc.next());
                System.out.println("Which Hashmap to add rating to?");
                System.out.println("[S] to add to SubjectUnderstandingRatings");
                System.out.println("[E] to add to ExplanationAbilityRatings");
                System.out.println("[A] to add to Attitude ratings");
                System.out.println("[P] to add to progress ratings");
                char input = sc.next().charAt(0);
                switch (input) {
                    case 'S':
                        ranking.getSubjectUnderstandingRatings().get(TutorController.searchByName(name)).add(newRating);
                        break;
                    case 'E':
                        ranking.getExplanationAbilityRatings().get(TutorController.searchByName(name)).add(newRating);
                        break;
                    case 'A':
                        ranking.getAttitudeAbilityRatings().get(TutorController.searchByName(name)).add(newRating);
                        break;
                    case 'P':
                        ranking.getProgressRatings().get(TutorController.searchByName(name)).add(newRating);
                        break;
                    default:
                        System.out.println("Incorrect input, cannot add");
                        break;
                }
            }
            System.out.println("Want to add a different rating? Press Y");
            char yesOrNo = sc.next().charAt(0);
            if (yesOrNo == 'Y') {
                continues = true;
            }
        } while (continues);
    }
    public static void main(String[] args) throws Exception {
        System.out.println("-------------------------------");
        System.out.println("Ranking Tutors Management");
        TutorController.load();
        load();
        Scanner sc = new Scanner(System.in);
        System.out.println("Tutor Rankings");
        ranking.rank();
        System.out.println("[A] to add a rating to a tutor");
        if (sc.next().charAt(0) == 'A') {
            try {
                addRating();
            } catch (Exception e) {
                System.out.println("Incorrect input");
            }
        }
        System.out.println("Update Tutor Rankings");
        ranking.rank();
        save();
        returnToMainMenuOrManagement();
    }
}
