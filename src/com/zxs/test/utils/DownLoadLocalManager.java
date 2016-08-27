package com.zxs.test.utils;

import java.util.List;

import android.util.Log;

import com.zxs.test.model.LocalDownInfo;

/**
 * 
 * TODO 下载管理器
 * <p/>
 * 创建时间: 2015年11月9日 下午4:47:11 <br/>
 * 
 * @author xszhang
 * @version
 * @since v0.0.1
 */
public class DownLoadLocalManager {
	private static DownLoadLocalManager instance = null;
	private static LocalDownInfoDao downLoadInfoDao;

	public synchronized static DownLoadLocalManager getInstance() {
		if (instance == null) {
			instance = new DownLoadLocalManager();
			downLoadInfoDao = new LocalDownInfoDaoImpl();
		}
		return instance;
	}

	/**
	 * 检查数据库中是存在数据
	 * 
	 * @param urlstr
	 * @return
	 */
	public boolean isHasInfors(String videoURl) {
		return downLoadInfoDao.isHasInfors(videoURl);
	}

	/**
	 * 保存 下载的具体信息
	 * 
	 * @param infos
	 */
	public void saveInfos(LocalDownInfo info) {
		try {
			int count = downLoadInfoDao.insertDownLoadInfo(info);
			Log.i("test", "插入成功了么!" + count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 功能说明:更新文件总大小
	 * <p>
	 * updateFileSize
	 * </p>
	 * 
	 * @param url
	 * @param fileSize
	 * @return
	 */
	public boolean updateFileSize(String url, int fileSize) {
		return downLoadInfoDao.updateFileSize(url, fileSize);
	}

	/**
	 * 功能说明:获取已完成的数据信息
	 * <p>
	 * getCompleteInfo
	 * </p>
	 * 
	 * @return
	 */
	public List<LocalDownInfo> getCompleteInfo() {
		return downLoadInfoDao.getCompleteInfo();
	}

	/**
	 * 功能说明:查询未完成的下载记录
	 * <p>
	 * getUnfinishedInfo
	 * </p>
	 * 
	 * @return
	 */
	public List<LocalDownInfo> getUnfinishedInfo() {
		return downLoadInfoDao.getUnfinishedInfo();
	}

	/**
	 * 
	 * TODO 获取本地所有的视频相关信息
	 * 
	 * @author xszhang
	 * @return
	 * @since v0.0.1
	 */
	public List<LocalDownInfo> getAllResourceList() {
		return downLoadInfoDao.getAllLocalResourceList();
	}

	/**
	 * 功能说明:根据url查询下载的信息
	 * <p>
	 * getDownLoadInfo
	 * </p>
	 * 
	 * @param url
	 * @return
	 */
	public LocalDownInfo getDownLoadInfo(String url) {
		return downLoadInfoDao.getDownLoadInfo(url);
	}

	/**
	 * 更新数据库中的下载信息
	 * 
	 * @param threadId
	 * @param compeleteSize
	 * @param urlstr
	 */
	public boolean updateProgress(int compeleteSize, String videoUrl) {
		return downLoadInfoDao.updateCompeleteSize(compeleteSize, videoUrl);
	}

	/**
	 * 功能说明:更新数据库中的下载记录
	 * <p>
	 * updateStatus
	 * </p>
	 * 
	 * @param status
	 * @param videoUrl
	 * @return
	 */
	public boolean updateStatus(int status, String videoUrl) {
		return downLoadInfoDao.updateDownLoadStatus(status, videoUrl);
	}

	/**
	 * 功能说明:删除下载数据
	 * <p>
	 * deleteVideoInfo
	 * </p>
	 * 
	 * @param videoUrl
	 * @return
	 */
	public boolean deleteVideoInfo(String videoUrl) {
		return downLoadInfoDao.deleteVideoInfoByUrl(videoUrl);
	}
}
