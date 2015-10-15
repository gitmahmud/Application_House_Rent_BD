package com.experiment.comp.application_house_rent_bd.Others;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by comp on 7/15/2015.
 */
public class CustomImageView extends ImageView{

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec,
                                       int heightMeasureSpec) {
//   let the default measuring occur, then force the desired aspect ratio
//   on the view (not the drawable).
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
//force a 4:3 aspect ratio
        int height = Math.round(width * .75f);
        setMeasuredDimension(width, height);
    }
}
