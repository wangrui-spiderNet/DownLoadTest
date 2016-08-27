package com.zxs.test.utils;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zxs.downloadtest.R;
import com.zxs.test.model.LocalDownInfo;

/**
 * 
 * TODO sqlite数据常用工具类
 * <p/>
 * 创建时间: 2015年11月9日 下午4:50:40 <br/>
 * 
 * @author xszhang
 * @version
 * @since v0.0.1
 */
public class AppDBHelper extends OrmLiteSqliteOpenHelper {
	private Dao<LocalDownInfo, Object> localInfoDao = null;

	public AppDBHelper(Context context) {
		super(context, context.getString(R.string.db_name), null, context.getResources().getInteger(R.integer.db_version));
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource source) {
		try {
			TableUtils.createTable(source, LocalDownInfo.class);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {

	}

	/**
	 * 功能说明:本地下载记录
	 * <p>
	 * getDownLoadVInfoDao
	 * </p>
	 * 
	 * @return
	 */
	public Dao<LocalDownInfo, Object> getLocalInfoDao() {
		if (localInfoDao == null) {
			try {
				localInfoDao = getDao(LocalDownInfo.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return localInfoDao;
	}

	@Override
	public void close() {
		super.close();
		localInfoDao = null;
	}
}
