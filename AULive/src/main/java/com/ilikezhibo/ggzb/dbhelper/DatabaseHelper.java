package com.ilikezhibo.ggzb.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.ilikezhibo.ggzb.dbhelper.entity.DBUserInfo;
import java.sql.SQLException;

/**
 * Database helper which creates and upgrades the database and provides the DAOs
 * for the app.
 *
 * @author big 2014-07-08
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

   /************************************************
    * Suggested Copy/Paste code. Everything from here to the done block.
    ************************************************/

   private static final String DATABASE_NAME = "qixi_ilvb.db";
   private static final int DATABASE_VERSION = 7;
   /**
    * userDao ，每张表对于一个
    */
   private Dao<DBUserInfo, String> userInfoDao = null;

   public DatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }

   private static DatabaseHelper instance;

   /**
    * 单例获取该Helper
    */
   public static synchronized DatabaseHelper getHelper(Context context) {
      if (instance == null) {
         synchronized (DatabaseHelper.class) {
            if (instance == null) {
               instance = new DatabaseHelper(context);
            }
         }
      }

      return instance;
   }

   /************************************************
    * Suggested Copy/Paste Done
    ************************************************/

   @Override public void onCreate(SQLiteDatabase sqliteDatabase,
       ConnectionSource connectionSource) {

      try {
         TableUtils.createTable(connectionSource, DBUserInfo.class);
      } catch (SQLException e) {
         Log.e(DatabaseHelper.class.getName(), "数据库创建失败", e);
      }
   }

   @Override public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource,
       int oldVer, int newVer) {

      try {
         TableUtils.dropTable(connectionSource, DBUserInfo.class, true);
         onCreate(sqliteDatabase, connectionSource);
      } catch (SQLException e) {
         Log.e(DatabaseHelper.class.getName(), "数据库更新失败 " + oldVer + " to new " + newVer, e);
      }
   }

   public Dao<DBUserInfo, String> getUserInfoDao() throws SQLException {
      if (userInfoDao == null) {
         userInfoDao = getDao(DBUserInfo.class);
      }
      return userInfoDao;
   }

   public DBUserInfo getUserInfo(String ID) throws SQLException {
      return getUserInfoDao().queryForId(ID);
   }

   public int addUserInfo(DBUserInfo userInfo) throws SQLException {
      if (getUserInfo(userInfo.getId()) != null) {
         return getUserInfoDao().update(userInfo);
      } else {
         return getUserInfoDao().create(userInfo);
      }
   }
}