package com.forward.movablelistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.forward.movablelistview.listener.OnMenuClickListener;
import com.forward.movablelistview.view.MenuListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements OnMenuClickListener, View.OnTouchListener, View.OnHoverListener {

    private MenuListView mMenuList;
    private List<String> mData;
    private FrameLayout mFrameLayout;
    private String[] mFiles;
    private String[] mEdits;
    private String[] mLooks;
    private View mLastView;
    private TextView mFileView;
    private TextView mEditView;
    private TextView mLookView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        mFileView = (TextView) findViewById(R.id.file);
        mEditView = (TextView) findViewById(R.id.edit);
        mLookView = (TextView) findViewById(R.id.look);
        mMenuList = (MenuListView) findViewById(R.id.menu_list);
        mFrameLayout = (FrameLayout) findViewById(R.id.main);
    }

    private void initData() {
        mFiles = new String[]{"新建", "打开", "保存", "全部保存", "最近打开"};
        mEdits = new String[]{"剪切", "复制", "粘贴", "全选", "撤销"};
        mLooks = new String[]{"查找/替换...", "跳到开始", "跳到行", "上个位置", "下个位置"};

        mMenuList.setParentView(mFrameLayout);
        mData = new ArrayList<>();

    }

    private void initListener() {
        mMenuList.setOnMenuClickListener(this);
        mFileView.setOnTouchListener(this);
        mEditView.setOnTouchListener(this);
        mLookView.setOnTouchListener(this);

        mFileView.setOnHoverListener(this);
        mEditView.setOnHoverListener(this);
        mLookView.setOnHoverListener(this);
    }

    public List<String> getData(View view) {
        mData.clear();
        switch (view.getId()) {
            case R.id.file:
                mData.addAll(Arrays.asList(mFiles));
                break;
            case R.id.edit:
                mData.addAll(Arrays.asList(mEdits));
                break;
            case R.id.look:
                mData.addAll(Arrays.asList(mLooks));
                break;
        }
        return mData;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mMenuList.setCanCancel(true);
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            mMenuList.dismiss();
            setUnselect();
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setUnselect() {
        if (!mMenuList.isVisibility() && mLastView != null) {
            mLastView.setSelected(false);
            mLastView = null;
        }
    }

    @Override
    public void onMenuItemClick(View view, String menuContent) {
        Toast.makeText(this, "点击了 " + menuContent, Toast.LENGTH_SHORT).show();
        mMenuList.setCanCancel(true);
        mMenuList.dismiss();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMenuList.show(v, getData(v));
                mMenuList.setCanCancel(false);
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }

    @Override
    public boolean onHover(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_HOVER_ENTER:
                if (mLastView != null && mLastView != v) {
                    mLastView.setSelected(false);
                }
                if (mMenuList.isVisibility()) {
                    v.setSelected(true);
                    mMenuList.show(v, getData(v));
                    mMenuList.setCanCancel(false);
                }
                mLastView = v;
                break;
            case MotionEvent.ACTION_HOVER_EXIT:
                break;
        }
        return false;
    }
}
