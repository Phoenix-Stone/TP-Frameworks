package framework.weibo.sina.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

import com.sun.imageio.plugins.bmp.BMPImageReader;
import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageReader;
import com.sun.imageio.plugins.png.PNGImageReader;

/**
 * 临时存储上传图片的内容，格式，文件信息等
 * 
 */
public class ImageItem {
	private byte[] content;
	private String name;
	private String contentType;
	
	public static final String DEFAULT_NAME = "pic";

	public ImageItem(byte[] content){
		this(DEFAULT_NAME, content);
	}

	public ImageItem(String name, byte[] content) {
		String imgtype = getContentType(content);
		this.content = content;
		this.name = name;
		this.contentType = imgtype;
	}

	public byte[] getContent() {
		return content;
	}

	public String getName() {
		return name;
	}

	public String getContentType() {
		return contentType;
	}

	public static String getContentType(byte[] mapObj) {

		String type = null;
		ByteArrayInputStream bais = null;
		MemoryCacheImageInputStream mcis = null;
		try {
			bais = new ByteArrayInputStream(mapObj);
			mcis = new MemoryCacheImageInputStream(bais);
			Iterator<ImageReader> itr = ImageIO.getImageReaders(mcis);
			while (itr.hasNext()) {
				ImageReader reader = itr.next();
				if (reader instanceof GIFImageReader) {
					type = "image/gif";
				} else if (reader instanceof JPEGImageReader) {
					type = "image/jpeg";
				} else if (reader instanceof PNGImageReader) {
					type = "image/png";
				} else if (reader instanceof BMPImageReader) {
					type = "application/x-bmp";
				}
			}
		} finally {
			if (bais != null) {
				try {
					bais.close();
				} catch (IOException ioe) {

				}
			}
			if (mcis != null) {
				try {
					mcis.close();
				} catch (IOException ioe) {

				}
			}
		}
		return type;
	}
}
