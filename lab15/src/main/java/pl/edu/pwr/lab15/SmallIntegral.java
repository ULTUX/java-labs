package pl.edu.pwr.lab15;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SmallIntegral {

    public static BigDecimal integrateBig(BigDecimal a, BigDecimal b) {
        BigDecimal N = BigDecimal.valueOf(100000000);
        int scale = 10;


        BigDecimal h = b.subtract(a).divide(N.subtract(BigDecimal.valueOf(1)), scale, RoundingMode.HALF_UP);

        BigDecimal sum = BigDecimal.valueOf(1).divide(BigDecimal.valueOf(3), scale, RoundingMode.HALF_UP).multiply(a).add(b);

        for (BigDecimal i = BigDecimal.valueOf(1); i.compareTo(N.subtract(BigDecimal.valueOf(1))) < 0; i =  i.add(BigDecimal.valueOf(2))) {
            BigDecimal x = a.add(h.multiply(i));
            sum = sum.add(BigDecimal.valueOf(4).divide(BigDecimal.valueOf(3), scale, RoundingMode.HALF_UP).multiply(fBig(x)));
        }

        for (BigDecimal i = BigDecimal.valueOf(2); i.compareTo(N.subtract(BigDecimal.valueOf(1))) < 0; i = i.add(BigDecimal.valueOf(2))) {
            BigDecimal x = a.add(h.multiply(i));
            sum = sum.add(BigDecimal.valueOf(2).divide(BigDecimal.valueOf(3), scale, RoundingMode.HALF_UP).multiply(fBig(x)));

        }
        return h.multiply(sum);
    }

    public static BigDecimal fBig(BigDecimal x) {
        return x.multiply(x);
    }

    public static double f(double x) {
        return x*x;
    }

    public static double integrate(double a, double b) {
        int N = 100000000;                    // precision parameter
        double h = (b - a) / (N - 1);     // step size

        // 1/3 terms
        double sum = 1.0 / 3.0 * (f(a) + f(b));

        // 4/3 terms
        for (int i = 1; i < N - 1; i += 2) {
            double x = a + h * i;
            sum += 4.0 / 3.0 * f(x);
        }

        // 2/3 terms
        for (int i = 2; i < N - 1; i += 2) {
            double x = a + h * i;
            sum += 2.0 / 3.0 * f(x);
        }

        return sum * h;
    }



    // sample client program
    public static void main(String[] args) {
        int a = 0;
        int b = 10;
        System.out.println(integrate(a, b));
        System.out.println(integrateBig(BigDecimal.valueOf(0), BigDecimal.valueOf(10)));
    }

}
