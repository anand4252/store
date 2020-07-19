package com.techopact.store.entities;

import java.util.ArrayList;

public class FixedCircularArrayList<T> extends ArrayList<T> {
    private final int maxSize;
    public FixedCircularArrayList(int maxSize) {
        super(maxSize);
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(T t) {
        if(super.size() >= maxSize){
            super.remove(0);
        }
        return super.add(t);
    }
}
