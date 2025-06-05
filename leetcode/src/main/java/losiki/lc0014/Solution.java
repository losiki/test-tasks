package losiki.lc0014;

/**
 * https://leetcode.com/problems/longest-common-prefix/description/
 */
class Solution {
	public String longestCommonPrefix(String[] strs) {
		if (strs == null || strs.length == 0) {
			return "";
		}
		char[] c1 = strs[0].toCharArray();
		int length = c1.length;
		for (String s : strs) {
			if (length == 0) {
				break;
			}
			char[] c2 = s.toCharArray();
			length = Math.min(c2.length, length);

			for (int i = 0; i < length; i++) {
				if (c1[i] != c2[i]) {
					length = i;
					break;
				}
			}
		}
		return new String(c1, 0, length);
	}
}