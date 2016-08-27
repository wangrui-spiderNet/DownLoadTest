package com.zxs.test.utils;

import java.util.List;

import com.zxs.test.model.LocalDownInfo;

/**
 * 
 * TODO 文件下载接口
 * <p/>
 * 创建时间: 2015年11月9日 下午4:48:15 <br/>
 * 
 * @author xszhang
 * @version
 * @since v0.0.1
 */
public interface LocalDownInfoDao {
	/**
	 * 功能说明:根据视频名称检索是否存在此视频
	 * <p>
	 * isHasInfors
	 * </p>
	 * 
	 * @param videoName
	 * @return
	 */
	public boolean isHasInfors(String videoUrl);

	/**
	 * 功能说明:查询已下载完成的视频记录
	 * <p>
	 * getDownLoadInfo
	 * </p>
	 * 
	 * @return
	 */
	public List<LocalDownInfo> getCompleteInfo();

	/**
	 * 功能说明:查询未完成下载的视频记录
	 * <p>
	 * getUnfinishedInfo
	 * </p>
	 * 
	 * @return
	 */
	public List<LocalDownInfo> getUnfinishedInfo();

	/**
	 * 
	 * TODO 查询本地所有的下载信息
	 * 
	 * @author xszhang
	 * @return
	 * @since v0.0.1
	 */
	public List<LocalDownInfo> getAllLocalResourceList();

	/**
	 * 功能说明:向下载记录表中插入下载记录
	 * <p>
	 * insertDownLoadVInfo
	 * </p>
	 * 
	 * @param downLoadVInfo
	 */
	public int insertDownLoadInfo(LocalDownInfo downLoadInfo);

	/**
	 * 功能说明:根据线程id，视频名称更新下载进度
	 * <p>
	 * updateCompeleteSize
	 * </p>
	 * 
	 * @param threadId
	 * @param compeleteSize
	 * @param videoName
	 * @return
	 */
	public boolean updateCompeleteSize(int compeleteSize, String video_url);

	/**
	 * 功能说明:更新文件下载状态
	 * <p>
	 * updateDownLoadStatus
	 * </p>
	 * 
	 * @param status
	 * @param video_url
	 * @return
	 */
	public boolean updateDownLoadStatus(int status, String video_url);

	/**
	 * 功能说明:更新文件大小
	 * <p>
	 * updateFileSize
	 * </p>
	 * 
	 * @param url
	 * @param fileSize
	 * @return
	 */
	public boolean updateFileSize(String url, int fileSize);

	/**
	 * 功能说明:根据视频id查询视频信息
	 * <p>
	 * getDownLoadInfo
	 * </p>
	 * 
	 * @param url
	 * @return
	 */
	public LocalDownInfo getDownLoadInfo(String url);

	/**
	 * 功能说明:删除数据
	 * <p>
	 * deleteVideInfoByUrl
	 * </p>
	 * 
	 * @param url
	 * @return
	 */
	public boolean deleteVideoInfoByUrl(String url);

	/**
	 * 功能说明:关闭连接
	 * <p>
	 * closeConnection
	 * </p>
	 */
	public void closeConnection();
}
