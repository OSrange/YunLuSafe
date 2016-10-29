package cn.edu.csu.bbs.yunlusafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Alan on 2016/9/12.
 */

/**
 * 可自动获取焦点
 */
public class FocusTextView extends TextView {

//    使用在通过Java代码创建控件
    public FocusTextView(Context context) {
        super(context);
    }

//    由系统调用（带属性+上下文环境构造方法）
    public FocusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    由系统调用（带属性+上下文环境构造方法+布局文件中定义样式文件构造方法）
    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    重写获取焦点的方法
    @Override
    public boolean isFocused() {
        return true;
    }
}
