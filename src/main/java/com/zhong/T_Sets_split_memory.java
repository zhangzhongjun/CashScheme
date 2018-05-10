package com.zhong;

import com.zhong.concurrent.Task;
import com.zhong.rm.MysqlUtils;
import com.zhong.utils.MyUtils;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * TSet = tuple-set<br>
 * tuple是所有的 关键词 索引的结果（注：这里的索引指的是使用PRF）<br>
 * set 是该关键词对应的文件名的密文
 *
 *
 * @author 张中俊
 * @create 2018-03-15 17:28
 **/
public class T_Sets_split_memory implements Serializable{
    public static MK mk = null;
    static {
        mk = EDBSetup_keyUtils.getKey();
    }
    /**
     * key是关键词通过PRF映射之后的结果<br>
     * value是该关键词生成的TSet
     */
    private HashMap<byte[],ArrayList<t_item>> T;

    public HashMap<byte[], ArrayList<t_item>> getT() {
        return T;
    }

    /**
     * 客户端<br>
     * 算法2：生成搜索所需的stag
     * @param w  要搜索的关键词
     * @return 搜索的stag，是搜索的凭证
     * @throws UnsupportedEncodingException 异常
     * @throws NoSuchAlgorithmException 异常
     */
    public static  byte[] TSetGetTag(String w) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] stag = MyUtils.F(mk.getKT(),w);
        return stag;
    }

    /**
     * 服务器端：<br>
     * 算法3：搜索
     * @param stag 要查询的关键字的stag
     * @return 查询到的结果
     */
    public static  ArrayList<t_item> TSetRetrieve(byte[] stag, String table_name){
        MysqlUtils mu = new MysqlUtils();
        Task task = mu.getTask(Arrays.toString(stag),table_name);
        if(task==null){
            return null;
        }else{
            return task.getT();
        }
    }

    /**
     * 客户端：<br>
     * 算法3：对搜索结果进行解密
     * @param w 查询的关键词
     * @param miwens 服务器返回的数据
     * @return 解密之后的结果
     * @throws IOException 异常
     * @throws NoSuchAlgorithmException 异常
     * @throws NoSuchProviderException 异常
     * @throws InvalidKeyException 异常
     * @throws InvalidAlgorithmParameterException 异常
     * @throws NoSuchPaddingException 异常
     */
    public  static ArrayList<String> TSetDecode(String w,ArrayList<byte[]> miwens) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        ArrayList<String> res = new ArrayList<String>();
        // 加密文件名所需的密钥
        byte[] K_e = MyUtils.F(mk.getKs(),w);
        for(byte[] miwen : miwens){
            res.add(MyUtils.decrypt_AES_CBC(K_e,miwen));
        }
        return res;
    }

    public T_Sets_split_memory(HashMap<byte[], ArrayList<t_item>> t) {
        T = t;
    }
}
