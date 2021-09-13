//DHARITRI DIXIT: 300109815

//imports required for program
import java.io.File;
import java.io.*;
import java.util.Scanner;
import java.util.Stack;
import java.io.IOException;
import java.io.FileNotFoundException;


public class GaleShapley {

  //instance variables
  private Stack<Integer> Sue;  //stores unmatched employers
  private int[] employers, students;  //currents matches
  private HeapPriorityQueue<Integer, Integer>[] PQ;   //array of priority queues
  private int[][] A;   //stores rank of employer scored by student
  private String[] studentNames, employerNames;   //names of employers and students

  //constructor
  public GaleShapley(){
    Sue = new Stack<>();
  }

  //methods
  @SuppressWarnings("unchecked")
  public void initialize(Scanner filename){
    int n;  //number of employers or students involved in matching process

    while(filename.hasNext()){

      n = Integer.parseInt(filename.nextLine());

      employers = new int[n];
      students = new int[n];
      for(int i=0; i< n; i++){
        //populate Sue with unmatched employers
        Sue.push(i);
        //set empty values for matches in employers and students
        employers[i] = -1;
        students[i] = -1;
      }

      employerNames = new String[n];
      studentNames = new String[n];
      for(int i=0; i < n; i++){
        //store employer name in list
        employerNames[i] = filename.nextLine();
      }
      for(int i=0; i < n; i++){
        //store student name in list
        studentNames[i] = filename.nextLine();
      }


      A = new int[n][n];
      PQ = new HeapPriorityQueue[n];
      for(int e=0; e < n; e++){
        String row = filename.nextLine();
        String[] coordinates = row.split(" ");   //store all coordinates in row of matrix as an element of array

        PQ[e] = new HeapPriorityQueue(n);   //instance of priority queue at element i in array of priority queues


        for(int s=0; s < n; s++){
          String[] parsedCoord = coordinates[s].split(",");   //store each value of coordinate as it's own element in array

          int employerRank = Integer.parseInt(parsedCoord[0]);
          PQ[e].insert(employerRank, s);

          A[s][e] = Integer.parseInt(parsedCoord[1]);
        }
      }
    }

  }

  public String[] execute(){

    while(!Sue.isEmpty()){
      int e = Sue.pop();
      Entry<Integer, Integer> prefStudent = PQ[e].removeMin();  //store most preferred student of employer
      int s = prefStudent.getValue();
      int ePrime = students[s];

      if(students[s] == -1){
        students[s] = e;
        employers[e] = s;
      } else if(A[s][e] < A[s][ePrime]){
        students[s]= e;
        employers[e]= s;
        employers[ePrime]= -1;
        Sue.push(ePrime);
      } else{
        Sue.push(e);
      }

    }


    //returning matched set
    String[] matched = new String[employers.length];
    for(int i=0; i < employers.length; i++){
      if(i == students[employers[i]]){
        matched[i] = "Match "+ i + ": " + employerNames[i] + " - " + studentNames[employers[i]];
      }
    }
    return matched;

  }

  public void save(String filename, String[] match) throws IOException {
    PrintWriter writer = new PrintWriter(filename); //create new file
    for(int i=0; i < employers.length; i++ ){
      writer.println(match[i]);
    }
    writer.close();
    System.out.println("\n" + "Check your directory for the output file! It will be named:" + filename);
  }

  public static void main(String[] args) throws FileNotFoundException{

    GaleShapley coopMatch = new GaleShapley();  //instance of GaleShapley

    Scanner input = new Scanner(System.in); //opening user input
    String inputFileName;
    System.out.println("Enter the filename containing the data: ");
    inputFileName = input.nextLine();
    File inputFile = new File(inputFileName);
    Scanner scan = new Scanner( inputFile );

    //constructing output file name
    String outputFileName = "matches_"+inputFileName ;

    //calling methods
    try{
      coopMatch.initialize(scan);
      String[] matches = coopMatch.execute();
      coopMatch.save(outputFileName, matches);
    } catch (Exception e){
      System.out.println(e);
    }


  }

}
