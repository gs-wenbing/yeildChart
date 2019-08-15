package com.zwb.yeildchart.view.home;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.zwb.filepicker.LFilePicker;
import com.zwb.yeildchart.DB.AppDatabase;
import com.zwb.yeildchart.R;
import com.zwb.yeildchart.base.BaseActivity;
import com.zwb.yeildchart.base.RecyclerFragment;
import com.zwb.yeildchart.base.adapter.BaseRVAdapter;
import com.zwb.yeildchart.model.Team;
import com.zwb.yeildchart.model.Team_Table;
import com.zwb.yeildchart.model.Yeild;
import com.zwb.yeildchart.model.Yeild_Table;
import com.zwb.yeildchart.view.adapter.TeamAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;

/**
 * @author zwb
 * @date 2019/8/9 12:34
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener, RecyclerFragment.RecyclerListener, BaseRVAdapter.OnItemClickLinsener, BaseRVAdapter.OnItemLongClickLinsener {
    private static final int REQUESTCODE_FROM_ACTIVITY = 1000;
    RecyclerFragment<Team> recyclerFragment;
    private BaseRVAdapter<Team> mAdapter;
    private String path = "";

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_home;
    }


    @Override
    protected void getIntentData() {
    }

    @Override
    protected void initPageView() {
        setTitleText("首页");
        setBackVisible(false);
        setRightVisible(true);
        mAdapter = new TeamAdapter(this, new ArrayList<>());
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        recyclerFragment = RecyclerFragment.newInstance();
        fragmentTransaction.add(R.id.framelayout, recyclerFragment).commit();
        recyclerFragment.init(mAdapter, this);

    }

    @Override
    protected void initListener() {
        llRight.setOnClickListener(this);
        mAdapter.setOnItemClickLinsener(this);
        mAdapter.setOnItemLongClickLinsener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_right) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            new LFilePicker()
                    .withActivity(this)
                    .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                    .withStartPath(sdcard)
                    .withIsGreater(true)
                    .withFileSize(1024)
                    .withChooseMode(true)
                    .withFileFilter(new String[]{".xls", ".xlsx"})
                    .withSortFileUp(true)
                    .start();
        }
    }

    @Override
    public void onRecyclerCreated(XRecyclerView recyclerView) {
        recyclerFragment.setLoadingEnable(false);
        recyclerFragment.setRefreshEnable(true);
    }

    @Override
    public void loadData(int action, int pageSize, int page) {
        List<Team> teams = SQLite.select().from(Team.class).where(Team_Table.taamPID.eq(-1)).queryList();
        recyclerFragment.loadCompleted(action, "", teams);
    }

    @Override
    public void onItemClick(BaseRVAdapter baseAdapter, int position) {
        Team team = mAdapter.getBeans().get(position - 1);
        Intent intent = new Intent(mContext, SecontActivity.class);
        intent.putExtra("data", team);
        mContext.startActivity(intent);

    }

    @Override
    public void onItemLongClick(BaseRVAdapter baseAdapter, int position) {
        Team team = mAdapter.getBeans().get(position - 1);
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("是否确定删除该条数据?")
                .setPositiveButton("确定", (dialog, which) -> {
                    Transaction transaction = FlowManager.getDatabase(AppDatabase.class).
                            beginTransactionAsync(databaseWrapper -> {
                                HomeActivity.this.deleteYeildData(team);
                                HomeActivity.this.deleteTeamData(team);
                                Toast.makeText(mContext, "删除成功，请下来刷新列表", Toast.LENGTH_SHORT).show();
                                Log.e(">>>>>>>>>>>", "删除成功");
                            }).success(transaction1 -> {
                                recyclerFragment.initData();
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                            }).build();
                    transaction.execute();
                })
                .setNegativeButton("取消", (dialog, which) -> {

                })
                .show();
    }

    public void deleteTeamData(Team parent) {
        //查询全部的数据库中记录
        List<Team> listTemp = SQLite.select().from(Team.class).where(Team_Table.taamPID.eq(parent.getTeamID())).queryList();
        for (int i = 0; i < listTemp.size(); i++) {
            Team team = listTemp.get(i);
            deleteTeamData(team);
        }
        //删除记录
        parent.delete();
    }

    private void deleteYeildData(Team parent) {
        if (parent.isLast()) {
            //删除报表数据
            SQLite.delete(Yeild.class)
                    .where(Yeild_Table.teamID.eq(parent.getTeamID()))
                    .execute();
        } else {
            List<Team> teams = SQLite.select().from(Team.class).where(Team_Table.taamPID.eq(parent.getTeamID())).queryList();
            for (Team team : teams) {
                deleteYeildData(team);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
                List<String> list = data.getStringArrayListExtra("paths");
//                String path = data.getStringExtra("path");
                Log.e("::::::::::::::", list.get(0));
                path = list.get(0);
                new TestAsyncTask(HomeActivity.this).execute(path);
            }
        }
    }

    class TestAsyncTask extends AsyncTask<String, Integer, List<File>> {

        private Context mContext;

        TestAsyncTask(Context context) {
            this.mContext = context;
        }

        private ProgressDialog dialog;

        //2、运行完onPreExecute后执行，长时间的操作放在此方法内，
        //此方法内不能操作UI
        //返回结果值
        @Override
        protected List<File> doInBackground(String... strings) {
            readExcel(strings[0]);
            return null;
        }

        //1、开始执行时运行
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(mContext, "", "文件解析中，请不要退出...");
            super.onPreExecute();
        }

        //3、执行完毕
        @Override
        protected void onPostExecute(List<File> list) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            super.onPostExecute(list);

            recyclerFragment.initData();
        }

        //执行过程进行进度更新等操作
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        //调用取消时，要做的操作
        @Override
        protected void onCancelled(List<File> list) {
            super.onCancelled(list);
        }

        //调用取消时，要做的操作
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    public void readExcel(String filePath) {
        File file = new File(filePath);
        Log.e("yy", "file=" + file.getAbsolutePath());
        try {
            InputStream is = new FileInputStream(file);
            Workbook book = Workbook.getWorkbook(is);
            getSheet(book);
            book.close();
        } catch (Exception e) {
            Log.e("yy", "e" + e);
        }
    }

    private void getSheet(Workbook book) {
        String[] sheetNames = book.getSheetNames();
        List<Team> teams = new ArrayList<>();
        //解析所有的sheet
        for (int i = 0; i < sheetNames.length; ++i) {
            Team team = new Team();
            team.setTeamID(i + 1);
            team.setTeamName(sheetNames[i]);
            team.setTaamPID(-1);
            team.setColumn(-1);
            team.setRow(-1);
            team.setSheetIndex(i);
            team.setXlsPath(path);
            team.setChildList(new ArrayList<>());
            team.setYeildList(new ArrayList<>());
            teams.add(team);
        }
        insertTeamModle1(teams);
    }

    private void insertTeamModle1(List<Team> teams) {
        Transaction transaction = FlowManager.getDatabase(AppDatabase.class).
                beginTransactionAsync(databaseWrapper -> {
                    for (Team info : teams) {
                        Team tempTeam = SQLite.select().from(Team.class)
                                .where(Team_Table.teamName.eq(info.getTeamName())).querySingle();
                        if (tempTeam == null) {
                            info.save();
                        }
                    }
                }).build();
        transaction.execute();
    }
}
