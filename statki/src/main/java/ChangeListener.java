import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ChangeListener<T> {
    private boolean ignoreEqual = false;

    ChangeListener(T object) {
        this.object = object;
    }

    ChangeListener() {
    }

    ChangeListener(boolean ignoreEqual) {
        this.ignoreEqual = ignoreEqual;
    }

    ChangeListener(T object, boolean ignoreEqual) {
        this.object = object;
        this.ignoreEqual = ignoreEqual;
    }

    private T object;

    private Map<Integer, Consumer<T>> callbacks = new HashMap<>();
    private Map<Integer, Consumer<T>> paused = new HashMap<>();

    private Function<T, T> transform;

    public void emit(T object) {
        if (object == null) {
            return;
        }
        if (!this.ignoreEqual && this.object != null && this.object.equals(object)) {
            return;
        }
        this.object = object;
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

    public void reapply() {
        if (this.object == null) {
            return;
        }
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
