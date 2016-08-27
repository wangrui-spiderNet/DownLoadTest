package com.zxs.test.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

import com.zxs.downloadtest.R;
import com.zxs.test.model.LocalDownInfo;
import com.zxs.test.ui.adapter.VideoDownLoadAdapter;

/**
 * 
 * TODO 文件下载工具类
 * <p/>
 * 创建时间: 2015年11月9日 下午4:59:53 <br/>
 * 
 * @author xszhang
 * @version
 * @since v0.0.1
 */
public class FileDownloader extends Thread {
	private String urlstr;// 远程URL下载的地址
	private int fileSize;// 所要下载的文件的大小
	private static final int INIT = 0;// 初始化状态表示未下载
	private static final int DOWNLOADING = 1;// 正在下载状态
	private static final int PAUSE = 2;// 暂停状态
	private int state = INIT;// 初始化状态
	private String localPath; // 本地文件地址
	private LocalDownInfo downLoadInfo;

	public FileDownloader(String urlstr) {
		this.urlstr = urlstr;
		// 保存sd卡路径信息
		this.localPath = AppTools.getVideoSavePath() + "/" + urlstr.substring(urlstr.lastIndexOf("/") + 1, urlstr.length());
	}

	/**
	 * 判断是否正在下载
	 * 
	 * @return
	 */
	public boolean isdownloading() {
		return state == DOWNLOADING;
	}

	/**
	 * 功能说明:根据url获取下载的信息
	 * <p>
	 * getDownloadInfo
	 * </p>
	 * 
	 * @return
	 */
	public LocalDownInfo getDownloadInfo() {
		this.downLoadInfo = DownLoadLocalManager.getInstance().getDownLoadInfo(urlstr);
		if (downLoadInfo.getEndPos() == 0) {
			this.init();
			downLoadInfo.setEndPos(fileSize);
			DownLoadLocalManager.getInstance().updateFileSize(urlstr, fileSize);// 更新文件大小
		}
		return downLoadInfo;
	}

	/**
	 * 初始化数据信息
	 */
	private void init() {
		try {
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(R.string.http_connection_out_time);
			connection.setRequestMethod("GET");
			fileSize = connection.getContentLength();
			File file = new File(localPath);
			// 本地访问文件
			RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
			accessFile.setLength(fileSize);
			accessFile.close();
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载数据
	 */
	public void download() {
		getDownloadInfo();
		if (this.downLoadInfo != null) {
			if (this.state == DOWNLOADING) { // 如果正在下载中
				return;
			}
			this.state = DOWNLOADING;
		}
	}

	@Override
	public void run() {
		download();
		HttpURLConnection connection = null;
		RandomAccessFile randomAccessFile = null;
		InputStream is = null;
		try {
			URL url = new URL(urlstr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(R.string.http_connection_out_time);
			connection.setRequestMethod("GET");
			// 设置范围，格式为Range：bytes x-y;
			connection.setRequestProperty("Range", "bytes=" + (downLoadInfo.getStartPos() + downLoadInfo.getProgressSize()) + "-" + downLoadInfo.getEndPos());
			randomAccessFile = new RandomAccessFile(localPath, "rwd");
			randomAccessFile.seek(downLoadInfo.getStartPos() + downLoadInfo.getProgressSize());
			// 将要下载的文件写到保存在保存路径下的文件中
			is = connection.getInputStream();
			byte[] buffer = new byte[4096];
			int length = -1;
			while ((length = is.read(buffer)) != -1) {
				randomAccessFile.write(buffer, 0, length);
				downLoadInfo.setProgressSize(downLoadInfo.getProgressSize() + length);
				// 更新数据库中的下载信息
				DownLoadLocalManager.getInstance().updateProgress(downLoadInfo.getProgressSize(), urlstr);
				if (state == PAUSE) {
					return;
				}
			}
			Log.i("test ", "while progressSize:" + downLoadInfo.getProgressSize());
			Log.i("test", "while endPos:" + downLoadInfo.getEndPos());
			// 下载完毕更新视频下载状态
			if (downLoadInfo.getProgressSize() == downLoadInfo.getEndPos()) {
				DownLoadLocalManager.getInstance().updateStatus(3, urlstr);
				VideoDownLoadAdapter.downloadersPool.remove(urlstr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				randomAccessFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 设置暂停
	 */
	public void pause() {
		this.state = PAUSE;// 0表示未下载 ,1表示下载中, 2表示暂停, 3表示已完成
		DownLoadLocalManager.getInstance().updateStatus(2, urlstr);
	}

	/**
	 * 设置继续下载
	 */
	public void restart() {
		this.state = DOWNLOADING;// 0表示未下载 ,1表示下载中, 2表示暂停, 3表示已完成
		DownLoadLocalManager.getInstance().updateStatus(1, urlstr);
	}

	/**
	 * 功能说明: 根据url删除对应的下载器的数据信息
	 * <p>
	 * delete
	 * </p>
	 * 
	 * @param urlstr
	 */
	public void delete(String urlstr) {
		this.state = PAUSE;//0表示未下载 ,1表示下载中, 2表示暂停, 3表示已完成
		DownLoadLocalManager.getInstance().deleteVideoInfo(urlstr);
	}
}
