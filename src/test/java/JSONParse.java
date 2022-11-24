import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.tools.nsc.doc.model.Val;

import java.util.Map;
import java.util.Objects;

/**
 * Created by Xu on 2022/11/24.
 * describe:
 */
public class JSONParse {
    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger(JSONParse.class);
        String str1 = "{\"MPID\":\"0008114560317215753574\",\"DT\":\"2022-11-23 11:40:00\",\"SSID\":\"EMS\",\"FROZEN\":\"0\",\"PERIOD\":\"0\",\"IDTYPE\":\"bpmpid\",\"MDATA\":[{\"TAG\":\"P\",\"VAL\":\"0.000000\",\"Q\":\"0\"},{\"TAG\":\"Q\",\"VAL\":\"0.000000\",\"Q\":\"0\"},{\"TAG\":\"cos\",\"VAL\":\"0.000000\",\"Q\":\"0\"},{\"TAG\":\"Ia\",\"VAL\":\"0.000000\",\"Q\":\"0\"},{\"TAG\":\"I\",\"VAL\":\"0.000000\",\"Q\":\"0\"},{\"TAG\":\"Ib\",\"VAL\":\"0.000000\",\"Q\":\"0\"},{\"TAG\":\"Ic\",\"VAL\":\"\",\"Q\":\"0\"}]}";
        String str2 = "{\"DT\":\"2022-11-23 12:45:00\",\"MPID\":\"0004000000000000000000000000000000000000000000000000019940854074\",\"MDATA\":[{\"VAL\":\"245.8\",\"Q\":\"0\",\"TAG\":\"Ua\"}],\"SSID\":\"EIA\"}";
        String str3 = "{\"MPID\":\"0008114560317249298032\",\"DT\":\"2022-11-23 13:10:00\",\"SSID\":\"EMS\",\"FROZEN\":\"0\",\"PERIOD\":\"0\",\"IDTYPE\":\"bpmpid\",\"MDATA\":[{\"TAG\":\"STValue\",\"VAL\":\"1\",\"Q\":\"0\"}]}";

        //方法①，不推荐
        JSONObject jsonObject = JSON.parseObject(str3);
        //getJSONArray可能会因为数据异常产生空指针
        JSONArray jsonArray = jsonObject.getJSONArray("MDATA");
        System.out.println(jsonArray);
        for (Object o : jsonArray.toArray()) {
            JSONObject jsonObject1 = JSON.parseObject(o.toString());
            String val1 = jsonObject1.getString("VAL");
            if (val1.equals("null") || val1.trim().equals("")) {
                log.warn("VAL:" + val1 + "||" + o + "||" + jsonObject);
            }
            log.info(val1 + "||" + o);
        }

        //方法②，推荐
        if (str1.contains("MDATA")) {
            JSONObject jsonObject1 = JSON.parseObject(str1);
            //通过getString获取数组，避免空指针，后续判断数据格式
            String mdata1 = jsonObject1.getString("MDATA");
            if (mdata1.startsWith("[") && mdata1 != null) {
                Object[] mdataObjects = JSON.parseArray(mdata1).toArray();
                for (Object mdataObject : mdataObjects) {
                    JSONObject parseObject = JSON.parseObject(mdataObject.toString());
                    String val = parseObject.getString("VAL");
                    if (val.equals("null") || val.trim().equals("")) {
                        log.warn("VAL:" + val + "||" + mdataObject + "||" + jsonObject1);
                    }/*else {
                        log.info("VAL:" + val + "||" + mdataObject + "||" + jsonObject1);
                    }*/
                }
            } else {
                System.out.println("mdata1" + mdata1);
            }
        } else {
            System.out.println(str1);
        }
    }

}
