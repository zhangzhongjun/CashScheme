package com.zhong;

import com.zhong.concurrent.Task;
import com.zhong.utils.MyUtils;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/**
 * 算法1：生成索引文件
 *
 * @author 张中俊
 */
public class EDBSetup_split_memory {
    /**
     * 所有的密钥信息
     */
    private final static MK mk = EDBSetup_keyUtils.getKey() ;
    /**
     * 双线性对
     */
    private final static  Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");


    /**
     * 算法1
     *
     * @param w 要生成索引的关键词
     * @param inds 包含关键词w的所有文件的identifier的明文
     * @throws Exception 抛出的异常
     */
    public static Task EDBSetup(final String w, final Collection<String> inds) throws Exception {
        /**
         * XSet
         */
        ArrayList<String> XSets = new ArrayList<String>();

        /**
         * key是关键字，value是该关键词对应的t_set
         */
        HashMap<String,ArrayList<t_item>> T = new HashMap<>();

        /**
         * 密文 +  t-set索引
         */
        ArrayList<t_item> t = new ArrayList<>();

        /**
         * 计数器 用于计数该关键词对应的文件个数
          */
        int c = 0;

        /**
         * ke 是用来加密文件名的Identifier 的key
         */
        byte[] Ke = MyUtils.F(mk.getKs(), w);

        /**
         * 对于包含该关键词的每个文件名
         */
        for (final String ind : inds) {
            // 计算TSet
            final Element xind = MyUtils.Fp(pairing, mk.getKI(), ind.getBytes("utf-8"));
            final Element z = MyUtils.Fp(pairing, mk.getKz(),(w+c+"").getBytes("utf-8"));
            Element y = xind.duplicate().mul(z.duplicate().invert());
            // e是加密之后的文件名
            final byte[] e = MyUtils.encrypt_AES_CBC(Ke, ind);

            t.add(new t_item(e, new SerializableElement(y)));

            // 计算XSet
            Element t1 = MyUtils.Fp(pairing, mk.getKx(), w.getBytes("utf-8"));
            Element t2 = t1.duplicate().mul(xind);
            Element res = mk.getG().duplicate().powZn(t2.duplicate());
            XSets.add(new SerializableElement(res).getElement().toString());

            c++;
        }//for每个文件名 结束

        byte stag[] = T_Sets_split_memory.TSetGetTag(w);

        return new Task(Arrays.toString(stag),t,XSets);
    }

}
