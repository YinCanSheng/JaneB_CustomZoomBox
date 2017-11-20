## JaneB_CustomZoomBox


#### 序
- 自定义图片裁剪矩形框
- 初入自定义的伙伴可观摩借鉴
- 效果图

  ![image](https://github.com/YinCanSheng/JaneB_CustomZoomBox/blob/master/MyCustomZoomBox/gif/zoomView.gif)
  

#### 纲要
 - 点击框内移动矩形
 - 点击边框线单向缩放矩形
 - 点击边角双向缩放矩形
 
#### 使用
 - 把源码copy到自己的工程
 - 在XML布局中引入（把包路径修改成自己的）   
 ```
 <zoombox.jinye.com.mycustomzoombox.customview.MCustomZoomView
        android:id="@+id/MCustomZoomView_MainActivity_ZoomView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@+id/CardView_MainActivity_head"
        />  
        
 ```
 ### 跋
 - 矩形初始状态自行修改源码即可（大小、颜色、位置）
 - 此Demo单纯实现自定义框的编写，至于其他交互，通读一遍源码，相信你可已举一反三活学活用。
