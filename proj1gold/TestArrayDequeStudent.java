import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestArrayDequeStudent {

    @Test
    public void testStudentArrayDeque() {
        ArrayDequeSolution<Integer> stdArray = new ArrayDequeSolution<>();
        ArrayDeque<Integer> testArray = new ArrayDeque<>();
        stdArray.addFirst(954);
        stdArray.removeLast();
        stdArray.addFirst(134);
        stdArray.addFirst(826);
        stdArray.addFirst(325);
        stdArray.addFirst(644);
        stdArray.addLast(533);
        stdArray.addFirst(456);
        stdArray.addFirst(748);
        stdArray.removeLast();
        stdArray.addLast(26);
        stdArray.removeLast();
        stdArray.addFirst(489);
        stdArray.addLast(494);
        stdArray.removeFirst();
        stdArray.removeLast();
        //stdArray.removeLast();

        testArray.addFirst(954);
        testArray.removeLast();
        testArray.addFirst(134);
        testArray.addFirst(826);
        testArray.addFirst(325);
        testArray.addFirst(644);
        testArray.addLast(533);
        testArray.addFirst(456);
        testArray.addFirst(748);
        testArray.removeLast();
        testArray.addLast(26);
        testArray.removeLast();
        testArray.addFirst(489);
        testArray.addLast(494);
        testArray.removeFirst();
        testArray.removeLast();
        //testArray.removeLast();

        Integer testremoveNumber = testArray.removeLast();
        Integer stdremoveNumber = stdArray.removeLast();
        assertEquals(stdremoveNumber, testremoveNumber);
    }
}
