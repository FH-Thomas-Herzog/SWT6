package at.fh.ooe.swt6.em.web.mvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Serializable;

/**
 * Created by Thomas on 5/27/2016.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorModel implements Serializable {

    private String key;
    private String action;
    private String method;
    private boolean postBack;

    public boolean isPostRequest() {
        return RequestMethod.POST.name().equalsIgnoreCase(method);
    }
}
