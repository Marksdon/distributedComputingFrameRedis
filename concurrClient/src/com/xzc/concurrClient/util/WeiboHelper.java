package com.xzc.concurrClient.util;

public class WeiboHelper {
	public static void main(String[] args) {
		String id="3852231831245245";
		String mid=Id2Mid(id);
		System.out.println(mid);
	}
	
	private static String str62keys = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	// mid转成id
		public static String Mid2Id(String mid) {
			String id = "";
			// 从最后往前以4字节为一组读取字符
			for (int i = mid.length() - 4; i > -4; i = i - 4) {
				String str1 = "";
				if (i < 0) {
					str1 = mid.substring(0, (mid.length() % 4));
				} else {
					str1 = mid.substring(i, i + 4);
				}
				long lon = Encode62ToInt(str1);
				String str = String.valueOf(lon);
				int offset = i < 0 ? 0 : i;
				if (offset > 0) {
					// 若不是第一组，则不足7位补0
					if (str.length() < 7) {
						int num = 7 - str.length();
						String sp = "";
						for (int j = 0; j < num; j++) {
							sp = sp + "0";
						}
						str = sp + str;
					}
				}
				id = str + id;
			}
			return id;
		}

		private static long Encode62ToInt(String midStr) {
			long i10 = 0;
			for (int i = 0; i < midStr.length(); i++) {
				double n = midStr.length() - i - 1;
//				String[] strs = midStr.split("");
				char strs[] = midStr.toCharArray();
				i10 += (str62keys.indexOf(strs[i]) * Math.pow(62, n));
			}
			return i10;
		}
		
		// id转成mid
		public static String Id2Mid(String id) {
			String mid = "";
			// 从最后往前以7字节为一组读取字符
			for (int i = id.length() - 7; i > -7; i = i - 7) {
				String str = "";
				if (i < 0) {
					str = IntToEnode62(Long.parseLong(id.substring(0, id.length() % 7)));
				} else {
					str = IntToEnode62(Long.parseLong(id.substring(i, i + 7)));
				}
				mid = str + mid;
			}
			return mid;
		}

		private static String IntToEnode62(long int10) {
			String s62 = "";
//			String[] str62key = str62keys.split("");
			char str62key[] = str62keys.toCharArray();
			int r = 0;
			while (int10 != 0) {
				r = (int) (int10 % 62);
				s62 = str62key[r] + s62;
				int10 = (long) Math.floor(int10 / 62.0);
			}
			return s62;
		}

}
