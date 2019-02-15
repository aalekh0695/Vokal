package com.example.vokal.pojo;

public class Card {
    private String time_slot, contact, msg_body;

    public Card(String time_slot, String contact, String msg_body) {
        this.time_slot = time_slot;
        this.contact = contact;
        this.msg_body = msg_body;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public String getContact() {
        return contact;
    }

    public String getMsg_body() {
        return msg_body;
    }
}
