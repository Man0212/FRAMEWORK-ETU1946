package etu1946.framework.view;

import java.util.HashMap;

public class ModelView {
    String view;
    boolean isJson = false;
    HashMap<String, Object> data;
    HashMap<String, Object> session = new HashMap<>();

    public boolean isJson() {
        return isJson;
    }

    public void setJson(boolean isJson) {
        this.isJson = isJson;
    }
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
