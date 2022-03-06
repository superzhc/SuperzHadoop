package com.github.superzhc.data.others;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.superzhc.data.common.HttpData;
import com.github.superzhc.data.utils.ResultT;

/**
 * @author superz
 * @create 2022/1/21 10:20
 */
public class QDataAPI extends HttpData {
    private static ObjectMapper mapper=new ObjectMapper();
    public void info(String qq) {
        try {
            // Q绑集合
            String url = String.format("http://ww.you-api.icu/hj.php?qq=%s", qq);
            ResultT rt = get(url);
            System.out.println(rt);

            JsonNode QBinds = mapper.readTree((String) rt.getData());
            //String mobile=QBinds.get("phone").asText();

            //Q绑查询
            url = String.format("http://ww.you-api.icu/qb.php?mod=cha&qq=%s", qq);
            rt = get(url);
            System.out.println(rt);

            //Q群关系
            url = String.format("http://ww.you-api.icu/qun.php?qq=%s", qq);
            rt = get(url);
            System.out.println(rt);

            //手机号查微博id
            /*url = String.format("http://ww.you-api.icu/bw.php?mod=cha&mobile=%s",mobile);
            rt = get(url);
            System.out.println(rt);*/


            //微博id查手机号
            //http://ww.you-api.icu/wb.php?mod=cha&oid=6057766172

            //英雄联盟名字查QQ
            //http://ww.you-api.icu/olo.php?mod=cha&name=我爱玩中路

            //QQ查英雄联盟名字
            url = String.format("http://ww.you-api.icu/lol.php?mod=cha&uin=%s", qq);
            rt = get(url);
            System.out.println(rt);

            //QQ老密(加密的)
            //http://ww.you-api.icu/lm.php?qq=10001
            //
            //QQ查地下城勇士
            //http://ww.you-api.icu/dnf.php?mod=cha&qq=10001
            //
            //126邮箱查老密
            //http://ww.you-api.icu/126email.php?mod=cha&email=10001@126.com
            //
            //QQ实名制查询
            url = String.format("http://ww.you-api.icu/cn.php?qq=%s", qq);
            rt = get(url);
            System.out.println(rt);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
