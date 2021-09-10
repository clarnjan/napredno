import java.util.Arrays;

public class DoubleMatrix {
    private final double [][]matrix;
    public DoubleMatrix(double[] a, int n, int m) throws InsufficientElementsException {
        if(n*m>a.length)
        throw new InsufficientElementsException();
        else{
            matrix=new double[n][m];
            double []b=new double[n*m];
            for(int i=a.length-(n*m);i<a.length;i++)
                b[i+n*m-a.length]=a[i];
            for(int i=0;i<b.length;i++){
                matrix[i/m][i%m]=b[i];
            }
        }
    }

    public String getDimensions() {
        return "["+rows()+" x "+columns()+"]";
    }

    public int rows() {
        return matrix.length;
    }

    public int columns() {
        return matrix[0].length;
    }

    public double maxElementAtRow(int row) throws InvalidRowNumberException {
        if(row>rows() || row<=0)
        throw new InvalidRowNumberException();
        else{
//            double max=Double.MIN_VALUE;
//            for(int i=0;i<matrix[row].length;i++){
//                if(matrix[row][i]>max)
//                    max=matrix[row][i];
//            }
//            return max;
            return Arrays.stream(matrix[row-1]).summaryStatistics().getMax();
        }

    }

    public double maxElementAtColumn(int col) throws InvalidColumnNumberException {
        if(col>columns() || col<=0)
        throw new InvalidColumnNumberException();
        else{
            double []a=new double[rows()];
            for(int i=0;i<rows();i++){
                a[i]=matrix[i][col-1];
            }
            return Arrays.stream(a).summaryStatistics().getMax();
        }
    }

    public double sum() {
        double sum=0.0d;
        for(int i=0;i<rows();i++){
            for(int j=0;j<columns();j++){
                sum+=matrix[i][j];
            }
        }
        return sum;
    }

    public double[] toSortedArray() {
        double []a=new double[rows()*columns()];
        for(int i=0;i<rows();i++){
            for(int j=0;j<columns();j++){
                a[i*columns()+j]=matrix[i][j];
            }
        }
        for(int i=0;i<a.length-1;i++){
            for(int j=i+1;j<a.length;j++){
                if(a[i]<a[j]){
                    double temp=a[i];
                    a[i]=a[j];
                    a[j]=temp;
                }
            }
        }
        return a;
    }

    @Override
    public String toString() {
        StringBuilder s=new StringBuilder();
        for(int i=0;i<rows();i++){
            StringBuilder temp=new StringBuilder();
            for(int j=0;j<columns();j++){
                temp.append(String.format("%.2f",matrix[i][j]));
                if(j<columns()-1)
                temp.append("\t");
            }
            s.append(temp);
            if(i<rows()-1)
            s.append("\n");
        }
        return s.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleMatrix matrix1 = (DoubleMatrix) o;
        if(rows()!=matrix1.rows() || columns() != matrix1.columns() )
            return false;
        for(int i=0;i<rows();i++){
            for(int j=0;j<columns();j++){
                if(matrix[i][j]!=matrix1.matrix[i][j])
                    return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int []a=new int[rows()];
        for(int i=0;i<rows();i++)
            a[i]=Arrays.hashCode(matrix[i]);
        return Arrays.hashCode(a);
    }
}

