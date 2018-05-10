package com.zhong.rm;

import org.junit.Test;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author 张中俊
 * @create 2018-03-29 15:25
 **/
public class MySqlTest {
    private static Connection conn;                                      //连接
    private PreparedStatement pres2;                                      //PreparedStatement对象
    @Test
    public void saveTest() throws ClassNotFoundException, SQLException, UnsupportedEncodingException {
        Class.forName("com.mysql.cj.jdbc.Driver");              //加载驱动
        System.out.println("数据库加载成功!!!");
        String url="jdbc:mysql://10.170.32.244:3306/vsse";
        String user="root";
        String password="root";

        conn= DriverManager.getConnection(url,user,password); //建立连接
        System.out.println("数据库连接成功!!!");

        String sql_TSets = "insert into TSets(stag,t) values(?,?)";

        byte[] arr = "hello".getBytes("utf-8");
        ArrayList<String> filenames = new ArrayList<>();
        for(int i=0;i<10;i++){
            filenames.add("ind"+i);
        }
        byte[] inds = MySqlTest.msg2Byte(filenames);

        pres2 = conn.prepareStatement(sql_TSets);
        pres2.setBytes(1,arr);
        pres2.setBytes(2,inds);
        pres2.execute();

        if(pres2!=null)
            pres2.close();
    }

    @Test
    public void getTest() throws ClassNotFoundException, SQLException, UnsupportedEncodingException {
        Class.forName("com.mysql.cj.jdbc.Driver");              //加载驱动
        System.out.println("数据库加载成功!!!");
        String url = "jdbc:mysql://10.170.32.244:3306/vsse";
        String user = "root";
        String password = "root";

        conn = DriverManager.getConnection(url, user, password); //建立连接
        System.out.println("数据库连接成功!!!");

        String sql_TSets = "select * from TSets";

        pres2 = conn.prepareStatement(sql_TSets);

        ResultSet res = pres2.executeQuery();
        while (res.next()) {
           byte[] b1 = res.getBytes(1);
            byte[] arr = "hello".getBytes("utf-8");
            System.out.println(Arrays.toString(arr));
            System.out.println(Arrays.toString(b1));
            byte[]b2 = res.getBytes(2);
            ArrayList<String> inds = (ArrayList<String>) byte2Msg(b2);
            System.out.println(inds.toString());
        }
    }

    /**
     * 将对象转化为数组
     *
     * @param o 待转化的对象
     * @return byte[]数组
     */
    public static  byte[] msg2Byte(Object o){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(baos);
            out.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    /**
     * 将byte[]数组转化为对象
     *
     * @param bytes 待转化的数组
     * @return 转化为的对象
     */
    public static Object byte2Msg(byte[] bytes){
        ByteArrayInputStream bais;
        ObjectInputStream in = null;
        try{
            bais = new ByteArrayInputStream(bytes);
            in = new ObjectInputStream(bais);

            return in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally{
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
            e.printStackTrace();
                }
            }
        }
        return null;
    }
}