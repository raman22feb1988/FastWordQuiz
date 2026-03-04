package com.tuchwords.wordquiz;

public class Filter {
    int length;
    String query;
    String sort;
    int blank;
    String name;
    long serial;

    public Filter(int length, String query, String sort, int blank, String name, long serial) {
        this.length = length;
        this.query = query;
        this.sort = sort;
        this.blank = blank;
        this.name = name;
        this.serial = serial;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public String getQuery() {
        return query;
    }

    public String getSort() {
        return sort;
    }

    public boolean getBlank() {
        return (blank != 0);
    }

    public long getSerial() {
        return serial;
    }
}