package losiki.lc1980;

/**
 * https://leetcode.com/problems/find-unique-binary-string/description/
 *
 * Solution:
 * - generate a string that differs from every given string by a digit at the corresponding position.
 *
 * Optimization:
 * - use a preallocated char array to avoid excessive intermediate object creation.
 */
public class Solution {
	public String findDifferentBinaryString(String[] nums) {
		char[] response = new char[nums.length];
		for (int i = 0; i < nums.length; i++) {
			if (nums[i].charAt(i) == '0') {
				response[i] = '1';
			} else {
				response[i] = '0';
			}
		}
		return new String(response);
	}
}
