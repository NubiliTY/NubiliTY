package com.example.papercut.learn;

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

	@Override
	public int getCount() {
		return mis.imageMyChooseName.length;
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
		
		Bitmap image = findImage(position);
		imageView.setImageBitmap(image);
		
		
		
		return imageView;
	}
	
	private Bitmap findImage (int position){
		Bitmap image=null;
		InputStream is=null;
		try {
			AssetManager mAm = mContext.getResources().getAssets();
			is = mAm.open("learn/"+mis.imageMyChooseName[position]+"/"+mis.imageMyChooseNum[position]+".png");//最后一张
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



