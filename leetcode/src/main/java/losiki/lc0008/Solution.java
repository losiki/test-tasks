package losiki.lc0008;

/**
 * https://leetcode.com/problems/string-to-integer-atoi/description/
 *
 */
class Solution {
	static final int	MAX_DIV_10	= Integer.MAX_VALUE / 10;
	static final int	MAX_MOD_10	= Integer.MAX_VALUE % 10;
	static final int	MIN_DIV_10	= Integer.MIN_VALUE / 10;
	static final int	MIN_MOD_10	= -(Integer.MIN_VALUE % 10);

	public int myAtoi(String s) {
		int response = 0;

		int length = s.length();
		int pos = 0;

		// skip leading whitespaces
		while (pos < length && s.charAt(pos) == ' ') {
			pos++;
		}

		if (pos == length)
			return 0;

		boolean negative = false;
		switch (s.charAt(pos)) {
		case '-':
			pos++;
			negative = true;
			break;
		case '+':
			pos++;
			break;
		}

		if (negative) {
			while (pos < length) {
				int digit = Character.digit(s.charAt(pos), 10);
				if (digit < 0) {
					break;
				}

				if (response < MIN_DIV_10 || response == MIN_DIV_10 && digit > MIN_MOD_10) {
					return Integer.MIN_VALUE;
				}
				response = response * 10 - digit;
				pos++;
			}
		} else {
			while (pos < length) {
				int digit = Character.digit(s.charAt(pos), 10);
				if (digit < 0) {
					break;
				}
				if (response > MAX_DIV_10 || response == MAX_DIV_10 && digit > MAX_MOD_10) {
					return Integer.MAX_VALUE;
				}
				response = response * 10 + digit;
				pos++;
			}
		}

		return response;
	}
}