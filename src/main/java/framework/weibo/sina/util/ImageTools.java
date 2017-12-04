package framework.weibo.sina.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 通过java获取图片的宽和高
 * 
 * @author sunlightcs 2011-6-1 http://hi.juziku.com/sunlightcs/
 */
public class ImageTools {
	public Image img;
	public int height;
	public int width;
	public ImageTools(){}
	
	public ImageTools(InputStream inputStream){
		try {
			img = ImageIO.read(inputStream);//构造Image对象
			width = img.getWidth(null);    // 得到源图宽  
		    height = img.getHeight(null);  // 得到源图长  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			img = null;
		}
	}

	/**
	 * 获取图片宽度
	 * 
	 * @param file
	 *            图片文件
	 * @return 宽度
	 */
	public static int getImgWidth(File file) {
		InputStream is = null;
		BufferedImage src = null;
		int ret = -1;
		try {
			is = new FileInputStream(file);
			src = javax.imageio.ImageIO.read(is);
			ret = src.getWidth(null); // 得到源图宽
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 获取图片高度
	 * 
	 * @param file
	 *            图片文件
	 * @return 高度
	 */
	public static int getImgHeight(File file) {
		InputStream is = null;
		BufferedImage src = null;
		int ret = -1;
		try {
			is = new FileInputStream(file);
			src = javax.imageio.ImageIO.read(is);
			ret = src.getHeight(null); // 得到源图高
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	/**
	 * 获取网络图片
	 * @param url
	 * @return 
	 * 		字节数组
	 */
	public static byte[] getRemoteImgByteArray(String sourceurl){
		byte[] data = null;
		//new一个URL对象  
        URL url = null;
		try {
			url = new URL(sourceurl);
			  //打开链接  
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	        //设置请求方式为"GET"  
	        conn.setRequestMethod("GET");  
	        //通过输入流获取图片数据  
	        InputStream inStream = conn.getInputStream();  
	        //得到图片的二进制数据，以二进制封装得到数据，具有通用性  
	        data = readInputStream(inStream); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			data = null;
		}
        return data;
	}
   public static byte[] readInputStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        //创建一个Buffer字符串  
        byte[] buffer = new byte[1024];  
        //每次读取的字符串长度，如果为-1，代表全部读取完毕  
        int len = 0;  
        //使用一个输入流从buffer里把数据读取出来  
        while( (len=inStream.read(buffer)) != -1 ){  
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
            outStream.write(buffer, 0, len);  
        }  
        //关闭输入流  
        inStream.close();  
        //把outStream里的数据写入内存  
        return outStream.toByteArray();  
    } 
   
   /** 
    * 按照宽度还是高度进行压缩 
    * @param w int 最大宽度 
    * @param h int 最大高度 
    */  
   public BufferedImage resizeFix(int w, int h) throws IOException {  
       if (width / height > w / h) {  
           resizeByWidth(w); 
       } else {  
           resizeByHeight(h);  
       }  
       return resize(w, h);
   }  
   /** 
    * 以宽度为基准，等比例放缩图片 
    * @param w int 新宽度 
    */  
   public int resizeByWidth(int w) throws IOException {  
	   return (int) (height * w / width);  
        
   }  
   /** 
    * 以高度为基准，等比例缩放图片 
    * @param h int 新高度 
    */  
   public int resizeByHeight(int h) throws IOException {  
       return(int) (width * h / height);  
   }  
   /** 
    * 强制压缩/放大图片到固定的大小 
    * @param w int 新宽度 
    * @param h int 新高度 
    */  
   public BufferedImage resize(int w, int h) throws IOException {  
       // SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢  
       BufferedImage image = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB );   
       image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图  
       return image;
   } 
   public void downloadResizeImage(String originalDir,BufferedImage image) throws IOException{
	   File destFile = new File(originalDir);  
       FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流  
       // 可以正常实现bmp、png、gif转jpg  
       JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
       encoder.encode(image); // JPEG编码  
       out.close();  
   }
}
