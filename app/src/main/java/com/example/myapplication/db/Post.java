package com.example.myapplication.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "stations_db")
public class Post {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "station_title")
    private String title;
    @ColumnInfo(name = "fuel_type")
    private String fuel_type;
    @ColumnInfo(name = "value")
    private String value;
    @ColumnInfo(name = "cost")
    private String cost;

    @Ignore
    public Post(String title, String fuel_type, String value, String cost) {
        this.title = title;
        this.fuel_type = fuel_type;
        this.value = value;
        this.cost = cost;
    }



    public String getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Post() {
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return fuel_type;
    }

    public void setContent(String content) {
        this.fuel_type = content;
    }
}
