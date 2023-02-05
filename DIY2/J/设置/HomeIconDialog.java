package com.github.tvbox.osc.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.tvbox.osc.R;
import com.github.tvbox.osc.util.FastClickCheckUtil;
import com.github.tvbox.osc.util.HawkConfig;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

/**
 * 描述
 *
 * @author pj567
 * @since 2020/12/27
 */
public class HomeIconDialog extends BaseDialog {
    private final TextView tvHomeSearch;
    private final TextView tvHomeMenu;
    private final TextView tvHomePush;
    private final TextView tvHomeDrive;
    private final TextView tvHomeHistory;
    private final TextView tvHomeFavorite;

    public HomeIconDialog(@NonNull @NotNull Context context) {
        super(context);
        setContentView(R.layout.dialog_homeoption);
        setCanceledOnTouchOutside(true);
        //搜索 设置
        tvHomeSearch = findViewById(R.id.tvHomeSearch);
        tvHomeSearch.setText(Hawk.get(HawkConfig.HOME_SEARCH_POSITION, true) ? "上方" : "下方");
        tvHomeMenu = findViewById(R.id.tvHomeMenu);
        tvHomeMenu.setText(Hawk.get(HawkConfig.HOME_MENU_POSITION, true) ? "上方" : "下方");
         //推送 应用     
        tvHomePush = findViewById(R.id.tvHomePush);
        tvHomePush.setText(Hawk.get(HawkConfig.HOME_PUSH_POSITION, true) ? "上方" : "下方");
        tvHomeDrive = findViewById(R.id.tvHomeDrive);
        tvHomeDrive.setText(Hawk.get(HawkConfig.HOME_APP_POSITION, true) ? "上方" : "下方");
        //历史 收藏
        tvHomeHistory = findViewById(R.id.tvHomeHistory);
        tvHomeHistory.setText(Hawk.get(HawkConfig.HOME_HIST_POSITION, true) ? "上方" : "下方");
        tvHomeFavorite = findViewById(R.id.tvHomeFavorite);
        tvHomeFavorite.setText(Hawk.get(HawkConfig.HOME_FAV_POSITION, true) ? "上方" : "下方");
        
        //搜索
        findViewById(R.id.llSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastClickCheckUtil.check(v);
                Hawk.put(HawkConfig.HOME_SEARCH_POSITION, !Hawk.get(HawkConfig.HOME_SEARCH_POSITION, true));
                tvHomeSearch.setText(Hawk.get(HawkConfig.HOME_SEARCH_POSITION, true) ? "上方" : "下方");
            }
        });
        findViewById(R.id.llMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastClickCheckUtil.check(v);
                Hawk.put(HawkConfig.HOME_MENU_POSITION, !Hawk.get(HawkConfig.HOME_MENU_POSITION, true));
                tvHomeMenu.setText(Hawk.get(HawkConfig.HOME_MENU_POSITION, true) ? "上方" : "下方");
            }
        });
        
        
        //推送
           findViewById(R.id.llPush).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastClickCheckUtil.check(v);
                Hawk.put(HawkConfig.HOME_PUSH_POSITION, !Hawk.get(HawkConfig.HOME_PUSH_POSITION, true));
                tvHomePush.setText(Hawk.get(HawkConfig.HOME_PUSH_POSITION, true) ? "上方" : "下方");
            }
        });
        //应用
            findViewById(R.id.llDrive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastClickCheckUtil.check(v);
                Hawk.put(HawkConfig.HOME_APP_POSITION, !Hawk.get(HawkConfig.HOME_APP_POSITION, true));
                tvHomeDrive.setText(Hawk.get(HawkConfig.HOME_APP_POSITION, true) ? "上方" : "下方");
            }
        });
        //收藏
          findViewById(R.id.llFav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastClickCheckUtil.check(v);
                Hawk.put(HawkConfig.HOME_FAV_POSITION, !Hawk.get(HawkConfig.HOME_FAV_POSITION, true));
                tvHomeFavorite.setText(Hawk.get(HawkConfig.HOME_FAV_POSITION, true) ? "上方" : "下方");
            }
        });
        //历史
            findViewById(R.id.llHist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastClickCheckUtil.check(v);
                Hawk.put(HawkConfig.HOME_HIST_POSITION, !Hawk.get(HawkConfig.HOME_HIST_POSITION, true));
                tvHomeHistory.setText(Hawk.get(HawkConfig.HOME_HIST_POSITION, true) ? "上方" : "下方");
            }
        });
    }

}

