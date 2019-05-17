package Listener;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import Listener.JavaSoundRecorder;

public class Listener_v2 {
	private static final Listener_v2 INSTANCE = new Listener_v2();
	
	final JavaSoundRecorder recorder = new JavaSoundRecorder();
	final Decoder decoder = new Decoder();

	public Listener_v2() {
		// TODO Auto-generated constructor stub
	}

	public void start() {
		Thread rec ;
		rec = new Thread(new Runnable() {
			public void run() {
				recorder.start();
			}
        	 });
        	 rec.start();  
	}

	public int stop() {
		recorder.finish();
		String res;
		try {
			res = decoder.compute();
			int tmpf = res.indexOf("\"utterance\": \"");
	 		if(tmpf != -1) {
	 			int tmp = res.indexOf(".");
	 			if(tmp != -1) {
	 				String words = res.substring(tmpf+14, tmp-1);
	 				String text = "";
	 				for(String word:words.split(" ")) {
	 					word = word.replace("\\","");
	 					//System.out.println(word);
	 					for(String ch:word.split("u")) {
	 						if(ch.length() > 0) {
	 							//System.out.println(ch);
	 							int hexVal = Integer.parseInt(ch,16);
	 							text+=(char) hexVal;
	 						}
	 					}
	 					text += " ";
	 				}
	 				int result = this.action(text);
	 				System.out.println("stateOut: "+result);
	 				return result;
	 			}
	 			else this.action("empty");
	 		}
	 		else {
	 			this.action("Fail");
	 		}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
 	}

	
	public int action(String s) {
		System.out.println(s);
		if (s.indexOf("ซื้อ") != -1) {
			return 5;
		}
		if (s.indexOf("สร้าง") != -1 || s.indexOf("แลนด์มาร์ค") != -1) {
			return 5;
		}
		if(s.indexOf("ขาย") != -1) {
			if(s.indexOf("โฉนด") != -1) {
				return 7;
			}
			else if(s.indexOf("โรงแรม") != -1) {
				return 7;
			}
			else {
				return 7;
			}
		}
		
		if (s.indexOf("ทอย") != -1 || s.indexOf("ลูกเต๋า") != -1) {
			return 2;
		}
		
		if (s.indexOf("เริ่ม") != -1 || s.indexOf("เกม") != -1) {
			return 1;
		}
		
		if (s.indexOf("เปลี่ยน") != -1 || s.indexOf("เทิร์น") != -1) {
			return 3;
		}
		
		if (s.indexOf("ยอม") != -1 || s.indexOf("แพ้") != -1) {
			return 4;
		}
		if (s.indexOf("เทค") != -1 || s.indexOf("โอเวอร์") != -1) {
			return 11;
		}
		if (s.indexOf("จ่าย") != -1 || s.indexOf("เงิน") != -1 ) {
			return 15;
		}
		return 0;
	}
	
	public static Listener_v2 getInstance () {
		return INSTANCE;
	}
}
