package com.example.flowLayout;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

	/**
	 * 在布局文件中书写控件属性并使用了自定义的属性时调用
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * 在布局文件里书写控件属性切未使用自定义属性时，会调用该方法，一遍情况下都会调用此方法
	 * 
	 * @param context
	 * @param attrs属性集
	 */
	public FlowLayout(Context context, AttributeSet attrs) {
		// 调用三个参数的构造方法
		this(context, attrs, 0);
	}

	/**
	 * 当new一个控件时，传入上下文时调用此方法
	 * 
	 * @param context
	 */
	public FlowLayout(Context context) {
		// 调用两个参数的构造方法
		this(context, null);
	}

	/**
	 * 
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

		// wrap_content
		int width = 0;
		int height = 0;
		// record height and widht of each line
		int lineHeight = 0;
		int lineWidth = 0;

		// get count of iner widget
		int cCount = getChildCount();

		for (int i = 0; i < cCount; i++) {
			View child = getChildAt(i);
			// measure height and width of sub view
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			// get LayoutParams
			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();
			// the width of sub View has taken up
			int childWidth = child.getMeasuredWidth() + lp.leftMargin
					+ lp.rightMargin;
			// height of sub View has taken up
			int childHeight = child.getMeasuredHeight() + lp.topMargin
					+ lp.bottomMargin;
			/*
			 * if widht of all sub view int cutent line is biger than
			 * lineWidth,then ,get another line
			 */
			if (lineWidth + childWidth > sizeWidth - getPaddingLeft()
					- getPaddingRight()) {

				// comparing to get the max width
				width = Math.max(width, lineWidth);
				// reset linewidth
				lineWidth = childWidth;
				// record lineheight，flowlayout 的高度等于加上新一行的高度
				height += lineHeight;
				// reset lineheight
				lineHeight = childHeight;
			} else {/* if not */
				// overlay
				lineWidth += childWidth;
				// get max
				lineHeight = Math.max(lineHeight, childHeight);
			}
			/* get to the last widget */
			if (i == cCount - 1) {
				width = Math.max(lineWidth, width);
				height += lineHeight;
			}

		}
		Log.e("TAG", "sizeWidth" + sizeWidth);
		Log.e("TAG", "sizeHeight" + sizeHeight);

		setMeasuredDimension(
				// if curent mode is exactly then use parameters from parent
				// view,else use parameters by we Measured
				modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width
						+ getPaddingLeft() + getPaddingRight(),
				modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height
						+ getPaddingTop() + getPaddingBottom()//
		);

	}

	/**
	 * 存储所有的view，以行的形式存储
	 */
	private List<List<View>> mAllViews = new ArrayList<List<View>>();
	/**
	 * 每一行的高度
	 */
	private List<Integer> mLineHeight = new ArrayList<Integer>();

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// clear two paramter;
		mAllViews.clear();
		mLineHeight.clear();
		// 当前viewGroup的宽度
		int width = getWidth();
		int lineWidth = 0;
		int lineHeight = 0;

		List<View> lineViews = new ArrayList<View>();

		int cCount = getChildCount();
		for (int i = 0; i < cCount; i++) {
			View child = getChildAt(i);
			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();

			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();
			/* if need get new line judge by current linewidth */
			if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width
					- getPaddingLeft() - getPaddingRight()) {
				// record lineHeight
				mLineHeight.add(lineHeight);
				// record views in curent line
				mAllViews.add(lineViews);

				// reset
				lineWidth = 0;
				lineHeight = childHeight + lp.topMargin + lp.bottomMargin;

				// reset listviews
				lineViews = new ArrayList<View>();

			}
			/* if don`t need */
			lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
			lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
					+ lp.bottomMargin);
			lineViews.add(child);
		}// for end
		/* the final line */
		mLineHeight.add(lineHeight);
		mAllViews.add(lineViews);

		// 设置sub view 的位置
		int left = getPaddingLeft();
		int top = getPaddingRight();
		// line

		int lineNum = mAllViews.size();
		/* for cycle to get each line */
		for (int i = 0; i < lineNum; i++) {
			// all view in current line
			lineViews = mAllViews.get(i);
			lineHeight = mLineHeight.get(i);
			/* for cycle to get each view in each line 并为其布局 */
			for (int j = 0; j < lineViews.size(); j++) {
				View child = lineViews.get(j);
				// 判断child的状态
				if (child.getVisibility() == View.GONE) {
					continue;
				}
				MarginLayoutParams lp = (MarginLayoutParams) child
						.getLayoutParams();
				// eg: lc = leftchild
				int lc = left + lp.leftMargin;
				int tc = top + lp.topMargin;
				int rc = lc + child.getMeasuredWidth();
				int bc = tc + child.getMeasuredHeight();
				// 为sub view进行布局
				child.layout(lc, tc, rc, bc);

				left += child.getMeasuredWidth() + lp.leftMargin
						+ lp.rightMargin;

			}

			// change left position for each sub view every set ,witch means
			// only left position is different in each line
			left = getPaddingLeft();
			top += lineHeight;
		}

	}

	/**
	 * 与当前ViewGroup对应的LayoutParamas
	 */
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		// TODO Auto-generated method stub
		return new MarginLayoutParams(getContext(), attrs);
	}

}
