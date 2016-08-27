package com.zxs.test.utils;

import android.content.Context;

/**
 * 
 * TODO 当前上下文
 * <p/>
 * 创建时间: 2015年11月10日 下午5:46:56 <br/>
 * 
 * @author xszhang
 * @version
 * @since v0.0.1
 */
public class AppConfig {
	// 当前上下文
	private static Context context;

	/**
	 * @return the mContext
	 */
	public static Context getContext() {
		return context;
	}

	/**
	 * @param mContext
	 *            the mContext to set
	 */
	public static void setContext(Context mContext) {
		context = mContext;
	}

}
