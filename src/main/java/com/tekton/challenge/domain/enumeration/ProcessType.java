package com.tekton.challenge.domain.enumeration;

import java.util.Arrays;

public enum ProcessType {
    SAVE_HISTORY("/api/calculate");

    private String pathProcess;

    ProcessType(String pathProcess) {
        this.pathProcess = pathProcess;
    }

    public static ProcessType findByPath(String pathProcess) {
        return Arrays.stream(values())
                     .filter(pt -> pt.pathProcess.equals(pathProcess))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("pathProcess not found"));
    }
}
