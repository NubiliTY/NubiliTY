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
 * on：2013-4-6 下午8:00:10 
 * Specification:  PaperCut主页面
 * 
 * 
 * 问题:  无法设置字体 见函数 mySetTypeFace()
 * 
 */

public class MainPage extends Activity {
	//SD 卡是否加载 
	private boolean isMediaMounted = false;
	//主界面按钮
	private Button btnWorld;
	private Button btnGallery;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_page);
        myFindViewById(); //查找对应的view
        //mySetTypeFace(); //设置字体
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
    		//切换到报告页面
    		Intent intent = new Intent();
    		//设置意图来源的Activity所在的class,与要前往的Activity所在的class
    		intent.putExtra("POSITION", 0);//设置初始位置
    		intent.setClass(MainPage.this, ChoosePage.class);
    		//新建一个bundle携带所需要的数据
    		//Bundle bundle = new Bundle();
    		//bundle.putString("KEY_HEIGHT", xxx.getText().toString());
    		//bundle.putString("KEY_WEIGHT", xxx.getText().toString());
    		//将bundle放入intent中
    		//intent.putExtras(bundle);
    		startActivity(intent);
    		MainPage.this.finish();
    	}//onClick function
    	
    };//Button.OnclickListener class
    
    private Button.OnClickListener btnGallery_Click = new Button.OnClickListener(){
    	public void onClick(View v){
    		Intent intent = new Intent();
    		intent.putExtra("PAGE", 0);//设置初始页面
    		intent.putExtra("POSITION", 0);//设置初始位置
    		intent.setClass(MainPage.this, MultiGridPage.class);
    		startActivity(intent);
    		MainPage.this.finish();
    	}//onClick function
    	
    };//Button.OnclickListener class
    
    private Button.OnClickListener btnWorld_Click = new Button.OnClickListener(){
    	public void onClick(View v){
    		//切换到报告页面
    		Intent intent = new Intent();
    		//设置意图来源的Activity所在的class,与要前往的Activity所在的class
    		intent.setClass(MainPage.this, WorldPage.class);
    		
    		//新建一个bundle携带所需要的数据
    		//Bundle bundle = new Bundle();
    		//bundle.putString("KEY_HEIGHT", xxx.getText().toString());
    		//bundle.putString("KEY_WEIGHT", xxx.getText().toString());
    		//将bundle放入intent中
    		//intent.putExtras(bundle);
    		
    		startActivity(intent);
    	}//onClick function
    	
    };//Button.OnclickListener class
    
    
	private void mySetTypeFace() {
		// TODO Auto-generated method stub
		//从assert中获取有资源，获得app的assert，采用getAserts()，通过给出在assert/下面的相对路径。在实际使用中，字体库可能存在于SD卡上，可以采用createFromFile()来替代createFromAsset。
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
