import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class Listenable<T> {
    Listenable(T object) {
        this.object = object;
    }

    Listenable() {
    }

    private T object;

    private Map<Integer, Consumer<T>> callbacks = new HashMap<>();
    private Map<Integer, Consumer<T>> paused = new HashMap<>();

    private Function<T, T> transform;

    public void emit(T object) {
        this.object = object;
        System.out.println(this.transform);
        for (Consumer<T> callback : this.callbacks.values()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    T o = object;
                    if (transform != null) {
                        o = transform.apply(object);
                    }
                    callback.accept(o);
                }
            }).start();
        }
    }

    public void setTransform(Function<T, T> newTransform) {
        this.transform = newTransform;
        System.out.println(this.transform);
    }

    public void subscribe(Consumer<T> listener, Integer key) {
        callbacks.put(key, listener);
    }

    public void unsubscribe(Integer key) {
        callbacks.remove(key);
    }

    public void pauseSubscription(Integer key) {
        Consumer<T> value = callbacks.remove(key);
        paused.put(key, value);
    }

    public void resumeSubscription(Integer key) {
        Consumer<T> value = paused.remove(key);
        callbacks.put(key, value);
    }

}
