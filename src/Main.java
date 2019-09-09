import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;

interface Generator<T> {
    T next();
}

class Coffee {
    private static long counter = 0;
    private final long id = counter++;
    @Override
    public String toString(){
        return getClass().getSimpleName() + " " + id;
    }
}

class CoffeeA extends Coffee {}
class CoffeeB extends Coffee {}
class CoffeeC extends Coffee {}
class CoffeeD extends Coffee {}
class CoffeeE extends Coffee {}

class BasicGenerator<T> implements Generator<T> {
    private Class<T> type;
    public BasicGenerator(Class<T> type) {
        this.type = type;
    }

    @Override
    public T next() {
        try {
            return type.newInstance();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static <T> Generator<T> create(Class<T> type) {
        return new BasicGenerator<>(type);
    }
}
class CoffeeGenerator implements Generator<Coffee>, Iterable<Coffee> {
    private Class[] types = {CoffeeA.class,CoffeeB.class,CoffeeC.class,CoffeeD.class,CoffeeE.class};
    private static Random random = new Random(47);
    private int size;
    public CoffeeGenerator(int size) {
        this.size = size;
    }
    @Override
    public Coffee next() {
        try {
            return (Coffee) types[random.nextInt(types.length)].newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterator<Coffee> iterator() {
        return new Iterator<Coffee>() {
            @Override
            public boolean hasNext() {
                return size > 0;
            }

            @Override
            public Coffee next() {
                size--;
                return CoffeeGenerator.this.next();
            }
        };
    }
}

class New {
    static <K,V> Map<K,V> map() {
        return new HashMap<K,V>();
    }
}
class GenericMethods {
    static void f(Map<String, Integer> map){
        System.out.println(1);
    }
}
class HasF {
    void f(){
        System.out.println(1);
    }
}

class Manipulator<T extends HasF> {
    private T obj;
    public Manipulator(T obj) {
        this.obj = obj;
    }
    public void manipulator(){
        obj.f();
    }
    public T get(){
        return obj;
    }
}
class HasFSub extends HasF {}

class Generic<T> {
    private T element;
    public void set(T element) {
        this.element = element;
    }
    public T get() {
        return element;
    }
}

class ArrayMaker<T> {
    private Class<T> kind;
    ArrayMaker(Class<T> kind) {
        this.kind = kind;
    }
    T[] create(T t,int size) {
        T[] ts = (T[])Array.newInstance(kind, size);
        for (int i = 0; i < size; i++) {
            ts[i] = t;
        }
        return ts;
    }
}
class Derived<T> extends Generic<T> {}

class Erased<T> {
    Class<T> kind;
    private T obj;
    public void f(Object arg){
        kind.isInstance(arg);
    }
}

class A {}
class Building extends A {}
class House extends Building {}


class ClassTypeCapture<T> {
    private Class<T> kind;
    public ClassTypeCapture(Class<T> kind) {
        this.kind = kind;
    }
    public boolean f(Object arg) {
        return kind.isInstance(arg);
    }
}

interface InterfaceA {
    void f();
}
interface InterfaceB extends InterfaceA {
    void f();
}

class StringAddress {
    private String s;
    public StringAddress(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return super.toString() + s;
    }
}

class CollectionData<T> extends ArrayList<T> {
    private CollectionData(Generator<T> gen, int quantity) {
        for (int i = 0; i < quantity; i++) {
            add(gen.next());
        }
    }
    public static <T> CollectionData<T> list(Generator<T> gen, int quantity) {
        return new CollectionData<>(gen, quantity);
    }
}

class Goverment implements Generator<String> {
    String[] strings = "i am a b c d e f g".split(" ");
    private int index;
    @Override
    public String next() {
        return strings[index++];
    }
}

interface aa<K,V> {
    interface bb<K,V>{

    }
    Set<aa.bb<K,V>> f();
}

class SetType {
    int i;
    public SetType(int i) {
        this.i = i;
    }
    public boolean equals(Object o) {
        return (o instanceof SetType) && (i == ((SetType)o).i);
    }

    @Override
    public String toString() {
        return Integer.toString(i);
    }
}

class HashType extends SetType {
    public HashType(int n) {
        super(n);
    }

    @Override
    public int hashCode() {
        return 20-i;
    }
}

class TreeType extends SetType implements Comparable<TreeType> {
    public TreeType(int n) {
        super(n);
    }

    @Override
    public int compareTo(TreeType o) {
        return Integer.compare(o.i, i);
    }
}

enum Shrubbery {
    GROUND(1,"qwe"),
    CRAWLING(2,"23rfgdgfh"),
    HAVING(3,"dsfsdaf");
    private int i;
    private String s;
    Shrubbery(int i, String s) {
        this.i = i;
        this.s = s;
    }
}

enum Signal {
    RED,YELLOW,GREEN
}

class TrafficLight {
    Signal color = Signal.RED;
    void change() {
        switch (color) {
            case RED:
                color = Signal.YELLOW;
                break;
            case YELLOW:
                color = Signal.GREEN;
                break;
            case GREEN:
                color = Signal.RED;
        }
    }

    @Override
    public String toString() {
        return "The traffic light is " + color;
    }
}


class Enums {

    public static Random rand = new Random(47);

    public static <T extends Enum> T random(Class<T> type) {
        return random(type.getEnumConstants());
    }
    public static <T> T random(T[] type) {
        return type[rand.nextInt(type.length)];
    }
}

enum Course {
    SDS(Food.Drink.class),
    FDFD(Food.Fruit.class);
    private Food[] values;
    Course(Class<? extends Food> kind) {
        values = kind.getEnumConstants();
    }
    interface Food {
        enum Fruit implements Food {
            APPLE, BANANA, ORIGINE
        }
        enum Drink implements Food {
            WATER, COFFEE, JUICE
        }
    }
    Food randomSelector() {
        return Enums.random(values);
    }
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface me {
    String value() default "123";
    int integer();
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface DBTable {
    String name() default "";
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Constraints {
    boolean primaryKey() default false;
    boolean allowNull() default true;
    boolean unique() default false;
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface SQLString {
    int value() default 0;
    String name() default "";
    Constraints constraints() default @Constraints(unique = true);
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface SQLInteger {
    String name() default "";
    Constraints constraints() default @Constraints;
}

@DBTable(name = "DBTable")
class Member {
    @SQLString(30)
    String firstName;
    @SQLString(50)
    String lastName;
    @SQLInteger()
    Integer age;
    @SQLString(value = 30,constraints = @Constraints(primaryKey = true))
    String handle;
    static int memberCount;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public String toString() {
        return handle;
    }
}

class MyHashMap {
    private MyHashMap() {}
    public static <K, V> HashMap<K, V> newInstance() {
        return new HashMap<>();
    }
}
public class Main {
    static <T> Set<T> fill(Set<T> set, Class<T> type) {
        try{
            for (int i = 10; i > 0; i--) {
                set.add(type.getConstructor(int.class).newInstance(i));
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return set;
    }

    static <T> void test(Set<T> set, Class<T> type) {
        fill(set, type);
        fill(set, type);
        fill(set, type);
        System.out.println(set);
    }
    static int finallyNotWork() {

        int temp = 10000;
        try {
            throw new Exception();
        } catch (Exception e) {
            return ++temp;
        } finally {
            temp = 9999;
        }
    }

    public static String getHint(String secret, String guess) {
        String res;
        int[] s1 = new int[11];
        int[] g1 = new int[11];
        int a = 0;
        int b = 0;
        for (int i = 0; i < 10; i++) {
            s1[i] = 0;
            g1[i] = 0;
        }
        int len = secret.length();
        for (int i = 0; i < len; i++) {
            if (secret.charAt(i) == guess.charAt(i)) {
                a++;
                s1[secret.charAt(i) - '0']++;
            } else {
                s1[secret.charAt(i) - '0']++;
                g1[guess.charAt(i) - '0']++;
            }
        }
        for (int i = 0; i < 10; i++) {
            b += Math.min(s1[i], g1[i]);
        }
        res = a + "A" + b + "B";
        return res;
    }

    private static final int COUNT = 100 * 100 * 100 * 3;

    @me(integer = 2)
    @Deprecated
    public static void main(String[] args) {
        String s = "23.01";
        float f = Float.valueOf(s);
        System.out.println(f + " > 23");
        System.out.println(f > 23);
    }
}

