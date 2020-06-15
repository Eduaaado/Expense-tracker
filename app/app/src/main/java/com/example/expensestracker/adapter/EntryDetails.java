package com.example.expensestracker.adapter;

public class EntryDetails {
    public String name;
    public String amount;
    public String type;
    public String time;

    public EntryDetails(String name, String amount, String type, String time) {
        this.name = name;
        this.amount = amount;
        this.type = type;
        this.time = time;
    }
}
