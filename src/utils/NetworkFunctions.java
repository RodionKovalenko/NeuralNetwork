package utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class NetworkFunctions {
    static double m[][];
    static double[][] identityM;
    static double[][] matrix;

    public static void printArray(double[][] a) {

        System.out.println("\nMatrix length: " + a.length + ", " + a[0].length);
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                System.out.print(a[i][j] + ", ");
            }
            System.out.println();
        }
        double max = 0;
        int index = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (max < a[i][j]) {
                    max = a[i][j];
                    if (Math.max(a.length, a[0].length) == a.length) {
                        index = i;
                    } else {
                        index = j;
                    }
                }
            }
        }
        System.out.println("max value: " + max + ", index: " + index);
    }

    public static void printArray(double[] a) {

        System.out.println("\nMatrix length: " + a.length);
        for (int i = 0; i < a.length; i++) {

            System.out.print(a[i] + ", ");
        }
        System.out.println();

    }

    public static double[][] activationSigmoidFunction(double[][] a) {
        double[][] result = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                result[i][j] = 1 / (1 + Math.exp(-1 * (a[i][j])));
            }
        }
        return result;
    }

    public static double[][] activationThresholdFunction(double[][] a, double threshold) {
        double[][] result = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] < threshold) {
                    result[i][j] = 0.0;
                } else {
                    result[i][j] = 1;
                }
            }
        }
        return result;
    }

    public static double[][] activationRelu(double[][] a) {
        double[][] result = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] < 0) {
                    result[i][j] = 0.0;
                } else {
                    result[i][j] = a[i][j];
                }
            }
        }
        return result;
    }

    public static double[][] activationSoftmax(double[][] a) {
        double[][] result = new double[a.length][a[0].length];
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                sum += Math.exp(a[i][j]);
            }
        }

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                result[i][j] = Math.exp(a[i][j]) / sum;
            }
        }

        return result;
    }

    public static double getMaxValue(double[][] a) {
        double max = a[0][0];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (max < a[i][j]) {
                    max = a[i][j];
                }
            }
        }
        return max;
    }

    public static double getMaxValue(double[] a) {
        double max = a[0];
        for (int i = 0; i < a.length; i++) {
            if (max < a[i]) {
                max = a[i];
            }
        }
        return max;
    }

    public static double getMinValue(double[][] a) {
        double min = a[0][0];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (min > a[i][j]) {
                    min = a[i][j];
                }
            }
        }
        return min;
    }

    public static double getMinValue(double[] a) {
        double min = a[0];
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (min > a[j]) {
                    min = a[j];
                }
            }
        }

        return min;
    }

    public static double[][] defineErrors(double[][] output, double[][] target) {
        double[][] result = new double[output.length][output[0].length];
        if (output.length == target.length & output[0].length == target[0].length) {
            for (int i = 0; i < output.length; i++) {
                for (int j = 0; j < output[i].length; j++) {
                    result[i][j] = Math.abs((target[i][j] - output[i][j]) / (target[0].length * target.length));
                }
            }


        } else {
            System.out.println("Size mismatch");
        }


        return result;
    }

    public static double[][] matrixMultiplyVector(double vektor, double[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] *= vektor;
            }
        }

        return a;
    }

    public static double[] matrixMultiplyVector(double vektor, double[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i] *= vektor;
        }

        return a;
    }

    public static double[][] matrixAddVector(double vektor, double[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                a[i][j] += vektor;
            }
        }

        return a;
    }

    public static double[][] matrixAdd(double[][] a, double[][] b) {
        // System.out.println("Adding of matrices");
        // System.out.println(a.length+", "+a[0].length);
        // System.out.println(b.length+", "+b[0].length);
        if (a.length == b.length & a[0].length == b[0].length) {
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[i].length; j++) {
                    a[i][j] += b[i][j];
                }
            }
        } else {
            System.out.println("Size dismatch");
        }

        return a;
    }

    public static double[] matrixAdd(double[] a, double[] b) {
        for (int i = 0; i < a.length; i++) {
            a[i] += b[i];
        }

        return a;
    }

    public static double[][] matrixMinusEinsVektor(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] = 1.0 - a[i][j];
            }
        }

        return a;
    }

    public static void printMatrixSize(double[][] a) {
        System.out.println("Matrix size: " + a.length + ", " + a[0].length);
    }

    // standardization of input

    public static double[][] transpose(double[][] a) {
        double[][] result = new double[a[0].length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                result[j][i] = a[i][j];
            }
        }

        return result;
    }

    public static double[] matrixAddNumber(double[] a, double number) {
        if (a.length == 0) {
            a = new double[1];
            a[0] = number;
            return a;
        }

        double[] result = new double[a.length + 1];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i];
        }
        result[a.length] = number;

        return result;
    }

    public static double matrixCosts(double[][] a) {
        double[][] result = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                result[i][j] = Math.pow(a[i][j], 2);
            }
        }

        return matrixSum(result) / 2;
    }

    public static double[][] matrixMultiply(double[][] m1, double[][] m2) {
        // System.out.print("\nMatrix 1: (" + m1.length + ", " + m1[0].length +
        // ")");
        // System.out.print("* Matrix 2: (" + m2.length + ", " + m2[0].length +
        // ")");

        double[][] result = new double[m1.length][m2[0].length];

        // System.out.println("==> Result: (" + m1.length + ", " + m2[0].length
        // + ")");
        if (m1[0].length != m2.length) {
            System.out.println("Multiply: Matrix size mismatch");
            return result;
        } else {
            for (int k = 0; k < m2.length; k++) {
                for (int i = 0; i < result.length; i++) {
                    for (int j = 0; j < result[i].length; j++) {
                        result[i][j] += m1[i][k] * m2[k][j];
                    }

                }
            }
        }

        return result;
    }

    public static double[][] matrixMultiply2(double[][] m1, double[][] m2) {
        // System.out.print("\nMatrix 1: (" + m1.length + ", " + m1[0].length +
        // ")");
        // System.out.print("* Matrix 2: (" + m2.length + ", " + m2[0].length +
        // ")");

        double[][] result = new double[m1.length][m2[0].length];

        // System.out.println("==> Result: (" + m1.length + ", " + m2[0].length
        // + ")");
        if (m1[0].length != m2.length) {
            System.out.println("Multiply: Matrix size mismatch");
            return result;
        } else {
                for (int i = 0; i < result.length; i++) {
                    for (int j = 0; j < result[i].length; j++) {
                        for (int k = 0; k < m2.length; k++) {
                        result[i][j] += m1[i][k] * m2[k][j];
                    }
                }
            }
        }

        return result;
    }


    public static double[][] matrixMultiplySameSize(double[][] m1, double[][] m2) {
        double[][] result = new double[m1.length][m1[0].length];

        if (m1.length == m2.length && m1[0].length == m2[0].length) {
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result[i].length; j++) {
                    result[i][j] = m1[i][j] * m2[i][j];
                }
            }

        } else {
            System.out.println("Size mismatch");
        }

        return result;
    }

    public static double[][] matrixMinusMatrix(double[][] t1, double[][] o2) {

        double[][] result = new double[t1.length][t1[0].length];
        if (t1.length == o2.length && t1[0].length == o2[0].length) {
            for (int i = 0; i < t1.length; i++) {
                for (int j = 0; j < t1[i].length; j++) {
                    result[i][j] = t1[i][j] - o2[i][j];
                }
            }
        } else {
            System.out.println("Size mismatch");
        }

        return result;
    }

    public static double[] matrixMinusMatrix(double[] m1, double[] m2) {

        double[] result = new double[m1.length];
        for (int i = 0; i < m1.length; i++) {
            result[i] = m1[i] - m2[i];
        }

        return result;
    }

    public static double[][] matrixAddVector(double[][] m1, double vector) {

        double[][] result = new double[m1.length][m1[0].length];

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[i].length; j++) {
                result[i][j] = m1[i][j] + vector;
            }
        }

        return result;
    }

    public static double[] matrixAddVector(double[] m1, double vector) {

        double[] result = new double[m1.length];

        for (int i = 0; i < m1.length; i++) {
            result[i] = m1[i] + vector;
        }

        return result;
    }

    public static double maxValue(double[][] a) {
        double max = 0;
        int index = 0;
        double max2 = 0;
        int index2 = 0;
        double max3 = 0;
        int index3 = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (max < a[i][j]) {
                    max = a[i][j];
                    index = i;
                }
                if (max2 < a[i][j] & max != a[i][j]) {
                    max2 = a[i][j];
                    index2 = i;
                }
                if (max3 < a[i][j] & max != a[i][j] & max2 != a[i][j]) {
                    max3 = a[i][j];
                    index3 = i;
                }

            }
        }
        System.out.println("Three max values are : " + max + ", index: " + (index + 1) + ", " + (index2 + 1) + ", "
                + (index3 + 1));
        return max;
    }

    public static double matrixSum(double[][] a) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                sum += a[i][j];
            }
        }

        return sum;
    }

    public static double matrixSum(double[] a) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
        }

        return sum;
    }

    public static double[] convertStringToNumbers(String s) {
        double[] result = new double[0];
        char[] c = s.toCharArray();
        String tmp = "";
        for (int i = 0; i < c.length; i++) {
            if ((int) c[i] >= 48 && (int) c[i] <= 57 || (int) c[i] == 46) {
                tmp += c[i];
            }
            if (c[i] == ' ' || i == c.length - 1) {
                if (tmp != "" && tmp != null) {

                    int count = 0;
                    char[] cTmp = tmp.toCharArray();
                    for (int j = 0; j < cTmp.length; j++) {
                        if (cTmp[j] == '.') {
                            count++;
                        }
                    }
                    if (count == 1 || count == 0) {
                        double number = Double.parseDouble(tmp);
                        result = matrixAddNumber(result, (double) number);
                    }
                    if (count == 2) {
                        System.out.println("Data are not structured properly");
                    }

                }
                tmp = "";

            }
        }

        return result;
    }

    public static double[][] preprocess(double[][] testX, double[][] testY) {
        for (int i = 0; i < testX.length; i++) {
            for (int j = 0; j < testX[0].length; j++) {
                for (int d = 0; d < testX.length; d++) {
                    for (int k = j + 1; k < testX[0].length - 1; k++) {
                        if (testX[i][j] == testX[d][k]) {
                            System.out.println(testX[i][j]);
                            testX[i][j] = 0;
                            testY[i][j] = 0;
                        }
                    }
                }
            }
        }

        double[] x1 = new double[]{};
        double[] y1 = new double[]{};

        for (int i = 0; i < testX.length; i++) {
            for (int j = 0; j < testX[0].length; j++) {
                if (testX[i][j] != 0) {
                    x1 = matrixAddNumber(x1, testX[i][j]);
                    y1 = matrixAddNumber(y1, testY[i][j]);
                }

            }
        }

        double[][] result = new double[][]{x1, y1};
        return result;

    }

    public static double[] convertToOneDimension(double[][] a) {
        double[] result = new double[a.length * a[0].length];
        int count = 0;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                result[count] = a[i][j];
                count++;
            }
        }
        return result;
    }

    public static String insertData(double[][] a, String name, int i) {

        String s = name + i + "=np.array([[";
        for (int k = 0; k < a.length; k++) {
            for (int d = 0; d < a[0].length; d++) {
                if (k > 0 && d == 0) {
                    s += "[";
                }
                if (k == a.length - 1 && d == a[0].length - 1) {
                    s += a[k][d] + "]])\n";
                } else if (d != a[0].length - 1) {
                    s += a[k][d] + ",";
                } else {
                    s += a[k][d];
                }
            }
            if (k != a.length - 1) {
                s += "],";
            }

        }
        return s;
    }

    public static double[][] matrixTransformLength(double[][] a) {
        double[][] aTemp = new double[a.length - 1][a[0].length - 1];
        for (int i = 1; i < a.length; i++) {
            for (int j = 1; j < a[0].length; j++) {
                if (i != 0 && j != 0)
                    aTemp[i - 1][j - 1] = a[i][j];

            }
        }
        return aTemp;
    }


    public static double rekursiveDeterminant(double[][] a) {
        double finalResult = 0;
        int count = 0;

        if (a.length > 4 & a[0].length > 4) {
            double[][] m3 = new double[a.length - 1][a[0].length - 1];
            for (int s = 0; s < m3.length; s++) {
                for (int s1 = 0; s1 < m3[0].length; s1++) {
                    m3[s][s1] = Double.POSITIVE_INFINITY;
                }
            }

            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < a[0].length; j++) {
                    for (int d = 0; d < a.length; d++) {
                        for (int k = 0; k < a[0].length; k++) {
                            if (d != i && k != j) {
                                if (count < (a.length - 1) * (a[0].length - 1)) {
                                    h:
                                    for (int c1 = 0; c1 < m3.length; c1++) {
                                        for (int c2 = 0; c2 < m3[0].length; c2++) {
                                            if (m3[c1][c2] == Double.POSITIVE_INFINITY) {
                                                m3[c1][c2] = a[d][k];
                                                count++;
                                                break h;
                                            }
                                        }

                                    }

                                } else {
                                    m3 = new double[a.length - 1][a[0].length - 1];
                                    for (int s = 0; s < m3.length; s++) {
                                        for (int s1 = 0; s1 < m3[0].length; s1++) {
                                            m3[s][s1] = Double.POSITIVE_INFINITY;
                                        }
                                    }
                                    count = 0;
                                    k = 0;
                                    d = 0;
                                }

                            }

                        }
                    }
                    //  NetworkFunctions.printArray(m3);
                    if (m3.length > 4) {
                        switch ((i + j) % 2) {
                            case 0:
                                finalResult += a[i][j] * rekursiveDeterminant(m3);
                                //   System.out.println("case 0: " + finalResult);
                                break;
                            case 1:
                                finalResult += -1 * a[i][j] * rekursiveDeterminant(m3);
                                //          System.out.println("case 1: " + finalResult);
                                break;

                        }

                    } else {
                        switch ((i + j) % 2) {
                            case 0:
                                finalResult += a[i][j] * determinant4x4(m3);
                                //    System.out.println("case 0 Matrix 4*4: " + finalResult);
                                break;
                            case 1:
                                finalResult += -1 * a[i][j] * determinant4x4(m3);
                                //      System.out.println("case 1 Matrix 4*4: " + finalResult);
                                break;
                        }

                    }
                    // NetworkFunctions.printArray(m3);


                }


            }


        } else {
            finalResult += determinant4x4(a);
        }

        //System.out.println("Determinant Final: " + finalResult);

        return finalResult;
    }


    public static double determinant4x4(double[][] a) {
        double finalResult = 0;
        int count = 0;
        if (a.length == 4 & a[0].length == 4) {
            double[][] m3 = new double[a.length - 1][a[0].length - 1];
        //    m3 = fillInArray(m3);
            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < a[0].length; j++) {
                    for (int d = 0; d < a.length; d++) {
                        for (int k = 0; k < a[0].length; k++) {
                            if (d != i && k != j) {
                                if (count < (a.length - 1) * (a[0].length - 1)) {
                                    h:
                                    for (int c1 = 0; c1 < m3.length; c1++) {
                                        for (int c2 = 0; c2 < m3[0].length; c2++) {
                                            if (m3[c1][c2] == Double.POSITIVE_INFINITY) {
                                                m3[c1][c2] = a[d][k];
                                                count++;
                                                break h;
                                            }
                                        }
                                    }

                                } else {
                                    m3 = new double[a.length - 1][a[0].length - 1];
                                    for (int s = 0; s < m3.length; s++) {
                                        for (int s1 = 0; s1 < m3[0].length; s1++) {
                                            m3[s][s1] = Double.POSITIVE_INFINITY;
                                        }
                                    }
                                    count = 0;
                                    k = 0;
                                    d = 0;
                                }

                            }

                        }
                    }
                    switch ((i + j) % 2) {
                        case 0:
                            finalResult += a[i][j] * determinant3x3(m3);
                            //    System.out.println("case 0: " + finalResult);
                            break;
                        case 1:
                            finalResult += -a[i][j] * determinant3x3(m3);
                            //        System.out.println("case 1: " + finalResult);
                            break;
                    }

                }

            }

        } else {
            finalResult += determinant3x3(a);
        }


        //   System.out.println("Determinant Final: " + finalResult);

        return finalResult;
    }

    public static double determinant3x3(double[][] a) {
        double result = 0;
        if (a.length == 2 && a[0].length == 2) {
            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < a[0].length; j++) {
                    result = a[i][j] * a[i + 1][j + 1] - a[i][j + 1] * a[i + 1][j];
                    break;
                }
            }
            //  System.out.println("Determinant: " + result);
            return result;
        }


        if (a.length == 3 && a[0].length == 3) {
            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < a[0].length; j++) {
                    switch ((i + j) % 2) {
                        case 0:
                            double tmp = a[i][j];
                            double s = 0;
                            for (int d = 0; d < a.length - 1; d++) {
                                for (int k = 0; k < a[0].length - 1; k++) {
                                    if (d != i && k != j) {
                                        s = (a[d][k] * a[d + 1][k + 1] - a[d][k + 1] * a[d + 1][k]);
                                        result += tmp * (a[d][k] * a[d + 1][k + 1] - a[d][k + 1] * a[d + 1][k]);
                                        //  System.out.println("Result case 0 " + result + ", temp " + tmp + ", s: " + s);
                                        break;
                                    }
                                }
                            }

                            break;
                        case 1:
                            tmp = a[i][j];
                            s = 0;
                            for (int d = 0; d < a.length - 1; d++) {
                                for (int k = 0; k < a[0].length - 1; k++) {
                                    if (d != i && k != j) {
                                        if (k + 1 == j) {
                                            result += -1 * tmp * (a[d][k] * a[d + 1][k + 2] - a[d][k + 2] * a[d + 1][k]);
                                        } else {
                                            s = (a[d][k] * a[d + 1][k + 1] - a[d][k + 1] * a[d + 1][k]);
                                            result += -1 * tmp * (a[d][k] * a[d + 1][k + 1] - a[d][k + 1] * a[d + 1][k]);
                                            //        System.out.println("Result case 1 " + result + ", temp " + tmp + ", s: " + s);
                                        }
                                        break;
                                    }
                                }
                            }

                            break;
                    }
                }
            }
        }
        // System.out.println("Determinant: " + result);
        return result;
    }

    public static double[][] pseudoInverse(double[][] a) {

        double[][] matrixProdukt = NetworkFunctions.matrixMultiply(NetworkFunctions.transpose(a), a);
        double[][] result = inverseMatrixIdentity(matrixProdukt);
        result = NetworkFunctions.matrixMultiply(result, NetworkFunctions.transpose(a));
        return result;
    }

    public static double[][] inverseMatrix(double[][] a) {

        double[][] inverseMatrix = new double[][]{{}};
        double[][] matrixProdukt = a;
        double determinant = NetworkFunctions.rekursiveDeterminant(matrixProdukt);

        // double determinant =determinant(matrixProdukt, matrixProdukt.length);
        System.out.println("Determinant : " + determinant);


        if (determinant != 0) {
            double[][] adjunction = new double[matrixProdukt.length][matrixProdukt[0].length];

            adjunction = fillInArray(adjunction);
            for (int i = 0; i < matrixProdukt.length; i++) {
                for (int j = 0; j < matrixProdukt[0].length; j++) {
                    h:
                    for (int k = 0; k < matrixProdukt.length; k++) {
                        for (int d = 0; d < matrixProdukt[0].length; d++) {
                            if (adjunction[k][d] == Double.MAX_VALUE) {
                                adjunction[k][d] = Math.pow(-1, i + j) * subDeterminant(matrixProdukt, j, i);
                                break h;
                            }
                        }
                    }

                }
            }

            inverseMatrix = NetworkFunctions.matrixMultiplyVector(1.0 / determinant, adjunction);


        } else {
            System.out.println("Determinant is null ***********************");
        }
        System.out.println("Inverse Matrix");
        //  NetworkFunctions.printArray(inverseMatrix);

        String s = NetworkFunctions.insertData(matrixProdukt, "test", 1);
        try {
            Files.write(Paths.get("testArray.txt"), s.getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {

        }

        return inverseMatrix;
    }


    public static double subDeterminant(double[][] a, int indI, int indJ) {
        double result = 0;


        double[][] m = new double[a.length - 1][a[0].length - 1];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j] = Double.MAX_VALUE;
            }
        }

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                h:
                for (int k = 0; k < m.length; k++) {
                    for (int d = 0; d < m[0].length; d++) {
                        if (m[k][d] == Double.MAX_VALUE) {
                            if (i != indI & j != indJ) {
                                m[k][d] = a[i][j];
                                break h;
                            }

                        }
                    }
                }
            }
        }
        result = rekursiveDeterminant(m);


        return result;
    }


    public static double[][] fillInArray(double[][] m) {

        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        return m;
    }


    public static void initializeIdentity(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                if (i == j) {
                    a[i][j] = 1;
                } else {
                    a[i][j] = 0;
                }
            }
        }

    }

    public static double[][] cloneArray(double[][] a) {
        double[][] result = new double[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                result[i][j] = a[i][j];
            }

        }
        return result;

    }

    public static double[][] inverseMatrixIdentity(double[][] a) {
        identityM = new double[a.length][a[0].length];
        initializeIdentity(identityM);
        matrix = cloneArray(a);
        double[][] result = new double[][]{{}};

        if (a.length != a[0].length) {
            System.out.println("Matrix size must be squared");
            return result;
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (i == j && matrix[i][j] != 0) {
                    double tmp = matrix[i][j];
                    processMatrixDiagonal(i, tmp);
                }

            }
        }


        int count = 9;
        while (count < 10) {
            for (int i = 0; i < a.length; i++) {
                matrix = rowManipulation(matrix[i], i);
            }
            count++;
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (i == j && matrix[i][j] != 0) {
                    double tmp = matrix[i][j];
                    processMatrixDiagonal(i, tmp);
                }

            }
        }

//        printArray(matrix);
//        printArray(identityM);

        return identityM;
    }


    public static void processMatrixDiagonal(int index, double value) {
        for (int i = 0; i < matrix.length; i++) {
            matrix[index][i] /= value;
            identityM[index][i] /= value;
        }
    }


    public static double[][] rowManipulation(double[] a, int i2) {
        double a2[] = matrix[i2];
        double multiplierA = 0;
        double multiplierB = 0;
        for (int j = 0; j < a.length; j++) {
            if (j == i2 && matrix[i2][j] != 0) {
                for (int k = 0; k < a.length; k++) {
                    if (k != i2) {
                        if (matrix[k][j] != 0) {
                            multiplierA = matrix[i2][j];
                            multiplierB = matrix[k][j];
                            multiplyRows(matrix[i2], matrix[k], multiplierA, multiplierB, i2, k);
                            //    printArray(matrix);

                        }
                    }
                }
            }
        }

        return matrix;
    }


    public static void multiplyRows(double[] a, double[] b, double multiplierA,
                                    double multiplierB, int i1, int i2) {

        double[] m1 = identityM[i1];
        double[] m2 = identityM[i2];
        for (int i = 0; i < a.length; i++) {
            b[i] = multiplierA * b[i] - multiplierB * a[i];
            m2[i] = multiplierA * m2[i] - multiplierB * m1[i];
        }

        matrix[i2] = b;
        identityM[i2] = m2;
    }





}