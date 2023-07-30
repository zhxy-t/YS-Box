#!/bin/bash

#获取目录
CURRENT_DIR=$(cd $(dirname $0); pwd)
num=$(find $CURRENT_DIR -name gradlew  | awk -F"/" '{print NF-1}')
DIR=$(find $CURRENT_DIR -name gradlew  | cut -d \/ -f$num)
cd $CURRENT_DIR/$DIR

#签名
signingConfigs='ICAgIHNpZ25pbmdDb25maWdzIHtcCiAgICAgICAgaWYgKHByb2plY3QuaGFzUHJvcGVydHkoIlJFTEVBU0VfU1RPUkVfRklMRSIpKSB7XAogICAgICAgICAgICBteUNvbmZpZyB7XAogICAgICAgICAgICAgICAgc3RvcmVGaWxlIGZpbGUoUkVMRUFTRV9TVE9SRV9GSUxFKVwKICAgICAgICAgICAgICAgIHN0b3JlUGFzc3dvcmQgUkVMRUFTRV9TVE9SRV9QQVNTV09SRFwKICAgICAgICAgICAgICAgIGtleUFsaWFzIFJFTEVBU0VfS0VZX0FMSUFTXAogICAgICAgICAgICAgICAga2V5UGFzc3dvcmQgUkVMRUFTRV9LRVlfUEFTU1dPUkRcCiAgICAgICAgICAgICAgICB2MVNpZ25pbmdFbmFibGVkIHRydWVcCiAgICAgICAgICAgICAgICB2MlNpZ25pbmdFbmFibGVkIHRydWVcCiAgICAgICAgICAgICAgICBlbmFibGVWM1NpZ25pbmcgPSB0cnVlXAogICAgICAgICAgICAgICAgZW5hYmxlVjRTaWduaW5nID0gdHJ1ZVwKICAgICAgICAgICAgfVwKICAgICAgICB9XAogICAgfVwKXA=='
signingConfig='ICAgICAgICAgICAgaWYgKHByb2plY3QuaGFzUHJvcGVydHkoIlJFTEVBU0VfU1RPUkVfRklMRSIpKSB7XAogICAgICAgICAgICAgICAgc2lnbmluZ0NvbmZpZyBzaWduaW5nQ29uZmlncy5teUNvbmZpZ1wKICAgICAgICAgICAgfVwK'
signingConfigs="$(echo "$signingConfigs" |base64 -d )"
signingConfig="$(echo "$signingConfig" |base64 -d )"
sed -i -e "/defaultConfig {/i\\$signingConfigs " -e "/debug {/a\\$signingConfig " -e "/release {/a\\$signingConfig " $CURRENT_DIR/$DIR/app/build.gradle
cp -f $CURRENT_DIR/DIY2/TVBoxOSC.jks $CURRENT_DIR/$DIR/app/TVBoxOSC.jks
cp -f $CURRENT_DIR/DIY2/TVBoxOSC.jks $CURRENT_DIR/$DIR/TVBoxOSC.jks
echo "" >>$CURRENT_DIR/$DIR/gradle.properties
echo "RELEASE_STORE_FILE=./TVBoxOSC.jks" >>$CURRENT_DIR/$DIR/gradle.properties
echo "RELEASE_KEY_ALIAS=TVBoxOSC" >>$CURRENT_DIR/$DIR/gradle.properties
echo "RELEASE_STORE_PASSWORD=TVBoxOSC" >>$CURRENT_DIR/$DIR/gradle.properties
echo "RELEASE_KEY_PASSWORD=TVBoxOSC" >>$CURRENT_DIR/$DIR/gradle.properties


#进度条颜色
sed -i 's/color_353744/color_1890FF/g' $CURRENT_DIR/$DIR/app/src/main/res/drawable/shape_player_control_vod_seek.xml



#图标修改 图标1 原版透明
cp $CURRENT_DIR/DIY2/原版透明.png $CURRENT_DIR/$DIR/app/src/main/res/drawable-hdpi/app_icon.png
cp $CURRENT_DIR/DIY2/原版透明.png $CURRENT_DIR/$DIR/app/src/main/res/drawable-xhdpi/app_icon.png
cp $CURRENT_DIR/DIY2/原版透明.png $CURRENT_DIR/$DIR/app/src/main/res/drawable-xxhdpi/app_icon.png
mv $CURRENT_DIR/DIY2/原版透明.png $CURRENT_DIR/$DIR/app/src/main/res/drawable-xxxhdpi/app_icon.png





#背景修改
cp $CURRENT_DIR/DIY2/背景3.png $CURRENT_DIR/$DIR/app/src/main/res/drawable/app_bg.png
#首页时间
sed -i 's/yyyy/MM/dd HH:mm/MM/dd aHH:mm:ss/g' $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/ui/activity/HomeActivity.java 
#首页多排
sed -i 's/380+200/360+200/g' $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/ui/activity/HomeActivity.java 
sed -i 's/380+200/360+200/g' $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/ui/fragment/ModelSettingFragment.java
sed -i 's/380+200/340+140/g' $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/ui/dialog/SearchCheckboxDialog.java

#首页排版边框
sed -i 's/vs_30/vs_15/g' $CURRENT_DIR/$DIR/app/src/main/res/layout/dialog_select.xml

# 默认设置
#cp $CURRENT_DIR/DIY2/J/App.java $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/base/App.java  

#自定义epg
cp $CURRENT_DIR/DIY2/epg_data.json $CURRENT_DIR/$DIR/app/src/main/assets/epg_data.json

#增加听书嗅探
sed -i 's/|mp4|/|mp4|mp3|m4a|p2p|/'g $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/util/DefaultConfig.java

//播放文字修改
#cp $CURRENT_DIR/DIY/PlayerHelper.java $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/util/PlayerHelper.java
sed -i 's/播放器//g' $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/util/PlayerHelper.java
sed -i 's/TextureView/Texture/g' $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/util/PlayerHelper.java
sed -i 's/SurfaceView/Surface/g' $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/util/PlayerHelper.java
#主界面首页文字颜色修改
sed -i 's/color_BBFFFFFF/color_FFFFFF/g' $CURRENT_DIR/$DIR/app/src/main/res/layout/item_home_sort.xml
sed -i 's/color_BBFFFFFF/color_FFFFFF/g' $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/ui/activity/HomeActivity.java


#修改进度条消失时间
sed -i 's/10000/6000/g'  $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/player/controller/VodController.java
#名称修改
sed -i 's/TVBox/影视Box/g' $CURRENT_DIR/$DIR/app/src/main/res/values/strings.xml

//增加参数
cp $CURRENT_DIR/DIY2/J/dimens.xml         $CURRENT_DIR/$DIR/app/src/main/res/values/dimens.xml
cp $CURRENT_DIR/DIY2/J/colors.xml         $CURRENT_DIR/$DIR/app/src/main/res/values/colors.xml
cp $CURRENT_DIR/DIY2/J/strings.xml        $CURRENT_DIR/$DIR/app/src/main/res/values/strings.xml
cp $CURRENT_DIR/DIY2/J/HawkConfig.java    $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/util/HawkConfig.java

//播放资源 
cp $CURRENT_DIR/DIY2/J/PlayerUtils.java   $CURRENT_DIR/$DIR/player/src/main/java/xyz/doikki/videoplayer/util/PlayerUtils.java

//增加图标
cp $CURRENT_DIR/DIY2/J/hm_history.png   $CURRENT_DIR/$DIR/app/src/main/res/drawable/hm_history.png
cp $CURRENT_DIR/DIY2/J/hm_mobile.png    $CURRENT_DIR/$DIR/app/src/main/res/drawable/hm_mobile.png
cp $CURRENT_DIR/DIY2/J/hm_lan.png   $CURRENT_DIR/$DIR/app/src/main/res/drawable/hm_lan.png

//添加直播历史
cp $CURRENT_DIR/DIY2/J/设置/ApiConfig.java           $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/api/ApiConfig.java
cp $CURRENT_DIR/DIY2/J/设置/ApiDialog.java           $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/ui/dialog/ApiDialog.java
cp $CURRENT_DIR/DIY2/J/设置/dialog_api.xml           $CURRENT_DIR/$DIR/app/src/main/res/layout/dialog_api.xml
cp $CURRENT_DIR/DIY2/J/设置/SettingActivity.java    $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/ui/activity/SettingActivity.java
cp $CURRENT_DIR/DIY2/J/直播/LivePlayActivity.java    $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/ui/activity/LivePlayActivity.java
cp $CURRENT_DIR/DIY2/J/设置/ApiModel.java           $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/bean/ApiModel.java
cp $CURRENT_DIR/DIY2/J/设置/SourceUtil.java           $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/util/SourceUtil.java


//其他修改
cp $CURRENT_DIR/DIY2/J/event/RefreshEvent.java            $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/event/RefreshEvent.java
cp $CURRENT_DIR/DIY2/index.html                         $CURRENT_DIR/$DIR/app/src/main/res/raw/index.html
cp $CURRENT_DIR/DIY2/DetailActivity.java          $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/ui/activity/DetailActivity.java
cp $CURRENT_DIR/DIY2/J/MyOkhttpDownLoader.java          $CURRENT_DIR/$DIR/app/src/main/java/com/github/tvbox/osc/picasso/MyOkhttpDownLoader.java


#版本号

sed -i 's/1.0.0/1.1.0/g'  $CURRENT_DIR/$DIR/app/build.gradle
sed -i 's/1.0.0/1.1.0/g'  $CURRENT_DIR/$DIR/app/src/main/res/layout/fragment_model.xml
#共存

sed -i 's/com.github.tvbox.osc/com.YsBox.tv/g' $CURRENT_DIR/$DIR/app/build.gradle

echo 'DIY end'
