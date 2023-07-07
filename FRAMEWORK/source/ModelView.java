package etu1946.framework.view;

import java.util.HashMap;

public class ModelView {
    String view;
    HashMap<String, Object> data;
    HashMap<String, Object> session = new HashMap<>();

    public HashMap<String, Object> getSession() {
        return session;
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }

    public boolean _session() {
        return session != null;
    }

    public ModelView() {
        this.data = new HashMap<>();
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public void addItem(String key, Object value) {
        this.data.put(key, value);
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
}
