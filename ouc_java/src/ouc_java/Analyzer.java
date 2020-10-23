package ouc_java;


/**
 * @author 聂源铭
 * @version 1.0
 * @description 对一个java源文件进行简单的词法分析，提取出自定义类，方法，变量类型及其名字
 * 未来可能会增加功能完整实现编译器前端词法分析
*/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyzer {
	private String filename;
	private StringBuilder filecontent = new StringBuilder();
	String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
	private char[] separators = {';','{','}','(',')','[',']','.'};
	private String[] keyWords = {"class","int","double","boolean","char","public","private"};
	private ArrayList<String>wayList = new ArrayList<String>();				//储存使用的方法
	private ArrayList<String>wayOfClassList = new ArrayList<String>();		//方法对应的类														
	private ArrayList<String>classList =  new ArrayList<String>();			//储存类
	@SuppressWarnings("unchecked")
	private ArrayList<String>[]numberList = (ArrayList<String>[])new ArrayList[4];      //0 int 1 double 2 boolean 3 char 
	
	
	private boolean isKey(String val) {
		for(String s:keyWords) {
			if(val.equals(s))
				return true;
		}
		return false;
	}
	
	private void getNumberKey(String temp) {
		
		if(temp.equals(keyWords[1]))
			numberList[0].add(temp);
		else if(temp.equals(keyWords[2]))
			numberList[1].add(temp);
		else if(temp.equals(keyWords[3]))
			numberList[2].add(temp);
		else if(temp.equals(keyWords[4]))
			numberList[3].add(temp);
		
	}
	//提取关键词进classList 中
	private void getClassKey(String val) {
		 Pattern p = Pattern.compile(regEx);
		 Matcher m = p.matcher(val);
		 String newString = m.replaceAll(" ").trim();		//去掉非法字符和空格
		 classList.add(newString);
		
	}
	//获取函数方法
	private void getFunKey(String val) {
		 Pattern p = Pattern.compile(regEx);
		 Matcher m = p.matcher(val);
		 String newString = m.replaceAll(" ").trim();
	}
	
	public Analyzer(String s) {
		filename = s;
		 for(int i =0;i<4;i++) {
				numberList[i] = new ArrayList<String>();
			}
	}
	
	
	
	
	//读取文件
	public void readFile() {
		try {
			String temp = null;
			FileReader fr = new FileReader(this.filename);
			BufferedReader br = new BufferedReader(fr);
			while((temp = br.readLine())!=null) {
				filecontent.append(temp);
			}
		}catch(FileNotFoundException e) {
			System.out.println("文件未找到！"); 
		}catch(IOException e) {
			System.out.println("读写文件错误！");
		}
	}
	
	public void analyze(){
	
		String content = filecontent.toString();
		StringTokenizer stk = new StringTokenizer(content,";");
		//第一层按;分割
		while(stk.hasMoreTokens()) {
			StringTokenizer stkSquare = new StringTokenizer(stk.nextToken());
		
			ArrayList<String> saveDataSqure = new ArrayList<String>();//空格分割后的存储数据
			ArrayList<String> saveDataPoint = new ArrayList<String>();//按.分割后存储数据
			
			
//			while(stkPoint.hasMoreTokens()) {
//				String gogal = stkPoint.nextToken();
//				saveDataPoint.add(gogal);
//			}		//对其进行处理
			
			
			//遍历依次按; 空格提取 得到原子命题
			while(stkSquare.hasMoreTokens()) {
				
				 
				String gogal = stkSquare.nextToken();
				if(gogal.contains(".")) {
					
					StringTokenizer stkPoint = new StringTokenizer(gogal,".");	//按.分离出方法和对象
					while(stkPoint.hasMoreTokens()) {
						String wayString = stkPoint.nextToken();
						saveDataPoint.add(wayString);
					}		//对其进行处理
				}
				saveDataSqure.add(gogal);
			}
			for(String val:saveDataPoint) {
				if(Character.isLowerCase(val.charAt(0))) {
					wayList.add(val);
				}else {
					wayOfClassList.add(val);
				}
			}
			for(int i =0;i<saveDataSqure.size();i++) {
				if(isKey(saveDataSqure.get(i))) {
					
					String temp = saveDataSqure.get(i);
				
					//判断类
					if(temp.equals("class")) {
						getClassKey(saveDataSqure.get(i+1));
					}
					if(temp.equals(keyWords[1]))
						numberList[0].add(saveDataSqure.get(i+1));
					else if(temp.equals(keyWords[2]))
						numberList[1].add(saveDataSqure.get(i+1));
					else if(temp.equals(keyWords[3]))
						numberList[2].add(saveDataSqure.get(i+1));
					else if(temp.equals(keyWords[4]))
						numberList[3].add(saveDataSqure.get(i+1));
						
				}
					
				System.out.println(saveDataSqure.get(i));
			}
		
			//System.out.println(stk.nextToken());
		}
			
	}
	
	public void showInfo() {
		System.out.println("使用的自定义类");
		for(String s:classList)
			System.out.print(s+", ");
		System.out.println("\n使用数据类型:");
		for(int i =1;i<=4;i++) {
			System.out.print(keyWords[i]+": ");
			for(String s:numberList[i-1])
				System.out.print(s+", ");
			System.out.print("\n");
		}
		System.out.println("使用的类和对应方法:");
		for(int i =0;i<Math.min(wayList.size(), wayOfClassList.size());i++) {
			System.out.println("类: "+wayOfClassList.get(i)+"方法: "+wayList.get(i));
		}
	}
	
	
	public static void main(String[]args) {
		Analyzer a = new Analyzer("demo.txt");
		a.readFile();
		a.analyze();
		a.showInfo();
	}
	
}
