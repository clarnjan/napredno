    import java.text.DateFormat;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.util.*;

    public class EventCalendarTest {
        public static void main(String[] args) throws ParseException {
            Scanner scanner = new Scanner(System.in);
            int n = scanner.nextInt();
            scanner.nextLine();
            int year = scanner.nextInt();
            scanner.nextLine();
            EventCalendar eventCalendar = new EventCalendar(year);
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            for (int i = 0; i < n; ++i) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                String name = parts[0];
                String location = parts[1];
                Date date = df.parse(parts[2]);
                try {
                    eventCalendar.addEvent(name, location, date);
                } catch (WrongDateException e) {
                    System.out.println(e.getMessage());
                }
            }
            Date date = df.parse(scanner.nextLine());
            eventCalendar.listEvents(date);
            eventCalendar.listByMonth();
        }
    }

    class Event implements Comparable<Event> {
        String name;
        String location;
        Date date;

        public Event(String name, String location, Date date) {
            this.name = name;
            this.location = location;
            this.date = date;
        }

        @Override
        public int compareTo(Event o) {
            if(date.compareTo(o.date)!=0)
                return date.compareTo(o.date);
            else
                return name.compareTo(o.name);
        }

        @Override
        public String toString() {
            return String.format("%s at %s, %s",EventCalendar.dateFormat.format(date),location,name);
        }
    }

    class WrongDateException extends Exception{
        public WrongDateException(String date) {
            super(String.format("Wrong date: %s",date.toString()));
        }
    }
    class EventCalendar {
        int year;
        HashMap<Integer, HashMap<Integer, TreeSet<Event>>> events;
        public static SimpleDateFormat dateFormat;
        public EventCalendar(int year) {
            this.year = year;
            events = new HashMap<>();
            dateFormat = new SimpleDateFormat("dd MMM, yyyy HH:mm");
        }

        public void addEvent(String name, String location, Date date) throws WrongDateException {
            int eventYear = date.getYear()+1900;
            if(eventYear!=year) {
                SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                throw (new WrongDateException(sdf.format(date)));
            }
            Integer month = date.getMonth()+1;
            Integer day = date.getDate();
            HashMap<Integer,TreeSet<Event>> monthEvents=new HashMap<>();
            TreeSet<Event> dayEvents = new TreeSet<>();
            if(events.containsKey(month))
                monthEvents = events.get(month);
            if(monthEvents.containsKey(day)){
                dayEvents = monthEvents.get(day);
            }
            dayEvents.add(new Event(name,location,date));
            monthEvents.put(day,dayEvents);
            events.put(month,monthEvents);
        }

        public void listEvents(Date date) {
            int month = date.getMonth()+1;
            int day = date.getDate();
            if(events.containsKey(month)){
                HashMap<Integer, TreeSet<Event>> monthEvents = events.get(month);
                if(monthEvents.containsKey(day)){
                    TreeSet<Event> dayEvents =monthEvents.get(day);
                    dayEvents.stream().forEach(System.out::println);
                }
            }
            else{
                System.out.println("No events on this day!");
            }
        }

        public void listByMonth() {
            for(int i=1;i<=12;i++) {
                int numEvents=0;
                if(events.containsKey(i)) {
                    HashMap<Integer, TreeSet<Event>> x = events.get(i);
                    for(Integer j : x.keySet()){
                        numEvents += x.get(j).size();
                    }
                }
                System.out.println(String.format("%d : %d",i,numEvents));
            }
        }
    }
