import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Query extends JFrame implements ActionListener {
    private TimeSeries series0;
    private TimeSeries series1;
    private TimeSeries series2;
    private TimeSeries series3;
    private TimeSeries series4;
    private TimeSeries series5;
    private TimeSeries series6;
    private TimeSeries series7;
    private TimeSeries series8;
    private TimeSeries series9;
    private String[] lastValue = new String[10];

    public Query() {
        getContentPane().setBackground(Color.green);
    }

    /**
     * 创建应用程序界面
     */
    public void createUI() {
        /*
        this.series0 = new TimeSeries("华夏成长混合");
        this.series1 = new TimeSeries("中海可转债债券A");
        this.series2 = new TimeSeries("中海可转债债券C");
        this.series3 = new TimeSeries("嘉实增强信用定期债券");
        this.series4 = new TimeSeries("西部利得量化成长混合A");
        this.series5 = new TimeSeries("嘉实中证500ETF联接A");
        this.series6 = new TimeSeries("华夏聚利债券");
        this.series7 = new TimeSeries("华夏纯债债券A");
        this.series8 = new TimeSeries("华夏纯债债券C");
        this.series9 = new TimeSeries("财通可持续混合");

         */
        this.series0 = new TimeSeries("浦发银行");
        this.series1 = new TimeSeries("白云机场");
        this.series2 = new TimeSeries("东风汽车");
        this.series3 = new TimeSeries("中国国贸");
        this.series4 = new TimeSeries("首创环保");
        this.series5 = new TimeSeries("上海机场");
        this.series6 = new TimeSeries("包钢股份");
        this.series7 = new TimeSeries("华能国际");
        this.series8 = new TimeSeries("皖通高速");
        this.series9 = new TimeSeries("华夏银行");
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(this.series0);
        dataset.addSeries(this.series1);
        dataset.addSeries(this.series2);
        dataset.addSeries(this.series3);
        dataset.addSeries(this.series4);
        dataset.addSeries(this.series5);
        dataset.addSeries(this.series6);
        dataset.addSeries(this.series7);
        dataset.addSeries(this.series8);
        dataset.addSeries(this.series9);
        ChartPanel chartPanel = new ChartPanel(createChart(dataset));
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        add(chartPanel);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JFreeChart createChart(XYDataset dataset) {
        JFreeChart result = ChartFactory.createTimeSeriesChart("股价价格实时变化图", "time",
                "price", dataset, true, true, false);
        XYPlot plot = (XYPlot) result.getPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);
        axis = plot.getRangeAxis();
        axis.setRange(6.1, 6.3);

        Font titleFont = new Font("黑体", Font.BOLD, 20);
        TextTitle textTitle = result.getTitle();
        textTitle.setFont(titleFont);// 为标题设置上字体

        Font LegendFont = new Font("楷体", Font.PLAIN, 18);
        LegendTitle legend = result.getLegend(0);
        legend.setItemFont(LegendFont);// 为图例说明设置字体
        return result;
    }

    /**
     * 动态运行
     */
    public void dynamicRun() throws SQLException {
        Connection connection = getConnection();
        if (connection == null) {
            System.out.println("get connection defeat");
            return;
        }
        Statement statement = connection.createStatement();
        while (true) {
            //String sql = "select last f0,f1,f2,f3,f4,f5,f6,f7,f8,f9 from root.fund";
            String sql = "select last s0,s1,s2,s3,s4,s5,s6,s7,s8,s9 from root.stock";
            ResultSet resultSet = statement.executeQuery(sql);
            System.out.println("sql: " + sql);
            outputResult(resultSet);
            this.series0.add(new Millisecond(), Double.valueOf(lastValue[0]));
            this.series1.add(new Millisecond(), Double.valueOf(lastValue[1]));
            this.series2.add(new Millisecond(), Double.valueOf(lastValue[2]));
            this.series3.add(new Millisecond(), Double.valueOf(lastValue[3]));
            this.series4.add(new Millisecond(), Double.valueOf(lastValue[4]));
            this.series5.add(new Millisecond(), Double.valueOf(lastValue[5]));
            this.series6.add(new Millisecond(), Double.valueOf(lastValue[6]));
            this.series7.add(new Millisecond(), Double.valueOf(lastValue[7]));
            this.series8.add(new Millisecond(), Double.valueOf(lastValue[8]));
            this.series9.add(new Millisecond(), Double.valueOf(lastValue[9]));
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 主函数入口
    public static void main(String[] args) throws SQLException {
        Query jsdChart = new Query();
        jsdChart.setTitle("动态折线图");
        jsdChart.createUI();
        jsdChart.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jsdChart.setBounds(100, 100, 900, 600);
        jsdChart.setVisible(true);
//      Color c=new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));

        jsdChart.dynamicRun();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


    public static Connection getConnection() {
        // JDBC driver name and database URL
        String driver = "org.apache.iotdb.jdbc.IoTDBDriver";
        String url = "jdbc:iotdb://127.0.0.1:6667/";

        // Database credentials
        String username = "root";
        String password = "root";

        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * This is an example of outputting the results in the ResultSet
     */
    private void outputResult(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            System.out.println("--------------------------");
            final ResultSetMetaData metaData = resultSet.getMetaData();
            final int columnCount = metaData.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                System.out.print(metaData.getColumnLabel(i + 1) + " ");
            }
            System.out.println();
            int cnt = 0;
            while (resultSet.next()) {
                for (int i = 1; ; i++) {
                    System.out.print(resultSet.getString(i));
                    if (i < columnCount) {
                        System.out.print(", ");
                    } else {
                        this.lastValue[cnt]= resultSet.getString(3);
                        cnt++;
                        System.out.println();
                        break;
                    }
                }
            }
            System.out.println("--------------------------\n");
        }
    }
}