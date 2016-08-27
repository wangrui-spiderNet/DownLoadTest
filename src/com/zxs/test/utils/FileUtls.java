package com.zxs.test.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

/**
 * 
 * TODO 类描述
 * <p/>
 * 创建时间: 2015年11月12日 下午6:59:14 <br/>
 * 
 * @author xszhang
 * @version
 * @since v0.0.1
 */
public class FileUtls {
	/**
	 * 检测文件是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isFileExist(String path) {
		try {
			if (!TextUtils.isEmpty(path)) {
				File file = new File(path);
				return file.exists();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

	/**
	 * 检测文件是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isFileExist(File file) {
		try {
			return file.exists();
		} catch (Exception ex) {
		}
		return false;
	}

	/**
	 * <b>通过文件file对象获取文件标识MimeType</b><br>
	 * <br>
	 * MIME type的缩写为(Multipurpose Internet Mail Extensions)代表互联网媒体类型(Internet
	 * media type)，
	 * MIME使用一个简单的字符串组成，最初是为了标识邮件Email附件的类型，在html文件中可以使用content-type属性表示，
	 * 描述了文件类型的互联网标准。MIME类型能包含视频、图像、文本、音频、应用程序等数据。
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileMimeTypeFromFile(File file) {
		String extension = getFileExtension(file);
		extension = extension.replace(".", "");
		if (extension.equals("docx") || extension.equals("wps")) {
			extension = "doc";
		} else if (extension.equals("xlsx")) {
			extension = "xls";
		} else if (extension.equals("pptx")) {
			extension = "ppt";
		}
		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
		if (mimeTypeMap.hasExtension(extension)) {
			// 获得txt文件类型的MimeType
			return mimeTypeMap.getMimeTypeFromExtension(extension);
		} else {
			if (extension.equals("dwg")) {
				return "application/x-autocad";
			} else if (extension.equals("dxf")) {
				return "application/x-autocad";
			} else if (extension.equals("ocf")) {
				return "application/x-autocad";
			} else {
				return "*/*";
			}
		}
	}

	/**
	 * <b>通过文件的扩展名Extension获取文件标识MimeType</b><br>
	 * <br>
	 * MIME type的缩写为(Multipurpose Internet Mail Extensions)代表互联网媒体类型(Internet
	 * media type)，
	 * MIME使用一个简单的字符串组成，最初是为了标识邮件Email附件的类型，在html文件中可以使用content-type属性表示，
	 * 描述了文件类型的互联网标准。MIME类型能包含视频、图像、文本、音频、应用程序等数据。
	 * 
	 * @param extension
	 * @return
	 */
	public static String getFileMimeTypeFromExtension(String extension) {
		extension = extension.replace(".", "");
		if (extension.equals("docx") || extension.equals("wps")) {
			extension = "doc";
		} else if (extension.equals("xlsx")) {
			extension = "xls";
		} else if (extension.equals("pptx")) {
			extension = "ppt";
		}
		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
		if (mimeTypeMap.hasExtension(extension)) {
			// 获得txt文件类型的MimeType
			return mimeTypeMap.getMimeTypeFromExtension(extension);
		} else {
			if (extension.equals("dwg")) {
				return "application/x-autocad";
			} else if (extension.equals("dxf")) {
				return "application/x-autocad";
			} else if (extension.equals("ocf")) {
				return "application/x-autocad";
			} else {
				return "*/*";
			}
		}
	}

	/**
	 * 获取文件扩展名(不包含前面那个.)
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileExtension(File file) {
		if (file == null || file.isDirectory()) {
			return "";
		}
		String filename = file.getName();
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return "";
	}

	/*
	 * 打开文件信息
	 */
	private void openFile(Context context, File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		// 调用getMIMEType()来取得MimeType 视频类型（支持wmv、flv、mpeg、3gp、rmvb、mp4、avi）
		String type = getMIMEType(f);
		// 设置intent的file与MimeType
		intent.setDataAndType(Uri.fromFile(f), type); // Type类型 video/mp4
		context.startActivity(intent);
	}

	// android获取一个用于打开音频文件的intent

	public static Intent getAudioFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}

	public static void openFile(Context context, String fileName) {
		// TODO 自动生成的方法存根
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		File file = new File(fileName);
		String type = getMIMEType(file);
		intent.setDataAndType(Uri.fromFile(file), type);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			 Toast.makeText(context, "没有打开此类型文件的应用！", Toast.LENGTH_LONG).show();
		}
	}

	public static String getMIMEType(File file) {
		String type = "*/*";
		String fName = file.getName();
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}

		/* 获取文件的后缀名 */
		String postFix = fName.substring(dotIndex, fName.length()).toLowerCase();
		if (postFix.equals(""))
			return type;

		for (int i = 0; i < Constants.MIME_MapTable.length; i++) {
			if (postFix.equals(Constants.MIME_MapTable[i][0]))
				type = Constants.MIME_MapTable[i][1];
		}
		return type;
	}

}
