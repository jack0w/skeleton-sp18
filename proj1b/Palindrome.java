public class Palindrome {

    public Deque<Character> wordToDeque(String word) {
        Deque<Character> deque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            deque.addLast(word.charAt(i));
        }
        return deque;
    }

    public boolean isPalindrome(String word) {
        Deque<Character> deque = wordToDeque(word);
        return isPalindromHelper(deque);
    }

    private boolean isPalindromHelper(Deque<Character> deque) {
        if (deque.size() < 2) {
            return true;
        } else {
            Character first = deque.removeFirst();
            Character last = deque.removeLast();
            if (first.equals(last)) {
                return isPalindromHelper(deque);
            } else {
                return false;
            }
        }
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> deque = wordToDeque(word);
        return isPalindromHelper(deque, cc);
    }

    private boolean isPalindromHelper(Deque<Character> deque, CharacterComparator cc) {
        if (deque.size() < 2) {
            return true;
        } else {
            Character first = deque.removeFirst();
            Character last = deque.removeLast();
            if (cc.equalChars(first, last)) {
                return isPalindromHelper(deque, cc);
            } else {
                return false;
            }
        }
    }
}
