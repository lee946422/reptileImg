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
                System.out.println("总共"+JsoupParse.getInfo(word)+"页,每页10-40张.要下载多少页?");
                int page = scanner.nextInt();
                System.out.println(JsoupParse.saveImg(page));
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
