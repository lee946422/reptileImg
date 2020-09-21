package cn.lei.reptile;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName JsoupParse
 * @Description TODO 根据检索关键字下载图片
 * @Author lei
 * @Date 2020/9/17 17:30
 * @Version 1.0
 */
public class JsoupParse {
    //搜索发送的请求
    private static final String url = "http://pic.netbian.com/e/search/index.php";
    //获取的文档
    private static Document document = null;
    //图片下载地址
    private static ArrayList<String> imgSrcs = new ArrayList<>();
    //存储位置
    private static final String position = "D://image";
    //当前分类的总页数
    private static int pageSize = 0;
    //当前分类的seachid
    private static int searchid = 0;
    //检索关键字
    private static String word = "";



    /**
     * @return 下载结果反馈
     * @Description //TODO 文件存储处理 * @Date 18:35 2020/9/17
     * @Param []
     **/
    public static String saveImg(int page) {
        //每天生成一个文件夹,并按关键字分类
        String fileJia = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String parPath = position.concat("//") + fileJia.concat("//") + word;
        System.out.println("正在下载,存储路径为:" + parPath);
        File file1 = new File(parPath);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        List<String> searchImg = searchImg(page);
        if (!searchImg.isEmpty()) {
            for (int i = 0; i < searchImg.size(); i++) {
                String name = UUID.randomUUID().toString() + ".jpg";
                File file = new File(file1, name);
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendPort(searchImg.get(i), file);
            }
            return "下载成功,共下载" + searchImg.size() + "个文件";
        }
        return "下载路径不存在";
    }

    /**
     * @return void
     * @Description //TODO 发送请求获取保存文件 * @Date 18:34 2020/9/17
     * @Param [imgurl, dest]
     **/
    public static String sendPort(String imgurl, File dest) {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;

        try {
            URL url = new URL(imgurl);
            bis = new BufferedInputStream(url.openStream());
            bos = new BufferedOutputStream(new FileOutputStream(dest));

            byte[] bytes = new byte[2048];
            int len = 0;
            //开始读取并保存
            while ((len = (bis.read(bytes))) != -1) {
                bos.write(bytes, 0, len);
            }
            bos.flush();
            return "下载成功";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "地址不存在";
        } catch (IOException e) {
            e.printStackTrace();
            return "文件读写错误";
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @return 图片地址集合
     * @Description //TODO 获取图片的访问地址* @Date 18:09 2020/9/17
     * @Param []
     **/
    public static List<String> searchImg(int page) {
        for (int i = 0; i < page; i++) {
            Document documentPage = null;
            try {
                documentPage= Jsoup.connect("http://pic.netbian.com/e/search/result/index.php?page=" + i + "&searchid=" + searchid + "").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements imgHrefs = documentPage.select("a[href^=/tupian/]");
            for (Element imgHref : imgHrefs) {
                String href = "http://pic.netbian.com" + imgHref.attr("href");
                //拿到单个img得地址并进行访问
                try {
                    document = Jsoup.connect(href).data("query", "Java")
                            .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")//设置urer-agent  get();
                            .get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //拿到图片的父
                Elements divs = document.getElementsByClass("photo-pic");
                for (Element div : divs) {
                    Element img = div.select("img[src$=.jpg]").first();
                    //把地址放置在集合当中
                    imgSrcs.add("http://pic.netbian.com" + img.attr("src"));
                }
            }
        }
        return imgSrcs;
    }

    /**
     * @return int 页数
     * @Description //TODO 获取当前分类的页数,查询id信息 * @Date 11:00 2020/9/21
     * @Param [word] 查询主题
     **/
    public static int getInfo(String sword) {
        word = sword;
        try {
            document = Jsoup.connect(url)//请求参数
                    .data("tempid", "1")
                    .data("tbname", "photo")
                    .data("keyboard", word)
                    .postDataCharset("GBK")
                    .data("show", "title")
                    .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")//设置urer-agent  get();
                    .post();
            int childNodeSize = document.getElementsByClass("page").first().childNodeSize();
            //该分类下的总页数
            pageSize = Integer.valueOf(document.getElementsByClass("page").first().child(childNodeSize - 2).text());
            //获取该分类的searchid
            searchid = (int) (Integer.valueOf(document.baseUri().split("=")[1]));
            return pageSize;
        } catch (IOException e) {
            return 0;
        }
    }
}
