package losiki.lc0022;

import java.util.ArrayList;
import java.util.List;

class Solution {
	public List<String> generateParenthesis(int n) {
		List<String> result = new ArrayList<>();
		char[] temp = new char[2 * n];
		generate(temp, result, n, 0, 0);
		return result;
	}

	private void generate(char[] temp, List<String> result, int bracketsToOpen, int bracketsToClose, int position) {
		if (bracketsToOpen == 0 && bracketsToClose == 0) {
			result.add(new String(temp));
		}

		if (bracketsToOpen > 0) {
			temp[position] = '(';
			generate(temp, result, bracketsToOpen - 1, bracketsToClose + 1, position + 1);
		}
		if (bracketsToClose > 0) {
			temp[position] = ')';
			generate(temp, result, bracketsToOpen, bracketsToClose - 1, position + 1);
		}
	}
}
