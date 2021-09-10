import java.text.SimpleDateFormat;
import java.util.*;

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

class Airport {
    String name;
    String country;
    int num;

    public Airport(String name, String country, int num) {
        this.name = name;
        this.country = country;
        this.num = num;
    }
}

class Flight implements Comparable<Flight>{
    String code;
    Date from;
    Date to;
    String diff;

    public Flight(String code, Date from, Date to, String diff) {
        this.code = code;
        this.from = from;
        this.to = to;
        this.diff = diff;
    }

    @Override
    public int compareTo(Flight o) {
        int res = code.compareTo(o.code);
        return res != 0 ? res : from.compareTo(o.from);
    }

    public Date getFrom() {
        return from;
    }

    public String getCode() {
        return code;
    }
}

class Airports {
    HashMap<String,Airport> airportList;
    TreeMap<String,TreeSet<Flight>> fligtFrom;
    TreeMap<String,TreeSet<Flight>> fligtTo;
    public static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public Airports() {
        airportList = new HashMap<>();
        fligtFrom = new TreeMap<>();
        fligtTo = new TreeMap<>();
    }

    public void addAirport(String name, String country, String code, int num) {
        airportList.put(code,new Airport(name,country,num));
    }

    public void addFlights(String from, String to, int time, int duration){
        Date dateFrom = new Date(time* 60000L);
        Date dateTo = new Date((time+duration)* 60000L);
        int minDiff = dateTo.getMinutes() - dateFrom.getMinutes();
        int hourDiff = dateTo.getHours() - dateFrom.getHours();
        if(minDiff<0) {
            minDiff = 60 + minDiff;
            hourDiff-=1;
        }
        int dayDiff = dateTo.getDate() - dateFrom.getDate();
        String diff = "";
        if(dayDiff>0){
            diff+="+"+dayDiff+"d ";
            hourDiff=24+hourDiff;
        }
        diff+=hourDiff+"h";
        if(minDiff<10)
            diff+="0";
        diff+=minDiff+"m";
        fligtFrom.putIfAbsent(from,new TreeSet<>());
        fligtFrom.get(from).add(new Flight(to,dateFrom,dateTo,diff));
        fligtTo.putIfAbsent(to,new TreeSet<>(Comparator.comparing(Flight::getFrom).thenComparing(Flight::getCode)));
        fligtTo.get(to).add(new Flight(from,dateFrom,dateTo,diff));
    }

    public void showFlightsFromAirport(String from) {
        TreeSet<Flight> flights = fligtFrom.getOrDefault(from, new TreeSet<>());
        ArrayList<Flight> a = new ArrayList<>(flights);
        Airport airport = airportList.get(from);
        System.out.printf("%s (%s)%n%s%n%d%n",airport.name,from,airport.country,airport.num);
        for(int i=0;i<a.size();i++){
            Flight flight = a.get(i);
            System.out.printf("%d. %s-%s %s-%s %s%n",
                    i+1,from,flight.code,sdf.format(flight.from),sdf.format(flight.to),flight.diff);
        }
    }

    public void showDirectFlightsFromTo(String from, String to) {
        TreeSet<Flight> flights = fligtFrom.getOrDefault(from, new TreeSet<>());
        ArrayList<Flight> a = new ArrayList<>(flights);
        boolean none = true;
        for (Flight flight : a) {
            if (flight.code.equals(to)) {
                none = false;
                System.out.printf("%s-%s %s-%s %s%n",
                        from, to, sdf.format(flight.from), sdf.format(flight.to), flight.diff);
            }
        }
        if(none)
            System.out.printf("No flights from %s to %s%n",from,to);
    }

    public void showDirectFlightsTo(String to) {
        TreeSet<Flight> flights = fligtTo.getOrDefault(to, new TreeSet<>(Comparator.comparing(Flight::getFrom).thenComparing(Flight::getCode)));
        ArrayList<Flight> a = new ArrayList<>(flights);
        for (Flight flight : a) {
            System.out.printf("%s-%s %s-%s %s%n",
                    flight.code, to, sdf.format(flight.from), sdf.format(flight.to), flight.diff);
        }
    }
}
