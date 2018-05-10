package com.zhong.rm;

import com.zhong.utils.MyUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * @author 张中俊
 * @create 2018-03-27 8:12
 **/
public class RedisWrapperTest {

    @Test
    public void getAndReleaseResourceTest(){
        Jedis jedis = RedisWrapper.getJedisObject();
        System.out.println(jedis);
        RedisPool.returnResource(jedis);
    }

    @Test
    public void getRecordsTest(){

        Map<String,Collection<String>> res = RedisWrapper.getRecords(0,100);
        for(String key : res.keySet()){
            Collection<String> filenames = res.get(key);
            System.out.println(key+" "+filenames);
        }
    }

    @Test
    public void getRecordsTest2() throws IOException {
        int begin=2094000;
        while(true) {
            System.out.println("正在处理 "+begin+"-"+(begin+1000)+" 条数据");
            File file = MyUtils.getFile("output_enwiki_redis", begin+"-"+(begin+1000)+".txt");
            Map<String, Collection<String>> res = RedisWrapper.getRecords(begin, begin+1000);
            begin = begin + 1000;
            FileOutputStream fos = new FileOutputStream(file);

            for (String key : res.keySet()) {
                Collection<String> filenames = res.get(key);
                //System.out.println(key + " " + filenames);
                IOUtils.write(key + " " + filenames.size() + " " + filenames + System.lineSeparator(), fos);
            }
            fos.close();
        }
    }

}
