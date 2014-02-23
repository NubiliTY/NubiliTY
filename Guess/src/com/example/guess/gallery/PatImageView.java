package com.example.guess.gallery;

import android.app.Service;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/** 
 * @author Infiniters 
 * @version 
 * Created by Jonathan.Xu E-mail: xuchi.int@gmail.com
 * on��2013-7-19 ����8:46:02 
 * Specification:  Ƕ����GalleryPage�������չImageView����ʵ�ֵ�ָ�϶���˫ָ���ţ�������תͼƬ
 * 
 * Todo�� ��ͼƬ���з���
 * 
 */

public class PatImageView extends ImageView {
	private Matrix matrix;  
    private Matrix savedMatrix;  
      
    private boolean long_touch = false;  
    private static int NONE = 0;  
    private static int DRAG = 1;    // �϶�   
    private static int ZOOM = 2;    // ����   
    private static int ROTA = 3;    // ��ת   
    private int mode = NONE;  
      
    private PointF startPoint;  
    private PointF middlePoint;  
      
    private float oldDistance;  
    private float oldAngle;  
  
    private Vibrator vibrator;  
      
    private GestureDetector gdetector;
    
    public PatImageView(final Context context, AttributeSet attr)// ���캯��
    {  
        super(context,attr);  
        matrix = new Matrix();  
        savedMatrix = new Matrix();  
          
        matrix.setTranslate(0f,0f);  
        setScaleType(ScaleType.MATRIX);  
        setImageMatrix(matrix);  
          
        startPoint = new PointF();  
        middlePoint = new PointF();  
          
        oldDistance = 1f;  
          
        gdetector = new GestureDetector(context, new GestureDetector.OnGestureListener()  
        {  
            @Override  
            public boolean onSingleTapUp(MotionEvent e)  
            {  
                return true;  
            }  
              
            @Override  
            public void onShowPress(MotionEvent e)  
            {  
            }  
              
            @Override  
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)  
            {  
                return true;  
            }  
              
            @Override  
            public void onLongPress(MotionEvent e)  
            {  
                long_touch = true;  
                vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);  
                // ��100ms����ʾ�����Ĳ���������תͼƬ����������ͼƬ   
                vibrator.vibrate(100);  
            }  
              
            @Override  
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)  
            {  
                return true;  
            }  
              
            @Override  
            public boolean onDown(MotionEvent e)  
            {  
                return true;  
            }  
        });  
          
        setOnTouchListener(new OnTouchListener()  
        {  
            public boolean onTouch(View view, MotionEvent event)  
            {  
                switch(event.getAction() & MotionEvent.ACTION_MASK)  
                {  
                case MotionEvent.ACTION_DOWN:           // ��һ����ָtouch   
                    savedMatrix.set(matrix);  
                    startPoint.set(event.getX(), event.getY());  
                    mode = DRAG;  
                    long_touch = false;  
                    break;  
                case MotionEvent.ACTION_POINTER_DOWN:   // �ڶ�����ָtouch   
                    oldDistance = getDistance(event);   // ����ڶ�����ָtouchʱ����ָ֮��ľ���   
                    oldAngle = getDegree(event);        // ����ڶ�����ָtouchʱ����ָ���γɵ�ֱ�ߺ�x��ĽǶ�   
                    if(oldDistance > 10f)  
                    {  
                        savedMatrix.set(matrix);  
                        middlePoint = midPoint(event);  
                        if(!long_touch)  
                        {  
                            mode = ZOOM;  
                        }  
                        else  
                        {  
                            mode = ROTA;  
                        }  
                    }  
                    break;  
                case MotionEvent.ACTION_UP:  
                    mode = NONE;  
                    break;  
                case MotionEvent.ACTION_POINTER_UP:  
                    mode = NONE;  
                    break;  
                case MotionEvent.ACTION_MOVE:  
                    if(vibrator != null)    vibrator.cancel();  
                    if(mode == DRAG)  
                    {  
                        matrix.set(savedMatrix);  
                        matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);  
                    }  
                      
                    if(mode == ZOOM)  
                    {  
                        float newDistance = getDistance(event);  
                          
                        if(newDistance > 10f)  
                        {  
                            matrix.set(savedMatrix);  
                            float scale = newDistance / oldDistance;  
                            matrix.postScale(scale, scale, middlePoint.x, middlePoint.y);  
                        }  
                    }  
                      
                    if(mode == ROTA)  
                    {  
                        float newAngle = getDegree(event);  
                        matrix.set(savedMatrix);  
                        float degrees = newAngle - oldAngle;  
                        matrix.postRotate(degrees, middlePoint.x, middlePoint.y);  
                    }  
                    break;  
                }  
                setImageMatrix(matrix);  
                invalidate();  
                gdetector.onTouchEvent(event);  
                return true;  
            }  
        });  
    }  
  
    // ����������ָ֮��ľ���   
        private float getDistance(MotionEvent event)  
        {  
            float x = event.getX(0) - event.getX(1);  
            float y = event.getY(0) - event.getY(1);  
            return FloatMath.sqrt(x * x + y * y);  
        }  
      
        // ����������ָ���γɵ�ֱ�ߺ�x��ĽǶ�   
        private float getDegree(MotionEvent event)  
        {  
            return (float)(Math.atan((event.getY(1) - event.getY(0)) / (event.getX(1) - event.getX(0))) * 180f);  
        }  
  
        // ����������ָ֮�䣬�м�������   
        private PointF midPoint( MotionEvent event)  
        {  
            PointF point = new PointF();  
            float x = event.getX(0) + event.getX(1);  
            float y = event.getY(0) + event.getY(1);  
            point.set(x / 2, y / 2);  
          
            return point;  
        }

		public void setinitialscale(int imagewidth, int imageheight, int viewwidth, int viewheight) {
			// TODO Auto-generated method stub
			Matrix m = new Matrix(); 
			m.reset();
	        RectF photoRect = new RectF(0, 0, imagewidth, imageheight);
	        //System.out.println(photoRect);
	        RectF viewRect = new RectF(0, 0, viewwidth, viewheight);
	        //System.out.println(viewRect);
	        m.setRectToRect(photoRect, viewRect, Matrix.ScaleToFit.CENTER);
	        //System.out.println(m);
	        this.setImageMatrix(m);
	        matrix.set(m);
	        savedMatrix.set(m);
		}
		
		
}  
