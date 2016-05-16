package com.zurich.mobile.adapter.itemfactory;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyGroupItem;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyGroupItemFactory;
import com.zurich.mobile.model.PackageClearGroup;

public class PackageClearGroupItemFactory extends AssemblyGroupItemFactory<PackageClearGroupItemFactory.PackageClearGroupItem> {
    private EventListener eventListener;

    public PackageClearGroupItemFactory(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof PackageClearGroup;
    }

    @Override
    public PackageClearGroupItem createAssemblyItem(ViewGroup parent) {
        return new PackageClearGroupItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_package_clear_group, parent, false), this);
    }

    public interface EventListener {
        void onClickGroupCheckedButton(PackageClearGroup packageClearGroup);
    }

    public class PackageClearGroupItem extends AssemblyGroupItem<PackageClearGroup, PackageClearGroupItemFactory> {
        private TextView titleTextView;
        private ImageView checkedImageView;
        private TextView sizeTextView;
        private ProgressBar progressBar;

        private Drawable upDrawable;
        private Drawable downDrawable;

        private Drawable allCheckedDrawable;
        private Drawable partCheckedDrawable;
        private Drawable allUncheckedDrawable;

        protected PackageClearGroupItem(View convertView, PackageClearGroupItemFactory itemFactory) {
            super(convertView, itemFactory);
        }


        @Override
        protected void onFindViews(View convertView) {
            titleTextView = (TextView) convertView.findViewById(R.id.text_packageClearGroupItem_title);
            checkedImageView = (ImageView) convertView.findViewById(R.id.image_packageClearGroupItem_checked);
            sizeTextView = (TextView) convertView.findViewById(R.id.text_packageClearGroupItem_size);
            progressBar = (ProgressBar) convertView.findViewById(R.id.progress_packageClearGroupItem);
        }

        @Override
        protected void onConfigViews(Context context) {
//            checkedImageView.setImageDrawable(SkinResFactory.createCheckboxDrawable(context));

            checkedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (eventListener != null && !getData().cleaning) {
                        eventListener.onClickGroupCheckedButton(getData());
                    }
                }
            });

//            upDrawable = new FontDrawable(context, FontDrawable.DIRECTION_UP).setTextColor(sizeTextView.getCurrentTextColor()).setTextSizeDp(12);
//            downDrawable = new FontDrawable(context, FontDrawable.DIRECTION_DOWN).setTextColor(sizeTextView.getCurrentTextColor()).setTextSizeDp(12);
//
//            allCheckedDrawable = new FontDrawable(context, FontDrawable.CHECKED).setTextSizeDp(18);
//            partCheckedDrawable = new FontDrawable(context, FontDrawable.PART_CHECKED).setTextSizeDp(18);
//            allUncheckedDrawable = new FontDrawable(context, FontDrawable.UNCHECKED).setTextSizeDp(18).setTextColor(context.getResources().getColor(R.color.font_icon_grey));
        }

        @Override
        protected void onSetData(int groupPosition, boolean isExpanded, PackageClearGroup packageClearGroup) {
            titleTextView.setText(packageClearGroup.title);

            if (packageClearGroup.scanning) {
                titleTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                sizeTextView.setText(null);

                progressBar.setVisibility(View.VISIBLE);

                checkedImageView.setVisibility(View.INVISIBLE);
            } else if(packageClearGroup.cleaning){
                titleTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                sizeTextView.setText("正在清理");

                progressBar.setVisibility(View.VISIBLE);

                checkedImageView.setVisibility(View.INVISIBLE);
            } else {
                if (packageClearGroup.getChildCount() > 0) {
                    titleTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, isExpanded ? upDrawable : downDrawable, null);
                    sizeTextView.setText(Formatter.formatShortFileSize(sizeTextView.getContext(), packageClearGroup.totalLength));

                    progressBar.setVisibility(View.INVISIBLE);

                    if (packageClearGroup.isAllChecked()) {
                        checkedImageView.setImageDrawable(allCheckedDrawable);
                    } else if (packageClearGroup.isAllUnchecked()) {
                        checkedImageView.setImageDrawable(allUncheckedDrawable);
                    } else {
                        checkedImageView.setImageDrawable(partCheckedDrawable);
                    }
                    checkedImageView.setVisibility(View.VISIBLE);
                } else {
                    titleTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    sizeTextView.setText("未发现");

                    progressBar.setVisibility(View.INVISIBLE);
                    checkedImageView.setVisibility(View.GONE);
                }
            }
        }
    }
}
