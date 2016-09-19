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
	 * �ڲ����ļ�����д�ؼ����Բ�ʹ�����Զ��������ʱ����
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/**
	 * �ڲ����ļ�����д�ؼ�������δʹ���Զ�������ʱ������ø÷�����һ������¶�����ô˷���
	 * 
	 * @param context
	 * @param attrs���Լ�
	 */
	public FlowLayout(Context context, AttributeSet attrs) {
		// �������������Ĺ��췽��
		this(context, attrs, 0);
	}

	/**
	 * ��newһ���ؼ�ʱ������������ʱ���ô˷���
	 * 
	 * @param context
	 */
	public FlowLayout(Context context) {
		// �������������Ĺ��췽��
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
				// record lineheight��flowlayout �ĸ߶ȵ��ڼ�����һ�еĸ߶�
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
	 * �洢���е�view�����е���ʽ�洢
	 */
	private List<List<View>> mAllViews = new ArrayList<List<View>>();
	/**
	 * ÿһ�еĸ߶�
	 */
	private List<Integer> mLineHeight = new ArrayList<Integer>();

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// clear two paramter;
		mAllViews.clear();
		mLineHeight.clear();
		// ��ǰviewGroup�Ŀ��
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

		// ����sub view ��λ��
		int left = getPaddingLeft();
		int top = getPaddingRight();
		// line

		int lineNum = mAllViews.size();
		/* for cycle to get each line */
		for (int i = 0; i < lineNum; i++) {
			// all view in current line
			lineViews = mAllViews.get(i);
			lineHeight = mLineHeight.get(i);
			/* for cycle to get each view in each line ��Ϊ�䲼�� */
			for (int j = 0; j < lineViews.size(); j++) {
				View child = lineViews.get(j);
				// �ж�child��״̬
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
				// Ϊsub view���в���
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
	 * �뵱ǰViewGroup��Ӧ��LayoutParamas
	 */
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		// TODO Auto-generated method stub
		return new MarginLayoutParams(getContext(), attrs);
	}

}
