import java.util.Scanner;
import matrix.Complex;
import matrix.ComplexMatrix;
public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Операции с комплексными матрицами ---");
            System.out.println("1. Сложение (A + B)");
            System.out.println("2. Умножение (A * B)");
            System.out.println("3. Деление (A / B)");
            System.out.println("4. Транспонирование (A^T)");
            System.out.println("5. Определитель (det A)");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            if (choice == 0) break;

            try {
                switch (choice) {
                    case 1 -> {
                        ComplexMatrix a = readMatrix("A");
                        ComplexMatrix b = readMatrix("B");
                        System.out.println("\nРезультат A + B:");
                        a.add(b).print();
                    }
                    case 2 -> {
                        ComplexMatrix a = readMatrix("A");
                        ComplexMatrix b = readMatrix("B");
                        System.out.println("\nРезультат A * B:");
                        a.multiply(b).print();
                    }
                    case 3 -> {
                        ComplexMatrix a = readMatrix("A");
                        ComplexMatrix b = readMatrix("B");
                        System.out.println("\nРезультат A / B (A * B^-1):");
                        a.divide(b).print();
                    }
                    case 4 -> {
                        ComplexMatrix a = readMatrix("A");
                        System.out.println("\nТранспонированная матрица:");
                        a.transpose().print();
                    }
                    case 5 -> {
                        ComplexMatrix a = readMatrix("A");
                        System.out.println("\nОпределитель матрицы: " + a.determinant());
                    }
                    default -> System.out.println("Неизвестный пункт меню.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка при выполнении операции: " + e.getMessage());
            }
        }
    }

    private static ComplexMatrix readMatrix(String name) {
        System.out.printf("Введите размеры матрицы %s (строки столбцы): ", name);
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();

        ComplexMatrix m = new ComplexMatrix(rows, cols);
        System.out.println("Введите элементы (для каждого числа два значения: Re и Im):");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("[%d][%d] Re Im: ", i + 1, j + 1);
                double re = scanner.nextDouble();
                double im = scanner.nextDouble();
                m.set(i, j, new Complex(re, im));
            }
        }
        return m;
    }
}