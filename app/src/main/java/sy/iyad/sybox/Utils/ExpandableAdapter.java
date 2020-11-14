package sy.iyad.sybox.Utils;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import sy.iyad.sybox.R;


public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<ExpandedMenuModel> menuModels;
    private HashMap<ExpandedMenuModel,List<String>> listHashMap;
    private ExpandableListView listView;

    public ExpandableAdapter(@NonNull Context context,@NonNull List<ExpandedMenuModel> menuModels,@NonNull HashMap<ExpandedMenuModel,List<String>> listHashMap,@NonNull ExpandableListView listView){

        this.context = context;
        this.listHashMap = listHashMap;
        this.listView = listView;
        this.menuModels = menuModels;
    }
    @Override
    public int getGroupCount() {
        return menuModels.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int childCount = 0;
        if (groupPosition != 2) {
            childCount = listHashMap.get(menuModels.get(groupPosition))
                    .size();
        }
        return childCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return menuModels.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(menuModels.get(childPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ExpandedMenuModel expandedMenuModel = (ExpandedMenuModel) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listheader,parent,false);
        }
        TextView lblListHeader =  convertView
                .findViewById(R.id.submenu);
        ImageView headerIcon =  convertView.findViewById(R.id.iconimage);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(expandedMenuModel.getIconName());
        headerIcon.setImageResource(expandedMenuModel.getIconImg());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_submenu,parent, false);
        }

        TextView txtListChild =  convertView
                .findViewById(R.id.submenux);

        txtListChild.setText(childText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
