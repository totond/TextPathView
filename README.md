# TextPathView


　　
![](https://i.imgur.com/l5o8XG5.gif)

 > [Go to the English README](https://github.com/totond/TextPathView/blob/master/README-en.md)


## 介绍
　　大家新年快乐，TextPathView是一个把文字转化为路径动画然后展现出来的自定义控件。效果如上图。

 > 这里有[原理解析！](https://juejin.im/post/5a9677b16fb9a063375765ad)

## 使用
　　主要的使用流程就是输入文字，然后设置一些动画的属性，还有画笔特效，最后启动就行了。想要自己控制绘画的进度也可以，详情见下面。

### Gradle

```
compile 'com.yanzhikai:TextPathView:0.0.6'
```

 > minSdkVersion 16

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
|showPainterActually**(新增) **| 在所有时候是否展示画笔特效| boolean| false|
|textStrokeWidth | 文字刻画的线条粗细     | dimension| 5px|
|textStrokeColor | 文字刻画的线条颜色   | color| Color.black|
|paintStrokeWidth | 画笔特效刻画的线条粗细    | dimension| 3px|
|paintStrokeColor | 画笔特效刻画的线条颜色   | color| Color.black|
|autoStart| 是否加载完后自动启动动画    | boolean| false|
|showInStart| 是否一开始就把文字全部显示    | boolean| false|
|textInCenter| 是否让文字内容处于控件中心    | boolean| false|
|repeat**(新增) **| 是否重复播放动画，重复类型| enum | NONE|

|**repeat属性值**|**意义**|
|--|--|
|NONE|不重复播放|
|RESTART|动画从头重复播放|
|REVERSE|动画从尾重复播放|


 > PS:showPainterActually属性，由于动画绘画完毕应该将画笔特效消失，所以每次执行完动画都会自动设置为false。因此最好用于使用非自带动画的时候。

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
　　另外，还有里面已经自带了3种画笔特效，可供参考和使用（关于这些画笔特效的实现，可以参考[原理解析](http://blog.csdn.net/totond/article/details/79375200)）：

```

//箭头画笔特效，根据传入的当前点与上一个点之间的速度方向，来调整箭头方向
public class ArrowPainter implements SyncTextPathView.SyncTextPainter{}

//一支笔的画笔特效，就是在绘画点旁边画多一支笔
public class PenPainter implements SyncTextPathView.SyncTextPainter,AsyncTextPathView.AsyncTextPainter {}

//火花特效，根据箭头引申变化而来，根据当前点与上一个点算出的速度方向来控制火花的方向
public class FireworksPainter implements SyncTextPathView.SyncTextPainter{}

```

　　由上面可见，因为烟花和箭头画笔特效都需要记录上一个点的位置，所以只适合按顺序绘画的SyncTextPathView，而PenPainter就适合两种TextPathView。仔细看它的代码的话，会发现画起来都是很简单的哦。

#### 自定义画笔特效
　　自定义画笔特效也是非常简单的，原理就是在当前绘画点上加上一个附加的Path，实现SyncTextPainter和AsyncTextPainter之中的一个或者两个接口，重写里面的`onDrawPaintPath(float x, float y, Path paintPath)`方法就行了，如下面这个：

```
        atpv2.setTextPainter(new AsyncTextPathView.AsyncTextPainter() {
            @Override
            public void onDrawPaintPath(float x, float y, Path paintPath) {
                paintPath.addCircle(x,y,3, Path.Direction.CCW);
            }
        });
```
![](https://i.imgur.com/fPeYF8f.gif)

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

#### 填充颜色

```
    //直接显示填充好颜色了的全部文字
    public void showFillColorText();
```
　　由于正在绘画的时候文字路径不是封闭的，填充颜色会变得很混乱，所以这里给出`showFillColorText()`来设置直接显示填充好颜色了的全部文字，一般可以在动画结束后文字完全显示后过渡填充，如Demo里面的：

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

#### 其他

```
    //设置文字内容
    public void setText(String text)；

    //清除画面
    public void clear();

    //设置动画时能否显示画笔效果
    public void setShowPainter(boolean showPainter)；

    //设置所有时候是否显示画笔效果,由于动画绘画完毕应该将画笔特效消失，所以每次执行完动画都会自动设置为false
    public void setCanShowPainter(boolean canShowPainter)；

```

## 更新

 - 2018/03/08 **version 0.0.5**:
     - 增加了`showFillColorText()`方法来设置直接显示填充好颜色了的全部文字。
     - 把TextPathAnimatorListener从TextPathView的内部类里面解放出来，之前使用太麻烦了。
     - 增加`showPainterActually`属性，设置所有时候是否显示画笔效果,由于动画绘画完毕应该将画笔特效消失，所以每次执行完动画都会自动将它设置为false。因此它用处就是在不使用自带Animator的时候显示画笔特效。

 - 2018/03/08 **version 0.0.6**:
     - 增加了`stop(), pause(), resume()`方法来控制动画。之前是觉得让使用者自己用Animator实现就好了，现在一位外国友人toanvc提交的PR封装好了，我稍作修改，不过后两者使用时API要大于等于19。
     - 增加了`repeat`属性，让动画支持重复播放，也是toanvc同学的PR。

![](https://i.imgur.com/qQ55UrW.gif)

 > Thanks for [toanvc](https://github.com/toanvc)'s contribution!



#### 后续将会往下面的方向努力：

 - 更多的特效，更多的动画，如果有什么想法和建议的欢迎issue提出来一起探讨。
 - 更多的输入，不单止局限于文字Path。
 - 更好的性能，目前单个TextPathView在模拟器上运行动画时是不卡的，多个就有一点点卡顿了，在性能较好的真机多个也是没问题的，这个性能方面目前还没头绪。

## 贡献代码
　　如果想为TextPathView的完善出一份力的同学，欢迎提交PR：
 - 首先创建一个分支branch。
 - 如果加入新的功能或者效果，请不要覆盖demo里面原来用于演示Activity代码，如FristActivity里面的实例，可以选择新增一个Activity做演示测试，或者不添加演示代码。
 - 如果修改某些功能或者代码，请附上合理的依据和想法。

## 开源协议
　　TextPathView遵循MIT协议。

## 关于作者
 > id：炎之铠

 > 炎之铠的邮箱：yanzhikai_yjk@qq.com

 > CSDN：http://blog.csdn.net/totond






