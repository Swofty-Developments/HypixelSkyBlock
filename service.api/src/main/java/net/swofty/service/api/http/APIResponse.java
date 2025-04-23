package net.swofty.service.api.http;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.Getter;

@Getter
public class APIResponse {
    private StatusResponse status;
    private String message;
    private JsonElement data;

    public APIResponse(StatusResponse status) {
        this.status = status;
    }

    public APIResponse(StatusResponse status, String message) {
        this.status = status;
        this.message = message;
    }

    public APIResponse(StatusResponse status, JsonElement data) {
        this.status = status;
        this.data = data;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static APIResponse success(String message) {
        return new APIResponse(StatusResponse.SUCCESS, message);
    }

    public static APIResponse error(String message) {
        return new APIResponse(StatusResponse.ERROR, message);
    }

    public static APIResponse serverError() {
        return new APIResponse(StatusResponse.ERROR, "An internal server error occurred");
    }
}
