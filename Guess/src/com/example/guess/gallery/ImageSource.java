package com.example.guess.gallery;

import java.io.IOException;
import android.content.Context;

/** 
 * @author Infiniters 
 * @version 
 * Created by Jonathan.Xu E-mail: xuchi.int@gmail.com
 * on：2013-4-11 下午2:39:18 
 * Specification:  图像源类,图像可来自drawable,assets文件夹中
 * 
 */

public class ImageSource  {
	
	Context mContext;
	//来自assets中
	 String[] imageMyPapercutName;
	 String[] imageMyHandpaintName; 
	 String[] imagePapercutArtsName; 
	 String[] imageMyScrawlName; 

	/*来自drawable
	 static Integer[] myPapercutIds = {
	    		R.drawable.btn_shuangxi,R.drawable.btn_xique,R.drawable.hello_papercut,R.drawable.first_show
	            };
	 */
	 //构造函数
	 public ImageSource(Context context){
		this.mContext = context;
		 try {
			 //得到所有文件夹中的文件名
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
