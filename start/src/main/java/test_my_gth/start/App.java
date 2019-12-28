package test_my_gth.start;

import java.util.Date;

/**
 * Hello world!
 *
 */
public class App 

{//github 1233-ttttttttt changes
    public static void main( String[] args )
    {
    	//3333333333333
        System.out.println( "Hello World!" +new Date());
        
        //进制
        int two=0b111;
        System.out.println("two="+two);
        int eight=0111;
        System.out.println("eight="+eight);
        int ten=111;
        System.out.println("ten="+ten);
        int sixteen=0x111;
        System.out.println("sixteen="+sixteen);
        /*Unicode:UTF-8、UTF-16
        1.八进制转义序列：\ + 1到3位5数字；范围'\000'~'\377'      \0：空字符
        2.Unicode转义字符：\ u + 四个十六进制数字；0~65535       \u0000：空字符
        */
        System.out.println("aa\007");
        System.out.println("aa\u0007");
    }
    //33333333333333
    public static void t1() {
    	System.out.println("t1");
    }
    public static void t2() {
    	System.out.println("t2");
    }
    //20190904
    //2
    //1+1=2 3333333333333333333333333333333333444xixihaha
    //
    //test 20191228
}
