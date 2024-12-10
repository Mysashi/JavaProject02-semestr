import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.sqlite.JDBC;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;

public class DataBase {

    private static final String CON_STR = "jdbc:sqlite:D:/sports.db";
    private Connection connection;

    private void injectData(int id, String name, String subject, String address, String data) {
        Parser parser = new Parser();
//        ArrayList<Data> arrayList = parser.parseCsv();
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO sports" +
                    "(id, name, subject, adress, date) VALUES (?, ?, ?, ?, ?)");
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setString(3, subject);
            stmt.setString(4, address);
            stmt.setString(5, data);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void loadInData(ArrayList<Data> data) {
        for (int i = 0; i < data.size(); i++) {

            int id = data.get(i).getId();
            String name = data.get(i).getName();
            String subject = data.get(i).getSubject();
            String address = data.get(i).getAddress();
            String date = data.get(i).getData();
            injectData(id, name, subject, address, date);
        }
        System.out.println("Success");
    }

    {
        DbHandler();
    }

    private void DbHandler() {
        try {
            DriverManager.registerDriver(new JDBC());
            this.connection = DriverManager.getConnection(CON_STR);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createSports() {

        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE sports" +
                    "(id INTEGER not NULL, " +
                    " name VARCHAR(255)," +
                    " subject VARCHAR(100), " +
                    " adress VARCHAR(255), " +
                    " date VARCHAR(250), " +
                    " PRIMARY KEY ( id ))";
            statement.executeUpdate(sql);
            System.out.println("Table is created");

        } catch (SQLException exception) {
            System.out.println("Not working");
            System.out.println(exception.getMessage());
        }

    }

    protected void selectAvgCount() {
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT AVG(facility_count) as average_facilities " +
                    "FROM ( " +
                    "    SELECT subject, COUNT(*) as facility_count " +
                    "    FROM sports " +
                    "    GROUP BY subject " +
                    ") as region_counts;");

            if (rs.next()) {
                double average = rs.getDouble("average_facilities");
                System.out.println("Среднее количество объектов спорта в регионах: " + average);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected double sortingTop() {
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT subject, COUNT(*) as facility_count " +
                    "FROM sports " +
                    "GROUP BY subject " +
                    "ORDER BY facility_count DESC " +
                    "LIMIT 3;");

            System.out.println("Первые 3 региона с самым большим количеством объектов спорта:");
            while (rs.next()) {
                String region = rs.getString("subject");
                int facilityCount = rs.getInt("facility_count");
                System.out.println("Регион: " + region + ", Количество объектов: " + facilityCount);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0.0;
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String sql = "SELECT subject, COUNT(*) as facility_count " +
                "FROM sports " +
                "WHERE subject NOT IN ('Москва', 'Московская область') " +
                "GROUP BY subject " +
                "UNION ALL " +
                "SELECT 'Москва и Московская область' as subject, COUNT(*) as facility_count " +
                "FROM sports " +
                "WHERE subject IN ('Москва', 'Московская область') " +
                "GROUP BY subject;";

//        String sql = "SELECT subject, facility_count FROM ( " +
//                "SELECT subject, COUNT(*) as facility_count " +
//                "FROM sports " +
//                "WHERE subject NOT IN ('Москва', 'Московская область') " +
//                "GROUP BY subject " +
//                "LIMIT 10 " +
//                ") AS other_regions " +
//                "UNION ALL " +
//                "SELECT 'Москва и Московская область' as subject, COUNT(*) as facility_count " +
//                "FROM sports " +
//                "WHERE subject IN ('Москва', 'Московская область') " +
//                "GROUP BY subject;"; // Здесь запрос на 10 записей - если нужно протестировать

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String region = resultSet.getString("subject");
                int facilityCount = resultSet.getInt("facility_count");
                dataset.addValue(facilityCount, "Количество объектов", region);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataset;
    }

    private JFreeChart createChart(CategoryDataset dataset)
    {
        JFreeChart chart = ChartFactory.createBarChart(
                "Количество объектов спорта по регионам",
                "Регион",
                "Количество объектов",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        return chart;
    }

    public JPanel createHistoPanel()
    {
        JFreeChart chart = createChart(createDataset());
        chart.setPadding(new RectangleInsets(4, 8, 2, 2));
        ChartPanel panel = new ChartPanel(chart);
        panel.setFillZoomRectangle(true);
        panel.setPreferredSize(new Dimension(600, 300));
        panel.setVisible(true);
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setSize(600, 300);
        frame.setVisible(true);
        return panel;
    }

}
