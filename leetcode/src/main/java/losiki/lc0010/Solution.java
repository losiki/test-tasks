package losiki.lc0010;

/**
 * https://leetcode.com/problems/regular-expression-matching/description/
 *
 */
class Solution {
	public boolean isMatch(String s, String p) {
		return isMatch(s, 0, s.length(), p, 0, p.length());
	}

	private boolean isMatch(String s, int i, int length, String p, int j, int length2) {
		if (length == 0 && length2 == 0)
			return true;
		if (length2 == 0)
			return false;

		char cp = p.charAt(j);
		boolean star = length2 > 1 && p.charAt(j + 1) == '*';
		if (star) {
			if (length2 == 2) {
				if (cp == '.')
					return true;
				while (length > 0) {
					if (s.charAt(i) != cp)
						return false;
					i++;
					length--;
				}
				return true;
			}
			if (cp == '.') {
				while (length >= 0) {
					if (isMatch(s, i, length, p, j + 2, length2 - 2))
						return true;
					i++;
					length--;
				}
			} else {
				while (length >= 0) {
					if (isMatch(s, i, length, p, j + 2, length2 - 2))
						return true;
					if (length > 0 && s.charAt(i) != cp)
						return false;
					i++;
					length--;
				}
			}
		} else if (length > 0 && (s.charAt(i) == cp || cp == '.')) {
			return isMatch(s, i + 1, length - 1, p, j + 1, length2 - 1);
		}

		return false;
	}

	public static void main(String[] args) {
		Solution s = new Solution();
		System.out.println(s.isMatch("a", ".*..a*"));
	}
}
