import java.io.Serializable;
import java.util.Scanner;
public class Main {
    public static void main(String [] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        boolean continues = true;
        System.out.println("-------------------------------");
        System.out.println("Welcome to the Peer Tutoring Management System!");
        System.out.println("Main Menu");
        do
        {
            System.out.println("Press [1] to Manage Tutors");
            System.out.println("Press [2] to Manage Tutees");
            System.out.println("Press [3] to Manage Peer Tutoring Sessions");
            System.out.println("Press [4] to Manage Tutor Rankings");
            System.out.println("Press [5] to Quit Application");
            int input = sc.nextInt();
            switch (input) {
                case 1:
                    TutorController.main(null);
                    continues = false;
                    break; // calling main method of tutor controller
                case 2:
                    TuteeController.main(null);
                    continues = false;
                    break;// calling main method of tutee controller
                case 3:
                    SessionController.main(null);
                    continues = false;
                    break;// calling main method of session controller
                case 4:
                    RankingController.main(null);
                    continues = false;
                    break;// calling main method of ranking controller
                case 5:
                    continues = false;
                    break;// quitting application
                default:
                    System.out.println("Incorrect input");
                    break;
            }
            SessionController.save();
            SubjectController.save();
            TutorController.save();
            TuteeController.save();
            RankingController.load();
        }
        while(continues);
        sc.close();
    }
}