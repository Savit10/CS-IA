import java.io.Serializable;
import java.util.*;

public class Ranking implements Serializable {
    //attributes - feedback from tutees about the tutors
    public static HashMap<Tutor, ArrayList<Double>> subjectUnderstandingRatings = new HashMap<>(); // the tutor's understanding and knowledge of the subject content
    public static HashMap<Tutor, ArrayList<Double>> explanationAbilityRatings = new HashMap<>(); // the tutor's clarity and ability to explain and simplify concepts
    public static HashMap<Tutor, ArrayList<Double>> attitudeAbilityRatings = new HashMap<>(); // the tutor's percieved effort and intention in tutoring
    public static HashMap<Tutor, ArrayList<Double>> progressRatings = new HashMap<>(); // the tutee's progress in the subject as a consequence of taking part in tutoring
    public static double[][] tutorOverallRatings = new double[4][5]; //the overall ratings of the tutor's based on these ratings
    public static boolean isRankedOrNot;

    //constructors
    public Ranking(HashMap<Tutor, ArrayList<Double> > subjectUnderstandingRatings, HashMap<Tutor, ArrayList<Double> > explanationAbilityRatings, HashMap<Tutor, ArrayList<Double> > attitudeAbilityRatings, HashMap<Tutor, ArrayList<Double> > progressRatings)
    {
        Ranking.subjectUnderstandingRatings = subjectUnderstandingRatings;
        Ranking.explanationAbilityRatings = explanationAbilityRatings;
        Ranking.attitudeAbilityRatings = attitudeAbilityRatings;
        Ranking.progressRatings = progressRatings;
    }
    public Ranking() {

    }
    // calculates the average of the data, placing more emphasis on newer data to signify the progress of the tutor/tutee
    private static double movingAverage(double subjectRating, double newRating) { // https://stackoverflow.com/questions/9200874/implementing-exponential-moving-average-in-java
        double alpha = 0.1, oneMinusAlpha = 0.9; // alpha - indicates the weight of the newest piece of data, with older pieces of data getting multiplied in a geometric sequence with the ratio oneMinusAlpha
        if (subjectRating <=0) {
            subjectRating = newRating;
        }
        else {
            subjectRating = (alpha*newRating) + (oneMinusAlpha*subjectRating);
        }
        return subjectRating;
    }
    //calculates the overall rating of a certain attribute using the list of doubles provided
    public static HashMap<Tutor, Double> calculateOverallRating (HashMap<Tutor, ArrayList<Double>> ratings)
    {
        HashMap<Tutor, Double> overallRatings = new HashMap<>();
        double overallRating = 0;
        for (Tutor tutor : TutorController.tutors) { // https://www.w3schools.com/java/java_hashmap.asp - iterates through a hashmap's keys
            ArrayList<Double> ratingsList = ratings.get(tutor);
            for (Double rating : ratingsList) { //iterates through the arraylist
                overallRating = movingAverage(overallRating, rating); // calls moving average method for each new piece of data
            }
            overallRatings.put(tutor, overallRating);
        }
        return overallRatings;
    }

    // calculates the sigmoid function, a commonly used activation function in machine learning
    private static double sigmoid(double number) {
        return 1/(1+(Math.exp(-number)));
    }

    //initializes the weights (relative importance of a value when multiplied with) as zeroes
    private static double[][] initializeWeights () {
        double[][] weights = new double[1][4]; //4 since there are 4 attributes of feedback to be considered
        for (int i = 0; i < 4; i++) {
            weights[0][i] = 0.0;
        }
        return weights;
    }
    //uses dot product from matrix multiplication to calculate the activations, which are essentially predictions
    private static double[][] calculateActivationUsingDotProduct(double[][] parameters, double [][] weights, double bias) {
        double [][] activations = new double[weights.length][parameters[0].length];
        for (int j = 0; j < weights[0].length; j++) {
            for (int k = 0; k < activations[0].length; k++) {
                activations[0][j] += (weights[0][j]*parameters[j][k]);
            }
            activations[0][j] = sigmoid(activations[0][j]+bias);
        }
        return activations;
    }
    // calculates the cost function, a mathematical function that returns the "error" in the prediction relative to the actual output
    private static double calculateCost(double[][] activations, double[][] tutorRatings) {
        double cost = 0;
        for (int i = 0; i < activations.length; i++) {
            for (int j = 0; j < tutorRatings[0].length; j++) {
                cost -= ((Math.log(activations[i][j])) * (tutorRatings[i][j]) + (Math.log(1 - activations[i][j])) * (1 - tutorRatings[i][j]))/activations.length;
            }
        }
        System.out.println("cost " + cost);
        return cost;
    }

    //calculates the gradients of the weights and biases using the cost function, which will be used to update weights and biases
    private static ArrayList<Object> calculateGradients (double [][] parameters, double[][] activations) {
        double [][] dw = new double[activations.length][parameters[0].length];
        double db = 0;
        for (int j = 0; j < activations.length; j++) {
            dw[0][j] = (parameters[0][j] * (activations[0][j] - tutorOverallRatings[0][j]))/ activations.length;
            db = (activations[0][j] - tutorOverallRatings[0][j])/activations[0].length;
        }
        ArrayList<Object> gradients = new ArrayList<Object>();
        gradients.add(dw);
        gradients.add(db);
        return gradients;
    }

    // brings together the attributes of tutor rankings HashMaps into one 2D array to be used in dot product and matrix multiplication calculations
    public static double[][] compileInputs() {
        // makes sure the hashmap sizes are the same in order to put them into a static-size 2D array
        assert (subjectUnderstandingRatings.size() == explanationAbilityRatings.size()) && (attitudeAbilityRatings.size() == explanationAbilityRatings.size()) && (attitudeAbilityRatings.size() == progressRatings.size());
        //4 indicates the number of ranking attributes
        double[][] parameters = new double[4][TutorController.tutors.size()];
        //calculates the overall rating of the list of doubles and instantiates a new HashMap with key: Tutor, Double: overall attribute rating
        HashMap <Tutor, Double> overallSubjectUnderstanding = calculateOverallRating(subjectUnderstandingRatings);
        HashMap <Tutor, Double> overallExplanationAbility = calculateOverallRating(explanationAbilityRatings);
        HashMap <Tutor, Double> overallTutoringAttitude = calculateOverallRating(attitudeAbilityRatings);
        HashMap <Tutor, Double> overallTuteeProgress = calculateOverallRating(progressRatings);
        //creates a list of Maps with all the ratings to add to
        List<Map<Tutor, Double>> listOfMaps = new ArrayList<>();
        Map<Tutor, List<Double>> tutorRatingsMap = new HashMap<>();
        listOfMaps.add(overallSubjectUnderstanding);
        listOfMaps.add(overallExplanationAbility);
        listOfMaps.add(overallTutoringAttitude);
        listOfMaps.add(overallTuteeProgress);
        //puts the list of maps data into a 2D array
        int counter = 0;
        for (double[] oneDimensionalArray : parameters) {
            int internalCounter = 0;
            for (Tutor key: listOfMaps.get(counter).keySet()) {
                oneDimensionalArray[internalCounter] = listOfMaps.get(counter).get(key);
                //copies the elements
                internalCounter++;
            }
            //put current array into 2d array
            counter++;
        }
        return parameters;
    }

    //updates weights using the gradient of the weights calculated
    private static double[][] updateWeights(double[][] weights, double[][] dw, double learning_rate) {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights.length; j++) {
                weights[i][j] -= (dw[i][j]*learning_rate);
            }
        }
        return weights;
    }

    // brings together all the methods to forward and backward propagate, optimizing the weights and biases
    //num_iterations: number of times the network will iterate through updating the network
    //learning_rate: arbitrarily chosen very small double (<0.1) that is used to reduce the flucutations in the training process and make sure it trains accurately over many iterations
    public static ArrayList<Object> optimize(int num_iterations, double learning_rate) {
        double[][] weights = initializeWeights();
        double bias = 0.0;
        double [][] parameters = compileInputs();
        // 2D array that are the predictions of the network
        double[][] activations;
        double cost = 0;
        ArrayList<Object> gradients = new ArrayList<>();
        for (int i = 0; i < num_iterations; i++) {
            activations = calculateActivationUsingDotProduct(parameters, weights, bias);
            cost = calculateCost(activations, tutorOverallRatings);
            gradients = calculateGradients(parameters, activations);
            weights = updateWeights(weights, (double[][]) (gradients.get(0)), learning_rate);
            // updating bias
            bias -= (double) gradients.get(1);
        }
        //adding all variables to data list for future access
        ArrayList<Object> data = new ArrayList<>();
        data.add(cost);
        data.add(weights);
        data.add(bias);
        data.add(gradients);
        data.add(parameters);
        return data;
    }

    //sorts HashMap of Tutors with overall tutorRatings, creating the rankings
    public static void insertionSort(HashMap<Tutor, Double> tutorDoubleHashMap) {
        int c = 0;
        int len = tutorDoubleHashMap.size();
        ArrayList<Tutor> tutors = new ArrayList<>(tutorDoubleHashMap.keySet());
        Tutor temp;
        for(int i = 1; i < tutors.size(); i++)
        {
            Tutor tutor = tutors.get(i);
            int j = i-1;
            temp = tutors.get(j);
            while(j >= 0  && tutorDoubleHashMap.get(temp) - (tutorDoubleHashMap.get(tutor)) < 0)
            {
                c++;
                tutorDoubleHashMap.put(tutors.get(j+1), tutorDoubleHashMap.get(temp));
                j--;
            }
            tutorDoubleHashMap.put(tutors.get(j+1), tutorDoubleHashMap.get(temp));
        }
    }

    // method to calculate and return tutor rankings after network training
    public static HashMap<Tutor, Double> rank(ArrayList<Object> data) {
        double [][] array = calculateActivationUsingDotProduct((double[][]) data.get(1), (double[][]) data.get(4), (double) data.get(2));
        HashMap<Tutor, Double> tutorDoubleHashMap = new HashMap<>();
        for(int i = 0; i < array[0].length; i++) {
            for (Tutor tutor: subjectUnderstandingRatings.keySet()) {
                tutorDoubleHashMap.put(tutor, array[0][i]);
            }
        }
        insertionSort(tutorDoubleHashMap);
        return tutorDoubleHashMap;
    }
    public static void main(String[] args) throws Exception {
    }

}
