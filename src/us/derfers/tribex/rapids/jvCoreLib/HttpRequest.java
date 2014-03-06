package us.derfers.tribex.rapids.jvCoreLib;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jdesktop.http.Method;
import org.jdesktop.http.async.AsyncHttpRequest;
import org.jdesktop.http.async.AsyncHttpRequest.ReadyState;

public class HttpRequest {
    final AsyncHttpRequest req = new AsyncHttpRequest();

    public void open(String method, String url) {
        req.open(Method.GET, "http://localhost/~joshua/test.php");

        req.addReadyStateChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getNewValue() == ReadyState.LOADED) {
                    String response = req.getResponseText();
                    System.out.println(response);
                }
            }
        });
        req.send();
    }
}
