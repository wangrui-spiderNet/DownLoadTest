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
public class VideoDownLoadAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Handler handler = null;
	private List<LocalDownInfo> list;
	private Activity activity;
	// 存放各个下载器
	public static Map<String, FileDownloader> downloadersPool = new HashMap<String, FileDownloader>();

	private List<DownloadStatusUpdateTask> taskList = new ArrayList<DownloadStatusUpdateTask>();

	public VideoDownLoadAdapter(Activity activity, Handler handler, List<LocalDownInfo> list) {
		this.mInflater = LayoutInflater.from(activity);
		this.list = list;
		this.activity = activity;
		this.handler = handler;
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
		return 0;
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

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Log.i("test", "position=======" + position);
		System.out.println("position=======" + position);
		LocalDownInfo downLoadInfo = getItem(position);
		String urlstr = downLoadInfo.getVideoUrl();
		// 根据url去检测本地的sd卡路径是否存在此文件，如果人为删除，则删除本地库的相关信息
		// if (false == FileUtls.isFileExist(urlstr)) {
		// // 根据url去检测本地库是否有此数据，避免第一次删除时报错
		// if (false == DownLoadLocalManager.getInstance().isHasInfors(urlstr)
		// &&
		// DownLoadLocalManager.getInstance().getDownLoadInfo(urlstr).getProgressSize()
		// != 0) {
		// // 如果存在则删除此条信息
		// DownLoadLocalManager.getInstance().deleteVideoInfo(urlstr);
		// Toast.makeText(activity, "执行删除了么"+urlstr, Toast.LENGTH_SHORT).show();
		// }
		// }
		// 重新检索本地库的数据
		List<LocalDownInfo> localList = DownLoadLocalManager.getInstance().getAllResourceList();// 本地所有的下载信息
		if (null != localList && localList.size() > 0) {
			for (int i = 0; i < localList.size(); i++) {
				if (downLoadInfo.getVideoUrl().equals(localList.get(i).getVideoUrl())) {// 网络url和本地url相同
					list.get(i).setDownLoadStatus(localList.get(i).getDownLoadStatus());
					list.get(i).setEndPos(localList.get(i).getEndPos());
					list.get(i).setProgressSize(localList.get(i).getProgressSize());
					list.get(i).setStartPos(localList.get(i).getStartPos());
					list.get(i).setVideoName(localList.get(i).getVideoName());
					list.get(i).setVideoUrl(localList.get(i).getVideoUrl());
					downLoadInfo = localList.get(i);
				}
			}
		}
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
			holder.lis = new MyCheckLis();
			holder.videoDownload.setOnClickListener(holder.lis);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.lis.bindData(holder, downLoadInfo);
		bindData(downLoadInfo, holder);
		return convertView;
	}

	private void bindData(LocalDownInfo downLoadInfo, ViewHolder holder) {
		holder.downloadProgress.setVisibility(View.VISIBLE);
		holder.videoName.setText(downLoadInfo.getVideoName());
		float num = (float) downLoadInfo.getProgressSize() / (float) downLoadInfo.getEndPos();// 下载数据量的百分比
		if (Float.compare(num, Float.NaN) == 0) {
			num = 0;
		}
		BigDecimal result = new BigDecimal((double) (num * 100));
		// 设置进度条按读取的length长度更新
		holder.downloadProgress.setProgress((int) (num * 100));
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
	}

	private class ViewHolder {
		TextView videoName;
		ProgressBar downloadProgress;
		TextView downloadType;
		LinearLayout videoDownload;
		ImageView typeIcon;
		ImageView imageVideoThum;
		TextView type;
		MyCheckLis lis = null;
		DownloadStatusUpdateTask task = null;
	}

	/**
	 * 
	 * @ClassName: MyCheckLis
	 * @Description: TODO(这里用一句话描述这个类的作用)
	 * @author zhang.shaohua
	 * @date 2013年11月23日 下午1:52:12
	 * 
	 */
	public class MyCheckLis implements OnClickListener {
		private ViewHolder holder;
		private LocalDownInfo downLoadInfo;

		public void bindData(ViewHolder holder, LocalDownInfo downLoadInfo) {
			this.holder = holder;
			this.downLoadInfo = downLoadInfo;
		}

		@Override
		public void onClick(View view) {
			if (view.getId() == R.id.video_download) {
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
							startDownloadTask(downLoadInfo, holder, downloader);
						} else if ("暂停".equals(type)) { // 暂停
							holder.typeIcon.setImageResource(R.drawable.button_play);
							holder.type.setText("继续");
							pauseDownloadTask(downloader);
						} else if ("继续".equals(type)) {
							holder.typeIcon.setImageResource(R.drawable.button_stop);
							holder.type.setText("暂停");
							restartDownloadTask(downLoadInfo, holder, downloader);
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
		}
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
		LocalDownInfo info = null;

		public void bindData(LocalDownInfo info, ViewHolder holder) {
			this.holder = holder;
			this.info = info;
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
			BigDecimal result = new BigDecimal((double) (params[1] / 100f));
			// 设置进度条按读取的length长度更新
			int incrementProgress = params[0];
			Log.e("test", "onProgressUpdate incrementProgress:" + incrementProgress);
			holder.downloadProgress.setProgress(incrementProgress);
			holder.downloadType.setText(activity.getResources().getString(R.string.download_type, result.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() + "%"));// 用TextView显示下载的百分比
		}

		@Override
		protected void onPostExecute(Integer status) {
			// 设置进度条按读取的length长度更新
			if (status == 3) {
				holder.type.setText("打开");
				holder.typeIcon.setImageResource(R.drawable.button_complete);
				holder.downloadType.setText(activity.getResources().getString(R.string.download_type, "100%"));// 用TextView显示下载的百分比
				Toast.makeText(activity, holder.videoName.getText() + "已下载完成！", Toast.LENGTH_SHORT).show();
			}
			handler.sendEmptyMessage(0);
		}

		@Override
		protected void onCancelled() {
			holder.type.setText("已取消");
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
	public void startDownloadTask(LocalDownInfo downLoadInfo, ViewHolder holder, FileDownloader downloader) {
		downLoadInfo.setDownLoadStatus(1);// 0表示未下载 ,1表示下载中, 2表示暂停, 3表示已完成
		DownLoadLocalManager.getInstance().saveInfos(downLoadInfo);// 保存数据
		LocalDownInfo info = DownLoadLocalManager.getInstance().getDownLoadInfo(downLoadInfo.getVideoUrl());
		if (info != null) {
			if (info.getDownLoadStatus() == 1 && !downloader.isAlive()) {
				downloader.start();
			}
		}
		DownloadStatusUpdateTask task = new DownloadStatusUpdateTask();
		task.bindData(downLoadInfo, holder);
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
	public void restartDownloadTask(LocalDownInfo downLoadInfo, ViewHolder holder, FileDownloader downloader) {
		downloader.restart();
		// 此处必须重新new一个Thread 否则会出现java.lang.IllegalThreadStateException: Thread
		// already started
		downloader = new FileDownloader(downLoadInfo.getVideoUrl());
		downloadersPool.put(downLoadInfo.getVideoUrl(), downloader);
		downloader.start();

		// 执行异步任务，更新ui
		DownloadStatusUpdateTask newtask = new DownloadStatusUpdateTask();
		newtask.bindData(downLoadInfo, holder);
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
