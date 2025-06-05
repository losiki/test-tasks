package losiki.lc0020;

/**
 * https://leetcode.com/problems/valid-parentheses/description/
 *
 */
class Solution {
	public boolean isValid(String s) {
		char[] chars = s.toCharArray();

		int j = 0;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == '(' || c == '[' || c == '{') {
				chars[j] = c;
				j++;
			} else {
				if (j <= 0) {
					return false;
				}

				j--;
				char c1 = chars[j];

				if (!((c1 == '(' && c == ')') ||
					(c1 == '[' && c == ']') ||
					(c1 == '{' && c == '}'))) {
					return false;
				}
			}

		}
		return j == 0;
	}
}