package com.zhong;

import com.zhong.utils.MyUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * @author 张中俊
 * @create 2018-03-31 19:30
 **/
public class SearchProtocol_Split_memory_Test {
    @Test
    public void searchProtocolTest() throws Exception {
        ArrayList<String> kws = new ArrayList<>();
        kws.add("Kaskels");
        Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1(kws);
        ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, "TSets_65536");
        ArrayList<String> ress = SearchProtocol_Split_memory.search_client_2(kws.get(0), res);
        for (String filename : ress) {
            System.out.println(filename);
        }
    }

    @Test
    public void searchProtocolTest2() throws Exception {
        MK mk = EDBSetup_keyUtils.getKey();
        ArrayList<String> kws = new ArrayList<>();
        kws.add("clubnumber13");
        kws.add("clubnumber14");
        kws.add("clubnumber15");
        System.out.println("查询第一阶段 开始");
        Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1(kws);

        System.out.println("查询第二阶段 开始");
        long t1 = System.currentTimeMillis();
        ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, "TSets_65536");
        long t2 = System.currentTimeMillis();
        System.out.println("查询所用时间 " + (t2 - t1) + " ms");

        System.out.println("查询第三阶段 开始");
        ArrayList<String> ress = SearchProtocol_Split_memory.search_client_2(kws.get(0), res);
        for (String filename : ress) {
            System.out.println(filename);
        }
    }

    @Test
    public void searchProtocolTest3() throws Exception {

        //读取测试集
        File testSetFile = MyUtils.getFile("testset", "testSet.txt");
        List<String> lines = IOUtils.readLines(new FileInputStream(testSetFile));

        Map<String, Integer> kws_count = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            Random random = new Random();
            int index = random.nextInt(lines.size());
            String t = lines.get(index);
            kws_count.put(t.split(" ")[0], Integer.parseInt(t.split(" ")[1]));
        }
        //这里将map.entrySet()转换成list
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(kws_count.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            //升序排序
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }

        });

        ArrayList<String> kws = new ArrayList<>();
        for (String s : kws_count.keySet()) {
            kws.add(s);
        }
        int count = kws_count.get(kws.get(0));

        MK mk = EDBSetup_keyUtils.getKey();
        System.out.println("查询第一阶段 开始");
        Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1_version_2(kws, count);

        System.out.println("查询第二阶段 开始");
        long t1 = System.currentTimeMillis();
        ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, "TSets_65536");
        long t2 = System.currentTimeMillis();
        System.out.println("查询所用时间 " + (t2 - t1) + " ms");

        System.out.println("查询第三阶段 开始");
        ArrayList<String> ress = SearchProtocol_Split_memory.search_client_2(kws.get(0), res);
        for (String filename : ress) {
            System.out.println(filename);
        }
    }

    @Test
    public void searchProtocolTest4() throws Exception {
        //读取测试集
        File testSetFile = MyUtils.getFile("testset", "testSet.txt");
        List<String> lines = IOUtils.readLines(new FileInputStream(testSetFile));

        long sum_time = 0;
        for (int i = 0; i < 100; i++) {
            Map<String, Integer> kws_count = new HashMap<>();
            for (int j = 0; j < 3; j++) {
                Random random = new Random();
                int index = random.nextInt(lines.size());
                String t = lines.get(index);
                kws_count.put(t.split(" ")[0], Integer.parseInt(t.split(" ")[1]));
            }
            //这里将map.entrySet()转换成list
            ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(kws_count.entrySet());
            //然后通过比较器来实现排序
            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                //升序排序
                public int compare(Map.Entry<String, Integer> o1,
                                   Map.Entry<String, Integer> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }

            });

            ArrayList<String> kws = new ArrayList<>();
            for (String s : kws_count.keySet()) {
                kws.add(s);
            }
            int count = kws_count.get(kws.get(0));

            //这里是查询部分
            SearchProtocol_Split_memory ssm = new SearchProtocol_Split_memory();
            MK mk = EDBSetup_keyUtils.getKey();
            System.out.println("查询第一阶段 开始");
            Search_Client_1_Output sc1o = ssm.search_client_1_version_2(kws, count);

            System.out.println("查询第二阶段 开始");
            long t1 = System.currentTimeMillis();
            ArrayList<byte[]> res = ssm.search_server(sc1o, "TSets_65536");
            long t2 = System.currentTimeMillis();
            System.out.println("查询所用时间 " + (t2 - t1) + " ms");
            sum_time = sum_time + (t2 - t1);
            if (res == null) {
                continue;
            }

            System.out.println("查询第三阶段 开始");
            ArrayList<String> ress = ssm.search_client_2(kws.get(0), res);
            for (String filename : ress) {
                System.out.println(filename);
            }
        }
        System.out.println("100次查询需要 " + sum_time + " ms");
    }


    @Test
    public void searchProtocolTest5() throws Exception {
        //读取测试集
        File testSetFile = MyUtils.getFile("testset", "testSet.txt");
        List<String> lines = IOUtils.readLines(new FileInputStream(testSetFile));
        //所有需要测试的表
        ArrayList<String> tableNames = new ArrayList<>();
        tableNames.add("TSets_65536");
        tableNames.add("TSets_131072");
        tableNames.add("TSets_262144");
        tableNames.add("TSets_524288");
        tableNames.add("TSets_1048576");
        tableNames.add("TSets_2097152");
        for (String tableName : tableNames) {
            long sum_time = 0;
            for (int i = 0; i < 100; i++) {
                Map<String, Integer> kws_count = new HashMap<>();
                for (int j = 0; j < 3; j++) {
                    Random random = new Random();
                    int index = random.nextInt(lines.size());
                    String t = lines.get(index);
                    kws_count.put(t.split(" ")[0], Integer.parseInt(t.split(" ")[1]));
                }
                //这里将map.entrySet()转换成list
                ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(kws_count.entrySet());
                //然后通过比较器来实现排序
                Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                    //升序排序
                    public int compare(Map.Entry<String, Integer> o1,
                                       Map.Entry<String, Integer> o2) {
                        return o1.getValue().compareTo(o2.getValue());
                    }

                });
                ArrayList<String> kws = new ArrayList<>();
                for (String s : kws_count.keySet()) {
                    kws.add(s);
                }
                int count = kws_count.get(kws.get(0));

                MK mk = EDBSetup_keyUtils.getKey();
                //System.out.println("查询第一阶段 开始");
                Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1_version_2(kws, count);

                //System.out.println("查询第二阶段 开始");
                long t1 = System.currentTimeMillis();
                ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, tableName);
                long t2 = System.currentTimeMillis();
                //System.out.println("查询所用时间 " + (t2 - t1) + " ms");
                sum_time = sum_time + (t2 - t1);
                if (res == null) {
                    continue;
                }

                //System.out.println("查询第三阶段 开始");
                //ArrayList<String> ress = ssm.search_client_2(mk, kws.get(0), res);
                //for (String filename : ress) {
                //     System.out.println(filename);
                // }
            }
            System.out.println(tableName + " 100次查询需要 " + sum_time + " ms");
        }
    }


    @Test
    public void searchProtocolTest8() throws Exception {
        //读取测试集
        File testSetFile = MyUtils.getFile("testset", "testSet.txt");
        List<String> lines = IOUtils.readLines(new FileInputStream(testSetFile));
        ArrayList<String> table_names = new ArrayList<>();
        table_names.add("TSets_32768");
        table_names.add("TSets_65536");
        table_names.add("TSets_131072");
        table_names.add("TSets_262144");
        table_names.add("TSets_524288");
        table_names.add("TSets_1048576");
        for (String tableName : table_names) {
            long sum_t = 0;
            //进行100次查询
            for (int i = 0; i < 1; i++) {
                //要查询的关键字
                List<String> kws = new ArrayList<>();
                int numLeastKeyword = Integer.MAX_VALUE;
                // 3个关键词做 并 查询
                for (int j = 0; j < 3; j++) {
                    Random random = new Random();
                    int index = random.nextInt(lines.size());
                    String t = lines.get(index);
                    String keyword = t.split(" ")[0];
                    int num = Integer.parseInt(t.split(" ")[1]);
                    if (num < numLeastKeyword) {
                        kws.add(0, keyword);
                        numLeastKeyword = num;
                    } else {
                        kws.add(keyword);
                    }
                }

                //System.out.println("查询第一阶段 开始");
                Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1_version_2(kws, numLeastKeyword);
                //System.out.println("查询第二阶段 开始");
                long t1 = System.currentTimeMillis();
                ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, tableName);
                long t2 = System.currentTimeMillis();
                //System.out.println("查询所用时间 " + (t2 - t1) + " ms");
                sum_t = sum_t + (t2 - t1);
                if (res == null) {
                    continue;
                }

                //ArrayList<String> res = DecryptAlgorithm.decrypt(kws.get(0), ws);
                //System.out.println("搜索的关键词："+kws.toString());
                //System.out.println("搜索结果："+res.toString());
            }
            System.out.println("表 " + tableName + " 花费时间：" + (sum_t / 1) + " ms");
        }
    }

    @Test
    public void searchProtocolTest9() throws Exception {

        ArrayList<String> table_names = new ArrayList<>();
        table_names.add("TSets_32768_rand");
        table_names.add("TSets_65536_rand");
        table_names.add("TSets_131072_rand");
        table_names.add("TSets_262144_rand");
        table_names.add("TSets_524288_rand");
        table_names.add("TSets_1048576_rand");
        for (String tableName : table_names) {
            long sum_t = 0;
            //进行100次查询
            for (int i = 0; i < 100; i++) {
                //要查询的关键字
                List<String> kws = new ArrayList<>();
                kws.add("key3");
                kws.add("key1");
                kws.add("key2");

                int numLeastKeyword = 10;

                //System.out.println("查询第一阶段 开始");
                Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1_version_2(kws, numLeastKeyword);
                //System.out.println("查询第二阶段 开始");
                long t1 = System.currentTimeMillis();
                ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, tableName);
                long t2 = System.currentTimeMillis();
                //System.out.println("查询所用时间 " + (t2 - t1) + " ms");
                sum_t = sum_t + (t2 - t1);

                //下面是解密的部分
                if (res == null) {
                    continue;
                }else {
                    //List<String> inds = SearchProtocol_Split_memory.search_client_2(kws.get(0),res);
                    //System.out.println("搜索结果"+res.size());
                    //System.out.println("搜索的关键词："+kws.toString());
                    //System.out.println("搜索结果："+inds.toString());
                }
            }
            System.out.println("表 " + tableName + " 花费时间：" + (sum_t / 100) + " ms");
        }
    }
    @Test
    public void searchProtocolTest10() throws Exception {

        ArrayList<String> table_names = new ArrayList<>();
        table_names.add("TSets_32768_rand");
        table_names.add("TSets_65536_rand");
        table_names.add("TSets_131072_rand");
        table_names.add("TSets_262144_rand");
        table_names.add("TSets_524288_rand");
        table_names.add("TSets_1048576_rand");
        for (String tableName : table_names) {
            long sum_t = 0;
            //进行100次查询
            for (int i = 0; i < 100; i++) {
                //要查询的关键字
                List<String> kws = new ArrayList<>();
                kws.add("clubnumber10");
                kws.add("clubnumber11");
                kws.add("clubnumber12");

                int numLeastKeyword = 9;

                //System.out.println("查询第一阶段 开始");
                Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1_version_2(kws, numLeastKeyword);
                //System.out.println("查询第二阶段 开始");
                long t1 = System.currentTimeMillis();
                ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, tableName);
                long t2 = System.currentTimeMillis();
                //System.out.println("查询所用时间 " + (t2 - t1) + " ms");
                sum_t = sum_t + (t2 - t1);

                //下面是解密的部分
                if (res == null) {
                    continue;
                }else {
                    //List<String> inds = SearchProtocol_Split_memory.search_client_2(kws.get(0),res);
                    //System.out.println("搜索结果"+res.size());
                    //System.out.println("搜索的关键词："+kws.toString());
                    //System.out.println("搜索结果："+inds.toString());
                }
            }
            System.out.println("表 " + tableName + " 花费时间：" + (sum_t / 100) + " ms");
        }
    }


    @Test
    public void searchProtocolTest11() throws Exception {

        ArrayList<String> table_names = new ArrayList<>();
        table_names.add("TSets_32768_rand");
        table_names.add("TSets_65536_rand");
        table_names.add("TSets_131072_rand");
        table_names.add("TSets_262144_rand");
        table_names.add("TSets_524288_rand");
        table_names.add("TSets_1048576_rand");
        for (String tableName : table_names) {
            long sum_t = 0;
            //进行100次查询
            for (int i = 0; i < 2; i++) {
                //要查询的关键字
                List<String> kws = new ArrayList<>();
                kws.add("oP");
                kws.add("nk");

                int numLeastKeyword = 4;

                //System.out.println("查询第一阶段 开始");
                Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1_version_2(kws, numLeastKeyword);
                //System.out.println("查询第二阶段 开始");
                long t1 = System.currentTimeMillis();
                ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, tableName);
                long t2 = System.currentTimeMillis();
                //System.out.println("查询所用时间 " + (t2 - t1) + " ms");
                sum_t = sum_t + (t2 - t1);

                //下面是解密的部分
                if (res == null) {
                    continue;
                }else {
                    List<String> inds = SearchProtocol_Split_memory.search_client_2(kws.get(0),res);
                    System.out.println("搜索结果"+res.size());
                    System.out.println("搜索的关键词："+kws.toString());
                    System.out.println("搜索结果："+inds.toString());
                }
            }
            System.out.println("表 " + tableName + " 花费时间：" + (sum_t / 100) + " ms");
        }
    }



    @Test
    public void search_serverTest12() throws Exception {
        ArrayList<String> table_names = new ArrayList<>();
        table_names.add("TSets_32768_rand");
        table_names.add("TSets_65536_rand_v2");
        table_names.add("TSets_131072_rand_v2");
        table_names.add("TSets_262144_rand_v2");
        table_names.add("TSets_524288_rand_v2");
        table_names.add("TSets_1048576_rand_v2");
        for (String tableName : table_names) {
            System.out.println("====正在搜索："+tableName+"======");
            long sum_t = 0;

            //要查询的关键字
            List<String> kws = new ArrayList<>();
            kws.add("clubnumber10");
            kws.add("clubnumber11");
            kws.add("clubnumber12");
            int numLeastKeyword = 9;

            //System.out.println("查询第一阶段 开始");
            Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1_version_2(kws, numLeastKeyword);
            //System.out.println("查询第二阶段 开始");
            long t1 = System.currentTimeMillis();
            ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, tableName);
            long t2 = System.currentTimeMillis();
            //System.out.println("查询所用时间 " + (t2 - t1) + " ms");
            sum_t = sum_t + (t2 - t1);

            //下面是解密的部分
            if (res == null) {
                continue;
            } else {
                List<String> inds = SearchProtocol_Split_memory.search_client_2(kws.get(0),res);
                System.out.println("搜索结果"+res.size());
                System.out.println("搜索的关键词："+kws.toString());
                System.out.println("搜索结果："+inds.toString());
            }
            System.out.println("===============");
        }
    }




    @Test
    public void searchProtocolTest13() throws Exception {

        ArrayList<String> table_names = new ArrayList<>();
        table_names.add("TSets_32768_rand_v2");
        table_names.add("TSets_65536_rand_v2");
        table_names.add("TSets_131072_rand_v2");
        table_names.add("TSets_262144_rand_v2");
        table_names.add("TSets_524288_rand_v2");
        table_names.add("TSets_1048576_rand_v2");
        for (String tableName : table_names) {
            long sum_t = 0;
            //进行100次查询
            for (int i = 0; i < 100; i++) {
                //要查询的关键字
                List<String> kws = new ArrayList<>();
                kws.add("clubnumber10");
                kws.add("clubnumber11");
                kws.add("clubnumber12");
                int numLeastKeyword = 9;

                //System.out.println("查询第一阶段 开始");
                Search_Client_1_Output sc1o = SearchProtocol_Split_memory.search_client_1_version_2(kws, numLeastKeyword);
                //System.out.println("查询第二阶段 开始");
                long t1 = System.currentTimeMillis();
                ArrayList<byte[]> res = SearchProtocol_Split_memory.search_server(sc1o, tableName);
                long t2 = System.currentTimeMillis();
                //System.out.println("查询所用时间 " + (t2 - t1) + " ms");
                sum_t = sum_t + (t2 - t1);

                //下面是解密的部分
                if (res == null) {
                    continue;
                }else {
                   // List<String> inds = SearchProtocol_Split_memory.search_client_2(kws.get(0),res);
                    //System.out.println("搜索结果"+res.size());
                   // System.out.println("搜索的关键词："+kws.toString());
                   // System.out.println("搜索结果："+inds.toString());
                }
            }
            System.out.println("表 " + tableName + " 花费时间：" + (sum_t / 100) + " ms");
        }
    }




}
