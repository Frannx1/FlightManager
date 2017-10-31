
package Model.FileTools;

import Model.Graph.AirportGraph.AirportManager;
import Model.Graph.AirportGraph.Structures.Flight;
import Model.Graph.AirportGraph.Structures.Day;
import Model.Graph.GraphStructures.Node;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class FileManager {

    private String path;
    private AirportManager airport;

    public FileManager(String path, AirportManager airport){
        if(path == null) throw new IllegalArgumentException("Null path.");
        this.path = path;
        this.airport = airport;
    }

    public void deleteOldAirportFile(String outputFileAirports) {
        File airportOldFile = new File(path,outputFileAirports);
        if(airportOldFile.exists()) {
            airportOldFile.delete();
        }
    }

    public void deleteOldFlightFile(String outputFileFlights) {
        File flightOldFile = new File(path,outputFileFlights);
        if(flightOldFile.exists()) {
            flightOldFile.delete();
        }
    }

    public void saveFile(String outputFileAirports, String outputFileFlights) throws IOException {
        String newLine = System.getProperty("line.separator");
        //AirportManager airport = AirportManager.getInstance();

        if(this.airport.getAirportsDijkstra().isEmpty()) {
            System.out.println("NotFound");
        } else {
                File airportFile = new File(path,outputFileAirports);
                FileWriter AirportWriter = new FileWriter(airportFile, true);

                for(Node air : airport.getAirportsDijkstra()){
                    AirportWriter.write(air.airport.getName() + "#" + air.airport.getLatitude() + "#" + air.airport.getLongitude() + newLine);
                }
                AirportWriter.close();
                if(!airport.getFlights().values().isEmpty()) {
                    File flightFile = new File(path,outputFileFlights);
                    FileWriter flightWriter = new FileWriter(flightFile, true);
                    airport = AirportManager.getInstance();
                    for(Flight fl : airport.getFlights().values()){
                        flightWriter.write(fl.getAirline() + "#" + fl.getFlightNumber() + "#" + Day.getDays(fl.getDays()) + "#" + fl.getOrigin() + "#" + fl.getTarget() + "#" + getDepartureTimeFormat(fl.getDepartureTime()) + "#" + getFlightTimeFormat(fl.getFlightTime()) + "#" + fl.getPrice() + newLine);
                    }
                    flightWriter.close();
                } else {
                    System.out.println("NotFound");
                }

        }

        return;
    }

    private String getDepartureTimeFormat(int t) {
        if (t < 0) throw new IllegalArgumentException();
        Integer hours = t/60;
        Integer minutes = t%60;
        String s = new String();

        if(hours < 10) {
            s = s.concat("0" + hours.toString() + ":");
        } else {
            s = s.concat(hours.toString() + ":");
        }
        if(minutes < 10) {
            s = s.concat("0" + minutes.toString());
        } else {
            s = s.concat(minutes.toString());
        }
        return s;
    }

    private String getFlightTimeFormat(int t) {
        Integer hours = t/60;
        Integer minutes = t%60;
        String s = new String();

        if(hours > 0) {
            s = s.concat(hours.toString() + "h");
        }
        if(minutes < 10) {
            s = s.concat("0" + minutes.toString() + "m");
        } else {
            s = s.concat(minutes.toString() + "m");
        }
        return s;
    }

    public void load(String airportFile, String flightFile) throws ClassNotFoundException, IOException {
        readAirports(airportFile);
        readFlights(flightFile);
    }

    public boolean writeRoute(List<Flight> route, String output, FileFormat outputFormat){
        if(route == null) return false;
        String newLine = System.getProperty("line.separator");
        double price = 0.0;
        int flightTime = 0;
        int totalTime = 0;
        int initialTime = route.get(0).getDepartureTime()+route.get(0).getCurrentDayIndex()*(60*24);
        int arrivalTime = initialTime;
        for(Flight fl: route){
            int aux ;
            if(fl.getDepartureTime()+fl.getCurrentDayIndex()*(60*24) >= (arrivalTime%(60*7*24))){
                aux = Math.abs(fl.getDepartureTime()+fl.getCurrentDayIndex()*(60*24)-(arrivalTime%(60*7*24)));
            }else
                aux = (7*60*24) - Math.abs(fl.getDepartureTime()+fl.getCurrentDayIndex()*(60*24)-(arrivalTime%(60*7*24)));
            arrivalTime += fl.getFlightTime()+aux;
            price += fl.getPrice();
            flightTime += fl.getFlightTime();
        }
        totalTime = arrivalTime-initialTime;
        int totalHours = totalTime/60;
        int totalMins = totalTime%60;

        int hoursFlight = flightTime/60;
        int minutesFlight = flightTime%60;

        if(outputFormat.equals(FileFormat.TEXT)) {
            if(output.equals("stdout")) {
                System.out.println("Precio#" + price);
                System.out.println("TiempoVuelo#" + hoursFlight + "h" + minutesFlight + "m");
                System.out.println("TiempoTotal#"+ totalHours+ "h"+totalMins+"m");
                for(Flight fl: route){
                    System.out.println(fl.getOrigin() + "#" + fl.getAirline() + "#" + fl.getFlightNumber()+ "#" + fl.getTarget());
                }
            } else {
                try {
                    File toWrite = new File(path,output);
                    if(toWrite.exists()){
                        toWrite.delete();
                    }
                    FileWriter writer = new FileWriter(toWrite, true);
                    writer.write("Precio#" + price + newLine);
                    writer.write("TiempoVuelo#" + hoursFlight + "h" + minutesFlight + "m" + newLine);
                    writer.write("TiempoTotal#"+ totalHours+ "h"+totalMins+"m"+newLine);
                    for(Flight fl: route){
                        writer.write(fl.getOrigin() + "#" + fl.getAirline() + "#" + fl.getFlightNumber() + "#" + fl.getTarget() + newLine);
                    }
                    writer.close();
                    System.out.println("Se ha cargado el resultado en el archivo: "+output);
                } catch (IOException e) {
                    System.out.println("NotFound");
                    return false;
                }
            }
        } else if(outputFormat.equals(FileFormat.KML)){
            if(output.equals("stdout")) {
                System.out.println("<?xml version=" + "\"1.0\"" + " encoding=" + "\"UTF-8\"" + "?>");
                System.out.println("<kml xmlns=" + "\"http://www.opengis.net/kml/2.2\"" + ">");
                System.out.println("<Document>");

                System.out.println("<description>" + "Precio: " + price + " TiempoVuelo: " + hoursFlight + "h" + minutesFlight + "m" + " TiempoTotal: " + totalHours + "h" + totalMins + "m" + "</description>");

                System.out.println("<Placemark>");
                System.out.println("<name>" + airport.getAirports().get(route.get(0).getOrigin()).airport.getName() + "</name>");
                System.out.println("<Point>");
                System.out.println("<Description></Description>");
                System.out.println("<coordinates>" + airport.getAirports().get(route.get(0).getOrigin()).airport.getLatitude() + ", " + AirportManager.getInstance().getAirports().get(route.get(0).getOrigin()).airport.getLongitude() + ",0" + "</coordinates>");
                System.out.println("</Point>");
                System.out.println("</Placemark>");
                for(Flight fl : route) {

                    System.out.println("<Placemark>");
                    System.out.println("<name>" + fl.getAirline() + "#" + fl.getFlightNumber() + "</name>");
                    System.out.println("<LineString>");
                    System.out.println("<tessellate>0</tessellate>");
                    System.out.println("<coordinates>" + airport.getAirports().get(route.get(0).getOrigin()).airport.getLatitude() + ", " + airport.getAirports().get(route.get(0).getOrigin()).airport.getLongitude() + ",0");
                    System.out.println(airport.getAirports().get(fl.getTarget()).airport.getLatitude() + ", " + airport.getAirports().get(fl.getTarget()).airport.getLongitude() + ",0" + "</coordinates>");
                    System.out.println("</LineString>");
                    System.out.println("</Placemark>");

                    System.out.println("<Placemark>");
                    System.out.println("<name>" + fl.getTarget() + "</name>");
                    System.out.println("<Point>");
                    System.out.println("<Description></Description>");
                    System.out.println("<coordinates>" + airport.getAirports().get(fl.getTarget()).airport.getLatitude() + ", " + airport.getAirports().get(fl.getTarget()).airport.getLongitude() + ",0" + "</coordinates>");
                    System.out.println("</Point>");
                    System.out.println("</Placemark>");
                }
                System.out.println("</Document>");
                System.out.println("</kml>");
            } else {
                try {
                    File toWrite = new File(path,output);
                    if(toWrite.exists()){
                        toWrite.delete();
                    }
                    FileWriter writer = new FileWriter(toWrite, true);
                    writer.write("<?xml version=" + "\"1.0\"" + " encoding=" + "\"UTF-8\"" + "?>" + newLine);
                    writer.write("<kml xmlns=" + "\"http://www.opengis.net/kml/2.2\"" + ">" + newLine);
                    writer.write("<Document>" + newLine);

                    writer.write("<description>" + "Precio: " + price + " TiempoVuelo: " + hoursFlight + "h" + minutesFlight + "m" + " TiempoTotal: " + totalHours + "h" + totalMins + "m" + "</description>" + newLine);

                    writer.write("<Placemark>" + newLine);
                    writer.write("<name>" + airport.getAirports().get(route.get(0).getOrigin()).airport.getName() + "</name>" + newLine);
                    writer.write("<Point>" + newLine);
                    writer.write("<Description></Description>" + newLine);
                    writer.write("<coordinates>" + airport.getAirports().get(route.get(0).getOrigin()).airport.getLatitude() + ", " + AirportManager.getInstance().getAirports().get(route.get(0).getOrigin()).airport.getLongitude() + ",0" + "</coordinates>" + newLine);
                    writer.write("</Point>" + newLine);
                    writer.write("</Placemark>" + newLine);
                    for(Flight fl : route) {

                        writer.write("<Placemark>" + newLine);
                        writer.write("<name>" + fl.getAirline() + "#" + fl.getFlightNumber() + "</name>" + newLine);
                        writer.write("<LineString>" + newLine);
                        writer.write("<tessellate>0</tessellate>" + newLine);
                        writer.write("<coordinates>" +  airport.getAirports().get(fl.getOrigin()).airport.getLatitude() + ", " + airport.getAirports().get(fl.getOrigin()).airport.getLongitude() + ",0" + newLine);
                        writer.write(airport.getAirports().get(fl.getTarget()).airport.getLatitude() + ", " + airport.getAirports().get(fl.getTarget()).airport.getLongitude() + ",0" + "</coordinates>" + newLine);
                        writer.write("</LineString>" + newLine);
                        writer.write("</Placemark>" + newLine);

                        writer.write("<Placemark>" + newLine);
                        writer.write("<name>" + fl.getTarget() + "</name>" + newLine);
                        writer.write("<Point>" + newLine);
                        writer.write("<Description></Description>" + newLine);
                        writer.write("<coordinates>" + airport.getAirports().get(fl.getTarget()).airport.getLatitude() + ", " + airport.getAirports().get(fl.getTarget()).airport.getLongitude() + ",0" + "</coordinates>" + newLine);
                        writer.write("</Point>" + newLine);
                        writer.write("</Placemark>" + newLine);
                    }
                    writer.write("</Document>" + newLine);
                    writer.write("</kml>" + newLine);
                    writer.close();
                    System.out.println("Se ha cargado el resultado en el archivo: "+output);
                } catch (IOException e) {
                    System.out.println("NotFound");
                    return false;
                }
            }
        } else {
            System.out.println("NotFound");
            return false;
        }

        return true;
    }

    public  void readFlights(String file) throws FileNotFoundException {
        File toRead = new File(path,file);
        try {
            int i = 1;
            Scanner sc = new Scanner(toRead);
            while(sc.hasNextLine()){
                String s = sc.nextLine();
                String format = "[a-z A-Z]{1,3}#[0-9]{1,7}#(Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))*#[a-z A-Z]{1,3}#[a-z A-Z]{1,3}#([0-1][0-9]|2[0-3]):[0-5][0-9]#([1-9]h|[1-9][0-9]h)?([0-9]|[0-5][0-9])m#[0-9]+\\.[0-9]+$";
                if(!Pattern.matches(format, s)){
                    System.out.println("formato no valido en la linea "+i);
                } else {
                    String[] res =  s.split("#");
                    String [] days = new String[] {res[2]};
                    airport.addFlight(res[0], res[1], days, res[3], res[4], new Integer(res[5]), new Integer(res[6]), new Double(res[7]));
                }
                i++;

            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.out.println("NotFound");
        }

    }

    public  void readAirports(String name) throws IOException, ClassNotFoundException{
        File toRead = new File(path,name);
        try {
            Scanner sc = new Scanner(toRead);
            while(sc.hasNextLine()){
                String s = sc.nextLine();
                String format = "[a-z A-Z]{3}#-?[0-9]+\\.[0-9]+#-?[0-9]+\\.[0-9]+$";
                if(!Pattern.matches(format, s)){
                    System.out.println("formato no valido");
                } else {
                    String[] res = s.split("#");
                    airport.addAirport(res[0], new Double(res[1]), new Double(res[2]));
                }
            }
            sc.close();
        } catch (IOException e) {
            System.out.println("NotFound");
        }
    }
}
