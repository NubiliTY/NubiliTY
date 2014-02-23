package com.example.guess.gallery;

import java.io.IOException;
import java.io.InputStream;

import com.example.papercut.R;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * @author Infiniters
 * @version Created by Jonathan.Xu E-mail: xuchi.int@gmail.com on：2013-4-12
 *          下午8:49:21 
 * Specification:   显示单独每个图像的页面，用到了PatImageView类
 * Todo: 重写返回按钮
 * 
 */

public class GalleryPage extends Activity {
	private String location = "artshow/My Papercut/11.png"; //
	private int page;
	private int position;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gallery_page);
		PatImageView patimageview = (PatImageView) findViewById (R.id.patimageview); 	        

		Intent intent = getIntent();
		location = intent.getStringExtra("location"); 
		System.out.println(location);

		Bundle bundle = this.getIntent().getExtras();
		page = bundle.getInt("PAGE");
		position = bundle.getInt("POSITION");

		Animation an = AnimationUtils.loadAnimation(this, R.anim.scale); //
		patimageview.setAnimation(an);
		Bitmap photo = getImageFromAssetsFile(location);//获取bundle中的参数,根据参数读取图片
        patimageview.setImageBitmap(photo);
        patimageview.setinitialscale(photo.getWidth(), photo.getHeight(),
        		getWindowManager().getDefaultDisplay().getWidth(), // Argue: 获取了屏幕的尺寸,代码适应性降低
        		getWindowManager().getDefaultDisplay().getHeight());
	}
	
	private Bitmap getImageFromAssetsFile(String fileName)  
	  {  
	      Bitmap image = null;  
	      //AssetManager从assets文件夹中获取资源
	      AssetManager am = getResources().getAssets(); 	      
	      try  
	      {  
	          InputStream is = am.open(fileName); 
	          //从InputStream解码生成image
	          image = BitmapFactory.decodeStream(is);  
	          is.close();  
	      }  
	      catch (IOException e)  
	      {  
	          e.printStackTrace();  
	      }  
	      return image;  
	  }
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
    		Intent intent = new Intent();
    		intent.putExtra("PAGE",page);
    		intent.putExtra("POSITION", position);//设置初始位置
    		intent.setClass(GalleryPage.this, MultiGridPage.class);
    		startActivity(intent);
    		GalleryPage.this.finish();
    		return false;
    		//return super.onKeyDown(keyCode, event);
    		}
    return super.onKeyDown(keyCode, event);    
	}
	
	
}
