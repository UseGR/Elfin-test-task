package ru.scoring.camunda_app.engine.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class DelegateBase implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        try {
            doExecute(delegateExecution);
        } catch (Exception e) {
            List<String> activityNames = new ArrayList<>();
            List<String> processIds = new ArrayList<>();
            ExecutionEntity execution = (ExecutionEntity) delegateExecution;
            while (execution != null) {
                activityNames.add(execution.getCurrentActivityName() + "/" + execution.getActivityId());
                processIds.add(execution.getProcessInstanceId());
                execution = execution.getProcessInstance().getSuperExecution();
            }
            log.error("Ошибка. Активити: {}, идентификаторы процессов: {}",
                    activityNames,
                    processIds,
                    e
                    );
        }
    }

    public abstract void doExecute(DelegateExecution execution);
}
