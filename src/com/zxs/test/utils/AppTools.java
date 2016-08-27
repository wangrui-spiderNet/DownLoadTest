package com.zxs.test.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

/**
 * 
 * TODO 张兴胜
 * <p/>
 * 创建时间: 2015年11月10日 下午5:36:58 <br/>
 * 
 * @author xszhang
 * @version
 * @since v0.0.1
 */
public class AppTools {
	/**
	 * 判断SD卡是否存在, 如果存在返回SD卡路径 , 如果不存在返回路径为空
	 * 
	 * @return
	 */
	public static String getSDPath() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			File sdDir = Environment.getExternalStorageDirectory();
			return sdDir.toString();
		}
		return "";
	}

	/**
	 * 功能说明:判断sd卡是否存在
	 * <p>
	 * isSDCardAvailable
	 * </p>
	 * 
	 * @return
	 */
	public static boolean isSDCardAvailable() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 功能说明:获取系统当期时间
	 * <p>
	 * getTime
	 * </p>
	 * 
	 * @return
	 */
	public static String getTime() {
		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();
		SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dformat.format(time);
	}

	/**
	 * 获取视频的保存路径
	 * 
	 * @return
	 */
	public static String getVideoSavePath() {
		if (AppTools.getSDPath().equals("")) {
			return "";
		}
		File file = new File(AppTools.getSDPath() + Constants.DOWNLOADVIDEOPATH);
		if (!file.exists()) {
			if (file.mkdirs()) {
				return file.getPath();
			}
			return "";
		}
		return file.getPath();
	}

	/**
	 * @方法名： getFileFullName<br>
	 * 
	 * @功能说明：<br>
	 * @param filesPath
	 *            文件存储目录地址
	 * @param fileName
	 *            文件名称
	 * @return
	 */
	public static String getFileFullName(String filesPath, String fileName) {// 20131027182407044323.mp4
		for (File file : new File(filesPath).listFiles()) {
			String name = file.getName();
			fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
			// name.substring(0, name.lastIndexOf(".")));
			if (fileName.equals(name)) {
				return file.getPath();
			}
		}
		return "";
	}

	/**
	 * 功能说明:获取屏幕密度
	 * <p>
	 * getDensity
	 * </p>
	 * 
	 * @param activity
	 * @return
	 */
	public static float getDensity(Activity activity) {
		DisplayMetrics metrices = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrices);
		float f = metrices.density;
		return f;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @param activity
	 * @return
	 */
	public static DisplayMetrics getScreenHight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	/**
	 * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
	 * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
	 * 
	 * @param videoPath
	 *            视频的路径
	 * @param width
	 *            指定输出视频缩略图的宽度
	 * @param height
	 *            指定输出视频缩略图的高度度
	 * @param kind
	 *            参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
	 *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	 * @return 指定大小的视频缩略图
	 */
	public static Bitmap getVideoThumbnail(String videoPath, int kind) {
		Bitmap bitmap = null;
		// 获取视频的缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		// System.out.println("w" + bitmap.getWidth());
		// System.out.println("h" + bitmap.getHeight());
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth(), bitmap.getHeight(), ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 功能说明:判断文件MimeType
	 * <p>
	 * getMIMEType
	 * </p>
	 * 
	 * @param f
	 * @return
	 */
	public static String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		/* 取得扩展名 */
		String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
		/* 依扩展名的类型决定MimeType */
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("wmv") || end.equals("flv") || end.equals("mpeg") || end.equals("3gp") || end.equals("mp4") || end.equals("avi") || end.equals("rmvb") || end.equals("rm")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			/* android.permission.INSTALL_PACKAGES */
			type = "application/vnd.android.package-archive";
		} else {
			type = "*";
		}
		/* 如果无法直接打开，就跳出软件列表给用户选择 */
		if (end.equals("apk")) {

		} else {
			type += "/*";
		}
		return type;
	}

	/**
	 * 将数据长度转换为KB
	 * 
	 * @param size
	 * @return
	 */
	public static String sizeTransform(int length) {
		double size = (double) length / 1024;
		return new DecimalFormat("0").format(size);
	}

	/**
	 * 根据URL下载图片
	 * 
	 * @param url
	 *            图片URL
	 * @return
	 * @throws IOException
	 */
	public static InputStream getNetInputStream(String url) throws IOException {
		HttpGet httpRequest = new HttpGet(url);
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
		HttpEntity entity = response.getEntity();
		BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
		InputStream is = bufferedHttpEntity.getContent();
		return is;
	}

	// 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
	public static void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	/**
	 * 获得SD卡总大小
	 * 
	 * @return
	 */
	public static String getSDTotalSize(Activity activity) {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return Formatter.formatFileSize(activity, blockSize * totalBlocks);
	}

	/**
	 * 获得sd卡剩余容量，即可用大小
	 * 
	 * @return
	 */
	public static String getSDAvailableSize(Activity activity) {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return Formatter.formatFileSize(activity, blockSize * availableBlocks);
	}

	/**
	 * 获得机身内存总大小
	 * 
	 * @return
	 */
	public static String getRomTotalSize(Activity activity) {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return Formatter.formatFileSize(activity, blockSize * totalBlocks);
	}

	/**
	 * 获得机身可用内存
	 * 
	 * @return
	 */
	public static String getRomAvailableSize(Activity activity) {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return Formatter.formatFileSize(activity, blockSize * availableBlocks);
	}

	/**
	 * 功能说明:返回数组，下标1代表大小，下标2代表单位 KB/MB
	 * <p>
	 * filesize
	 * </p>
	 * 
	 * @param size
	 * @return
	 */
	public static String[] filesize(long size) {
		String str = "";
		if (size >= 1024) {
			str = "KB";
			size /= 1024;
			if (size >= 1024) {
				str = "MB";
				size /= 1024;
			}
		}
		DecimalFormat formatter = new DecimalFormat();
		formatter.setGroupingSize(3);
		String result[] = new String[2];
		result[0] = formatter.format(size);
		result[1] = str;
		return result;
	}

	/**
	 * 
	 * 方法名:getFileByte
	 * <p>
	 * 功能说明：将字节数转换成文件流
	 * </p>
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileByte(String filePath) {
		int count;
		int num = 0;
		File file = new File(filePath);
		long len = file.length();
		if (file.exists()) {
			FileInputStream fis = null;
			StringBuffer sb = new StringBuffer();
			try {
				fis = new FileInputStream(file);
				byte[] buffer = new byte[(int) len];
				while ((count = fis.read(buffer)) != -1) {
					sb.append(buffer.toString());
					num = count++;
				}
				return sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fis.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}
		return "";
	}

	/**
	 * 方法名:将uri 转换为绝对路径
	 * <p>
	 * 功能说明： 将uri 转换为绝对路径
	 * </p>
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static String getAbsolutePath(Context context, Uri uri) {
		if (uri == null) {
			return null;
		}
		if (uri.toString().contains("file://")) {

			String path = uri.toString().replace("file://", "");

			return Uri.decode(path);
		}
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
		if (cursor == null) {
			return null;
		}
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/**
	 * 图片无损压缩
	 * 
	 * @param fromPath
	 *            图片来源路径
	 * @param toPath
	 *            目標存儲路徑
	 * @param width
	 *            最長邊壓縮到的長度
	 */
	public static String compressImage(String fromPath, String toPath, int width) {
		Log.i("test1", "fromPath  : " + fromPath + " ; toPath : " + toPath);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(fromPath, options);
		int be;
		// 缩放比
		if (options.outWidth > options.outHeight) {
			be = (int) (options.outWidth / width);
			be = be + ((options.outWidth % width == 0) ? 0 : 1);
		} else {
			be = (int) (options.outHeight / (float) width);
			be = be + ((options.outHeight % width == 0) ? 0 : 1);
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(fromPath, options);
		File file = new File(toPath);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return toPath;
	}

	/**
	 * 方法名: inistallAPKFile
	 * <p>
	 * 功能说明：安装APK文件
	 * </p>
	 * 
	 * @param activity
	 * @param path
	 */
	public static void inistallAPKFile(Activity activity, String path) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
		activity.startActivity(intent);
	}

}
