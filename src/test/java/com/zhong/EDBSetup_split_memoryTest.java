package com.zhong;

import com.zhong.concurrent.Task;
import com.zhong.rm.MysqlUtils;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author 张中俊
 * @create 2018-03-31 15:24
 **/
public class EDBSetup_split_memoryTest {
    @Test
    public void t1() throws Exception {
        String keyword = "keyword";
        ArrayList<String> filenames = new ArrayList<>();
        for(int i=0;i<10;i++){
            filenames.add("ind"+i);
        }
        Task task = EDBSetup_split_memory.EDBSetup(keyword,filenames);
        System.out.println(task.getStag().length());
        System.out.println(task.getStag());
        ArrayList<String> xSets = task.getXSets();
        for(String xSet : xSets){
            System.out.println(xSet.length());
            System.out.println(xSet);
        }
        task.getT();
    }

    @Test
    public void insertItemTest() throws Exception {
        String keyword = "key1";
        ArrayList<String> filenames = new ArrayList<>();
        for(int i=0;i<100;i++){
            filenames.add("ind"+i);
        }
        Task task = EDBSetup_split_memory.EDBSetup(keyword,filenames);
        MysqlUtils.saveTask(task,"TSets_32768");
        MysqlUtils.saveTask(task,"TSets_65536");
        MysqlUtils.saveTask(task,"TSets_131072");
        MysqlUtils.saveTask(task,"TSets_262144");
        MysqlUtils.saveTask(task,"TSets_524288");
        MysqlUtils.saveTask(task,"TSets_1048576");

        keyword = "key2";
        filenames = new ArrayList<>();
        for(int i=0;i<15;i++){
            filenames.add("ind"+i);
        }
        task = EDBSetup_split_memory.EDBSetup(keyword,filenames);
        MysqlUtils.saveTask(task,"TSets_32768");
        MysqlUtils.saveTask(task,"TSets_65536");
        MysqlUtils.saveTask(task,"TSets_131072");
        MysqlUtils.saveTask(task,"TSets_262144");
        MysqlUtils.saveTask(task,"TSets_524288");
        MysqlUtils.saveTask(task,"TSets_1048576");


        keyword = "key3";
        filenames = new ArrayList<>();
        for(int i=0;i<10;i++){
            filenames.add("ind"+i);
        }
        task = EDBSetup_split_memory.EDBSetup(keyword,filenames);
        MysqlUtils.saveTask(task,"TSets_32768");
        MysqlUtils.saveTask(task,"TSets_65536");
        MysqlUtils.saveTask(task,"TSets_131072");
        MysqlUtils.saveTask(task,"TSets_262144");
        MysqlUtils.saveTask(task,"TSets_524288");
        MysqlUtils.saveTask(task,"TSets_1048576");
    }
}

