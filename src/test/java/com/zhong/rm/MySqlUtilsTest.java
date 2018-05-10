package com.zhong.rm;

import bloomfilter.CanGenerateHashFrom;
import bloomfilter.mutable.BloomFilter;
import com.zhong.*;
import com.zhong.concurrent.Task;
import com.zhong.utils.MyUtils;
import com.zhong.utils.SerializationDemonstrator;
import org.junit.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author 张中俊
 * @create 2018-03-29 15:56
 **/
public class MySqlUtilsTest {
    @Test
    public void connectTest(){
        MysqlUtils mysqlUtils = new MysqlUtils();
    }
    @Test
    public void saveTaskTest() throws Exception {
        String keyword = "key1";
        ArrayList<String> filenames = new ArrayList<>();
        filenames.add("ind1");
        filenames.add("ind2");
        filenames.add("ind3");
        Task task = EDBSetup_split_memory.EDBSetup(keyword,filenames);
        //System.out.println(Arrays.toString(task.getStag()));
        ArrayList<t_item> items = task.getT();
        for(t_item item : items){
            System.out.println(item.toString());
        }

        MysqlUtils mu = new MysqlUtils();
        mu.saveTask(task);
    }

    @Test
    public void getAllTaskTest() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MysqlUtils mu = new MysqlUtils();
        ArrayList<Task> tasks = mu.getAllTask();
        byte[] key1 = T_Sets_split_memory.TSetGetTag("key1");
        byte[] key2 = T_Sets_split_memory.TSetGetTag("key2");
        byte[] key3 = T_Sets_split_memory.TSetGetTag("key3");
        System.out.println(Arrays.toString(key1));
        System.out.println(Arrays.toString(key2));
        System.out.println(Arrays.toString(key3));
        for(Task task : tasks){
            System.out.println(task.getStag());
        }
    }

    @Test
    public void getTask() throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        byte[] key1 = T_Sets_split_memory.TSetGetTag("key1");
        MysqlUtils mu = new MysqlUtils();
        Task task = mu.getTask(Arrays.toString(key1));
        ArrayList<t_item> ts = task.getT();
        for(t_item t : ts){
            byte []e = t.getE();
            MK mk = EDBSetup_keyUtils.getKey();
            byte[] Ke = MyUtils.F(mk.getKs(), "key1");
            System.out.println(MyUtils.decrypt_AES_CBC(Ke,e));
        }
    }

    @Test
    public void saveTaskTest2() throws Exception {
        String keyword = "key1";
        ArrayList<String> filenames = new ArrayList<>();
        filenames.add("ind1");
        filenames.add("ind2");
        filenames.add("ind3");
        Task task = EDBSetup_split_memory.EDBSetup(keyword,filenames);
        MysqlUtils mu = new MysqlUtils();
        mu.saveTask(task);

        keyword = "key2";
        filenames = new ArrayList<>();
        filenames.add("ind1");
        filenames.add("ind2");
        task = EDBSetup_split_memory.EDBSetup(keyword,filenames);
        mu = new MysqlUtils();
        mu.saveTask(task);

        keyword = "key3";
        filenames = new ArrayList<>();
        filenames.add("ind1");
        task = EDBSetup_split_memory.EDBSetup(keyword,filenames);
        mu = new MysqlUtils();
        mu.saveTask(task);
    }

    @Test
    public void deserializeTest(){
        MysqlUtils mu = new MysqlUtils();
        ArrayList<Task> tasks = mu.getAllTask();
        for(Task task : tasks){
            System.out.println("=========================================================");
           // System.out.println(Arrays.toString(task.getStag()));
            ArrayList<t_item> items = task.getT();
            for(t_item item:items){
                System.out.println(item.toString());
            }
            System.out.println("=========================================================");
        }
    }

    @Test
    public void getXSetsTest(){
        MysqlUtils mu = new MysqlUtils();
        ArrayList<String> xSets = mu.getXSets(10,10);
        for(String xSet : xSets){
            System.out.println(xSet);
        }
    }

    @Test
    public void getXSetsTest2() throws Exception{
        MysqlUtils mu = new MysqlUtils();
        int offset = 0;
        long expectedElements = 10000000;
        double falsePositiveRate = 0.01;
        BloomFilter<byte[]> bf = BloomFilter.apply(
                expectedElements,
                falsePositiveRate,
                CanGenerateHashFrom.CanGenerateHashFromByteArray$.MODULE$);

        File file = MyUtils.getFile("tsets_bloom_filter","xset_bf_10000000.bf");
        while(offset<1000000) {
            System.out.println(offset+"~"+(offset+1000));
            ArrayList<String> xSets = mu.getXSets(offset, 1000);
            for(String xSet:xSets){
                bf.add(xSet.getBytes("utf-8"));
            }
            offset += 1000;
        }
        SerializationDemonstrator.serialize(bf,file.getAbsolutePath());
        bf.dispose();
    }
}
