package asw1028.access;

import javax.servlet.*;
import java.io.*;
import java.util.HashMap;

public class AsyncAdapter implements AsyncListener{
    HashMap<String, Object> clients;
    public AsyncAdapter(HashMap<String, Object> clients) {
        this.clients = clients;
    }
    @Override
    public void onComplete(AsyncEvent event) throws IOException {}
    @Override
    public void onError(AsyncEvent event) throws IOException {}
    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {}
    @Override
    public void onTimeout(AsyncEvent event) throws IOException {}
}
