# IoTDB+Grafana实现股票实时走势图

### 一、IoTDB + Grafana 安装部署

IoTDB的安装参考：http://iotdb.apache.org/zh/UserGuide/Master/QuickStart/QuickStart.html

使用Grafana可以将oTDB中时序数据可视化，在IoTDB项目中，实现了连接器IoTDB-Grafana，相关部署和连接参考：http://iotdb.apache.org/zh/UserGuide/Master/Ecosystem%20Integration/Grafana.html

### 二、数据库的连接

使用JDBC连接，在maven中添加IoTDB JDBC依赖，相关配置和sql命令参考代码见：http://iotdb.apache.org/zh/UserGuide/Master/API/Programming-JDBC.html

### 三、数据处理和写入

使用股票信息api可以获得JSON形式的实时数据，如可以用新浪提供的接口：http://hq.sinajs.cn/list=sh601006

#### 创建Fund实体存储股票信息

```java
public class Fund {
    private String fID;
    private String fName;
    private double fEWorth;
}
```

#### FundManage类实现了访问接口进行股票信息的更新以及数据解析的功能

```java
public class FundManage {
    private HashMap<String,Fund> fundHashMap = new HashMap<String, Fund>();
    String[] stockID = {"sh600000","sh600004","sh600006","sh600007","sh600008","sh600009","sh600010","sh600011","sh600012","sh600015"};

    public FundManage() {
        return;
    }

    public HashMap<String, Fund> getFundHashMap() {
        return fundHashMap;
    }

    public void update() {
        fundHashMap.clear();
        for (int i = 0;i < 10;i++) {
            String id = stockID[i];
            String param = "xxxxx";
            String s=HttpRequest.sendGet("xxxxxxx", param);
            Fund fund = parseJson(s);
            fundHashMap.put(id,fund);
        }
    }
    
    public Fund parseJson(String s) {
        JSONObject JS_s = JSONObject.parseObject(s);
        String data = JS_s.getString("data");
        data = data.substring(1,data.length()-1);
        JSONObject JS_data = JSONObject.parseObject(data);
        String name = JS_data.getString("name");
        double fEWorth = JS_data.getDouble("price");
		Fund fund = new Fund(id,name,fEWorth);
        return fund;
    }
}
```

#### UpdateFund类实现了数据的写入

创建存储组root.stock进行存储，共存储10支股票的实时信息，创建时间序列：

```sql
CREATE TIMESERIES root.stock.s0.price WITH DATATYPE=FLOAT, ENCODING=RLE
CREATE TIMESERIES root.stock.s1.price WITH DATATYPE=FLOAT, ENCODING=RLE
CREATE TIMESERIES root.stock.s2.price WITH DATATYPE=FLOAT, ENCODING=RLE
CREATE TIMESERIES root.stock.s3.price WITH DATATYPE=FLOAT, ENCODING=RLE
CREATE TIMESERIES root.stock.s4.price WITH DATATYPE=FLOAT, ENCODING=RLE
CREATE TIMESERIES root.stock.s5.price WITH DATATYPE=FLOAT, ENCODING=RLE
CREATE TIMESERIES root.stock.s6.price WITH DATATYPE=FLOAT, ENCODING=RLE
CREATE TIMESERIES root.stock.s7.price WITH DATATYPE=FLOAT, ENCODING=RLE
CREATE TIMESERIES root.stock.s8.price WITH DATATYPE=FLOAT, ENCODING=RLE
CREATE TIMESERIES root.stock.s9.price WITH DATATYPE=FLOAT, ENCODING=RLE
```

每五秒钟访问一次接口，更新数据并使用addBatch()批量写入IoTDB中

```java
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
                String id = stockID[i];
                Fund fund = fundHashMap.get(id);
                double FEWorth = fund.getfEWorth();
                String sql = "insert into root.stock.s" + i + "(timestamp,price) values(" + dateString +"," + FEWorth + ");";
                System.out.println(sql);
                statement.addBatch(sql);
            }
            statement.executeBatch();
            statement.clearBatch();
            Thread.sleep(5000);
        }
```

### 四、数据可视化

在Grafana中添加IoTDB数据源，选择时间序列进行可视化，得到的结果如下：
![avatar](https://github.com/wang-lucy/iotdb-example/blob/master/result.PNG)
