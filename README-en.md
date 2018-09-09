# TextPathView
　　
![](https://i.imgur.com/l5o8XG5.gif)


## Introduction
　　TextPathView is a view with text path animation!

## Get started
　　
### Gradle

```
compile 'com.yanzhikai:TextPathView:0.1.3'
```

 > minSdkVersion 16

 > Hardware acceleration may cause some problems when using version 0.1.2.

### Usage

#### TextPathView
　　There are tow types of TextPathView:
 - SyncTextPathView, draw the text paths one by one.
 - AsyncTextPathView, draw the each text path in the same time.
　　Here is a demo：

In the xml：

```
     <yanzhikai.textpath.SyncTextPathView
         android:id="@+id/stpv_2017"
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         app:duration="12000"
         app:showPainter="true"
         app:text="2017"
         app:textInCenter="true"
         app:textSize="60sp"
         android:layout_weight="1"
         />

    <yanzhikai.textpath.AsyncTextPathView
        android:id="@+id/atpv_1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:duration="12000"
        app:showPainter="true"
        app:text="炎之铠"
        app:textStrokeColor="@android:color/holo_orange_light"
        app:textInCenter="true"
        app:textSize="62sp"
        android:layout_gravity="center_horizontal"
        />

```

In the java：

```
        atpv1 = findViewById(R.id.atpv_1);
        stpv_2017 = findViewById(R.id.stpv_2017);

        //从无到显示
        atpv1.startAnimation(0,1);
        //从显示到消失
        stpv_2017.startAnimation(1,0);
```

Also you can use SeekBar to control the progess of TextPathView：

```
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                atpv1.drawPath(progress / 1000f);
                stpv_2017.drawPath(progress / 1000f);

            }
        }
```

#### PathView
　　PathView is a new class after version 0.1.1. It has three subclass:TextPathView, SyncPathView, AsyncPathView.The latter are used to play to animation of path.

```
public class TestPath extends Path {
    public TestPath(){
        init();
    }

    private void init() {
        addCircle(350,300,150,Direction.CCW);
        addCircle(350,300,100,Direction.CW);
        addCircle(350,300,50,Direction.CCW);
        moveTo(350,300);
        lineTo(550,500);
    }
}
```
　　You must use `setPath()` before `startAnimation()`:
```
        aspv.setPath(new TestPath());
        aspv.startAnimation(0,1);
```
![](https://i.imgur.com/YQMVBwz.gif)

### Attributes

|**Attribute Name**|**Description**|**Type**|**Default**|
|--|--|:--:|:--:|
|textSize      | text size     | integer| 108 |
|text      | text content      | String| Test|
|autoStart| start animation from 0 to 1 at the beginning    | boolean| false|
|showInStart| show the text path at the beginning    | boolean| false|
|textInCenter| make the text in the center    | boolean| false|
|duration | duration of animation(ms)   | integer| 10000|
|showPainter | whether the Painter Effects can show while animating  | boolean| false|
|showPainterActually| whether the Painter Effects can show while drawing.**It will be set to false when the animator finish.**| boolean| false|
||~~textStrokeWidth~~ strokeWidth  |the stroke width of path      | dimension| 5px|
|~~textStrokeColor~~ paintStrokeColor | the stroke color of path     | color| Color.black|
|paintStrokeWidth |width of paint effect stroke     | dimension| 3px|
|paintStrokeColor | color of paint effect stroke   | color| Color.black|
|repeat| repeat type of animation| enum | NONE|

|repeat|Description|
|--|--|
|NONE|no repeat|
|RESTART|restart to play|
|REVERSE|reverse to play|



### Methods

#### Painter Effects

```
    //设置画笔特效
    public void setPainter(SyncPathPainter listener);
    //设置画笔特效
    public void setPainter(AsyncPathPainter listener);
```
　　There are types of PathPainter：

```
    public interface SyncPathPainter extends PathPainter {
        //开始动画的时候执行
        void onStartAnimation();

        /**
         * 绘画画笔特效时候执行
         * @param x 当前绘画点x坐标
         * @param y 当前绘画点y坐标
         * @param paintPath 画笔Path对象，在这里画出想要的画笔特效
         */
        @Override
        void onDrawPaintPath(float x, float y, Path paintPath);
    }

    public interface AsyncPathPainter extends PathPainter {
        /**
         * 绘画画笔特效时候执行
         * @param x 当前绘画点x坐标
         * @param y 当前绘画点y坐标
         * @param paintPath 画笔Path对象，在这里画出想要的画笔特效
         */
        @Override
        void onDrawPaintPath(float x, float y, Path paintPath);
    }
```
　　You can extend one of these PathPainter to draw your custom paint effects.
 
　　There are three paint effects in TextPathView's code:

```

//an arrow point ahead
public class ArrowPainter implements SyncPathPainter {

//a simple shape of pen
public class PenPainter implements SyncPathPainter,AsyncPathPainter {

//a firework effec
public class FireworksPainter implements SyncPathPainter {

```

　

#### Custom Effects
　　Making custom effect is very easy. You can override `onDrawPaintPath(float x, float y, Path paintPath)`：

```
        atpv2.setPathPainter(new AsyncPathPainter() {
            @Override
            public void onDrawPaintPath(float x, float y, Path paintPath) {
                paintPath.addCircle(x,y,6, Path.Direction.CCW);
            }
        });
```
![](https://i.imgur.com/fPeYF8f.gif)

#### AnimatorListener

```
    //设置自定义动画监听
    public void setAnimatorListener(PathAnimatorListener animatorListener);
  
```
　　PathAnimatorListener implements AnimatorListener and we can use to listen the text path animation.

#### Getting the Paint

```
    //获取绘画文字的画笔:get the paint of path 
    public Paint getDrawPaint() {
        return mDrawPaint;
    }

    //获取绘画画笔特效的画笔:get the paint of paint effect
    public Paint getPaint() {
        return mPaint;
    }
```

#### Drawing Contorl

```
    /**
     * 开始绘制文字路径动画
     * @param start 路径比例，范围0-1
     * @param end 路径比例，范围0-1
     */
    public void startAnimation(float start, float end);

    /**
     * 绘画文字路径的方法
     * @param progress 绘画进度，0-1
     */
    public void drawPath(float progress);

    /**
     * Stop animation
     */
    public void stopAnimation();

    /**
     * Pause animation
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void pauseAnimation();

    /**
     * Resume animation
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void resumeAnimation();
```

#### Filling Color

```
    //直接显示填充好颜色了的全部文字: fill the text path with storke color
    public void showFillColorText();
```
　　Usually, you can use `showFillColorText()` when the animation(0-1) end：

```
        //设置动画播放完后填充颜色
        stpv_fortune.setAnimatorListener(new PathAnimatorListener(stpv_fortune){
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                stpv_fortune.showFillColorText();
            }
        });
        stpv_wish.setCanShowPainter(true);
```
![](https://i.imgur.com/Ziovoic.gif)

#### Else

```
    //设置文字内容
    public void setText(String text)；

    //设置路径，必须先设置好路径在startAnimation()，不然会报错！
    //It must be used before startAnimation().
    public void setPath(Path path) ；

    //设置字体样式
    public void setTypeface(Typeface typeface)；

    //清除画面
    public void clear();

    //设置动画时能否显示画笔效果:Here to set whether the Painter Effects can show while animating.
    public void setShowPainter(boolean showPainter)；

    //设置所有时候是否显示画笔效果,由于动画绘画完毕应该将画笔特效消失，所以每次执行完动画都会自动设置为false:Here to set whether the Painter Effects can show while drawing.It will be set to false when the animator finish.
    public void setCanShowPainter(boolean canShowPainter)；

    //设置动画持续时间
    public void setDuration(int duration);

    //设置重复方式
    public void setRepeatStyle(int repeatStyle);

```

## Update

 - 2018/03/08 **version 0.0.5**:
     - add `showFillColorText()`
     - 把PathAnimatorListener从PathView的内部类里面解放出来，之前使用太麻烦了。
     - add `showPainterActually` to control whether the Painter Effects can show while drawing.It will be set to false when the animator finish.

 - 2018/03/08 **version 0.0.6**:
     - add `stop(), pause(), resume()`,API version >= 19.
     - add `repeat`，API version >= 19.
     - Thanks for [toanvc](https://github.com/toanvc)'s contribution!

 - 2018/03/18 **version 0.1.0**:
     -  Made a reconstruction，add Class :SyncPathView and AsyncPathView，and their superclass PathView.
     -  Add `setDuration()`、`setRepeatStyle()`
     -  Changed names：

|Old Name|New Name|
|---|---|
|TextPathPainter|PathPainter|
|SyncTextPainter|SyncPathPainter|
|AsyncTextPainter|AsyncPathPainter|
|TextAnimatorListener|PathAnimatorListener|

 - 2018/03/21 **version 0.1.2**:
     - Fixed: It will not completely display when warp_content.
     - TextPathView can use `setTypeface()` now.
     - Suppurted word wrap.

![](https://i.imgur.com/5wHvQvD.gif)

 - 2018/09/09 **version 0.1.3**:
     - Turn off the hardware acceleration by default
     - Avoided memory leaks




### To do：

 - More pianter effects, more types of animation! 
 - Make it smoother and more efficient.
 - Word wrap support.



## Code contribution
　　If it's a feature that you think would need to be discussed please open an issue first, otherwise, you can:
 - Create a feature branch (git checkout -b my_branch)
 - If you want to fix a problem. Your code must contain test for reproducing problem. Your tests must be passed with help of your fix.
 - If you want to add a new feature. Please create a new activity, such as SecondActivity, to show the new feature.

## LICENSE
　　TextPathView follows MIT license.

## About
 > id：Yanzhikai

 > Email：yanzhikai_yjk@qq.com

 > CSDN：http://blog.csdn.net/totond






