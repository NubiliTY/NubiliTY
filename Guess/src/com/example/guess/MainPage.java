package com.example.guess;

import java.io.File;

import com.example.guess.contribution.WorldPage;
import com.example.guess.gallery.MultiGridPage;
import com.example.papercut.R;
import com.example.papercut.learn.ChoosePage;
import com.example.papercut.scrawl.ScrawlPage;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/** 
 * @author Infiniters
 * @version 
 * Created by Jonathan.Xu E-mail: xuchi.int@gmail.com
 * on��2013-4-6 ����8:00:10 
 * Specification:  PaperCut��ҳ��
 * 
 * 
 * ����:  �޷��������� ������ mySetTypeFace()
 * 
 */

public class MainPage extends Activity {
	//SD ���Ƿ���� 
	private boolean isMediaMounted = false;
	//�����水ť
	private Button btnWorld;
	private Button btnGallery;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_page);
        myFindViewById(); //���Ҷ�Ӧ��view
        //mySetTypeFace(); //��������
        setListeners();
        
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
        	//System.out.println(Environment.getExternalStorageDirectory().getPath()+
        			//"/Papercut");
        	File file = new File(Environment.getExternalStorageDirectory().getPath()+"/Papercut");
        	if(!file.exists() && !file.isDirectory()){
        		boolean result = file.mkdirs();
        		System.out.println(result);
        	}
        	isMediaMounted = true;
        }else{
        	isMediaMounted = false;
        }
    }

    private void setListeners() {
		// TODO Auto-generated method stub
		btnWorld.setOnClickListener(btnWorld_Click);
		btnGallery.setOnClickListener(btnGallery_Click);
	}

    private Button.OnClickListener btnLearn_Click = new Button.OnClickListener(){
    	public void onClick(View v){
    		//�л�������ҳ��
    		Intent intent = new Intent();
    		//������ͼ��Դ��Activity���ڵ�class,��Ҫǰ����Activity���ڵ�class
    		intent.putExtra("POSITION", 0);//���ó�ʼλ��
    		intent.setClass(MainPage.this, ChoosePage.class);
    		//�½�һ��bundleЯ������Ҫ������
    		//Bundle bundle = new Bundle();
    		//bundle.putString("KEY_HEIGHT", xxx.getText().toString());
    		//bundle.putString("KEY_WEIGHT", xxx.getText().toString());
    		//��bundle����intent��
    		//intent.putExtras(bundle);
    		startActivity(intent);
    		MainPage.this.finish();
    	}//onClick function
    	
    };//Button.OnclickListener class
    
    private Button.OnClickListener btnGallery_Click = new Button.OnClickListener(){
    	public void onClick(View v){
    		Intent intent = new Intent();
    		intent.putExtra("PAGE", 0);//���ó�ʼҳ��
    		intent.putExtra("POSITION", 0);//���ó�ʼλ��
    		intent.setClass(MainPage.this, MultiGridPage.class);
    		startActivity(intent);
    		MainPage.this.finish();
    	}//onClick function
    	
    };//Button.OnclickListener class
    
    private Button.OnClickListener btnWorld_Click = new Button.OnClickListener(){
    	public void onClick(View v){
    		//�л�������ҳ��
    		Intent intent = new Intent();
    		//������ͼ��Դ��Activity���ڵ�class,��Ҫǰ����Activity���ڵ�class
    		intent.setClass(MainPage.this, WorldPage.class);
    		
    		//�½�һ��bundleЯ������Ҫ������
    		//Bundle bundle = new Bundle();
    		//bundle.putString("KEY_HEIGHT", xxx.getText().toString());
    		//bundle.putString("KEY_WEIGHT", xxx.getText().toString());
    		//��bundle����intent��
    		//intent.putExtras(bundle);
    		
    		startActivity(intent);
    	}//onClick function
    	
    };//Button.OnclickListener class
    
    
	private void mySetTypeFace() {
		// TODO Auto-generated method stub
		//��assert�л�ȡ����Դ�����app��assert������getAserts()��ͨ��������assert/��������·������ʵ��ʹ���У��������ܴ�����SD���ϣ����Բ���createFromFile()�����createFromAsset��
    	Typeface face = Typeface.createFromAsset(getAssets(), "fonts/papercut.ttf");
    	btnWorld.setTypeface(face);
    	btnGallery.setTypeface(face);
	}

	private void myFindViewById() {
		// TODO Auto-generated method stub
    	btnWorld = (Button)findViewById(R.id.btnWorld);
    	btnGallery = (Button)findViewById(R.id.btnGallery);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_page, menu);
        return true;
    }

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		System.out.println("Destroy---->Main");
		super.onDestroy();
	}
	protected void onStart() {
		System.out.println("Start---->Main");
		super.onDestroy();
	}

    
}
