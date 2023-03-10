package com.github.tvbox.osc.base;

import android.app.Activity;
import androidx.multidex.MultiDexApplication;

import com.github.tvbox.osc.bean.VodInfo;
import com.github.tvbox.osc.callback.EmptyCallback;
import com.github.tvbox.osc.callback.LoadingCallback;
import com.github.tvbox.osc.data.AppDataManager;
import com.github.tvbox.osc.server.ControlManager;
import com.github.tvbox.osc.util.AppManager;
import com.github.tvbox.osc.util.EpgUtil;
import com.github.tvbox.osc.util.FileUtils;
import com.github.tvbox.osc.util.HawkConfig;
import com.github.tvbox.osc.util.OkGoHelper;
import com.github.tvbox.osc.util.PlayerHelper;
import com.github.tvbox.osc.util.js.JSEngine;
import com.kingja.loadsir.core.LoadSir;
import com.orhanobut.hawk.Hawk;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;
import com.p2p.P2PClass;
import com.github.tvbox.osc.util.js.jianpian;
/**
 * @author pj567
 * @date :2020/12/17
 * @description:
 */
public class App extends MultiDexApplication {
        public static String burl;
    private static App instance;
    private static P2PClass p;
    private Activity homeActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initParams();
        // OKGo
        OkGoHelper.init(); //台标获取
        EpgUtil.init();
        // 初始化Web服务器
        ControlManager.init(this);
        //初始化数据库
        AppDataManager.init();
        LoadSir.beginBuilder()
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .commit();
        AutoSizeConfig.getInstance().setCustomFragment(true).getUnitsManager()
                .setSupportDP(false)
                .setSupportSP(false)
                .setSupportSubunits(Subunits.MM);
        PlayerHelper.init();
        JSEngine.getInstance().create();
        FileUtils.cleanPlayerCache();
    }

    private void initParams() {
        // Hawk
        Hawk.init(this).build();
        Hawk.put(HawkConfig.DEBUG_OPEN, false);
        if (!Hawk.contains(HawkConfig.PLAY_TYPE)) {
            Hawk.put(HawkConfig.PLAY_TYPE, 1);// Player   0=系统, 1=IJK, 2=Exo
            Hawk.put(HawkConfig.IJK_CODEC, "硬解码");// IJK Render 软解码, 硬解码
            Hawk.put(HawkConfig.HOME_REC, 1);       // Home Rec 0=豆瓣, 1=推荐, 2=历史
            Hawk.put(HawkConfig.HISTORY_NUM, 2);       // History Number
            Hawk.put(HawkConfig.DOH_URL, 0);        // DNS
            Hawk.put(HawkConfig.PLAY_RENDER, 1);        // 渲染
            Hawk.put(HawkConfig.IJK_CACHE_PLAY, false);        //true=开, false=关  ijk缓存            
          Hawk.put(HawkConfig.HOME_SEARCH_POSITION, false);     // 搜索
          Hawk.put(HawkConfig.HOME_MENU_POSITION, true);        // 设置
          Hawk.put(HawkConfig.HOME_HIST_POSITION, false);     // 历史
          Hawk.put(HawkConfig.HOME_PUSH_POSITION, false);        // 推送          
          Hawk.put(HawkConfig.HOME_APP_POSITION, true);     // 应用
          Hawk.put(HawkConfig.HOME_FAV_POSITION, false);        // 收藏    
        // Hawk.put(HawkConfig.PLAY_SCALE, 1);       //播放比例
          //Hawk.put(HawkConfig.PLAY_TIME_STEP, 1);      
        // Hawk.put(HawkConfig.SEARCH_VIEW, 1);    // Text or Picture   
            
            
        }
   
 }
    
     public static P2PClass getp2p() {
        try {
            if (p == null) {
                p = new P2PClass(instance.getExternalCacheDir().getAbsolutePath());
            }
            return p;
        } catch (Exception unused) {
            return null;
        }
    }
    
    public static App getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        JSEngine.getInstance().destroy();
    }


    private VodInfo vodInfo;
    public void setVodInfo(VodInfo vodinfo){
        this.vodInfo = vodinfo;
    }
    public VodInfo getVodInfo(){
        return this.vodInfo;
    }

    public Activity getCurrentActivity() {
        return AppManager.getInstance().currentActivity();
    }
}


