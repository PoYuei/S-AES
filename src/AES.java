import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.Arrays;


public class AES {
    private final Stage stage;
    private final Scene aesScene;
    private final Scene attackScene;
    private final Scene CBCScene;
    private final AESClient aesClient;

    public AES(Stage stage, Scene aesScene, Scene attackScene, Scene CBCScene){
        this.stage = stage;
        this.aesScene = aesScene;
        this.attackScene = attackScene;
        this.CBCScene = CBCScene;
        this.aesClient = new AESClient();
    }

    public void switch_to(){
        Platform.runLater(() -> stage.setScene(attackScene));
    }

    public void switch_to2(){Platform.runLater(() -> stage.setScene(aesScene));}

    public void switch_to3(){Platform.runLater(() -> stage.setScene(CBCScene));}

    // 将字符串类型的二进制转换为字符串类型的十六进制
    public static String binaryToHex(String binaryString) {
        // 将二进制字符串转换为十进制整数
        int decimalValue = Integer.parseInt(binaryString, 2);

        // 将十进制整数转换为十六进制字符串

        return Integer.toHexString(decimalValue).toUpperCase();
    }

    public String encryption(String ming, String key) {
        //获得密钥
        char[][] keys = get_key(key);
        //存储当前状态
        char[][] state = new char[2][8];
        //轮密钥加（异或)
        state[0] = XOR(ming.substring(0,8).toCharArray(), keys[0]);
        state[1] = XOR(ming.substring(8).toCharArray(), keys[1]);

        //半字节代替,与SubNib操作一样。
        state[0] = SubNib(state[0]);
        state[1] = SubNib(state[1]);

        //行移位,
        state[1] = RotNib(state[1]);

        //列混淆
        state = mix(martrix,state);

        //轮密钥加
        state[0] = XOR(state[0], keys[2]);
        state[1] = XOR(state[1], keys[3]);

        //半字节代替。
        state[0] = SubNib(state[0]);
        state[1] = SubNib(state[1]);

        //行移位,
        state[1] = RotNib(state[1]);

        //轮密钥加
        state[0] = XOR(state[0], keys[4]);
        state[1] = XOR(state[1], keys[5]);


        //格式转换
        String state0 = String.valueOf(state[0]);
        String state1 = String.valueOf(state[1]);
        return state0.concat(state1);
    }

    public static char[][] RCON = new char[][]{
            {'1', '0', '0', '0', '0', '0', '0', '0',},
            {'0', '0', '1', '1', '0', '0', '0', '0', }
    };
    public static int[][] martrix = new int[][]{{1, 4},{4, 1}};
    public static int[][] I_martrix = new int[][]{{9, 2},{2, 9}};
    //S盒与逆S盒
    public static int[][] S_Box = new int[][] {
            { 9,  4, 10, 11},
            {13,  1,  8,  5},
            { 6,  2,  0,  3},
            {12, 14, 15,  7}
    };
    public static int[][] I_S_Box = new int[][] {
            {10,  5,  9, 11},
            { 1,  7,  8, 15},
            { 6,  0,  2,  3},
            {12,  4, 13, 14}
    };

    // SubNib(RotNib(a))函数
    // 每次只处理8个bit
    public static char[] Sub_Rot(char[] a){
        return SubNib(RotNib(a));
    }
    private static char[] RotNib(char[] a){
        int[] b = new int[]{5,6,7,8,1,2,3,4};
        char[] ans = new char[b.length];
        for (int i=0;i<b.length;i++)
        {
            ans[i] = a[b[i]-1];     //由于定义的常量是1起始，此处-1校正数组索引。
        }
        return ans;
    }
    private static char[] SubNib(char[] ans){

        int x1 = S_Box[2*ans[0]+ans[1]-3*'0'][2*ans[2]+ans[3]-3*'0'];  //-3*'0'来保证char类型的ASCII码的差值在范围内，否则越界
        int x2 = S_Box[2*ans[4]+ans[5]-3*'0'][2*ans[6]+ans[7]-3*'0'];

        //直接定义string类然后while循环内改为s+="0"，ans的赋值删去toString亦可
        StringBuilder s = new StringBuilder(Integer.toBinaryString(16 * x1 + x2)); //将x1左移二位后与x2做加法即等于连接且无需考虑x2的大小影响
        while(s.length()<8){
            s.insert(0, "0");
        }
        ans = s.toString().toCharArray();
        return ans;
    }
    private static char[] I_SubNib(char[] ans){

        int x1 = I_S_Box[2*ans[0]+ans[1]-3*'0'][2*ans[2]+ans[3]-3*'0'];  //-3*'0'来保证char类型的ASCII码的差值在范围内，否则越界
        int x2 = I_S_Box[2*ans[4]+ans[5]-3*'0'][2*ans[6]+ans[7]-3*'0'];

        //直接定义string类然后while循环内改为s+="0"，ans的赋值删去toString亦可
        StringBuilder s = new StringBuilder(Integer.toBinaryString(16 * x1 + x2)); //将x1左移二位后与x2做加法即等于连接且无需考虑x2的大小影响
        while(s.length()<8){
            s.insert(0, "0");
        }
        ans = s.toString().toCharArray();
        return ans;
    }


    //根据输入的16进制数获取密钥。
    public  static char[][] get_key(String hex){
        hex = binaryToHex(hex);

        char[][] keys = new char[6][8];
        //hex需要时十六进制数
        StringBuilder raw = new StringBuilder(Integer.toBinaryString(Integer.parseInt(hex, 16)));
        while (raw.length()<16){
            raw.insert(0, "0");
        }
        //16个二进制位划分为原始的两个密钥。  此处按照PDF处理好了密钥，但是为了方便计算，将对每组密钥进行重组.
        keys[0] = raw.substring(0,8).toCharArray();
        keys[1] = raw.substring(8).toCharArray();
        keys[2] = XOR(XOR(keys[0],RCON[0]),Sub_Rot(keys[1]));
        keys[3] = XOR(keys[2],keys[1]);
        keys[4] = XOR(XOR(keys[2],RCON[1]),Sub_Rot(keys[3]));
        keys[5] = XOR(keys[4], keys[3]);

        //重组如下：
        for (int i=0;i<3;i++){
            //提取为两字符串
            String s1 = String.valueOf(keys[i*2]);
            String s2 = String.valueOf(keys[i*2+1]);
            //每半字节一组
            String s1_1 = s1.substring(0,4);
            String s1_2 = s1.substring(4);

            String s2_1 = s2.substring(0,4);
            String s2_2 = s2.substring(4);
            //每组向量的左边共8个数为一组，右边8个位一组
            keys[i*2] = (s1_1+s2_1).toCharArray();
            keys[i*2+1] = (s1_2+s2_2).toCharArray();
        }

        return keys;
    }
    private static char[] XOR(char[] a, char[] b){
        //GF乘法
        if(b.length==5){
            //a 为积，b为formulate
            int n=b.length;
            //第一个非零元素的索引
            int temp = 0;
            char[] ans;
            if(a.length<4){
                String s = String.valueOf(a);
                while(s.length()<4){
                    s = "0" + s;
                };
                ans = s.toCharArray();
                return ans;
            }
            while(a.length-temp>4) {
                for (int i = 0; i < n; i++) {
                    a[temp+i] = XOR(a[temp+i], b[i]);
                }
                while(a[temp]=='0'){
                    if(a.length-temp<5) { break; }
                    temp++;
                }
            }

            return new char[]{a[temp],a[temp+1],a[temp+2],a[temp+3]};
        } else{ //正常异或
            int n=a.length;
            char[] c = new char[n];
            for(int i=0;i<n;i++){
                c[i] = XOR(a[i], b[i]);
            }
            return c;
        }
    }
    private static char XOR(char a, char b){
        return a==b?'0':'1';
    }

    //GF(2^4)域上的矩阵乘法，仅对于二阶。
    //传入的b是二进制数组，2行8列,a是左乘的给定矩阵
    private static char[][] mix(int[][] b,char[][] a){
        char[][] ans = new char[2][8];
        int x1 = 8*a[0][0] + 4*a[0][1] + 2*a[0][2] + a[0][3] - 15*'0';
        int x2 = 8*a[0][4] + 4*a[0][5] + 2*a[0][6] + a[0][7] - 15*'0';
        int x3 = 8*a[1][0] + 4*a[1][1] + 2*a[1][2] + a[1][3] - 15*'0';
        int x4 = 8*a[1][4] + 4*a[1][5] + 2*a[1][6] + a[1][7] - 15*'0';
        //四个半字节
        char[] y1 = XOR(GF_mul(b[0][0],x1),GF_mul(b[0][1],x3));
        char[] y2 = XOR(GF_mul(b[0][0],x2),GF_mul(b[0][1],x4));
        char[] y3 = XOR(GF_mul(b[1][0],x1),GF_mul(b[1][1],x3));
        char[] y4 = XOR(GF_mul(b[1][0],x2),GF_mul(b[1][1],x4));
        //四个半字节对应的字符串
        String s1 = String.valueOf(y1);
        String s2 = String.valueOf(y2);
        String s3 = String.valueOf(y3);
        String s4 = String.valueOf(y4);

        ans[0] = s1.concat(s2).toCharArray();
        ans[1] = s3.concat(s4).toCharArray();
        return ans;
    }

    //GF(2^4)域上的两数乘法
    private static char[] GF_mul(int a, int b){
        //将二者相乘然后位mod2
        String a_s=Integer.toBinaryString(a);
        String b_s=Integer.toBinaryString(b);
        a = Integer.parseInt(a_s);
        b = Integer.parseInt(b_s);
        //直接转为int相乘则每一位代表的是对应项的系数
        String s = String.valueOf(a*b);
        //转为char数组mod 2
        char[] c = s.toCharArray();
        for (int i=0;i<c.length;i++){
            //防止char类型越界显示错误
            c[i] = (char)((c[i]-'0')%2+'0');
        }
        //x^4 + x^2 + 1的二进制表示，与乘积结果从左到右异或，至结果小于等于4位则结束计算
        char[] formulate = new char[] {'1','0','0','1','1'};

        c = XOR(c,formulate);
        return c;

    }

    public String decryption(String mi, String key){
        //获得密钥
        char[][] keys = get_key(key);
        //存储当前状态
        char[][] state = new char[2][8];
        //轮密钥加（异或)
        state[0] = XOR(mi.substring(0,8).toCharArray(), keys[4]);
        state[1] = XOR(mi.substring(8).toCharArray(), keys[5]);

        //行移位,
        state[1] = RotNib(state[1]);

        //半字节代替,与SubNib操作一样。
        state[0] = I_SubNib(state[0]);
        state[1] = I_SubNib(state[1]);

        //轮密钥加
        state[0] = XOR(state[0], keys[2]);
        state[1] = XOR(state[1], keys[3]);

        //列混淆,使用域内逆矩阵
        state = mix(I_martrix,state);

        //行移位,
        state[1] = RotNib(state[1]);

        //半字节代替。
        state[0] = I_SubNib(state[0]);
        state[1] = I_SubNib(state[1]);

        //轮密钥加
        state[0] = XOR(state[0], keys[0]);
        state[1] = XOR(state[1], keys[1]);

        String state0 = String.valueOf(state[0]);
        String state1 = String.valueOf(state[1]);
        return state0.concat(state1);
    }
    public String ASC_AES(int Mode, String words, String key) {
        StringBuilder binaryStr = new StringBuilder();
        StringBuilder result = new StringBuilder();

        if (Mode == 0) {  // Encryption
            for (char c : words.toCharArray()) {
                binaryStr.append(String.format("%16s", Integer.toBinaryString(c)).replace(' ', '0'));
            }

            for (int i = 0; i < binaryStr.length(); i += 16) {
                String byteStr = binaryStr.substring(i, i + 16);
                String encrypted = encryption(byteStr, key);
                int encryptedAscii0 = Integer.parseInt(encrypted, 2);
                result.append((char) encryptedAscii0);
            }
            return result.toString();

        } else if (Mode == 1) {  // Decryption
            for (char c : words.toCharArray()) {
                binaryStr.append(String.format("%16s", Integer.toBinaryString(c)).replace(' ', '0'));
            }

            for (int i = 0; i < binaryStr.length(); i += 16) {
                String byteStr = binaryStr.substring(i, i + 16);
                String decrypted = decryption(byteStr, key);
                int encryptedAscii0 = Integer.parseInt(decrypted, 2);
                result.append((char) encryptedAscii0);
            }
            return result.toString();

        } else {
            throw new IllegalArgumentException("Invalid Mode. Use 0 for encryption and 1 for decryption.");
        }
    }

    public String double_encryption(String words, String key){
        String key1 = key.substring(0,16);
        String key2 = key.substring(16);

        String temp = encryption(words, key1);

        return encryption(temp,key2);

    }
    public String double_decryption(String words, String key){
        String key1 = key.substring(0,16);
        String key2 = key.substring(16);

        String temp = decryption(words, key2);

        return decryption(temp,key1);

    }
    public String triple_encryption(String words, String key){
        String key1 = key.substring(0,16);
        String key2 = key.substring(16,32);
        String key3 = key.substring(32);
        //第一次加密
        String temp = encryption(words, key1);
        //第二次加密
        temp = encryption(temp, key2);
        //第三次加密
        return encryption(temp, key3);
    }
    public String triple_decryption(String ming, String key){
        String key1 = key.substring(0,16);
        String key2 = key.substring(16,32);
        String key3 = key.substring(32);
        //第一次加密
        String temp = decryption(ming, key1);
        //第二次加密
        temp = encryption(temp, key2);
        //第三次加密
        return encryption(temp, key3);
    }

    //P表示明文，C表示密文
    public void middle_attack(String P,String P1, String C,String C1){

        String[] temp = new String[65536];
        int num = 1;
        //建表
        for (int i=0;i<65536;i++){
            String s =Integer.toBinaryString(i);
            while(s.length()<16){
                s = "0" + s;
            }
            temp[i] = encryption(P, s);
        }

        //求解并且查表
        for (int j=0;j<65536;j++){
            String s =Integer.toBinaryString(j);
            while(s.length()<16){
                s = "0" + s;
            }
            String mid = decryption(C, s);
            if(Arrays.asList(temp).contains(mid)){

                int x = find(mid,temp);

                String a =Integer.toBinaryString(x);
                while(a.length()<16){ a = "0" + a;}

                String b =Integer.toBinaryString(j);
                while(b.length()<16){b = "0" + b;}

                if (encryption(P1, a).equals(decryption(C1, b))) {
                    System.out.println(111);
                    String message = "报告长官，找到敌方密钥！！！" + a + b + "第" + num + "次找到！！！";
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message));
                    num++;
                }
            }
        }
    }
    public static int find(String findstr, String[] arr) {//返回int接收索引

        for (int i = 0; i < arr.length; i++) {//去遍历搜索
            if (findstr.equals(arr[i])) {   //字符串需用.equals来对比
                return i;	//搜到了就返回i
            }
        }
        return -1;  //没搜到就返回-1
    }

    public String CBC(String word, String IV, String key, int mode){
        int n = word.length();
        StringBuilder binaryStr = new StringBuilder();
        StringBuilder result = new StringBuilder();
        String temp = IV;   //初始向量
        char[] c = word.toCharArray();
        for(int i=0;i<c.length;i=i+1){
            binaryStr.append(String.format("%16s", Integer.toBinaryString(c[i])).replace(' ', '0'));

        }
        if(mode==0)//加密
        {
            for (int i = 0; i < binaryStr.length(); i += 16) {
                String byteStr = binaryStr.substring(i, i + 16);
                String encrypted = encryption(String.valueOf(XOR(byteStr.toCharArray(), temp.toCharArray())), key);
                temp = encrypted;
                int encryptedAscii0 = Integer.parseInt(encrypted, 2);

                result.append((char) encryptedAscii0);
            }
            return result.toString();

        } else if (mode==1) {   //解密
            for (int i = 0; i < binaryStr.length(); i += 16) {
                String byteStr = binaryStr.substring(i, i + 16);
                String encrypted = String.valueOf(XOR(decryption(byteStr, key).toCharArray(),temp.toCharArray()));
                temp = byteStr;//更新向量为刚使用的密文
                int encryptedAscii0 = Integer.parseInt(encrypted, 2);

                result.append((char) encryptedAscii0);
            }
            return result.toString();

        } else {
            throw new IllegalArgumentException("Invalid Mode. Use 0 for encryption and 1 for decryption.");
        }
    }

}
