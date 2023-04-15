package com.example.tryagain;

import java.util.List;

public class History_Child {


    private String history_child_date, history_child_total_quantity, history_child_total_price;
    private List<History_Item> historyItemList;

    public History_Child() {
    }

    public History_Child(String history_child_date, String history_child_total_quantity, String history_child_total_price, List<History_Item> historyItemList) {
        this.history_child_date = history_child_date;
        this.history_child_total_quantity = history_child_total_quantity;
        this.history_child_total_price = history_child_total_price;
        this.historyItemList = historyItemList;
    }

    public String getHistory_child_date() {
        return history_child_date;
    }

    public void setHistory_child_date(String history_child_date) {
        this.history_child_date = history_child_date;
    }

    public String getHistory_child_total_quantity() {
        return history_child_total_quantity;
    }

    public void setHistory_child_total_quantity(String history_child_total_quantity) {
        this.history_child_total_quantity = history_child_total_quantity;
    }

    public String getHistory_child_total_price() {
        return history_child_total_price;
    }

    public void setHistory_child_total_price(String history_child_total_price) {
        this.history_child_total_price = history_child_total_price;
    }

    public List<History_Item> getHistoryItemList() {
        return historyItemList;
    }

    public void setHistoryItemList(List<History_Item> historyItemList) {
        this.historyItemList = historyItemList;
    }
}
