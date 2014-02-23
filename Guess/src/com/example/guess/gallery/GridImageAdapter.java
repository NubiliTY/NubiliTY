package com.example.guess.gallery;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.example.papercut.R;

/** 
 * @author Infiniters 
 * @version 
 * Created by Jonathan.Xu E-mail: xuchi.int@gmail.com
 * on：2013-4-12 下午4:42:43 
 * Specification:  重写了网格图像适应接口以及图像视图两个类
 * 
 */

/** 构造网格图像适应接口 */
public class GridImageAdapter extends BaseAdapter {
	//上下文
	private Context mContext;
	//Drawable
	Drawable btnDrawable;
	private DisplayMetrics mDm;//展示参数
	private String mPage="";
	//adapter中添加ImageSource
	private ImageSource mis;
	
	//构造函数
	public GridImageAdapter(Context context) {
		mContext = context;
		Resources resources = context.getResources();
		btnDrawable = resources.getDrawable(R.drawable.bg);
	}
	
	public GridImageAdapter(Context context, DisplayMetrics dm, ImageSource is) {
		mContext = context;
		Resources resources = context.getResources();
		btnDrawable = resources.getDrawable(R.drawable.bg);
	    mDm = dm;
	    this.mis = is;
	}

	/**根据页面构建Adapter,加载不同的ImageSource*/
	public GridImageAdapter(Context context, DisplayMetrics dm, String page,ImageSource is) {
		mContext = context;
		Resources resources = context.getResources();
		btnDrawable = resources.getDrawable(R.drawable.bg);
	    mDm = dm;
	    this.mPage = page;
	    System.out.println(page);
	    this.mis = is;
	}
	
	
	@Override
	public int getCount() {
		if (mPage.equals("My Handpaint")){
			return mis.imageMyHandpaintName.length;
		}else if(mPage.equals("Papercut Arts")){
			return mis.imagePapercutArtsName.length;
		}else if(mPage.equals("My Scrawl")){
			return mis.imageMyScrawlName.length;
		}else if(mPage.equals("My Papercut")){
			return mis.imageMyPapercutName.length;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		int space;

		if (convertView == null) {
			imageView = new ImageView(mContext);
				//手动设置 固定space By xcch
				//space = mDm.widthPixel / mImageCol;
			space =  (int) (mDm.widthPixels * 0.7);
			imageView.setLayoutParams(new GridView.LayoutParams(space, space/2));
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);	// 缩放图片在内部
			imageView.setPadding(3, 3, 3, 3);
		} else {
			imageView = (ImageView) convertView;
		}
		
		System.out.println(mPage);
		
		Bitmap image = findImage(mPage, position);
		imageView.setImageBitmap(image);
		
		
		
		return imageView;
	}
	
	private Bitmap findImage (String mPage, int position){
		Bitmap image=null;
		InputStream is=null;
		try {
			AssetManager mAm = mContext.getResources().getAssets();
			if(mPage.equals("My Papercut")){
	    	  is = mAm.open("artshow/My Papercut/"+mis.imageMyPapercutName[position]);
			}else if(mPage.equals("My Handpaint")){
		      is = mAm.open("artshow/My Handpaint/"+mis.imageMyHandpaintName[position]);
			}else if (mPage.equals("Papercut Arts")){
			  is = mAm.open("artshow/Papercut Arts/"+mis.imagePapercutArtsName[position]);
			}else if (mPage.equals("My Scrawl")){
			  is = mAm.open("artshow/My Scrawl/"+mis.imageMyScrawlName[position]);
			}
		    // 从InputStream解码生成image
	    	System.out.println(""+is.available());
	    	BitmapFactory.Options options=new BitmapFactory.Options();
	    	options.inJustDecodeBounds = false;
	    	options.inSampleSize = 3;   //width，hight设为原来的十分一
	    	image = BitmapFactory.decodeStream(is,null,options);
	    	is.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }					
		return image;
	}


}



