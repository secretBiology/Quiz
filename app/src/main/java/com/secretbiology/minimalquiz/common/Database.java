package com.secretbiology.minimalquiz.common;

/**
 * Created by Dexter on 12/3/2017.
 */

public enum Database {

    OPENDB(1, "OpenDB");

    private int type;
    private String database;

    Database(int type, String database) {
        this.type = type;
        this.database = database;
    }

    public int getType() {
        return type;
    }


    @Override
    public String toString() {
        return database;
    }

    public static Database getDatabase(int type) {
        for (Database d : Database.values()) {
            if (d.getType() == type) {
                return d;
            }
        }
        return OPENDB;
    }
}
