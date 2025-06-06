package losiki.lc0006;

/**
 * https://leetcode.com/problems/zigzag-conversion/description/
 *
 */
class Solution {
	public String convert(String s, int numRows) {
		if (numRows == 1 || numRows >= s.length()) {
			return s;
		}

		StringBuilder[] sb = new StringBuilder[numRows];
		for (int i = 0; i < numRows; i++) {
			sb[i] = new StringBuilder();
		}

		int index = 0, step = -1;
		for (char c : s.toCharArray()) {
			sb[index].append(c);
			if (index == 0) {
				step = 1;
			} else if (index == numRows - 1) {
				step = -1;
			}
			index += step;
		}

		StringBuilder result = new StringBuilder();
		for (StringBuilder sBuilder : sb) {
			result.append(sBuilder);
		}
		return result.toString();
	}
}
