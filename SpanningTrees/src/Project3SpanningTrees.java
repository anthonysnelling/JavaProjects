/**
 * @author (Anthony Snelling)
 * @version (May 28,2019)
 *
 * This program produces the spanning trees for traversal across 12 states.
 * It produces fewest number of highways to be constructed across these states
 * using both depth first and breadth first searches and a 12x12 matrix
 *
 * */

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.InputMismatchException;


public class Project3SpanningTrees {

    /**Method for turning text files into a 2d array
     * @param textFile the file holding the matrix
     * @return This returns a 2d array with the files*/
    public static int[][] matrixGrab(String textFile){

        Scanner inputStream = null;

        //Variables needed for matrix
        int rows = 12;
        int columns = 12;
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
    public static LinkedList statesGrab(String textFile){

        Scanner inputStream = null;

        LinkedList stateNames = new LinkedList();

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
            stateNames.add(inputStream.next());
        }
//        System.out.println(Arrays.deepToString(inputMatrix));

        inputStream.close();
        System.out.println("closing file.");
        return stateNames;
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

    /**A method to calculate the spanning tree using
     * a depth first algorithm
     * @param adjacencyMatrix holds the adjacency Matrix between states
     * @param listOfStates holds the name of states
     * @param rootVertex holds name of first state
     * @param rootLocation the index of the first state
     * @param visitedArray an array that holds what adjacent verts have been visited
     * @param visitOrderList list to hold the visited states for backtracking
     */
    public static void depthFirstSearch(int[][] adjacencyMatrix, LinkedList listOfStates, String rootVertex, int rootLocation, boolean[] visitedArray, LinkedList visitOrderList ){

        LinkedList tempList = new LinkedList();

        //find states adjacent to vertex
        for (int i=0; i<adjacencyMatrix.length; i++){
            if (adjacencyMatrix[rootLocation][i] == 1 && visitedArray[i]!= true) {
                tempList.add(listOfStates.get(i));
            }
        }

        int tracker=0;
        int currNextInLine=0;
        String nextState;

        /**This part finds the next state in line*/
        // find adjacent vertex in lexico order
        if (tempList.size()!=0) {
            //the adjacent vert
            while (tracker<=tempList.size()-1){
                if (tempList.get(currNextInLine).toString().compareTo(tempList.get(tracker).toString()) >=1){
                    currNextInLine=tracker;
                }
                tracker++;
            }
            nextState = tempList.get(currNextInLine).toString();
        }else {
            //finding the order to backtrack in
            LinkedList backtrackList = new LinkedList();

            for (int k=0; k<visitOrderList.size(); k++){
                backtrackList.add(listOfStates.indexOf(visitOrderList.get(k)));
            }
            int i= backtrackList.size()-1;

            while (i>0){
                //find if the previous node has an adjacent unvisited node
                /** adjacency check*/
                //find states adjacent to vertex
                for (int j= 0; j<adjacencyMatrix.length; j++){
                    if (adjacencyMatrix[(int)backtrackList.get(i)][j] == 1 && visitedArray[j]!= true) {
                        tempList.add(listOfStates.get(j));
                    }
                    if (tempList.size()==0 && j==adjacencyMatrix.length-1){
                        i--;
                        j=0;
                    }
                }
                //if you do find one make nextState equal that
                tracker=0;
                currNextInLine=0;
                while (tracker<=tempList.size()-1){
                    if (tempList.get(currNextInLine).toString().compareTo(tempList.get(tracker).toString()) >=1){
                        currNextInLine=tracker;
                    }
                    tracker++;
                }
                break;
            }
            rootVertex = (String)listOfStates.get(((int)backtrackList.get(i))) ;
            nextState = tempList.get(currNextInLine).toString();
        }

        // make that location true in visitedArray
        int markForTrue=listOfStates.indexOf(nextState);
        visitedArray[markForTrue]=true;

        //add to the visited order of states
        visitOrderList.add(nextState);

        //printing out results
        System.out.println(rootVertex + " - " + nextState);

        // recursion step
        if (visitOrderList.size()!=12) {
            depthFirstSearch(adjacencyMatrix, listOfStates, nextState, markForTrue, visitedArray, visitOrderList);
        }
    }

    /**A method to calculate the spanning tree using
     * a breadth first algorithm
     * @param adjacencyMatrix holds the adjacency Matrix between states
     * @param listOfStates holds the name of states
     * @param rootVertex holds name of first state
     * @param rootLocation the index of the first state
     * @param visitOrderList2 list to hold the visited states
     */
    public static void breadthFirstSearch(int[][] adjacencyMatrix, LinkedList listOfStates, String rootVertex, int rootLocation, LinkedList visitOrderList2){

        //Variables and structs for
        LinkedList tempList = new LinkedList();
        LinkedList sortList=new LinkedList();
        sortList.addAll(listOfStates);

        //sort the states to make comparisons easier
        sortList.sort(Comparator.naturalOrder());
        int[] swapper=new int[sortList.size()];

        //creating a key so that we can swap the adjacency matrix according to the sorted list
        for (int i =0; i< sortList.size(); i++){
            swapper[i]= sortList.indexOf(listOfStates.get(i));
        }

        // add the first root vertex
        tempList.add(rootVertex);
        //tracker to keep track of which state to visit next
        int j=1;

        // matrix to go along with the sorted states list
        int [][] sortedMatrix = new int[adjacencyMatrix.length][adjacencyMatrix.length];

        Boolean rootSwitchCheck = false;

        // sorting the matrix
        for (int i = 0; i <adjacencyMatrix.length; i++){
            for (int k = 0; k<adjacencyMatrix.length; k++){
                sortedMatrix[swapper[i]][swapper[k]]=adjacencyMatrix[i][k];
            }
            if (rootLocation == i && rootSwitchCheck!=true){
                rootLocation= swapper[i];
                rootSwitchCheck = true;
            }
        }

        /**Beginning of BFS ALGO*/
        //add the source state to the visited list
        visitOrderList2.add(rootVertex);
        //keep going till you have visited all states
        while (tempList.size() != 0){
            tempList.removeFirst();
            for (int i=0; i<adjacencyMatrix.length; i++){
                if (sortedMatrix[rootLocation][i] == 1 && !visitOrderList2.contains(sortList.get(i))){
                    visitOrderList2.add(sortList.get(i));
                    tempList.add(sortList.get(i));
                    System.out.println(sortList.get(rootLocation) + " - " + sortList.get(i));
                }
            }

            //get the next state fot the next loop
            rootLocation=sortList.indexOf(visitOrderList2.get(j));

            //update the tracker
            if (j<adjacencyMatrix.length-1) {
                j++;
            }
        }
    }

    public static void main(String[] args) {

        System.out.println("This program produces the spanning trees for traversal across 12 states.\n" +
                "It produces fewest number of highways to be constructed across these states\n" +
                "using both depth first and breadth first searches using an adjacency matrix and state name '.txt' files.\n\n");

        Scanner keyboardInput = new Scanner(System.in);

        //File name input
        System.out.println("Type in the name of the file you wish to open, be exact and include ' .txt '" +
                ", No need for \"'s (quotes) in your input \n");

        //gathering info for weighted matrix
        System.out.println("Type in the name for the file containing the adjacency matrix");
        String matrix = keyboardInput.next();
        int[][] adjacencyMatrix = matrixGrab(matrix);

        //gathering airport codes
        System.out.println("Type in the name for the file containing state names");
        String names = keyboardInput.next();
        LinkedList listOfStates = statesGrab(names);

        //display options
        System.out.println();
        display(listOfStates);

        String root;
        System.out.println("from the list, What state would you like to be the root?");
        root = keyboardInput.next();
        String root2=root;
        System.out.println();

        /**-----Depth first algorithm call------*/
        boolean[] visitedArray = new boolean[12];
        boolean[] visitedArray2 = new boolean[12];
        LinkedList visitOrderList = new LinkedList();
        LinkedList visitOrderList2 = new LinkedList();

        if (!listOfStates.contains(root)) {
            System.out.println("You incorrectly typed in a state or typed one not in the list. Run the program again!");
        } else {

            /**Taking care of the root*/
            // visit the root
            int rootLocation = listOfStates.indexOf(root);
            int rootLocation2 = listOfStates.indexOf(root);
            visitedArray[rootLocation] = true;
            visitedArray2[rootLocation2] = true;

            //add to first of visit order array
            visitOrderList.add(root);

            System.out.println("The following interstate highways need to be constructed between the twelve states in the Midwest region:\n" +
                    "Using depth-first algorithm rooted at "+ root  +":");

            /**-------Depth first algorithm call------------*/
            depthFirstSearch(adjacencyMatrix, listOfStates, root, rootLocation, visitedArray, visitOrderList);


            /**-------Breadth first algorithm call------------*/
            System.out.println("\nUsing Breadth-first algorithm rooted at "+ root  +":");
            breadthFirstSearch(adjacencyMatrix, listOfStates, root2, rootLocation2, visitOrderList2);
        }
    }
}

/**============= Sample Runs ===================================

 This program produces the spanning trees for traversal across 12 states.
 It produces fewest number of highways to be constructed across these states
 using both depth first and breadth first searches using an adjacency matrix and state name '.txt' files.


 Type in the name of the file you wish to open, be exact and include ' .txt ', No need for "'s (quotes) in your input

 Type in the name for the file containing the adjacency matrix
 adjacency_matrix.txt

 Opening file...
 closing file.
 Type in the name for the file containing state names
 states.txt

 Opening file...
 closing file.

 [NorthDakota, SouthDakota, Nebraska, Kansas, Minnesota, Iowa, Missouri, Wisconsin, Illinois, Michigan, Indiana, Ohio]
 from the list, What state would you like to be the root?
 Michigan

 The following interstate highways need to be constructed between the twelve states in the Midwest region:
 Using depth-first algorithm rooted at Michigan:
 Michigan - Indiana
 Indiana - Illinois
 Illinois - Iowa
 Iowa - Minnesota
 Minnesota - NorthDakota
 NorthDakota - SouthDakota
 SouthDakota - Nebraska
 Nebraska - Kansas
 Kansas - Missouri
 Minnesota - Wisconsin
 Indiana - Ohio

 Using Breadth-first algorithm rooted at Michigan:
 Michigan - Indiana
 Michigan - Ohio
 Michigan - Wisconsin
 Indiana - Illinois
 Wisconsin - Iowa
 Wisconsin - Minnesota
 Illinois - Missouri
 Iowa - Nebraska
 Iowa - SouthDakota
 Minnesota - NorthDakota
 Missouri - Kansas

 Process finished with exit code 0

 ===========================================================================================

 This program produces the spanning trees for traversal across 12 states.
 It produces fewest number of highways to be constructed across these states
 using both depth first and breadth first searches using an adjacency matrix and state name '.txt' files.


 Type in the name of the file you wish to open, be exact and include ' .txt ', No need for "'s (quotes) in your input

 Type in the name for the file containing the adjacency matrix
 adjacency_matrix.txt

 Opening file...
 closing file.
 Type in the name for the file containing state names
 states.txt

 Opening file...
 closing file.

 [NorthDakota, SouthDakota, Nebraska, Kansas, Minnesota, Iowa, Missouri, Wisconsin, Illinois, Michigan, Indiana, Ohio]
 from the list, What state would you like to be the root?
 NorthDakota

 The following interstate highways need to be constructed between the twelve states in the Midwest region:
 Using depth-first algorithm rooted at NorthDakota:
 NorthDakota - Minnesota
 Minnesota - Iowa
 Iowa - Illinois
 Illinois - Indiana
 Indiana - Michigan
 Michigan - Ohio
 Michigan - Wisconsin
 Illinois - Missouri
 Missouri - Kansas
 Kansas - Nebraska
 Nebraska - SouthDakota

 Using Breadth-first algorithm rooted at NorthDakota:
 NorthDakota - Minnesota
 NorthDakota - SouthDakota
 Minnesota - Iowa
 Minnesota - Wisconsin
 SouthDakota - Nebraska
 Iowa - Illinois
 Iowa - Missouri
 Wisconsin - Michigan
 Nebraska - Kansas
 Illinois - Indiana
 Michigan - Ohio

 Process finished with exit code 0

 ==========================================================================

 This program produces the spanning trees for traversal across 12 states.
 It produces fewest number of highways to be constructed across these states
 using both depth first and breadth first searches using an adjacency matrix and state name '.txt' files.


 Type in the name of the file you wish to open, be exact and include ' .txt ', No need for "'s (quotes) in your input

 Type in the name for the file containing the adjacency matrix
 adjacency_matrix.txt

 Opening file...
 closing file.
 Type in the name for the file containing state names
 states.txt

 Opening file...
 closing file.

 [NorthDakota, SouthDakota, Nebraska, Kansas, Minnesota, Iowa, Missouri, Wisconsin, Illinois, Michigan, Indiana, Ohio]
 from the list, What state would you like to be the root?
 Ohio

 The following interstate highways need to be constructed between the twelve states in the Midwest region:
 Using depth-first algorithm rooted at Ohio:
 Ohio - Indiana
 Indiana - Illinois
 Illinois - Iowa
 Iowa - Minnesota
 Minnesota - NorthDakota
 NorthDakota - SouthDakota
 SouthDakota - Nebraska
 Nebraska - Kansas
 Kansas - Missouri
 Minnesota - Wisconsin
 Wisconsin - Michigan

 Using Breadth-first algorithm rooted at Ohio:
 Ohio - Indiana
 Ohio - Michigan
 Indiana - Illinois
 Michigan - Wisconsin
 Illinois - Iowa
 Illinois - Missouri
 Wisconsin - Minnesota
 Iowa - Nebraska
 Iowa - SouthDakota
 Missouri - Kansas
 Minnesota - NorthDakota

 Process finished with exit code 0

==============================================================================

 This program produces the spanning trees for traversal across 12 states.
 It produces fewest number of highways to be constructed across these states
 using both depth first and breadth first searches using an adjacency matrix and state name '.txt' files.


 Type in the name of the file you wish to open, be exact and include ' .txt ', No need for "'s (quotes) in your input

 Type in the name for the file containing the adjacency matrix
 adjacency_matrix.txt

 Opening file...
 closing file.
 Type in the name for the file containing state names
 states.txt

 Opening file...
 closing file.

 [NorthDakota, SouthDakota, Nebraska, Kansas, Minnesota, Iowa, Missouri, Wisconsin, Illinois, Michigan, Indiana, Ohio]
 from the list, What state would you like to be the root?
 Illinois

 The following interstate highways need to be constructed between the twelve states in the Midwest region:
 Using depth-first algorithm rooted at Illinois:
 Illinois - Indiana
 Indiana - Michigan
 Michigan - Ohio
 Michigan - Wisconsin
 Wisconsin - Iowa
 Iowa - Minnesota
 Minnesota - NorthDakota
 NorthDakota - SouthDakota
 SouthDakota - Nebraska
 Nebraska - Kansas
 Kansas - Missouri

 Using Breadth-first algorithm rooted at Illinois:
 Illinois - Indiana
 Illinois - Iowa
 Illinois - Missouri
 Illinois - Wisconsin
 Indiana - Michigan
 Indiana - Ohio
 Iowa - Minnesota
 Iowa - Nebraska
 Iowa - SouthDakota
 Missouri - Kansas
 Minnesota - NorthDakota

 Process finished with exit code 0


 */