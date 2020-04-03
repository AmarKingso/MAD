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

- 使用侦听器（onClickListener）：   
```java
// java实现
Button button = (Button) findViewById(R.id.button_send);
button.setOnClickListener(new View.OnClickListener() {
    public void onClick(View v) {
        // Do something in response to button click
    }
});
```
使用onClickListen实现事件响应有多种方法，这里不再赘述
