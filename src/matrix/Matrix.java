package matrix;

import exception.MatrixException;

public class Matrix {


    private final int rows;
    private final int columns;
    private Complex[][] matrix;


    public Matrix() {
        this.rows    = 1;
        this.columns = 1;
        this.matrix  = new Complex[rows][columns];
        this.fill(new Complex(0, 0));
    }


    public Matrix(int rows, int columns) throws MatrixException {
        if (rows <= 0 || columns <= 0) {
            throw new MatrixException("Заданы неверные размеры матрицы");
        }
        this.rows    = rows;
        this.columns = columns;
        this.matrix  = new Complex[rows][columns];
        this.fill(new Complex(0, 0));
    }

    // [ТВОЙ КОД - АДАПТИРОВАНО ПОД Complex]
    public Matrix(int size) throws MatrixException {
        if (size <= 0) {
            throw new MatrixException("Заданы неверные размеры матрицы");
        }
        this.rows = this.columns = size;
        this.matrix = new Complex[size][size];
        this.fill(new Complex(0, 0));
    }


    public Matrix(Complex[][] matrix) throws MatrixException {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            throw new MatrixException("Передан пустой массив");
        }
        this.rows    = matrix.length;
        this.columns = matrix[0].length;
        this.matrix  = new Complex[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                this.matrix[i][j] = (matrix[i][j] != null) ? matrix[i][j] : new Complex(0, 0);
            }
        }
    }



    public int getRows() { return rows; }
    public int getColumns() { return columns; }

    public void setElement(int row, int col, Complex value) throws MatrixException {
        if (row < 0 || row >= rows || col < 0 || col >= columns) {
            throw new MatrixException("Индекс выходит за границы матрицы");
        }
        this.matrix[row][col] = value;
    }

    public Complex getElement(int row, int col) throws MatrixException {
        if (row < 0 || row >= rows || col < 0 || col >= columns) {
            throw new MatrixException("Индекс выходит за границы матрицы");
        }
        return this.matrix[row][col];
    }

    // [ТВОЙ КОД - ИСПРАВЛЕНО] Сложение матриц
    public static final Matrix sum(Matrix matrix1, Matrix matrix2) throws MatrixException {
        if (!check(matrix1, matrix2, false)) {
            throw new MatrixException("Размеры матриц не совпадают для сложения");
        } else {
            Matrix result = new Matrix(matrix1.rows, matrix1.columns);

            for (int i = 0; i < matrix1.rows; i++) {
                for (int j = 0; j < matrix1.columns; j++) {
                    result.matrix[i][j] = matrix1.matrix[i][j].add(matrix2.matrix[i][j]);
                }
            }
            return result;
        }
    }

    // [ТВОЙ КОД - ИСПРАВЛЕНО] Умножение матриц
    public static final Matrix multiply(Matrix matrix1, Matrix matrix2) throws MatrixException {
        if (!check(matrix1, matrix2, true)) {
            throw new MatrixException("Число столбцов первой матрицы не равно числу строк второй матрицы");
        } else {
            Matrix result = new Matrix(matrix1.rows, matrix2.columns);
            int n = matrix1.columns;

            for (int i = 0; i < result.rows; i++) {
                for (int j = 0; j < result.columns; j++) {
                    Complex sum = new Complex(0, 0);
                    for (int k = 0; k < n; k++) {
                        sum = sum.add(matrix1.matrix[i][k].multiply(matrix2.matrix[k][j]));
                    }
                    result.matrix[i][j] = sum;
                }
            }
            return result;
        }
    }

    // [ДОБАВЛЕНО] Транспонирование матрицы
    public final Matrix transpose() throws MatrixException {
        Matrix transposed = new Matrix(this.columns, this.rows);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                transposed.matrix[j][i] = this.matrix[i][j];
            }
        }
        return transposed;
    }

    // [ДОБАВЛЕНО] Вычисление определителя (разложение по первой строке)
    public final Complex determinant() throws MatrixException {
        if (!this.checkSquare()) {
            throw new MatrixException("Определитель можно вычислить только для квадратной матрицы");
        }
        if (this.rows == 1) {
            return this.matrix[0][0];
        }
        if (this.rows == 2) {
            return this.matrix[0][0].multiply(this.matrix[1][1])
                    .subtract(this.matrix[0][1].multiply(this.matrix[1][0]));
        }

        Complex det = new Complex(0, 0);
        for (int j = 0; j < this.columns; j++) {
            Complex cofactor = getSubMatrix(0, j).determinant().scale((j % 2 == 0) ? 1.0 : -1.0);
            det = det.add(this.matrix[0][j].multiply(cofactor));
        }
        return det;
    }

    // [ДОБАВЛЕНО] Вспомогательный метод: получение минора
    private Matrix getSubMatrix(int excludeRow, int excludeCol) throws MatrixException {
        Matrix sub = new Matrix(this.rows - 1, this.columns - 1);
        int r = 0;
        for (int i = 0; i < this.rows; i++) {
            if (i == excludeRow) continue;
            int c = 0;
            for (int j = 0; j < this.columns; j++) {
                if (j == excludeCol) continue;
                sub.matrix[r][c] = this.matrix[i][j];
                c++;
            }
            r++;
        }
        return sub;
    }

    // [ДОБАВЛЕНО] Нахождение обратной матрицы
    public final Matrix inverse() throws MatrixException {
        if (!this.checkSquare()) {
            throw new MatrixException("Обратная матрица существует только для квадратных матриц");
        }
        Complex det = this.determinant();
        if (det.isZero()) {
            throw new MatrixException("Определитель равен 0. Обратной матрицы не существует (деление невозможно)");
        }

        if (this.rows == 1) {
            Matrix res = new Matrix(1, 1);
            res.matrix[0][0] = new Complex(1, 0).divide(det);
            return res;
        }

        Matrix cofactors = new Matrix(this.rows, this.columns);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                double sign = ((i + j) % 2 == 0) ? 1.0 : -1.0;
                Complex minorDet = this.getSubMatrix(i, j).determinant();
                cofactors.matrix[i][j] = minorDet.scale(sign);
            }
        }

        Matrix adjugate = cofactors.transpose();
        Matrix inverseMatrix = new Matrix(this.rows, this.columns);

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                inverseMatrix.matrix[i][j] = adjugate.matrix[i][j].divide(det);
            }
        }
        return inverseMatrix;
    }

    // [ДОБАВЛЕНО] Деление матриц: A / B = A * B^(-1)
    public static final Matrix divide(Matrix matrix1, Matrix matrix2) throws MatrixException {
        if (!matrix2.checkSquare()) {
            throw new MatrixException("Делитель (вторая матрица) должен быть квадратной матрицей");
        }
        Matrix inverseMatrix2 = matrix2.inverse();
        return multiply(matrix1, inverseMatrix2);
    }

    // [ТВОЙ КОД - УЛУЧШЕНО ФОРМАТИРОВАНИЕ] Вывод матрицы в консоль
    public final void print() {
        for (int i = 0; i < rows; i++) {
            System.out.print("| ");
            for (int j = 0; j < columns; j++) {
                System.out.printf("%14s ", matrix[i][j].toString());
            }
            System.out.println("|");
        }
    }

    // [ТВОЙ КОД - ИСПРАВЛЕНО] Заполнение матрицы заданным числом
    public final void fill(Complex number) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = number;
            }
        }
    }

    // [ТВОЙ КОД - ИСПРАВЛЕНО] Проверка размерностей
    private final static boolean check(Matrix matrix1, Matrix matrix2, boolean forMultiply) {
        if (forMultiply) {
            return matrix1.columns == matrix2.rows;
        } else {
            return (matrix1.rows == matrix2.rows) && (matrix1.columns == matrix2.columns);
        }
    }

    // [ТВОЙ КОД] Проверка на квадратность
    public final boolean checkSquare() {
        return rows == columns;
    }
}