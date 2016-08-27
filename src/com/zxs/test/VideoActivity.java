package com.zxs.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.zxs.downloadtest.R;
import com.zxs.test.model.LocalDownInfo;
import com.zxs.test.ui.adapter.VideoDownLoadAdapter2;
import com.zxs.test.ui.adapter.VideoDownLoadAdapter2.DownloadStatusUpdateTask;

public class VideoActivity extends Activity {
	private ListView listView;
	private VideoDownLoadAdapter2 adapter;

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download_layout);
		this.initModule();
		this.initData();
	}

	private void initModule() {
		this.listView = (ListView) findViewById(R.id.listview);
	}

	private void initData() {
		List<LocalDownInfo> list = new ArrayList<LocalDownInfo>();

		LocalDownInfo local = new LocalDownInfo();
		local.setVideoId("001");
		local.setVideoName("别问我是谁.mp3");
		local.setVideoUrl("http://jxfile.qiniudn.com/1447726137107aWRlmkDXiq.mp3");
		list.add(local);

		LocalDownInfo local1 = new LocalDownInfo();
		local1.setVideoId("002");
		local1.setVideoName("校园云第三方登录接入指南.pdf");
		local1.setVideoUrl("http://jxfile.qiniudn.com/1447668593511azW6PFjMhW.pdf");
		list.add(local1);

		LocalDownInfo local2 = new LocalDownInfo();
		local2.setVideoId("003");
		local2.setVideoName("在市场下，如何面的低价中标.ppt");
		local2.setVideoUrl("http://jxfile.qiniudn.com/1447668590924m3PNersVB1.ppt");
		list.add(local2);

		LocalDownInfo local3 = new LocalDownInfo();
		local3.setVideoId("004");
		local3.setVideoName("eclipse工具相关快捷键.docx");
		local3.setVideoUrl("http://jxfile.qiniudn.com/14476684912328ZGkWvROk0.docx");
		list.add(local3);

		LocalDownInfo local4 = new LocalDownInfo();
		local4.setVideoId("005");
		local4.setVideoName("爱情转移.mp4");
		local4.setVideoUrl("http://jxfile.qiniudn.com/14476684572698j1ghrXaxz.mp4");
		list.add(local4);

		LocalDownInfo local5 = new LocalDownInfo();
		local5.setVideoId("006");
		local5.setVideoName("006.mp4");
		local5.setVideoUrl("");
		list.add(local5);

		LocalDownInfo local6 = new LocalDownInfo();
		local6.setVideoId("007");
		local6.setVideoName("007.mp4");
		local6.setVideoUrl("");
		list.add(local6);

		LocalDownInfo local7 = new LocalDownInfo();
		local7.setVideoId("008");
		local7.setVideoName("008.mp4");
		local7.setVideoUrl("");
		list.add(local7);

		LocalDownInfo local8 = new LocalDownInfo();
		local8.setVideoId("009");
		local8.setVideoName("009.mp4");
		local8.setVideoUrl("");
		list.add(local8);

		LocalDownInfo local9 = new LocalDownInfo();
		local9.setVideoId("100");
		local9.setVideoName("100.mp4");
		local9.setVideoUrl("");
		list.add(local9);

		LocalDownInfo local10 = new LocalDownInfo();
		local10.setVideoId("1001");
		local10.setVideoName("1001.mp4");
		local10.setVideoUrl("");
		list.add(local10);

		this.adapter = new VideoDownLoadAdapter2(this, handler, list);
		this.listView.setAdapter(adapter);
	}

	private Handler handler = new Handler() {

		/**
		 * TODO 简单描述该方法的实现功能（可选）.
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		List<DownloadStatusUpdateTask> list = adapter.getTaskList();
		for (DownloadStatusUpdateTask task : list) {
			if (!task.isCancelled()) {
				task.cancel(true);
			}
		}
		super.onDestroy();
	}

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}

}
