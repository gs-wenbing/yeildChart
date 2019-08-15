package com.zwb.yeildchart.DB;


import com.raizlabs.android.dbflow.annotation.Database;

/**
 * @author Administrator
 * @date 2019/8/9 9:36
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    //数据库名称
    public static final String NAME = "MyDatabase";
    //数据库版本号
    public static final int VERSION = 1;
}
