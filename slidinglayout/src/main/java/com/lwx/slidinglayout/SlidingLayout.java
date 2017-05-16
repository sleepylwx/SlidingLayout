package com.lwx.slidinglayout;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 36249 on 2017/3/26.
 */

public class SlidingLayout extends ViewGroup {


    private int layoutGravity;
    private int menuSide;

    private MenuOpenListener menuOpenListener;
    private MenuCloseListener menuCloseListener;
    public SlidingLayout(Context context) {

        super(context);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StrengthenSlidingLayout, defStyleAttr, 0);

        layoutGravity = typedArray.getInt(R.styleable.StrengthenSlidingLayout_layout_gravity, 0);
        menuSide = typedArray.getInt(R.styleable.StrengthenSlidingLayout_menuSide, 0);

        initial();
//        secondStartXOffSet = typedArray.getDimensionPixelSize(R.styleable.StrengthenSlidingLayout_secondStartXOffSet,
//                0);
//        menuOutSideClickMissing = typedArray.getBoolean(R.styleable.StrengthenSlidingLayout_menuClickOutSideMissing,
//                true);//默认点击取消
//        menuMonopolize = typedArray.getBoolean(R.styleable.StrengthenSlidingLayout_menuMonopolize,
//                true);//默认菜单独占焦点
//        if(menuOutSideClickMissing){
//
//            menuMonopolize = true;
//        }
//        menuLinkageContent = typedArray.getBoolean(R.styleable.StrengthenSlidingLayout_menuLinkageContent,
//                false);
//        menuStartMoveScrollGravity = typedArray.getInt(R.styleable.StrengthenSlidingLayout_menuStartMoveScrollGravity,
//                0);
//        menuStartMoveScrollOffSet = typedArray.getDimensionPixelSize(R.styleable.StrengthenSlidingLayout_menuStartMoveScrollOffSet,
//                0);

        typedArray.recycle();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = widthSize;
        int height = heightSize;


        switch (widthMode) {


            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;

            case MeasureSpec.AT_MOST:
                int firstWidth = getChildAt(0).getMeasuredWidth();
                MarginLayoutParams lp = (MarginLayoutParams) getChildAt(0).getLayoutParams();

                int width1 = firstWidth + lp.leftMargin + lp.rightMargin + getPaddingLeft()
                        + getPaddingRight();
                width = width1 > widthSize ? widthSize :
                        width1;
                break;

            case MeasureSpec.UNSPECIFIED:
                break;

        }

        switch (heightMode) {

            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;


            case MeasureSpec.AT_MOST:
                int firstHeight = getChildAt(0).getMeasuredHeight();
                MarginLayoutParams lp = (MarginLayoutParams) getChildAt(0).getLayoutParams();
                int height1 = firstHeight + lp.topMargin + lp.bottomMargin + getPaddingTop() +
                        getPaddingBottom();
                height = height1 > heightSize ? heightSize : height1;
                break;


            case MeasureSpec.UNSPECIFIED:
                break;


        }


        setMeasuredDimension(width, height);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {


        View first = getChildAt(0);
        int firstL = ((MarginLayoutParams) first.getLayoutParams()).leftMargin + getPaddingLeft();
        int firstT = ((MarginLayoutParams) first.getLayoutParams()).topMargin + getPaddingTop();
        int firstR = firstL + first.getMeasuredWidth();
        int firstB = firstT + first.getMeasuredHeight();
        first.layout(firstL, firstT, firstR, firstB);
        //Log.d("PLM",first.getWidth() + " " + first.getMeasuredWidth() + " " + first.getX());

        View second = getChildAt(1);
        MarginLayoutParams secondLp = ((MarginLayoutParams) second.getLayoutParams());
        Log.d("WSX", getWidth() + "  " + getMeasuredWidth());

        int secondL;
        int secondR;
        int secondT;
        int secondB;
        if (menuSide == 0) {

            secondL = -second.getMeasuredWidth();
            secondR = 0;

        } else {

            secondL = getWidth();
            secondR = secondL + second.getMeasuredWidth();
        }


        secondT = 0;
        secondB = secondT + second.getMeasuredHeight();
        switch (layoutGravity) {


            case 0:
                secondT = 0 + secondLp.topMargin;
                secondB = secondT + +second.getMeasuredHeight();
                break;

            case 1:
                secondT = getHeight() / 2 + secondLp.topMargin;
                secondB = secondT + second.getMeasuredHeight();
                break;

            case 2:
                secondB = getHeight();
                secondT = secondB - second.getMeasuredHeight();

        }

        second.layout(secondL, secondT, secondR, secondB);

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {

        return new MarginLayoutParams(getContext(), attrs);
    }


    @Override
    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        final int size = getChildCount();

        for (int i = 0; i < size; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChild(child, i, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }


    protected void measureChild(View child, int index, int parentWidthMeasureSpec,
                                int parentHeightMeasureSpec) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                index == 0 ? getPaddingLeft() + getPaddingRight() : 0,
                lp.leftMargin + lp.rightMargin, lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                index == 0 ? getPaddingTop() + getPaddingBottom() : 0,
                lp.topMargin + lp.bottomMargin, lp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }


    public static int getChildMeasureSpec(int spec, int padding, int margin, int childDimension) {

        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);

        int size = Math.max(0, specSize - padding - margin);

        int resultSize = 0;
        int resultMode = 0;

        switch (specMode) {
            // Parent has imposed an exact size on us
            case MeasureSpec.EXACTLY:
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size. So be it.
                    resultSize = size;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;

            // Parent has imposed a maximum size on us
            case MeasureSpec.AT_MOST:
                if (childDimension >= 0) {
                    // Child wants a specific size... so be it
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size, but our size is not fixed.
                    // Constrain child to not be bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;

        }
        //noinspection ResourceType
        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }





    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        View menu = getChildAt(1);
        if(isMenuOpen){

            switch (action){

                case MotionEvent.ACTION_DOWN:

                    //菜单的矩形区域
                    Rect rect = new Rect(0,0,menu.getWidth(),menu.getHeight());
                    if(!rect.contains(x,y)){


                        return true;
                    }
                    break;

                case MotionEvent.ACTION_MOVE:


                    return true;
                
                case MotionEvent.ACTION_UP:

                    break;

                default:

                    break;
            }

        }
        else{


            switch (action) {

                case MotionEvent.ACTION_DOWN:

                    break;

                case MotionEvent.ACTION_MOVE:
                    if (needIntercept(x)) {


                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    break;

                default:

                    break;
            }

        }


        return false;
    }

    /**
     *
     *
     * @param x
     * @return
     * 滑动菜单的条件
     */
    private boolean needIntercept(float x){

        if(menuSide == 0){

            if(x < getWidth() / 4){


                return true;
            }
        }
        else{

            if(x > getWidth() / 4 * 3){

                return true;
            }
        }

        return false;
    }
    private int startPressX;
    private int startMenuX;
    private long startTime;

    private boolean isMenuOpen;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();

        if(!isMenuOpen && action == MotionEvent.ACTION_DOWN && !needIntercept(x)){

            return false;
        }

        View menu = getChildAt(1);

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                startPressX = x;
                startMenuX = (int) menu.getX();
                startTime = System.currentTimeMillis();

                break;

            case MotionEvent.ACTION_MOVE:

                if(startMenuX == -1 && startPressX == -1 && startTime == -1){

                    startPressX = x;
                    startMenuX = (int) menu.getX();
                    startTime = System.currentTimeMillis();

                }

                if(menuSide == 0){


                    if (startMenuX + x - startPressX > 0) {

                        menu.setX(0);
                    }
                    else {

                        menu.setX(startMenuX + x - startPressX);
                    }

                }
                else{



                    if(startMenuX + menu.getWidth() + x - startPressX < getWidth() ){

                        menu.setX(getWidth()-menu.getWidth());
                    }
                    else{

                         menu.setX(startMenuX + x - startPressX);
                    }
                }


                break;

            case MotionEvent.ACTION_UP:

                long curTime = System.currentTimeMillis();

                Log.d("ABC", "" + Math.abs((x - startPressX) / (curTime+1 - startTime)));


                if(menuSide == 0){

                    if (Math.abs((x - startPressX) / (curTime + 1 - startTime)) < 3) {


                        if (x - startPressX < 0 && Math.abs(x - startPressX) > menu.getWidth() / 6) {

                            //往回划并且 划过的距离大于侧滑栏宽度的1/6
                            closeMenu();

                        }
                        else if (x - startPressX > 0 && Math.abs(menu.getX()) > menu.getWidth() / 3) {

                            //往打开方向划，并且划过的距离小于侧滑栏宽度的2/3
                            closeMenu();
                        }

                        else {

                            openMenu(100);
                        }
                    }
                    else{

                        if (x - startPressX < 0) {

                            //快速往回划
                            closeMenu();
                        }
                        else if(x - startPressX >0){

                            openMenu(100);
                        }
                    }


                }
                else{

                    if (Math.abs((x - startPressX) / (curTime - startTime)) < 3){


                        if (x - startPressX > 0 && Math.abs(x - startPressX) > menu.getWidth() / 6) {

                            //往回划并且 划过的距离大于侧滑栏宽度的1/6
                            closeMenu();

                        }
                        else if (x - startPressX < 0 && Math.abs(menu.getX()) > (getWidth() - menu.getWidth() / 3 * 2)) {

                            //往打开方向划，并且划过的距离小于侧滑栏宽度的2/3
                            closeMenu();
                        }

                        else {

                            openMenu(100);
                        }

                    }
                    else{

                        if (x - startPressX > 0) {

                            //快速往回划
                            closeMenu();
                        }
                        else if(x - startPressX < 0){

                            openMenu(100);
                        }

                    }

                }


                initial();
                break;

            default:
                break;
        }

        //不考虑该View上层还有View的情况，即默认该View为根View
        return true;
    }

    private void initial(){

        startPressX = -1;
        startMenuX = -1;
        startTime = -1;

    }

    //滑动拉出菜单的条件
    //1.拉出幅度大于菜单宽度3/4
    //2.滑动速度大于某一值
    //其他条件下滑动停止后，菜单自动收回

    //滑动样式:
    //0(默认) 主view不动，menu从边缘开始滑出
    //1 主View跟随menu一起移动，menu从边缘开始滑出
    //2 主View跟随menu一起移动，menu从某一位置滑出
    //3  前三种情况下，在菜单没有完全滑出时，停止滑动菜单后，菜单不自动收回


    //菜单滑动过程中调用外部接口 CallBack

    public void closeMenu() {

        View menu = getChildAt(1);

        if(menuSide == 0){


            ObjectAnimator.ofFloat(menu, "X", -menu.getWidth())
                    .start();
        }
        else{

            ObjectAnimator.ofFloat(menu,"X",getWidth())
                    .start();
        }

        if(menuCloseListener != null){

            menuCloseListener.onMenuClose(menu);
        }

        isMenuOpen = false;
    }

    public void closeMenu(long duration) {

        View menu = getChildAt(1);

        if (duration > 0) {

            if(menuSide == 0){

                ObjectAnimator.ofFloat(menu, "X", -menu.getWidth())
                        .setDuration(duration)
                        .start();
            }
            else{

                ObjectAnimator.ofFloat(menu,"X",getWidth())
                        .setDuration(duration)
                        .start();
            }


            if(menuCloseListener != null){

                menuCloseListener.onMenuClose(menu);
            }

            isMenuOpen = false;
        }

    }

    public void openMenu() {

        View menu = getChildAt(1);

        if(menuSide == 0){

            ObjectAnimator.ofFloat(menu, "X", 0)
                    .start();
        }
        else{

            ObjectAnimator.ofFloat(menu, "X", getWidth() - menu.getWidth())
                    .start();
        }


        if(menuOpenListener != null){

            menuOpenListener.onMenuOpen(menu);
        }

        isMenuOpen = true;
    }

    public void openMenu(long duration) {

        View menu = getChildAt(1);
        if (duration > 0) {

            if(menuSide == 0){

                ObjectAnimator.ofFloat(menu, "X", 0)
                        .setDuration(duration)
                        .start();
            }
            else{

                ObjectAnimator.ofFloat(menu,"X",getWidth() - menu.getWidth())
                        .setDuration(duration)
                        .start();
            }


            if(menuOpenListener != null){

                menuOpenListener.onMenuOpen(menu);
            }

            isMenuOpen = true;
        }



    }

    public void setMenuOpenListener(MenuOpenListener listener){


        this.menuOpenListener = listener;
    }

    public void setMenuCloseListener(MenuCloseListener listener){

        this.menuCloseListener = listener;
    }


    public static interface MenuOpenListener{


        public void onMenuOpen(View menu);

    }

    public static interface MenuCloseListener{

        public void onMenuClose(View menu);
    }
}
