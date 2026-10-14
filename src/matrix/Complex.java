package matrix;

import exception.MatrixException;


public class Complex {
    private final double re;
    private final double im;

    public Complex() {
        this(0.0, 0.0);
    }

    public Complex(double re) {
        this(re, 0.0);
    }

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public double getRe() { return re; }
    public double getIm() { return im; }

    // Сложение: (a + bi) + (c + di)
    public Complex add(Complex other) {
        return new Complex(this.re + other.re, this.im + other.im);
    }

    // Вычитание: (a + bi) - (c + di)
    public Complex subtract(Complex other) {
        return new Complex(this.re - other.re, this.im - other.im);
    }

    // Умножение: (a + bi) * (c + di) = (ac - bd) + (ad + bc)i
    public Complex multiply(Complex other) {
        double newRe = this.re * other.re - this.im * other.im;
        double newIm = this.re * other.im + this.im * other.re;
        return new Complex(newRe, newIm);
    }

    // Умножение на действительное число (скаляр)
    public Complex scale(double factor) {
        return new Complex(this.re * factor, this.im * factor);
    }

    // Деление: (a + bi) / (c + di)
    public Complex divide(Complex other) throws MatrixException {
        double denominator = other.re * other.re + other.im * other.im;
        if (Math.abs(denominator) < 1e-9) {
            throw new MatrixException("Деление на ноль в комплексных числах");
        }
        double newRe = (this.re * other.re + this.im * other.im) / denominator;
        double newIm = (this.im * other.re - this.re * other.im) / denominator;
        return new Complex(newRe, newIm);
    }

    // Проверка на ноль (с учетом погрешности double)
    public boolean isZero() {
        return Math.abs(re) < 1e-9 && Math.abs(im) < 1e-9;
    }

    @Override
    public String toString() {
        double r = Math.abs(re) < 1e-9 ? 0.0 : re;
        double i = Math.abs(im) < 1e-9 ? 0.0 : im;

        if (i == 0) return String.format("%.2f", r);
        if (r == 0) return String.format("%.2fi", i);
        return String.format("%.2f %s %.2fi", r, (i > 0 ? "+" : "-"), Math.abs(i));
    }
}