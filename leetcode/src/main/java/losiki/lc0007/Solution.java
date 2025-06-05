package losiki.lc0007;

/**
 * https://leetcode.com/problems/reverse-integer/description/
 */
class Solution {
	static final int	MAX_DIV_10	= Integer.MAX_VALUE / 10;
	static final int	MAX_MOD_10	= Integer.MAX_VALUE % 10;
	static final int	MIN_DIV_10	= Integer.MIN_VALUE / 10;
	static final int	MIN_MOD_10	= Integer.MIN_VALUE % 10;

	public int reverse(int x) {
		int response = 0;
		while (x != 0) {
			int digit = x % 10;
			if (response < MIN_DIV_10 || response == MIN_DIV_10 && digit < MIN_MOD_10) {
				return 0;
			} else if (response > MAX_DIV_10 || response == MAX_DIV_10 && digit > MAX_MOD_10) {
				return 0;
			}
			response = response * 10 + digit;
			x /= 10;
		}
		return response;
	}
}
