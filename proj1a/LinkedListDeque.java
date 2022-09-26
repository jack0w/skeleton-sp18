public class LinkedListDeque<T> {
    private Node sentinel;
    private int size;

    private class Node {
        private Node prev;
        private T item;
        private Node next;

        Node(Node prevNode, T value, Node nextNode) {
            prev = prevNode;
            item = value;
            next = nextNode;
        }

    }

    /**
     *Constructor: Create empty list
     */
    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    // Example: Sentinel <-> A <-> B <-> Sentinel
    // addFirst: Sentinel <-> C <-> A <-> B <-> Sentinel
    public void addFirst(T item) {
        Node first = sentinel.next;
        sentinel.next = new Node(sentinel, item, first);
        first.prev = sentinel.next;
        size += 1;
    }

    // Example: Sentinel <-> A <-> B <-> Sentinel
    // addFirst: Sentinel <-> A <-> B <-> C <-> Sentinel
    public void addLast(T item) {
        Node last = sentinel.prev;
        sentinel.prev = new Node(last, item, sentinel);
        last.next = sentinel.prev;
        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node cur = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(cur.item);
            System.out.print(' ');
            cur = cur.next;
        }
    }

    // Example: Sentinel <-> A <-> B <-> Sentinel
    // removeFirst: Sentinel <-> B <-> Sentinel
    public T removeFirst() {
        T value = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size = size == 0 ? 0 : size - 1;
        return value;
    }

    public T removeLast() {
        T value = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size = size == 0 ? 0 : size - 1;
        return value;
    }

    public T get(int index) {
        Node cur = sentinel.next;
        if (index < size) {
            for (int i = 0; i < index; i++) {
                cur = cur.next;
            }
            return cur.item;
        } else {
            return null;
        }
    }

    public T getRecursive(int index) {
        if (index < size) {
            return getRecursiveHelper(index, sentinel.next);
        } else {
            return null;
        }
    }

    private T getRecursiveHelper(int index, Node cur) {
        if (index == 0) {
            return cur.item;
        } else {
            return getRecursiveHelper(index - 1, cur.next);
        }
    }

}
