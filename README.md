# S-AES
The Simplified AES.

开发手册：
1. 介绍
	1.1 概述
	本用户指南提供了关于使用简化AES算法的加解密系统的详细信息。该系统旨在帮助您安全地加密和解密数据，以保护您的敏感信息。
	
	1.2 功能
	a.强大的AES算法加密和解密功能
	b.多种加密模式可供选择
	c.增加暴力破解密钥功能
	d.可以加密ASCLL字符串以及任何文字信息
	e.美观的ui
	
2. 安装与设置
	2.1 安装
	1.下载最新版本的IDEA开发环境并安装。
	2.打开IDEA，选择“导入项目”，然后导入AES算法加解密项目。
	2.2 配置
	1.打开项目后，确保项目依赖和库已正确配置。
	2.检查项目设置，包括编译器版本、Java SDK等，以确保项目可以正确构建。
	3.配置项目所需的外部依赖项或配置文件。

3. 功能介绍
	3.1 使用AES算法加密/解密
	如下图所示，若要加密/解密的内容是16位二进制数，则先选择使用几重加密(默认选择一重加密)，而后输入密钥(一、二、三重加密对应的密钥分别为16、32、48bit二进制数)，		再点击加密/解密按钮即可展示出结果。若要加密/解密的是字符串或者汉字，则在输入框中输入待处理的内容后再在密钥框中输入16bit二进制密钥，然后点击字符加密/字符解密		按钮，即可得到结果

 	 ![image](https://github.com/PoYuei/S-AES/assets/140697450/301ffa31-4e88-4982-a34d-91cc7e9c2785)
	![image](https://github.com/PoYuei/S-AES/assets/140697450/ffa37f3b-7837-49d0-9dc6-2397c51f59b2)
	![image](https://github.com/PoYuei/S-AES/assets/140697450/ac73107a-4f12-4a69-a7e0-d766bf9fba20)
	![image](https://github.com/PoYuei/S-AES/assets/140697450/439e0c5b-cff3-4854-b351-fe8065ca7017)


	3.2 暴力破解密钥
	在主界面点击密钥破解按钮即可进入密钥破解的界面 
	这里需要输入两组明/密文，然后点击暴力破解按钮进行密钥破解

 ![image](https://github.com/PoYuei/S-AES/assets/140697450/5c07a48b-ce86-4c76-b39d-ad2f078902ba)

 ![image](https://github.com/PoYuei/S-AES/assets/140697450/f604e907-ad48-4e97-84b1-671c6aca0c73)

	注意：这里运行时间可能有些长

	3.3 CBC加密
	在主界面点击CBC进入CBC加密界面

 ![image](https://github.com/PoYuei/S-AES/assets/140697450/1a5e656a-315c-4b2d-a080-88126b18743b)

	这里的明文可以是任意的字符串或者汉字数字等等、密钥为16bit二进制数，随机向量也是16bit二进制数，随机向量可以点击按钮生成

 ![image](https://github.com/PoYuei/S-AES/assets/140697450/d6d12cb2-c25f-4a56-a5df-990ff2c6ad91)
![image](https://github.com/PoYuei/S-AES/assets/140697450/e07d186e-7ac8-44b6-b599-29ba6f6a23e6)

 
4.	常见问题解答
q:为什么字符串和汉字加密的结果会是一堆奇怪的字符？
a:因为这里采用的是将二进制数转化为对应的UTF-8字符，若是采用ASCLL字符，转化时可能导致越界产生乱码，进而无法进行解密，因此考虑到算法的实用性，选用了UTF-8进行转换，而这其中包含所有的字符，自然会有一些较为生僻的

q:忘记了密钥怎么办？
a:若是有两组以上的二进制明密文，则可以使用暴力破解找到密钥，但是这可能有多个结果

5. 技术支持与反馈
5.1 技术支持联系信息
邮箱：3065055748@qq.com

5.2 反馈途径
邮箱联系我们即可





测试文档：可以下载AES_测试文档.pdf获取文档原文件。

第一关：

![image](https://github.com/PoYuei/S-AES/assets/140697450/de1b8dae-1cda-4b2c-a426-9708f5da4560)


第二关： 交叉测试
	经过作业表格提供的链接上的部分组进行交叉测试得到的结果与Lucas组等一样，通过检验。

 
第三关：扩展功能：
	加密字符串 Hello, world！得到输出，
而后对输出进行解密得到原字符串。可以正常加密解密。此处代码中使用了UTF-8编码，解决了乱码无法解密的问题。

  ![image](https://github.com/PoYuei/S-AES/assets/140697450/1b72b0e0-062a-4a6c-9b6c-4060b440eff6)
  ![image](https://github.com/PoYuei/S-AES/assets/140697450/54d5b31e-96b9-43cd-b3c7-88c72db5b663)


第四关：多重加密:
	1.双重加密：
 
  ![image](https://github.com/PoYuei/S-AES/assets/140697450/eecc5b4e-70ee-4f82-9f97-0314266374e7)
  ![image](https://github.com/PoYuei/S-AES/assets/140697450/24e280aa-86f8-4d0b-b7df-1e67f174fc0d)

	2.中间相遇攻击：
	两组数据为上一问给出的示例
1100110011001100	1111011000001001
1110101011110000	1111011011110001

 ![image](https://github.com/PoYuei/S-AES/assets/140697450/719e0c17-0dce-4c2b-9e9a-7d887238a97d)

恰好破解出来只有二个密钥如下，正好有一个密钥与给定密钥相同。
	 
![image](https://github.com/PoYuei/S-AES/assets/140697450/b23b0737-3efc-4a8c-a1e2-90ddbfe4bad9)

	3.三重加密：48bit密钥输出，加密解密正常。
 
 ![image](https://github.com/PoYuei/S-AES/assets/140697450/df963e41-d35c-4f01-aaac-b84d170e040d)

 
5.CBC：

![image](https://github.com/PoYuei/S-AES/assets/140697450/cc4ae123-9ad3-43a8-af7f-a53f901c6c90)
![image](https://github.com/PoYuei/S-AES/assets/140697450/65cc259d-9dd8-46da-92c7-f5eec332666d)


  
