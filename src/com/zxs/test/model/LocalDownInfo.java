package com.zxs.test.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * TODO 创建一个下载信息的实体类
 * <p/>
 * 创建时间: 2015年11月9日 下午2:56:24 <br/>
 * 
 * @author xszhang
 * @version
 * @since v0.0.1
 */
@DatabaseTable(tableName = "local_downInfo")
public class LocalDownInfo implements Serializable {
	private static final long serialVersionUID = 2090092676706030440L;
	@DatabaseField(generatedId = true)
	private int id; // 主id
	@DatabaseField(columnName = "video_id")
	private String videoId;// 视频id
	@DatabaseField(columnName = "video_name")
	private String videoName; // 视频名称
	@DatabaseField(columnName = "video_url")
	private String videoUrl; // 视频URL
	@DatabaseField(columnName = "download_status")
	private int downLoadStatus; // 0表示未下载 ,1表示下载中, 2表示暂停, 3表示已完成
	@DatabaseField(columnName = "progress_size")
	private int progressSize;// 下载进度大小
	@DatabaseField(columnName = "start_pos")
	private int startPos;// 开始点
	@DatabaseField(columnName = "end_pos")
	private int endPos;// 结束点

	/**
	 * videoId.
	 * 
	 * @return the videoId
	 * @since v0.0.1
	 */
	public String getVideoId() {
		return videoId;
	}

	/**
	 * videoId.
	 * 
	 * @param videoId
	 *            the videoId to set
	 * @since v0.0.1
	 */
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	/**
	 * videoName.
	 * 
	 * @return the videoName
	 * @since v0.0.1
	 */
	public String getVideoName() {
		return videoName;
	}

	/**
	 * videoName.
	 * 
	 * @param videoName
	 *            the videoName to set
	 * @since v0.0.1
	 */
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	/**
	 * videoUrl.
	 * 
	 * @return the videoUrl
	 * @since v0.0.1
	 */
	public String getVideoUrl() {
		return videoUrl;
	}

	/**
	 * videoUrl.
	 * 
	 * @param videoUrl
	 *            the videoUrl to set
	 * @since v0.0.1
	 */
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	/**
	 * downLoadStatus.
	 * 
	 * @return the downLoadStatus
	 * @since v0.0.1
	 */
	public int getDownLoadStatus() {
		return downLoadStatus;
	}

	/**
	 * downLoadStatus.
	 * 
	 * @param downLoadStatus
	 *            the downLoadStatus to set
	 * @since v0.0.1
	 */
	public void setDownLoadStatus(int downLoadStatus) {
		this.downLoadStatus = downLoadStatus;
	}

	/**
	 * progressSize.
	 * 
	 * @return the progressSize
	 * @since v0.0.1
	 */
	public int getProgressSize() {
		return progressSize;
	}

	/**
	 * progressSize.
	 * 
	 * @param progressSize
	 *            the progressSize to set
	 * @since v0.0.1
	 */
	public void setProgressSize(int progressSize) {
		this.progressSize = progressSize;
	}

	/**
	 * startPos.
	 * 
	 * @return the startPos
	 * @since v0.0.1
	 */
	public int getStartPos() {
		return startPos;
	}

	/**
	 * startPos.
	 * 
	 * @param startPos
	 *            the startPos to set
	 * @since v0.0.1
	 */
	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}

	/**
	 * endPos.
	 * 
	 * @return the endPos
	 * @since v0.0.1
	 */
	public int getEndPos() {
		return endPos;
	}

	/**
	 * endPos.
	 * 
	 * @param endPos
	 *            the endPos to set
	 * @since v0.0.1
	 */
	public void setEndPos(int endPos) {
		this.endPos = endPos;
	}

}