package com.example.guess.contribution;

import android.graphics.Bitmap;

/** 
 * @author Infiniters 
 * @version 
 * Created by Jonathan.Xu E-mail: xuchi.int@gmail.com
 * on��2013-8-5 ����12:03:34 
 * Specification:  ���̴����߳�
 * 
 */

public class YangkeThread extends Thread {

	WorldPage activity;
	int percentage = 1;
	private static final int PROCESS_SUCCESS = 0;//��ȡͼƬ�ɹ��ı�ʶ
	
	public YangkeThread(WorldPage activity){
		this.activity = activity;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
	    // TODO �����ఴť��ͣ��Ӧ��
		
		
		ProcessFactory.IniGauss_2(ProcessFactory.gauss_radius);  //��ʼ����˹����
	
		for(percentage=1; percentage<=10; percentage++){
			activity.myHandler.sendEmptyMessage(percentage);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		Bitmap bmpGrayscale=ProcessFactory.toGray2(activity.imageBmp);  //ת��Ϊ�Ҷ�ͼ
	
		for(percentage=11;percentage<=30;percentage++){
			activity.myHandler.sendEmptyMessage(percentage);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Bitmap bmpInverse=ProcessFactory.toInverse(bmpGrayscale); //��ɫ
		
		for(percentage=31;percentage<=50;percentage++){
			activity.myHandler.sendEmptyMessage(percentage);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Bitmap bmpGauss=ProcessFactory.toGauss(bmpInverse); //��˹ģ��
		
		for(percentage=51;percentage<=60;percentage++){
			activity.myHandler.sendEmptyMessage(percentage);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Bitmap bmpColorDodge=ProcessFactory.toColorDodge(bmpGauss,bmpGrayscale);
		// TODO bmpColorDodge ͼ��Ϊ����ͼ
		
		for(percentage=61;percentage<=80;percentage++){
			activity.myHandler.sendEmptyMessage(percentage);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Bitmap bmpPapercut=ProcessFactory.toPapercut(bmpColorDodge);
		
		for(percentage=81;percentage<=90;percentage++){
			activity.myHandler.sendEmptyMessage(percentage);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Bitmap bmpYangke=ProcessFactory.toYangkeFilter(bmpPapercut);
		
		for(percentage=91;percentage<=100;percentage++){
			activity.myHandler.sendEmptyMessage(percentage);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		activity.myHandler.obtainMessage(PROCESS_SUCCESS,bmpYangke).sendToTarget();
		
	}

}
