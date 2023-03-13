package com.example.socialnetworkgui.domain;

import com.example.socialnetworkgui.utils.Utils;

import java.time.LocalDateTime;

public class Message extends Entity<Long>{
    private String text;
    private long senderId;
    private String senderName;
    private LocalDateTime date;

    public Message(Long id, String text, long senderId, String senderName, LocalDateTime date) {
        super(id);
        this.text = text;
        this.senderId = senderId;
        this.senderName = senderName;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public long getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getDate() {
        return date.format(Utils.DATE_TIME_FORMATTER);
    }
}
