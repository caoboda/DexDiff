package com.fcxin.voice;


public class EventBusEntity {
    private String option;
    private int value;
    private String data;
    public EventBusEntity(String option,int value){
        setOption(option);
        setValue(value);
    }

    public EventBusEntity(String option,String data){
        setOption(option);
        setData(data);
    }
    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
