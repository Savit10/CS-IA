import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class RankingController {
    public static Ranking ranking;

    private static void returnToMainMenuOrManagement() throws Exception {
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
        ranking = null;
        try {
            FileInputStream fi = new FileInputStream("ranking.txt");
            ObjectInputStream oi = new ObjectInputStream(fi);
            ranking = (Ranking) oi.readObject();
            oi.close();
            fi.close();
            System.out.println("Loading files");
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
        Scanner sc = new Scanner(System.in);
        System.out.println("Name of tutor rating to add to: ");
        String name = sc.nextLine();
        if (TutorController.searchByName(name) == null) {
            System.out.println("Tutor not found.");
        }
        else {
            System.out.println("Which Hashmap to add rating to?");
            System.out.println("[S] to add to SubjectUnderstandingRatings");
            System.out.println("[E] to add to ExplanationAbilityRatings");
            System.out.println("[A] to add to Attitude ratings");
            System.out.println("[P] to add to progress ratings");
            char input = sc.next().charAt(0);
            double newRating = inputRating();
            switch (input) {
                case 'S':
                    System.out.println(ranking.subjectUnderstandingRatings);
                    System.out.println(TutorController.searchByName(name));
                    System.out.println(newRating);
                    ranking.subjectUnderstandingRatings.get(TutorController.searchByName(name)).add(newRating);
                    System.out.println(ranking.subjectUnderstandingRatings);
                    break;
                case 'E':
                    ranking.explanationAbilityRatings.get(TutorController.searchByName(name)).add(newRating);
                    break;
                case 'A':
                    ranking.attitudeAbilityRatings.get(TutorController.searchByName(name)).add(newRating);
                    break;
                case 'P':
                    ranking.progressRatings.get(TutorController.searchByName(name)).add(newRating);
                    break;
                default:
                    System.out.println("Incorrect input, cannot add");
                    break;
            }
        }
    }

    public static double inputRating() throws Exception{
        Scanner sc = new Scanner(System.in);
        double newRating = 0;
        System.out.println("Rating: ");
        boolean inputException = false;
        while (!inputException) {
            try {
                newRating = Double.parseDouble(sc.next());
                inputException = true;
            } catch (Exception e) {
                System.out.println("Incorrect input format");
            }
        }
        return newRating;
    }
    public static void main(String[] args) throws Exception {
        TutorController.load();
        load();
        Scanner sc = new Scanner(System.in);
        System.out.println(TutorController.tutors);
        HashMap<Tutor, ArrayList<Double>> subjectUnderstandingRatings = new HashMap<>();
        HashMap<Tutor, ArrayList<Double>> explanationAbilityRatings = new HashMap<>();
        HashMap<Tutor, ArrayList<Double>> attitudeAbilityRatings = new HashMap<>();
        HashMap<Tutor, ArrayList<Double>> progressRatings = new HashMap<>();
        ArrayList<Double> subjectRatings = new ArrayList<>();
        subjectRatings.add(0, 5.2);
        subjectRatings.add(1, 5.5);
        subjectRatings.add(2, 5.5);
        subjectRatings.add(3, 5.4);
        subjectRatings.add(4, 6.1);
        subjectRatings.add(5, 6.7);
        subjectRatings.add(6, 6.9);
        subjectRatings.add(7, 7.5);
        subjectRatings.add(8, 7.1);
        subjectRatings.add(9, 7.6);
        subjectUnderstandingRatings.put(TutorController.tutors.get(0), subjectRatings);
        System.out.println(subjectUnderstandingRatings);
        ArrayList<Double> subjectRatings1 = new ArrayList<>();
        subjectRatings1.add(0, 4.3);
        subjectRatings1.add(1, 5.2);
        subjectRatings1.add(2, 5.7);
        subjectRatings1.add(3, 6.3);
        subjectRatings1.add(4, 6.5);
        subjectRatings1.add(5, 6.6);
        subjectRatings1.add(6, 6.5);
        subjectRatings1.add(7, 6.9);
        subjectRatings1.add(8, 7.0);
        subjectRatings1.add(9, 7.1);
        subjectUnderstandingRatings.put(TutorController.tutors.get(1), subjectRatings1);
        System.out.println(subjectUnderstandingRatings);
        ArrayList<Double> subjectRatings2 = new ArrayList<>();
        subjectRatings2.add(0, 7.3);
        subjectRatings2.add(1, 7.4);
        subjectRatings2.add(2, 7.6);
        subjectRatings2.add(3, 7.5);
        subjectRatings2.add(4, 7.6);
        subjectRatings2.add(5, 7.8);
        subjectRatings2.add(6, 7.7);
        subjectRatings2.add(7, 7.7);
        subjectRatings2.add(8, 7.9);
        subjectRatings2.add(9, 8.0);
        subjectUnderstandingRatings.put(TutorController.tutors.get(2), subjectRatings2);
        System.out.println(subjectUnderstandingRatings);
        ArrayList<Double> subjectRatings3 = new ArrayList<>();
        subjectRatings3.add(0, 8.0);
        subjectRatings3.add(1, 7.6);
        subjectRatings3.add(2, 7.7);
        subjectRatings3.add(3, 8.1);
        subjectRatings3.add(4, 8.2);
        subjectRatings3.add(5, 8.1);
        subjectRatings3.add(6, 8.3);
        subjectRatings3.add(7, 8.5);
        subjectRatings3.add(8, 8.4);
        subjectRatings3.add(9, 8.6);
        subjectUnderstandingRatings.put(TutorController.tutors.get(3), subjectRatings3);
        System.out.println(subjectUnderstandingRatings);
        ArrayList<Double> subjectRatings4 = new ArrayList<>();
        subjectRatings4.add(0, 3.2);
        subjectRatings4.add(1, 3.7);
        subjectRatings4.add(2, 4.1);
        subjectRatings4.add(3, 4.0);
        subjectRatings4.add(4, 4.9);
        subjectRatings4.add(5, 4.8);
        subjectRatings4.add(6, 5.2);
        subjectRatings4.add(7, 5.4);
        subjectRatings4.add(8, 5.7);
        subjectRatings4.add(9, 5.6);
        System.out.println(TutorController.tutors.get(4));
        subjectUnderstandingRatings.put(TutorController.tutors.get(4), subjectRatings4);

        ArrayList<Double> explanationRatings = new ArrayList<>();
        explanationRatings.add(0, 1.3);
        explanationRatings.add(1, 1.2);
        explanationRatings.add(2, 1.5);
        explanationRatings.add(3, 2.0);
        explanationRatings.add(4, 2.5);
        explanationRatings.add(5, 3.2);
        explanationRatings.add(6, 3.9);
        explanationRatings.add(7, 3.6);
        explanationRatings.add(8, 3.8);
        explanationRatings.add(9, 4.6);
        explanationAbilityRatings.put(TutorController.tutors.get(0), explanationRatings);
        System.out.println(explanationAbilityRatings);
        ArrayList<Double> explanationRatings1 = new ArrayList<>();
        explanationRatings1.add(0, 5.2);
        explanationRatings1.add(1, 5.6);
        explanationRatings1.add(2, 6.0);
        explanationRatings1.add(3, 6.1);
        explanationRatings1.add(4, 6.4);
        explanationRatings1.add(5, 6.7);
        explanationRatings1.add(6, 6.8);
        explanationRatings1.add(7, 6.7);
        explanationRatings1.add(8, 6.7);
        explanationRatings1.add(9, 6.8);
        explanationAbilityRatings.put(TutorController.tutors.get(1), explanationRatings1);
        System.out.println(explanationAbilityRatings);
        ArrayList<Double> explanationRatings2 = new ArrayList<>();
        explanationRatings2.add(0, 7.5);
        explanationRatings2.add(1, 7.4);
        explanationRatings2.add(2, 7.7);
        explanationRatings2.add(3, 7.8);
        explanationRatings2.add(4, 8.1);
        explanationRatings2.add(5, 8.0);
        explanationRatings2.add(6, 7.9);
        explanationRatings2.add(7, 8.0);
        explanationRatings2.add(8, 8.3);
        explanationRatings2.add(9, 8.5);
        explanationAbilityRatings.put(TutorController.tutors.get(2), explanationRatings2);
        System.out.println(explanationAbilityRatings);
        ArrayList<Double> explanationRatings3 = new ArrayList<>();
        explanationRatings3.add(0, 8.7);
        explanationRatings3.add(1, 8.8);
        explanationRatings3.add(2, 8.7);
        explanationRatings3.add(3, 8.9);
        explanationRatings3.add(4, 9.1);
        explanationRatings3.add(5, 8.6);
        explanationRatings3.add(6, 8.9);
        explanationRatings3.add(7, 8.7);
        explanationRatings3.add(8, 8.5);
        explanationRatings3.add(9, 8.8);
        explanationAbilityRatings.put(TutorController.tutors.get(3), explanationRatings3);
        ArrayList<Double> explanationRatings4 = new ArrayList<>();
        explanationRatings4.add(0, 4.3);
        explanationRatings4.add(1, 4.5);
        explanationRatings4.add(2, 4.4);
        explanationRatings4.add(3, 4.6);
        explanationRatings4.add(4, 4.8);
        explanationRatings4.add(5, 5.0);
        explanationRatings4.add(6, 4.9);
        explanationRatings4.add(7, 5.3);
        explanationRatings4.add(8, 5.6);
        explanationRatings4.add(9, 5.5);
        explanationAbilityRatings.put(TutorController.tutors.get(4), explanationRatings4);

        ArrayList<Double> attitudeRatings = new ArrayList<>();
        attitudeRatings.add(0, 8.9);
        attitudeRatings.add(1, 8.3);
        attitudeRatings.add(2, 7.7);
        attitudeRatings.add(3, 7.0);
        attitudeRatings.add(4, 6.8);
        attitudeRatings.add(5, 6.3);
        attitudeRatings.add(6, 6.1);
        attitudeRatings.add(7, 4.5);
        attitudeRatings.add(8, 4.3);
        attitudeRatings.add(9, 3.7);
        attitudeAbilityRatings.put(TutorController.tutors.get(0), attitudeRatings);
        ArrayList<Double> attitudeRatings1 = new ArrayList<>();
        attitudeRatings1.add(0, 9.6);
        attitudeRatings1.add(1, 9.3);
        attitudeRatings1.add(2, 9.1);
        attitudeRatings1.add(3, 8.9);
        attitudeRatings1.add(4, 9.0);
        attitudeRatings1.add(5, 8.8);
        attitudeRatings1.add(6, 8.6);
        attitudeRatings1.add(7, 8.7);
        attitudeRatings1.add(8, 8.3);
        attitudeRatings1.add(9, 6.8);
        attitudeAbilityRatings.put(TutorController.tutors.get(1), attitudeRatings1);
        ArrayList<Double> attitudeRatings2 = new ArrayList<>();
        attitudeRatings2.add(0, 4.5);
        attitudeRatings2.add(1, 3.2);
        attitudeRatings2.add(2, 3.6);
        attitudeRatings2.add(3, 4.4);
        attitudeRatings2.add(4, 3.7);
        attitudeRatings2.add(5, 3.8);
        attitudeRatings2.add(6, 3.3);
        attitudeRatings2.add(7, 3.9);
        attitudeRatings2.add(8, 3.2);
        attitudeRatings2.add(9, 1.7);
        attitudeAbilityRatings.put(TutorController.tutors.get(2), attitudeRatings2);
        ArrayList<Double> attitudeRatings3 = new ArrayList<>();
        attitudeRatings3.add(0, 6.6);
        attitudeRatings3.add(1, 6.3);
        attitudeRatings3.add(2, 5.7);
        attitudeRatings3.add(3, 5.3);
        attitudeRatings3.add(4, 5.5);
        attitudeRatings3.add(5, 5.2);
        attitudeRatings3.add(6, 5.1);
        attitudeRatings3.add(7, 4.9);
        attitudeRatings3.add(8, 4.2);
        attitudeRatings3.add(9, 1.9);
        attitudeAbilityRatings.put(TutorController.tutors.get(3), attitudeRatings3);
        ArrayList<Double> attitudeRatings4 = new ArrayList<>();
        attitudeRatings4.add(0, 3.2);
        attitudeRatings4.add(1, 6.7);
        attitudeRatings4.add(2, 6.2);
        attitudeRatings4.add(3, 5.4);
        attitudeRatings4.add(4, 5.7);
        attitudeRatings4.add(5, 5.0);
        attitudeRatings4.add(6, 4.4);
        attitudeRatings4.add(7, 4.2);
        attitudeRatings4.add(8, 2.6);
        attitudeRatings4.add(9, 1.4);
        attitudeAbilityRatings.put(TutorController.tutors.get(4), attitudeRatings4);

        ArrayList<Double> progressRatingsList = new ArrayList<>();
        progressRatingsList.add(0, 1.0);
        progressRatingsList.add(1, 1.4);
        progressRatingsList.add(2, 1.9);
        progressRatingsList.add(3, 2.5);
        progressRatingsList.add(4, 3.3);
        progressRatingsList.add(5, 3.9);
        progressRatingsList.add(6, 4.5);
        progressRatingsList.add(7, 5.3);
        progressRatingsList.add(8, 5.9);
        progressRatingsList.add(9, 6.3);
        progressRatings.put(TutorController.tutors.get(0), progressRatingsList);
        ArrayList<Double> progressRatingsList1 = new ArrayList<>();
        progressRatingsList1.add(0, 0.3);
        progressRatingsList1.add(1, 0.7);
        progressRatingsList1.add(2, 1.4);
        progressRatingsList1.add(3, 2.1);
        progressRatingsList1.add(4, 2.7);
        progressRatingsList1.add(5, 3.3);
        progressRatingsList1.add(6, 3.8);
        progressRatingsList1.add(7, 4.5);
        progressRatingsList1.add(8, 5.1);
        progressRatingsList1.add(9, 5.6);
        progressRatings.put(TutorController.tutors.get(1), progressRatingsList1);;
        ArrayList<Double> progressRatingsList2 = new ArrayList<>();
        progressRatingsList2.add(0, 1.4);
        progressRatingsList2.add(1, 2.5);
        progressRatingsList2.add(2, 3.3);
        progressRatingsList2.add(3, 4.3);
        progressRatingsList2.add(4, 5.5);
        progressRatingsList2.add(5, 6.2);
        progressRatingsList2.add(6, 6.9);
        progressRatingsList2.add(7, 7.4);
        progressRatingsList2.add(8, 7.9);
        progressRatingsList2.add(9, 8.4);
        progressRatings.put(TutorController.tutors.get(2), progressRatingsList2);
        ArrayList<Double> progressRatingsList3 = new ArrayList<>();
        progressRatingsList3.add(0, 0.2);
        progressRatingsList3.add(1, 0.4);
        progressRatingsList3.add(2, 0.8);
        progressRatingsList3.add(3, 1.3);
        progressRatingsList3.add(4, 1.6);
        progressRatingsList3.add(5, 1.9);
        progressRatingsList3.add(6, 2.3);
        progressRatingsList3.add(7, 2.9);
        progressRatingsList3.add(8, 3.6);
        progressRatingsList3.add(9, 4.2);
        progressRatings.put(TutorController.tutors.get(3), progressRatingsList3);
        ArrayList<Double> progressRatingsList4 = new ArrayList<>();
        progressRatingsList4.add(0, 0.8);
        progressRatingsList4.add(1, 1.9);
        progressRatingsList4.add(2, 2.6);
        progressRatingsList4.add(3, 3.2);
        progressRatingsList4.add(4, 3.7);
        progressRatingsList4.add(5, 4.1);
        progressRatingsList4.add(6, 4.5);
        progressRatingsList4.add(7, 5.0);
        progressRatingsList4.add(8, 5.4);
        progressRatingsList4.add(9, 5.7);
        progressRatings.put(TutorController.tutors.get(4), progressRatingsList4);
        ranking = new Ranking(subjectUnderstandingRatings, explanationAbilityRatings, attitudeAbilityRatings, progressRatings);
        ranking.rank();
        System.out.println("[A] to add a rating to a tutor");
        if (sc.next().charAt(0) == 'A') {
            //try {
                addRating();
            /*} catch (Exception e) {
                System.out.println("Incorrect input");
            } */
        }
        save();
        returnToMainMenuOrManagement();
    }
}
