package com.example.guess.gallery;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import java.util.ArrayList;

import com.example.guess.MainPage;
import com.example.papercut.R;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * @author Infiniters
 * @version Created by Jonathan.Xu E-mail: xuchi.int@gmail.com 
 * on：2013-4-11下午11:17:37 
 * Specification:由4组页面组成的Activity，用于图片展示。
 * 
 * 
 * 
 */

public class MultiGridPage extends Activity {
	//单个页面
	private ViewPager viewPager;
	//视图数组
	private ArrayList<View> pageViews;
	
	//点视图
	private ImageView pointView;
	//点视图数组
	private ImageView[] pointViews;
	// 包裹滑动页面LinearLayout
	private ViewGroup gallery_view_group;
	// 包裹小圆点的LinearLayout
	private ViewGroup point_view_group;
	
	private GridView gridMyPapercut;//网格视图
	private GridView gridMyHandpaint;
	private GridView gridPapercutArts;
	private GridView gridMyScrawl;
	
	private GridImageAdapter myPapercutAdapter;//图像网格显示适配器
	private GridImageAdapter myHandpaintAdapter;
	private GridImageAdapter papercutArtsAdapter;
	private GridImageAdapter myScrawlAdapter;
	
	private DisplayMetrics dm;//展示参数
	
	private AssetManager am;
	
	//ImageSource 包含在该页面中
	private ImageSource is;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题窗口
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		
		//把这个4个布局页面加入到集合中
		//获取当前页面的布局扩充器
		LayoutInflater inflater = getLayoutInflater();
		
		//inflate 填充 即从XML文件中中对应到java中来
		View v1 = inflater.inflate(R.layout.gallery_my_papercut, null);
		View v2 = inflater.inflate(R.layout.gallery_my_handpaint, null);
		View v3 = inflater.inflate(R.layout.gallery_papercut_arts, null);
		View v4 = inflater.inflate(R.layout.gallery_my_scrawl, null);

		pageViews = new ArrayList<View>();
		pageViews.add(v1);
		pageViews.add(v2);
		pageViews.add(v3);
		pageViews.add(v4);
		
		//有几个布局页面就有几个圆点图片
		pointViews = new ImageView[pageViews.size()];
		
		gallery_view_group = (ViewGroup) inflater.inflate(R.layout.activity_multi_gallery_page, null);
		point_view_group = (ViewGroup) gallery_view_group.findViewById(R.id.pointViewGroup);
		viewPager = (ViewPager) gallery_view_group.findViewById(R.id.guidePages);

		//通过for循环设置圆点图片的布局
		for (int i = 0; i < pageViews.size(); i++) {
			pointView = new ImageView(MultiGridPage.this);
			pointView.setLayoutParams(new LayoutParams(20, 20));
			pointView.setPadding(20, 0, 20, 0);
			pointViews[i] = pointView;

			if (i == 0) {
				// 默认选中第一张图片
				pointViews[i]
						.setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				pointViews[i].setBackgroundResource(R.drawable.page_indicator);
			}
			//将图片添加到图片group中
			point_view_group.addView(pointViews[i]);
		}
		setContentView(gallery_view_group);

		//然后再为ViewPager设置数据适配器和页面切换事件监听器
		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
		
		/**TODO：这里应该可以尝试使用单例模式重写
		 * 目前实现方法：对不同页面设置不同的监听器，实际上完成的功能类似，只是路径不同
		 * 暂时没有测试得到获取被调用者信息的方法
		 * 
		 */
		gridMyPapercut = (GridView) v1.findViewById(R.id.grid_my_papercut);
		gridMyPapercut.setOnItemClickListener(MyPapercutImageItemClicklistener);
		gridMyHandpaint = (GridView) v2.findViewById(R.id.grid_my_handpaint);
		gridMyHandpaint.setOnItemClickListener(MyHandpaintImageItemClicklistener);
		gridPapercutArts = (GridView) v3.findViewById(R.id.grid_papercut_arts);
		gridPapercutArts.setOnItemClickListener(PapercutArtsImageItemClicklistener);
		gridMyScrawl = (GridView) v4.findViewById(R.id.grid_my_scrawl);
		gridMyScrawl.setOnItemClickListener(MyScrawlImageItemClicklistener);
		
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm); 
		
		am = getResources().getAssets(); 	
		is = new ImageSource(this);
		
		myPapercutAdapter = new GridImageAdapter(this, dm, "My Papercut", is);//新建一个网格图片适应器
		gridMyPapercut.setAdapter(myPapercutAdapter);//设置自适应
		myHandpaintAdapter = new GridImageAdapter(this, dm, "My Handpaint", is);
		gridMyHandpaint.setAdapter(myHandpaintAdapter);
		papercutArtsAdapter = new GridImageAdapter(this, dm, "Papercut Arts", is);
		gridPapercutArts.setAdapter(papercutArtsAdapter);
		myScrawlAdapter = new GridImageAdapter(this, dm, "My Scrawl", is);
		gridMyScrawl.setAdapter(myScrawlAdapter);
		
		//checkOrientation();
		Bundle bundle = this.getIntent().getExtras();
		int temp_page = 0;
		int temp_position = 0;
		temp_page = bundle.getInt("PAGE");
		viewPager.setCurrentItem(temp_page);
		temp_position = bundle.getInt("POSITION");
		switch(temp_page){
			case 0:
				gridMyPapercut.setSelection(temp_position);
			case 1:
				gridMyHandpaint.setSelection(temp_position);
			case 2:
				gridPapercutArts.setSelection(temp_position);
			case 3:
				gridMyScrawl.setSelection(temp_position);
			default:
		}
		
		
		
	}

	// 指引页面数据适配器
	private class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}

	// 指引页面更改事件监听器
	private class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		//在指引页面更改事件监听器(GuidePageChangeListener)中要确保在切换页面时下面的圆点图片也跟着改变
		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < pointViews.length; i++) {
				pointViews[arg0]
						.setBackgroundResource(R.drawable.page_indicator_focused);

				if (arg0 != i) {
					pointViews[i]
							.setBackgroundResource(R.drawable.page_indicator);
				}
			}
		}
	}

	AdapterView.OnItemClickListener MyPapercutImageItemClicklistener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			/**思路：根据position找到在asset文件夹中的路径，
			 * 将路径传递到intent里面。在下一个页面中直接读取
			 */
			Intent intent = new Intent();
			intent.setClass(MultiGridPage.this, GalleryPage.class);
			System.out.println(is.imageMyPapercutName[position]);
			String location = "artshow/My Papercut/"+is.imageMyPapercutName[position];
			System.out.println(location);
			intent.putExtra("location", location);
			intent.putExtra("PAGE", 0);
			intent.putExtra("POSITION", position);
			startActivity(intent);
			MultiGridPage.this.finish();
		}
	};
	
	AdapterView.OnItemClickListener MyHandpaintImageItemClicklistener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			/**思路：根据position找到在asset文件夹中的路径，
			 * 将路径传递到intent里面。在下一个页面中直接读取
			 */
			Intent intent = new Intent();
			intent.setClass(MultiGridPage.this, GalleryPage.class);
			System.out.println(is.imageMyHandpaintName[position]);
			String location = "artshow/My Handpaint/"+is.imageMyHandpaintName[position];
			System.out.println(location);
			intent.putExtra("location", location);
			intent.putExtra("PAGE", 1);
			intent.putExtra("POSITION", position);
			startActivity(intent);
			MultiGridPage.this.finish();
		}
	};
	
	AdapterView.OnItemClickListener PapercutArtsImageItemClicklistener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			/**思路：根据position找到在asset文件夹中的路径，
			 * 将路径传递到intent里面。在下一个页面中直接读取
			 */
			Intent intent = new Intent();
			intent.setClass(MultiGridPage.this, GalleryPage.class);
			System.out.println(is.imagePapercutArtsName[position]);
			String location = "artshow/Papercut Arts/"+is.imagePapercutArtsName[position];
			System.out.println(location);
			intent.putExtra("location", location);
			intent.putExtra("PAGE", 2);
			intent.putExtra("POSITION", position);
			startActivity(intent);
			MultiGridPage.this.finish();
		}
	};
	
	AdapterView.OnItemClickListener MyScrawlImageItemClicklistener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			/**思路：根据position找到在asset文件夹中的路径，
			 * 将路径传递到intent里面。在下一个页面中直接读取
			 */
			Intent intent = new Intent();
			intent.setClass(MultiGridPage.this, GalleryPage.class);
			System.out.println(is.imageMyScrawlName[position]);
			String location = "artshow/My Scrawl/"+is.imageMyScrawlName[position];
			System.out.println(location);
			intent.putExtra("location", location);
			intent.putExtra("PAGE", 3);
			intent.putExtra("POSITION", position);
			startActivity(intent);
			MultiGridPage.this.finish();
		}
	};
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(MultiGridPage.this, MainPage.class);
			startActivity(intent);
			MultiGridPage.this.finish();
			return false;
			// return super.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void onDestroy() {
		System.out.println("MultiGridPage---->Destroy");
		super.onDestroy();
	}

}
