package losiki.lc0009;

/**
 * https://leetcode.com/problems/palindrome-number/description/
 *
 */
class Solution {
	public boolean isPalindrome(int x) {
		if (x < 0) {
			return false;
		}
		if (x == 0) {
			return true;
		}

		int y = 0;
		for (int t = x; t != 0; t /= 10) {
			y = y * 10 + t % 10;
		}

		return y == x;
	}
}
