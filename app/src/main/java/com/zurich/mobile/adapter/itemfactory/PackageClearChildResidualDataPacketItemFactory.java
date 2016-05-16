package com.zurich.mobile.adapter.itemfactory;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyChildItem;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyChildItemFactory;
import com.zurich.mobile.model.PackageClearGroup;
import com.zurich.mobile.model.ResidualDataPacket;

public class PackageClearChildResidualDataPacketItemFactory extends AssemblyChildItemFactory<PackageClearChildResidualDataPacketItemFactory.PackageClearChildResidualDataPacketItem> {
    private EventListener eventListener;

    public PackageClearChildResidualDataPacketItemFactory(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public boolean isTarget(Object itemObject) {
        return itemObject instanceof ResidualDataPacket;
    }

    @Override
    public PackageClearChildResidualDataPacketItem createAssemblyItem(ViewGroup parent) {
        return new PackageClearChildResidualDataPacketItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_package_clear_child_residual_data_packet, parent, false), this);
    }

    public interface EventListener {
        void onClickChildResidualDataPacketCheckedButton(PackageClearGroup packageClearGroup, ResidualDataPacket residualDataPacket);

        void onClickChildResidualDataPacket(PackageClearGroup packageClearGroup, ResidualDataPacket residualDataPacket);
    }

    public class PackageClearChildResidualDataPacketItem extends AssemblyChildItem<ResidualDataPacket, PackageClearChildResidualDataPacketItemFactory> {
        private TextView appNameTextView;
        private TextView typeTextView;
        private TextView sizeTextView;
        private ImageView checkedImageView;

        private Drawable checkedDrawable;
        private Drawable uncheckedDrawable;

        protected PackageClearChildResidualDataPacketItem(View convertView, PackageClearChildResidualDataPacketItemFactory itemFactory) {
            super(convertView, itemFactory);
        }

        @Override
        protected void onFindViews(View convertView) {
            appNameTextView = (TextView) convertView.findViewById(R.id.text_packageClearChildResidualDataPacketItem_name);
            typeTextView = (TextView) convertView.findViewById(R.id.text_packageClearChildResidualDataPacketItem_type);
            sizeTextView = (TextView) convertView.findViewById(R.id.text_packageClearChildResidualDataPacketItem_size);
            checkedImageView = (ImageView) convertView.findViewById(R.id.image_packageClearChildResidualDataPacketItem_checked);
        }

        @Override
        protected void onConfigViews(Context context) {
            checkedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (eventListener != null) {
                        eventListener.onClickChildResidualDataPacketCheckedButton((PackageClearGroup) getAdapter().getGroup(getGroupPosition()), getData());
                    }
                }
            });

            getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (eventListener != null) {
                        eventListener.onClickChildResidualDataPacket((PackageClearGroup) getAdapter().getGroup(getGroupPosition()), getData());
                    }
                }
            });

//            checkedDrawable = new FontDrawable(context, FontDrawable.CHECKED).setTextSizeDp(18);
//            uncheckedDrawable = new FontDrawable(context, FontDrawable.UNCHECKED).setTextSizeDp(18).setTextColor(context.getResources().getColor(R.color.font_icon_grey));
        }

        @Override
        protected void onSetData(int groupPosition, int childPosition, boolean isLastChild, ResidualDataPacket residualDataPacket) {
            appNameTextView.setText(residualDataPacket.fileName);
            sizeTextView.setText(Formatter.formatFileSize(sizeTextView.getContext(), residualDataPacket.fileLength));

            typeTextView.setText(residualDataPacket.obb ? "游戏数据包" : "应用数据");

            if (residualDataPacket.isChecked()) {
                checkedImageView.setImageDrawable(checkedDrawable);
            } else {
                checkedImageView.setImageDrawable(uncheckedDrawable);
            }
        }
    }
}
