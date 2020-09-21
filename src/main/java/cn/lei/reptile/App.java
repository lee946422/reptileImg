package cn.lei.reptile;

import java.util.Scanner;

/**
 * @ClassName App
 * @Description TODO
 * @Author lei
 * @Date 2020/9/17 17:42
 * @Version 1.0
 */
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Boolean flag = true;
        String word = null;
        while (flag) {
            System.out.print("请输入想下载的图片类型:");
           word = scanner.next().trim();
            if ("".equals(word) || word == null) {
                System.out.println("无效的图片类型,请重新输入!!");
            } else {
                //下载并打印信息
                int result = JsoupParse.getInfo(word);
                if (0 == result) {
                    System.out.println("该检索词下没有匹配的图片");
                    continue;
                }
                if (-1 == result) {
                    System.out.println("该检索下仅有一页匹配");
                    System.out.println(JsoupParse.saveImg(1,1));
                    continue;
                }
                int minPage = 0;
                int maxPage = 0;
                System.out.println("总共"+result+"页,每页10-40张.从第几页开始下载?");

                System.out.print("开始页:");
                try {
                    minPage = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("非法页码,请用1,2,3,...表示");
                    continue;
                }
                System.out.print("结束页:");
                try {
                    maxPage = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("非法页码,请用1,2,3,...表示");
                    continue;
                }
                if (maxPage > result) {
                    System.out.println("超出页码最大值,无法识别");
                    continue;
                } else if (maxPage < minPage) {
                    System.out.println("开始页码大于结束页码,不要瞎搞");
                    continue;
                }
                System.out.println(JsoupParse.saveImg(minPage,maxPage));
                System.out.println("退出:1");
                System.out.println("继续:2");
                int info= 0;
                try {
                    Scanner scanner1 = new Scanner(System.in);
                    info = scanner1.nextInt();
                } catch (Exception e) {

                }
                if (1 == info) {
                    System.exit(0);
                }
            }
        }


    }
}
