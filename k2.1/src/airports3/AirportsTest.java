package airports3;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

class Flight implements Comparable<Flight>{
    String from;
    String to;
    int time;
    int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    @Override
    public int compareTo(Flight o) {
        return Comparator.comparing(Flight::getTo)
                .thenComparing(Flight::getTime)
                .thenComparing(Flight::getDuration)
                .compare(this, o);    }

    @Override
    public String toString() {
        return String.format("%s %s",from,to);
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }
}

class Airport{
    String name;
    String country;
    String code;
    int passangers;
    public Map<String,TreeSet<Flight>> flights;

    public Airport(String name, String country, String code, int passangers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passangers = passangers;
        flights = new HashMap<>();
    }

    public void addFlight(String from, String to, int time, int duration) {
        flights.computeIfAbsent(to,(k)-> new TreeSet<>());
        flights.get(to).add(new Flight(from,to,time,duration));
    }

    @Override
    public String toString() {
        return String.format("%s (%s)%n%s%n%d",name,code,country,passangers);
    }
}

class Airports{
    HashMap<String,Airport> airports;

    public Airports() {
        airports = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        airports.put(code,new Airport(name,country,code,passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        if(airports.containsKey(from)){
            airports.get(from).addFlight(from,to,time,duration);
        }
    }

    public void showFlightsFromAirport(String from) {
        Airport a = airports.get(from);
        System.out.println(a);
        List<Flight> x = a.flights.entrySet().stream().flatMap(o -> o.getValue().stream()).collect(Collectors.toList());
        for(int i=0;i<x.size();i++){
            System.out.printf("%d. %s-%s %s-%s%n",i+1,x.get(i).from,x.get(i).to,x.get(i).time,x.get(i).duration);
        }

    }

    public void showDirectFlightsFromTo(String from, String to) {
        if(airports.containsKey(from)){
            Airport a=airports.get(from);
            if(a.flights.containsKey(to)){
                List<Flight> x = new ArrayList<>(a.flights.get(to));
                for(int i=0;i<x.size();i++){
                    System.out.printf("%d. %s-%s %s-%s%n",i+1,x.get(i).from,x.get(i).to,x.get(i).time,x.get(i).duration);
                }
                return;
            }
        }
        System.out.println("Nema takvi be smotan");
    }

    public void showDirectFlightsTo(String to) {
        Collection<Airport> values = airports.values();
        List<TreeSet<Flight>> treesets = values.stream().flatMap(x -> x.flights.values().stream()).collect(Collectors.toList());
        List<Flight> flights = treesets.stream().flatMap(o -> o.stream()).collect(Collectors.toList());

        List<Flight> x = airports.values().stream().flatMap(o -> o.flights.values().stream()).flatMap(o -> o.stream()).filter(o->o.to.equals(to)).collect(Collectors.toList());
        for (Flight f:x) {
            System.out.printf("%s-%s %s-%s%n",f.from,f.to,f.time,f.duration);
        }
    }

}
