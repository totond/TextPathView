# TextPathView
　　
![](https://i.imgur.com/l5o8XG5.gif)


## Introduction
　　TextPathView is a view with text path animation!

## Get started
　　
### Gradle

```
compile 'com.yanzhikai:TextPathView:0.0.6'
```

 > minSdkVersion 16

### Usage
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

### Attributes

|**Attribute Name**|**Description**|**Type**|**Default**|
|--|--|:--:|:--:|
|textSize      | text size     | integer| 108 |
|text      | text content      | String| Test|
|duration | duration of animation(ms)   | integer| 10000|
|showPainter | whether the Painter Effects can show while animating  | boolean| false|
|showPainterActually| whether the Painter Effects can show while drawing.**It will be set to false when the animator finish.**| boolean| false|
|textStrokeWidth | width of text stroke      | dimension| 5px|
|textStrokeColor | color of text stroke    | color| Color.black|
|paintStrokeWidth |width of paint effect stroke     | dimension| 3px|
|paintStrokeColor | color of paint effect stroke   | color| Color.black|
|autoStart| start animation from 0 to 1 at the beginning    | boolean| false|
|showInStart| show the text path at the beginning    | boolean| false|
|textInCenter| make the text in the center    | boolean| false|
|repeat**(新增) **| repeat type of animation| enum | NONE|

|repeat|Description|
|--|--|
|NONE|no repeat|
|RESTART|restart to play|
|REVERSE|reverse to play|



### Methods

#### Painter Effects

```
    //设置画笔特效
    public void setTextPainter(SyncTextPainter listener);
```
　　There are types of TextPainter：

```
    public interface SyncTextPainter extends TextPainter {
        //开始动画的时候执行
        void onStartAnimation();

        /**
         * 绘画画笔特效时候执行:do when animation start
         * @param x 当前绘画点x坐标
         * @param y 当前绘画点y坐标
         * @param paintPath 画笔Path对象，在这里画出想要的画笔特效: darw the paint effects here
         */
        @Override
        void onDrawPaintPath(float x, float y, Path paintPath);
    }

    public interface AsyncTextPainter extends TextPainter{
        /**
         * 绘画画笔特效时候执行:do when animation start
         * @param x 当前绘画点x坐标
         * @param y 当前绘画点y坐标
         * @param paintPath 画笔Path对象，在这里画出想要的画笔特效: darw the paint effects here
         */
        @Override
        void onDrawPaintPath(float x, float y, Path paintPath);
    }
```
　　You can extend one of these TextPainter to draw your custom paint effects.
 
　　There are three paint effects in TextPathView's code:

```

//箭头画笔特效，根据传入的当前点与上一个点之间的速度方向，来调整箭头方向: an arrow point ahead
public class ArrowPainter implements SyncTextPathView.SyncTextPainter{}

//一支笔的画笔特效，就是在绘画点旁边画多一支笔: a simple shape of pen
public class PenPainter implements SyncTextPathView.SyncTextPainter,AsyncTextPathView.AsyncTextPainter {}

//火花特效，根据箭头引申变化而来，根据当前点与上一个点算出的速度方向来控制火花的方向: a firework effec
public class FireworksPainter implements SyncTextPathView.SyncTextPainter{}

```

　

#### Custom Effects
　　Making custom effect is very easy. You can override `onDrawPaintPath(float x, float y, Path paintPath)`：

```
        atpv2.setTextPainter(new AsyncTextPathView.AsyncTextPainter() {
            @Override
            public void onDrawPaintPath(float x, float y, Path paintPath) {
                paintPath.addCircle(x,y,3, Path.Direction.CCW);
            }
        });
```
![](https://i.imgur.com/fPeYF8f.gif)

#### AnimatorListener

```
    //设置自定义动画监听
    public void setAnimatorListener(TextPathAnimatorListener animatorListener);
  
```
　　TextPathAnimatorListener implements AnimatorListener and we can use to listen the text path animation.

#### Getting the Paint

```
    //获取绘画文字的画笔:get the paint of text path 
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
        stpv_fortune.setAnimatorListener(new TextPathAnimatorListener(stpv_fortune){
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

    //清除画面
    public void clear();

    //设置动画时能否显示画笔效果:Here to set whether the Painter Effects can show while animating.
    public void setShowPainter(boolean showPainter)；

    //设置所有时候是否显示画笔效果,由于动画绘画完毕应该将画笔特效消失，所以每次执行完动画都会自动设置为false:Here to set whether the Painter Effects can show while drawing.It will be set to false when the animator finish.
    public void setCanShowPainter(boolean canShowPainter)；

```

## Update

 - 2018/03/08 **version 0.0.5**:
     - add `showFillColorText()`
     - 把TextPathAnimatorListener从TextPathView的内部类里面解放出来，之前使用太麻烦了。
     - add `showPainterActually` to control whether the Painter Effects can show while drawing.It will be set to false when the animator finish.

 - 2018/03/08 **version 0.0.6**:
     - add `stop(), pause(), resume()`,API version >= 19.
     - add `repeat`，API version >= 19.

![](https://i.imgur.com/qQ55UrW.gif)

 > Thanks for [toanvc](https://github.com/toanvc)'s contribution!



### To do：

 - More pianter effects, more types of animation! 
 - More input types, not just text.
 - Make it smoother and more efficient.

　　such as this:
![](https://i.imgur.com/Nk5ApAX.gif)

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






