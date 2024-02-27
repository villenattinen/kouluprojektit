package com.server;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class WarningMessage {
    
    private String nickname;
    private double latitude;
    private double longitude;
    private String dangertype;
    private ZonedDateTime sent;
    private String areacode;
    private String phonenumber;

    public WarningMessage() {
    }

    public WarningMessage(String nickname, double latitude, double longitude, String dangertype, ZonedDateTime sent) {
        this.nickname = nickname;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dangertype = dangertype;
        this.sent = sent;
    }

    public WarningMessage(String nickname, double latitude, double longitude, String dangertype, ZonedDateTime sent, String areacode, String phonenumber) {
        this.nickname = nickname;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dangertype = dangertype;
        this.sent = sent;
        this.areacode = areacode;
        this.phonenumber = phonenumber;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDangertype() {
        return this.dangertype;
    }

    public void setDangertype(String dangertype) {
        this.dangertype = dangertype;
    }

    public long dateAsInt() {
        return sent.toInstant().toEpochMilli();
    }

    public ZonedDateTime getSent() {
        return this.sent;
    }

    public void setSent(long epoch) {
        sent = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneOffset.UTC);
    }

    public String getAreacode() {
        return this.areacode;
    }

    public void setAreacode(String areacode) {
        this.areacode = areacode;
    }

    public String getPhonenumber() {
        return this.phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

}
