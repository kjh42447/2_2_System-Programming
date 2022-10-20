package final_Project;

import java.io.*;
import java.util.*;

class AAT {
	String index;
	String actual_argument;
	
	public AAT(String index, String actual_argument) {
		super();
		this.index = index;
		this.actual_argument = actual_argument;
	}
}

public class Pass2 {
	
	public static void main(String[] args) throws Exception{
		int mdtp, argument_index = 0;
		boolean is_macro = false;
		ArrayList<MDT> mdt = new ArrayList<MDT>();
		ArrayList<MNT> mnt = new ArrayList<MNT>();
		ArrayList<AAT> aat = new ArrayList<AAT>();
		
		BufferedReader br = new BufferedReader(new FileReader(".\\src\\final_Project\\macro_p1.asm"));
		PrintWriter pw = new PrintWriter(".\\src\\final_Project\\macro_final.asm");
		PrintWriter pw_aat = new PrintWriter(".\\src\\final_Project\\aat.txt");
		//BufferedReader br_aat = new BufferedReader(new FileReader(".\\src\\final_Project\\aat.txt"));
		BufferedReader br_mnt = new BufferedReader(new FileReader(".\\src\\final_Project\\mnt.txt"));
		BufferedReader br_mdt = new BufferedReader(new FileReader(".\\src\\final_Project\\mdt.txt"));
		
		//mdt 읽어오기
		int index = 0;
		while(true) {
			String s = br_mdt.readLine();
			if(s == null)	break;
			
			mdt.add(new MDT(index, s));
			index = index + 1;
		}
		
		//mnt 읽어오기
		index = 0;
		while(true) {
			String s = br_mnt.readLine();
			if(s == null)	break;
			
			String word[] = s.split(" ");
			mnt.add(new MNT(index, word[0], Integer.parseInt(word[1]), Integer.parseInt(word[2])));
			index = index + 1;
		}
		
		//패스 2시작
		while(true) {
			//소스로부터 다음 줄을 읽음
			String sentence = br.readLine();
			if(sentence == null)	break;
			sentence = sentence.toUpperCase();
			is_macro = false;
			
			String word[] = sentence.split(" ");
			
			//MACRO 지시어 확인
			for(int i = 0; i < mnt.size(); i++) {
				//매크로 이름 발견
				if(word[0].equals(mnt.get(i).macro_name)) {
					mdtp = mnt.get(i).mdt_index;
					is_macro = true;
					
					//실인수표 작성
					for(int j = 1; j < word.length; j++) {
						aat.add(new AAT("#"+(mnt.get(i).argument_index+j-1), word[j].replace(",", "")));
					}
					
					while (true) {
						mdtp = mdtp + 1;
						String mdt_sentence = mdt.get(mdtp).sentence;
						
						if (mdt_sentence.equals("ENDM")) break;
						
						//인수번호를 실인수로 치환
						for (int j = argument_index; j < aat.size(); j++) {
							mdt_sentence = mdt_sentence.replace(aat.get(j).index, aat.get(j).actual_argument);
						}
						//명령어 출력
						pw.println(mdt_sentence);
					}
					
					argument_index = aat.size();
					break;
				}
			}
			
			//매크로가 아닐 경우
			if (!sentence.equals("ENDM") && !is_macro) {
				pw.println(sentence);
				
				if(sentence.equals("END"))	break;
			}
		}
		
		for(int i = 0; i < aat.size(); i++) {
			System.out.printf("%s\t%s\n", aat.get(i).index, aat.get(i).actual_argument);
			pw_aat.println(aat.get(i).index + " " +aat.get(i).actual_argument);
		}
		System.out.println();
		for(int i = 0; i < mdt.size(); i++) {
			System.out.println(mdt.get(i).sentence);
		}
		System.out.println();
		for(int i = 0; i < mnt.size(); i++) {
			System.out.printf("%s\t%d\n", mnt.get(i).macro_name, mnt.get(i).mdt_index);
		}
		br.close();
		pw.close();
		pw_aat.close();
		br_mdt.close();
		br_mnt.close();
	}
}
