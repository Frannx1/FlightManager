package Model;

import Model.FileTools.FileManager;
import Model.Graph.AirportGraph.AirportManager;

import java.io.IOException;
import java.util.Scanner;

public class FlightManager {

    private static final String AIRPORT_FILE = "./airports.txt";
    private static final String FLIGHT_FILE = "./flights.txt";

    public void start(Parser parser) throws ClassNotFoundException, IOException {
        boolean quit = false;
        Scanner scanner = new Scanner(System.in);
        String read;
        System.out.println("Welcome to the FlightManager.");

        while(!quit) {
            System.out.println("Please, enter a command (H for command help):");
            read = scanner.nextLine();
            quit = parser.parseConsole(read);
        }
        scanner.close();
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException {

        AirportManager airportManager = new AirportManager();
        FileManager fileManager = new FileManager();
        Parser parser = new Parser(airportManager, AIRPORT_FILE, FLIGHT_FILE, fileManager)
        if(args.length != 0){
            if(parser.parseArguments(args) == false) {
                System.out.println("Invalid format of arguments.");
            }
        }
        FlightManager man = new FlightManager();
        man.start(parser);
    }
}