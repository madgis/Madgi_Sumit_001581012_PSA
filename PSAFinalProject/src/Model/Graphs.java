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
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author sumit
 */
public class Graphs extends JFrame {

    private String label;
    private List<Integer> deathGenotype;
    private List<Integer> popGenotype;
    private List<Integer> recoveredGenotype;

    /**
     * Creates a new demo instance.
     *
     * @param title the frame title.
     */
    public Graphs(String title, String label, List<Integer> deathGenotype, List<Integer> recoveredGenotype, List<Integer> popGenotype) {

        super(title);
        this.label = label;
        this.deathGenotype = deathGenotype;
        this.recoveredGenotype = recoveredGenotype;
        this.popGenotype = popGenotype;
        final CategoryDataset dataset1 = dataSetDeathGenotype();

        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
                this.label, // chart title
                "Genotype", // domain axis label
                this.label.split(" ")[0], // range axis label
                dataset1, // data
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips?
                false // URL generator?  Not required...
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
//        chart.getLegend().setAnchor(Legend.SOUTH);

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        //renderer.setBarPainter(new StandardBarPainter());
        renderer.setSeriesPaint(0, new Color(66, 133, 244));
        renderer.setSeriesPaint(1, new Color(219, 68, 55));
        renderer.setSeriesPaint(2, new Color(244, 180, 0));
        renderer.setSeriesPaint(3, new Color(27, 158, 119));

        //final CategoryDataset dataset2 = createDataset2();
        //plot.setDataset(1, dataset2);
        //plot.mapDatasetToRangeAxis(1, 1);
        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
//        final ValueAxis axis2 = new NumberAxis("Secondary");
//        plot.setRangeAxis(1, axis2);

        final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        renderer2.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        plot.setRenderer(1, renderer2);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);
        // OPTIONAL CUSTOMISATION COMPLETED.

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }

    public Graphs(String title, String label, HashMap<String, Double> fitness) {

        super(title);
        this.label = label;
        CategoryDataset dataset = dataSetDeathGenotype();
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
                this.label, // chart title
                "Variants", // domain axis label
                "Fitness", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips?
                false // URL generator?  Not required...
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
//        chart.getLegend().setAnchor(Legend.SOUTH);

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(0xEE, 0xEE, 0xFF));
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        //renderer.setBarPainter(new StandardBarPainter());
        renderer.setSeriesPaint(0, new Color(66, 133, 244));
        renderer.setSeriesPaint(1, new Color(219, 68, 55));
        renderer.setSeriesPaint(2, new Color(244, 180, 0));
        renderer.setSeriesPaint(3, new Color(27, 158, 119));

        //final CategoryDataset dataset2 = createDataset2();
        //plot.setDataset(1, dataset2);
        //plot.mapDatasetToRangeAxis(1, 1);
        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
//        final ValueAxis axis2 = new NumberAxis("Secondary");
//        plot.setRangeAxis(1, axis2);

        final LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();
        renderer2.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        plot.setRenderer(1, renderer2);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);
        // OPTIONAL CUSTOMISATION COMPLETED.

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }

    Graphs() {
        //displayDualAxis();
    }

    /**
     * Creates a sample data set.
     *
     * @return The data set.
     */
    private CategoryDataset dataSetDeathGenotype() {
        if (this.label.equals("Variants")) {
            final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (String key : RuntimeAttributes.virusvariantLog.keySet()) {
                dataset.addValue(RuntimeAttributes.virusvariantLog.get(key), "Fitness", key);
            }
            return dataset;
        }
        final String series1;
        // row keys...
        if (this.label.equals("Deaths Based on Genotype")) {
            series1 = "Deaths";
        } else if (this.label.equals("Recovered Based on Genotype")) {
            series1 = "Recovered";
        } else {
            series1 = "Population";
        }

        // column keys...
        final String category1 = "A1 Genotype";
        final String category2 = "B1 Genotype";
        final String category3 = "A2 Genotype";
        final String category4 = "B2 Genotype";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        if (this.label.equals("Deaths Based on Genotype")) {
            dataset.addValue(this.deathGenotype.get(0), series1, category1);
            dataset.addValue(this.deathGenotype.get(1), series1, category2);
            dataset.addValue(this.deathGenotype.get(2), series1, category3);
            dataset.addValue(this.deathGenotype.get(3), series1, category4);
        } else if (this.label.equals("Recovered Based on Genotype")) {
            dataset.addValue(this.recoveredGenotype.get(0), series1, category1);
            dataset.addValue(this.recoveredGenotype.get(1), series1, category2);
            dataset.addValue(this.recoveredGenotype.get(2), series1, category3);
            dataset.addValue(this.recoveredGenotype.get(3), series1, category4);
        } else {
            dataset.addValue(this.popGenotype.get(0), series1, category1);
            dataset.addValue(this.popGenotype.get(1), series1, category2);
            dataset.addValue(this.popGenotype.get(2), series1, category3);
            dataset.addValue(this.popGenotype.get(3), series1, category4);
        }

        return dataset;

    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args ignored.
     */
    public void displayDualAxis() {

        this.pack();
        this.setSize(600, 527);
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);

    }

}
