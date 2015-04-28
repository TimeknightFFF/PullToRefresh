package com.sun.androi.pulltorefresh;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private PullToRefreshListView viewById;
    private  ArrayAdapter<String> adapter;
    private int pageIndex=0;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    List<String> list=new ArrayList<>();
                    for(int i=0;i<40;i++){
                        list.add(String.format("new 第%03d条数据",i));
                    }
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                    //调用刷新完成方法，网络下载的情况下，任何情况下都要调用
                    viewById.onRefreshComplete();
                    break;
                case 1:
                    List<String> list1=new ArrayList<>();
                    for(int i=0;i<40;i++){
                        list1.add(String.format("new 第%03d条数据",i+pageIndex*40));
                    }
                    adapter.addAll(list1);
                    adapter.notifyDataSetChanged();
                    viewById.onRefreshComplete();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewById = (PullToRefreshListView) findViewById(R.id.list);
        List<String> list=new ArrayList<>();
        for(int i=0;i<40;i++){
            list.add(String.format("第%03d条数据",i));
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        viewById.setAdapter(adapter);

        viewById.setMode(PullToRefreshBase.Mode.BOTH);

        //一端拉动模式下，用OnRefreshListener
//        viewById.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//
//            }
//        });
        //两端下，用OnRefreshListener2  http://203.208.46.146/
        viewById.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Toast.makeText(MainActivity.this,"onPullDownToRefresh",Toast.LENGTH_SHORT).show();
                adapter.clear();
                handler.sendEmptyMessageDelayed(0,2000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Toast.makeText(MainActivity.this,"onPullUpToRefresh",Toast.LENGTH_SHORT).show();
                pageIndex++;
                handler.sendEmptyMessageDelayed(1,2000);
            }
        });
    }

}
