package com.tekton.challenge.service.processtask;

import com.tekton.challenge.domain.History;
import com.tekton.challenge.domain.enumeration.ProcessType;
import com.tekton.challenge.repository.HistoryRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SaveHistoryTask implements ProcessTask {

    private final HistoryRepository historyRepository;

    public SaveHistoryTask(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public ProcessType getType() {
        return ProcessType.SAVE_HISTORY;
    }

    @Override
    public void process(ProcessData data) {
        var history = new History().setEndpoint(data.getEndpoint())
                .setParams(data.getParams())
                .setResponse(data.getResponse())
                .setCreatedAt(Instant.now());

        historyRepository.save(history);
    }
}
