package ouc_java;


/**
 * @author ��Դ��
 * @version 1.0
 * @description ��һ��javaԴ�ļ����м򵥵Ĵʷ���������ȡ���Զ����࣬�������������ͼ�������
 * δ�����ܻ����ӹ�������ʵ�ֱ�����ǰ�˴ʷ�����
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
	String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~��@#��%����&*��������+|{}�������������������� ����]";
	private char[] separators = {';','{','}','(',')','[',']','.'};
	private String[] keyWords = {"class","int","double","boolean","char","public","private"};
	private ArrayList<String>wayList = new ArrayList<String>();				//����ʹ�õķ���
	private ArrayList<String>wayOfClassList = new ArrayList<String>();		//������Ӧ����														
	private ArrayList<String>classList =  new ArrayList<String>();			//������
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
	//��ȡ�ؼ��ʽ�classList ��
	private void getClassKey(String val) {
		 Pattern p = Pattern.compile(regEx);
		 Matcher m = p.matcher(val);
		 String newString = m.replaceAll(" ").trim();		//ȥ���Ƿ��ַ��Ϳո�
		 classList.add(newString);
		
	}
	//��ȡ��������
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
	
	
	
	
	//��ȡ�ļ�
	public void readFile() {
		try {
			String temp = null;
			FileReader fr = new FileReader(this.filename);
			BufferedReader br = new BufferedReader(fr);
			while((temp = br.readLine())!=null) {
				filecontent.append(temp);
			}
		}catch(FileNotFoundException e) {
			System.out.println("�ļ�δ�ҵ���"); 
		}catch(IOException e) {
			System.out.println("��д�ļ�����");
		}
	}
	
	public void analyze(){
	
		String content = filecontent.toString();
		StringTokenizer stk = new StringTokenizer(content,";");
		//��һ�㰴;�ָ�
		while(stk.hasMoreTokens()) {
			StringTokenizer stkSquare = new StringTokenizer(stk.nextToken());
		
			ArrayList<String> saveDataSqure = new ArrayList<String>();//�ո�ָ��Ĵ洢����
			ArrayList<String> saveDataPoint = new ArrayList<String>();//��.�ָ��洢����
			
			
//			while(stkPoint.hasMoreTokens()) {
//				String gogal = stkPoint.nextToken();
//				saveDataPoint.add(gogal);
//			}		//������д���
			
			
			//�������ΰ�; �ո���ȡ �õ�ԭ������
			while(stkSquare.hasMoreTokens()) {
				
				 
				String gogal = stkSquare.nextToken();
				if(gogal.contains(".")) {
					
					StringTokenizer stkPoint = new StringTokenizer(gogal,".");	//��.����������Ͷ���
					while(stkPoint.hasMoreTokens()) {
						String wayString = stkPoint.nextToken();
						saveDataPoint.add(wayString);
					}		//������д���
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
				
					//�ж���
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
		System.out.println("ʹ�õ��Զ�����");
		for(String s:classList)
			System.out.print(s+", ");
		System.out.println("\nʹ����������:");
		for(int i =1;i<=4;i++) {
			System.out.print(keyWords[i]+": ");
			for(String s:numberList[i-1])
				System.out.print(s+", ");
			System.out.print("\n");
		}
		System.out.println("ʹ�õ���Ͷ�Ӧ����:");
		for(int i =0;i<Math.min(wayList.size(), wayOfClassList.size());i++) {
			System.out.println("��: "+wayOfClassList.get(i)+"����: "+wayList.get(i));
		}
	}
	
	
	public static void main(String[]args) {
		Analyzer a = new Analyzer("demo.txt");
		a.readFile();
		a.analyze();
		a.showInfo();
	}
	
}
