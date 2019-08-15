package com.zwb.yeildchart.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.zwb.yeildchart.DB.AppDatabase;

import java.util.Date;

/**
 * @author Administrator
 * @date 2019/8/12 13:42
 */
@Table(database = AppDatabase.class,name="Yeild")
public class Yeild extends BaseModel {
    @PrimaryKey(autoincrement = true)
    @Column
    private int id;
    @Column
    private float number;
    @Column
    private int teamID;
    @Column
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Yeild(float number, int teamID, String date) {
        this.number = number;
        this.teamID = teamID;
        this.date = date;
    }
    public Yeild(){

    }
    @Override
    public String toString() {
        return this.id+">>"+this.date+">>"+this.number+">>"+this.teamID;
    }
}
