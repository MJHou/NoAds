package cn.edu.zzti.soft.noads.sql;

import com.raizlabs.android.dbflow.annotation.Database;
import static cn.edu.zzti.soft.noads.sql.AdDatabase.DATABASE_NAME;
import static cn.edu.zzti.soft.noads.sql.AdDatabase.DATABASE_VERSION;

/**
 * 广告木马数据库
 */
@Database(name = DATABASE_NAME,version = DATABASE_VERSION)
public final class AdDatabase {
    public static final String DATABASE_NAME ="ad_database";
    public static final int DATABASE_VERSION =1;
    public static final String T_AD_DATA ="t_ad_data";
    public static final String T_TROJAN_DATA="t_trojan_data";
}
