package cn.edu.zzti.soft.noads.sql;

import com.raizlabs.android.dbflow.annotation.Database;

import static cn.edu.zzti.soft.noads.sql.UserRecordsDatabase.DATABASE_NAME;
import static cn.edu.zzti.soft.noads.sql.UserRecordsDatabase.DATABASE_VERSION;


/**
 * 用户操作纪录数据库
 */
@Database(name = DATABASE_NAME,version = DATABASE_VERSION)
public final class UserRecordsDatabase {
    public static final String DATABASE_NAME ="user_records_database";
    public static final int DATABASE_VERSION =1;
    public static final String T_USER_SEARCH_RECORDS ="t_user_search_records";
    public static final String T_USER_HISTORICAL_RECORDS="t_user_historical_records";
    public static final String T_USER_OFFLINE_FILE="t_user_offline_file";
}
