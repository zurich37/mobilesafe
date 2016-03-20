package com.zurich.mobile;

/**
 * Created by weixi_000
 */
public class Account {

    public static final String PACKAGE_NAME = Account.class.getPackage().getName();

    //隐式跳转协议
    public static final String STTART_MAIN_PAGE = "main_page";

    public static final String ACTION_NOTIFY_DOWNLOADING_CONTENTS_CHANGED = "notify_downloading_contents_changed";
    public static final String ACTION_PACKAGE_STATUS_CHANGED = PACKAGE_NAME + ".PACKAGE_STATUS_CHANGED";
    public static final String ACTION_EXTERNAL_APPLICATIONS_AVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE";

    public static final String ACTION_NOTIFY_CHECK_INCREMENTAL_UPDATE = "notify_check_incremental_upate";

}
