package com.zhong;

import bloomfilter.CanGenerateHashFrom;
import bloomfilter.mutable.BloomFilter;
import com.zhong.concurrent.Task;
import com.zhong.utils.MyUtils;
import com.zhong.utils.SerializationDemonstrator;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * @author 张中俊
 * @create 2018-04-03 22:58
 **/
public class BloomFilterTest {
    @Test
    public void t1(){
        long expectedElements = 10000000;
        double falsePositiveRate = 0.1;
        BloomFilter<byte[]> bf = BloomFilter.apply(
                expectedElements,
                falsePositiveRate,
                CanGenerateHashFrom.CanGenerateHashFromByteArray$.MODULE$);

        byte[] element = new byte[100];
        bf.add(element);
        System.out.println(bf.mightContain(element));
        bf.dispose();
    }

    @Test
    public void serializeTest() throws UnsupportedEncodingException {
        long expectedElements = 10000000;
        double falsePositiveRate = 0.01;
        BloomFilter<byte[]> bf = BloomFilter.apply(
                expectedElements,
                falsePositiveRate,
                CanGenerateHashFrom.CanGenerateHashFromByteArray$.MODULE$);

        for(int i=0;i<10000000-5000;i++) {
            String str = "ind"+i;
            byte[] element = str.getBytes("utf-8");
            bf.add(element);
        }
        File file = MyUtils.getFile("tsets_bloom_filter","xset_bf_10000000.bf");
        SerializationDemonstrator.serialize(bf,file.getAbsolutePath());
        bf.dispose();
    }


    @Test
    public void serializeTest2() throws UnsupportedEncodingException {
        long expectedElements = 30;
        double falsePositiveRate = 0.1;
        BloomFilter<byte[]> bf = BloomFilter.apply(
                expectedElements,
                falsePositiveRate,
                CanGenerateHashFrom.CanGenerateHashFromByteArray$.MODULE$);

        for(int i=0;i<30;i++) {
            String str = "ind"+i;
            byte[] element = str.getBytes("utf-8");
            bf.add(element);
        }
        File file = MyUtils.getFile("tsets_bloom_filter","xset_bf_30.bf");
        SerializationDemonstrator.serialize(bf,file.getAbsolutePath());
        bf.dispose();
    }


    @Test
    public void deserializeTest() throws UnsupportedEncodingException {
        File file = MyUtils.getFile("tsets_bloom_filter","xset_bf_10000000.bf");
        BloomFilter<byte[]>bf = SerializationDemonstrator.deserialize(file.getAbsolutePath());
        for(int i=0;i<100;i++) {
            System.out.println("ind" + i + " " + bf.mightContain(("ind" + i).getBytes("utf-8")));
        }
        for(int i=10000000-10000;i<10000000;i++){
            System.out.println("ind" + i + " " + bf.mightContain(("ind" + i).getBytes("utf-8")));
        }
    }

    @Test
    public void deserializeTest2() throws UnsupportedEncodingException {
        File file = MyUtils.getFile("tsets_bloom_filter","xset_bf_10000000.bf");
        BloomFilter<byte[]>bf = SerializationDemonstrator.deserialize(file.getAbsolutePath());
        String s = "2380861030071907131608961606385712952712819915716076756892477422633644570634315473266678896243141038049641201426050271253500291289947213077678140573586014,512057974343840931912874138338913094775819858068236740696392012601625663885297493078797098279030074503785042488406511505251998590496239153172622268377697,0";
        byte [] b = s.getBytes("utf-8");
        System.out.println(bf.mightContain(b));
    }
    @Test
    public void serializeTest3() throws UnsupportedEncodingException {
        File file = MyUtils.getFile("tsets_bloom_filter","test.bf");
        long expectedElements = 10000000;
        double falsePositiveRate = 0.01;
        BloomFilter<byte[]> bf = BloomFilter.apply(
                expectedElements,
                falsePositiveRate,
                CanGenerateHashFrom.CanGenerateHashFromByteArray$.MODULE$);
        bf.add("ind1".getBytes("utf-8"));
        System.out.println(bf.mightContain("ind1".getBytes("utf-8")));
        SerializationDemonstrator.serialize(bf,file.getAbsolutePath());
    }
    @Test
    public void deserializeTest3() throws UnsupportedEncodingException {
        File file = MyUtils.getFile("tsets_bloom_filter","test.bf");
        BloomFilter<byte[]>bf = SerializationDemonstrator.deserialize(file.getAbsolutePath());
        System.out.println(bf.mightContain("ind1".getBytes("utf-8")));
        System.out.println(bf.mightContain("ind2".getBytes("utf-8")));
        bf.add("ind2".getBytes("utf-8"));
        System.out.println(bf.mightContain("ind2".getBytes("utf-8")));
        SerializationDemonstrator.serialize(bf,file.getAbsolutePath());
    } @Test
    public void deserializeTest4() throws UnsupportedEncodingException {
        File file = MyUtils.getFile("tsets_bloom_filter","test.bf");
        BloomFilter<byte[]>bf = SerializationDemonstrator.deserialize(file.getAbsolutePath());
        System.out.println(bf.mightContain("ind1".getBytes("utf-8")));
        System.out.println(bf.mightContain("ind2".getBytes("utf-8")));
    }

    @Test
    public void insertItemTest() throws Exception {
        File file = MyUtils.getFile("tsets_bloom_filter","xset_bf_10000000.bf");
        BloomFilter<byte[]>bf = SerializationDemonstrator.deserialize(file.getAbsolutePath());

        String keyword = "key1";
        ArrayList<String> filenames = new ArrayList<>();
        for(int i=0;i<100;i++){
            filenames.add("ind"+i);
        }
        Task task = EDBSetup_split_memory.EDBSetup(keyword,filenames);
        for(int i=0;i<task.getXSets().size();i++) {
            bf.add(task.getXSets().get(i).getBytes("utf-8"));
        }

        keyword = "key2";
        filenames = new ArrayList<>();
        for(int i=0;i<15;i++){
            filenames.add("ind"+i);
        }
        task = EDBSetup_split_memory.EDBSetup(keyword,filenames);
        for(int i=0;i<task.getXSets().size();i++) {
            bf.add(task.getXSets().get(i).getBytes("utf-8"));
        }


        keyword = "key3";
        filenames = new ArrayList<>();
        for(int i=0;i<10;i++){
            filenames.add("ind"+i);
        }
        task = EDBSetup_split_memory.EDBSetup(keyword,filenames);
        for(int i=0;i<task.getXSets().size();i++) {
            bf.add(task.getXSets().get(i).getBytes("utf-8"));
        }
        SerializationDemonstrator.serialize(bf,file.getAbsolutePath());
    }
}
