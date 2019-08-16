package com.zwb.yeildchart.view.home;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.zwb.yeildchart.DB.AppDatabase;
import com.zwb.yeildchart.R;
import com.zwb.yeildchart.base.BaseActivity;
import com.zwb.yeildchart.base.RecyclerFragment;
import com.zwb.yeildchart.base.adapter.BaseRVAdapter;
import com.zwb.yeildchart.model.Team;
import com.zwb.yeildchart.model.Team_Table;
import com.zwb.yeildchart.model.Yeild;
import com.zwb.yeildchart.view.adapter.TeamAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * @author zwb
 * @date 2019/8/9 12:34
 */
public class SecontActivity extends BaseActivity implements RecyclerFragment.RecyclerListener, BaseRVAdapter.OnItemClickLinsener {
    RecyclerFragment<Team> recyclerFragment;
    private BaseRVAdapter<Team> mAdapter;
    //二级以下用到
    private Team intentTeam;

    @Override
    protected int initPageLayoutID() {
        return R.layout.activity_home;
    }


    @Override
    protected void getIntentData() {
        intentTeam = getIntent().getParcelableExtra("data");

        Log.e("yy", "parent====" + intentTeam.toString());
    }

    @Override
    protected void initPageView() {
        setTitleText(intentTeam.getTeamName());
        setBackVisible(true);
        setRightVisible(false);
        long count = SQLite.select().from(Team.class).where(Team_Table.taamPID.eq(intentTeam.getTeamID())).count();
        if(count == 0){
            new TestAsyncTask(SecontActivity.this).execute(intentTeam.getXlsPath(), intentTeam.getTeamID() + "");
        }

        mAdapter = new TeamAdapter(this, new ArrayList<>());
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        recyclerFragment = RecyclerFragment.newInstance();
        fragmentTransaction.add(R.id.framelayout, recyclerFragment).commit();
        recyclerFragment.init(mAdapter, this);

    }

    @Override
    protected void initListener() {
        mAdapter.setOnItemClickLinsener(this);
    }


    @Override
    public void onRecyclerCreated(XRecyclerView recyclerView) {
        recyclerFragment.setLoadingEnable(false);
        recyclerFragment.setRefreshEnable(false);
    }

    @Override
    public void loadData(int action, int pageSize, int page) {
        List<Team> teams = SQLite.select().from(Team.class).where(Team_Table.taamPID.eq(intentTeam.getTeamID())).queryList();
        recyclerFragment.loadCompleted(action, "", teams);
    }

    @Override
    public void onItemClick(BaseRVAdapter baseAdapter, int position) {
        Team team = mAdapter.getBeans().get(position - 1);
        Intent intent;
        if(team.isLast()){
            intent = new Intent(mContext, ChartActivity.class);
        }else{
            intent = new Intent(mContext,SecontActivity.class);
        }
        intent.putExtra("data", team);
        mContext.startActivity(intent);
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
        InputStream is = null;
        Workbook book = null;
        try {
            is = new FileInputStream(file);
            book = Workbook.getWorkbook(is);
            getXlsSheet(book);
            book.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    boolean isTitleRowFinish = false;
    int mCurrRow = 0;
    ArrayList<Team> mTeamlist = new ArrayList<>();

    private void getXlsSheet(Workbook book) {
        Sheet sheet = book.getSheet(intentTeam.getSheetIndex());
        mTeamlist = intentTeam.getChildList();
        //解析标题
        while (!isTitleRowFinish){
            analysisTitle(sheet,mTeamlist);
        }
        analysisDate(sheet,intentTeam.getChildList());
        Log.e("yy",intentTeam.toString());
        //插入数据
        insertTransaction(intentTeam.getChildList());
    }
    //从第1列开始，第0行是日期
    int mCurrColumn = 1;
    private void analysisDate(Sheet sheet, ArrayList<Team> pTeamlist){

        for (int i=0;i<pTeamlist.size();i++){
            Team team =  pTeamlist.get(i);
            if(team.getChildList().isEmpty()){
                team.setLast(true);
                int rows = sheet.getRows();
                for (int k=mCurrRow;k<rows;k++){
                    String time = (sheet.getCell(0, k)).getContents();
                    String column;
                    try {
                        column = (sheet.getCell(mCurrColumn, k)).getContents();
                    }catch (Exception e){
                        continue;
                    }
                    Yeild yeild;
                    try {
                        yeild = new Yeild(Float.parseFloat(column),  team.getTeamID(), changeNumToDate(time));
                    }catch (Exception e){
                        yeild = new Yeild(0,  team.getTeamID(), changeNumToDate(time));
                    }
                    team.getYeildList().add(yeild);
                }
                mCurrColumn++;
            }else{
                analysisDate(sheet,team.getChildList());
            }
        }
    }
    private void analysisTitle(Sheet sheet, ArrayList<Team> pTeamlist){
        //获取sheet的mCurrRow行的所有列
        Cell[] cells = sheet.getRow(mCurrRow);
        //第mCurrRow行的第一列 是不是日期
        String firstColumn =  cells[0].getContents();
        if (!TextUtils.isEmpty(firstColumn) && !isTitleRow(firstColumn)) {
            isTitleRowFinish = true;
            return;
        }
        if(pTeamlist.isEmpty()){
            loopColumn(sheet,0,cells.length,intentTeam);
        }else{
            for (int i = 0; i < pTeamlist.size(); ++i) {
                Team cTeam = pTeamlist.get(i);
                Cell[] ccellsxxx = sheet.getRow(cTeam.getRow() + 1);
                loopColumn(sheet,cTeam.getColumn(),ccellsxxx.length,cTeam);
            }
        }
        if(!isTitleRowFinish){
            mCurrRow++;
        }
    }
    private void loopColumn(Sheet sheet,int startIndex,int columnLength,Team pTeam){
        Range[] rangeCell = sheet.getMergedCells();
        for (int i = startIndex; i < columnLength; ++i) {
            String column1 = (sheet.getCell(i, mCurrRow)).getContents();
            if (!TextUtils.isEmpty(column1) &&!column1.equals("时间") && !column1.equals("日期")) {
                if(pTeam.isMergedCell()){
                    if (i >= pTeam.getTopLeftColumn() && i <= pTeam.getBottomRightColumn()) {
                        parseTeam(sheet,rangeCell, column1, pTeam, i);
                    }
                }else{
                      parseTeam(sheet,rangeCell, column1, pTeam, i);
                }
            }
        }
        mTeamlist = pTeam.getChildList();
    }

    private void parseTeam(Sheet sheet,Range[] rangeCell,String column1,Team pTeam,int i){
        Team team = new Team();
        team.setTeamID(Integer.parseInt(pTeam.getTeamID()+""+(i+1)));
        team.setTeamName(column1);
        team.setColumn(i);
        team.setRow(mCurrRow);
        team.setTaamPID(pTeam.getTeamID());
        team.setChildList(new ArrayList<>());
        team.setYeildList(new ArrayList<>());
        for (Range r : rangeCell) {
            Log.e("yy",r.getTopLeft().getRow()+"=="+r.getBottomRight().getRow()+
                    "::::"+r.getTopLeft().getColumn()+"=="+ r.getBottomRight().getColumn());
            if (mCurrRow >= r.getTopLeft().getRow() && mCurrRow <= r.getBottomRight().getRow()
                    && i >= r.getTopLeft().getColumn() && i <= r.getBottomRight().getColumn()) {
                column1 = sheet.getCell(r.getTopLeft().getColumn(), r.getTopLeft().getRow()).getContents();
                team.setTeamName(column1);
                team.setMergedCell(true);
                team.setTopLeftRow(r.getTopLeft().getRow());
                team.setBottomRightRow(r.getBottomRight().getRow());
                team.setTopLeftColumn(r.getTopLeft().getColumn());
                team.setBottomRightColumn(r.getBottomRight().getColumn());
            }
        }
        pTeam.getChildList().add(team);
    }


    public boolean isTitleRow(String column) {
        return changeNumToDate(column).contains("1900");
    }

    public String changeNumToDate(String s) {
        if(TextUtils.isEmpty(s)){
            return "";
        }
        String rtn = "1900-01-01";
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date1 = new java.util.Date();
            date1 = format.parse("1900-01-01");
            long i1 = date1.getTime();
            //这里要减去2，(Long.parseLong(s)-2) 不然日期会提前2天，具体原因不清楚，
            //估计和java计时是从1970-01-01开始有关
            //而excel里面的计算是从1900-01-01开始
            i1 = i1 / 1000 + ((Long.parseLong(s) - 2) * 24 * 3600);
            date1.setTime(i1 * 1000);
            rtn = format.format(date1);
//            System.out.println("rtn=" + rtn);
        } catch (Exception e) {
            rtn = "1900-01-01";
        }
        return rtn;
    }

    private void insertTransaction(List<Team> teams) {
        Transaction transaction = FlowManager.getDatabase(AppDatabase.class).
                beginTransactionAsync(databaseWrapper -> {
                    //处理list保存：批量添加
                    insertTeam(teams);
                }).build();
        transaction.execute();
    }

    private void insertTeam(List<Team> teams){
        for (Team info : teams) {
            info.save();
            if(info.getChildList().isEmpty()){
                insertYeild(info.getYeildList());
            }else{
                insertTeam(info.getChildList());
            }
        }
    }

    private void insertYeild(List<Yeild> yeilds){
        for (Yeild info : yeilds) {
            info.save();
        }
    }
}
