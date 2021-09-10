import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

enum Color {
    RED, GREEN, BLUE
}

interface Scalable{
    void scale(float scaleFactor);
}
interface Stackable{
    float weight();
}

abstract class Shape implements Scalable, Stackable {
    String id;
    Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    @Override
    abstract public void scale(float scaleFactor);

    @Override
    abstract public float weight();
}

class Circle extends Shape{
    float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius=radius;
    }

    @Override
    public void scale(float scaleFactor) {
        this.radius*=scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (radius*radius*Math.PI);
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f",id,color,weight());
    }
}
class Rectangle extends Shape{
    float width;
    float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width=width;
        this.height=height;
    }

    @Override
    public void scale(float scaleFactor) {
        this.width*=scaleFactor;
        this.height*=scaleFactor;
    }

    @Override
    public float weight() {
        return width*height;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f",id,color,weight());
    }
}

class Canvas {
    List<Shape> shapeList;

    public Canvas() {
        shapeList=new ArrayList<>();
    }

    void add(String id, Color color, float radius){
        Shape c=new Circle(id,color,radius);
        addShape(c);
    }

    private void addShape(Shape s) {
        int i=0;
        for(;i<shapeList.size();i++) {
            if(shapeList.get(i).weight()<s.weight()){
                break;
            }
        }
        shapeList.add(i,s);
    }

    void add(String id, Color color, float width, float height){
        Shape r=new Rectangle(id,color,width,height);
        addShape(r);
    }
    void scale(String id, float scaleFactor){
        for(Shape shape : shapeList){
            if(shape.id.equals(id)){
                shape.scale(scaleFactor);
                shapeList.remove(shape);
                addShape(shape);
                return;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for(Shape shape : shapeList){
            sb.append(shape.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}