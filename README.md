## JaneB_CustomZoomBox

---

#### 跋
- 自定义裁剪矩形框

- 效果图

  ![image](https://github.com/YinCanSheng/JaneB_CustomZoomBox/blob/master/MyCustomZoomBox/gif/ZoomBox.gif)
  

#### 纲要
 - 点击框内移动矩形
 - 点击边框线单向缩放矩形
 - 点击边角双向缩放矩形
 
#### 使用
 - 在布局中引入（把包路径修改成自己的）   
 ```
 <zoombox.jinye.com.mycustomzoombox.customview.MCustomZoomView
        android:id="@+id/MCustomZoomView_MainActivity_ZoomView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@+id/CardView_MainActivity_head"
        />  
        
 ```
 - 矩形初始状态自行修改源码即可
