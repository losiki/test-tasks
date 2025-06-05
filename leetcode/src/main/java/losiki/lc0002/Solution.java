package losiki.lc0002;

/**
 * https://leetcode.com/problems/add-two-numbers/description/
 *
 */
class Solution {
	public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		ListNode result = new ListNode();
		ListNode current = result;
		int overflow = 0;
		while (l1 != null && l2 != null) {
			int x = l1.val + l2.val + overflow;
			if (x >= 10) {
				x -= 10;
				overflow = 1;
			} else {
				overflow = 0;
			}
			current.val = x;

			l1 = l1.next;
			l2 = l2.next;
			if (l1 != null || l2 != null) {
				current.next = new ListNode();
				current = current.next;
			}
		}

		if (l1 == null) {
			l1 = l2;
		}

		while (l1 != null) {
			int x = l1.val + overflow;
			if (x >= 10) {
				x -= 10;
				overflow = 1;
			} else {
				overflow = 0;
			}
			current.val = x;

			l1 = l1.next;
			if (l1 != null) {
				current.next = new ListNode();
				current = current.next;
			}
		}

		if (overflow == 1) {
			current.next = new ListNode(1);
		}

		return result;
	}
}
