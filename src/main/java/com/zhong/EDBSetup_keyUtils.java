package com.zhong;

import com.zhong.utils.CryptoPrimitives;
import com.zhong.utils.MyUtils;
import com.zhong.utils.SerializationDemonstrator;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.File;

/**
 * 与密钥有关的类
 *
 * @author 张中俊
 **/
public class EDBSetup_keyUtils {

    /**
     * 双线性对
     */
    private static final Pairing pairing = PairingFactory.getPairing("params/curves/a.properties");

    /**
     * Ks是随机函数F的参数
     */
    private static final byte[] Ks = CryptoPrimitives.randomBytes(128 / 8);

    /**
     * Kx KI Kz是Fp的参数 128 比特 = 16 byte
     */
    private static final byte[] KI = CryptoPrimitives.randomBytes(128 / 8);
    private static final byte[] Kz = CryptoPrimitives.randomBytes(128 / 8);
    private static final byte[] Kx = CryptoPrimitives.randomBytes(128 / 8);
    /**
     * 群的生成元
     */
    private static final Element g = pairing.getG1().newRandomElement();

    /**
     * 将密钥写入到文件中
     */
    private static void SerializeKey() {
        File file = MyUtils.getFile("output_keys", "allKeys.keys");
        if (file.exists()) {
            System.out.println("密钥文件已经生成，请从output_keys/allKeys.keys中读取");
            return;
        }
        MK mk = new MK(Kx, KI, Kz, Ks, MyUtils.generateK_T(), new SerializableElement(g));
        SerializationDemonstrator.serialize(mk, file.getAbsolutePath());
    }

    /**
     * 将密钥从文件中读取出来
     *
     * @return 读取出的密钥
     */
    private static MK DeserializeKey() {
        File file = MyUtils.getFile("output_keys", "allKeys.keys");
        MK mk = SerializationDemonstrator.deserialize(file.getAbsolutePath());
        return mk;
    }

    /**
     * 获得EDBSetup算法所需的密钥：<br>
     * 如果是之前没有生成过的话，首先生成，再返回生成的密钥<br>
     * 如果是有现场的密钥，则返回现成的密钥
     * @return 一个可用的密钥
     */
    public static MK getKey() {
        File file = MyUtils.getFile("output_keys", "allKeys.keys");
        if (file.exists()) {
            return EDBSetup_keyUtils.DeserializeKey();
        } else {
            EDBSetup_keyUtils.SerializeKey();
            return EDBSetup_keyUtils.DeserializeKey();
        }
    }

}
