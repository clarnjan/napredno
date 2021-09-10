
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ComparatorByLength implements Comparator<String>{

    @Override
    public int compare(String o1, String o2) {
        return Integer.compare(o1.length(),o2.length());
    }
}

public class main {

    public static void main(String[] args) {
        Set<String> s1 = new HashSet<>();  // hash kade shto key=value
        Set<String> s2 = new LinkedHashSet<>();  // hash kade shto key=value koga sakas da go zapazis pocetniot redosled
        Set<String> s3 = new TreeSet<>();   // sortirano drvo
        Set<String> s4 = new TreeSet<String>(new ComparatorByLength());

        Map<String,String> m1 = new HashMap<>();  // hash normalen
        Map<String,String> m2 = new LinkedHashMap<>();  // hash koga sakas da go zapazis pocetniot redosled
        Map<String,String> m3 = new TreeMap<>();  // hash sortiran po key
        Map<String,String> m4 = new TreeMap<>(new ComparatorByLength());

        s1.add("asdf");
        s1.add("%");
        s1.add("1542");
        s1.add("");

        s2.add("asdf");
        s2.add("%");
        s2.add("1542");
        s2.add("");

        s3.add("asdf");
        s3.add("%");
        s3.add("1542");
        s3.add("");

        s4.add("asdf");
        s4.add("%");
        s4.add("1542");
        s4.add("");

//        System.out.println("Hash");
//        System.out.println(s1);
//        System.out.println("LinkedHash");
//        System.out.println(s2);
//        System.out.println("Tree");
//        System.out.println(s3);
//        System.out.println("TreeByLength");
//        System.out.println(s4);

        //flatMap
        List<List<String>> listOfListsOfStrings = new ArrayList<>();
        List<String> listOfStrings = new ArrayList<>();
        listOfStrings.add("hello");
        listOfStrings.add("how");
        listOfStrings.add("asd");
        listOfListsOfStrings.add(listOfStrings);
        List<String> listOfStrings2 = new ArrayList<>();
        listOfStrings2.add("as");
        listOfStrings2.add("qwe");
        listOfStrings2.add("qwer");
        listOfListsOfStrings.add(listOfStrings2);
        listOfListsOfStrings.add(listOfStrings2);
        listOfListsOfStrings.add(listOfStrings);

//        listOfListsOfStrings.stream().forEach(System.out::println);
//        listOfListsOfStrings.stream().forEach(x->x.stream().forEach(System.out::println));
//        listOfListsOfStrings.stream().flatMap(x->x.stream()).sorted().forEach(System.out::println);

        HashSet<HashSet<String>> hashSetOfHashSetsOfStrings = new HashSet<>();
        HashSet<String> hashSetOfStrings = new HashSet<>();
        hashSetOfStrings.add("ASD");
        hashSetOfStrings.add("GDFF");
        hashSetOfStrings.add("sdaf");
        hashSetOfStrings.add("bgfbgf");
        hashSetOfHashSetsOfStrings.add(hashSetOfStrings);
        HashSet<String> hashSetOfStrings2 = new HashSet<>();
        hashSetOfStrings2.add("tre");
        hashSetOfStrings2.add("hyr");
        hashSetOfStrings2.add("RHG");
        hashSetOfStrings2.add("hgtew");
        hashSetOfHashSetsOfStrings.add(hashSetOfStrings2);
//        hashSetOfHashSetsOfStrings.stream().flatMap(x->x.stream()).sorted().forEach(System.out::println);

        HashMap<Integer,TreeSet<String>> hashMapofTreeSetes = new HashMap<>();
        TreeSet<String> a = new TreeSet<>();
        a.add("tre");
        a.add("hyr");
        a.add("RHG");
        a.add("hgtew");
        TreeSet<String> b = new TreeSet<>();
        b.add("graewfwweqd");
        b.add("hsadfyr");
        b.add("RHwG");
        b.add("q");
        hashMapofTreeSetes.put(0,a);
        hashMapofTreeSetes.put(1,b);
        hashMapofTreeSetes.put(2,a);
        List<String> sortedList = hashMapofTreeSetes.entrySet().stream().flatMap(x -> x.getValue().stream())
                .sorted(Comparator.comparing(String::length).thenComparing(Comparator.naturalOrder()))
                .collect(Collectors.toList());
//        for(int i=0;i<sortedList.size();i++){
//            System.out.printf("%d. %s%n",i,sortedList.get(i));
//        }
        Map<Integer, ArrayList<String>> mapOfTreeSetFromSortedList = sortedList.stream().collect(Collectors.groupingBy(String::length, Collectors.toCollection(ArrayList::new)));
        System.out.println(mapOfTreeSetFromSortedList.values());
        Map<Integer, Integer> mapIfIntegers = sortedList.stream().collect(Collectors.groupingBy(String::length, Collectors.summingInt(String::length)));
//        System.out.println(mapIfIntegers);
//        hashMapofTreeSetes.forEach((k,v)->v.stream().forEach(System.out::println));
//        IntStream.range(0,listOfStrings.size()).forEach(i-> System.out.printf("%d. %s%n",i,listOfListsOfStrings.get(i)));
        mapOfTreeSetFromSortedList.computeIfPresent(11,(k,v)->{
            v.add("Not 11 !");
            return v;
        });
//        System.out.println(mapOfTreeSetFromSortedList);

        HashMap<String,TreeSet<String>> flights = new HashMap<>();
        String to="asd";
        flights.computeIfAbsent(to,(k)->new TreeSet<>());
        flights.get(to).add("qwe");
        flights.computeIfAbsent(to,(k)->new TreeSet<>());
        flights.get(to).add("qewr");
        to="asdf";
        flights.computeIfAbsent(to,(k)->new TreeSet<>());
        flights.get(to).add("qwe");
        flights.computeIfPresent(to,(k,v)->{
            return v;
        });
        Date d = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MMM.yy E hh:mm:ss z");
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0,"GMT"));
        System.out.println(simpleDateFormat.format(d));
    }
}


