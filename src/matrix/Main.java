package matrix;

import exception.MatrixException;

import java.util.Scanner;


public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== КАЛЬКУЛЯТОР МАТРИЦ КОМПЛЕКСНЫХ ЧИСЕЛ ===");

        while (true) {
            printMenu();
            int choice = readInt("Выберите операцию: ");

            if (choice == 0) {
                System.out.println("Выход из программы.");
                break;
            }

            try {
                switch (choice) {
                    case 1: {
                        System.out.println("\n--- СЛОЖЕНИЕ МАТРИЦ (A + B) ---");
                        Matrix m1 = inputMatrix("A");
                        Matrix m2 = inputMatrix("B");
                        Matrix result = Matrix.sum(m1, m2);
                        System.out.println("\nРезультат сложения:");
                        result.print();
                        break;
                    }
                    case 2: {
                        System.out.println("\n--- УМНОЖЕНИЕ МАТРИЦ (A * B) ---");
                        Matrix m1 = inputMatrix("A");
                        Matrix m2 = inputMatrix("B");
                        Matrix result = Matrix.multiply(m1, m2);
                        System.out.println("\nРезультат умножения:");
                        result.print();
                        break;
                    }
                    case 3: {
                        System.out.println("\n--- ДЕЛЕНИЕ МАТРИЦ (A / B = A * B^-1) ---");
                        Matrix m1 = inputMatrix("A");
                        Matrix m2 = inputMatrix("B");
                        Matrix result = Matrix.divide(m1, m2);
                        System.out.println("\nРезультат деления:");
                        result.print();
                        break;
                    }
                    case 4: {
                        System.out.println("\n--- ТРАНСПОНИРОВАНИЕ МАТРИЦЫ ---");
                        Matrix m = inputMatrix("A");
                        Matrix result = m.transpose();
                        System.out.println("\nТранспонированная матрица:");
                        result.print();
                        break;
                    }
                    case 5: {
                        System.out.println("\n--- ВЫЧИСЛЕНИЕ ОПРЕДЕЛИТЕЛЯ ---");
                        Matrix m = inputMatrix("A");
                        Complex det = m.determinant();
                        System.out.println("\nОпределитель матрицы: " + det);
                        break;
                    }
                    default:
                        System.out.println("Неверный пункт меню. Попробуйте снова.");
                }
            } catch (MatrixException e) {
                System.out.println("\n[Ошибка матрицы]: " + e.cause());
            } catch (Exception e) {
                System.out.println("\n[Непредвиденная ошибка]: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nМеню операций:");
        System.out.println("1. Сложить две матрицы");
        System.out.println("2. Перемножить две матрицы");
        System.out.println("3. Разделить две матрицы");
        System.out.println("4. Транспонировать матрицу");
        System.out.println("5. Вычислить определитель матрицы");
        System.out.println("0. Выход");
    }

    private static Matrix inputMatrix(String name) throws MatrixException {
        System.out.println("\nСоздание матрицы " + name + ":");
        int rows = readInt("  Введите количество строк: ");
        int cols = readInt("  Введите количество столбцов: ");

        Matrix matrix = new Matrix(rows, cols);
        System.out.println("  Введите элементы матрицы (действительная и мнимая части через пробел, например: 3 -2 для 3 - 2i):");

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("    Элемент [%d][%d] (Re Im): ", i + 1, j + 1);
                double re = readDouble();
                double im = readDouble();
                matrix.setElement(i, j, new Complex(re, im));
            }
        }

        System.out.println("Матрица " + name + " успешно создана:");
        matrix.print();
        return matrix;
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Ошибка! Введите целое число: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static double readDouble() {
        while (!scanner.hasNextDouble()) {
            System.out.print("Ошибка! Введите число (Re Im): ");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}