package com.zxs.test.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.zxs.test.model.LocalDownInfo;

/**
 * 
 * TODO 文件下载接口实现类
 * <p/>
 * 创建时间: 2015年11月9日 下午4:48:44 <br/>
 * 
 * @author xszhang
 * @version
 * @since v0.0.1
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class LocalDownInfoDaoImpl implements LocalDownInfoDao {
	private AppDBHelper dbHelper;
	private Dao dao;

	public LocalDownInfoDaoImpl() {
		this.dbHelper = new AppDBHelper(AppConfig.getContext());
		this.dao = dbHelper.getLocalInfoDao();
	}

	@Override
	public boolean isHasInfors(String videoUrl) {
		long count = 0;
		try {
			count = dao.countOf(dao.queryBuilder().setCountOf(true).where().eq("video_url", videoUrl).prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count == 0;
	}

	@Override
	public int insertDownLoadInfo(LocalDownInfo localDownInfo) {
		int count = 0;
		try {
			count = dao.create(localDownInfo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public List<LocalDownInfo> getCompleteInfo() {
		List<LocalDownInfo> list = new ArrayList<LocalDownInfo>();
		try {
			list = dao.query(dao.queryBuilder().where().eq("download_status", 2).prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<LocalDownInfo> getUnfinishedInfo() {
		List<LocalDownInfo> list = new ArrayList<LocalDownInfo>();
		try {
			list = dao.query(dao.queryBuilder().where().eq("download_status", 0).or().eq("download_status", 1).prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public LocalDownInfo getDownLoadInfo(String url) {
		List<LocalDownInfo> list = new ArrayList<LocalDownInfo>();
		try {
			list = dao.query(dao.queryBuilder().where().eq("video_url", url).prepare());
			if (list.size() > 0) {
				return list.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateCompeleteSize(int compeleteSize, String video_url) {
		try {
			UpdateBuilder<LocalDownInfo, Integer> updateBuilder = dao.updateBuilder();
			// set the criteria like you would a QueryBuilder
			updateBuilder.where().eq("video_url", video_url);
			// update the value of your field(s)
			Log.i("test", "compeleteSize:" + compeleteSize);
			updateBuilder.updateColumnValue("progress_size", compeleteSize);
			int lint = updateBuilder.update();
			if (lint != -1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateFileSize(String url, int fileSize) {
		try {
			UpdateBuilder<LocalDownInfo, Integer> updateBuilder = dao.updateBuilder();
			// set the criteria like you would a QueryBuilder
			updateBuilder.where().eq("video_url", url);
			// update the value of your field(s)
			updateBuilder.updateColumnValue("end_pos", fileSize);
			int lint = updateBuilder.update();
			if (lint != -1) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateDownLoadStatus(int status, String video_url) {
		try {
			UpdateBuilder<LocalDownInfo, Integer> updateBuilder = dao.updateBuilder();
			// set the criteria like you would a QueryBuilder
			updateBuilder.where().eq("video_url", video_url);
			// update the value of your field(s)
			updateBuilder.updateColumnValue("download_status", status);
			int lint = updateBuilder.update();
			if (lint != -1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteVideoInfoByUrl(String url) {
		try {
			DeleteBuilder<LocalDownInfo, Integer> deleteBuilder = dao.deleteBuilder();
			deleteBuilder.where().eq("video_url", url);
			int lint = deleteBuilder.delete();
			if (lint != -1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void closeConnection() {
		dbHelper.close();
	}

	@Override
	public List<LocalDownInfo> getAllLocalResourceList() {
		List<LocalDownInfo> list = new ArrayList<LocalDownInfo>();
		try {
			list = dao.queryForAll();
			if (list.size() > 0) {
				return list;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
