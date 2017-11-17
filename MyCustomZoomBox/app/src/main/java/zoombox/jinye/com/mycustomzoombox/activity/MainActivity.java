package zoombox.jinye.com.mycustomzoombox.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import rx.functions.Action1;
import zoombox.jinye.com.mycustomzoombox.R;
import zoombox.jinye.com.mycustomzoombox.base.BaseActivity;
import zoombox.jinye.com.mycustomzoombox.customview.MCustomZoomView;


/**
 * 主页
 *
 * @author ChenHong
 */
public class MainActivity extends BaseActivity {

  /*自定义框*/
  @BindView(R.id.MCustomZoomView_MainActivity_ZoomView)
  MCustomZoomView mCustomZoomView;
  /*是否开启动画*/
  @BindView(R.id.CheckBox_MainActivity_startAnima)
  AppCompatCheckBox mStartAnimaCB;
  /*是否显示坐标点*/
  @BindView(R.id.CheckBox_MainActivity_showCoordinate)
  AppCompatCheckBox mShowCoordinateCB;
  /*是否显示测绘线*/
  @BindView(R.id.CheckBox_MainActivity_showMappingLine)
  AppCompatCheckBox mMappingLineCB;

  /*回调一次主页-获取边界坐标*/

  /**
   * 给实现类用 返回布局资源ID加载界面布局
   */
  @Override
  public int getLayoutResID() {
    return R.layout.activity_main;
  }

  /**
   * 初始化控件
   */
  @Override
  public void findViews() {

  }

  /**
   * 配置控件初始化状态
   */
  @Override
  public void configureView() {
    /*提示*/
    Snackbar.make(mStartAnimaCB,"点击框内移动,点击边框线单向伸缩,点击边角双向伸缩",Snackbar.LENGTH_INDEFINITE)
        .setAction("退出", new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
        }).show();
  }

  /**
   * 初始化对象
   */
  @Override
  public void initObj() {

  }

  /**
   * 设置监听
   */
  @Override
  public void setListener() {
    /*显示坐标*/
    RxView.clicks(mShowCoordinateCB)
        .compose(this.<Void>bindToLifecycle())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            if(isNullOfObject(mCustomZoomView)){
              if(mShowCoordinateCB.isChecked()){
                mCustomZoomView.setIsDrawPonit(true);
              }else{
                mCustomZoomView.setIsDrawPonit(false);
              }
            }
          }
        });
    /*显示测绘线*/
    RxView.clicks(mMappingLineCB)
        .compose(this.<Void>bindToLifecycle())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            if(isNullOfObject(mCustomZoomView)){
              if(mMappingLineCB.isChecked()){
                mCustomZoomView.setISDrawMapLine(true);
              }else{
                mCustomZoomView.setISDrawMapLine(false);
              }
            }
          }
        });
    /*开启动画*/
    RxView.clicks(mStartAnimaCB)
        .compose(this.<Void>bindToLifecycle())
        .subscribe(new Action1<Void>() {
          @Override
          public void call(Void aVoid) {
            if(isNullOfObject(mCustomZoomView)){
              if(mStartAnimaCB.isChecked()){
                mCustomZoomView.setOpenAnima(true);
              }else{
                mCustomZoomView.setOpenAnima(false);
              }
            }
          }
        });
  }

  /**
   * 数据操作
   */
  @Override
  public void initData() {

  }

  /**
   * 单击监听响应
   *
   * @param mWeigetId
   */
  @Override
  public void responseClick(int mWeigetId) {

  }
}
