package com.google.mgmg22demo.history;

/**
 *
 * @Author shenxiaoshun
 * @Date 2021/2/24
 */
public class Singleton {
    private Singleton(){

    }

    public static Singleton getInstance(){
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder{
        private static final Singleton sInstance = new Singleton();
    }

}