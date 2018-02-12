# TextPathView
　　TextPathView是一个把文字转化为路径动画然后展现出来的自定义控件。效果如下图：

![](https://i.imgur.com/rd3JUdQ.gif)

## 介绍
　　

## 使用
　　主要的使用流程就是输入文字，然后设置一些动画的属性，还有画笔特效，最后启动就行了。

### Gradle

```
compile 'com.yanzhikai:TextPathView:0.0.2'
```

### 使用方法
　　TextPathView分为两种，一种是每个笔画按顺序刻画的SyncTextPathView，一种是每个笔画同时刻画的AsyncTextPathView，使用方法都是一样，在xml里面配置属性，然后直接在java里面调用startAnimation()方法就行了，具体的可以看例子和demo。下面是一个简单的例子：

xml里面：

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

java里面使用：

```
        atpv1 = findViewById(R.id.atpv_1);
        stpv_2017 = findViewById(R.id.stpv_2017);

        //从无到显示
        atpv1.startAnimation(0,1);
        //从显示到消失
        stpv_2017.startAnimation(1,0);
```

还可以通过控制进度，来控制TextPathView显示,这里用SeekBar：

```
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                atpv1.drawPath(progress / 1000f);
                stpv_2017.drawPath(progress / 1000f);

            }
        }
```

### 属性

|**属性名称**|**意义**|**类型**|**默认值**|
|--|--|:--:|:--:|
|textSize      | 文字的大小size     | integer| 108 |
|text      | 文字的具体内容     | String| Test|
|duration | 动画的持续时间，单位ms   | integer| 10000|
|showPainter | 在动画执行的时候是否执行画笔特效  | boolean| false|
|textStrokeWidth | 文字刻画的线条粗细     | dimension| 5px|
|textStrokeColor | 文字刻画的线条颜色   | color| Color.black|
|paintStrokeWidth | 画笔特效刻画的线条粗细    | dimension| 3px|
|paintStrokeColor | 画笔特效刻画的线条颜色   | color| Color.black|
|autoStart| 是否加载完后自动启动动画    | boolean| false|
|showInStart| 是否一开始就把文字全部显示    | boolean| false|
|textInCenter| 是否让文字内容处于控件中心    | boolean| false|

### 方法

#### 画笔特效

```
    //设置画笔特效
    public void setTextPainter(SyncTextPainter listener);
```
　　因为绘画的原理不一样，画笔特效也分两种：

```
    public interface SyncTextPainter extends TextPainter {
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

    public interface AsyncTextPainter extends TextPainter{
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
　　看名字就知道是对应哪一个了，想要自定义画笔特效的话就可以实现上面之中的一个或者两个接口来自己画啦。
　　另外，还有里面已经自带了3种画笔特效，可供参考和使用（关于这些画笔特效的实现，后面的将要写原理解析会讲，正在写）：

```

//箭头画笔特效，根据传入的当前点与上一个点之间的速度方向，来调整箭头方向
public class ArrowPainter implements SyncTextPathView.SyncTextPainter{}

//一支笔的画笔特效，就是在绘画点旁边画多一支笔
public class PenPainter implements SyncTextPathView.SyncTextPainter,AsyncTextPathView.AsyncTextPainter {}

//火花特效，根据箭头引申变化而来，根据当前点与上一个点算出的速度方向来控制火花的方向
public class FireworksPainter implements SyncTextPathView.SyncTextPainter{}

```

　　由上面可见，因为烟花和箭头画笔特效都需要记录上一个点的位置，所以只适合按顺序绘画的SyncTextPathView，而PenPainter就适合两种TextPathView。仔细看它的代码的话，会发现画起来都是很简单的哦。

#### 动画监听

```
    //设置自定义动画监听
    public void setAnimatorListener(TextPathAnimatorListener animatorListener);
  
```
　　TextPathAnimatorListener是实现了AnimatorListener接口的类，继承它的时候注意不要删掉super父类方法，因为里面可能有一些操作。


#### 画笔获取

```
    //获取绘画文字的画笔
    public Paint getDrawPaint() {
        return mDrawPaint;
    }

    //获取绘画画笔特效的画笔
    public Paint getPaint() {
        return mPaint;
    }
```

#### 控制绘画

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
```

#### 其他

```
    //设置文字内容
    public void setText(String text)；

    //清除画面
    public void clear();

    //设置能否显示画笔效果
    public void setShowPainter(boolean showPainter);

```

## 更新
　　后续将会往下面的方向努力：
 - 写出实现原理介绍博客（估计过年前写完，今天先溜回老家了）
 - 文字上色，目前只是画出轮廓，强行让画笔设置为填充会导致变成一坨，最近好像想到思路
 - 更多的特效，更多的动画，如果有什么想法和建议的欢迎issue提出来一起探讨。
 - 更好的性能，目前单个TextPathView在模拟器上运行动画时是不卡的，多个就有些卡顿了，在性能较好的真机多个也是没问题的，这个性能方面目前还没头绪。


## 开源协议
　　TextPathView遵循MIT协议。

## 关于作者
 > id：炎之铠

 > 炎之铠的邮箱：yanzhikai_yjk@qq.com

 > CSDN：http://blog.csdn.net/totond






