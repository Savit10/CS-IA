import java.io.Serializable;
import java.util.*;

public class Ranking implements Serializable {
    // attributes
    public LinkedHashMap<Tutor, ArrayList<Double>> subjectUnderstandingRatings; // the tutor's understanding and knowledge of the subject content
    public LinkedHashMap<Tutor, ArrayList<Double>> explanationAbilityRatings; // the tutor's clarity and ability to explain and simplify concepts
    public LinkedHashMap<Tutor, ArrayList<Double>> attitudeAbilityRatings; // the tutor's perceived effort and intention in tutoring
    public LinkedHashMap<Tutor, ArrayList<Double>> progressRatings; // the tutee's progress in the subject as a consequence of taking part in tutoring

    // constructors
    public Ranking(LinkedHashMap<Tutor, ArrayList<Double>> subjectUnderstandingRatings, LinkedHashMap<Tutor, ArrayList<Double>> explanationAbilityRatings, LinkedHashMap<Tutor, ArrayList<Double>> attitudeAbilityRatings, LinkedHashMap<Tutor, ArrayList<Double>> progressRatings) {
        this.subjectUnderstandingRatings = subjectUnderstandingRatings;
        this.explanationAbilityRatings = explanationAbilityRatings;
        this.attitudeAbilityRatings = attitudeAbilityRatings;
        this.progressRatings = progressRatings;
    }

    public Ranking() {

    }

    //methods

    // calculates a moving weighted average of the data, weighting newer data entries higher in the calculation
    private static double movingAverage(double subjectRating, double newRating) {
        double alpha = 0.1; // alpha - indicates the weight of the newest piece of data, with older pieces of data getting multiplied with the ratio 1-alpha
        if (subjectRating <= 0) {
            subjectRating = newRating;
        } else {
            subjectRating = (alpha * newRating) + ((1-alpha) * subjectRating);
        }
        return subjectRating;
    }

    //calculates the overall rating of a certain attribute using the list of doubles provided
    public static LinkedHashMap<Tutor, Double> calculateOverallRating(LinkedHashMap<Tutor, ArrayList<Double>> ratings) {
        LinkedHashMap<Tutor, Double> overallRatings = new LinkedHashMap<>();
        double overallRating = 0;
        for (Tutor tutor : ratings.keySet()) { //iterates through hashmap's keys
            ArrayList<Double> ratingsList = ratings.get(tutor);
            for (Double rating : ratingsList) { //iterates through the arraylist
                overallRating = movingAverage(overallRating, rating); // calls moving average method for each new piece of data
            }
            overallRatings.put(tutor, overallRating); // adds average to overallRatings
        }
        return overallRatings;
    }
    public void rank() {
        //calculates the overall rating of the list of doubles and instantiates a new HashMap with key: Tutor, Double: overall attribute rating
        LinkedHashMap<Tutor, Double> overallSubjectUnderstanding = calculateOverallRating(this.subjectUnderstandingRatings);
        LinkedHashMap<Tutor, Double> overallExplanationAbility = calculateOverallRating(this.explanationAbilityRatings);
        LinkedHashMap<Tutor, Double> overallTutoringAttitude = calculateOverallRating(this.attitudeAbilityRatings);
        LinkedHashMap<Tutor, Double> overallTuteeProgress = calculateOverallRating(this.progressRatings);
        //creates a Hashmap of Maps with all the ratings to add to
        List<LinkedHashMap<Tutor, Double>> listOfMaps = new ArrayList<>();
        listOfMaps.add(overallSubjectUnderstanding);
        listOfMaps.add(overallExplanationAbility);
        listOfMaps.add(overallTutoringAttitude);
        listOfMaps.add(overallTuteeProgress);
        //LinkedHashMap to map all the overall ratings from each category of a tutor in each category to the tutor
        LinkedHashMap<Tutor, ArrayList<Double>> tutorOverallRatings = new LinkedHashMap<>();
        // list of tutors to map to
        ArrayList<Tutor> tutorArrayList = new ArrayList<>(overallSubjectUnderstanding.keySet());
        for (Tutor tutor: tutorArrayList) { // iterates through the tutors list
            ArrayList<Double> ratings = new ArrayList<>();
            for (LinkedHashMap<Tutor, Double> currentHashMap : listOfMaps) { // iterates through the List of Hashmaps to get the rating of each tutor in one category
                ratings.add(currentHashMap.get(tutor));
            }
            tutorOverallRatings.put(tutor, ratings);
        }
        //LinkedHashMap mapping tutor to their overall rating
        LinkedHashMap<Tutor, Double> tutorOverallRating = new LinkedHashMap<>();
        double overallTutorRating = 0;
        for (Tutor tutor: tutorOverallRatings.keySet()) {
            overallTutorRating = 0.4*tutorOverallRatings.get(tutor).get(0) + 0.3*tutorOverallRatings.get(tutor).get(1) + 0.2*tutorOverallRatings.get(tutor).get(2) + 0.1*tutorOverallRatings.get(tutor).get(3);
            tutorOverallRating.put(tutor, overallTutorRating);
        }
        // list of tutors
        ArrayList<Tutor> tutors = new ArrayList<>(tutorOverallRating.keySet());
        //list of their overall ratings to add to
        ArrayList<Double> doubles = new ArrayList<>();
        for (Tutor tutor: tutors) {
            doubles.add(tutorOverallRating.get(tutor));
        }
        //parallel sorts tutors with their ratings using insertion sort
        for(int i = 1; i < doubles.size(); i++)
        {
            double temp = doubles.get(i);
            Tutor tempTutor = tutors.get(i);
            int j = i-1;
            while(j >= 0  && (temp < doubles.get(j)))
            {
                doubles.set(j+1, doubles.get(j));
                tutors.set(j+1, tutors.get(j));
                j--;
            }
            doubles.set(j+1, temp);
            tutors.set(j+1, tempTutor);
        }
        //reversing list using stacks
        Stack <Double> doubleStack = new Stack<>();
        Stack <Tutor> tutorStack = new Stack<>();
        for (Double aDouble : doubles) {
            doubleStack.push(aDouble);
        }
        for(int i = 0; i < doubles.size(); i++) {
            doubles.set(i, doubleStack.pop());
        }
        for (Tutor tutor : tutors) {
            tutorStack.push(tutor);
        }
        for(int i = 0; i < tutors.size(); i++) {
            tutors.set(i, tutorStack.pop());
        }
        // printing rankings
        for (int i = 0; i < tutors.size(); i++) {
            System.out.println("Rank " + (i+1) + ": " + tutors.get(i).getName() + ", Tutor Rating: " + doubles.get(i));
        }
    }
}
