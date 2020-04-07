## Button

### Button的几种形式

带有文字，或带有图片，或既有文字又有图片  
![三种button](https://img-blog.csdnimg.cn/20200403155014372.png)  

### 三种Button的实现（XML）

- 仅文字，即使用Button：  

```xml
<Button
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/button_text"
    ... />
```

- 仅图片，即使用ImageButton：

```xml
<ImageButton
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@drawable/button_icon"
    android:contentDescription="@string/button_icon_desc"
    ... />
```

- 图片加文字，使用Button控件，定义drawableLeft（图标在左侧，drawableRight图标在右侧）属性：

```xml
<Button
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/button_text"
    android:drawableLeft="@drawable/button_icon"
    ... />
```

### Button响应点击事件的两种方式

- XML布局添加onClick属性：  

```xml
<Button
    android:id="@+id/button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:onClick="sendMessage"
    android:text="@string/button_send" />
```

onClick的值为响应点击事件的方法的名字，该方法在Activity的定义中实现：  

```java
public void sendMessage(View view) {
    // Do something in response to button click
}
```

上方的两段代码会使用户点击button时，调用sendMessage方法  

要注意定义方法时需满足以下条件才能与```android:onClick```属性兼容：  

  1. 公开访问（public)
  2. 返回类型为void
  3. 只有一个```View```参数  

- 使用监听器（onClickListener）：

```java
// java实现
Button button = findViewById(R.id.button_send);
button.setOnClickListener(new View.OnClickListener() {
    public void onClick(View v) {
        // Do something in response to button click
    }
});
```

使用onClickListener实现事件响应有多种方法，这里不再赘述

### 常用的Listener监听器

- View.OnClickListener: onClick()  
```void onClick(View v)```  
监听到View点击事件时触发onClick方法  
参数v为触发该监听器的view，下同

- View.OnLongClickListener: onLongClickListener()  
```boolean onLongClick(View v)```  
监听到View长按事件时触发onClick方法  
返回值为布尔类型，表示当前手势是否被该方法消耗，false表示不消耗，反之则消耗

- View.OnTouchListener: onTouch()  
```boolean onTouch(View v, MotionEvent event)```  
监听到View点击事件时触发onTouch方法  
返回值与onLongClick作用类似，例如一个按钮绑定了一个onClickListener和一个onTouchListener，如果onTouch返回值为true，点击按钮后，该动作会先出发onTouch方法，而后消耗该手势，导致onClick不被触发；若返回值设为false，则先执行onTouch后执行onClick  
参数event记录了当前点事件的各个属性，包括坐标、按下、移动、抬起等  
该方法与onClick的区别在于，onTouch可以细分为按下，移动，抬起三个动作，操作性更强

- View.OnFocusChangeListener: onFocusChange()  
```void onFocusChange(View view, boolean hasFocus)```  
当View获取焦点改变时触发onFocusChange方法，获取焦点和失去焦点都会触发该方法  
参数hasFocus表示是否获得焦点

- View.OnKeyListener(): onKey()  
```boolean onKey(View v, int keyCode, KeyEvent event)```  
监听软键盘  
参数keyCode为软键盘上的按键码，event封装了键盘事件的详细信息
