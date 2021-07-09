import com.alibaba.fastjson.JSONObject;
import java.sql.*;
import org.apache.iotdb.jdbc.IoTDBSQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class UpdateFund {

    public static void main(String[] args) throws SQLException, InterruptedException {
        String[] fundID = {"000001","000003","000004","000005","000006","000008","000014","000015","000016","000017"};
        String[] stockID = {"sh600000","sh600004","sh600006","sh600007","sh600008","sh600009","sh600010","sh600011","sh600012","sh600015"};
        FundManage fundManage = new FundManage();

        while(true) {
            fundManage.run();
            HashMap<String,Fund> fundHashMap = fundManage.getFundHashMap();
            Connection connection = getConnection();
            if (connection == null) {
                System.out.println("get connection defeat");
                return;
            }
            Statement statement = connection.createStatement();
            long times = System.currentTimeMillis();//时间戳
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(times);
            for (int i = 0;i < 10;i++) {
                //String id = fundID[i];
                String id = stockID[i];
                Fund fund = fundHashMap.get(id);
                double FEWorth = fund.getfEWorth();
                //String sql = "insert into root.fund.f" + i + "(timestamp,value) values(" + dateString +"," + FEWorth + ");";
                String sql = "insert into root.stock.s" + i + "(timestamp,price) values(" + dateString +"," + FEWorth + ");";
                System.out.println(sql);
                statement.addBatch(sql);
            }
            statement.executeBatch();
            statement.clearBatch();
            Thread.sleep(5000);
        }
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
    private static void outputResult(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            System.out.println("--------------------------");
            final ResultSetMetaData metaData = resultSet.getMetaData();
            final int columnCount = metaData.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                System.out.print(metaData.getColumnLabel(i + 1) + " ");
            }
            System.out.println();
            while (resultSet.next()) {
                for (int i = 1; ; i++) {
                    System.out.print(resultSet.getString(i));
                    if (i < columnCount) {
                        System.out.print(", ");
                    } else {
                        System.out.println();
                        break;
                    }
                }
            }
            System.out.println("--------------------------\n");
        }
    }
}
