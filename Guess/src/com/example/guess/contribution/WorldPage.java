package com.example.guess.contribution;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.papercut.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

/** 
 * @author Infiniters 
 * @version 
 * Created by Jonathan.Xu E-mail: xuchi.int@gmail.com
 * on：2013-7-28 下午10:13:59 
 * Specification:  
 * 
 */

public class WorldPage extends Activity {
	protected static final int CAMERA_RESULT = 0; //摄像头请求
	protected static final int GALLERY_RESULT = 1;//Gallery请求
	
	private static final int PROCESS_SUCCESS = 0;//获取图片成功的标识
	
	//页面上方选取图片按钮
	ImageButton btn_camera;
	ImageButton btn_gallery;
	//页面中部选取类型按钮
	ImageButton btn_yangke;
	ImageButton btn_potrait;
	ImageButton btn_scene;
	ImageButton btn_handpaint;
	// 展示板
	ImageView process_board;
	
	ProgressBar progress_bar;
	TextView progress_percentage;
	ProgressDialog mydialog;
	
	// 待处理图片
	String imageFilePath;
	// 图片
	Bitmap imageBmp = null;
	// 标志位 图片是否就绪？
	boolean isImageReady = false;
	
	// Handler用以处理 thread发过来的消息，消息即为处理进度percentage
	Handler myHandler = new Handler(){
		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case PROCESS_SUCCESS:
				process_board.setImageBitmap((Bitmap) msg.obj);
	    		Toast.makeText(WorldPage.this, R.string.toast_processfinished
						, Toast.LENGTH_SHORT).show();
				progress_bar.setVisibility(View.INVISIBLE);
				progress_percentage.setVisibility(View.INVISIBLE);
			    break;
			default:
				progress_percentage.setText(Integer.toString(msg.what)+"%");
				progress_bar.setProgress(msg.what);
				break;
			}		
			super.handleMessage(msg);
		}		
	};
	
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_world_page);
        myFindViewById(); //查找对应的view
        //mySetTypeFace(); //设置字体
        setListeners();
        progress_bar.setVisibility(View.INVISIBLE);
        progress_percentage.setVisibility(View.INVISIBLE);
	}

	private void myFindViewById() {
		// TODO Auto-generated method stub
		btn_camera=(ImageButton)findViewById(R.id.btn_camera);
		btn_gallery=(ImageButton)findViewById(R.id.btn_gallery);
		process_board=(ImageView)findViewById(R.id.process_board);
		btn_yangke=(ImageButton)findViewById(R.id.btn_yangke);
		progress_bar=(ProgressBar)findViewById(R.id.progress_bar);
		progress_percentage=(TextView)findViewById(R.id.progress_percentage);
	}

	private void setListeners() {
		// TODO Auto-generated method stub
		btn_camera.setOnClickListener(btn_camera_Click);
		btn_gallery.setOnClickListener(btn_gallery_Click);
		btn_yangke.setOnClickListener(btn_yangke_Click);
	}

	private ImageButton.OnClickListener btn_camera_Click = new ImageButton.OnClickListener(){
    	public void onClick(View v){
    	imageFilePath = Environment.getExternalStorageDirectory().getPath() + "/Papercut/mypicture.jpg";
    	//imageFilePath = getResources().getAssets()+"/mypicture.jpg";
    	//imageFilePath = DIRECTORY + "/mypicture.jpg";
    	System.out.println(imageFilePath);
    	File imageFile = new File(imageFilePath);
    	Uri imageFileUri = Uri.fromFile(imageFile);
    	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    	intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
    	startActivityForResult(intent, CAMERA_RESULT);	
    	}//onClick function  调用摄像头返回照片
	};
	
	private ImageButton.OnClickListener btn_gallery_Click = new ImageButton.OnClickListener(){
    	public void onClick(View v){
    	Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),GALLERY_RESULT);
    	}//onClick function  调用GALLERY返回照片
	};
	
	private ImageButton.OnClickListener btn_yangke_Click = new ImageButton.OnClickListener(){
    	public void onClick(View v){
    	if (isImageReady==false){
    		Toast.makeText(WorldPage.this, R.string.toast_imagenotready
					, Toast.LENGTH_SHORT).show();
    		}else{
    			progress_bar.setVisibility(View.VISIBLE);
    			progress_percentage.setVisibility(View.VISIBLE);
    			
    			LayoutInflater inflater = getLayoutInflater();
    			   View layout = inflater.inflate(R.layout.upload_dialog,
    			     (ViewGroup) findViewById(R.id.dialog));

    			
    			new AlertDialog.Builder(WorldPage.this)
    			.setTitle("Input:")
    			.setIcon(android.R.drawable.ic_dialog_info)
    			.setView(layout)
    			.setPositiveButton("Submit", new DialogInterface.OnClickListener() {//设置监听事件  
                  
    				public String executeHttpPost() {
    			        String result = null;
    			        URL url = null;
    			        HttpURLConnection connection = null;
    			        InputStreamReader in = null;
    			        try {
    			            url = new URL("http://192.168.16.81:8080/api/hackathon/");
    			            connection = (HttpURLConnection) url.openConnection();
    			            connection.setDoInput(true);
    			            connection.setDoOutput(true);
    			            connection.setRequestMethod("POST");
    			            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    			            connection.setRequestProperty("Charset", "utf-8");
    			            DataOutputStream dop = new DataOutputStream(
    			                    connection.getOutputStream());
    			            dop.writeBytes("token=alexzhou");
    			            dop.flush();
    			            dop.close();
    			 
    			            in = new InputStreamReader(connection.getInputStream());
    			            BufferedReader bufferedReader = new BufferedReader(in);
    			            StringBuffer strBuffer = new StringBuffer();
    			            String line = null;
    			            while ((line = bufferedReader.readLine()) != null) {
    			                strBuffer.append(line);
    			            }
    			            result = strBuffer.toString();
    			        } catch (Exception e) {
    			            e.printStackTrace();
    			        } finally {
    			            if (connection != null) {
    			                connection.disconnect();
    			            }
    			            if (in != null) {
    			                try {
    			                    in.close();
    			                } catch (IOException e) {
    			                    e.printStackTrace();
    			                }
    			            }
    			 
    			        }
    			        return result;
    			    }

                  @Override  
                  public void onClick(DialogInterface dialog, int which) {  
                      // 输入完成后点击“确定”开始登录  
                      mydialog=ProgressDialog.show(WorldPage.this, "Please Wait a second...", "Uploading...",true);  
                      new Thread()  
                      {  
                          public void run()  
                          {  
                              try  
                              {  
                                  sleep(3000);  
                              }catch(Exception e)  
                              {  
                                  e.printStackTrace();  
                              }finally  
                              {  
                                  //登录结束，取消mydialog对话框  
                                  mydialog.dismiss();  
                              }  
                          }  
                      }.start();  
                  }  
              })
    			.setNegativeButton("Cancel", null)
    			.show();
    			
    			//YangkeThread yangkeThread = new YangkeThread(WorldPage.this);
    			//yangkeThread.start();
    		}
    	}	
	};
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAMERA_RESULT){
			if (resultCode == RESULT_OK) {
				// Bundle extras = intent.getExtras();
				// Bitmap bmp = (Bitmap)extras.get("data");
				//取得屏幕的显示大小
				setProcessBoardFromFilePath();
				isImageReady=true;
			}
		}
		if(requestCode == GALLERY_RESULT){
			if (resultCode == RESULT_OK){
				/**Uri _uri = data.getData();                      
                // this will be null if no image was selected...   
                if (_uri != null) {   
                // now we get the path to the image file   
                Cursor cursor = getContentResolver().query(_uri, null,null, null, null);   
                cursor.moveToFirst();   
                imageFilePath = cursor.getString(1);  //返回图片的地址
                cursor.close();
                setProcessBoardFromFilePath();*/
				ContentResolver resolver = getContentResolver();
				Uri uri = data.getData();
				
				try{
					imageBmp = MediaStore.Images.Media.getBitmap(resolver, uri);
					String[] filePathColumn = {MediaStore.Images.Media.DATA};
					Cursor cursor = resolver.query(uri, filePathColumn, null, null, null);
					if (null != cursor){
						cursor.moveToFirst();
						int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
						imageFilePath = cursor.getString(columnIndex);
						cursor.close();
						System.out.println("图片的路径和名字–>"+ imageFilePath);
					}
				}
				catch (Exception e){
					e.printStackTrace();
					}
				setProcessBoardFromFilePath();
				isImageReady=true;
			}
		}
	}

	//根据路径来设置ProcessBoard
	private void setProcessBoardFromFilePath() {
		Display currentDisplay = getWindowManager().getDefaultDisplay();
		int dw = currentDisplay.getWidth();
		int dh = currentDisplay.getHeight();
		//对拍出的照片进行缩放
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		imageBmp = BitmapFactory.decodeFile(imageFilePath,
				bmpFactoryOptions);
		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight/ (float) dh);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth/ (float) dw);
		if (heightRatio > 1 && widthRatio > 1) {
			if (heightRatio > widthRatio) {
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}
		bmpFactoryOptions.inJustDecodeBounds = false;
		imageBmp = BitmapFactory.decodeFile(imageFilePath, bmpFactoryOptions);
		System.out.println(imageBmp.getByteCount());
		process_board.setImageBitmap(imageBmp);
	}	
		

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}



	
}
