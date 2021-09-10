import java.util.Arrays;

public final class IntegerArray {
    private final int []a;

    public IntegerArray(int[] a) {
        this.a=a.clone();
    }

    public int length() {
        return a.length;
    }

    public int getElementAt(int i) {
        return a[i];
    }

    public int sum() {
        int result=0;
        for(int i :a){
            result+=i;
        }
        return result;
    }

    public float average() {
        return sum()/(float)a.length;
    }

    public IntegerArray getSorted() {
        int []copy=a.clone();
        for(int i=0;i<copy.length-1;i++){
            for(int j=i+1;j<copy.length;j++){
                if(copy[i]>copy[j]){
                    int temp=copy[i];
                    copy[i]=copy[j];
                    copy[j]=temp;
                }
            }
        }
        return new IntegerArray(copy);
    }

    public IntegerArray concat(IntegerArray ia) {
        int []b=ia.a.clone();
        int []x=new int[a.length+b.length];
        int i=0;
        for(;i<a.length;i++){
            x[i]=a[i];
        }
        for(int j=i;j<b.length+a.length;j++){
            x[j]=b[j-i];
        }
        return new IntegerArray(x);
    }

    @Override
    public String toString() {
        StringBuilder str= new StringBuilder("[");
        for(int i : a){
            str.append(i).append(", ");
        }
        str = new StringBuilder(str.substring(0, str.length() - 2));
        str.append("]");
        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerArray that = (IntegerArray) o;
        return Arrays.equals(a, that.a);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(a);
    }
}
