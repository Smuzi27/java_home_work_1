package matrix;

import java.util.Arrays;

public class ComplexMatrix {
    private final int rows;
    private final int cols;
    private final Complex[][] data;

    public ComplexMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(data[i], Complex.ZERO);
        }
    }

    public ComplexMatrix(Complex[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new Complex[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(data[i], 0, this.data[i], 0, cols);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Complex get(int r, int c) {
        return data[r][c];
    }

    public void set(int r, int c, Complex val) {
        data[r][c] = val;
    }

    // Сложение
    public ComplexMatrix add(ComplexMatrix o) {
        if (this.rows != o.rows || this.cols != o.cols) {
            throw new IllegalArgumentException("Размеры матриц для сложения должны совпадать");
        }
        ComplexMatrix res = new ComplexMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.data[i][j] = this.data[i][j].add(o.data[i][j]);
            }
        }
        return res;
    }

    // Транспонирование
    public ComplexMatrix transpose() {
        ComplexMatrix res = new ComplexMatrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.data[j][i] = this.data[i][j];
            }
        }
        return res;
    }

    // Умножение
    public ComplexMatrix multiply(ComplexMatrix o) {
        if (this.cols != o.rows) {
            throw new IllegalArgumentException("Число столбцов первой матрицы должно быть равно числу строк второй");
        }
        ComplexMatrix res = new ComplexMatrix(this.rows, o.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < o.cols; j++) {
                Complex sum = Complex.ZERO;
                for (int k = 0; k < this.cols; k++) {
                    sum = sum.add(this.data[i][k].multiply(o.data[k][j]));
                }
                res.data[i][j] = sum;
            }
        }
        return res;
    }

    // Определитель (рекурсивно по первой строке)
    public Complex determinant() {
        if (rows != cols) {
            throw new IllegalStateException("Определитель существует только для квадратных матриц");
        }
        return computeDet(this.data);
    }

    private Complex computeDet(Complex[][] m) {
        int n = m.length;
        if (n == 1) return m[0][0];
        if (n == 2) {
            return m[0][0].multiply(m[1][1]).subtract(m[0][1].multiply(m[1][0]));
        }

        Complex det = Complex.ZERO;
        for (int col = 0; col < n; col++) {
            Complex minorDet = computeDet(getMinor(m, 0, col));
            Complex term = m[0][col].multiply(minorDet);
            det = (col % 2 == 0) ? det.add(term) : det.subtract(term);
        }
        return det;
    }

    private Complex[][] getMinor(Complex[][] m, int excludedRow, int excludedCol) {
        int n = m.length;
        Complex[][] minor = new Complex[n - 1][n - 1];
        int r = 0;
        for (int i = 0; i < n; i++) {
            if (i == excludedRow) continue;
            int c = 0;
            for (int j = 0; j < n; j++) {
                if (j == excludedCol) continue;
                minor[r][c++] = m[i][j];
            }
            r++;
        }
        return minor;
    }

    // Обратная матрица: A^-1 = adj(A) / det(A)
    public ComplexMatrix inverse() {
        Complex det = determinant();
        if (det.isZero()) {
            throw new ArithmeticException("Матрица вырождена: определитель равен нулю");
        }

        ComplexMatrix adjugate = new ComplexMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Complex cofactor = computeDet(getMinor(this.data, i, j));
                if ((i + j) % 2 != 0) {
                    cofactor = Complex.ZERO.subtract(cofactor);
                }
                adjugate.data[j][i] = cofactor;
            }
        }

        ComplexMatrix inv = new ComplexMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                inv.data[i][j] = adjugate.data[i][j].divide(det);
            }
        }
        return inv;
    }

    // Деление матриц: A / B = A * B^-1
    public ComplexMatrix divide(ComplexMatrix b) {
        return this.multiply(b.inverse());
    }

    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%-18s", data[i][j]);
            }
            System.out.println();
        }
    }
}
