package com.hello.spring.custom.query.bean;

import java.util.List;

/**
 * Created by jingmin.xiao on 2017/6/12.
 */
public class GridDataBean {

    private int totals;

    private List<?> items;

    public GridDataBean(){

    }

    public GridDataBean(int totals, List<?> items){
        this.items = items;
        this.totals = totals;
    }

    public int getTotals() {
        return totals;
    }

    public List<?> getItems() {
        return items;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

    public void setItems(List<?> items) {
        this.items = items;
    }

}
