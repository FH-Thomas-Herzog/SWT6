package swt6.spring.basic.hello;

/**
 * Created by Thomas on 4/30/2016.
 */
public class GreetingServiceImpl implements GreetingService {

    private static final long serialVersionUID = 6978833085587425773L;

    private String message;

    @Override
    public void sayHello() {
        System.out.println("Hello message: " + message);
    }

    //<editor-fold desc="Getter and setter">
    public void setMessage(String message) {
        this.message = message;
    }
    //</editor-fold>
}
