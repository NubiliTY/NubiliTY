package com.example.papercut.learn;


import java.io.IOException;

import com.example.guess.MainPage;
import com.example.papercut.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

/** 
 * @author Infiniters 
 * @version 
 * Created by Jonathan.Xu E-mail: xuchi.int@gmail.com
 * on：2013-7-24 上午12:02:08 
 * Specification:  
 * 
 */

public class ChoosePage extends Activity {
	private GridView gridMyChoose;//网格视图
	private GridImageAdapter myChooseAdapter;//图像网格显示适配器
	private DisplayMetrics dm;//展示参数	
	private AssetManager am;
	//ImageSource 包含在该页面中
	private ImageSource is;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题窗口
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_choose_page);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm); 
		am = getResources().getAssets(); 	
		is = new ImageSource(this);
		myFindViewById(); 
		setListeners();
		myChooseAdapter = new GridImageAdapter(this, dm, is);//新建一个网格图片适应器
		gridMyChoose.setAdapter(myChooseAdapter);//设置自适应
		
		//若从ManPage页进入，则设置为初始值
		//若从ShowPage页返回，则设置为上一次选择的那个item.
		Bundle bundle = this.getIntent().getExtras();
		int temp_position = 0;
		temp_position = bundle.getInt("POSITION");
		gridMyChoose.setSelection(temp_position);
		
	}
	
	private void setListeners() {
		// TODO Auto-generated method stub
		gridMyChoose.setOnItemClickListener(MyChooseImageItemClicklistener);
	}

	private void myFindViewById() {
		// TODO Auto-generated method stub
		gridMyChoose = (GridView) this.findViewById(R.id.grid_my_choose);
	}
	
	AdapterView.OnItemClickListener MyChooseImageItemClicklistener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			/**思路：根据position找到在asset文件夹中的路径，
			 * 将路径传递到intent里面。在下一个页面中直接读取
			 */
			Intent intent = new Intent();
			intent.setClass(ChoosePage.this, ShowPage.class);
			System.out.println(is.imageMyChooseName[position]);
			String location = "learn/"+is.imageMyChooseName[position]+"/"+is.imageMyChooseNum[position]+".png";
			System.out.println(location);
			intent.putExtra("POSITION", position);
			intent.putExtra("CHOICE", is.imageMyChooseName[position]);
			startActivity(intent);
			ChoosePage.this.finish();
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(ChoosePage.this, MainPage.class);
			startActivity(intent);
			ChoosePage.this.finish();
			return false;
			// return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onDestroy() {
		System.out.println("ChoosePage---->Destroy");
		super.onDestroy();
	}

	
	
}


class ImageSource  {
	
	Context mContext;
	//来自assets中
	 String[] imageMyChooseName;//记录文件夹名称
     int[] imageMyChooseNum;//记录对应文件夹下面有多少张图片
     
	 //构造函数
	 public ImageSource(Context context){
		this.mContext = context;
		 try {
			 //得到learn文件夹中的子文件夹名
			imageMyChooseName = mContext.getResources().getAssets().list("learn");
			imageMyChooseNum= new int[imageMyChooseName.length];
			for(int i=0;i<imageMyChooseName.length;i++){
				imageMyChooseNum[i]=mContext.getResources().getAssets().list("learn/"+imageMyChooseName[i]).length;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
}


