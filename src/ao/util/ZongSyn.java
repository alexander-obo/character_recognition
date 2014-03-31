package ao.util;

import java.awt.image.WritableRaster;

public class ZongSyn {

    public static WritableRaster skeletonizaciya(int[][] matrix, WritableRaster binarizedData) {
        boolean isPixelRemoved;
        int[][] parallelMatrix = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            parallelMatrix[i] = matrix[i].clone();
        }
        do {
            isPixelRemoved = false;
            for (int y = 1; y < matrix.length - 1; y++) {
                for (int x = 1; x < matrix[y].length - 1; x++) {
                    if (matrix[y][x] == 0) {
                        continue;
                    }
                    if (isPointBelongsToContour(matrix, x, y)) {
                        boolean c1 = condition1(matrix, x, y);
                        boolean c2 = condition2(matrix, x, y);
                        boolean c3a = condition3a(matrix, x, y);
                        boolean c4a = condition4a(matrix, x, y);
                        if (c1 && c2 && c3a && c4a) {
                            parallelMatrix[y][x] = 0;
                            isPixelRemoved = true;
                        }
                    }
                }
            }
            for (int y = 0; y < matrix.length; y++) {
                for (int x = 0; x < matrix[y].length; x++) {
                    if (matrix[y][x] == 0) {
                        continue;
                    }
                    if (isPointBelongsToContour(matrix, x, y)) {
                        boolean c1 = condition1(matrix, x, y);
                        boolean c2 = condition2(matrix, x, y);
                        boolean c3b = condition3b(matrix, x, y);
                        boolean c4b = condition4b(matrix, x, y);
                        if (c1 && c2 && c3b && c4b) {
                            parallelMatrix[y][x] = 0;
                            isPixelRemoved = true;
                        }
                    }
                }
            }
            for (int i = 0; i < parallelMatrix.length; i++) {
                matrix[i] = parallelMatrix[i].clone();
            }
        } while (isPixelRemoved);
        int[] blackPixel = {0, 0, 0};
        int[] whitePixel = {255, 255, 255};
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                if (matrix[y][x] == 0) {
                    binarizedData.setPixel(x, y, whitePixel);
                } else {
                    binarizedData.setPixel(x, y, blackPixel);
                }
            }
        }
        return binarizedData;
    }

    public static WritableRaster skeletonizaciya2(int[][] matrix, WritableRaster binarizedData) {
        boolean isPixelRemoved;
        int[][] parallelMatrix = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            parallelMatrix[i] = matrix[i].clone();
        }
        do {
            isPixelRemoved = false;
            for (int y = 1; y < matrix.length - 1; y++) {
                for (int x = 1; x < matrix[y].length - 1; x++) {
                    if (matrix[y][x] == 0) {
                        continue;
                    }
                    if (isPointBelongsToContour(matrix, x, y)) {
                        int a = A(matrix, x, y);
                        int b = B(matrix, x, y);
                        int c = C(matrix, x, y);
                        int d = D(matrix, x, y);
                        if ((a == 1)
                                && ((2 <= b) && (b <= 6))
                                && (c == 0)
                                && (d == 0)) {
                            parallelMatrix[y][x] = 0;
                            isPixelRemoved = true;
                        }
                    }
                }
            }
            for (int i = 0; i < parallelMatrix.length; i++) {
                matrix[i] = parallelMatrix[i].clone();
            }
            for (int y = 0; y < matrix.length; y++) {
                for (int x = 0; x < matrix[y].length; x++) {
                    if (matrix[y][x] == 0) {
                        continue;
                    }
                    if (isPointBelongsToContour(matrix, x, y)) {
                        int a = A(matrix, x, y);
                        int b = B(matrix, x, y);
                        int c2 = C2(matrix, x, y);
                        int d2 = D2(matrix, x, y);
                        if ((a == 1)
                                && ((2 <= b) && (b <= 6))
                                && (c2 == 0)
                                && (d2 == 0)) {
                            parallelMatrix[y][x] = 0;
                            isPixelRemoved = true;
                        }
                    }
                }
            }
            for (int i = 0; i < parallelMatrix.length; i++) {
                matrix[i] = parallelMatrix[i].clone();
            }
        } while (isPixelRemoved);
        int[] blackPixel = {0, 0, 0};
        int[] whitePixel = {255, 255, 255};
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                if (matrix[y][x] == 0) {
                    binarizedData.setPixel(x, y, whitePixel);
                } else {
                    binarizedData.setPixel(x, y, blackPixel);
                }
            }
        }
        return binarizedData;
    }

    /*
     P9	P2 P3
     P8	P1 P4
     P7	P6 P5
     */
    //2 <= P2+P3+...+P8+P9 <= 6
    private static boolean condition1(int[][] matrix, int x, int y) {
        int p2 = matrix[y - 1][x];
        int p3 = matrix[y - 1][x + 1];
        int p4 = matrix[y][x + 1];
        int p5 = matrix[y + 1][x + 1];
        int p6 = matrix[y + 1][x];
        int p7 = matrix[y + 1][x - 1];
        int p8 = matrix[y][x - 1];
        int p9 = matrix[y - 1][x - 1];
        int count = p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9;
        boolean result = (2 <= count) && (count <= 6);
        return result;
    }

    /*
     S(P1) = 1
     S(P1) - количество найденных последовательностей 01 в последовательности P1, P2, P3, P4, P5, P6, P7, P8, P9
     Т.е. для удаления пикселя, вокруг него должен существовать только один переход от нуля к единице.
     ТУТ МОЖЕТ БЫТЬ ОШИБКА
     */
    private static boolean condition2(int[][] matrix, int x, int y) {
        int count = 0;
        int p1 = matrix[y][x];
        int p2 = matrix[y - 1][x];
        int p3 = matrix[y - 1][x + 1];
        int p4 = matrix[y][x + 1];
        int p5 = matrix[y + 1][x + 1];
        int p6 = matrix[y + 1][x];
        int p7 = matrix[y + 1][x - 1];
        int p8 = matrix[y][x - 1];
        int p9 = matrix[y - 1][x - 1];
        //P1-P2
        if (p1 == 0 && p2 == 1) {
            count++;
        }
        //P2-P3
        if (p2 == 0 && p3 == 1) {
            count++;
        }
        //P3-P4
        if (p3 == 0 && p4 == 1) {
            count++;
        }
        //P4-P5
        if (p4 == 0 && p5 == 1) {
            count++;
        }
        //P5-P6
        if (p5 == 0 && p6 == 1) {
            count++;
        }
        //P6-P7
        if (p6 == 0 && p7 == 1) {
            count++;
        }
        //P7-P8
        if (p7 == 0 && p8 == 1) {
            count++;
        }
        //P8-P9   
        if (p8 == 0 && p9 == 1) {
            count++;
        }
        return count == 1;
    }

    // P2*P4*P6 = 0
    private static boolean condition3a(int matrix[][], int x, int y) {
        int p2 = matrix[y - 1][x];
        int p4 = matrix[y][x + 1];
        int p6 = matrix[y + 1][x];
        int product = p2 * p4 * p6;
        boolean result = product == 0;
        return result;
    }

    // P4*P6*P8 = 0
    private static boolean condition4a(int matrix[][], int x, int y) {
        int p4 = matrix[y][x + 1];
        int p6 = matrix[y + 1][x];
        int p8 = matrix[y][x - 1];
        int product = p4 * p6 * p8;
        boolean result = product == 0;
        return result;
    }

    // вторая подитерация
    // P2*P4*P8 = 0
    private static boolean condition3b(int matrix[][], int x, int y) {
        int p2 = matrix[y - 1][x];
        int p4 = matrix[y][x + 1];
        int p8 = matrix[y][x - 1];
        int product = p2 * p4 * p8;
        boolean result = product == 0;
        return result;
    }

    // P2*P6*P8 = 0
    private static boolean condition4b(int matrix[][], int x, int y) {
        int p2 = matrix[y - 1][x];
        int p6 = matrix[y + 1][x];
        int p8 = matrix[y][x - 1];
        int product = p2 * p6 * p8;
        boolean result = product == 0;
        return result;
    }

    private static boolean isPointBelongsToContour(int matrix[][], int x, int y) {
        int p2 = matrix[y - 1][x];
        int p3 = matrix[y - 1][x + 1];
        int p4 = matrix[y][x + 1];
        int p5 = matrix[y + 1][x + 1];
        int p6 = matrix[y + 1][x];
        int p7 = matrix[y + 1][x - 1];
        int p8 = matrix[y][x - 1];
        int p9 = matrix[y - 1][x - 1];
        boolean result = (p2 == 0)
                || (p3 == 0)
                || (p4 == 0)
                || (p5 == 0)
                || (p6 == 0)
                || (p7 == 0)
                || (p8 == 0)
                || (p9 == 0);
        return result;
    }

    /*
     p8	p1 p2
     p7	p  p3
     p6	p5 p4
     */
    // number of nonzero 8-neighbors
    private static int B(int matrix[][], int x, int y) {
        int p1 = matrix[y - 1][x];
        int p2 = matrix[y - 1][x + 1];
        int p3 = matrix[y][x + 1];
        int p4 = matrix[y + 1][x + 1];
        int p5 = matrix[y + 1][x];
        int p6 = matrix[y + 1][x - 1];
        int p7 = matrix[y][x - 1];
        int p8 = matrix[y - 1][x - 1];
        int result = p1 + p2 + p3 + p4 + p5 + p6 + p7 + p8;
        return result;
    }

    // number of zeto-to-one transitions in the order sequence p1p2...p8p1
    private static int A(int matrix[][], int x, int y) {
        int p1 = matrix[y - 1][x];
        int p2 = matrix[y - 1][x + 1];
        int p3 = matrix[y][x + 1];
        int p4 = matrix[y + 1][x + 1];
        int p5 = matrix[y + 1][x];
        int p6 = matrix[y + 1][x - 1];
        int p7 = matrix[y][x - 1];
        int p8 = matrix[y - 1][x - 1];
        int result = 0;
        if (p1 == 0 && p2 == 1) {
            result++;
        }
        if (p2 == 0 && p3 == 1) {
            result++;
        }
        if (p3 == 0 && p4 == 1) {
            result++;
        }
        if (p4 == 0 && p5 == 1) {
            result++;
        }
        if (p5 == 0 && p6 == 1) {
            result++;
        }
        if (p6 == 0 && p7 == 1) {
            result++;
        }
        if (p7 == 0 && p8 == 1) {
            result++;
        }
        if (p8 == 0 && p1 == 1) {
            result++;
        }
        return result;
    }

    // p1 * p3 * p5
    private static int C(int[][] matrix, int x, int y) {
        int p1 = matrix[y - 1][x];
        int p3 = matrix[y][x + 1];
        int p5 = matrix[y + 1][x];
        int result = p1 * p3 * p5;
        return result;
    }

    // p3 * p5 * p7
    private static int D(int[][] matrix, int x, int y) {
        int p3 = matrix[y][x + 1];
        int p5 = matrix[y + 1][x];
        int p7 = matrix[y][x - 1];
        int result = p3 * p5 * p7;
        return result;
    }

    // p1 * p3 * p7
    private static int C2(int[][] matrix, int x, int y) {
        int p1 = matrix[y - 1][x];
        int p3 = matrix[y][x + 1];
        int p7 = matrix[y][x - 1];
        int result = p1 * p3 * p7;
        return result;
    }

    // p1 * p5 * p7
    private static int D2(int[][] matrix, int x, int y) {
        int p1 = matrix[y - 1][x];
        int p5 = matrix[y + 1][x];
        int p7 = matrix[y][x - 1];
        int result = p1 * p5 * p7;
        return result;
    }
}
