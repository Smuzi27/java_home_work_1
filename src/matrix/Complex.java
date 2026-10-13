package matrix;

public record Complex(double re, double im) {
    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE = new Complex(1, 0);

    public Complex add(Complex o) {
        return new Complex(this.re + o.re, this.im + o.im);
    }

    public Complex subtract(Complex o) {
        return new Complex(this.re - o.re, this.im - o.im);
    }

    public Complex multiply(Complex o) {
        return new Complex(
                this.re * o.re - this.im * o.im,
                this.re * o.im + this.im * o.re
        );
    }

    public Complex divide(Complex o) {
        double denom = o.re * o.re + o.im * o.im;
        if (Math.abs(denom) < 1e-9) {
            throw new ArithmeticException("Деление на нулевое комплексное число");
        }
        return new Complex(
                (this.re * o.re + this.im * o.im) / denom,
                (this.im * o.re - this.re * o.im) / denom
        );
    }

    public boolean isZero() {
        return Math.hypot(re, im) < 1e-9;
    }

    @Override
    public String toString() {
        if (Math.abs(im) < 1e-9) return String.format("%.2f", re);
        if (Math.abs(re) < 1e-9) return String.format("%.2fi", im);
        return String.format("(%.2f %c %.2fi)", re, im >= 0 ? '+' : '-', Math.abs(im));
    }
}