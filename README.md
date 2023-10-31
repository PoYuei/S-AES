# S-AES
The Simplified AES.

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


  
