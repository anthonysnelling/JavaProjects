/**
 * @author (Anthony Snelling)
 * @version (May 28,2019)
 *
 * This program determines the shortest path between two cities based on airport
 * distance. it implements Dijkstra's algorithm to find the shortest length in miles
 * on a graph
 * */

import java.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;

public class Project2Distance {

        /**Method for turning text files into a 2d array
         * @param textFile the file holding the matrix
         * @return This returns a 2d array with the files*/
        public static int[][] matrixGrab(String textFile){

            Scanner inputStream = null;

            //Variables needed for matrix
            int rows = 8;
            int columns = 8;
            int[][] inputMatrix = new int[rows][columns];

            //Try and catch block to open the file and catch any file errors with opening
            try{
                inputStream = new Scanner(new FileInputStream(textFile));
            }
            catch(FileNotFoundException e){
                System.out.println("File "+ textFile +" was not found");
                System.out.println("or could not be opened.");
                System.out.println("rerun the program to try again");
                System.exit(0);
            }

            System.out.println("\nOpening file...");
            //putting data from file into the array
            while(inputStream.hasNextLine()) {
                for (int i=0; i<inputMatrix.length; i++) {
                    String[] line = inputStream.nextLine().trim().split(" ");
                    for (int j=0; j<line.length; j++) {
                        inputMatrix[i][j] = Integer.parseInt(line[j]);
                    }
                }
            }

            inputStream.close();
            System.out.println("closing file.");
            return inputMatrix;
        }

    /**Method for grabbing the airport codes
     * @param textFile the file holding the matrix
     * @return This returns a 2d array with the files*/
    public static LinkedList airportGrab(String textFile){

        Scanner inputStream = null;

        LinkedList airportCodes = new LinkedList();

        //Try and catch block to open the file and catch any file errors with opening
        try{
            inputStream = new Scanner(new FileInputStream(textFile));
        }
        catch(FileNotFoundException e){
            System.out.println("File "+ textFile +" was not found");
            System.out.println("or could not be opened.");
            System.out.println("rerun the program to try again");
            System.exit(0);
        }

        System.out.println("\nOpening file...");
        //putting data from file into the array
        while(inputStream.hasNext()) {
            airportCodes.add(inputStream.next());
        }
//        System.out.println(Arrays.deepToString(inputMatrix));

        inputStream.close();
        System.out.println("closing file.");
        return airportCodes;
    }

        /**A simple method to display a matrix for testing purposes
         * @param arr arr to be displayed
         * returns printout of your array
         * */
        public static void display(int[][] arr){

            for(int i = 0;i<arr.length; i++){
                for (int j = 0; j<arr.length; j++) {
                    System.out.print(arr[i][j] + " ");
                    if (j==arr.length-1) {
                        System.out.println();
                    }
                }
            }
        }

    /**A simple method to display a matrix for testing purposes
     * @param list list to be displayed
     * returns printout of your list
     * */
    public static void display(LinkedList list){
            System.out.println(list);
    }

   /**Method for Calculating the shortest path
     * it prints out the path and the distance it  would take to travel the shortest path*/
    public static void dijkstraShortestPath(){
        // for setting max value
        int MAX_VAL = Integer.MAX_VALUE;

        Scanner keyboardInput = new Scanner(System.in);

        //File name input
        System.out.println("Type in the name of the file you wish to open, be exact and include ' .txt '" +
                ", No need for \"'s (quotes) in your input \n");

        //gathering info for weighted matrix
        System.out.println("Type in the name for the file containing the weighted matrix");
        String matrix = keyboardInput.next();
        int[][] weightedMatrix = matrixGrab(matrix);

        //gathering airport codes
        System.out.println("Type in the name for the file containing airport codes");
        String codes = keyboardInput.next();
        LinkedList listOfAirports = airportGrab(codes);

        //display options
        System.out.println();
        display(listOfAirports);

        //Extracting data for calculations
        //take in inputs for Starting airport...
        System.out.println("\nWhat airport do you want to start at? An incorrect input will cause an error.(Case sensitive)");
        String start= keyboardInput.next();

        //input for ending airport.
        System.out.println("What airport do you want to end at? An incorrect input will cause an error.(Case sensitive)");
        String end= keyboardInput.next();

        //gather start and end vertices
        int startV= listOfAirports.indexOf(start);
        int endV = listOfAirports.indexOf(end);


        /**==========Beginning of Algo=============*/

        //matrix for carrying distance traveled and a matrix for previous distance
        int[] distTraveledMatrix = new int[weightedMatrix.length];
        int[] previous = new int[weightedMatrix.length];
        int[] visited= new int[weightedMatrix.length];
        int min;
        int nextNode=0;

        //set all values to 0
        for (int i=0; i<distTraveledMatrix.length; i++){
                previous[i]=0;
                visited[i]=0;
        }
        distTraveledMatrix= weightedMatrix[startV];

        //set source to 0
        distTraveledMatrix[startV]=0;

        visited[startV]=1;

        //setup for the algorithm
       for (int i = 0; i<weightedMatrix.length;i++){
           min = MAX_VAL;
           for (int j = 0; j < weightedMatrix.length; j++){
               if (min > distTraveledMatrix[j] && visited[j] != 1){
                   min = distTraveledMatrix[j];
                   nextNode=j;
               }
           }
           visited[nextNode] = 1;

           //test paths
           for (int j = 0; j< weightedMatrix.length; j++){
               if (visited[j] != 1){
                   if (min + weightedMatrix[nextNode][j] < distTraveledMatrix[j]){
                       distTraveledMatrix[j] = min + weightedMatrix[nextNode][j];
                       previous[j] = nextNode;
                   }
               }
           }
       }

       //variables for holding the shortest path route
        int[] pathArr = new int[weightedMatrix.length];
        int incrementer = 0;
        pathArr[incrementer]=startV;

//       //helper to print the path
        for (int i = endV; i< weightedMatrix.length; i++){
            int j;
            j=i;
            pathArr[incrementer]=j;
            do {
                j= previous[j];
                incrementer++;
                pathArr[incrementer]=j;
            }while (j!=0 && j!=endV);
            System.out.println();
            break;
        }

        //output final data
        System.out.println("start: "+ listOfAirports.get(startV));
        System.out.println("end: "+ listOfAirports.get(endV));
        System.out.println("The shortest distance between "+ listOfAirports.get(startV) +" and " + listOfAirports.get(endV) + " is " + distTraveledMatrix[endV] + " miles." );
        System.out.println("The shortest route between "+ listOfAirports.get(startV) +" and " + listOfAirports.get(endV) + " is ");
        System.out.print(listOfAirports.get(startV) +" -> " );
        //print the shortest path direction
        for (int i=pathArr.length-1; i>-1;i--){
            if (pathArr[i]==0){
            }else {
                System.out.print(listOfAirports.get(pathArr[i]));
                if (i != 0) {
                    System.out.print(" -> ");
                }
                if (0 == endV) {
                    System.out.print("HNL");
                }
            }
        }
    }

    public static void main(String[] args) {

            int MAX_VAL = Integer.MAX_VALUE;
            Scanner keyboardInput = new Scanner(System.in);

            System.out.println("This program takes in a 8 x 8 weighted matrix .txt corresponding to distance and the airport codes " +
                    "and determines the shortest distance and shortest path from one airport to another\n");

            //use Dijkstra’s algorithm to find the shortest distance between two points
             dijkstraShortestPath();

        //variable for breaking the loop
        boolean choiceLoopTrack = true;

        //Switch method for restarting with another file or quitting.
        while(choiceLoopTrack) {
            System.out.println("\n\ntype '1' to try another matrix or path, any other key to quit");

            try {
                int choice = keyboardInput.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println(("you chose to continue! \n"));
                        dijkstraShortestPath();
                        break;
                    default:
                        System.out.println("You chose to quit, have a nice day!");
                        choiceLoopTrack = false;
                        break;
                }
            }catch (InputMismatchException e){
                System.out.println("You chose to quit, have a nice day!");
                System.exit(0);
            }
        }
    }
}

/** ==================== SAMPLE RUNS =====================

        This program takes in a 8 x 8 weighted matrix .txt corresponding to distance and the airport codes and determines the shortest distance and shortest path from one airport to another

        Type in the name of the file you wish to open, be exact and include ' .txt ', No need for "'s (quotes) in your input

        Type in the name for the file containing the weighted matrix
        distance_matrix.txt

        Opening file...
        closing file.
        Type in the name for the file containing airport codes
        airport_codes.txt

        Opening file...
        closing file.

        [HNL, LAX, DFW, MIA, SFO, ORD, LGA, PVD]

        What airport do you want to start at? An incorrect input will cause an error.(Case sensitive)
        HNL
        What airport do you want to end at? An incorrect input will cause an error.(Case sensitive)
        PVD

        start: HNL
        end: PVD
        The shortest distance between HNL and PVD is 5147 miles.
        The shortest route between HNL and PVD is
        HNL -> LAX -> ORD -> PVD

        type '1' to try another matrix or path, any other key to quit
        1
        you chose to continue!

        Type in the name of the file you wish to open, be exact and include ' .txt ', No need for "'s (quotes) in your input

        Type in the name for the file containing the weighted matrix
        distance_matrix.txt

        Opening file...
        closing file.
        Type in the name for the file containing airport codes
        airport_codes.txt

        Opening file...
        closing file.

        [HNL, LAX, DFW, MIA, SFO, ORD, LGA, PVD]

        What airport do you want to start at? An incorrect input will cause an error.(Case sensitive)
        SFO
        What airport do you want to end at? An incorrect input will cause an error.(Case sensitive)
        MIA

        start: SFO
        end: MIA
        The shortest distance between SFO and MIA is 2690 miles.
        The shortest route between SFO and MIA is
        SFO -> LAX -> DFW -> MIA

        type '1' to try another matrix or path, any other key to quit
        1
        you chose to continue!

        Type in the name of the file you wish to open, be exact and include ' .txt ', No need for "'s (quotes) in your input

        Type in the name for the file containing the weighted matrix
        distance_matrix.txt

        Opening file...
        closing file.
        Type in the name for the file containing airport codes
        airport_codes.txt

        Opening file...
        closing file.

        [HNL, LAX, DFW, MIA, SFO, ORD, LGA, PVD]

        What airport do you want to start at? An incorrect input will cause an error.(Case sensitive)
        ORD
        What airport do you want to end at? An incorrect input will cause an error.(Case sensitive)
        MIA

        start: ORD
        end: MIA
        The shortest distance between ORD and MIA is 1922 miles.
        The shortest route between ORD and MIA is
        ORD -> DFW -> MIA

        type '1' to try another matrix or path, any other key to quit
        1
        you chose to continue!

        Type in the name of the file you wish to open, be exact and include ' .txt ', No need for "'s (quotes) in your input

        Type in the name for the file containing the weighted matrix
        distance_matrix.txt

        Opening file...
        closing file.
        Type in the name for the file containing airport codes
        airport_codes.txt

        Opening file...
        closing file.

        [HNL, LAX, DFW, MIA, SFO, ORD, LGA, PVD]

        What airport do you want to start at? An incorrect input will cause an error.(Case sensitive)
        LGA
        What airport do you want to end at? An incorrect input will cause an error.(Case sensitive)
        SFO

        start: LGA
        end: SFO
        The shortest distance between LGA and SFO is 2834 miles.
        The shortest route between LGA and SFO is
        LGA -> PVD -> ORD -> SFO

        type '1' to try another matrix or path, any other key to quit
        3
        You chose to quit, have a nice day!

        Process finished with exit code 0
*/
