package com.zurich.mobile.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.zurich.mobile.R;
import com.zurich.mobile.adapter.itemfactory.PackageClearChildAppItemFactory;
import com.zurich.mobile.adapter.itemfactory.PackageClearChildResidualDataPacketItemFactory;
import com.zurich.mobile.adapter.itemfactory.PackageClearGroupItemFactory;
import com.zurich.mobile.assemblyadapter.AssemblyExpandableAdapter;
import com.zurich.mobile.model.AppPackage;
import com.zurich.mobile.model.PackageClearChild;
import com.zurich.mobile.model.PackageClearGroup;
import com.zurich.mobile.model.ResidualDataPacket;
import com.zurich.mobile.model.XpkInfo;
import com.zurich.mobile.utils.AppInstaller;
import com.zurich.mobile.utils.ArgbEvaluatorSupport;
import com.zurich.mobile.utils.DateUtil;
import com.zurich.mobile.utils.DensityUtil;
import com.zurich.mobile.utils.FileScanner;
import com.zurich.mobile.utils.FileUtil;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.utils.ListHeadScrollListener;
import com.zurich.mobile.utils.StorageUtils;
import com.zurich.mobile.widget.ClearHeaderView;

import net.lingala.zip4j.core.ZipFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 安装包清理
 * Created by weixinfei on 16/5/4.
 */
public class PackageClearActivity extends FragmentActivity {
    private ClearHeaderView clearHeaderView;
    private ExpandableListView listView;
    private TextView cleanButtonTextView;
    private ViewGroup bottomViewGroup;

    private ListHeadScrollListener listHeadScrollListener;

    private FileScanner fileScanner;
    private AssemblyExpandableAdapter expandableAdapter;
    private ClearDataHolder clearDataHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("安装包清理");
        setContentView(R.layout.activity_package_clear);
        initViews();
        startScan();
    }

    public void initViews() {
        listView = (ExpandableListView) findViewById(R.id.expandList_packageClear_list);
        cleanButtonTextView = (TextView) findViewById(R.id.text_packageClear_cleanButton);
        bottomViewGroup = (ViewGroup) findViewById(R.id.layout_packageClear_bottom);

        clearHeaderView = new ClearHeaderView(getBaseContext());
        clearHeaderView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getBaseContext(), 260)));
        listView.addHeaderView(clearHeaderView);

        int toolbarHeight = DensityUtil.dip2px(getBaseContext(), 50);
        clearHeaderView.setPadding(clearHeaderView.getPaddingLeft(), clearHeaderView.getPaddingTop() + toolbarHeight, clearHeaderView.getPaddingRight(), clearHeaderView.getPaddingBottom());

        listView.setOnScrollListener(listHeadScrollListener = new ListHeadScrollListener() {
            @Override
            protected void onHeadScroll(int headHeight, int scrollDistance, float percent) {
                updateToolbarBackgroundColor(percent);
//                View windowContentOverlayView = getSimpleToolbarHelper().getWindowContentOverlayView();
//                windowContentOverlayView.getBackground().setAlpha((int) (percent * 255));
//                windowContentOverlayView.setVisibility(View.VISIBLE);
            }
        }.setToolbarHeight(toolbarHeight));

        cleanButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fileScanner != null) {
                    if (fileScanner.isRunning()) {
                        fileScanner.cancel();

                    } else {
                        if (clearDataHolder.getTotalWasteLength() == 0) {
                            startScan();

                        } else {
                            clean();
                        }
                    }
                }
            }
        });
    }

    private void startScan() {

        new AsyncTask<String, Integer, String[]>() {
            @Override
            protected void onPreExecute() {
                bottomViewGroup.setVisibility(View.INVISIBLE);

                clearDataHolder = new ClearDataHolder();
                fileScanner = new FileScanner(new MyFileChecker(getBaseContext()), new MyScanListener());
                fileScanner.setDirFilter(new MyDirFilter());

                ItemFactoryEvent itemFactoryEvent = new ItemFactoryEvent();
                expandableAdapter = new AssemblyExpandableAdapter(clearDataHolder.dataList);
                expandableAdapter.addGroupItemFactory(new PackageClearGroupItemFactory(itemFactoryEvent));
                expandableAdapter.addChildItemFactory(new PackageClearChildAppItemFactory(itemFactoryEvent));
                expandableAdapter.addChildItemFactory(new PackageClearChildResidualDataPacketItemFactory(itemFactoryEvent));

                listView.setAdapter(expandableAdapter);
            }

            @Override
            protected String[] doInBackground(String... params) {
                return StorageUtils.getAllAvailableSdcardPath(getBaseContext());
            }

            @Override
            protected void onPostExecute(String[] files) {
                bottomViewGroup.setVisibility(View.VISIBLE);

                if (files == null || files.length == 0) {
                    new MyScanListener().onCompleted();
                } else {
                    fileScanner.execute(files);
                }
            }
        }.execute("");
    }

    @Override
    public void onBackPressed() {
        if (fileScanner != null && fileScanner.isRunning()) {
            fileScanner.cancel();

            return;
        }

        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (fileScanner != null && fileScanner.isRunning()) {
            fileScanner.cancel();
        }
    }

    private void updateToolbarBackgroundColor(float scrollRatio) {
        int toolbarBackgroundColor = ArgbEvaluatorSupport.evaluate(scrollRatio, Color.TRANSPARENT, clearHeaderView.getBackgroundColor());
//        getSimpleToolbar().setBackgroundColor(toolbarBackgroundColor);
    }

    @SuppressLint("SetTextI18n")
    private void updateCleanButtonText() {
        if (fileScanner != null && fileScanner.isRunning()) {
            cleanButtonTextView.setText("停止扫描");
        } else {
            long totalWasteSize = clearDataHolder.getTotalWasteLength();
            if (totalWasteSize == 0) {
                cleanButtonTextView.setText("重新扫描");
                cleanButtonTextView.setEnabled(true);
            } else {
                long cleanWasteSize = clearDataHolder.getCheckedTotalLength();
                if (cleanWasteSize == 0) {
                    cleanButtonTextView.setText("清理垃圾");
                    cleanButtonTextView.setEnabled(false);
                } else {
                    String cleanWasteSizeFormatted = Formatter.formatShortFileSize(getBaseContext(), cleanWasteSize);
                    cleanButtonTextView.setText("清理" + cleanWasteSizeFormatted + "垃圾");
                    cleanButtonTextView.setEnabled(true);
                }
            }
        }
    }

    private void clean() {
        new AsyncTask<String, Integer, Long>() {

            @Override
            protected void onPreExecute() {
                bottomViewGroup.setVisibility(View.INVISIBLE);

                clearDataHolder.setCleaning(true);
                expandableAdapter.notifyDataSetChanged();
            }

            @Override
            protected Long doInBackground(String... params) {
                return clearDataHolder.deleteAllCheckedFile();
            }

            @Override
            protected void onPostExecute(Long cleanWasteSize) {
                if (isDestroyed()) {
                    return;
                }

                bottomViewGroup.setVisibility(View.VISIBLE);
                updateCleanButtonText();

                long totalWasteSize = clearDataHolder.getTotalWasteLength();
                clearHeaderView.setWasteSize(totalWasteSize);
                if (totalWasteSize == 0) {
                    clearHeaderView.showAllCleanView(false);
                } else {
                    GlobalUtils.showToast(getBaseContext(), "本次共清理了" + Formatter.formatShortFileSize(getBaseContext(), cleanWasteSize) + "垃圾");
                }

                clearDataHolder.setCleaning(false);
                clearDataHolder.deleteAllCheckedChild();
                expandableAdapter.notifyDataSetChanged();
            }
        }.execute("");
    }

    public static String DEFAULT_DL_SUBDIR = "/Yingyonghui/yyhdownload";

    private class MyDirFilter implements FileScanner.DirFilter {

        @Override
        public boolean accept(File dir) {
            String fileNameLowerCase = dir.getName().toLowerCase();
            String keyword;

            keyword = ".";
            if (fileNameLowerCase.startsWith(".")) {
                return false;
            }

            keyword = "tuniuapp";
            if (keyword.equalsIgnoreCase(fileNameLowerCase)) {
                return false;
            }

            keyword = "cache";
            if (keyword.equalsIgnoreCase(fileNameLowerCase) || fileNameLowerCase.endsWith(keyword)) {
                return false;
            }

            keyword = "log";
            if (keyword.equalsIgnoreCase(fileNameLowerCase) || fileNameLowerCase.endsWith(keyword)) {
                return false;
            }

            keyword = "dump";
            if (keyword.equalsIgnoreCase(fileNameLowerCase) || fileNameLowerCase.endsWith(keyword)) {
                return false;
            }

            return true;
        }
    }

    private class MyFileChecker implements FileScanner.FileChecker {
        private PackageManager packageManager;

        public MyFileChecker(Context context) {
            packageManager = context.getPackageManager();
        }

        @Override
        public FileScanner.FileItem accept(File pathname) {
            String parentPathLowerCase = pathname.getParent().toLowerCase();
            // 是文件的话根据后缀名判断是APK还是XPK
            String fileNameLowerCase = pathname.getName().toLowerCase();
            if (pathname.isFile()) {
                String suffix = FileUtil.subSuffix(fileNameLowerCase);
                if (".apk".equalsIgnoreCase(suffix)) {
                    return parseFromApk(pathname);
                } else if (".xpk".equalsIgnoreCase(suffix)) {
                    return parseFromXpk(pathname);
                } else {
                    return null;
                }
            }

            // 是文件夹的话就检查是不是卸载残留的数据文件夹
            if (pathname.isDirectory()) {
                // 不检查以点开头的隐藏文件夹或名字叫system的文件夹
                if (fileNameLowerCase.startsWith(".") || "system".equalsIgnoreCase(fileNameLowerCase)) {
                    return null;
                }

                if (parentPathLowerCase.endsWith("android/obb")) {
                    ResidualDataPacket residualDataPacket = parseDataPacketDir(pathname);
                    if (residualDataPacket != null) {
                        residualDataPacket.obb = true;
                        return residualDataPacket;
                    }
                } else if (parentPathLowerCase.endsWith("android/data")) {
                    ResidualDataPacket residualDataPacket = parseDataPacketDir(pathname);
                    if (residualDataPacket != null) {
                        residualDataPacket.obb = false;
                        return residualDataPacket;
                    }
                }
            }

            return null;
        }

        @Override
        public void onFinished() {
            clearDataHolder.sort();
        }

        private AppPackage parseFromApk(File file) {
            AppPackage appPackage = new AppPackage();
            appPackage.filePath = file.getPath();
            appPackage.fileName = file.getName();
            appPackage.fileLength = file.length();
            appPackage.fileLastModified = file.lastModified();

            PackageInfo packageInfo = packageManager.getPackageArchiveInfo(file.getPath(), 0);
            if (packageInfo != null) {
                packageInfo.applicationInfo.sourceDir = file.getPath();
                packageInfo.applicationInfo.publicSourceDir = file.getPath();

                appPackage.appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                appPackage.appPackageName = packageInfo.packageName;
                appPackage.appVersionName = packageInfo.versionName;
                appPackage.appVersionCode = packageInfo.versionCode;

                try {
                    PackageInfo installedPackage = packageManager.getPackageInfo(appPackage.appPackageName, 0);
                    appPackage.installed = true;
                    appPackage.installedVersionCode = installedPackage.versionCode;
                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
                }
            } else {
                appPackage.broken = true;
            }

            return appPackage;
        }

        private AppPackage parseFromXpk(File file) {
            AppPackage appPackage = new AppPackage();
            appPackage.filePath = file.getPath();
            appPackage.fileName = file.getName();
            appPackage.fileLength = file.length();
            appPackage.xpk = true;
            appPackage.yyhSelfDownload = file.getPath().contains(DEFAULT_DL_SUBDIR);

            XpkInfo xpkInfo;
            try {
                xpkInfo = XpkInfo.getXPKManifestDom(new ZipFile(file));
                if (xpkInfo == null) {
                    throw new Exception();
                }

                appPackage.appName = xpkInfo.getAppName();
                appPackage.appPackageName = xpkInfo.getPackageName();
                appPackage.appVersionName = xpkInfo.getVersionName();
                appPackage.appVersionCode = xpkInfo.getVersionCode();

                try {
                    PackageInfo installedPackage = packageManager.getPackageInfo(appPackage.appPackageName, 0);
                    appPackage.installed = true;
                    appPackage.installedVersionCode = installedPackage.versionCode;
                } catch (PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                appPackage.broken = true;
            }

            return appPackage;
        }

        private ResidualDataPacket parseDataPacketDir(File dir) {
            String fileName = dir.getName();
            try {
                //noinspection WrongConstant
                packageManager.getPackageInfo(fileName, PackageManager.GET_UNINSTALLED_PACKAGES);
                return null;
            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
            }

            long length = FileUtil.countFileLength(dir);

            // 空的数据目录直接删除
            if (length == 0) {
                //noinspection ResultOfMethodCallIgnored
                dir.delete();
                return null;
            }

            ResidualDataPacket dataPacket = new ResidualDataPacket();
            dataPacket.filePath = dir.getPath();
            dataPacket.fileName = fileName;
            dataPacket.fileLength = length;
            dataPacket.fileLastModified = dir.lastModified();

            dataPacket.appPackageName = fileName;

            return dataPacket;
        }
    }

    private class MyScanListener implements FileScanner.ScanListener {
        private long startTime;

        @Override
        public void onStarted() {
            updateCleanButtonText();

            clearDataHolder.setScanning(true);

            clearHeaderView.setWasteSize(0);
            clearHeaderView.setProgress(0);
            clearHeaderView.setScanDir(null);

            expandableAdapter.notifyDataSetChanged();

            startTime = System.currentTimeMillis();
        }

        @Override
        public void onScanDir(File dir) {
            clearHeaderView.setScanDir(dir);
        }

        @Override
        public void onFindFile(FileScanner.FileItem fileItem) {
            if (fileItem instanceof PackageClearChild) {
                clearDataHolder.addPackageClearChild((PackageClearChild) fileItem);

                expandableAdapter.notifyDataSetChanged();
                clearHeaderView.setWasteSize(clearHeaderView.getWasteSize() + fileItem.getFileLength());
            }
        }

        @Override
        public void onUpdateProgress(int totalLength, int completedLength) {
            float percent = (float) completedLength / totalLength;

            clearHeaderView.setProgress((int) (percent * 100));
            updateToolbarBackgroundColor(listHeadScrollListener.getPercent());
        }

        @Override
        public void onCompleted() {
            clearDataHolder.setScanning(false);
            clearDataHolder.setCompleted();

            updateCleanButtonText();

            clearHeaderView.setCompleted(System.currentTimeMillis() - startTime);
            long totalWasteSize = clearDataHolder.getTotalWasteLength();
            clearHeaderView.setWasteSize(totalWasteSize);
            if (totalWasteSize == 0) {
                clearHeaderView.showAllCleanView(true);
            }

            expandableAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCanceled() {
            if (!isDestroyed()) {
                onCompleted();
            }
        }
    }

    private class ItemFactoryEvent implements PackageClearGroupItemFactory.EventListener, PackageClearChildAppItemFactory.EventListener, PackageClearChildResidualDataPacketItemFactory.EventListener {

        @Override
        public void onClickGroupCheckedButton(PackageClearGroup packageClearGroup) {
            if (packageClearGroup.cleaning) {
                return;
            }

            clearDataHolder.clickGroupCheckedButton(packageClearGroup);
            expandableAdapter.notifyDataSetChanged();
            updateCleanButtonText();
        }

        @Override
        public void onClickChildAppCheckedButton(PackageClearGroup packageClearGroup, AppPackage appPackage) {
            if (packageClearGroup.cleaning) {
                return;
            }

            clearDataHolder.clickChildCheckedButton(packageClearGroup, appPackage);
            expandableAdapter.notifyDataSetChanged();
            updateCleanButtonText();
        }

        @Override
        public void onClickChildResidualDataPacketCheckedButton(PackageClearGroup packageClearGroup, ResidualDataPacket residualDataPacket) {
            if (packageClearGroup.cleaning) {
                return;
            }

            clearDataHolder.clickChildCheckedButton(packageClearGroup, residualDataPacket);
            expandableAdapter.notifyDataSetChanged();
            updateCleanButtonText();
        }

        @Override
        public void onClickChildApp(PackageClearGroup packageClearGroup, final AppPackage appPackage) {
            if (packageClearGroup.cleaning) {
                return;
            }

            if (appPackage.broken) {
                Dialog.Builder builder = null;
                builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        super.onNegativeActionClicked(fragment);
                    }
                };

                ((SimpleDialog.Builder) builder).message("版本：" + "未知"
                        + "\n"
                        + "时间：" + DateUtil.formatTime(appPackage.fileLastModified)
                        + "\n"
                        + "包名：" + "未知"
                        + "\n"
                        + "位置：" + appPackage.filePath)
                        .title("破损安装包")
                        .positiveAction("确认")
                        .negativeAction("我知道了");
                DialogFragment fragment = DialogFragment.newInstance(builder);
                fragment.show(getSupportFragmentManager(), null);

            } else {
                Dialog.Builder builder = null;
                builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        if (appPackage.xpk) {

                        } else {
                            AppInstaller.install(PackageClearActivity.this, appPackage.filePath);
                        }
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        super.onNegativeActionClicked(fragment);
                    }
                };

                ((SimpleDialog.Builder) builder).message("版本：" + makeSafe(appPackage.appVersionName)
                        + "\n"
                        + "时间：" + DateUtil.formatTime(appPackage.fileLastModified)
                        + "\n"
                        + "包名：" + makeSafe(appPackage.appPackageName)
                        + "\n"
                        + "位置：" + appPackage.filePath)
                        .title("破损安装包")
                        .positiveAction("安装")
                        .negativeAction("我知道了");
                DialogFragment fragment = DialogFragment.newInstance(builder);
                fragment.show(getSupportFragmentManager(), null);

            }
        }

        public String makeSafe(String s) {
            return (s == null) ? "" : s;
        }

        @Override
        public void onClickChildResidualDataPacket(PackageClearGroup packageClearGroup, final ResidualDataPacket residualDataPacket) {
            if (packageClearGroup.cleaning) {
                return;
            }
            Dialog.Builder builder = null;
            builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                @Override
                public void onPositiveActionClicked(DialogFragment fragment) {

                    super.onPositiveActionClicked(fragment);
                }

                @Override
                public void onNegativeActionClicked(DialogFragment fragment) {
                    super.onNegativeActionClicked(fragment);
                }
            };

            ((SimpleDialog.Builder) builder).message("时间：" + DateUtil.formatTime(residualDataPacket.fileLastModified)
                    + "\n"
                    + "包名：" + makeSafe(residualDataPacket.appPackageName)
                    + "\n"
                    + "位置：" + residualDataPacket.filePath)
                    .title(residualDataPacket.obb ? "残留游戏数据包" : "残留应用数据")
                    .positiveAction("确定")
                    .negativeAction("我知道了");
            DialogFragment fragment = DialogFragment.newInstance(builder);
            fragment.show(getSupportFragmentManager(), null);

            /*AppChinaDialog.Builder builder = new AppChinaDialog.Builder(PackageClearActivity.this);
            builder.setTitle(residualDataPacket.obb ? "残留游戏数据包" : "残留应用数据");
            builder.setMessage(
                    "时间：" + DateUtil.formatTime(residualDataPacket.fileLastModified)
                            + "\n"
                            + "包名：" + StringUtil.makeSafe(residualDataPacket.appPackageName)
                            + "\n"
                            + "位置：" + residualDataPacket.filePath
            );
            builder.setNegativeButton("我知道了");
            builder.setPositiveButton("去详情页", new AppChinaDialog.OnClickListener() {
                @Override
                public boolean onClick(AppChinaDialog dialog, View buttonView) {
                    AppDetailActivity.launchByPackageName(PackageClearActivity.this, residualDataPacket.appPackageName, "");
                    return false;
                }
            });
            builder.show();*/
        }
    }

    private class ClearDataHolder {
        private PackageClearGroup apkPackageGroup;
        private PackageClearGroup xpkPackageGroup;
        private PackageClearGroup brokenPackageGroup;
        private PackageClearGroup residualDataPacketGroup;

        private List<PackageClearChild> apkClearChildList;
        private List<PackageClearChild> xpkClearChildList;
        private List<PackageClearChild> brokenPackageClearChildList;
        private List<PackageClearChild> residualDataPacketChildList;

        private List<Object> dataList;

        public ClearDataHolder() {
            dataList = new ArrayList<Object>(4);
            dataList.add(apkPackageGroup = new PackageClearGroup("应用安装包"));
            dataList.add(xpkPackageGroup = new PackageClearGroup("大型游戏XPK安装包"));
            dataList.add(brokenPackageGroup = new PackageClearGroup("破损安装包"));
            dataList.add(residualDataPacketGroup = new PackageClearGroup("卸载残留"));

            setScanning(true);
        }

        public void addPackageClearChild(PackageClearChild clearChild) {
            if (clearChild instanceof AppPackage) {
                AppPackage appPackage = (AppPackage) clearChild;
                if (appPackage.broken) {
                    if (brokenPackageClearChildList == null) {
                        brokenPackageClearChildList = new LinkedList<PackageClearChild>();
                    }
                    brokenPackageClearChildList.add(appPackage);
                } else if (appPackage.xpk) {
                    if (xpkClearChildList == null) {
                        xpkClearChildList = new LinkedList<PackageClearChild>();
                    }
                    xpkClearChildList.add(appPackage);
                } else {
                    if (apkClearChildList == null) {
                        apkClearChildList = new LinkedList<PackageClearChild>();
                    }
                    apkClearChildList.add(appPackage);
                }
            } else if (clearChild instanceof ResidualDataPacket) {
                ResidualDataPacket residualDataPacket = (ResidualDataPacket) clearChild;
                if (residualDataPacketChildList == null) {
                    residualDataPacketChildList = new LinkedList<PackageClearChild>();
                }
                residualDataPacketChildList.add(residualDataPacket);
            }
        }

        public void setCompleted() {
            apkPackageGroup.setItemList(apkClearChildList);
            xpkPackageGroup.setItemList(xpkClearChildList);
            brokenPackageGroup.setItemList(brokenPackageClearChildList);
            residualDataPacketGroup.setItemList(residualDataPacketChildList);

            if (dataList != null && dataList.size() > 0) {
                for (Object itemObject : dataList) {
                    if (itemObject instanceof PackageClearGroup) {
                        PackageClearGroup clearGroup = (PackageClearGroup) itemObject;
                        setDefaultChecked(clearGroup.itemList);
                        clearGroup.refreshState();
                    }
                }
            }

        }

        private void setDefaultChecked(List<PackageClearChild> clearItemList) {
            if (clearItemList != null && clearItemList.size() > 0) {
                AppPackage appPackage;
                for (PackageClearChild clearItem : clearItemList) {
                    if (clearItem instanceof AppPackage) {
                        appPackage = (AppPackage) clearItem;

                        if (appPackage.broken) {
                            if (!appPackage.yyhSelfDownload) {
                                appPackage.tempChecked = true;
                            }
                        } else {
                            if (appPackage.isOldVersion() || appPackage.isSameVersion()) {
                                appPackage.tempChecked = true;
                            }
                        }
                    }
                }
            }
        }

        public void setScanning(boolean scanning) {
            if (dataList != null && dataList.size() > 0) {
                for (Object itemObject : dataList) {
                    if (itemObject instanceof PackageClearGroup) {
                        PackageClearGroup packageClearGroup = (PackageClearGroup) itemObject;
                        packageClearGroup.scanning = scanning;
                    }
                }
            }
        }

        private void setCleaning(boolean cleaning) {
            if (dataList != null && dataList.size() > 0) {
                for (Object itemObject : dataList) {
                    if (itemObject instanceof PackageClearGroup) {
                        PackageClearGroup packageClearGroup = (PackageClearGroup) itemObject;
                        packageClearGroup.cleaning = cleaning;
                    }
                }
            }
        }

        public void clickGroupCheckedButton(PackageClearGroup packageClearGroup) {
            boolean newCheckedState;
            if (packageClearGroup.isAllChecked()) {
                newCheckedState = false;
            } else if (packageClearGroup.isAllUnchecked()) {
                newCheckedState = true;
            } else {
                newCheckedState = true;
            }
            packageClearGroup.setAllChildCheckedState(newCheckedState);
        }

        public void clickChildCheckedButton(PackageClearGroup packageClearGroup, PackageClearChild packageClearChild) {
            packageClearChild.setChecked(!packageClearChild.isChecked());
            packageClearGroup.refreshState();
        }

        public long getTotalWasteLength() {
            long totalLength = 0;
            if (dataList != null && dataList.size() > 0) {
                for (Object itemObject : dataList) {
                    if (itemObject instanceof PackageClearGroup) {
                        totalLength += ((PackageClearGroup) itemObject).totalLength;
                    }
                }
            }
            return totalLength;
        }

        public long getCheckedTotalLength() {
            long checkedTotalLength = 0;
            if (dataList != null && dataList.size() > 0) {
                for (Object itemObject : dataList) {
                    if (itemObject instanceof PackageClearGroup) {
                        checkedTotalLength += ((PackageClearGroup) itemObject).checkedTotalLength;
                    }
                }
            }
            return checkedTotalLength;
        }

        public long deleteAllCheckedFile() {
            if (dataList == null || dataList.size() == 0) {
                return 0;
            }

            long deletedTotalLength = 0;
            for (Object itemObject : dataList) {
                if (itemObject instanceof PackageClearGroup) {
                    PackageClearGroup clearGroup = (PackageClearGroup) itemObject;
                    if (clearGroup.itemList != null && clearGroup.itemList.size() > 0) {
                        for (PackageClearChild clearChild : clearGroup.itemList) {
                            if (clearChild != null && clearChild.isChecked()) {
                                File wasteFile = new File(clearChild.getFilePath());
                                if (!wasteFile.exists() || FileUtil.deleteFile(wasteFile)) {
                                    clearChild.setDeleted(true);
                                    deletedTotalLength += clearChild.getFileLength();
                                }
                            }
                        }
                        clearGroup.refreshState();
                    }
                }
            }

            return deletedTotalLength;
        }

        public void deleteAllCheckedChild() {
            if (dataList == null || dataList.size() == 0) {
                return;
            }

            for (Object itemObject : dataList) {
                if (itemObject instanceof PackageClearGroup) {
                    PackageClearGroup clearGroup = (PackageClearGroup) itemObject;
                    if (clearGroup.itemList != null && clearGroup.itemList.size() > 0) {
                        Iterator<PackageClearChild> childIterator = clearGroup.itemList.iterator();
                        PackageClearChild clearChild;
                        while (childIterator.hasNext()) {
                            clearChild = childIterator.next();
                            if (clearChild != null && clearChild.isChecked() && clearChild.isDeleted()) {
                                childIterator.remove();
                            }
                        }
                        clearGroup.refreshState();
                    }
                }
            }
        }

        public void sort() {
            Comparator<PackageClearChild> comparator = new Comparator<PackageClearChild>() {
                @Override
                public int compare(PackageClearChild lhs, PackageClearChild rhs) {
                    return (int) (rhs.getFileLength() - lhs.getFileLength());
                }
            };

            if (apkClearChildList != null && apkClearChildList.size() > 0) {
                Collections.sort(apkClearChildList, comparator);
            }

            if (xpkClearChildList != null && xpkClearChildList.size() > 0) {
                Collections.sort(xpkClearChildList, comparator);
            }

            if (brokenPackageClearChildList != null && brokenPackageClearChildList.size() > 0) {
                Collections.sort(brokenPackageClearChildList, comparator);
            }

            if (residualDataPacketChildList != null && residualDataPacketChildList.size() > 0) {
                Collections.sort(residualDataPacketChildList, comparator);
            }
        }
    }
}
