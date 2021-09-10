import java.util.*;

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}

class Participant{
    String city;
    String code;
    String name;
    int age;

    public Participant(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d",code,name,age);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

class Audition{
    Map<String,Map<String,Participant>> participantMap;

    public Audition() {
        participantMap = new HashMap<>();
    }

    public void addParticpant(String city, String code, String name, int age) {
        participantMap.computeIfPresent(city,(k,v)->{
            v.computeIfAbsent(code,(x)-> new Participant(city,code,name,age));
            return v;
        });
        participantMap.computeIfAbsent(city,(k)->{
            Map<String, Participant> participantCity = new HashMap<>();
            participantCity.put(code,new Participant(city,code,name,age));
            return participantCity;
        });
    }

    public void listByCity(String city) {
        participantMap.computeIfPresent(city,(k,v)->{
            v.values().stream().sorted(Comparator.comparing(Participant::getName).thenComparing(Participant::getAge)).forEach(System.out::println);
            return v;
        });
    }
}