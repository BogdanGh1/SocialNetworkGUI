package com.example.socialnetworkgui.domain;

import java.time.LocalDateTime;

public class Message extends Entity<Long>{
    private String text;
    private long fromUserId;
    private long toUserId;
    private LocalDateTime date;
    private long conversationId;
    public Message(Long aLong) {
        super(aLong);
    }
}
