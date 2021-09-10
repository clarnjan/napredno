package airports2;

import java.util.*;
import java.util.stream.Collectors;

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

// vashiot kod ovde

class Airport {
    private final String name;
    private final String country;
    private final String code;
    private final int passengers;
    Map<String, TreeSet<Flight>> flights;

    public Airport(String name, String country, String code, int passengers) {
        super();
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        flights = new TreeMap<>();
    }

    @Override
    public String toString() {
        return String.format("%s (%s)\n%s\n%d", name, code, country, passengers);
    }

    public void addFlight(String from, Flight flight) {

        flights.computeIfPresent(from, (key, value) -> {
            value.add(flight);
            return value;
        });

        flights.computeIfAbsent(from, (key) -> {
            TreeSet<Flight> set = new TreeSet<>();
            set.add(flight);
            return set;
        });
    }

    public List<Flight> getFlights() {
        return flights
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}

class Flight implements Comparable<Flight>{
    private final String from;
    private final String to;
    private final int time;
    private final int duration;

    public Flight(String from, String to, int time, int duration) {
        super();
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time;
    }

    @Override
    public int compareTo(Flight o) {
        return Comparator.comparing(Flight::getTo)
                .thenComparing(Flight::getTime)
                .thenComparing(Flight::endTime)
                .compare(this, o);
    }

    String duration() {
        int startHour = time / 60;
        int startMinute = time % 60;
        int endHour = (time+duration) /60;
        int endMinute = (time+duration) %60;
        boolean day = false;

        int durMinute = endMinute - startMinute;
        if (endHour >= 24) {
            day = true;
        }
        if (durMinute < 0) {
            durMinute+=60;
            endHour--;
        }
        int durHour = endHour - startHour;
        if (day)
            return String.format("+1d %dh%02dm", durHour,durMinute);
        else
            return String.format("%dh%02dm", durHour,durMinute);
    }

    @Override
    public String toString() {
        //1. LHR-ATL 12:44-17:35 4h51m
        return String.format("%s-%s %s-%s %s"
                , from, to, startTime(), endTime(), duration());
    }

    public String startTime() {
        int hours = time / 60;
        int minutes = time % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    String endTime() {
        int hours = (time+duration) /60;
        int minutes = (time+duration) %60;
        if (hours >= 24)
            hours -=24;
        return String.format("%02d:%02d", hours,minutes);
    }
}

class Airports {
    private final HashMap<String, Airport> airports;

    public Airports() {
        airports = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        airports.put(code, new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        Flight flight = new Flight(from, to, time, duration);
        Airport airport = this.airports.get(from);
        airport.addFlight(to, flight);
    }

    public void showFlightsFromAirport(String code) {
        System.out.println(this.airports.get(code).toString());
        List<Flight> list = this.airports.get(code).getFlights();

        for(int i=0; i<list.size(); i++) {
            System.out.println(i+1 +". "+list.get(i).toString());
        }
    }

    public void showDirectFlightsFromTo(String from, String to) {

        if(airports.get(from).flights.get(to) == null)
            System.out.printf("No flights from %s to %s\n",from,to);

        airports
                .get(from)
                .getFlights()
                .stream()
                .filter(o -> o.getTo().compareTo(to) == 0)
                .forEach(o -> System.out.println(o.toString()));

    }

    public void showDirectFlightsTo(String to) {
        airports
                .values()
                .stream()
                .flatMap(o -> o.getFlights().stream())
                .filter(o -> o.getTo().compareTo(to) == 0)
                .sorted(Comparator.comparing(Flight::getTo)
                        .thenComparing(Flight::getTime)
                        .thenComparing(Flight::endTime))
                .forEach(o -> System.out.println(o.toString()));
    }
}