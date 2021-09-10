import java.util.*;

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

class Component implements Comparable<Component>{
    String color;
    int weight;
    Set<Component> componentSet;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        componentSet = new TreeSet<>();
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public int compareTo(Component o) {
        return Comparator.comparing(Component::getWeight).thenComparing(Component::getColor).compare(this,o);
    }

    public void addComponent(Component component) {
        componentSet.add(component);
    }

    public String print(String dash) {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%s%d:%s%n",dash,weight,color));
        for (Component c : componentSet){
            result.append(c.print(dash+"---"));
        }
        return result.toString();
    }

    public void changeColor(int weight, String color) {
        if(this.weight<weight)
            this.color=color;
        for(Component c : componentSet){
            c.changeColor(weight,color);
        }
    }
}

class Window{
    String name;
    Map<Integer,Component> componentMap;

    public Window(String name) {
        this.name=name;
        componentMap = new TreeMap<>();
    }

    public void addComponent(int position, Component component) {
        if(componentMap.containsKey(position))
            throw new InvalidPositionException(position);
        componentMap.put(position,component);
    }

    public void changeColor(int weight, String color) {
        for(Component c : componentMap.values()){
            c.changeColor(weight,color);
        }
    }

    public void swichComponents(int pos1, int pos2) {
        Component c=componentMap.get(pos1);
        componentMap.put(pos1,componentMap.get(pos2));
        componentMap.put(pos2,c);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("WINDOW %S%n",name));
        for(Map.Entry<Integer, Component> e : componentMap.entrySet()){
            result.append(String.format("%d:%s",e.getKey(),e.getValue().print("")));
        }
        return result.toString();
    }
}

class InvalidPositionException extends RuntimeException{
    InvalidPositionException(int position){
        super(String.format("Invalid position %d, alredy taken!",position));
    }
}