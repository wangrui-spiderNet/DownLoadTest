package com.zxs.test.ui.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zxs.downloadtest.R;
import com.zxs.test.model.LocalDownInfo;
import com.zxs.test.utils.AppTools;
import com.zxs.test.utils.DownLoadLocalManager;
import com.zxs.test.utils.FileDownloader;
import com.zxs.test.utils.FileUtls;

/**
 * 
 * TODO 下载适配器
 * <p/>
 * 创建时间: 2015年11月9日 下午2:49:50 <br/>
 * 
 * @author xszhang
 * @version
 * @since v0.0.1
 */
public class VideoDownLoadAdapter2 extends BaseAdapter {
	private LayoutInflater mInflater;
	private Handler handler = null;
	private List<LocalDownInfo> list;
	private Activity activity;
	// 存放各个下载器
	public static Map<String, FileDownloader> downloadersPool = new HashMap<String, FileDownloader>();

	private List<DownloadStatusUpdateTask> taskList = new ArrayList<DownloadStatusUpdateTask>();

	public VideoDownLoadAdapter2(Activity activity, Handler handler, List<LocalDownInfo> list) {
		this.mInflater = LayoutInflater.from(activity);
		this.list = list;
		this.activity = activity;
		this.handler = handler;
		notifyData();
	}

	public void update(List<LocalDownInfo> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (null != list && list.size() > 0) {
			return list.size();
		}
		return list == null ? 0 : list.size();
	}

	@Override
	public LocalDownInfo getItem(int position) {
		if (null != list && list.size() > 0) {
			return list.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 
	 * TODO 任务列表
	 * 
	 * @author xszhang
	 * @return
	 * @since v0.0.1
	 */
	public List<DownloadStatusUpdateTask> getTaskList() {
		return taskList;
	}

	private void notifyData() {
		// 重新检索本地库的数据
		List<LocalDownInfo> localList = DownLoadLocalManager.getInstance().getAllResourceList();// 本地所有的下载信息
		for (int i = 0; i < list.size(); i++) {// 编历数据源，为了与本地数据同步
												// 。这个是不明智的作为。因为。每刷新 一次都 会操作文件
												// 。效率性能很差

			if (null != localList && localList.size() > 0) {
				for (int j = 0; j < localList.size(); j++) {
					if (list.get(i).getVideoUrl().equals(localList.get(j).getVideoUrl())) {// 网络url和本地url相同
						list.get(i).setDownLoadStatus(localList.get(j).getDownLoadStatus());
						list.get(i).setEndPos(localList.get(j).getEndPos());
						list.get(i).setProgressSize(localList.get(j).getProgressSize());
						list.get(i).setStartPos(localList.get(j).getStartPos());
						list.get(i).setVideoId(localList.get(j).getVideoId());
						list.get(i).setVideoName(localList.get(j).getVideoName());
						list.get(i).setVideoUrl(localList.get(j).getVideoUrl());
					}
				}
			}
		}

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {

		final ViewHolder holder;
		if (null == convertView) {
			convertView = this.mInflater.inflate(R.layout.download_list_item_layout, null);
			holder = new ViewHolder();
			holder.videoName = (TextView) convertView.findViewById(R.id.video_name);
			holder.downloadProgress = (ProgressBar) convertView.findViewById(R.id.download_progress);
			holder.downloadType = (TextView) convertView.findViewById(R.id.download_type);
			holder.videoDownload = (LinearLayout) convertView.findViewById(R.id.video_download);
			holder.typeIcon = (ImageView) convertView.findViewById(R.id.type_icon);
			holder.imageVideoThum = (ImageView) convertView.findViewById(R.id.image_video_thum);
			holder.type = (TextView) convertView.findViewById(R.id.type);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final LocalDownInfo downLoadInfo = getItem(position);

		holder.downloadProgress.setVisibility(View.VISIBLE);
		holder.videoName.setText(downLoadInfo.getVideoName());
		float num = (float) downLoadInfo.getProgressSize() / (float) downLoadInfo.getEndPos();// 下载数据量的百分比
		if (Float.compare(num, Float.NaN) == 0) {
			num = 0;
		}
		BigDecimal result = new BigDecimal((double) (num * 100));
		// 设置进度条按读取的length长度更新
		holder.downloadProgress.setProgress((int) (num * 100));
		Log.e("TT", "num---" + (int) (num * 100));
		holder.downloadType.setText(activity.getResources().getString(R.string.download_type, result.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() + "%"));// 用TextView显示下载的百分比
		switch (downLoadInfo.getDownLoadStatus()) { // 0表示未下载 ,1表示下载中, 2表示暂停,
													// 3表示已完成
		case 0:
			holder.typeIcon.setImageResource(R.drawable.button_install);
			holder.type.setText("下载");
			break;
		case 1:
			holder.typeIcon.setImageResource(R.drawable.button_stop);
			holder.type.setText("暂停");
			break;
		case 2:
			holder.typeIcon.setImageResource(R.drawable.button_play);
			holder.type.setText("继续");
			break;
		case 3:
			holder.typeIcon.setImageResource(R.drawable.button_complete);
			holder.type.setText("打开");
			break;
		default:
			break;
		}

		holder.videoDownload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断SDCard是否存在
				if (AppTools.isSDCardAvailable()) {
					try {
						// 下载操作
						String type = holder.type.getText().toString().trim();
						FileDownloader downloader = downloadersPool.get(downLoadInfo.getVideoUrl());
						if (downloader == null) {
							downloader = new FileDownloader(downLoadInfo.getVideoUrl());
							downloadersPool.put(downLoadInfo.getVideoUrl(), downloader);
						}
						if ("下载".equals(type)) {// 下载
							holder.typeIcon.setImageResource(R.drawable.button_stop);
							holder.type.setText("暂停");
							startDownloadTask(downLoadInfo, holder, downloader, position);
						} else if ("暂停".equals(type)) { // 暂停
							holder.typeIcon.setImageResource(R.drawable.button_play);
							holder.type.setText("继续");
							pauseDownloadTask(downloader);
						} else if ("继续".equals(type)) {
							holder.typeIcon.setImageResource(R.drawable.button_stop);
							holder.type.setText("暂停");
							restartDownloadTask(downLoadInfo, holder, downloader, position);
						} else if ("打开".equals(type)) {
							String urlstr = downLoadInfo.getVideoUrl();
							String param = AppTools.getVideoSavePath() + "/" + urlstr.substring(urlstr.lastIndexOf("/") + 1, urlstr.length());
							FileUtls.openFile(activity, param);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					Toast.makeText(activity, "SD卡存在错误!", Toast.LENGTH_SHORT).show();
				}

			}
		});

		return convertView;
	}

	private class ViewHolder {
		TextView videoName;
		ProgressBar downloadProgress;
		TextView downloadType;
		LinearLayout videoDownload;
		ImageView typeIcon;
		ImageView imageVideoThum;
		TextView type;

		DownloadStatusUpdateTask task = null;
	}

	/**
	 * 
	 * TODO 异步任务更新ui
	 * <p/>
	 * 创建时间: 2015年11月13日 下午3:08:08 <br/>
	 * 
	 * @author xszhang
	 * @version VideoDownLoadAdapter
	 * @since v0.0.1
	 */
	public class DownloadStatusUpdateTask extends AsyncTask<ViewHolder, Integer, Integer> {
		FileDownloader downloader = null;
		View view = null;
		String downUrl = null;
		boolean stop = false;
		ViewHolder holder = null;
		public LocalDownInfo info = null;
		int position;

		public void bindData(LocalDownInfo info, ViewHolder holder, int position) {
			this.holder = holder;
			this.info = info;
			this.position = position;
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Integer doInBackground(ViewHolder... params) {
			Integer result = 0;
			while (!stop && !isCancelled()) {
				try {
					info = DownLoadLocalManager.getInstance().getDownLoadInfo(info.getVideoUrl());
					if (info != null && info.getDownLoadStatus() != 3) {// 0表示未下载
																		// ,1表示下载中,
																		// 2表示暂停,
																		// 3表示已完成
						float num = (float) info.getProgressSize() / (float) info.getEndPos();// 得到当前文件的下载进度
						int currentProgress = (int) (num * 10000);
						int length = (int) (num * 100);
						// 调用publishProgress公布进度,最后onProgressUpdate方法将被执行
						publishProgress(length, currentProgress);
						result = 1;
						Thread.sleep(1000);
					} else if (info != null && info.getDownLoadStatus() == 3) {// 表示下载已完成
						result = 3;
						// 调用publishProgress公布进度,最后onProgressUpdate方法将被执行
						publishProgress(100, 10000);
						Thread.sleep(1000);
						break;
					} else {
						result = 2;
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... params) {
			upData();
		}

		@Override
		protected void onPostExecute(Integer status) {
			upData();
			Toast.makeText(activity, info.getVideoName() + "已下载完成！", Toast.LENGTH_SHORT).show();
			handler.sendEmptyMessage(0);
		}

		/**
		 * 
		 * TODO 跟新数据
		 * 
		 * @author xszhang
		 * @since v0.0.1
		 */
		private void upData() {
			list.get(position).setDownLoadStatus(info.getDownLoadStatus());
			list.get(position).setEndPos(info.getEndPos());
			list.get(position).setProgressSize(info.getProgressSize());
			list.get(position).setStartPos(info.getStartPos());
			list.get(position).setVideoId(info.getVideoId());
			list.get(position).setVideoName(info.getVideoName());
			list.get(position).setVideoUrl(info.getVideoUrl());
			notifyDataSetChanged();
		}

		@Override
		protected void onCancelled() {
			holder.type.setText("已取消");
			if (info != null && info.getDownLoadStatus() != 3) {//
				DownLoadLocalManager.getInstance().updateStatus(2, info.getVideoUrl());
			}
			handler.sendEmptyMessage(0);
		}
	};

	/**
	 * 
	 * TODO 开始下载任务
	 * 
	 * @author xszhang
	 * @param downLoadInfo
	 * @param holder
	 * @param downloader
	 * @since v0.0.1
	 */
	public void startDownloadTask(LocalDownInfo downLoadInfo, ViewHolder holder, FileDownloader downloader, int position) {
		downLoadInfo.setDownLoadStatus(1);// 0表示未下载 ,1表示下载中, 2表示暂停, 3表示已完成
		DownLoadLocalManager.getInstance().saveInfos(downLoadInfo);// 保存数据
		LocalDownInfo info = DownLoadLocalManager.getInstance().getDownLoadInfo(downLoadInfo.getVideoUrl());
		if (info != null) {
			if (info.getDownLoadStatus() == 1 && !downloader.isAlive()) {
				downloader.start();
			}
		}
		DownloadStatusUpdateTask task = new DownloadStatusUpdateTask();
		task.bindData(downLoadInfo, holder, position);
		task.execute();
		holder.task = task;
		taskList.add(task);
	}

	/**
	 * 
	 * TODO 暂停后继续下载任务
	 * 
	 * @author xszhang
	 * @param downloader
	 * @since v0.0.1
	 */
	public void restartDownloadTask(LocalDownInfo downLoadInfo, ViewHolder holder, FileDownloader downloader, int position) {
		downloader.restart();
		// 此处必须重新new一个Thread 否则会出现java.lang.IllegalThreadStateException: Thread
		// already started
		downloader = new FileDownloader(downLoadInfo.getVideoUrl());
		downloadersPool.put(downLoadInfo.getVideoUrl(), downloader);
		downloader.start();

		// 执行异步任务，更新ui
		DownloadStatusUpdateTask newtask = new DownloadStatusUpdateTask();
		newtask.bindData(downLoadInfo, holder, position);
		newtask.execute();
		holder.task = newtask;
		taskList.add(newtask);
	}

	/**
	 * 
	 * TODO 暂停下载任务
	 * 
	 * @author xszhang
	 * @param downloader
	 * @since v0.0.1
	 */
	public void pauseDownloadTask(FileDownloader downloader) {
		if (downloader.isAlive()) {
			downloader.pause();
		}
	}
}
