package zoombox.jinye.com.mycustomzoombox.customview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import zoombox.jinye.com.mycustomzoombox.R;

/**
 * Created by 今夜犬吠 on 2017/11/9.
 * 自定义缩放、移动框
 */

public class MCustomZoomView extends View {

  /*屏幕像素密度*/private float mDensity = getContext().getResources().getDisplayMetrics().density;

  /*控件宽*/private float mViewWidth;
  /*控件高*/private float mViewHeight;

  /*上下文*/private Context mContext;

  /*自定框相关*/
  /*矩形边长*/private float mRectLength = 200f * mDensity;
  /*矩形四个边角坐标*/private float[][] mRect_FourCorner_coordinate;
  /*边角线长度*/private float mCornerLength = 30f * mDensity;

  /*边角线偏移值*/private float mCornerOffset = 5 * mDensity;

  /*画笔*/
  /*边框画笔*/private Paint mRectPaint;
  /*边角线画笔*/private Paint mCornerPaint;
  /*文字画笔*/private Paint mTextPaint;
  /*测绘线画笔*/private Paint mMappingLinePaint;

  /*0-不动 1-拖动 2-边角缩放 3-边框缩放*/
  /*矩形操作状态*/private int mOperatingStatus = 0;

  /*0-左 1-上 2-右 3-下*/
  /*边框线点击-操作状态*/private int mBorderlineStatus = -1;

  /*0-左上角 1-左下角 2-右上角 3-右下角*/
  /*边角点击-操作状态*/private int mCornerStatus = -1;

  /*是否绘制坐标点*/private boolean mIsDrawPonit;
  /*是否绘制测绘线*/private boolean mISDrawMapLine;
  /*是否开启动画*/private boolean mIsOpenAnima;

  public MCustomZoomView(Context context) {
    super(context);
    this.mContext = context;
    initPaint();
  }

  public MCustomZoomView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    this.mContext = context;
    this.setFocusable(true);
    this.setFocusableInTouchMode(true);//触摸获取焦点
    initPaint();
    toolLoadRotateAnimation();
  }


  /**
   * 获取控件宽、高
   *
   * @param w
   * @param h
   * @param oldw
   * @param oldh
   */
  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    /*获取控件宽高*/
    mViewWidth = getWidth();
    mViewHeight = getHeight();

    /*初始化矩形四边角坐标*/
    mRect_FourCorner_coordinate = new float[][]{
        {(mViewWidth - mRectLength) / 2, (mViewHeight - mRectLength) / 2},//左上角
        {(mViewWidth - mRectLength) / 2, (mViewHeight + mRectLength) / 2},//左下角
        {(mViewWidth + mRectLength) / 2, (mViewHeight - mRectLength) / 2},//右上角
        {(mViewWidth + mRectLength) / 2, (mViewHeight + mRectLength) / 2},//右下角
    };
  }

  /**
   * 绘制框相关元素
   *
   * @param canvas
   */
  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onDraw(Canvas canvas) {
    /*绘制边框*/
    canvas.drawRect(mRect_FourCorner_coordinate[0][0], mRect_FourCorner_coordinate[0][1]
        , mRect_FourCorner_coordinate[3][0], mRect_FourCorner_coordinate[3][1], mRectPaint);

    /*绘制边角*/
    /*左上-横*/
    canvas.drawLine(mRect_FourCorner_coordinate[0][0] - mCornerOffset, mRect_FourCorner_coordinate[0][1]
        , mRect_FourCorner_coordinate[0][0] + mCornerLength, mRect_FourCorner_coordinate[0][1], mCornerPaint);
    /*左上-竖*/
    canvas.drawLine(mRect_FourCorner_coordinate[0][0], mRect_FourCorner_coordinate[0][1] - mCornerOffset
        , mRect_FourCorner_coordinate[0][0], mRect_FourCorner_coordinate[0][1] + mCornerLength, mCornerPaint);
     /*左下-横*/
    canvas.drawLine(mRect_FourCorner_coordinate[1][0] - mCornerOffset, mRect_FourCorner_coordinate[1][1]
        , mRect_FourCorner_coordinate[1][0] + mCornerLength, mRect_FourCorner_coordinate[1][1], mCornerPaint);
    /*左上下-竖*/
    canvas.drawLine(mRect_FourCorner_coordinate[1][0], mRect_FourCorner_coordinate[1][1] - mCornerLength
        , mRect_FourCorner_coordinate[1][0], mRect_FourCorner_coordinate[1][1] + mCornerOffset, mCornerPaint);
     /*右上-横*/
    canvas.drawLine(mRect_FourCorner_coordinate[2][0] - mCornerLength, mRect_FourCorner_coordinate[2][1]
        , mRect_FourCorner_coordinate[2][0] + mCornerOffset, mRect_FourCorner_coordinate[2][1], mCornerPaint);
    /*右上-竖*/
    canvas.drawLine(mRect_FourCorner_coordinate[2][0], mRect_FourCorner_coordinate[2][1] - mCornerOffset
        , mRect_FourCorner_coordinate[2][0], mRect_FourCorner_coordinate[2][1] + mCornerLength, mCornerPaint);
     /*右下-横*/
    canvas.drawLine(mRect_FourCorner_coordinate[3][0] - mCornerLength, mRect_FourCorner_coordinate[3][1]
        , mRect_FourCorner_coordinate[3][0] + mCornerOffset, mRect_FourCorner_coordinate[3][1], mCornerPaint);
    /*右下-竖*/
    canvas.drawLine(mRect_FourCorner_coordinate[3][0], mRect_FourCorner_coordinate[3][1] - mCornerLength
        , mRect_FourCorner_coordinate[3][0], mRect_FourCorner_coordinate[3][1] + mCornerOffset, mCornerPaint);

    if (mIsDrawPonit) {
      /*绘制坐标*/
      toolDrawPoint(canvas);
    }

    if (mISDrawMapLine) {
      toolDrawMapLine(canvas);
    }
  }

  /**
   * 初始化画笔
   */
  private void initPaint() {
    /*边框画笔*/
    /**初始化*/mRectPaint = new Paint();
    /**设置画笔颜色*/mRectPaint.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
    /**设置画笔样式*/mRectPaint.setStyle(Paint.Style.STROKE);
    /**设置画笔粗细*/mRectPaint.setStrokeWidth(2 * mDensity);
    /**使用抗锯齿*/mRectPaint.setAntiAlias(true);
    /**使用防抖动*/mRectPaint.setDither(true);
    /**设置笔触样式-圆*/mRectPaint.setStrokeCap(Paint.Cap.ROUND);
    /**设置结合处为圆弧*/mRectPaint.setStrokeJoin(Paint.Join.ROUND);

    /*边角画笔*/
    /**初始化*/mCornerPaint = new Paint();
    /**设置画笔颜色*/mCornerPaint.setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
    /**设置画笔样式*/mCornerPaint.setStyle(Paint.Style.FILL);
    /**设置画笔粗细*/mCornerPaint.setStrokeWidth(5 * mDensity);
    /**使用抗锯齿*/mCornerPaint.setAntiAlias(true);
    /**使用防抖动*/mCornerPaint.setDither(true);
    /**设置笔触样式-圆*/mRectPaint.setStrokeCap(Paint.Cap.ROUND);
    /**设置结合处为圆弧*/mRectPaint.setStrokeJoin(Paint.Join.ROUND);

    /*文字画笔*/
    /**初始化*/mTextPaint = new Paint();
    /**设置画笔颜色*/mTextPaint.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
    /**设置画笔样式*/mTextPaint.setStyle(Paint.Style.FILL);
    /**设置画笔粗细*/mTextPaint.setStrokeWidth(5 * mDensity);
    /**使用抗锯齿*/mTextPaint.setAntiAlias(true);
    /**使用防抖动*/mTextPaint.setDither(true);
    /**字体大小*/mTextPaint.setTextSize(30);
    /**设置笔触样式-圆*/mRectPaint.setStrokeCap(Paint.Cap.ROUND);
    /**设置结合处为圆弧*/mRectPaint.setStrokeJoin(Paint.Join.ROUND);

    /*测绘线画笔*/
    /**初始化*/mMappingLinePaint = new Paint();
    /**设置画笔颜色*/mMappingLinePaint.setColor(ContextCompat.getColor(mContext, R.color.cardview_shadow_start_color));
    /**设置画笔样式*/mMappingLinePaint.setStyle(Paint.Style.FILL);
    /**设置画笔粗细*/mMappingLinePaint.setStrokeWidth(1 * mDensity);
    /**使用抗锯齿*/mMappingLinePaint.setAntiAlias(true);
    /**使用防抖动*/mMappingLinePaint.setDither(true);
    /**设置笔触样式-圆*/mMappingLinePaint.setStrokeCap(Paint.Cap.ROUND);
    /**设置结合处为圆弧*/mMappingLinePaint.setStrokeJoin(Paint.Join.ROUND);
  }


  /*上一次按下的X坐标*/ float mLastPressX;

  /*上一次按下的Y坐标*/ float mLastPressY;

  @Override
  public boolean onTouchEvent(MotionEvent event) {

    switch (event.getAction()) {
      /*按下*/
      case MotionEvent.ACTION_DOWN:
        /**当前按下的X坐标*/float mPressX = event.getX();
        /**当前按下的Y坐标*/float mPressY = event.getY();

        /*判断按下的点是都在边界线上*/
        if (toolPointIsInBorderline(mPressX, mPressY)) {
          mOperatingStatus = 3;
        }
        /*判断按下的点是否在边角上*/
        else if (toolPointIsInCorner(mPressX, mPressY)) {
          mOperatingStatus = 2;//边角的范围是一个长宽等于边角线长的矩形范围内
        }
        /*判断按下的点是否在矩形内*/
        else if (toolPointIsInRect(mPressX, mPressY)) {
          mOperatingStatus = 1;
        }

        mLastPressX = mPressX;
        mLastPressY = mPressY;
        break;
      /*移动*/
      case MotionEvent.ACTION_MOVE:

        /*移动-改变矩形四个点坐标*/
        if (mOperatingStatus == 1) {
          mRect_FourCorner_coordinate[0][0] += event.getX() - mLastPressX;
          mRect_FourCorner_coordinate[0][1] += event.getY() - mLastPressY;

          mRect_FourCorner_coordinate[1][0] += event.getX() - mLastPressX;
          mRect_FourCorner_coordinate[1][1] += event.getY() - mLastPressY;

          mRect_FourCorner_coordinate[2][0] += event.getX() - mLastPressX;
          mRect_FourCorner_coordinate[2][1] += event.getY() - mLastPressY;

          mRect_FourCorner_coordinate[3][0] += event.getX() - mLastPressX;
          mRect_FourCorner_coordinate[3][1] += event.getY() - mLastPressY;

          /**重绘*/invalidate();

        }
        /*边角缩放*/
        else if (mOperatingStatus == 2) {
            /*是否继续缩放*/
          if (toolCornerIsTouch(event.getX(), event.getY(), mLastPressX, mLastPressY)) {
            return true;
          }
          /*判断点击的是哪一个角*/
          /*点击了左上角*/
          if (mCornerStatus == 0) {

//            /***/
//            if (mRect_FourCorner_coordinate[0][0] + mCornerLength >= mRect_FourCorner_coordinate[2][0] - mCornerLength
//                && mRect_FourCorner_coordinate[0][1] + mCornerLength >= mRect_FourCorner_coordinate[1][1] - mCornerLength) {
//
//
//            }
            /*左上角坐标变化*/
            mRect_FourCorner_coordinate[0][0] += event.getX() - mLastPressX;
            mRect_FourCorner_coordinate[0][1] += event.getY() - mLastPressY;

            /*右上角坐标变化-不影响矩形位置-影响边角线位置*/
            mRect_FourCorner_coordinate[2][1] += event.getY() - mLastPressY;

            /*左下角-同上*/
            mRect_FourCorner_coordinate[1][0] += event.getX() - mLastPressX;

          }
          /*点击了左下角*/
          else if (mCornerStatus == 1) {
            /*左下角坐标变化*/
            mRect_FourCorner_coordinate[1][0] += event.getX() - mLastPressX;
            mRect_FourCorner_coordinate[1][1] += event.getY() - mLastPressY;
            /*左上角坐标变化-不影响矩形位置-影响边角线位置*/
            mRect_FourCorner_coordinate[0][0] += event.getX() - mLastPressX;
            /*右下角-同上*/
            mRect_FourCorner_coordinate[3][1] += event.getY() - mLastPressY;
          }
          /*点击了右上角*/
          else if (mCornerStatus == 2) {

            /*右上角坐标变化*/
            mRect_FourCorner_coordinate[2][0] += event.getX() - mLastPressX;
            mRect_FourCorner_coordinate[2][1] += event.getY() - mLastPressY;
            /*左上角坐标变化-不影响矩形位置-影响边角线位置*/
            mRect_FourCorner_coordinate[0][1] += event.getY() - mLastPressY;
            /*右下角-同上*/
            mRect_FourCorner_coordinate[3][0] += event.getX() - mLastPressX;
          }
          /*点击了右下角*/
          else if (mCornerStatus == 3) {
            /*右下角坐标变化*/
            mRect_FourCorner_coordinate[3][0] += event.getX() - mLastPressX;
            mRect_FourCorner_coordinate[3][1] += event.getY() - mLastPressY;
            /*右上角坐标变化-不影响矩形位置-影响边角线位置*/
            mRect_FourCorner_coordinate[2][0] += event.getX() - mLastPressX;
            /*左下角-同上*/
            mRect_FourCorner_coordinate[1][1] += event.getY() - mLastPressY;
          }

          /*重新绘制*/
          invalidate();
        }
        /*边框缩放*/
        else if (mOperatingStatus == 3) {

          /*是否继续缩放*/
          if (toolCornerIsTouch(event.getX(), event.getY(), mLastPressX, mLastPressY)) {
            return true;
          }
          if (mBorderlineStatus == 0) {
            Log.e("自定义", "边框线-左");
            mRect_FourCorner_coordinate[0][0] += event.getX() - mLastPressX;
            mRect_FourCorner_coordinate[1][0] += event.getX() - mLastPressX;
          } else if (mBorderlineStatus == 1) {
            Log.e("自定义", "边框线-上");
            mRect_FourCorner_coordinate[0][1] += event.getY() - mLastPressY;
            mRect_FourCorner_coordinate[2][1] += event.getY() - mLastPressY;
          } else if (mBorderlineStatus == 2) {
            Log.e("自定义", "边框线-右");
            mRect_FourCorner_coordinate[2][0] += event.getX() - mLastPressX;
            mRect_FourCorner_coordinate[3][0] += event.getX() - mLastPressX;
          } else if (mBorderlineStatus == 3) {
            Log.e("自定义", "边框线-下");
            mRect_FourCorner_coordinate[1][1] += event.getY() - mLastPressY;
            mRect_FourCorner_coordinate[3][1] += event.getY() - mLastPressY;
          }
          /*重新绘制*/
          invalidate();
        }
        /**保存上一次按下的点X坐标*/mLastPressX = event.getX();
        /**保存上一次按下的点Y坐标*/mLastPressY = event.getY();
        break;
      /*松开*/
      case MotionEvent.ACTION_UP:
        /**恢复静止*/mOperatingStatus = 0;
        mBorderlineStatus = -1;
        mCornerStatus = -1;
        break;
      default:
        break;
    }
    return true;
  }

  /**
   * 判断按下的点是否在矩形内
   */
  private boolean toolPointIsInRect(float x, float y) {
    if (x > mRect_FourCorner_coordinate[0][0] + 2 * mDensity
        && x < mRect_FourCorner_coordinate[2][0] - 2 * mDensity
        && y > mRect_FourCorner_coordinate[0][1] + 2 * mDensity
        && y < mRect_FourCorner_coordinate[1][1] - 2 * mDensity) {
      return true;
    }
    return false;
  }

  /**
   * 绘制测绘线
   */
  private void toolDrawMapLine(Canvas canvas) {
    /*绘制横线*/
    /*绘制第一根线-位于矩形框的3分之一处*/
    canvas.drawLine(mRect_FourCorner_coordinate[0][0]
        , mRect_FourCorner_coordinate[0][1] + (mRect_FourCorner_coordinate[1][1] - mRect_FourCorner_coordinate[0][1]) / 3
        , mRect_FourCorner_coordinate[2][0]
        , mRect_FourCorner_coordinate[0][1] + (mRect_FourCorner_coordinate[1][1] - mRect_FourCorner_coordinate[0][1]) / 3
        , mMappingLinePaint);
     /*绘制第二根线-位于矩形框的3分之二处*/
    canvas.drawLine(mRect_FourCorner_coordinate[0][0]
        , mRect_FourCorner_coordinate[0][1] + (mRect_FourCorner_coordinate[1][1] - mRect_FourCorner_coordinate[0][1]) / 3 * 2
        , mRect_FourCorner_coordinate[2][0]
        , mRect_FourCorner_coordinate[0][1] + (mRect_FourCorner_coordinate[1][1] - mRect_FourCorner_coordinate[0][1]) / 3 * 2
        , mMappingLinePaint);

    /*绘制竖线*/
    /*绘制第一根线-位于矩形框的3分之一处*/
    canvas.drawLine(mRect_FourCorner_coordinate[0][0] + (mRect_FourCorner_coordinate[2][0] - mRect_FourCorner_coordinate[0][0]) / 3
        , mRect_FourCorner_coordinate[0][1]
        , mRect_FourCorner_coordinate[0][0] + (mRect_FourCorner_coordinate[2][0] - mRect_FourCorner_coordinate[0][0]) / 3
        , mRect_FourCorner_coordinate[1][1]
        , mMappingLinePaint);
     /*绘制第二根线-位于矩形框的3分之二处*/
    canvas.drawLine(mRect_FourCorner_coordinate[0][0] + (mRect_FourCorner_coordinate[2][0] - mRect_FourCorner_coordinate[0][0]) / 3 * 2
        , mRect_FourCorner_coordinate[0][1]
        , mRect_FourCorner_coordinate[0][0] + (mRect_FourCorner_coordinate[2][0] - mRect_FourCorner_coordinate[0][0]) / 3 * 2
        , mRect_FourCorner_coordinate[1][1]
        , mMappingLinePaint);
  }

  /**
   * 绘制坐标点
   */
  private void toolDrawPoint(Canvas canvas) {

   /*保存画布状态*/
    canvas.save();

    /*把画布逆时针旋转45度*/
    canvas.rotate(-45, mRect_FourCorner_coordinate[0][0], mRect_FourCorner_coordinate[0][1]);

    /*绘制左上角坐标*/
    canvas.drawText("x:" + mRect_FourCorner_coordinate[0][0]
            + " y:" + mRect_FourCorner_coordinate[0][1], mRect_FourCorner_coordinate[0][0], mRect_FourCorner_coordinate[0][1] - 10 * mDensity
        , mTextPaint);

    /*使画布恢复到save之前的状态*/
    canvas.restore();

    canvas.save();

    /*把画布顺时针旋转45度*/
    canvas.rotate(45, mRect_FourCorner_coordinate[2][0], mRect_FourCorner_coordinate[2][1]);

    /*绘制右上角坐标*/
    canvas.drawText("x:" + mRect_FourCorner_coordinate[2][0]
            + " y:" + mRect_FourCorner_coordinate[2][1], mRect_FourCorner_coordinate[2][0], mRect_FourCorner_coordinate[2][1] - 10 * mDensity
        , mTextPaint);

    canvas.restore();

    canvas.save();

    /*把画布逆时针旋转45度*/
    canvas.rotate(135, mRect_FourCorner_coordinate[3][0], mRect_FourCorner_coordinate[3][1]);

    /*绘制右下角坐标*/
    canvas.drawText("x:" + mRect_FourCorner_coordinate[3][0]
            + " y:" + mRect_FourCorner_coordinate[3][1], mRect_FourCorner_coordinate[3][0], mRect_FourCorner_coordinate[3][1] - 10 * mDensity
        , mTextPaint);


    canvas.restore();

    canvas.save();
    /*把画布逆时针旋转45度*/
    canvas.rotate(225, mRect_FourCorner_coordinate[1][0], mRect_FourCorner_coordinate[1][1]);

    /*绘制左下角坐标*/
    canvas.drawText("x:" + mRect_FourCorner_coordinate[1][0]
            + " y:" + mRect_FourCorner_coordinate[1][1], mRect_FourCorner_coordinate[1][0], mRect_FourCorner_coordinate[1][1] - 10 * mDensity
        , mTextPaint);

    canvas.restore();
  }

  /*动画控制*/private ObjectAnimator objectAnimator;

  /**
   * 开启旋转动画
   */
  private void toolLoadRotateAnimation() {
    objectAnimator = ObjectAnimator.ofFloat(this, "rotation", 0, 360)
        .setDuration(700);
    objectAnimator.setInterpolator(new LinearInterpolator());
    objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
    objectAnimator.addListener(new Animator.AnimatorListener() {
      @Override
      public void onAnimationStart(Animator animation) {

      }

      @Override
      public void onAnimationEnd(Animator animation) {
        if (mIsOpenAnima) {
          objectAnimator.start();
        }
      }

      @Override
      public void onAnimationCancel(Animator animation) {
      }

      @Override
      public void onAnimationRepeat(Animator animation) {

      }
    });
  }

  /**
   * 判断按下的点是否在边角范围内
   */
  private boolean toolPointIsInCorner(float x, float y) {
    if (x > mRect_FourCorner_coordinate[0][0]
        && x < mRect_FourCorner_coordinate[0][0] + mCornerLength / 2
        && y > mRect_FourCorner_coordinate[0][1]
        && y < mRect_FourCorner_coordinate[0][1] + mCornerLength / 2) {
      mCornerStatus = 0;
      return true;
    } else if (x > mRect_FourCorner_coordinate[0][0]
        && x < mRect_FourCorner_coordinate[0][0] + mCornerLength / 2
        && y > mRect_FourCorner_coordinate[1][1] - mCornerLength / 2
        && y < mRect_FourCorner_coordinate[1][1]) {
      mCornerStatus = 1;
      return true;
    } else if (x > mRect_FourCorner_coordinate[2][0] - mCornerLength / 2
        && x < mRect_FourCorner_coordinate[2][0]
        && y > mRect_FourCorner_coordinate[2][1]
        && y < mRect_FourCorner_coordinate[2][1] + mCornerLength / 2) {
      mCornerStatus = 2;
      return true;
    } else if (x > mRect_FourCorner_coordinate[3][0] - mCornerLength / 2
        && x < mRect_FourCorner_coordinate[3][0]
        && y > mRect_FourCorner_coordinate[3][1] - mCornerLength / 2
        && y < mRect_FourCorner_coordinate[3][1]) {
      mCornerStatus = 3;
      return true;
    }
    return false;
  }

  /**
   * 判断按下的点是否在边框线范围内
   */
  private boolean toolPointIsInBorderline(float x, float y) {
    if (x > mRect_FourCorner_coordinate[0][0] && x < mRect_FourCorner_coordinate[0][0] + 10 * mDensity
        && y > mRect_FourCorner_coordinate[0][1] + mCornerLength
        && y < mRect_FourCorner_coordinate[1][1] - mCornerLength) {
      mBorderlineStatus = 0;
      return true;
    } else if (x > mRect_FourCorner_coordinate[0][0] + mCornerLength
        && x < mRect_FourCorner_coordinate[2][0] - mCornerLength
        && y > mRect_FourCorner_coordinate[0][1] && y < mRect_FourCorner_coordinate[0][1] + 10 * mDensity) {
      mBorderlineStatus = 1;
      return true;
    } else if (x > mRect_FourCorner_coordinate[2][0] && x < mRect_FourCorner_coordinate[2][0] + 10 * mDensity
        && y > mRect_FourCorner_coordinate[2][1] + mCornerLength
        && y < mRect_FourCorner_coordinate[3][1] - mCornerLength) {
      mBorderlineStatus = 2;
      return true;
    } else if (x > mRect_FourCorner_coordinate[1][0] + mCornerLength && x < mRect_FourCorner_coordinate[3][0] - mCornerLength
        && y > mRect_FourCorner_coordinate[1][1] && y < mRect_FourCorner_coordinate[1][1] + 10 * mDensity) {
      mBorderlineStatus = 3;
      return true;
    }

    return false;
  }

  /**
   * 判断是否还能缩放-边角线触碰就不再缩放
   * true:触碰
   * false:没有触碰
   * 0:点击边框线没有触碰 1:触碰
   * 2:点击左上边角、
   */
  private boolean toolCornerIsTouch(float mNowx, float mNowY, float mLastPressX, float mLastPressY) {
    /*如果左右边角触碰*/
    if (mRect_FourCorner_coordinate[0][0] + mCornerLength >= mRect_FourCorner_coordinate[2][0] - mCornerLength) {
      /*如果点击左侧边框*/
      if (mBorderlineStatus == 0 || mCornerStatus == 0 || mCornerStatus == 1) {
        /*如果继续向右滑-禁止滑动*/
        if (mNowx > mLastPressX) {
          return true;
        } else {
          return false;
        }
      }
      /*如果点击右侧边框*/
      else if (mBorderlineStatus == 2 || mCornerStatus == 2 || mCornerStatus == 3) {
        /*如果继续向左滑动-禁止滑动*/
        if (mNowx < mLastPressX) {
          return true;
        } else {
          return false;
        }
      }
    } else if (mRect_FourCorner_coordinate[0][1] + mCornerLength >= mRect_FourCorner_coordinate[1][1] - mCornerLength) {
      /*如果点击的是上侧边框*/
      if (mBorderlineStatus == 1 || mCornerStatus == 0 || mCornerStatus == 2) {
        /*如果继续向下滑-禁止滑动*/
        if (mNowY > mLastPressY) {
          return true;
        } else {
          return false;
        }
      }
      /*如果点击的是下侧边框*/
      else if (mBorderlineStatus == 3 || mCornerStatus == 1 || mCornerStatus == 3) {
        /*如果继续向上滑-禁止滑动*/
        if (mNowY < mLastPressY) {
          return true;
        } else {
          return false;
        }
      }
    }
    return false;
  }

  /**
   * 判断是横向滑动还是垂直滑动
   * true:横向滑动
   * false:垂直滑动
   */
  private boolean toolHorOrVer(float mNowx, float mNowY, float mLastPressX, float mLastPressY) {

    return false;
  }

  public void setIsDrawPonit(boolean mIsDrawPonit) {
    this.mIsDrawPonit = mIsDrawPonit;
    invalidate();
  }

  public void setISDrawMapLine(boolean mISDrawMapLine) {
    this.mISDrawMapLine = mISDrawMapLine;
    invalidate();
  }

  public void setOpenAnima(boolean mOpenStatus) {
    if (!mIsOpenAnima) {
      this.mIsOpenAnima = mOpenStatus;
      objectAnimator.start();
    } else {
      this.mIsOpenAnima = mOpenStatus;
      objectAnimator.cancel();
    }

  }
}
