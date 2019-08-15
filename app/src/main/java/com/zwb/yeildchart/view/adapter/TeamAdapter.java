package com.zwb.yeildchart.view.adapter;

import android.content.Context;

import com.zwb.yeildchart.R;
import com.zwb.yeildchart.base.adapter.BaseRVAdapter;
import com.zwb.yeildchart.model.Team;

import java.util.List;

/**
 * @author zwb
 * @date 2019/8/13 14:07
 */
public class TeamAdapter extends BaseRVAdapter<Team> {

    public TeamAdapter(Context context, List<Team> beans) {
        super(context, beans);
    }

    @Override
    public int getItemLayoutID(int viewType) {
        return R.layout.item_team_layout;
    }

    @Override
    protected void onBindDataToView(CommonViewHolder holder, Team bean, int position) {
        holder.setText(R.id.tv_team_name, bean.getTeamName());
    }
}
