package com.tekton.challenge.service.processtask;

public class ProcessData {

    private String endpoint;
    private String params;
    private String response;

    public ProcessData(Object[] params) {
        this.endpoint = params[0].toString();
        this.params = params[1].toString();
        this.response = params[2].toString();
    }

    public ProcessData() {}

    public String getEndpoint() {
        return endpoint;
    }

    public String getParams() {
        return params;
    }

    public String getResponse() {
        return response;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "{" +
                "\"endpoint\":\"" + endpoint + "\"" +
                ", \"params\":\"" + params + "\"" +
                ", \"response\":\"" + response + "\"" +
                "}";
    }
}
