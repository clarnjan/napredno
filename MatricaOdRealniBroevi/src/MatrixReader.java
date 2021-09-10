import java.io.*;

public class MatrixReader {
    public static DoubleMatrix read(InputStream input) throws InsufficientElementsException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String[] nm = br.readLine().split(" ");
        int n = Integer.parseInt(nm[0].trim());
        int m = Integer.parseInt(nm[1].trim());
        double[] a = new double[n * m];
        for (int i = 0; i < n; i++) {
            String[] spl = br.readLine().split(" ");
            for (int j = 0; j < spl.length; j++) {
                double number = Double.parseDouble(spl[j].trim());
                a[m * i + j] = number;
            }
        }
        return new DoubleMatrix(a, n, m);
    }
}
