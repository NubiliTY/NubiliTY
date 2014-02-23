package com.example.guess.contribution;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/** 
 * @author Infiniters 
 * @version 
 * Created by Jonathan.Xu E-mail: xuchi.int@gmail.com
 * on��2013-8-5 ����12:05:18 
 * Specification:  �����õ���ͼƬ������������
 * 
 */

public class ProcessFactory {

	public static double[] GAUSS;
	public static final int gauss_radius = 5;      //�������Ӧ�ĸ�˹����������أ���1����2.
	public static double sigma = 30;
    public static double toOne = 0;
	
    /**
     * ��ʼ����˹����
     * @param fi
     */
	public static void IniGauss_2(int fi)
    {
        toOne = 0;           //һ��Ҫ�Դ˱������г�ʼ��������
        GAUSS = new double[(fi*2+1)*(fi*2+1)];
        int index = 0;

        for (int x=-fi; x<=fi; x++){
            for (int y=-fi; y<=fi; y++){
                double sqrtFi = sigma*sigma;
                double ex = Math.pow(Math.E, (-(double)(x*x + y*y)/(2*(double)sqrtFi)));
                double result = ex/(double)(2 * Math.PI * sqrtFi);
                GAUSS[index] = result;
                toOne += result;
                index++;
                //MessageBox.Show(result.ToString());
                }
            }
        for (int i = 0; i < index; i++){
            GAUSS[i] = GAUSS[i] / toOne;
            System.out.println("GAUSS["+i+"] = " + GAUSS[i]);
        }
        
        double sum = 0;
        for( double i : GAUSS) {
            sum += i;
        }
        System.out.println("sum is"+sum);
        
    }
	
	/**
	 * ȡ�Ҷ�ͼ����1
	 * @param bmpOriginal
	 * @return
	 */
	public static Bitmap toGray1(Bitmap bmpOriginal){ 
		int width = bmpOriginal.getWidth(); //��ȡλͼ�Ŀ� 
		int height = bmpOriginal.getHeight(); //��ȡλͼ�ĸ� 

		int[] pixels = new int[width*height]; //ͨ��λͼ�Ĵ�С�������ص����� 

		bmpOriginal.getPixels(pixels, 0, width, 0, 0, width, height); 
		int alpha = (pixels[0] & 0xFF000000)>>24; 
		//int alpha = (byte)0xFF; 
		for(int i = 0; i < height; i++){ 
			for(int j = 0; j < width; j++){ 
				int pixel_src = pixels[width * i + j]; 
				int red = (pixel_src & 0x00FF0000 ) >> 16; 
				int green = (pixel_src & 0x0000FF00) >> 8; 
				int blue = pixel_src & 0x000000FF; 
				//ע����Ҫ��ת����float����
				int pixel_gray = (int)(((float)red) * 0.299 + ((float)green) * 0.587 + ((float)blue) * 0.114);
				int pixel_output = ((alpha <<24) & 0xFF000000) | ((pixel_gray << 16) & 0x00FF0000) | 
						((pixel_gray << 8) & 0x0000FF00) | (pixel_gray & 0x000000FF); 
				pixels[width * i + j] = pixel_output; 
				} 
			} 
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Config.ARGB_8888); 
		bmpGrayscale.setPixels(pixels, 0, width, 0, 0, width, height); 
		return bmpGrayscale; 
		} 

	/**
	 * ȡ�Ҷ�ͼ����2
	 * @param bmpOriginal
	 * @return
	 */
	 public static Bitmap toGray2(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();    
		
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
		}
	 
	 /**
	  * ȡ��ɫ
	  * @param bmpOriginal
	  * @return
	  */
	 public static Bitmap toInverse(Bitmap bmpOriginal){
		int width = bmpOriginal.getWidth(); //��ȡλͼ�Ŀ� 
		int height = bmpOriginal.getHeight(); //��ȡλͼ�ĸ� 

		int[] pixels = new int[width*height]; //ͨ��λͼ�Ĵ�С�������ص����� 

		bmpOriginal.getPixels(pixels, 0, width, 0, 0, width, height); 
		int alpha = (byte)((pixels[0] & 0xFF000000)>>24); 
		for(int i = 0; i < height; i++){ 
			for(int j = 0; j < width; j++){ 
				int pixel_src = pixels[width * i + j]; 
				int red = ((pixel_src & 0x00FF0000 ) >> 16); 
				int green = ((pixel_src & 0x0000FF00) >> 8); 
				int blue = (pixel_src & 0x000000FF); 
				
				red = 255 - red;
				green = 255 - green;
				blue = 255 - blue;
				
				pixel_src = (alpha<<24) | (red << 16) | (green << 8) | blue; 
				pixels[width * i + j] = pixel_src; 
				} 
			} 
		Bitmap bmpInverse = Bitmap.createBitmap(width, height, Config.ARGB_8888); 
		bmpInverse.setPixels(pixels, 0, width, 0, 0, width, height); 
		return bmpInverse; 
		}
	 
	 
	 /**
	  * �����˹ģ��
	  * @param bmpOriginal
	  * @return
	  */
	public static Bitmap toGauss(Bitmap bmpOriginal) {
		int width = bmpOriginal.getWidth(); //��ȡλͼ�Ŀ� 
		int height = bmpOriginal.getHeight(); //��ȡλͼ�ĸ� 

		int[] pixels = new int[width*height]; //ͨ��λͼ�Ĵ�С�������ص����� 
		int[] pixels_result = new int[width*height];
		
		bmpOriginal.getPixels(pixels, 0, width, 0, 0, width, height); 
		int alpha = (pixels[0] & 0xFF000000)>>24; 
		
		/*remark
		 * ����ѭ�����ø�˹��������˹ģ��
		 */
		
		//��������
		int i,j,col,row;
		//����ÿ���������
		int index=0;
		//���ռ�Ȩ��RGBֵ
		double sum_red = 0, sum_green = 0, sum_blue = 0;
		//����ԭʼͼƬ��pixel���Ĵ�
		int pixel_src;
		//����ԭʼͼƬ��RGB���Ĵ�
		int red = 0, green = 0, blue = 0;
		//��������pixel���Ĵ�
		int pixel_result;
		
		for(i = gauss_radius; i < height - gauss_radius; i++){ 
			for(j = gauss_radius; j < width - gauss_radius; j++){ 
				//long start = System.nanoTime(); //��΢��
				index=0;
				//��Ȩ��RGBֵ
				sum_red = 0; sum_green = 0; sum_blue = 0;

				for(col = (-gauss_radius); col <= gauss_radius; col++){
					for(row = (-gauss_radius); row <= gauss_radius; row++){
						
						pixel_src = pixels[width*(i+col) + (j+row)]; 
						red = ((pixel_src & 0x00FF0000 ) >> 16); 
						green = ((pixel_src & 0x0000FF00) >> 8); 
						blue = (pixel_src & 0x000000FF); 
						
						sum_red = sum_red + (((double)red)*GAUSS[index]);
						sum_green = sum_green + (((double)green)*GAUSS[index]);
						sum_blue = sum_blue + (((double)blue)*GAUSS[index]);
						
						index++;
						
					}
				}//������˹����ѭ��
				//long end=System.currentTimeMillis();//��ȡ���н���ʱ�䡡��
				//System.out.println("��������ʱ�䣺"+(end-start)+"ms");//���
				//������ɫֵ���
				if (sum_red > 255) sum_red = 255;
                if (sum_red < 0) sum_red = 0;
                if (sum_green > 255) sum_green = 255;
                if (sum_green < 0) sum_green = 0;
                if (sum_blue > 255) sum_blue = 255;
                if (sum_blue < 0) sum_blue = 0;
                
				//���⣺�����intԭ��Ϊbyte��������
				pixel_result =  (alpha<<24 & 0xFF000000) | (((int)sum_red) << 16 & 0x00FF0000) | 
						(((int)sum_green) << 8 & 0x0000FF00) | (((int)sum_blue) & 0x000000FF); 
				//pixels_result[width * i + j] = pixel_result; 
				pixels[width * i + j] = pixel_result; 
				//System.out.println((System.nanoTime() - start) / 1000); //΢��
				} 
			}		
		Bitmap bmpGauss = Bitmap.createBitmap(width, height, Config.ARGB_8888); 
		//bmpGauss.setPixels(pixels_result, 0, width, 0, 0, width, height); 		
		bmpGauss.setPixels(pixels, 0, width, 0, 0, width, height); 
		return bmpGauss; 
	}

	/**
	 * ������ɫ����
	 * @param bmpGauss ��˹ģ����ϵ�ͼ��
	 * @param bmpGrayscale �Ҷ�ͼ��
	 * @return
	 */
	public static Bitmap toColorDodge(Bitmap bmpGauss, Bitmap bmpGrayscale) {
		// TODO Auto-generated method stub
		
		// ��ԭ�ȵĻҶ�ͼ������ɫ������ʹ�÷�ɫ��˹ͼ����
		// TODO ��ʡ�ڴ�ռ� δʹ�ñ�����ע��
		
		int width = bmpGauss.getWidth(); //��ȡλͼ�Ŀ� 
		int height = bmpGauss.getHeight(); //��ȡλͼ�ĸ� 

		int[] pixels_Gauss = new int[width*height]; //ͨ��λͼ�Ĵ�С�������ص����� 
		int[] pixels_Grayscale = new int[width*height];
		
		bmpGauss.getPixels(pixels_Gauss, 0, width, 0, 0, width, height); 
		bmpGrayscale.getPixels(pixels_Grayscale, 0, width, 0, 0, width, height);
		
		//int alpha_Gauss = (byte)((pixels_Gauss[0] & 0xFF000000)>>24); 
		int alpha_Grayscale = (byte)((pixels_Grayscale[0] & 0xFF000000)>>24); 
		
		for(int i = 0; i < height; i++){ 
			for(int j = 0; j < width; j++){ 
				int pixel_Gauss = pixels_Gauss[width * i + j];
				int pixel_Grayscale = pixels_Grayscale[width * i + j];
				
				int red_Gauss = ((pixel_Gauss & 0x00FF0000 ) >> 16); 
				//int green_Gauss = ((pixel_Gauss & 0x0000FF00) >> 8); 
				//int blue_Gauss = (pixel_Gauss & 0x000000FF); 
				
				int red_Grayscale = ((pixel_Grayscale & 0x00FF0000 ) >> 16); 
				//int green_Grayscale = ((pixel_Grayscale & 0x0000FF00) >> 8); 
				//int blue_Grayscale = (pixel_Grayscale & 0x000000FF); 
				
				if(red_Grayscale != 0){
					int temp = red_Grayscale + red_Gauss;
					if(temp > 255) 
						temp = 255;
					else if(temp <85) 
						temp = 0;
					else if(temp < 235 && temp >= 85) 
						temp -= 85;
					//red_Grayscale = (int)temp;
					//green_Grayscale = (int)temp;
					//blue_Grayscale = (int)temp;
					int pixel_Output = ((int)alpha_Grayscale << 24) | ((int)temp << 16) | ((int)temp << 8) | ((int)temp); 
					pixels_Grayscale[width * i + j] = pixel_Output; 
					}
				} 
			} 
		Bitmap bmpOutput = Bitmap.createBitmap(width, height, Config.ARGB_8888); 
		bmpOutput.setPixels(pixels_Grayscale, 0, width, 0, 0, width, height); 
		return bmpOutput; 
	}

	public static Bitmap toPapercut(Bitmap bmpOriginal) {
		// TODO Auto-generated method stub
		int width = bmpOriginal.getWidth(); //��ȡλͼ�Ŀ� 
		int height = bmpOriginal.getHeight(); //��ȡλͼ�ĸ� 

		int[] pixels = new int[width*height]; //ͨ��λͼ�Ĵ�С�������ص����� 

		bmpOriginal.getPixels(pixels, 0, width, 0, 0, width, height); 
		int alpha = (byte)((pixels[0] & 0xFF000000)>>24); 
		for(int i = 0; i < height; i++){ 
			for(int j = 0; j < width; j++){ 
				int pixel_src = pixels[width * i + j]; 
				int red = ((pixel_src & 0x00FF0000 ) >> 16); 
				int green = ((pixel_src & 0x0000FF00) >> 8); 
				int blue = (pixel_src & 0x000000FF); 
				
				int temp = (int)(0.299 * ((double)red) + 0.587 * ((double)green) + 0.114 * ((double)blue));

				if (temp < 245){
					red = 255; green = 0; blue = 0;
				}else{
					red = 255; green = 255; blue = 255;
				}
				
				pixel_src = (alpha<<24) | (red << 16) | (green << 8) | blue; 
				pixels[width * i + j] = pixel_src; 
				} 
			} 
		Bitmap bmpPapercut = Bitmap.createBitmap(width, height, Config.ARGB_8888); 
		bmpPapercut.setPixels(pixels, 0, width, 0, 0, width, height); 
		return bmpPapercut; 
	}

	public static Bitmap toYangkeFilter(Bitmap bmpOriginal) {
		// TODO Auto-generated method stub
		int width = bmpOriginal.getWidth(); //��ȡλͼ�Ŀ� 
		int height = bmpOriginal.getHeight(); //��ȡλͼ�ĸ� 

		int[] pixels = new int[width*height]; //ͨ��λͼ�Ĵ�С�������ص����� 

		bmpOriginal.getPixels(pixels, 0, width, 0, 0, width, height); 
		for(int times=1; times<=4; times++){//�����Ĵε���
			for(int i = 0; i < height-1; i++){ 
				for(int j = 0; j < width-1; j++){ 
					
					/* λ��ʾ��
					 * pixel_1 pixel_2
					 * pixel_3
					 */
					
					int pixel_1 = pixels[width * i + j];
					int pixel_2 = pixels[width * i + j +1];
					int pixel_3 = pixels[width * (i+1) + j];
					
					if(times==1||times==2){
						if( (pixel_1 & 0x00FFFFFF) == 0x00FFFFFF ){
							//pixel_1Ϊ��ɫ
							if( ((pixel_2 & 0x00FFFFFF) == 0x00FF0000) || 
									((pixel_3 & 0x00FFFFFF) == 0x00FF0000) ){
							//pixel_2Ϊ��ɫ �� pixel_3Ϊ��ɫ
								pixel_1 = ((pixel_1 & 0xFF000000) | 0x00FF0000); //((����͸����)����Ϊ��ɫ) 
								pixels[width * i + j] = pixel_1; 
							}
						}
					}
					
					if(times==3 || times==4){
						if( (pixel_1 & 0x00FFFFFF) == 0x00FF0000 ){
							//pixel_1Ϊ��ɫ
							if( ((pixel_2 & 0x00FFFFFF) == 0x00FFFFFF) || 
									((pixel_3 & 0x00FFFFFF) == 0x00FFFFFF) ){
							//pixel_2Ϊ��ɫ �� pixel_3Ϊ��ɫ
								pixel_1 = ((pixel_1 & 0xFF000000) | 0x00FFFFFF); //((����͸����)����Ϊ��ɫ) 
								pixels[width * i + j] = pixel_1; 
							}
						}
					}					
				} 
			} 
		}
		
		
		Bitmap bmpPapercut = Bitmap.createBitmap(width, height, Config.ARGB_8888); 
		bmpPapercut.setPixels(pixels, 0, width, 0, 0, width, height); 
		return bmpPapercut; 
	}
	
}
