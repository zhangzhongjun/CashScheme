package com.zhong.rm;

import bloomfilter.mutable.BloomFilter;
import com.zhong.concurrent.Task;
import com.zhong.t_item;
import com.zhong.utils.MyUtils;
import com.zhong.utils.SerializationDemonstrator;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 *
 * @author 张中俊
 **/
public class MysqlUtils {
    private static Connection conn;                                      //连接
    private static PreparedStatement pres;                                      //PreparedStatement对象
   private static BloomFilter<byte[]> bf;
    static {
        File file = MyUtils.getFile("tsets_bloom_filter","xset_bf_10000000.bf");
        bf = SerializationDemonstrator.deserialize(file.getAbsolutePath());

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");              //加载驱动
            System.out.println("数据库加载成功!!!");
            String url = "jdbc:mysql://10.170.32.244:3306/vsse";
            //String url = "jdbc:mysql://127.0.0.1:3306/vsse?serverTimezone=UTC";
            String user = "root";
            String password = "root";

            conn = DriverManager.getConnection(url, user, password); //建立连接
            System.out.println("数据库连接成功!!!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向数据库中的表TSets中插入数据
     *
     * @param task
     *         要插入的数据
     */
    public static void saveTask(Task task) {
        String sql_TSets = "insert into TSets(stag,t) values(?,?)";
        String sql_XSets = "insert into XSets(xSet) values(?)";
        try {
            //开始保存XSet
            pres = conn.prepareStatement(sql_XSets);
            for (int i = 0; i < task.getXSets().size(); i++) {
                pres.setString(1, task.getXSets().get(i));
                pres.addBatch(); //实现批量插入
            }
            pres.executeBatch();//批量插入到数据库中

            //开始保存TSets
            pres = conn.prepareStatement(sql_TSets);
            pres.setString(1, task.getStag());
            byte[] temp = MyUtils.msg2Byte(task.getT());
            if(temp.length>65*1024){
                if (pres != null)
                    pres.close();
                System.out.println("t太大了,t里面有 "+task.getT().size()+" 个t_item");
                return ;
            }
            pres.setBytes(2, temp);
            try {
                pres.execute();
            }catch(Exception e){
                if (pres != null)
                    pres.close();
                System.out.println("插入表TSets时发生异常 " + e);
                return;
            }
            if (pres != null)
                pres.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向指定的表中插入Task
     * @param task 要插入的数据
     * @param tableName 要插入的表
     */
    public static void saveTask(Task task, String tableName) {
        String sql_TSets = "insert into "+tableName+"(stag,t) values(?,?)";
        String sql_XSets = "insert into XSets(xSet) values(?)";
        try {
            //开始保存XSet
            pres = conn.prepareStatement(sql_XSets);
            for (int i = 0; i < task.getXSets().size(); i++) {
                pres.setString(1, task.getXSets().get(i));
                pres.addBatch(); //实现批量插入
            }
            pres.executeBatch();//批量插入到数据库中

            //开始保存TSets
            pres = conn.prepareStatement(sql_TSets);
            pres.setString(1, task.getStag());
            byte[] temp = MyUtils.msg2Byte(task.getT());
            if(temp.length>65*1024){
                if (pres != null)
                    pres.close();
                System.out.println("t太大了,t里面有 "+task.getT().size()+" 个t_item");
                return ;
            }
            pres.setBytes(2, temp);
            try {
                pres.execute();
            }catch(Exception e){
                if (pres != null)
                    pres.close();
                System.out.println("插入表TSets时发生异常 " + e);
                return;
            }
            if (pres != null)
                pres.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据库中读出存入的对象
     * @return 得到所有的Task
     */
    public static ArrayList<Task> getAllTask() {
        ArrayList<Task> tasks = new ArrayList<>();

        String sql_TSets = "select * from TSets";
        try {
            pres = conn.prepareStatement(sql_TSets);

            ResultSet res = pres.executeQuery();
            while (res.next()) {
                String stag = res.getString(1);
                byte[] b2 = res.getBytes(2);
                ArrayList<t_item> t = (ArrayList<t_item>) MyUtils.byte2Msg(b2);
                Task task = new Task(stag, t, null);
                tasks.add(task);
            }

            if (pres != null)
                pres.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /**
     * 通过指定的stag查询
     *
     * @param key 指定的stag
     * @return 该stag对应的task
     */
    public static Task getTask(String key){
        Task task = null;
        String sql_TSets = "select * from TSets WHERE stag=(?)";
        try {
            pres = conn.prepareStatement(sql_TSets);
            pres.setString(1,key);
            ResultSet res = pres.executeQuery();

            while (res.next()) {
                String stag = res.getString(1);
                byte[] b2 = res.getBytes(2);
                ArrayList<t_item> t = (ArrayList<t_item>) MyUtils.byte2Msg(b2);
                task = new Task(stag, t, null);
            }

            if (pres != null)
                pres.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }

    /**
     * 通过指定的stag 在特定的表中查询
     *
     * @param key 指定的stag
     * @param table_name 指定的表名
     * @return 该stag对应的task
     */
    public static Task getTask(String key, String table_name){
        Task task = null;
        String sql_TSets = "select * from "+table_name+" WHERE stag=(?) limit 1";
        try {
            pres = conn.prepareStatement(sql_TSets);
            pres.setString(1,key);
            ResultSet res = pres.executeQuery();

            while (res.next()) {
                String stag = res.getString(1);
                byte[] b2 = res.getBytes(2);
                ArrayList<t_item> t = (ArrayList<t_item>) MyUtils.byte2Msg(b2);
                task = new Task(stag, t, null);
            }

            if (pres != null)
                pres.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }

    /**
     * 使用bloom filter测试xset是否在表XSet中
     *
     * @param key
     *         要测试的数据库
     *
     * @return key是否包含在bloom filter中
     *
     * @throws UnsupportedEncodingException
     *         异常
     */
    public static boolean isInXSets_v2(String key) throws UnsupportedEncodingException {
        return bf.mightContain(key.getBytes("utf-8"));
    }

    /**
     * 通过查询数据表判断key是否在XSets中
     *
     * @param key
     *         要查询的关键词
     *
     * @return 是否在表Xsets中
     */
    public static boolean isInXSets(String key){
        Task task = null;
        String sql_XSets = "select count(*) from XSets WHERE xSet=(?) limit 1 ";
        try {
            pres = conn.prepareStatement(sql_XSets);
            pres.setString(1,key);

            ResultSet res = pres.executeQuery();
            res.next();
            int count = res.getInt(1);

            if (pres != null)
                pres.close();

            if(count==0){
                return false;
            }else{
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获得表Xsets中的内容
     *
     * @param offset
     *         偏移量
     * @param row_count
     *         要获得的行数
     *
     * @return Xsets中的内容
     */
    public ArrayList<String> getXSets(int offset,int row_count){
        ArrayList<String> xSets = new ArrayList<>();
        Task task = null;
        String sql_XSets = "select * from XSets limit "+offset+","+row_count;
        try {
            pres = conn.prepareStatement(sql_XSets);

            ResultSet res = pres.executeQuery();
            while (res.next()) {
                String xSet = res.getString(1);
                xSets.add(xSet);
            }
            if (pres != null)
                pres.close();
            return xSets;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

