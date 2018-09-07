import java.util.Locale;
import javax.accessibility.AccessibleState;

public class R extends AccessibleState {
    public R(String key) {
        super(key);
    }

    @Override
    public String toDisplayString(Locale locale) {
        return toDisplayString("resources", locale);
    }

    public static void main(String[] args) {
        final R r = new R("key1");
        System.out.println("R:" + r);
    }

}
