package ZKI.gost;

public class Gost {

	private static final int BREAKS_NUMBER = 8;

	private static final int SHIFT_NUMBER = 11;

	private String key;

	private String message;

	private String[] keys = new String[32];

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String encrypt() {
		String[] N = new String[2];
		String[] S;
		N[0] = message.substring(0, 32);
		N[1] = message.substring(32);

		generateKeys(key);

		for (int i = 0; i < 32; i++) {
			String mod = sumAndMod32(N[0], keys[i]);
			S = breakByFourBits(BREAKS_NUMBER, mod);
			String allSBlocks = changeByTable(S);
			allSBlocks = leftShiftBits(allSBlocks, 11);
			allSBlocks = xor(allSBlocks, N[1]);
			N[1] = N[0];
			N[0] = allSBlocks;
		}

		return N[1] + N[0];
	}

	private void generateKeys(String key) {
		for (int i = 0; i < 8; i++) {
			keys[i] = key.substring(i * 32, (i + 1) * 32);
		}

		for (int i = 8; i < 24; i++) {
			keys[i] = keys[i % 8];
		}

		for (int i = 24; i < 32; i++) {
			keys[i] = new StringBuilder(keys[i % 8]).reverse().toString();
		}
	}

	private String sumAndMod32(String val1, String val2) {
		long num1 = Long.parseLong(val1, 2);
		long num2 = Long.parseLong(val2, 2);
		long sum = num1 + num2;

		sum = sum % (long)Math.pow(2, 32);
		StringBuilder answer = new StringBuilder(Long.toBinaryString(sum));

		for (int i = answer.length(); i < 32; i++) {
			answer.insert(0, "0");
		}

		return answer.toString();
	}

	private String[] breakByFourBits(int breaksNumber, String mod) {
		String[] blocks = new String[breaksNumber];
		for (int i = 0; i < breaksNumber; i++) {
			blocks[i] = mod.substring(i * 4, (i + 1) * 4);
		}

		return blocks;
	}

	private String changeByTable(String[] s) {
		int[][] sBoxs = new int[][] {
				{ 4, 10, 9, 2, 13, 8, 0, 14, 6, 11, 1, 12, 7, 15, 5, 3 },
				{ 14, 11, 4, 12, 6, 13, 15, 10, 2, 3, 8, 1, 0, 7, 5, 9 },
				{ 5, 8, 1, 13, 10, 3, 4, 2, 14, 15, 12, 7, 6, 0, 9, 11 },
				{ 7, 13, 10, 1, 0, 8, 9, 15, 14, 4, 6, 12, 11, 2, 5, 3 },
				{ 6, 12, 7, 1, 5, 15, 13, 8, 4, 10, 9, 14, 0, 3, 11, 2 },
				{ 4, 11, 10, 0, 7, 2, 1, 13, 3, 6, 8, 5, 9, 12, 15, 14 },
				{ 13, 11, 4, 1, 3, 15, 5, 9, 0, 10, 14, 7, 6, 8, 2, 12 },
				{ 1, 15, 13, 0, 5, 7, 10, 4, 9, 2, 3, 14, 6, 11, 8, 12 },
		};

		StringBuilder seq = new StringBuilder();

		for (int i = 0; i < s.length; i++) {
			int col = Integer.parseInt(s[i], 2);
			seq.append(Integer.toBinaryString(sBoxs[i][col]));
		}

		return seq.toString();
	}

	private String leftShiftBits(String input, int shift) {
		String shifted = input;

		for (int j = 0; j < shift; j++) {
			shifted = shifted.substring(1, shifted.length()) + shifted.substring(0, 1);
		}

		return shifted;
	}

	private static String xor(String x1, String x2) {
		StringBuilder out = new StringBuilder();
		int len;
		int start;

		if (x1.length() < x2.length()) {
			len = x1.length();
			start = x2.length() - x1.length();
			out.append(x2.substring(0, start));
			x2 = x2.substring(start);
		} else {
			len = x2.length();
			start = x1.length() - x2.length();
			out.append(x1.substring(0, start));
			x1 = x1.substring(start);
		}

		for (int i = 0; i < len; i++) {
			String s1 = x1.substring(i, i + 1);
			String s2 =x2.substring(i, i + 1);
			out.append(Integer.parseInt(x1.substring(i, i + 1)) ^ Integer.parseInt(x2.substring(i, i + 1)));
		}

		return out.toString();
	}

}
