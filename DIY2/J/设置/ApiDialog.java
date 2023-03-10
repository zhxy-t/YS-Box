package com.github.tvbox.osc.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.tvbox.osc.R;

import com.github.tvbox.osc.event.RefreshEvent;
import com.github.tvbox.osc.server.ControlManager;
import com.github.tvbox.osc.ui.adapter.ApiHistoryDialogAdapter;
import com.github.tvbox.osc.ui.tv.QRCodeGen;
import com.github.tvbox.osc.util.HawkConfig;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.utils.AutoSizeUtils;
import com.github.tvbox.osc.ui.activity.HomeActivity;


import com.github.tvbox.osc.bean.ApiModel;
import com.github.tvbox.osc.util.SourceUtil;

/**
 * 描述
 *
 * @author pj567
 * @since 2020/12/27
 */
public class ApiDialog extends BaseDialog {
    private final ImageView ivQRCode;
    private final TextView tvAddress;
    
   
//taka epg 直播地址
   private final EditText inputApi;  
   private final EditText inputLive;

   private final EditText inputEPG;
   private final EditText inputApiName; 

   
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshEvent event) {
        if (event.type == RefreshEvent.TYPE_API_URL_CHANGE) {
            inputApi.setText((String) event.obj);
            
            inputApiName.setText((String) event.obj);
            ApiModel apiModel = (ApiModel) event.obj;
            inputApiName.setText(apiModel.getName());
            inputApi.setText(apiModel.getUrl());
 
        }
          if (event.type == RefreshEvent.TYPE_LIVE_URL_CHANGE) {
            inputLive.setText((String) event.obj);
        }
        if (event.type == RefreshEvent.TYPE_EPG_URL_CHANGE) {
            inputEPG.setText((String) event.obj);
        }
    }

    public ApiDialog(@NonNull @NotNull Context context) {
        
        
        super(context);
        setContentView(R.layout.dialog_api);
        setCanceledOnTouchOutside(true);
        ivQRCode = (ImageView) findViewById(R.id.ivQRCode);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        EditText editText = (EditText) findViewById(R.id.input);
        
        inputApi = editText;
        String str = "";
        editText = (EditText) findViewById(R.id.input);
        editText.setText((CharSequence) Hawk.get("api_url", str));
        
        editText = (EditText) findViewById(R.id.inputApiName);
        inputApiName = editText;
        editText.setText((CharSequence) Hawk.get("api_name", str));
        
        editText = (EditText) findViewById(R.id.input_live);
        inputLive = editText;
        editText.setText((CharSequence) Hawk.get("live_url", str));
        
        editText = (EditText) findViewById(R.id.input_epg);
        inputEPG = editText;
        editText.setText((CharSequence) Hawk.get("epg_url", str));
        
        
        
        /*
        super(context);
        setContentView(R.layout.dialog_api);
        setCanceledOnTouchOutside(true);
        ivQRCode = findViewById(R.id.ivQRCode);

        tvAddress = findViewById(R.id.tvAddress);       
        //内置网络接口在此处添加          

        tvAddress = findViewById(R.id.tvAddress);
       
        
        
        //内置网络接口在此处添加   
        inputApiName = findViewById(R.id.inputApiName);
        inputApiName.setText(Hawk.get(HawkConfig.API_NAME, ""));

        inputApi = findViewById(R.id.input);
        inputApi.setText(Hawk.get(HawkConfig.API_URL, ""));
        
        inputApiName = findViewById(R.id.inputApiName);
        inputApiName.setText(Hawk.get(HawkConfig.API_NAME, ""));
        // takagen99: Add Live & EPG Address
        inputLive = findViewById(R.id.input_live);
        inputLive.setText(Hawk.get(HawkConfig.LIVE_URL, ""));
        inputEPG = findViewById(R.id.input_epg);
        inputEPG.setText(Hawk.get(HawkConfig.EPG_URL, ""));
        
       */ 
        findViewById(R.id.inputSubmit).setOnClickListener(new View.OnClickListener() {
            
         

    public void onClick(View view) {
        ArrayList arrayList;
                String trim = inputApi.getText().toString().trim();
                String trim2 = inputApiName.getText().toString().trim();
                String trim3 = inputLive.getText().toString().trim();
                String trim4 = inputEPG.getText().toString().trim();
        
       // String trim = ApiDialog.access$000(this.this$0).getText().toString().trim();
       // String trim2 = ApiDialog.access$100(this.this$0).getText().toString().trim();
       // String trim3 = ApiDialog.access$200(this.this$0).getText().toString().trim();
       // String trim4 = ApiDialog.access$300(this.this$0).getText().toString().trim();
        String str = "file://";
        String str2 = "clan://localhost/";
        if (trim.startsWith(str)) {
            trim = trim.replace(str, str2);
        } else {
            str = "./";
            if (trim.startsWith(str)) {
                trim = trim.replace(str, str2);
            }
        }
        if (!trim.isEmpty()) {
            if (trim2.isEmpty()) {
                trim2 = trim;
            }
            ApiModel apiModel = new ApiModel();
            apiModel.setUrl(trim);
            apiModel.setName(trim2);
           // SourceUtil.setCurrentApi(apiModel);
            //SourceUtil.addHistory(apiModel);
            
              ArrayList<String> history = Hawk.get(HawkConfig.API_HISTORY, new ArrayList<String>());
                    if (!history.contains(trim))
                        history.add(0, trim);
                    if (history.size() > 30)
                        history.remove(30);
                    Hawk.put(HawkConfig.API_HISTORY, history);
            
            listener.onchange(trim);
            dismiss();
        }
        Hawk.put("live_url", trim3);
        if (!trim3.isEmpty()) {
            String str3 = "live_history";
            arrayList = (ArrayList) Hawk.get(str3, new ArrayList());
            if (!arrayList.contains(trim3)) {
                arrayList.add(0, trim3);
            }
            if (arrayList.size() > 20) {
                arrayList.remove(20);
            }
            Hawk.put(str3, arrayList);
        }
        Hawk.put("epg_url", trim4);
        if (!trim4.isEmpty()) {
            trim3 = "epg_history";
            arrayList = (ArrayList) Hawk.get(trim3, new ArrayList());
            if (!arrayList.contains(trim4)) {
                arrayList.add(0, trim4);
            }
            if (arrayList.size() > 20) {
                arrayList.remove(20);
            }
            Hawk.put(trim3, arrayList);
        }
    }

   });          
            
           /* 
            
            @Override
            public void onClick(View view) {
                ArrayList arrayList;
                String newApi = inputApi.getText().toString().trim();
                String newApiName = inputApiName.getText().toString().trim();
                String newLive = inputLive.getText().toString().trim();
                String newEPG = inputEPG.getText().toString().trim();

                if (!newApi.isEmpty()) {
                    if (!newApiName.isEmpty()) {
               newApiName = newApi;
            }
  
            ApiModel apiModel = new ApiModel();
             apiModel.setUrl(newApi);
            apiModel.setName(newApiName);
            SourceUtil.setCurrentApi(apiModel);
            SourceUtil.addHistory(apiModel);

                
                    ArrayList<String> history = Hawk.get(HawkConfig.API_HISTORY, new ArrayList<String>());
                    if (!history.contains(newApi))
                        history.add(0, newApi);
                    if (history.size() > 30)
                        history.remove(30);
                    Hawk.put(HawkConfig.API_HISTORY, history);
                    listener.onchange(newApi);
                    dismiss();
                }
  // Capture Live input into Settings & Live History (max 20)
                Hawk.put(HawkConfig.LIVE_URL, newLive);
                if (!newLive.isEmpty()) {
                    ArrayList<String> liveHistory = Hawk.get(HawkConfig.LIVE_HISTORY, new ArrayList<String>());
                    if (!liveHistory.contains(newLive))
                        liveHistory.add(0, newLive);
                    if (liveHistory.size() > 20)
                        liveHistory.remove(20);
                    Hawk.put(HawkConfig.LIVE_HISTORY, liveHistory);
                }
                // Capture EPG input into Settings
                Hawk.put(HawkConfig.EPG_URL, newEPG);
                if (!newEPG.isEmpty()) {
                    ArrayList<String> EPGHistory = Hawk.get(HawkConfig.EPG_HISTORY, new ArrayList<String>());
                    if (!EPGHistory.contains(newEPG))
                        EPGHistory.add(0, newEPG);
                    if (EPGHistory.size() > 20)
                        EPGHistory.remove(20);
                    Hawk.put(HawkConfig.EPG_HISTORY, EPGHistory);
                }                             
            }
        });
        
        */
        
        
        findViewById(R.id.apiHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> history = Hawk.get(HawkConfig.API_HISTORY, new ArrayList<String>());
                if (history.isEmpty())
                    return;
                String current = Hawk.get(HawkConfig.API_URL, "");
                int idx = 0;
                if (history.contains(current))
                    idx = history.indexOf(current);
                ApiHistoryDialog dialog = new ApiHistoryDialog(getContext());
              dialog.setTip("历史配置列表");
                dialog.setAdapter(new ApiHistoryDialogAdapter.SelectDialogInterface() {
                    @Override
                    public void click(String value) {
                        inputApi.setText(value);
                        listener.onchange(value);
                        dialog.dismiss();
                    }

                    @Override
                    public void del(String value, ArrayList<String> data) {
                        Hawk.put(HawkConfig.API_HISTORY, data);
                    }
                }, history, idx);
                dialog.show();
            }
        });
        //直播历史列表
        
               findViewById(R.id.liveHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> liveHistory = Hawk.get(HawkConfig.LIVE_HISTORY, new ArrayList<String>());
                if (liveHistory.isEmpty())
                    return;
                String current = Hawk.get(HawkConfig.LIVE_URL, "");
                int idx = 0;
                if (liveHistory.contains(current))
                    idx = liveHistory.indexOf(current);
                ApiHistoryDialog dialog = new ApiHistoryDialog(getContext());
                //dialog.setTip(HomeActivity.getRes().getString(R.string.dia_history_live));
               dialog.setTip("历史直播列表");
                dialog.setAdapter(new ApiHistoryDialogAdapter.SelectDialogInterface() {
                    @Override
                    public void click(String liveURL) {
                        inputLive.setText(liveURL);
                        Hawk.put(HawkConfig.LIVE_URL, liveURL);
                        dialog.dismiss();
                    }

                    @Override
                    public void del(String value, ArrayList<String> data) {
                        Hawk.put(HawkConfig.LIVE_HISTORY, data);
                    }
                }, liveHistory, idx);
                dialog.show();
            }
        });
        
          findViewById(R.id.EPGHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> EPGHistory = Hawk.get(HawkConfig.EPG_HISTORY, new ArrayList<String>());
                if (EPGHistory.isEmpty())
                    return;
                String current = Hawk.get(HawkConfig.EPG_URL, "");
                int idx = 0;
                if (EPGHistory.contains(current))
                    idx = EPGHistory.indexOf(current);
                ApiHistoryDialog dialog = new ApiHistoryDialog(getContext());
                //dialog.setTip(HomeActivity.getRes().getString(R.string.dia_history_epg));
                  dialog.setTip("历史EPG列表");
                dialog.setAdapter(new ApiHistoryDialogAdapter.SelectDialogInterface() {
                    @Override
                    public void click(String epgURL) {
                        inputEPG.setText(epgURL);
                        Hawk.put(HawkConfig.EPG_URL, epgURL);
                        dialog.dismiss();
                    }

                    @Override
                    public void del(String value, ArrayList<String> data) {
                        Hawk.put(HawkConfig.EPG_HISTORY, data);
                    }
                }, EPGHistory, idx);
                dialog.show();
            }
        });
         
        
        
        findViewById(R.id.storagePermission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (XXPermissions.isGranted(getContext(), Permission.Group.STORAGE)) {
                    Toast.makeText(getContext(), "已获得存储权限", Toast.LENGTH_SHORT).show();
                } else {
                    XXPermissions.with(getContext())
                            .permission(Permission.Group.STORAGE)
                            .request(new OnPermissionCallback() {
                                @Override
                                public void onGranted(List<String> permissions, boolean all) {
                                    if (all) {
                                        Toast.makeText(getContext(), "已获得存储权限", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onDenied(List<String> permissions, boolean never) {
                                    if (never) {
                                        Toast.makeText(getContext(), "获取存储权限失败,请在系统设置中开启", Toast.LENGTH_SHORT).show();
                                        XXPermissions.startPermissionActivity((Activity) getContext(), permissions);
                                    } else {
                                        Toast.makeText(getContext(), "获取存储权限失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        refreshQRCode();
    }

    private void refreshQRCode() {
        String address = ControlManager.get().getAddress(false);
        tvAddress.setText(String.format("手机/电脑扫描上方二维码或者直接浏览器访问地址\n%s", address));
        ivQRCode.setImageBitmap(QRCodeGen.generateBitmap(address, AutoSizeUtils.mm2px(getContext(), 300), AutoSizeUtils.mm2px(getContext(), 300)));
    }

    public void setOnListener(OnListener listener) {
        this.listener = listener;
    }

    OnListener listener = null;

    public interface OnListener {
        void onchange(String api);
    }
}
