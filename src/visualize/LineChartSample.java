package visualize;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.awt.*;
import java.lang.reflect.Method;
import java.io.*;
import java.util.concurrent.TimeUnit;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import javafx.animation.PauseTransition;
import javafx.application.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import networks.Adaline;
import networks.Data;
import networks.Network;

import utils.NetworkFunctions;
import networks.NeuralNetworkWith3Layers;
import utils.Statistics;

import javax.swing.*;
import javax.swing.text.Position;
import java.io.File;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;


public class LineChartSample extends Application {
    private Desktop desktop = Desktop.getDesktop();

    static double[][] a;
    static double[][] b;
    static double[][] c;
    static double[][] d;
    static double[][] t1;
    static double[][] t2;
    static String label = "";
    static double rX;
    static double rY;
    public static Stage s;
    static int indexTmp;

    static XYChart.Series series1;
    static XYChart.Series series2;
    static XYChart.Series series3;
    public static Network network;
//    public static LineChart<Number, Number> lc = new LineChart<Number, Number>(new NumberAxis(0, 800, 10), new NumberAxis(0, 20, 1));

    @Override
    public void start(Stage stage) {
        s = stage;

        // the wumpus doesn't leave when the last stage is hidden.
        final NumberAxis xAxis = new NumberAxis(0, rX, 10);
        final NumberAxis yAxis = new NumberAxis(0, rY, 1);
        xAxis.setLabel("Quantity");
        yAxis.setLabel("Price");
        //  final ScatterChart<Number, Number> sc = new ScatterChart<>(xAxis, yAxis);
        final LineChart<Number, Number> lc = new LineChart<>(xAxis, yAxis);
        lc.setTitle(label);


        lc.setTitle(label);
        series1 = new XYChart.Series();

        series1.setName("Trained Data");
        // populating the series with data
        if (a != null && b != null) {
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[i].length; j++) {
                    series1.getData().add(new XYChart.Data(a[i][j], b[i][j]));
                }
            }
        }
        series2 = new XYChart.Series();
        series2.setName("Original Data");
        if (c != null && d != null) {
            for (int i = 0; i < c.length; i++) {
                for (int j = 0; j < c[i].length; j++) {
                    series2.getData().add(new XYChart.Data(c[i][j], d[i][j]));
                }
            }
        }

        series3 = new XYChart.Series();
        series3.setName("Test Data");
        if (t1 != null && t2 != null) {
            for (int i = 0; i < t1.length; i++) {
                for (int j = 0; j < t1[i].length; j++) {
                    series3.getData().add(new XYChart.Data(t1[i][j], t2[i][j]));
                }
            }
        }


        lc.getData().addAll(series1, series2, series3);
        lc.setStyle("-fx-font-size: " + 12 + "px;");
        lc.setStyle("-fx-font-size: " + 12 + "px;");

        Scene scene = new Scene(lc, 1000, 700);
        stage.setScene(scene);

        stage.show();

//        try {
//            final FileChooser fileChooser = new FileChooser();
//
//            File file = fileChooser.showOpenDialog(stage);
//            if (file != null) {
//                desktop.open(file);
//            }
//        } catch (Exception e) {
//            e.printStackTrace(System.err);
//        }

        final double[] counts = new double[Data.mengen.length];

        Scanner s = new Scanner(System.in);
        Thread thread2 = new Thread((new Runnable() {
            public void run() {

                try {
                    Thread.sleep(900);
                } catch (InterruptedException exc) {
                    throw new Error("Unexpected interruption", exc);
                } finally {


                    Thread thread = new Thread((new Runnable() {
                        @Override
                        public void run() {
                            int tmp = 0;
                            int time = 2000;
                            CountDownLatch latch = new CountDownLatch(30);
                            while (tmp < Data.mengen.length) {
                                tmp++;
                                try {
                                    Thread.sleep(time);
                                } catch (Exception exc) {
                                    throw new Error("Unexpected interruption", exc);
                                }
                                time = 3000;
                                // Update text on FX Application Thread:
                                Platform.runLater(() -> {


                                    int index = 0;
                                    for (int k = 0; k < counts.length; k++) {
                                        if (counts[k] == 0) {
                                            counts[k] = -1;
                                            index = k;
                                            break;
                                        }
                                    }


                                    boolean show;
                                    series1.getData().clear();
                                    series2.getData().clear();
                                    series3.getData().clear();


                                    Button button1 = new Button("Previous chart " + (indexTmp));
                                    Button button2 = new Button("Pause for 6 seconds");
                                    Button button3 = new Button("Next chart " + (indexTmp + 2));


                                    button1.setOnAction((actionEvent) -> {
                                        if (indexTmp - 1 >= 0 && indexTmp - 1 < Data.mengen.length) {
                                            indexTmp -= 1;
                                        }
                                        button1.setText("Previous chart " + (indexTmp + 1));
                                        System.out.println("''''''''''''''''''''''''''''''''''''''");
                                        System.out.println(indexTmp);
                                        System.out.println("''''''''''''''''''''''''''''''''''''''");

                                    });

                                    button2.setOnAction((actionEvent) -> {
                                        try {
                                            Thread.sleep(6000);
                                        } catch (Exception e) {
                                        }
                                    });

                                    button3.setOnAction((actionEvent) -> {
                                        if (indexTmp + 1 >= 0 && indexTmp + 1 < Data.mengen.length) {
                                            indexTmp += 1;
                                        }
                                        button3.setText("Next chart " + (indexTmp + 1));
                                        System.out.println("''''''''''''''''''''''''''''''''''''''");
                                        System.out.println(indexTmp);
                                        System.out.println("''''''''''''''''''''''''''''''''''''''");
                                    });

                                    index = indexTmp;

                                    String m = "";
                                    String p = "";

                                    m = Data.mengen[indexTmp];
                                    p = Data.preise[indexTmp];
                                    c = new double[][]{NetworkFunctions.convertStringToNumbers(m)};
                                    d = new double[][]{NetworkFunctions.convertStringToNumbers(p)};


                                    show = true;


                                    final NumberAxis xAxis2 = new NumberAxis(0, NetworkFunctions.getMaxValue(c) + 5, 10);
                                    final NumberAxis yAxis2 = new NumberAxis(0, NetworkFunctions.getMaxValue(d) + 5, 1);
                                    xAxis2.setLabel("Quantity");
                                    yAxis2.setLabel("Price");
                                    final LineChart<Number, Number> lc2 = new LineChart<>(xAxis2, yAxis2);


                                    if (network.type != null) {
                                        lc2.setTitle("Sample Number " + (indexTmp + 1) + ", normalisation type " + network.type + ", " + label);
                                        if (network.type == Statistics.Normalisation.STANDARDIZE) {
                                            network.trainAndTest(c, d, network.type, 1, 1, new String[]{"22"}, "s" + (indexTmp + 1), false);
                                        } else if (network.type == Statistics.Normalisation.NORMALIZEMAXMIN) {
                                            network.trainAndTest(c, d, network.type, 1, 1, new String[]{"22"}, "m" + (indexTmp + 1), false);
                                        } else if (network.type == Statistics.Normalisation.NORMALIZECUMULATIVE) {
                                            network.trainAndTest(c, d, network.type, 1, 1, new String[]{"22"}, "c" + (indexTmp + 1), false);

                                        }
                                    } else {
                                        lc2.setTitle("Sample Number " + (indexTmp + 1) + ", " + label);
                                        network.trainAndTest(c, d, false, null, index + 1);
                                    }

                                    series1 = new XYChart.Series();

                                    series1.setName("Trained Data");
                                    // populating the series with data
                                    if (a != null && b != null) {
                                        for (int i = 0; i < a.length; i++) {
                                            for (int j = 0; j < a[i].length; j++) {
                                                series1.getData().add(new XYChart.Data(a[i][j], b[i][j]));
                                            }
                                        }
                                    }
                                    series2 = new XYChart.Series();
                                    series2.setName("Original Data");
                                    if (c != null && d != null) {
                                        for (int i = 0; i < c.length; i++) {
                                            for (int j = 0; j < c[i].length; j++) {
                                                series2.getData().add(new XYChart.Data(c[i][j], d[i][j]));
                                            }
                                        }
                                    }

                                    series3 = new XYChart.Series();
                                    series3.setName("Test Data");
                                    if (t1 != null && t2 != null) {
                                        for (int i = 0; i < t1.length; i++) {
                                            for (int j = 0; j < t1[i].length; j++) {
                                                series3.getData().add(new XYChart.Data(t1[i][j], t2[i][j]));
                                            }
                                        }
                                    }
                                    lc2.getData().addAll(series1, series2, series3);
                                    lc2.setStyle("-fx-font-size: " + 12 + "px;");
                                    lc2.setStyle("-fx-font-size: " + 12 + "px;");


                                    lc2.setMaxHeight(Double.MAX_VALUE);

                                    FlowPane flowPane = new FlowPane();
                                    flowPane.setPadding(new Insets(10, 10, 10, 10));
                                    flowPane.setVgap(4);
                                    flowPane.setHgap(4);


                                    flowPane.getChildren().addAll(lc2, button1,button2,button3);


                                    Scene scene2 = new Scene(flowPane, 1000, 700);

                                    stage.setScene(scene2);

                                    if (indexTmp < Data.mengen.length) {
                                        indexTmp++;
                                    }


                                });
                            }
                        }


                    }));


                    thread.setDaemon(true);
                    thread.start();
                }
            }

        }));

        thread2.start();

    }


    public static void updateChart(LineChart lc) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException exc) {
                throw new Error("Unexpected interruption", exc);
            }

            // Update text on FX Application Thread:
            Platform.runLater(() -> {

            });
        });
        thread.setDaemon(true);
        thread.start();


    }

    public static void setData(double[][] a1, double[][] b1, double rangeX, double rangeY, String s) {
        a = a1;
        b = b1;
        rX = rangeX;
        rY = rangeY;
        label = s;
    }

    public static void setData(double[][] a1, double[][] b1, double[][] c1, double[][] d1, double rangeX,
                               double rangeY, String s) {
        a = a1;
        b = b1;
        c = c1;
        d = d1;
        rX = rangeX;
        rY = rangeY;
        label = s;
    }

    public static void setData(double[][] a1, double[][] b1, double[][] test1, double[][] test2, double[][] c1,
                               double[][] d1, double rangeX, double rangeY, String s) {
        a = a1;
        b = b1;
        c = c1;
        d = d1;
        t1 = test1;
        t2 = test2;
        rX = rangeX;
        rY = rangeY;
        label = s;

    }

    public static void main(String[] args) {
        launch(args);
    }
}