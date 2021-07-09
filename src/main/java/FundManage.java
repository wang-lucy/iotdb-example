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
