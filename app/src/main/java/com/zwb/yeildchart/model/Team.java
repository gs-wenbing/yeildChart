package com.zwb.yeildchart.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.zwb.yeildchart.DB.AppDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2019/8/9 9:38
 */
@Table(database = AppDatabase.class,name="Team")
public class Team extends BaseModel implements Parcelable {

    public Team() {
    }

    @PrimaryKey()
    @Column
    private int teamID;

    @Column
    private int taamPID;
    @Column
    private String teamName;
    @Column
    private String xlsPath;
    @Column
    private int sheetIndex;
    @Column
    private int row;
    @Column
    private int column;
    @Column
    private boolean isLast;

    private boolean isMergedCell;
    private int topLeftRow;
    private int bottomRightRow;
    private int topLeftColumn;
    private int bottomRightColumn;

    private ArrayList<Team> childList;
    private ArrayList<Yeild> yeildList;


    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public int getTaamPID() {
        return taamPID;
    }

    public void setTaamPID(int taamPID) {
        this.taamPID = taamPID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isMergedCell() {
        return isMergedCell;
    }

    public void setMergedCell(boolean mergedCell) {
        isMergedCell = mergedCell;
    }

    public int getTopLeftRow() {
        return topLeftRow;
    }

    public void setTopLeftRow(int topLeftRow) {
        this.topLeftRow = topLeftRow;
    }

    public int getBottomRightRow() {
        return bottomRightRow;
    }

    public void setBottomRightRow(int bottomRightRow) {
        this.bottomRightRow = bottomRightRow;
    }

    public int getTopLeftColumn() {
        return topLeftColumn;
    }

    public void setTopLeftColumn(int topLeftColumn) {
        this.topLeftColumn = topLeftColumn;
    }

    public int getBottomRightColumn() {
        return bottomRightColumn;
    }

    public void setBottomRightColumn(int bottomRightColumn) {
        this.bottomRightColumn = bottomRightColumn;
    }

    public String getXlsPath() {
        return xlsPath;
    }

    public void setXlsPath(String xlsPath) {
        this.xlsPath = xlsPath;
    }

    public ArrayList<Team> getChildList() {
        return childList;
    }

    public void setChildList(ArrayList<Team> childList) {
        this.childList = childList;
    }

    public ArrayList<Yeild> getYeildList() {
        return yeildList;
    }

    public void setYeildList(ArrayList<Yeild> yeildList) {
        this.yeildList = yeildList;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public Team(int teamID, int taamPID, String teamName) {
        this.teamID = teamID;
        this.taamPID = taamPID;
        this.teamName = teamName;
    }


    @Override
    public String toString() {
        return "teamID="+this.teamID+">>teamName="+this.teamName+">>taamPID="+this.taamPID
                +">>sheetIndex="+this.sheetIndex+">>row="+this.row+">>column="+this.column+">>xlsPath="+this.xlsPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.teamID);
        dest.writeInt(this.taamPID);
        dest.writeString(this.teamName);
        dest.writeString(this.xlsPath);
        dest.writeInt(this.sheetIndex);
        dest.writeInt(this.row);
        dest.writeInt(this.column);
        dest.writeByte(this.isMergedCell ? (byte) 1 : (byte) 0);
        dest.writeInt(this.topLeftRow);
        dest.writeInt(this.bottomRightRow);
        dest.writeInt(this.topLeftColumn);
        dest.writeInt(this.bottomRightColumn);
        dest.writeByte(this.isLast ? (byte) 1 : (byte) 0);
        dest.writeList(this.childList);
        dest.writeList(this.yeildList);
    }

    protected Team(Parcel in) {
        this.teamID = in.readInt();
        this.taamPID = in.readInt();
        this.teamName = in.readString();
        this.xlsPath = in.readString();
        this.sheetIndex = in.readInt();
        this.row = in.readInt();
        this.column = in.readInt();
        this.isMergedCell = in.readByte() != 0;
        this.topLeftRow = in.readInt();
        this.bottomRightRow = in.readInt();
        this.topLeftColumn = in.readInt();
        this.bottomRightColumn = in.readInt();
        this.isLast = in.readByte() != 0;
        this.childList = new ArrayList<Team>();
        in.readList(this.childList, Team.class.getClassLoader());
        this.yeildList = new ArrayList<Yeild>();
        in.readList(this.yeildList, Yeild.class.getClassLoader());
    }

    public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel source) {
            return new Team(source);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };
}

