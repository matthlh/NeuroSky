import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphListener implements ActionListener{

    private NeuroSocket socket;
    UserInterface ui;
    XYChart chart;
    JPanel chartPanel;

    public GraphListener(NeuroSocket socket) {
        this.socket = socket;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        chart = new XYChartBuilder().width(600).height(350).title("Area Chart").xAxisTitle("Time (s)").yAxisTitle("Band Power").build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);

        XYSeries deltaSeries = chart.addSeries("Delta", socket.deltaArray);
        XYSeries thetaSeries = chart.addSeries("Theta", socket.thetaArray);
        XYSeries lowAlphaSeries = chart.addSeries("Low Alpha", socket.lowAlphaArray);
        XYSeries highAlphaSeries = chart.addSeries("High Alpha", socket.highAlphaArray);
        XYSeries lowBetaSeries = chart.addSeries("Low Beta", socket.lowBetaArray);
        XYSeries highBetaSeries = chart.addSeries("High Beta", socket.highBetaArray);
        XYSeries lowGammaSeries = chart.addSeries("Low Gamma", socket.lowGammaArray);
        XYSeries highGammaSeries = chart.addSeries("High Gamma", socket.highGammaArray);

        deltaSeries.setEnabled(true);
        thetaSeries.setEnabled(true);
        lowAlphaSeries.setEnabled(true);
        highAlphaSeries.setEnabled(true);
        lowBetaSeries.setEnabled(true);
        highBetaSeries.setEnabled(true);
        lowGammaSeries.setEnabled(true);
        highGammaSeries.setEnabled(true);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                //Create and set up the window.
                JFrame frame = new JFrame("Graph");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                //Chart
                chartPanel = new XChartPanel<>(chart);
                frame.add(chartPanel, BorderLayout.CENTER);

                JPanel checkBox = new JPanel(new FlowLayout());

                JCheckBox delta = new JCheckBox("Delta");
                JCheckBox theta = new JCheckBox("Theta");
                JCheckBox lowAlpha = new JCheckBox("Low Alpha");
                JCheckBox highAlpha = new JCheckBox("High Alpha");
                JCheckBox lowBeta = new JCheckBox("Low Beta");
                JCheckBox highBeta = new JCheckBox("High Beta");
                JCheckBox lowGamma = new JCheckBox("Low Gamma");
                JCheckBox highGamma = new JCheckBox("High Gamma");

                delta.setSelected(true);
                theta.setSelected(true);
                lowAlpha.setSelected(true);
                highAlpha.setSelected(true);
                lowBeta.setSelected(true);
                highBeta.setSelected(true);
                lowGamma.setSelected(true);
                highGamma.setSelected(true);

                delta.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(delta.isSelected());
                        if (delta.isSelected()) {
                            deltaSeries.setEnabled(true);
                        } else {
                            deltaSeries.setEnabled(false);
                        }
                        chartPanel.revalidate();
                        chartPanel.repaint();
                    }
                });
                theta.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (theta.isSelected()) {
                            thetaSeries.setEnabled(true);
                        } else {
                            thetaSeries.setEnabled(false);
                        }
                        chartPanel.revalidate();
                        chartPanel.repaint();
                    }
                });
                lowAlpha.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (lowAlpha.isSelected()) {
                            lowAlphaSeries.setEnabled(true);
                        } else {
                            lowAlphaSeries.setEnabled(false);
                        }
                        chartPanel.revalidate();
                        chartPanel.repaint();
                    }
                });
                highAlpha.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (highAlpha.isSelected()) {
                            highAlphaSeries.setEnabled(true);
                        } else {
                            highAlphaSeries.setEnabled(false);
                        }
                        chartPanel.revalidate();
                        chartPanel.repaint();
                    }
                });
                lowBeta.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (lowBeta.isSelected()) {
                            lowBetaSeries.setEnabled(true);
                        } else {
                            lowBetaSeries.setEnabled(false);
                        }
                        chartPanel.revalidate();
                        chartPanel.repaint();
                    }
                });
                highBeta.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (highBeta.isSelected()) {
                            highBetaSeries.setEnabled(true);
                        } else {
                            highBetaSeries.setEnabled(false);
                        }
                        chartPanel.revalidate();
                        chartPanel.repaint();
                    }
                });
                lowGamma.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (lowGamma.isSelected()) {
                            lowGammaSeries.setEnabled(true);
                        } else {
                            lowGammaSeries.setEnabled(false);
                        }
                        chartPanel.revalidate();
                        chartPanel.repaint();
                    }
                });
                highGamma.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (highGamma.isSelected()) {
                            highGammaSeries.setEnabled(true);
                        } else {
                            highGammaSeries.setEnabled(false);
                        }
                        chartPanel.revalidate();
                        chartPanel.repaint();
                    }
                });

                checkBox.add(delta);
                checkBox.add(theta);
                checkBox.add(lowAlpha);
                checkBox.add(highAlpha);
                checkBox.add(lowBeta);
                checkBox.add(highBeta);
                checkBox.add(lowGamma);
                checkBox.add(highGamma);

                frame.add(checkBox, BorderLayout.SOUTH);

                //Display the window.
                frame.pack();
                frame.setVisible(true);
            }
        });
        ui.myContainer.requestFocusInWindow();
    }
}
