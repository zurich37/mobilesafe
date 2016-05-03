package com.zurich.mobile.adapter;


import com.zurich.mobile.assemblyadapter.AssemblyAdapter;
import com.zurich.mobile.assemblyadapter.AssemblyItemFactory;
import com.zurich.mobile.widget.PinnedSectionListView;

import java.util.List;

public class AssemblyPinnedSectionAdapter extends AssemblyAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    public AssemblyPinnedSectionAdapter(List dataList) {
        super(dataList);
    }

    @SuppressWarnings("unused")
    public AssemblyPinnedSectionAdapter(Object... dataArray) {
        super(dataArray);
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        List<AssemblyItemFactory> itemFactoryList = getItemFactoryList();
        if (itemFactoryList != null && itemFactoryList.size() > 0) {
            for (AssemblyItemFactory itemFactory : itemFactoryList) {
                if (itemFactory.getItemType() == viewType && itemFactory instanceof PinnedSectionItemFactory) {
                    return true;
                }
            }
        }
        return false;
    }

    public interface PinnedSectionItemFactory {

    }
}
