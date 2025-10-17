package com.tekton.challenge.service.processtask;

import com.tekton.challenge.domain.enumeration.ProcessType;

public interface ProcessTask {

    ProcessType getType();

    void process(ProcessData data);
}
