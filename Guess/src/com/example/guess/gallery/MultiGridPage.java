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
 * on��2013-4-11����11:17:37 
 * Specification:��4��ҳ����ɵ�Activity������ͼƬչʾ��
 * 
 * 
 * 
 */

public class MultiGridPage extends Activity {
	//����ҳ��
	private ViewPager viewPager;
	//��ͼ����
	private ArrayList<View> pageViews;
	
	//����ͼ
	private ImageView pointView;
	//����ͼ����
	private ImageView[] pointViews;
	// ��������ҳ��LinearLayout
	private ViewGroup gallery_view_group;
	// ����СԲ���LinearLayout
	private ViewGroup point_view_group;
	
	private GridView gridMyPapercut;//������ͼ
	private GridView gridMyHandpaint;
	private GridView gridPapercutArts;
	private GridView gridMyScrawl;
	
	private GridImageAdapter myPapercutAdapter;//ͼ��������ʾ������
	private GridImageAdapter myHandpaintAdapter;
	private GridImageAdapter papercutArtsAdapter;
	private GridImageAdapter myScrawlAdapter;
	
	private DisplayMetrics dm;//չʾ����
	
	private AssetManager am;
	
	//ImageSource �����ڸ�ҳ����
	private ImageSource is;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �����ޱ��ⴰ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		
		//�����4������ҳ����뵽������
		//��ȡ��ǰҳ��Ĳ���������
		LayoutInflater inflater = getLayoutInflater();
		
		//inflate ��� ����XML�ļ����ж�Ӧ��java����
		View v1 = inflater.inflate(R.layout.gallery_my_papercut, null);
		View v2 = inflater.inflate(R.layout.gallery_my_handpaint, null);
		View v3 = inflater.inflate(R.layout.gallery_papercut_arts, null);
		View v4 = inflater.inflate(R.layout.gallery_my_scrawl, null);

		pageViews = new ArrayList<View>();
		pageViews.add(v1);
		pageViews.add(v2);
		pageViews.add(v3);
		pageViews.add(v4);
		
		//�м�������ҳ����м���Բ��ͼƬ
		pointViews = new ImageView[pageViews.size()];
		
		gallery_view_group = (ViewGroup) inflater.inflate(R.layout.activity_multi_gallery_page, null);
		point_view_group = (ViewGroup) gallery_view_group.findViewById(R.id.pointViewGroup);
		viewPager = (ViewPager) gallery_view_group.findViewById(R.id.guidePages);

		//ͨ��forѭ������Բ��ͼƬ�Ĳ���
		for (int i = 0; i < pageViews.size(); i++) {
			pointView = new ImageView(MultiGridPage.this);
			pointView.setLayoutParams(new LayoutParams(20, 20));
			pointView.setPadding(20, 0, 20, 0);
			pointViews[i] = pointView;

			if (i == 0) {
				// Ĭ��ѡ�е�һ��ͼƬ
				pointViews[i]
						.setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				pointViews[i].setBackgroundResource(R.drawable.page_indicator);
			}
			//��ͼƬ��ӵ�ͼƬgroup��
			point_view_group.addView(pointViews[i]);
		}
		setContentView(gallery_view_group);

		//Ȼ����ΪViewPager����������������ҳ���л��¼�������
		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
		
		/**TODO������Ӧ�ÿ��Գ���ʹ�õ���ģʽ��д
		 * Ŀǰʵ�ַ������Բ�ͬҳ�����ò�ͬ�ļ�������ʵ������ɵĹ������ƣ�ֻ��·����ͬ
		 * ��ʱû�в��Եõ���ȡ����������Ϣ�ķ���
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
		
		myPapercutAdapter = new GridImageAdapter(this, dm, "My Papercut", is);//�½�һ������ͼƬ��Ӧ��
		gridMyPapercut.setAdapter(myPapercutAdapter);//��������Ӧ
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

	// ָ��ҳ������������
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

	// ָ��ҳ������¼�������
	private class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		//��ָ��ҳ������¼�������(GuidePageChangeListener)��Ҫȷ�����л�ҳ��ʱ�����Բ��ͼƬҲ���Ÿı�
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
			/**˼·������position�ҵ���asset�ļ����е�·����
			 * ��·�����ݵ�intent���档����һ��ҳ����ֱ�Ӷ�ȡ
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
			/**˼·������position�ҵ���asset�ļ����е�·����
			 * ��·�����ݵ�intent���档����һ��ҳ����ֱ�Ӷ�ȡ
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
			/**˼·������position�ҵ���asset�ļ����е�·����
			 * ��·�����ݵ�intent���档����һ��ҳ����ֱ�Ӷ�ȡ
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
			/**˼·������position�ҵ���asset�ļ����е�·����
			 * ��·�����ݵ�intent���档����һ��ҳ����ֱ�Ӷ�ȡ
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
