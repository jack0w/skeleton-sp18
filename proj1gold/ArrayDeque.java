public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int front; //position of front of the array
    private int back; //position of back of the array

    //case1: 0 ... back ... front ... length -1
    //case2: 0 ... front ... back ... length - 1
    //invariant: nothing before front, nothing after back
    //invariant: length of array is a multiplication of 8


    public ArrayDeque() {
        items = (T []) new Object[8];
        front = items.length - 1;
        back = 0;
        size = 0;
    }

    private void resize(int capacity){
        T [] a = (T []) new Object[capacity];
        int firstLength = (front+1+size) > items.length ? items.length - front - 1 : size;
        int secondLength = size - firstLength;
        System.arraycopy(items, front + 1, a, 0, firstLength);
        System.arraycopy(items, 0, a, firstLength, secondLength);
        items = a;
        front = items.length - 1;
        back = size;
    }

    public void addFirst(T item){
        if (size == items.length){
            resize(items.length * 2);
        }
        items[front] = item;
        front = (front - 1 + items.length) % items.length;
        size ++;
    }

    public void addLast(T item){
        if (size == items.length){
            resize(items.length * 2);
        }
        items[back] = item;
        back = (back + 1 + items.length) % items.length;
        size ++;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int size(){
        return size;
    }

    public void printDeque(){
        int i = (front + 1 + items.length) % items.length;
        while (i != back){
            System.out.print(items[i]);
            System.out.print(" ");
            i = (i + 1 + items.length) % items.length;
        }
    }

    public T removeFirst(){
        if (size < items.length * 0.25 && size > 8){
            resize(items.length / 2);
        }
        if (size != 0){
            front = (front + 1 + items.length) % items.length;
            T value = items[front];
            items[front] = null;
            size --;
            return value;
        }
        else{
            return null;
        }
    }

    public T removeLast(){
        if (size < items.length * 0.25 && size > 8){
            resize(items.length / 2);
        }
        if (size != 0){
            back = (back - 1 + items.length) % items.length;
            T value = items[back];
            items[back] = null;
            size --;
            return value;
        }
        else{
            return null;
        }
    }

    public T get(int index){
        return items[(front + 1 + index + items.length) % items.length];
    }
}
