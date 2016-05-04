package com.zurich.mobile.model;

public interface PackageClearChild {
    long getFileLength();

    boolean isChecked();

    void setChecked(boolean checked);

    String getFilePath();

    boolean isDeleted();

    void setDeleted(boolean deleted);
}
