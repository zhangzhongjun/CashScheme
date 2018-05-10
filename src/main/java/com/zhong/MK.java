package com.zhong;

import it.unisa.dia.gas.jpbc.Element;

import java.io.Serializable;
import java.util.Arrays;

/**
 * master key的类
 *
 * @author 张中俊
 **/

public class MK implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private byte[] Kx = null;
    private byte[] KI = null;
    private byte[] Kz = null;
    private byte[] Ks = null;
    private byte[] KT = null;
    private SerializableElement g ;

    public MK(byte[] kx, byte[] KI, byte[] kz, byte[] ks, byte[] KT, SerializableElement g) {
        this.Kx = kx;
        this.KI = KI;
        this.Kz = kz;
        this.Ks = ks;
        this.KT = KT;
        this.g = g;
    }

    public byte[] getKI() {
        return KI;
    }

    public byte[] getKT() {
        return KT;
    }

    public byte[] getKz() {
        return Kz;
    }

    public byte[] getKx() {
        return Kx;
    }

    public byte[] getKs() {
        return Ks;
    }

    public Element getG() {
        return g.getElement();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===您现在查看的是Master Key的信息==="+System.lineSeparator());
        sb.append("Kx: "+ Arrays.toString(Kx));
        sb.append(System.lineSeparator());
        sb.append("KI: "+ Arrays.toString(KI));
        sb.append(System.lineSeparator());
        sb.append("Kz: "+ Arrays.toString(Kz));
        sb.append(System.lineSeparator());
        sb.append("Ks: "+ Arrays.toString(Ks));
        sb.append(System.lineSeparator());
        sb.append("KT: "+ Arrays.toString(KT));
        sb.append(System.lineSeparator());
        sb.append("g: "+g.getElement().toString());
        sb.append(System.lineSeparator());
        sb.append("=======================================");
        return sb.toString();
    }
}