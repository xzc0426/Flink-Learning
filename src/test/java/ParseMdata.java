import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Xu on 2022/11/29.
 * describe:
 */
public class ParseMdata {
    public static void main(String[] args) {
        String json = "{\"DT\":\"2022-11-23 12:45:00\",\"MPID\":\"0004000000000000000000000000000000000000000000000000019940854074\",\"MDATA\":\"[{\\\"VAL\\\":\\\"245.8\\\",\\\"Q\\\":\\\"0\\\",\\\"TAG\\\":\\\"Ua\\\"}]\",\"SSID\":\"EIA\"}";

        JSONObject jsonObject = JSON.parseObject(json);
        String mdata = jsonObject.getString("MDATA");
        System.out.println(mdata);
        List<HashMap> hashMaps = JSON.parseArray(mdata, HashMap.class);
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        System.out.println(hashMaps.size());
        for (HashMap hashMap : hashMaps) {

            stringStringHashMap.put(hashMap.get("TAG").toString(), hashMap.get("VAL").toString());

        }

        System.out.println(stringStringHashMap);
        System.out.println(notEmpty(stringStringHashMap,"Ua"));
    }

    /**
     * 校验 map 是否含所有的指定键值，非空时返回 true，否则返回 false
     */
    public static Boolean notEmpty(HashMap<String, String> req, String... str) {
        if (req == null) {
            return false;
        }
        for (String strIndex : str) {
            if (StringUtils.isEmpty(req.get(strIndex))) {
                return false;
            }
        }
        return true;
    }
}
