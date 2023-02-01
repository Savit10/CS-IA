import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class RankingController{

    public static Ranking ranking;
    public static void save() throws IOException
    {
        System.out.println("Saving changes");
        try {
            FileOutputStream f = new FileOutputStream(new File("ranking.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(ranking);
            o.close();
            f.close();
            System.out.println("Changes saved to file");
        } catch (IOException e) {
            System.out.println("Error intializing stream");
        }
    }

    public static void load() throws Exception //
    {
        Ranking ranking = null;
        try {
            FileInputStream fi = new FileInputStream("ranking.txt");
            ObjectInputStream oi = new ObjectInputStream(fi);
            ranking = (Ranking) oi.readObject();
            oi.close();
            fi.close();
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
    public static void main(String[] args) throws Exception {
        TutorController.load();
        load();
        Ranking.compileInputs();
        HashMap<Tutor, Double> tutorDoubleHashMap = Ranking.rank(Ranking.optimize(10, 0.1));

        Scanner sc = new Scanner(System.in);
        System.out.println("Press Y to Print tutor rankings");
        char printRanking = sc.next().charAt(0);
        if (printRanking == 'Y') {
            Ranking.isRankedOrNot = true;
            Ranking.rank(Ranking.optimize(1000, 0.01));
            System.out.println("Pair tutors with tutee? Press Y to do so");
            char pairRankings = sc.next().charAt(0);
            if (pairRankings == 'Y') {
                TuteeController.load();
                for (Tutee tutee: TuteeController.tutees) {
                    for (Tutor tutor: Ranking.rank(Ranking.optimize(1000, 0.01)).keySet()) {
                        // returning subjects of both tutors and tutees
                        ArrayList<Subject> subjectsTaught = tutor.getSubjectsTaught();
                        ArrayList<Subject> subjectsLearning = tutee.getSubjectsLearning();
                        for (Subject subjectTaught: subjectsTaught) {
                            for (Subject subjectLearning: subjectsLearning) {
                                if(subjectTaught.equals(subjectLearning)) {
                                    tutee.setTutor(tutor);
                                    tutor.setTutee(tutee);
                                }
                            }
                        }
                    }
                }
            }
        }
        save();
    }
    public static void addRating(double rating) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Name of tutor rating to add to: ");
        String name = sc.nextLine();
        if (TutorController.searchByName(name) == null) {
            System.out.println("Tutor not found.");
        }
        else {
            double newRating = 0;
            System.out.println("Rating: ");
            boolean inputException = false;
            while (!inputException) {
                try {
                    newRating = sc.nextDouble();
                    inputException = true;
                } catch (Exception e) {
                    System.out.println("Incorrect input format");
                }
            }
            System.out.println("Which Hashmap to add rating to?");
            System.out.println("[S] to add to SubjectUnderstandingRatings");
            System.out.println("[E] to add to ExplanationAbilityRatings");
            System.out.println("[A] to add to Attitude ratings");
            System.out.println("[P] to add to progress ratings");
            char input = sc.next().charAt(0);
            switch (input) {
                case 'S':
                    Ranking.subjectUnderstandingRatings.get(TutorController.searchByName(name)).add(newRating);
                    break;
                case 'E':
                    Ranking.explanationAbilityRatings.get(TutorController.searchByName(name)).add(newRating);
                    break;
                case 'A':
                    Ranking.attitudeAbilityRatings.get(TutorController.searchByName(name)).add(newRating);
                    break;
                case 'P':
                    Ranking.progressRatings.get(TutorController.searchByName(name)).add(newRating);
                    break;
                default:
                    System.out.println("Incorrect input, cannot add");
                    break;
            }
        }
    }
    public static void deleteRating(double rating, int index) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Name of tutor rating to delete from: ");
        String name = sc.nextLine();
        if (TutorController.searchByName(name) == null) {
            System.out.println("Tutor not found.");
        }
        else {
            System.out.println("Which Hashmap to delete rating from?");
            System.out.println("[S] to add to SubjectUnderstandingRatings");
            System.out.println("[E] to add to ExplanationAbilityRatings");
            System.out.println("[A] to add to Attitude ratings");
            System.out.println("[P] to add to progress ratings");
            char input = sc.next().charAt(0);
            switch (input) {
                case 'S':
                    Ranking.subjectUnderstandingRatings.get(TutorController.searchByName(name)).remove(index);
                    break;
                case 'E':
                    Ranking.explanationAbilityRatings.get(TutorController.searchByName(name)).remove(index);
                    break;
                case 'A':
                    Ranking.attitudeAbilityRatings.get(TutorController.searchByName(name)).remove(index);
                    break;
                case 'P':
                    Ranking.progressRatings.get(TutorController.searchByName(name)).remove(index);
                    break;
                default:
                    System.out.println("Incorrect input, cannot add");
                    break;
            }
        }
    }
}
