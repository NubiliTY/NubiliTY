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
 * on��2013-7-24 ����12:02:08 
 * Specification:  
 * 
 */

public class ChoosePage extends Activity {
	private GridView gridMyChoose;//������ͼ
	private GridImageAdapter myChooseAdapter;//ͼ��������ʾ������
	private DisplayMetrics dm;//չʾ����	
	private AssetManager am;
	//ImageSource �����ڸ�ҳ����
	private ImageSource is;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �����ޱ��ⴰ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_choose_page);
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm); 
		am = getResources().getAssets(); 	
		is = new ImageSource(this);
		myFindViewById(); 
		setListeners();
		myChooseAdapter = new GridImageAdapter(this, dm, is);//�½�һ������ͼƬ��Ӧ��
		gridMyChoose.setAdapter(myChooseAdapter);//��������Ӧ
		
		//����ManPageҳ���룬������Ϊ��ʼֵ
		//����ShowPageҳ���أ�������Ϊ��һ��ѡ����Ǹ�item.
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
			/**˼·������position�ҵ���asset�ļ����е�·����
			 * ��·�����ݵ�intent���档����һ��ҳ����ֱ�Ӷ�ȡ
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
	//����assets��
	 String[] imageMyChooseName;//��¼�ļ�������
     int[] imageMyChooseNum;//��¼��Ӧ�ļ��������ж�����ͼƬ
     
	 //���캯��
	 public ImageSource(Context context){
		this.mContext = context;
		 try {
			 //�õ�learn�ļ����е����ļ�����
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


