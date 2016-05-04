package com.zurich.mobile.widget;

import android.content.Context;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.utils.ArgbEvaluatorSupport;
import com.zurich.mobile.utils.DateUtil;
import com.zurich.mobile.utils.ViewAnimations;

import java.io.File;

public class ClearHeaderView extends FrameLayout {
    private static final int MAX_WASTE_SIZE = 500 * 1024 * 1024;
    private static final int MIN_WASTE_SIZE_BACKGROUND_COLOR = 0xFF1FA525;
    private static final int MAX_WASTE_SIZE_BACKGROUND_COLOR = 0xFFC0340A;

    private TextView wasteSizeTextView;
    private TextView unitTextView;
    private TextView dirTextView;
    private ProgressBar progressBar;
    private TextView allCleanTextView;

    private long wasteSize;
    private int progress;
    private int backgroundColor;
    private File scanDir;

    public ClearHeaderView(Context context) {
        super(context);
        init(context);
    }

    public ClearHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_clear_header, this);
        wasteSizeTextView = (TextView) findViewById(R.id.text_clearHeader_wasteSize);
        progressBar = (ProgressBar) findViewById(R.id.progress_clearHeader);
        dirTextView = (TextView) findViewById(R.id.text_clearHeader_dir);
        unitTextView = (TextView) findViewById(R.id.text_clearHeader_unit);
        allCleanTextView = (TextView) findViewById(R.id.text_clearHeader_allClean);

        allCleanTextView.setVisibility(View.INVISIBLE);

        if (isInEditMode()) {
            wasteSize = 200 * 1024 * 1024;
            progress = 40;
        }
        setWasteSize(wasteSize);
        setProgress(progress);
        setScanDir(scanDir);
    }

    public void setProgress(int progress) {
        this.progress = progress % 100;
        progressBar.setProgress(progress);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public long getWasteSize() {
        return wasteSize;
    }

    public void setWasteSize(long newWasteSize) {
        this.wasteSize = newWasteSize;
        String wasteSizeFormatted = Formatter.formatShortFileSize(getContext(), wasteSize);
        String[] sizeFormattedItems = splitSizeFormatted(wasteSizeFormatted);
        if (sizeFormattedItems != null && sizeFormattedItems.length == 2) {
            wasteSizeTextView.setText(sizeFormattedItems[0]);
            unitTextView.setText(sizeFormattedItems[1]);
        } else {
            wasteSizeTextView.setText("0");
            unitTextView.setText("KB");
        }

        /*
         * 根据垃圾的多少改变背景色
         */
        long progressSize = wasteSize;
        if (progressSize > MAX_WASTE_SIZE) {
            progressSize = MAX_WASTE_SIZE;
        }
        float backgroundProgress = (float) progressSize / MAX_WASTE_SIZE;
        backgroundColor = ArgbEvaluatorSupport.evaluate(backgroundProgress, MIN_WASTE_SIZE_BACKGROUND_COLOR, MAX_WASTE_SIZE_BACKGROUND_COLOR);
        setBackgroundColor(backgroundColor);

        if(allCleanTextView.getVisibility() == View.VISIBLE){
            ViewAnimations.visibleViewByAlpha(wasteSizeTextView);
            ViewAnimations.visibleViewByAlpha(unitTextView);
            ViewAnimations.visibleViewByAlpha(dirTextView);
            ViewAnimations.visibleViewByAlpha(progressBar);

            ViewAnimations.invisibleViewByAlpha(allCleanTextView);
        }
    }

    public void setScanDir(File newScanDir) {
        this.scanDir = newScanDir;
        if (dirTextView.getGravity() != (Gravity.LEFT | Gravity.CENTER_VERTICAL)) {
            dirTextView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        }
        if (scanDir != null) {
            dirTextView.setText(newScanDir.getPath());
        } else {
            dirTextView.setText(null);
        }
    }

    public void setCompleted(long useTime) {
        setProgress(100);
        dirTextView.setGravity(Gravity.CENTER);
        dirTextView.setText("扫描完成，用时" + DateUtil.convertLucidUseTime(useTime));
    }

    public void showAllCleanView(boolean notFondWaste){
        ViewAnimations.invisibleViewByAlpha(wasteSizeTextView);
        ViewAnimations.invisibleViewByAlpha(unitTextView);
        ViewAnimations.invisibleViewByAlpha(dirTextView);
        ViewAnimations.invisibleViewByAlpha(progressBar);

        allCleanTextView.setText(notFondWaste ? "没有发现安装包" : "清理完毕");
        ViewAnimations.visibleViewByAlpha(allCleanTextView);
    }

    private static String[] splitSizeFormatted(String sizeFormatted) {
        if (sizeFormatted == null) {
            return null;
        }

        int subPosition = sizeFormatted.length() - 1;
        char currentChar;
        for (int w = 0, size = sizeFormatted.length(); w < size; w++) {
            currentChar = sizeFormatted.charAt(w);
            // 不是数字也不是小数点
            if (!Character.isDigit(currentChar) && '.' != currentChar) {
                subPosition = w;
                break;
            }
        }

        String[] items = new String[2];
        items[0] = sizeFormatted.substring(0, subPosition);
        items[1] = sizeFormatted.substring(subPosition);
        return items;
    }
}
