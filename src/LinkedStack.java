//内部链式存储
public class LinkedStack<T> {
    //内部类Node
    private static class Node<U> {
        U item;
        Node<U> next;
        Node() {
            item = null;
            next = null;
        }
        Node(U item, Node<U> next) {
            this.item = item;
            this.next = next;
        }
        boolean end() {
            return item == null && next == null;
        }
    }
    //末端哨兵
    private Node<T> top = new Node();//end sentinel

    public void push(T item) {
        top = new Node<>(item, top);
    }

    public T pop(){
        T result = top.item;
        if (!top.end()){
            top = top.next;
        }
        return result;
    }

}

////内部链式存储(移除Node类上的类型参数，修改后，证明内部类可以访问其外部类的类型参数)
//public class LinkedStack<T> {
//    //内部类Node
//    private class Node {
//        T item;
//        Node next;
//        Node() {
//            item = null;
//            next = null;
//        }
//        Node(T item, Node next) {
//            this.item = item;
//            this.next = next;
//        }
//        boolean end() {
//            return item == null && next == null;
//        }
//    }
//    //末端哨兵
//    private Node top = new Node();//end sentinel
//
//    public void push(T item) {
//        top = new Node(item, top);
//    }
//
//    public T pop(){
//        T result = top.item;
//        if (!top.end()){
//            top = top.next;
//        }
//        return result;
//    }
//
//}
