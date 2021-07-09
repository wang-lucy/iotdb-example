import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.HashMap;

public class FundManage {
    private HashMap<String,Fund> fundHashMap = new HashMap<String, Fund>();
    String[] fundID = {"000001","000003","000004","000005","000006","000008","000014","000015","000016","000017"};
    String[] stockID = {"sh600000","sh600004","sh600006","sh600007","sh600008","sh600009","sh600010","sh600011","sh600012","sh600015"};

    public FundManage() {
        return;
    }


    public HashMap<String, Fund> getFundHashMap() {
        return fundHashMap;
    }

    public void run() {
        fundHashMap.clear();
        for (int i = 0;i < 10;i++) {
            //String id = fundID[i];
            String id = stockID[i];
            String param = "code=" + id + "&token=atTPd9c8sA";
            //String s=HttpRequest.sendGet("https://api.doctorxiong.club/v1/fund", param);
            String s=HttpRequest.sendGet("https://api.doctorxiong.club/v1/stock", param);
            JSONObject JS_s = JSONObject.parseObject(s);
            String data = JS_s.getString("data");
            data = data.substring(1,data.length()-1);
            JSONObject JS_data = JSONObject.parseObject(data);
            String name = JS_data.getString("name");
            //double fEWorth = JS_data.getDouble("expectWorth");
            double fEWorth = JS_data.getDouble("price");
            Fund fund = new Fund(id,name,fEWorth);
            fundHashMap.put(id,fund);
        }
    }

    public void update() {
        for (int i = 0;i < 10;i++) {
            String id = fundID[i];
            Fund fund = fundHashMap.get(id);
            String param = "code=" + id + "&token=atTPd9c8sA";
            String s=HttpRequest.sendGet("https://api.doctorxiong.club/v1/fund", param);
            JSONObject JS_s = JSONObject.parseObject(s);
            String data = JS_s.getString("data");
            data = data.substring(1,data.length()-1);
            JSONObject JS_data = JSONObject.parseObject(data);
            float fEWorth = JS_data.getFloat("expectWorth");
            fund.setfEWorth(fEWorth);
            fundHashMap.replace(id,fund);
        }
    }
}
