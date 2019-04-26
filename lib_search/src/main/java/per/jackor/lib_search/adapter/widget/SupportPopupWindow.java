package per.jackor.lib_search.adapter.widget;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by Jackor on 2019/3/13.
 * Email: jackor.liao@foxmail.com
 * Description:
 */
public class SupportPopupWindow extends PopupWindow {

    public SupportPopupWindow(View contentView, int width, int height){
        super(contentView,width,height);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if(Build.VERSION.SDK_INT == 24 && anchor!=null) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h=anchor.getResources().getDisplayMetrics().
                    heightPixels -rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);

    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        if(Build.VERSION.SDK_INT == 24 && anchor!=null) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h=anchor.getResources().getDisplayMetrics().
                    heightPixels -rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor, xoff, yoff);
    }
}
