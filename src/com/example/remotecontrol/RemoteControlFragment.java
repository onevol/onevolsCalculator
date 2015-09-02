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
	int step = 0;//��ʼ�Ƿ���������,�����˼������ֵ�״̬
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_remote_control, parent, false);
		
		mSelectedTextView = (TextView)v.findViewById(R.id.fragment_remote_control_selectedTextView);
		mWorkingTextView = (TextView)v.findViewById(R.id.fragment_remote_control_workingTextView);
		mSelectedTextView.setMovementMethod(new ScrollingMovementMethod());//�ɹ���
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
					if(isClear){//����������,���¼�������������
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
						if(isClear){//����������,���¼�������������
							str = "";
							mWorkingTextView.setText("");
//							mSelectedTextView.setText("");
						}
						if(str.equals("")){//���û��������,�Ͱ���,��0.����ʼ����
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
						if(isClear && mSelectedTextView.getText().toString() != null){//����������,ֱ�ӵ���������,��ʹ�øý��
	//						mWorkingTextView.setText("");
							str = mSelectedTextView.getText().toString();
						}
						if(str.equals("")){//���û�������־Ͱ��������,��һ�����ְ�0��ʼ����
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
					if(step == 0)//ֱ������һ�����ֻ������κ�����ʱ��return
						return;
					if(isClear){//����������,�ٴε�����ں�����
						str = "";
						mWorkingTextView.setText("");
						mSelectedTextView.setText("");
					} 
					else if(chooseSign){//���û���ڶ�������,�Ͱ��Ⱥ�,û��Ӧ
					}else{//һ������������
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
//	    int space = exp.indexOf(' ');//���������ո�λ��
//        String s1 = exp.substring(0, space);//s1���ڱ����һ��������
//        String op = exp.substring(space + 1, space + 2);//op���ڱ��������
//        String s2 = exp.substring(space + 3);//s2���ڱ���ڶ���������
//        double arg1 = Double.parseDouble(s1);//����������stringת��ΪSingle
//        double arg2 = Double.parseDouble(s2);
//        if(op.equals("��")){
//        	 r = arg1 + arg2;
//        }else if(op.equals("��")){
//        	r = arg1 - arg2;
//        }else if(op.equals("��")){
//        	 r = arg1 * arg2;
//        }else if(op.equals("��")){
//        	 if (arg2 == 0)
//             {
//                r=0;
//             }
//             else
//             {
//                 r = arg1 / arg2;
//             }
//        }       
//        if(!s1.contains(".") && !s2.contains(".") && !op.equals("��")){
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
	 *            �������ʽ
	 * @return ���ݱ��ʽ���ؽ��
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
	 *            �������ʽ
	 * @return ��Ϊ���������,ȫ����Ҫ��С������.
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
