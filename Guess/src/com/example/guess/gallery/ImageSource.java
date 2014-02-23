package com.example.guess.gallery;

import java.io.IOException;
import android.content.Context;

/** 
 * @author Infiniters 
 * @version 
 * Created by Jonathan.Xu E-mail: xuchi.int@gmail.com
 * on��2013-4-11 ����2:39:18 
 * Specification:  ͼ��Դ��,ͼ�������drawable,assets�ļ�����
 * 
 */

public class ImageSource  {
	
	Context mContext;
	//����assets��
	 String[] imageMyPapercutName;
	 String[] imageMyHandpaintName; 
	 String[] imagePapercutArtsName; 
	 String[] imageMyScrawlName; 

	/*����drawable
	 static Integer[] myPapercutIds = {
	    		R.drawable.btn_shuangxi,R.drawable.btn_xique,R.drawable.hello_papercut,R.drawable.first_show
	            };
	 */
	 //���캯��
	 public ImageSource(Context context){
		this.mContext = context;
		 try {
			 //�õ������ļ����е��ļ���
			imageMyPapercutName = mContext.getResources().getAssets().list("artshow/My Papercut");
			imageMyHandpaintName = mContext.getResources().getAssets().list("artshow/My Handpaint");
			imagePapercutArtsName = mContext.getResources().getAssets().list("artshow/Papercut Arts");
			imageMyScrawlName = mContext.getResources().getAssets().list("artshow/My Scrawl");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
}
