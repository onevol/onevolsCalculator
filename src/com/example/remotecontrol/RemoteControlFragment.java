package com.example.remotecontrol;


import java.util.Arrays;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import bsh.EvalError;
import bsh.Interpreter;

public class RemoteControlFragment extends Fragment {

	private TextView mSelectedTextView;
	private TextView mWorkingTextView;
	boolean isClear = false;
	boolean chooseSign = false;
	boolean dotSign = false;
	int step = 0;//开始是否输入数字,输入了几个数字的状态
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_remote_control, parent, false);
		
		mSelectedTextView = (TextView)v.findViewById(R.id.fragment_remote_control_selectedTextView);
		mWorkingTextView = (TextView)v.findViewById(R.id.fragment_remote_control_workingTextView);
		mSelectedTextView.setMovementMethod(new ScrollingMovementMethod());//可滚动
		mWorkingTextView.setMovementMethod(new ScrollingMovementMethod());
		
		View.OnClickListener numberButtonListener = new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str = mWorkingTextView.getText().toString();
				switch (v.getId()) {
				case R.id.button_0:
				case R.id.button_1:
				case R.id.button_2:
				case R.id.button_3:
				case R.id.button_4:
				case R.id.button_5:
				case R.id.button_6:
				case R.id.button_7:
				case R.id.button_8:
				case R.id.button_9:
					if(isClear){//计算出结果后,重新计算其他的数字
						str = "";
						mWorkingTextView.setText("");
//						mSelectedTextView.setText("");
					}
					mWorkingTextView.setText(str + ((Button) v).getText());
					isClear = false;
					chooseSign = false;
					break;
				case R.id.button_dot:
					if(!dotSign){
						if(isClear){//计算出结果后,重新计算其他的数字
							str = "";
							mWorkingTextView.setText("");
//							mSelectedTextView.setText("");
						}
						if(str.equals("")){//如果没输入数字,就按点,按0.几开始计算
							str = "0";
						}
						mWorkingTextView.setText(str + ((Button) v).getText());
						isClear = false;
						chooseSign = false;
						dotSign = true;
					}
					break;
				case R.id.button_add:
				case R.id.button_minus:
				case R.id.button_multiply:
				case R.id.button_divide:
					if(chooseSign){
						mWorkingTextView.setText(str.substring(0, str.length() - 1));
						mWorkingTextView.setText(str.substring(0, str.length() - 1) +((Button) v).getText());
						return;
					}else{
						if(isClear && mSelectedTextView.getText().toString() != null){//计算出结果后,直接点击运算符号,会使用该结果
	//						mWorkingTextView.setText("");
							str = mSelectedTextView.getText().toString();
						}
						if(str.equals("")){//如果没输入数字就按运算符号,第一个数字按0开始计算
							str = "0";
						}
						mWorkingTextView.setText(str +((Button) v).getText());
						isClear = false;
						chooseSign = true;
						dotSign = false;
						step=1;
					}
					break;
				case R.id.button_equal:
					if(step == 0)//直接输入一个数字或不输入任何数字时，return
						return;
					if(isClear){//计算出结果后,再次点击等于号清零
						str = "";
						mWorkingTextView.setText("");
						mSelectedTextView.setText("");
					} 
					else if(chooseSign){//如果没按第二个数字,就按等号,没反应
					}else{//一切正常计算结果
						getResult();
						isClear = true;
						chooseSign = false;
						dotSign = false;
					}
					break;
				}
			}
		};
		
		TableRow row1 = (TableRow)v.findViewById(R.id.button_row1);
		for(int j = 0; j < row1.getChildCount(); j++){
			Button button = (Button)row1.getChildAt(j);
			button.setOnClickListener(numberButtonListener);
		}
		TableRow row2 = (TableRow)v.findViewById(R.id.button_row2);
		for(int j = 0; j < row2.getChildCount(); j++){
			Button button = (Button)row2.getChildAt(j);
			button.setOnClickListener(numberButtonListener);
		}
		TableRow row3 = (TableRow)v.findViewById(R.id.button_row3);
		for(int j = 0; j < row3.getChildCount(); j++){
			Button button = (Button)row3.getChildAt(j);
			button.setOnClickListener(numberButtonListener);
		}
		Button button_dot = (Button)v.findViewById(R.id.button_dot);
		button_dot.setOnClickListener(numberButtonListener);
		
		Button button_0 = (Button)v.findViewById(R.id.button_0);
		button_0.setOnClickListener(numberButtonListener);
		
		Button button_equal = (Button)v.findViewById(R.id.button_equal);
		button_equal.setOnClickListener(numberButtonListener);
		
		Button button_divide = (Button)v.findViewById(R.id.button_divide);
		button_divide.setOnClickListener(numberButtonListener);
		return v;
	}
	private void getResult() {
		String exp = mWorkingTextView.getText().toString();
//		double r = 0;
//	    int space = exp.indexOf(' ');//用于搜索空格位置
//        String s1 = exp.substring(0, space);//s1用于保存第一个运算数
//        String op = exp.substring(space + 1, space + 2);//op用于保存运算符
//        String s2 = exp.substring(space + 3);//s2用于保存第二个运算数
//        double arg1 = Double.parseDouble(s1);//将运算数从string转换为Single
//        double arg2 = Double.parseDouble(s2);
//        if(op.equals("＋")){
//        	 r = arg1 + arg2;
//        }else if(op.equals("－")){
//        	r = arg1 - arg2;
//        }else if(op.equals("×")){
//        	 r = arg1 * arg2;
//        }else if(op.equals("÷")){
//        	 if (arg2 == 0)
//             {
//                r=0;
//             }
//             else
//             {
//                 r = arg1 / arg2;
//             }
//        }       
//        if(!s1.contains(".") && !s2.contains(".") && !op.equals("÷")){
//        	int result = (int)r;
//        	mSelectedTextView.setText(result+"");
//        }else{
//        	mSelectedTextView.setText(r+"");
//        }
		String r = getRs(exp);
		mSelectedTextView.setText(r);
	}

	/***
	 * @param exp
	 *            算数表达式
	 * @return 根据表达式返回结果
	 */
	private String getRs(String exp) {
		Interpreter bsh = new Interpreter();
		Number result = null;
		try {
			exp = filterExp(exp);
			result = (Number) bsh.eval(exp);
		} catch (EvalError e) {
			e.printStackTrace();
			return "0";
		}
		return result.doubleValue() + "";
	}

	/**
	 * @param exp
	 *            算数表达式
	 * @return 因为计算过程中,全程需要有小数参与.
	 */
	private String filterExp(String exp) {
		String num[] = exp.split("");
		String temp = null;
		double dtemp = 0 ;
		String str_temp="";
		for (int i = 0; i < num.length; i++) {
			temp = num[i];
			
			if (temp.equals("+") || temp.equals("-") || temp.equals("*")
					|| temp.equals("/")) {
				if(dtemp==0){
					try{
						dtemp=Double.parseDouble(str_temp);
						}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				
				str_temp="";
			}else{
				str_temp=str_temp+temp;
			}
		}

		int begin = 0, end = 0;
		for (int i = 1; i < num.length; i++) {
			temp = num[i];
			if (temp.matches("[+-/()*]")) {
				if (temp.equals("."))
					continue;
				end = i - 1;
				temp = exp.substring(begin, end);
				if (temp.trim().length() > 0 && temp.indexOf(".") < 0)
					num[i - 1] = num[i - 1] + ".0";
				begin = end + 1;
			}
		}
		return Arrays.toString(num).replaceAll("[\\[\\], ]", "");
	}

}
