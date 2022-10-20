package final_Project;

import java.io.*;
import java.util.*;

class FAT {
	String formal_argument;
	String index;
	
	public FAT(String formal_argument, String index) {
		super();
		this.formal_argument = formal_argument;
		this.index = index;
	}
}

public class Pass1 {
	
	public static void main(String[] args) throws Exception{
		//한개 이상의 매크로를 지원하기 위한 인수 색인을 추가
		//인수 색인 : pass2에서 실인수 값 변환을 할 때 시작해야할 번호
		int mdtc, mntc, argument_index;
		ArrayList<MDT> mdt = new ArrayList<MDT>();
		ArrayList<MNT> mnt = new ArrayList<MNT>();
		ArrayList<FAT> fat = new ArrayList<FAT>();
		
		BufferedReader br = new BufferedReader(new FileReader(".\\src\\final_Project\\macro.asm"));
		PrintWriter pw = new PrintWriter(".\\src\\final_Project\\macro_p1.asm");
		PrintWriter pw_fat = new PrintWriter(".\\src\\final_Project\\fat.txt");
		PrintWriter pw_mnt = new PrintWriter(".\\src\\final_Project\\mnt.txt");
		PrintWriter pw_mdt = new PrintWriter(".\\src\\final_Project\\mdt.txt");
		
		//MDTC, MNTC = 0
		mdtc = 0;
		mntc = 0;
		argument_index = 0;
        
		while(true) {
			//소스로부터 다음 줄을 읽음
			String sentence = br.readLine();
			if(sentence == null)	break;
			sentence = sentence.toUpperCase();
			
			String word[] = sentence.split(" ");
			
			//MACRO 지시어 확인
			if (word.length > 1) {
				if(word[1].equals("MACRO")) {
					//MNT 기록
					mnt.add(new MNT(mntc, word[0], mdtc, argument_index));
					argument_index  = argument_index + word.length-2;
					mntc = mntc + 1;
					
					String formal_argument;
					//형식 인수표 작성
					for(int i = 2; i < word.length; i++) {
						formal_argument = word[i].replace(",", "");
						fat.add(new FAT(formal_argument, "#"+fat.size()));
					}
					
					//MDT 기록
					mdt.add(new MDT(mdtc, sentence));
					mdtc = mdtc + 1;
					
					while(true) {
						//소스로부터 다음 줄을 읽음
						sentence = br.readLine();
						if(sentence == null)	break;
						sentence = sentence.toUpperCase();
						
						//문장 속 인수를 색인 기호로 치환
						for(int i = 0; i < fat.size(); i++) {
							//sentence = sentence.replace(fat.get(i).formal_argument, fat.get(i).index);
							String tmp[] = sentence.split(" ");
							String s = new String();
							for(int j = 0; j < tmp.length; j++) {
								if(tmp[j].equals(fat.get(i).formal_argument))
									tmp[j] = fat.get(i).index;
								else if(tmp[j].equals(fat.get(i).formal_argument+","))
									tmp[j] = fat.get(i).index+",";
								else if(tmp[j].equals(fat.get(i).formal_argument+":"))
									tmp[j] = fat.get(i).index+",";
								s = s + tmp[j] + " ";
							}
							sentence = s.substring(0, s.length()-1);
						}
						
						//MDT 기록
						mdt.add(new MDT(mdtc, sentence));
						mdtc = mdtc + 1;
						
						//ENDM 지시어 확인
						if(sentence.equals("ENDM"))	break;
					}
				}
			}
			
			//MACRO 지시어가 아닐 경우
			//패스 2를 위해 출력
			if(!sentence.equals("ENDM"))
				pw.println(sentence);
			
			//END 지시어 확인
			if(sentence.equals("END"))	break;
		}
		
		for(int i = 0; i < fat.size(); i++) {
			System.out.printf("%s\t%s\n", fat.get(i).formal_argument, fat.get(i).index);
			pw_fat.println(fat.get(i).formal_argument + " " +fat.get(i).index);
		}
		System.out.println();
		for(int i = 0; i < mdt.size(); i++) {
			System.out.println(mdt.get(i).sentence);
			pw_mdt.println(mdt.get(i).sentence);
		}
		System.out.println();
		for(int i = 0; i < mnt.size(); i++) {
			System.out.printf("%s\t%d\t%d\n", mnt.get(i).macro_name, mnt.get(i).mdt_index, mnt.get(i).argument_index);
			pw_mnt.println(mnt.get(i).macro_name + " " + mnt.get(i).mdt_index + " " + mnt.get(i).argument_index);
		}
		
		br.close();
		pw.close();
		pw_fat.close();
		pw_mnt.close();
		pw_mdt.close();
	}
}
