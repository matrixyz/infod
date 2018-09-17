package com.zzsc.infod.util;


import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.ServletOutputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import java.util.Iterator;

public class UtilImg {
    /**
     * 图片裁剪
     * @param srcImageFile 图片裁剪地址

     * @param destWidth 图片裁剪宽度
     * @param destHeight 图片裁剪高度
     */
    public final static void cutImage(String srcImageFile,
                                      int destWidth, int destHeight,ServletOutputStream out) {
        try {
            Iterator iterator = ImageIO.getImageReadersByFormatName("JPEG");/*PNG,BMP*/
            ImageReader reader = (ImageReader)iterator.next();/*获取图片尺寸*/
            InputStream inputStream = new FileInputStream(srcImageFile);
            ImageInputStream iis = ImageIO.createImageInputStream(inputStream);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rectangle = new Rectangle(0,0, destWidth, destHeight);/*指定截取范围*/
            param.setSourceRegion(rectangle);
            BufferedImage bi = reader.read(0,param);

            ImageIO.write(bi, "JPEG", out);
        } catch (Exception e) {
            System.out.println("图片裁剪出现异常:"+e);
        }
    }
    /*
     * 根据尺寸图片居中裁剪
     */
    public static void cutCenterImage(String src, int w,int h ,ServletOutputStream out) throws IOException{
        Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");
        ImageReader reader = (ImageReader)iterator.next();


        try {
            InputStream in=new FileInputStream(src);
            ImageInputStream iis = ImageIO.createImageInputStream(in);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            int imageIndex = 0;
            Rectangle rect = new Rectangle((reader.getWidth(imageIndex)-w)/2, (reader.getHeight(imageIndex)-h)/2, w, h);
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0,param);
            ImageIO.write(bi, "jpg", out);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     使用zoomImage将图片放大或缩小到指定大小，然后居中裁剪
     */


    public static void main(String[] args) {
        try {
            //cutCenterImg("D:/test/upload/201801/1516086842.jpg",200,100);
          //  String x=getFormatName(new File("D:\\test\\upload\\201608\\1471402395.jpg"));
            String result="C:\\Users\\lenovo\\IdeaProjects\\dtt\\web\\spriteSpin\\images\\R2\\" ;
            for (int i = 1; i <61 ; i++) {
                String sourcName="C:\\Users\\lenovo\\IdeaProjects\\dtt\\web\\spriteSpin\\images\\R2\\"+"0 ("+i+").jpg";
               String resultName="a_0"+i+".jpg";
                if (i<10)
                    resultName="a_00"+i+".jpg";


                scale(sourcName,result+resultName,2,false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * 图片缩放，只根据用参数中的宽度w 按照原始宽高比例缩小
     */
    public static BufferedImage zoomImages(String DirRoot , int w,int h ) throws Exception {
        BufferedImage bufImg = ImageIO.read( new File(DirRoot ));
        double wr=0,hr=0;
        if(w==0){
            w=bufImg.getWidth();
            h=bufImg.getHeight();
        }


        if (h==0)
            h=(int)(bufImg.getHeight()*w*1.0/bufImg.getWidth());
        Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);
        wr=w*1.0/bufImg.getWidth();
        hr=h*1.0 / bufImg.getHeight();
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        Itemp = ato.filter(bufImg, null);
        return (BufferedImage) Itemp;

    }

    /*
     * 图片缩放，只根据用参数中的宽度w 按照原始宽高比例缩小
     */
    public static BufferedImage zoomImage(String src, int w,int h ) throws Exception {



        double wr=0,hr=0;
        File srcFile = new File(src);

        BufferedImage bufImg = ImageIO.read(srcFile);
        if (h==0)
            h=(int)(bufImg.getHeight()*w*1.0/bufImg.getWidth());
        Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);
        wr=w*1.0/bufImg.getWidth();
        hr=h*1.0 / bufImg.getHeight();
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        Itemp = ato.filter(bufImg, null);
       return (BufferedImage) Itemp;

    }
    /** *//**
     * 缩放图像
     * @param srcImageFile 源图像文件地址
     * @param result       缩放后的图像地址
     * @param scale        缩放比例
     * @param flag         缩放选择:true 放大; false 缩小;
     */
    public static void scale(String srcImageFile, String result, int scale, boolean flag)
    {
        try
        {
            BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
            int width = src.getWidth(); // 得到源图宽
            int height = src.getHeight(); // 得到源图长
            if (flag)
            {
                // 放大
                width = width * scale;
                height = height * scale;
            }
            else
            {
                // 缩小
                width = width / scale;
                height = height / scale;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    /** *//**
     * 图像类型转换 GIF->JPG GIF->PNG PNG->JPG PNG->GIF(X)
     */
    public static void convert(String source, String result)
    {
        try
        {
            File f = new File(source);
            f.canRead();
            f.canWrite();
            BufferedImage src = ImageIO.read(f);
            ImageIO.write(src, "JPG", new File(result));
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /** *//**
     * 彩色转为黑白
     * @param source
     * @param result
     */
    public static void gray(String source, String result)
    {
        try
        {
            BufferedImage src = ImageIO.read(new File(source));
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            ColorConvertOp op = new ColorConvertOp(cs, null);
            src = op.filter(src, null);
            ImageIO.write(src, "JPEG", new File(result));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String getFormatName(Object o) {
        try {
            // Create an image input stream on the image
            ImageInputStream iis = ImageIO.createImageInputStream(o);

            // Find all image readers that recognize the image format
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) {
                // No readers found
                return null;
            }

            // Use the first reader
            ImageReader reader = iter.next();

            // Close stream
            iis.close();

            // Return the format name
            return reader.getFormatName();
        } catch (IOException e) {
            //
        }

        // The image could not be read
        return null;
    }
    /**
     * 按照 宽高 比例压缩
     *
     * @param img
     * @param width
     * @param height
     * @param out
     * @throws IOException
     */
    public static void thumbnail_w_h(File img, int width, int height,
                                     OutputStream out) throws IOException {
        BufferedImage bi = ImageIO.read(img);
        double srcWidth = bi.getWidth(); // 源图宽度
        double srcHeight = bi.getHeight(); // 源图高度

        double scale = 1;

        if (width > 0) {
            scale = width / srcWidth;
        }
        if (height > 0) {
            scale = height / srcHeight;
        }
        if (width > 0 && height > 0) {
            scale = height / srcHeight < width / srcWidth ? height / srcHeight
                    : width / srcWidth;
        }

        thumbnail(img, (int) (srcWidth * scale), (int) (srcHeight * scale), out);

    }

    /**
     * 按照固定宽高原图压缩
     *
     * @param img
     * @param width
     * @param height
     * @param out
     * @throws IOException
     */
    public static void thumbnail(File img, int width, int height,
                                 OutputStream out) throws IOException {
        BufferedImage BI = ImageIO.read(img);
        Image image = BI.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        BufferedImage tag = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.setColor(Color.RED);
        g.drawImage(image, 0, 0, null); // 绘制处理后的图
        g.dispose();
        ImageIO.write(tag, "JPEG", out);
    }

    /**
     * 按照宽高裁剪
     *
     * @param srcImageFile
     * @param destWidth
     * @param destHeight
     * @param out
     */
    public static void cut_w_h(File srcImageFile, int destWidth,
                               int destHeight, OutputStream out) {
        cut_w_h(srcImageFile, 0, 0, destWidth, destHeight, out);
    }

    public static void cut_w_h(File srcImageFile, int x, int y, int destWidth,
                               int destHeight, OutputStream out) {
        try {
            Image img;
            ImageFilter cropFilter;
            // 读取源图像
            BufferedImage bi = ImageIO.read(srcImageFile);
            int srcWidth = bi.getWidth(); // 源图宽度
            int srcHeight = bi.getHeight(); // 源图高度

            if (srcWidth >= destWidth && srcHeight >= destHeight) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight,
                        Image.SCALE_DEFAULT);

                cropFilter = new CropImageFilter(x, y, destWidth, destHeight);
                img = Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(image.getSource(), cropFilter));
                BufferedImage tag = new BufferedImage(destWidth, destHeight,
                        BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, null); // 绘制截取后的图
                g.dispose();
                // 输出为文件
                ImageIO.write(tag, "JPEG", out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// #cut_w_h

    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 根据图片路径生成缩略图 </p>
     * @param  imgFile    原图片路径
     * @param w            缩略图宽
     * @param h            缩略图高

     * @param force        是否强制按照宽高生成缩略图(如果为false，则生成最佳比例缩略图)
     */
    public static void thumbnailImage(File imgFile, int w, int h,   boolean force) {

        try {

            Image img = ImageIO.read(imgFile);
            if (!force) {
                // 根据原图与要求的缩略图比例，找到最合适的缩略图比例
                int width = img.getWidth(null);
                int height = img.getHeight(null);
                if ((width * 1.0) / w < (height * 1.0) / h) {
                    if (width > w) {
                        h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w / (width * 1.0)));
                    }
                } else {
                    if (height > h) {
                        w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h / (height * 1.0)));
                    }
                }
            }
            BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.getGraphics();
            g.drawImage(img, 0, 0, w, h, Color.LIGHT_GRAY, null);
            g.dispose();
            String p = imgFile.getPath();
            // 将图片保存在原目录并加上前缀
            ImageIO.write(bi, "JPEG", new File(p.substring(0, p.lastIndexOf(File.separator)) + File.separator  + imgFile.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

