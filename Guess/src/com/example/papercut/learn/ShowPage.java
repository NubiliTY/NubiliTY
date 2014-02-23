package com.example.papercut.learn;

import java.io.IOException;
import java.io.InputStream;

import com.example.guess.MainPage;
import com.example.papercut.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

/** 
 * @author Infiniters 
 * @version 
 * Created by Jonathan.Xu E-mail: xuchi.int@gmail.com
 * on��2013-4-8 ����8:21:50 
 * Specification:  չʾ��ֽ����ҳ��
 * 
 */

public class ShowPage extends Activity {
	
	private ImageButton btnBack; 
	private ImageButton btnNext;
	private ImageView showboard;
	private String whichphoto;//��¼ͼƬ�ļ���
	private int count;//��¼�ڼ���ͼ
	private int position;
	//private int total;//���ļ����¹��м���ͼ
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_page);
		//���Ҷ�Ӧ��view
		myFindViewById();
		//��������
        //mySetTypeFace(); 
        setListeners();
        
        Bundle bundle = this.getIntent().getExtras();
		whichphoto = bundle.getString("CHOICE");
		position = bundle.getInt("POSITION");
		
		//��ʼ��count = 1
		count = 1;
		Bitmap photo = getImageFromAssetsFile("learn/" + whichphoto + "/1.png");//��ȡbundle�еĲ���,���ݲ�����ȡͼƬ
        showboard.setImageBitmap(photo);
        
        /*try {
        	
			total = getResources().getAssets().list("learn/" + whichphoto + "/").length;
			Log.d(this.toString(), "total is "+Integer.toString(total));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	private void setListeners() {
		// TODO Auto-generated method stub
		btnBack.setOnClickListener(btnBack_Click);
		btnNext.setOnClickListener(btnNext_Click);
	}
	
	private ImageButton.OnClickListener btnBack_Click = new ImageButton.OnClickListener(){
    	public void onClick(View v){
    		count--;
    		if(count<=0){
    			count = 1;
    			Toast.makeText(ShowPage.this, R.string.toast_cannotback, Toast.LENGTH_SHORT).show();
    		}else{
    		//��ȡbundle�еĲ���,���ݲ�����ȡͼƬ
    		Bitmap photo = getImageFromAssetsFile("learn/" + whichphoto + "/"+ Integer.toString(count) + ".png");
    		//��ȡ�˿�showboard�е�ͼƬ������
    		Drawable drawable = showboard.getDrawable();
    		//�ж��Ƿ�ΪBitmapDrawable
    		if (drawable instanceof BitmapDrawable) {
    		    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
    		    Bitmap bitmap = bitmapDrawable.getBitmap();
    		    bitmap.recycle();
    		}
    		//��������showboard�е�ͼƬ
    		showboard.setImageBitmap(photo);
            }
    	}//onClick function
    	
    };//ImageButton.OnclickListener class
	
	private ImageButton.OnClickListener btnNext_Click = new ImageButton.OnClickListener(){
    	public void onClick(View v){
    		count++;
    		//��ȡbundle�еĲ���,���ݲ�����ȡͼƬ
    		Bitmap photo = getImageFromAssetsFile("learn/" + whichphoto + "/"+ Integer.toString(count) + ".png");
    		if(photo == null){
    			count--;
    			Toast.makeText(ShowPage.this, R.string.toast_cannotnext
    					, Toast.LENGTH_SHORT).show();
    		}else{
    		//��ȡ�˿�showboard�е�ͼƬ������
    		Drawable drawable = showboard.getDrawable();
    		//�ж��Ƿ�ΪBitmapDrawable
    		if (drawable instanceof BitmapDrawable) {
    		    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
    		    Bitmap bitmap = bitmapDrawable.getBitmap();
    		    bitmap.recycle();
    		}
    		//��������showboard�е�ͼƬ
    		showboard.setImageBitmap(photo);
            }
    	}//onClick function
    	
    };//ImageButton.OnclickListener class
	
	private void myFindViewById() {
		// TODO Auto-generated method stub
		btnBack = (ImageButton)findViewById(R.id.btnBack);
		btnNext = (ImageButton)findViewById(R.id.btnNext);
		showboard = (ImageView)findViewById(R.id.showboard);
	}
	
	
	//��assets�ļ����з���Bitmap���͵�ͼ��
	//���÷�ʽbgimg0 = getImageFromAssetsFile("image/shuangxi/1.png");
	private Bitmap getImageFromAssetsFile(String fileName)  
	  {  
	      Bitmap image = null;  
	      //AssetManager��assets�ļ����л�ȡ��Դ
	      AssetManager am = getResources().getAssets(); 	      
	      try  
	      {  
	          InputStream is = am.open(fileName); 
	          //��InputStream��������image
	          image = BitmapFactory.decodeStream(is);  
	          is.close();  
	      }  
	      catch (IOException e)  
	      {  
	          e.printStackTrace();  
	      }  
	      return image;  
	  }

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
    		Intent intent = new Intent();
    		intent.setClass(ShowPage.this, ChoosePage.class);
    		intent.putExtra("POSITION", position);
    		startActivity(intent);
    		ShowPage.this.finish();
    		return false;
    		//return super.onKeyDown(keyCode, event);
    		}
    return super.onKeyDown(keyCode, event);    
	}
	
	protected void onDestroy() {
		System.out.println("Show---->Destroy");
		super.onDestroy();
	}
}
