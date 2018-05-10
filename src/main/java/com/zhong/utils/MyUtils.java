package com.zhong.utils;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.*;

public class MyUtils {

    // 获得resources文件夹下的文件
    public static File getFile(String dirname, String fileName) {
        String path = System.getProperty("user.dir");
        path = path + File.separator + dirname;
        File file = new File(path, fileName);
        return file;
    }

    /**
     * PRF  {0,1}^lambda X {0,1}^lambda -> {0,1}^lambda
     * @param Ks 密钥
     * @param w 要加密的字符串
     * @return 由密钥和字符串决定的
     * @throws NoSuchAlgorithmException 初始化MD5算法是出错
     * @throws UnsupportedEncodingException 字符串转byte时出错
     */
    public static byte[] F(byte[] Ks, String w) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = md.digest(w.getBytes("utf-8"));
        return bytes;
    }


    /**
     * 一个随机函数 {0,1}^lambda X {0,1}^lambda -> Zp*
     *
     * @param pairing
     * @param a1
     * @param a2
     * @return
     * @throws Exception
     */
    public static Element Fp(final Pairing pairing, final byte[] a1, final byte[] a2) throws Exception {
        byte[] res = F(a1, new String(a2, "utf-8"));
        return pairing.getZr().newElementFromHash(res, 0, res.length);
    }

    /**
     * 获得用来索引关键词的key K_T
     *
     * @return 用来索引关键词的K_T
     */
    public static byte[] generateK_T() {
        // Ks是随机函数F的参数
        final byte[] K_T = CryptoPrimitives.randomBytes(128 / 8);
        return K_T;
    }
    /**
     * AES加密时候使用的初始向量
     */
    private static final byte[] ivBytes = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * 使用CBC模式下的AES加密算法加密
     *
     * @param Ke 密钥 16 byte
     * @param mingWen 要加密的明文
     * @return 加密之后得到的密文
     * @throws IOException 异常
     * @throws InvalidAlgorithmParameterException 异常
     * @throws NoSuchAlgorithmException 异常
     * @throws NoSuchPaddingException 异常
     * @throws NoSuchProviderException 异常
     * @throws InvalidKeyException 异常
     */
    public static byte[] encrypt_AES_CBC(byte[] Ke, String mingWen) throws IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException {
        final byte[] miWen = CryptoPrimitives.encryptAES_CBC(Ke, ivBytes, mingWen.getBytes("utf-8"));
        return miWen;
    }

    /**
     * 使用CBC模式下的AES加密算法解密
     * @param Ke 解密密钥
     * @param miWen 需要解密的密文
     * @return 解密之后得到的明文
     * @throws InvalidAlgorithmParameterException 异常
     * @throws NoSuchAlgorithmException 异常
     * @throws NoSuchPaddingException 异常
     * @throws NoSuchProviderException 异常
     * @throws InvalidKeyException 异常
     * @throws IOException 异常
     */
    public static String decrypt_AES_CBC(byte[] Ke,byte[] miWen) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, InvalidKeyException, IOException {
        byte[] s = CryptoPrimitives.decryptAES_CBC(miWen, Ke);
        return new String(s,"utf-8");
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
