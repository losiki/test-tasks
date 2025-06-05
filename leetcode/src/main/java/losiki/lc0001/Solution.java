package losiki.lc0001;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.com/problems/two-sum/description/
 *
 */
class Solution {
	public int[] twoSum(int[] nums, int target) {
		Map<Integer, Integer> indices = new HashMap<>();
		for (int i = 0; i < nums.length; i++) {
			Integer x = indices.get(target - nums[i]);
			if (x != null) {
				return new int[] { x, i };
			}
			indices.put(nums[i], i);
		}
		return null;
	}
}
