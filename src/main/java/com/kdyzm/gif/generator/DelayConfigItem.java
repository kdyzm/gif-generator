package com.kdyzm.gif.generator;

public class DelayConfigItem {
    private String file;
    private Integer delay;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "DelayConfigItem{" +
                "file='" + file + '\'' +
                ", delay='" + delay + '\'' +
                '}';
    }
}
