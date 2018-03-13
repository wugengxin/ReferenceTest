package com.wugx.test;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class SoftReferenceTest {

    static void hasRecycle() {
        boolean recycleFlag = false;
        String str = new String("Hello World!");
        ReferenceQueue<String> rq = new ReferenceQueue<>();

        WeakReference<String> wf = new WeakReference<String>(str, rq);


        while (!recycleFlag) {
            String strGet = wf.get();
            if (null == strGet) {
                System.out.println("object has recycly ...............");
                recycleFlag = true;

                Reference r = rq.poll();
                if(null != r){
                    String rqStr = (String) r.get();
                    System.out.println("------------------1_recycly queue:" + rqStr + "----------------");
                }else{
                    System.out.println("------------------2_recycly queue is empty----------------");
                }

            }else{
                System.out.println("++++++++++++++++object not recycle:"+strGet+"++++++++++++++++++");

                Reference r = rq.poll();
                if(null != r){
                    String rqStr = (String) r.get();
                    System.out.println("------------------1_recycly queue:" + rqStr + "----------------");
                }else{
                    System.out.println("------------------1_recycly queue is empty----------------");
                }
            }
        }
    }

    static void QueueMoitor(){
        Object value = new Object();
        Map<Object,Object> map = new HashMap<>();
        ReferenceQueue<byte[]> referenceQueue = new ReferenceQueue<>();
        for(int i=0;i < 10000;i++){
            byte[] bytes = new byte[1024*1024];
            WeakReference<byte[]> wf= new WeakReference<byte[]>(bytes,referenceQueue);
            System.out.println("new object add map:"+wf);
            map.put(wf,value);
        }

        System.out.println("map size:"+map.size());

        Thread thread = new Thread(()->{
            int cnt = 0;

            WeakReference<byte[]> k;

            try {
                    while((k = (WeakReference) referenceQueue.remove()) != null){
                    System.out.println((++cnt)+"次"+"回收了:"+k);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        System.gc();
    }


    static void MapValueRecycle(){
        Map<Integer,WeakR> map = new HashMap<>();
        ReferenceQueue<byte[]> referenceQueue = new ReferenceQueue<>();

        for(int a=0;a < 100;a++){
            byte[] value = new byte[1024*1024];
            map.put(a,new WeakR(a,value,referenceQueue));
            System.out.println("HashMap Put Key:"+a+",Value:"+value);
        }

        System.out.println("map size:"+map.size());

        WeakR weakref;
        int count = 0;
//        try {
            while(null != (weakref = (WeakR) referenceQueue.poll())){
                System.out.print("释放了:"+(++count)+"次"+weakref);
                System.out.println(",Map recycle Key:"+weakref.getReferenceKey());
                map.remove(weakref.getReferenceKey());
            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("Map size:"+map.size());
    }

    static void PhantomReferenceTest(){
        Map<Integer,PhantomR> map = new HashMap<>();
        ReferenceQueue<byte[]> referenceQueue = new ReferenceQueue<>();

        for(int a=0;a < 100;a++){
            byte[] value = new byte[1024*1024];
            map.put(a,new PhantomR(a,value,referenceQueue));
        }

        PhantomR pR ;
        int cunt =0;
        while(null != (pR = (PhantomR)referenceQueue.poll())){
            System.out.println("回收了:"+(++cunt)+"次"+",Value:"+pR.getKey());
            map.remove(pR.getKey());
        }
    }

    public static void main(String[] args){
//        SoftReferenceTest.hasRecycle();
//        SoftReferenceTest.QueueMoitor();
//        SoftReferenceTest.MapValueRecycle();
        SoftReferenceTest.PhantomReferenceTest();
    }
}
