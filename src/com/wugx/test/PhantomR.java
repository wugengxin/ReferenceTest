package com.wugx.test;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

public class PhantomR extends PhantomReference {
    private int key;

    public PhantomR(int key, byte[] value, ReferenceQueue<byte[]> referenceQueue){
        super(value,referenceQueue);
        this.key = key;
    }

    public int getKey(){
        return this.key;
    }
}
