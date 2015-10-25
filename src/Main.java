import java.util.ArrayList;
import java.util.List;

/**
 * Created by niejia on 15/10/4.
 */
public class Main {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("A");
        list.remove("A");
        list.remove("B");
        System.out.println(list);
    }

}
