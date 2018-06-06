package com.yd.test;

public class ReceiveDataFormat {

    public int recevieDataLength;
    public int receiveDateContent;

    public ReceiveDataFormat(int length, int data) {

        this.recevieDataLength = length;
        this.receiveDateContent = data;
    }

    @Override
    public String toString() {
        return "大小为" + recevieDataLength + "\n内容为" + receiveDateContent;
    }
}
