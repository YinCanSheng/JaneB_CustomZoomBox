package zoombox.jinye.com.mycustomzoombox.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * activity基类
 *
 * @author ChenHong
 */
public abstract class BaseActivity extends RxAppCompatActivity
    implements OnClickListener, Baselogic {

  /*緩存控件-集合*/private SparseArray<View> mViewsSparesA;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    /*加载界面布局*/
    if (getLayoutResID() != 0) {

      /*如果后台有应用-启用之前的*/
      if ((getIntent().getFlags()
          & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {

       // MDefApplication.getInstance().finishActivity();
        return;
      }

      /**加载布局*/setContentView(getLayoutResID());

      /**绑定子V*/ButterKnife.bind(this);
    }

    /*初始化控件集合-用于緩存*/
    mViewsSparesA = new SparseArray<View>();

    /*初始化控件*/
    findViews();

    /*配置控件初始状态*/
    configureView();

    /*初始化对象*/
    initObj();

    /*设置监听*/
    setListener();

    /*数据操作*/
    initData();
  }

  /**
   * 单击监听响应
   */
  @Override
  public void onClick(View mView) {
    responseClick(mView.getId());

  }

  /**
   * 通过控件ID得到控件对象
   */
  public <E extends View> E getViewObjectOfId(int ViewId) {
           /*拿取已加载过的控件*/
    E mView = (E) mViewsSparesA.get(ViewId);
            /*没有加载过则加载控件*/
    if (mView == null) {
                /*初始化控件*/
      mView = (E) findViewById(ViewId);
                /*把控件添加到集合*/
      mViewsSparesA.put(ViewId, mView);
    }
    return mView;
  }

  /**
   * 判断对象是否为空
   */
  public boolean isNullOfObject(Object mObject) {
    if (mObject != null) {
      return true;
    }
    return false;
  }

}
