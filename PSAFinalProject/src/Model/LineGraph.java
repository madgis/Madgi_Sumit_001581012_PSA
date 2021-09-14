/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author sumit
 */
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

public class LineGraph extends JFrame {

    private static final long serialVersionUID = 1L;
    private String label;
    private DefaultCategoryDataset dataset;

    public LineGraph(String title) {
        super(title);
        this.label = title;
        this.dataset=new DefaultCategoryDataset();
        // Create chart  
        JFreeChart chart = ChartFactory.createLineChart(
                "Days vs " + this.label, // Chart title  
                "Days", // X-Axis Label  
                this.label, // Y-Axis Label  
                dataset, PlotOrientation.VERTICAL, true, true, false
        );

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    public DefaultCategoryDataset addToDataset(String label, int day, int recovered) {

        String series = label;
        this.dataset.addValue(day, series, String.valueOf(recovered));

        return this.dataset;
    }

    public DefaultCategoryDataset getDataset() {
        return dataset;
    }

    public void display() {
        this.pack();
        this.setSize(600, 527);
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);

    }
}
