package losiki.lc0003;

/**
 * https://leetcode.com/problems/longest-substring-without-repeating-characters/description/
 *
 */
class Solution {
	public int lengthOfLongestSubstring(String s) {
		int max = 0;
		int length = s.length();
		int[] indices = new int[128];
		char[] chars = s.toCharArray();
		int left = 0;
		int right = 0;
		while (right < length) {
			char c = chars[right];
			int i = indices[c];
			if (i > left) {
				left = i;
			}
			right++;
			indices[c] = right;
			max = Math.max(max, right - left);
		}
		return max;
	}
}
