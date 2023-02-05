import java.io.Serializable;
import java.util.Scanner;
public class Main {
    public static void main(String [] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        boolean continues = false;
        System.out.println("-------------------------------");
        System.out.println("Main Menu");
        do
        {
            int input = 0;
            boolean inputMisMatch = true;
            while(inputMisMatch) {
                System.out.println("Press [1] to Manage Tutors");
                System.out.println("Press [2] to Manage Tutees");
                System.out.println("Press [3] to Manage Peer Tutoring Sessions");
                System.out.println("Press [4] to Manage Tutor Rankings");
                System.out.println("Press [5] to Manage Subjects");
                System.out.println("Press [6] to Quit Application");
                try {
                    input = Integer.parseInt(sc.next());
                    if (input != 1 && input != 2 && input != 3 && input != 4 && input != 5 && input != 6) {
                        System.out.println("Incorrect input.");
                    }
                    else {
                        inputMisMatch = false;
                    }
                } catch (Exception e) {
                        System.out.println("Incorrect input format");
                    }
                }
            switch (input) {
                case 1:
                    TutorController.main(null);
                    break;
                case 2:
                    TuteeController.main(null);
                    break;
                case 3:
                    SessionController.main(null);
                    break;
                case 4:
                    RankingController.main(null);
                    break;
                case 5:
                    SubjectController.main(null);
                    break;
                case 6:
                    continues = true;
                    break;
                default:
                    System.out.println("Incorrect input");
                    continues = false;
                    break;
            }
        }
        while(!continues);
    }
}