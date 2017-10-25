package Model;
import Model.FileTools.FileFormat;
import Model.FileTools.FileManager;
import Model.Graph.AirportGraph.AirportManager;
import Model.Graph.AirportGraph.FlightPriority;
import Model.Graph.AirportGraph.Structures.Day;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class Parser {
    private FileFormat fileFormat;
    private String output;
    private AirportManager airportManager;
    private String airportFile;
    private String flightFile;
    private FileManager fileManager;

   public Parser(AirportManager manager, String airportFile, String flightFile, FileManager fileManager)    {
       if(manager == null || airportFile == null || flightFile == null) throw  new IllegalArgumentException();
       this.airportFile = airportFile;
       this.flightFile = flightFile;
       this.airportManager = manager;
       this.fileManager = fileManager;
       this.fileFormat = null;
       this.output = null;
   }

    public boolean parseConsole(String command) throws ClassNotFoundException, IOException {

        String CONSOLE_MESSAGE = 	"******************* List of Commands: ******************* \n" +
                "#insert airport [name] [lat] [lng]\n" +
                "#insert all airport [FILE]\n" +
                "#delete airport [name]\n" +
                "#delete all airport\n" +
                "#insert flight [airline] [flightNumber] [weekDays] [origin] [target] [departureTime] [duration] [price]\n" +
                "#insert all flight [FILE]\n" +
                "#delete flight [airline] [flightNumber]\n" +
                "#delete all flight\n" +
                "#findRoute src=[origin] dst=[target] priority={ft|pr|tt} *weekdays=[weekDays]\n" +
                "#fileFormat {text|KML}\n" +
                "#output {stdout|file [fileName]}\n" +
                "#load\n" +
                "#exit&save\n" +
                "#quit";
        String helpExpReg = "[hH]";
        String addAirportExpReg = "insert airport [a-z A-Z]{1,3} -?[0-9]+\\.[0-9]+ -?[0-9]+\\.[0-9]+";
        String delAirportExpReg = "delete airport [a-z A-Z]{1,3}";
        String addAllAirportExpReg = "insert all airport [a-z A-Z 0-9]+\\.txt";
        String delAllAirportExpReg = "delete all airport";
        String addFlightExpReg = "insert flight [a-z A-Z]{1,3} [0-9]{1,7} (Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))* [a-z A-Z]{1,3} [a-z A-Z]{1,3} ([0-1][0-9]|2[0-3]):[0-5][0-9] ([1-9]h|[1-9][0-9]h)?[0-5][0-9]m [0-9]+\\.[0-9]+$";
        String delFlightExpReg = "delete flight [a-z A-Z]{1,3} [1-9][0-9]*";
        String addAllFlightExpReg = "insert all flight [a-z A-Z 0-9]+\\.txt";
        String delAllFlightExpReg = "delete all flight";
        String findRouteExpReg = "findRoute src=[a-z A-Z]{3} dst=[a-z A-Z]{3} priority=(pr|tt|ft)( weekdays=(Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))*)?$";
        String fileFormatExpReg = "fileFormat ((text)|(KML))";
        String outputExpReg = "output stdout";
        String outputFileExpReg = "output file [a-z A-Z 0-9]+(\\.txt|\\.kml)";
        String exitSaveExpReg = "exit&save";
        String loadExpReg = "load";
        String quitExpReg = "quit";

        if(Pattern.matches(helpExpReg, command)) {
            System.out.println(CONSOLE_MESSAGE);
            return false;
        }


        if(Pattern.matches(addAirportExpReg,command)){
            String[] res = command.split(" ");
            this.airportManager.addAirport(res[2],new Double(res[3]),new Double(res[4]));
            return false;
        }

        else if(Pattern.matches(addAllAirportExpReg,command)){
            String[] res = command.split(" ");
            fileManager.readAirports(res[3]);
            return false;
        }

        else if(Pattern.matches(delAirportExpReg,command)){
            airportManager.deleteAirport(command);
            return false;
        }

        else if(Pattern.matches(delAllAirportExpReg, command)){
            airportManager.deleteAirports();
            return false;
        }

        else if(Pattern.matches(addFlightExpReg, command)){
            String[] res = command.split(" ");
            String[] days = res[4].split("-");
            if(!Day.checkDays(days)){
                System.out.println("Repeated days.");
                return false;
            }
            airportManager.addFlight(res[2], res[3], days, res[5], res[6],
                    new Integer(res[7]), new Integer(res[8]), new Double(res[9]));
            return false;
        }

        else if(Pattern.matches(delFlightExpReg, command)){
            String[] args = command.split("-");
            if(args.length > 2){
                 System.out.println("Wrong parameters.");
            }    return false;
            airportManager.deleteFlight(args[0], args[1]);
            return false;
        }

        else if(Pattern.matches(addAllFlightExpReg, command)){
            String[] res = command.split(" ");
            fileManager.readFlights(res[3]);
            return false;
        }

        else if(Pattern.matches(delAllFlightExpReg, command)){
            airportManager.deleteFlights();
            return false;
        }

        else if(Pattern.matches(findRouteExpReg, command)){
            String[] res = command.split(" ");
            String source = res[1].split("=")[1];
            String target = res[2].split("=")[1];
            String p = res[3].split("=")[1];

           FlightPriority priority;
            if(output==null || fileFormat == null){
                if(output !=null)
                    System.out.println("You must set the format of the output.");
                else
                    System.out.println("You must specified the path for the output file.");
                return false;
            }
            if(p.equals("ft"))
                priority = FlightPriority.TIME;
            else if (p.equals("pr"))
                priority = FlightPriority.PRICE;
            else
                priority = FlightPriority.TOTAL_TIME;

            if(res.length == 5){
                String[] days = res[4].split("=");
                days = days[1].split("-");
                if(!Day.checkDays(days)){
                    System.out.println("Repeated days.");
                    return false;
                }
                List<Day> newDays = Day.getDays(days);
                airportManager.findRoute(source,target,priority,newDays, fileFormat, output);
            }else
                airportManager.findRoute(source,target,priority,Day.getAllDays(), fileFormat, output);
            return false;
        }

        else if(Pattern.matches(fileFormatExpReg, command)){
            String[] strings = command.split(" ");
            if(strings[1].equals("text")) {
                fileFormat = FileFormat.TEXT;
            } else {
                fileFormat = FileFormat.KML;
            }
            return false;
        }
        else if(Pattern.matches(outputExpReg, command)){
            output = "stdout";
        }
        else if(Pattern.matches(outputFileExpReg, command)){
            String[] strings = command.split(" ");
            String[] strings2 = strings[2].split("\\.");
            String extension = strings2[1];


            if(extension.equals("kml") && fileFormat.equals(FileFormat.TEXT)) {
                System.out.println("Error: the name end with .kml but the set format is .txt.");
                return false;
            }
            if(extension.equals("txt") && fileFormat.equals(FileFormat.KML)) {
                System.out.println("Error: the name end with .txt but the set format is .kml.");
                return false;
            }
            output = strings[2];


            return false;
        }
        else if(Pattern.matches(loadExpReg, command)) {
            fileManager.load(airportFile, flightFile);
        }
        else if(Pattern.matches(exitSaveExpReg, command)){
            fileManager.deleteExistingAirportFile(airportFile);
            fileManager.deleteExistingFlightFile(flightFile);
            fileManager.save(airportFile, flightFile);
            return true;
        }
        else if(Pattern.matches(quitExpReg, command)){
            System.out.println("Quiting...");
            return true;
        }
        else
            System.out.println("Command not valid.");

        return false;
    }




    public  boolean parseArguments(String[] args) throws ClassNotFoundException, IOException {

        if(args.length == 1) {
            if(args[0].equals("--delete-airports")) {
                fileManager.load(airportFile, flightFile);
                airportManager.deleteAirports();
            }
            else if(args[0].equals("--delete-flights")) {
                fileManager.load(airportFile, flightFile);
                airportManager.deleteFlights();
            }
            else {
                return false;
            }
        }
        else if(args.length == 3) {
            if(args[0].equals("--airport-file")) {
                if(!Pattern.matches("[a-z A-Z 0-9]+\\.txt", args[1]))
                    return false;
                if(args[2].equals("--append-airports")) {
                    fileManager.load(airportFile, flightFile);
                    fileManager.readAirports(args[1]);
                }
                else if(args[2].equals("--replace-airports")) {
                    fileManager.load(airportFile, flightFile);
                    airportManager.deleteAirports();
                    fileManager.readAirports(args[1]);
                }
                else {
                    return false;
                }
            }
            else if(args[0].equals("--flight-file")) {
                if(!Pattern.matches("[a-z A-Z 0-9]+\\.txt", args[1]))
                    return false;
                if(args[2].equals("--append-flights")) {
                    fileManager.load(airportFile, flightFile);
                    fileManager.readFlights(args[1]);
                }
                else if(args[2].equals("--replace-flights")) {
                    fileManager.load(airportFile, flightFile);
                    airportManager.deleteFlights();
                    fileManager.readFlights(args[1]);
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }

        return true;

    }

}