import java.io.*;
import java.util.*;

public class RankingController {
    // static attribute
    public static Ranking ranking;
    // gives options to return to the main menu or to return to the main method of this class
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
    //serializes ranking data to file
    public static void save() throws IOException
    {
        System.out.println("Saving changes");
        try {
            FileOutputStream f = new FileOutputStream(new File("ranking.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(ranking); // writes object to file
            o.close();
            f.close();
            System.out.println("Changes saved to file");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

    //deserializes ranking data from file
    public static void load() throws Exception //
    {
        System.out.println("-------------------------------");
        ranking = null;
        try {
            FileInputStream fi = new FileInputStream("ranking.txt");
            ObjectInputStream oi = new ObjectInputStream(fi);
            ranking = (Ranking) oi.readObject(); // reads object from file
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
        boolean continues;
        do {
            System.out.println("-------------------------------");
            System.out.println("Adding a rating to a tutor");
            Scanner sc = new Scanner(System.in);
            System.out.println("Name of tutor rating to add to: ");
            String name = sc.nextLine();
            if (TutorController.searchByName(name) == null) { // if tutor to add rating to is not found
                System.out.println("Tutor not found, Exiting Rankings.");
                continues = false; // exits loop
            }
            else {
                double newRating = 0;
                System.out.println("Which Hashmap to add rating to?");
                System.out.println("[S] to add to SubjectUnderstandingRatings");
                System.out.println("[E] to add to ExplanationAbilityRatings");
                System.out.println("[A] to add to Attitude ratings");
                System.out.println("[P] to add to progress ratings");
                char input = sc.next().charAt(0);
                System.out.print("Rating: ");
                sc.nextLine();
                try { // validation for ensuring data is a double
                    newRating = Double.parseDouble(sc.nextLine());
                } catch (Exception e) {
                    System.out.println("Not a double");
                }
                switch (input) {
                    case 'S':
                        if (ranking.subjectUnderstandingRatings.get(TutorController.searchByName(name)) == null) { // checking if the LinkedHashMap for that tutor is null
                            ArrayList<Double> subjectUnderstandingRatings = new ArrayList<>();
                            subjectUnderstandingRatings.add(newRating);
                            ranking.subjectUnderstandingRatings.put(TutorController.searchByName(name), subjectUnderstandingRatings); // adds instantiated arraylist with new rating to the LinkedHashMap, mapping the tutor to it
                        }
                        else {
                            ranking.subjectUnderstandingRatings.get(TutorController.searchByName(name)).add(newRating); // adds rating to existing arraylist mapped to the tutor
                        }
                        break;
                    case 'E':
                        if (ranking.explanationAbilityRatings.get(TutorController.searchByName(name)) == null) { // checking if the LinkedHashMap for that tutor is null
                            ArrayList<Double> explanationAbilityRatings = new ArrayList<>();
                            explanationAbilityRatings.add(newRating);
                            ranking.explanationAbilityRatings.put(TutorController.searchByName(name), explanationAbilityRatings); // adds instantiated arraylist with new rating to the LinkedHashMap, mapping the tutor to it
                        }
                        else {
                            ranking.explanationAbilityRatings.get(TutorController.searchByName(name)).add(newRating); // adds rating to existing arraylist mapped to the tutor
                        }
                        break;
                    case 'A':
                        if (ranking.attitudeAbilityRatings.get(TutorController.searchByName(name)) == null) { // checking if the LinkedHashMap for that tutor is null
                            ArrayList<Double> attitudeAbilityRatings = new ArrayList<>();
                            attitudeAbilityRatings.add(newRating);
                            ranking.attitudeAbilityRatings.put(TutorController.searchByName(name), attitudeAbilityRatings); // adds instantiated arraylist with new rating to the LinkedHashMap, mapping the tutor to it
                        }
                        else {
                            ranking.attitudeAbilityRatings.get(TutorController.searchByName(name)).add(newRating); // checking if the LinkedHashMap for that tutor is null
                        }
                        break;
                    case 'P':
                        if (ranking.progressRatings.get(TutorController.searchByName(name)) == null) { // checking if the LinkedHashMap for that tutor is null
                            ArrayList<Double> progressRatings = new ArrayList<>();
                            progressRatings.add(newRating);
                            ranking.progressRatings.put(TutorController.searchByName(name), progressRatings); // adds instantiated arraylist with new rating to the LinkedHashMap, mapping the tutor to it
                        }
                        else {
                            ranking.progressRatings.get(TutorController.searchByName(name)).add(newRating); // checking if the LinkedHashMap for that tutor is null
                        }
                        break;
                    default:
                        System.out.println("Incorrect input, cannot add");
                        break;
                }
                System.out.println("Want to add a different rating? Press Y");
                char yesOrNo = sc.next().charAt(0);
                continues = yesOrNo == 'Y'; // sets continues to if (yesOrNo == 'Y')
            }
        }  while (continues) ;
    }
    public static void main(String[] args) throws Exception {
        System.out.println("-------------------------------");
        System.out.println("Ranking Tutors Management");
        TutorController.load(); // loads tutors data
        load(); // loads existing rankings data
        System.out.println("Tutor Rankings");
        ranking.rank(); // prints out tutor rankings
        addRating(); // calls add rating method to update rankings
        System.out.println("Updated Tutor Rankings");
        ranking.rank(); // prints out updated tutor rankings
        save(); // saves updated data to file
        returnToMainMenuOrManagement(); // option to return to this main method or to the main menu
    }
}