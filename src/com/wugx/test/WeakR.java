package com.wugx.test;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

class WeakR extends WeakReference<byte[]> {
    private int key;

    WeakR(int key,byte[] value,ReferenceQueue<? super byte[]> referenceQueue){
        super(value,referenceQueue);
        this.key = key;
    }

    public int getReferenceKey(){
        return this.key;
    }
}