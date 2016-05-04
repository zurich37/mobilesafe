package com.zurich.mobile.model;


import com.zurich.mobile.utils.FileScanner;

public class ResidualDataPacket implements FileScanner.FileItem, PackageClearChild {
    public String filePath;
    public String fileName;
    public long fileLastModified;
    public long fileLength;

    public String appPackageName;
    public boolean obb; // true：obb；false：data

    public boolean tempChecked;

    // 清理的时候会先在子线程中把所有标记删除的文件删掉
    // 然后在列表中数据部分先标记为已删除，等全部删除完毕回到主线程的时候一次性把所有标记删除的数据从列表中删除
    // 这么做是因为在非主线程中删除列表中的数据会导致Adapter刷新不及时而引发IndexOutOfBoundsException异常
    public boolean tempDeleted;

    @Override
    public long getFileLength() {
        return fileLength;
    }

    @Override
    public boolean isChecked() {
        return tempChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        tempChecked = checked;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public boolean isDeleted() {
        return tempDeleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.tempDeleted = deleted;
    }
}