import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.tools.nsc.doc.model.Val;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Xu on 2022/11/24.
 * describe:
 */
public class JSONParse {
    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger(JSONParse.class);
        String str1 = "{\"MPID\":\"0008114560317215753574\",\"DT\":\"2022-11-23 11:40:00\",\"SSID\":\"EMS\",\"FROZEN\":\"0\",\"PERIOD\":\"0\",\"IDTYPE\":\"bpmpid\",\"MDATA\":[{\"TAG\":\"P\",\"VAL\":\"0.0500000\",\"Q\":\"0\"},{\"TAG\":\"Q\",\"VAL\":\"2.0004000\",\"Q\":\"0\"},{\"TAG\":\"cos\",\"VAL\":\"0.0030000\",\"Q\":\"0\"},{\"TAG\":\"Ia\",\"VAL\":\"0.0020000\",\"Q\":\"0\"},{\"TAG\":\"I\",\"VAL\":\"0.1000\",\"Q\":\"0\"},{\"TAG\":\"Ib\",\"VAL\":\"1.000000\",\"Q\":\"0\"}]}";
        String str2 = "{\"DT\":\"2022-11-23 12:45:00\",\"MPID\":\"0004000000000000000000000000000000000000000000000000019940854074\",\"MDATA\":[{\"VAL\":\"245.8\",\"Q\":\"0\",\"TAG\":\"Ua\"}],\"SSID\":\"EIA\"}";
        String str3 = "{\"MPID\":\"0008114560317249298032\",\"DT\":\"2022-11-23 13:10:00\",\"SSID\":\"EMS\",\"FROZEN\":\"0\",\"PERIOD\":\"0\",\"IDTYPE\":\"bpmpid\",\"MDATA\":[{\"TAG\":\"STValue\",\"VAL\":\"1\",\"Q\":\"0\"}]}";

        //方法①，不推荐
        JSONObject jsonObject = JSON.parseObject(str1);
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
            if (mdata1.startsWith("[") && mdata1 != "null") {
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

        System.out.println("============================1");
        Map map = JSONArray.parseObject(str1, Map.class);
        System.out.println(map.size());//7
        System.out.println(map);//{DT=2022-11-23 11:40:00, IDTYPE=bpmpid, PERIOD=0, MPID=0008114560317215753574, MDATA=[{"VAL":"0.000000","Q":"0","TAG":"P"},{"VAL":"0.000000","Q":"0","TAG":"Q"},{"VAL":"0.000000","Q":"0","TAG":"cos"},{"VAL":"0.000000","Q":"0","TAG":"Ia"},{"VAL":"0.000000","Q":"0","TAG":"I"},{"VAL":"0.000000","Q":"0","TAG":"Ib"},{"VAL":"","Q":"0","TAG":"Ic"}], FROZEN=0, SSID=EMS}
        System.out.println(map.get("DT"));//2022-11-23 11:40:00
        //将数组转换成 List<Map>
        List<Map> mdata1 = JSONArray.parseArray(map.get("MDATA").toString(), Map.class);
        System.out.println(mdata1);//[{VAL=0.0500000, Q=0, TAG=P}, {VAL=2.0004000, Q=0, TAG=Q}, {VAL=0.0030000, Q=0, TAG=cos}, {VAL=0.0020000, Q=0, TAG=Ia}, {VAL=0.1000, Q=0, TAG=I}, {VAL=1.000000, Q=0, TAG=Ib}]

        System.out.println("============================2");
        Object parse = JSONArray.parse(str3);
        System.out.println(parse.toString());//{"DT":"2022-11-23 13:10:00","IDTYPE":"bpmpid","PERIOD":"0","MPID":"0008114560317249298032","MDATA":[{"VAL":"1","Q":"0","TAG":"STValue"}],"FROZEN":"0","SSID":"EMS"}

        System.out.println("==========遍历MDATA的值并累加输出==================");
        String mdata = jsonObject.getString("MDATA");
        List<Map> maps = JSONArray.parseArray(mdata, Map.class);
        System.out.println(maps);//[{VAL=0.000000, Q=0, TAG=P}, {VAL=0.000000, Q=0, TAG=Q}, {VAL=0.000000, Q=0, TAG=cos}, {VAL=0.000000, Q=0, TAG=Ia}, {VAL=0.000000, Q=0, TAG=I}, {VAL=0.000000, Q=0, TAG=Ib}, {VAL=, Q=0, TAG=Ic}]
        System.out.println(maps.size());
        Double sum = 0.0D;
        for (Map map1 : maps) {
            sum += Double.parseDouble(map1.get("VAL").toString());
        }
        System.out.println(sum);

        System.out.println("==========获取VAL==================");
        String val = maps.get(0).get("VAL").toString();
        System.out.println(val);


    }


}
