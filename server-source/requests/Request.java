package requests;

import json.JSONObject;

public class Request {
    public JSONObject content;

    public Request(String id, Type type, Name name, JSONObject data) {
        this.content = new JSONObject();
        this.content.put("id", id);
        this.content.put("type", type);
        this.content.put("name", name);
        this.content.put("data", data);
    }
}
