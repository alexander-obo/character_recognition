package ao.util;

import java.awt.image.WritableRaster;

public class ZongSyn {

    public static WritableRaster skeletonization(int[][] matrix, WritableRaster binarizedData) {
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
     p8	p1 p2
     p7	p  p3
     p6	p5 p4
     */
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

    // number of zero-to-one transitions in the order sequence p1p2...p8p1
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

    private static int[][] corruptSkeleton(int[][] matrix) {
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                try {
                    if (isJoint(matrix, x, y)) {
                        matrix[y][x] = 0;
                        matrix[y - 1][x] = 0;
                        matrix[y - 1][x + 1] = 0;
                        matrix[y][x + 1] = 0;
                        matrix[y + 1][x + 1] = 0;
                        matrix[y + 1][x] = 0;
                        matrix[y + 1][x - 1] = 0;
                        matrix[y][x - 1] = 0;
                        matrix[y - 1][x - 1] = 0;
                    }
                } catch (IndexOutOfBoundsException ex) {
                    // stub
                }
            }
        }
        return matrix;
    }

    private static boolean isJoint(int[][] matrix, int x, int y) {
        boolean isJoint = false;
        int p = matrix[y][x];
        int p1 = matrix[y - 1][x];
        int p2 = matrix[y - 1][x + 1];
        int p3 = matrix[y][x + 1];
        int p4 = matrix[y + 1][x + 1];
        int p5 = matrix[y + 1][x];
        int p6 = matrix[y + 1][x - 1];
        int p7 = matrix[y][x - 1];
        int p8 = matrix[y - 1][x - 1];
//     p8   p1  p2
//     p7   p   p3
//     p6   p5  p4
        boolean b1 = p1 == 1 && p3 == 1 && p5 == 1 && p == 1;
        boolean b2 = p1 == 1 && p7 == 1 && p5 == 1 && p == 1;
        boolean b3 = p7 == 1 && p5 == 1 && p3 == 1 && p == 1;
        boolean b4 = p7 == 1 && p1 == 1 && p3 == 1 && p == 1;

        boolean b5 = p1 == 1 && p == 1 && p3 == 1;
        boolean b6 = p3 == 1 && p == 1 && p5 == 1;
        boolean b7 = p5 == 1 && p == 1 && p7 == 1;
        boolean b8 = p7 == 1 && p == 1 && p1 == 1;
        if (b1 || b2 || b3 || b4 || b5 || b6 || b7 || b8) {
            isJoint = true;
        }
        return isJoint;
    }

    public static WritableRaster corruptSkeletonization(int[][] matrix, WritableRaster binarizedData) {
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
        matrix = corruptSkeleton(matrix);
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
}
