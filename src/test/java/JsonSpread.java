import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Xu on 2022/11/28.
 * describe:
 * 1.创建map对象用于存储组装后的数据
 * 2.解析jsonString转为jsonObj
 * 3.通过jsonObj取出想要的值，放入map
 * 4.JSON.parseArray获取数组，遍历数组取出想要的key，放入map
 * 5.如有必要，可将map通过JSON.toJSONString转为String
 */
public class JsonSpread {
    public static void main(String[] args) {
        String str1 = "{\"MPID\":\"0008114560317215753574\",\"DT\":\"2022-11-23 11:40:00\",\"SSID\":\"EMS\",\"FROZEN\":\"0\",\"PERIOD\":\"0\",\"IDTYPE\":\"bpmpid\",\"MDATA\":[{\"TAG\":\"P\",\"VAL\":\"0.0500000\",\"Q\":\"0\"},{\"TAG\":\"Q\",\"VAL\":\"2.0004000\",\"Q\":\"0\"},{\"TAG\":\"cos\",\"VAL\":\"0.0030000\",\"Q\":\"0\"},{\"TAG\":\"Ia\",\"VAL\":\"0.0020000\",\"Q\":\"0\"},{\"TAG\":\"I\",\"VAL\":\"0.1000\",\"Q\":\"0\"},{\"TAG\":\"Ib\",\"VAL\":\"1.000000\",\"Q\":\"0\"}]}";
        String str2 = "{\"DT\":\"2022-11-23 12:45:00\",\"MPID\":\"0004000000000000000000000000000000000000000000000000019940854074\",\"MDATA\":[{\"VAL\":\"245.8\",\"Q\":\"0\",\"TAG\":\"Ua\"}],\"SSID\":\"EIA\"}";

        JSONObject jsonObject = JSON.parseObject(str1);
        String mdata = jsonObject.getString("MDATA");
        System.out.println("mdata:\n" + mdata);
        HashMap<String, String> stringHashMap = new HashMap<>();
        List<Map> maps = JSON.parseArray(mdata, Map.class);
        System.out.println("List:\n" + maps);

        /*for (Map map : maps) {
            String val = map.get("VAL").toString();
            stringHashMap.put("VAL", val);
        }*/

        //适合数组有多个对象
        for (int i = 0; i < maps.size(); i++) {
            String val = maps.get(i).get("VAL").toString();
            stringHashMap.put("VAL" + (i + 1), val);
        }

        String dt = jsonObject.getString("DT");
        String mpid = jsonObject.getString("MPID");
        stringHashMap.put("DT", dt);
        stringHashMap.put("MPID", mpid);
        System.out.println("stringHashMap:\n" + stringHashMap);//返回map，可在框架使用
        String s = JSON.toJSONString(stringHashMap);//map转String
        System.out.println("toJSONString:\n" + stringHashMap);

    }
}
