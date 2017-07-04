package com.hello.spring.util;

import java.util.Iterator;

/**
 *
 */
public class ForEachUtil {

    public static <T> void forEach(Iterable<T> iterable,EachFunction<T> function){
        Iterator<T> iterator = iterable.iterator();
        boolean isFirst = true;
        int i=0;
        while (iterator.hasNext()){
            T item = iterator.next();
            function.apply(item,i,isFirst,!iterator.hasNext());
            i++;
            isFirst = false;
        }
    }

    public static <T> void forEach(T[] array,EachFunction<T> function){
        boolean isFirst = true;
        boolean isEnd = false;
        int length = array.length;
        for(int i=0;i<length;i++){
            T item = array[i];
            if(i == length-1){
                isEnd = true;
            }
            function.apply(item,i,isFirst,isEnd);
            isFirst = false;
        }
    }

    public interface EachFunction<T>{
        public void apply(T item, int index, boolean isFist, boolean isEnd);
    }
}
