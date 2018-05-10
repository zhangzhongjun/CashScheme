package com.zhong;

import com.zhong.rm.MysqlUtils;
import com.zhong.utils.MyUtils;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索的类
 *
 * @author 张中俊
 **/
public class SearchProtocol_Split_memory {
    private static MK mk;
    private static Pairing pairing;
    static {
        mk = EDBSetup_keyUtils.getKey();
        pairing = PairingFactory.getPairing("params/curves/a.properties");
    }

    /**
     * 在搜索中客户端需要做的事情 1
     *
     * @param kws 要搜索的关键词
     * @return 返回两部分内容，第一部分是 w1 的 stag，第二部分是搜索的token
     * @throws Exception 异常
     */
    public static Search_Client_1_Output search_client_1(ArrayList<String> kws) throws Exception {
        //群的生成元
        Element g = mk.getG();
        //w1的映射结果
        byte[] stag = T_Sets_split_memory.TSetGetTag(kws.get(0));
        // 计算token
        Element xtoken[][] = new Element[100][kws.size()];
        //预测：最多有c个文件包含关键词w1
        // until the server stops
        for (int c = 0; c < 100; c++) {
            for (int i = 1; i < kws.size(); i++) {
                Element left =MyUtils.Fp(pairing,mk.getKz(),(kws.get(0)+c+"").getBytes("utf-8"));
                Element right = MyUtils.Fp(pairing,mk.getKx(),kws.get(i).getBytes("utf-8"));
                Element res = g.duplicate().powZn(left.duplicate().mul(right));
                xtoken[c][i] = res;
            }
        }
        return new Search_Client_1_Output(stag,xtoken);
    }

    /**
     * 在搜索中客户端需要做的事情 1
     *
     * @param kws 要搜索的关键词，要求是结果排序的
     * @return 返回两部分内容，第一部分是 w1 的 stag，第二部分是搜索的token
     * @throws Exception 异常
     */
    public static Search_Client_1_Output search_client_1_version_2(List<String> kws, int count) throws Exception {
        //群的生成元
        Element g = mk.getG();

        //w1的映射结果
        byte[] stag = T_Sets_split_memory.TSetGetTag(kws.get(0));
        // 计算token
        Element xtoken[][] = new Element[count][kws.size()];
        //预测：最多有c个文件包含关键词w1
        // until the server stops
        for (int c = 0; c < count; c++) {
            for (int i = 1; i < kws.size(); i++) {
                Element left =MyUtils.Fp(pairing,mk.getKz(),(kws.get(0)+c+"").getBytes("utf-8"));
                Element right = MyUtils.Fp(pairing,mk.getKx(),kws.get(i).getBytes("utf-8"));

                Element res = g.duplicate().powZn(left.duplicate().mul(right));
                xtoken[c][i] = res;
            }
        }
        return new Search_Client_1_Output(stag,xtoken);
    }

    /**
     * 搜索中服务器需要做的事情
     * @param search_client_1_output 客户端提交的东西
     * @return 搜索的结果，是密文形式的
     */
    public static ArrayList<byte[]> search_server(Search_Client_1_Output search_client_1_output, String table_name) throws UnsupportedEncodingException {
        //搜索的结果
        ArrayList<byte[]> es = new ArrayList<>();

        ArrayList<t_item> t = T_Sets_split_memory.TSetRetrieve(search_client_1_output.getStag(),table_name);
        //mysql的utils类
        MysqlUtils mu = new MysqlUtils();
        if(t==null){
            //System.out.println("w1不包含在TSets中");
            return null;
        }
        for(int c=0;c<t.size();c++){
            //要求全部是真
            boolean flag2 = true;
            t_item tt = t.get(c);
            Element y = tt.getY().getElement();
            for(int i=1;i<search_client_1_output.getXtoken()[c].length;i++){
                Element e = search_client_1_output.getXtoken()[c][i].duplicate();
                Element ee = e.duplicate().powZn(y);

                //System.out.println(ee.toString());
                flag2 = flag2 && MysqlUtils.isInXSets_v2(ee.toString());
                if(!flag2){
                    break;
                }
            }
            if (flag2) {
                es.add(tt.getE());
            }
        }
        return es;
    }

    /**
     * 在搜索中客户端需要做的事情 2
     *
     * @param w1 搜索的关键字的出现次数最少的
     * @param es 搜索得到的文件名的密文
     * @return 搜索得到的文件名的明文
     * @throws IOException 异常
     * @throws NoSuchAlgorithmException 异常
     * @throws NoSuchProviderException 异常
     * @throws InvalidKeyException 异常
     * @throws InvalidAlgorithmParameterException 异常
     * @throws NoSuchPaddingException 异常
     */
    public static ArrayList<String> search_client_2(String w1,ArrayList<byte[]> es) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException {
        ArrayList<String> inds = new ArrayList<>();
        // ke 是用来加密文件名的Identifier 的key
        byte[] Ke = MyUtils.F(mk.getKs(), w1);
        for(byte[] e : es){
            String ind = MyUtils.decrypt_AES_CBC(Ke,e);
            inds.add(ind);
        }
        return inds;
    }
}

/**
 * 客户端在第1阶段的输出
 */
class Search_Client_1_Output {
    /**
     * w1 的stag
     */
    byte[] stag;
    /**
     * 搜索的令牌
     */
    Element[][] xtoken;

    public Search_Client_1_Output(byte[] stag, Element[][] xtoken) {
        this.stag = stag;
        this.xtoken = xtoken;
    }

    public byte[] getStag() {
        return stag;
    }

    public Element[][] getXtoken() {
        return xtoken;
    }
}