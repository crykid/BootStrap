package com.example.bootstrap;

import com.example.flowLayout.FlowLayout;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.TextView;
/**
 *���ľ���FlowLayout�࣬�Զ���view����������˵Ӧ�ÿ��������ٲ����������һ�û��ʵ��
 * @author d1
 *
 */
public class MainActivity extends Activity {
	
	private String[] values = new String[] { "hello", "china", "earth",
			"cat is qute", "apple trees", "hello", "china", "earth",
			"cat is qute", "apple trees", "hello", "china", "earth",
			"cat is qute", "apple trees" };
	private FlowLayout mflowLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	@SuppressLint("InlinedApi")
	private void initView() {
		// TODO Auto-generated method stub
		mflowLayout = (FlowLayout) findViewById(R.id.flayout);
		/*����button�Ŀ��*/
//		MarginLayoutParams lp = new MarginLayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LayoutInflater mInflater = LayoutInflater.from(this);
		for (int i = 0; i < values.length; i++) {
			//�������ӿ�ߣ�Ĭ����ÿ��view match_parent
			TextView tview = (TextView) mInflater.inflate(R.layout.textview, mflowLayout,false);
			tview.setText(values[i]);
//			mflowLayout.addView(tview,lp);
			mflowLayout.addView(tview);
		}
	}

}
