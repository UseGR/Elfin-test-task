package ru.scoring.camunda_app.engine.variables;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.lang.Nullable;

import static org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper;
import static org.camunda.spin.Spin.JSON;

@UtilityClass
public class Classifier {

    public <T> void setVariable(@NonNull DelegateExecution execution, @NonNull MetaData<T> metaData, @Nullable T data) {
        if (data == null) {
            execution.setVariable(metaData.key(), null);
        } else if (jsonCompatibleType(metaData)) {
            execution.setVariable(metaData.key(), JSON(data));
        } else {
            execution.setVariable(metaData.key(), data);
        }
    }

    public <T> boolean jsonCompatibleType(@NonNull MetaData<T> metaData) {
        if (isPrimitiveOrWrapper(metaData.type())) {
            return false;
        }

        return !String.class.equals(metaData.type());
    }

    @SuppressWarnings("unchecked")
    public static <T> T getVariable(DelegateExecution execution, MetaData<T> metaData) {
        if (hasVariable(execution, metaData)) {
            if (jsonCompatibleType(metaData)) {
                return JSON(execution.getVariable(metaData.key())).mapTo(metaData.type());
            } else {
                return (T) execution.getVariable(metaData.key());
            }
        } else {
            throw new BpmnError("Переменная " + metaData.key() + " не найдена");
        }

    }

    public static <T> boolean hasVariable(@NonNull DelegateExecution execution, @NonNull MetaData<T> metaData) {
        return execution.hasVariable(metaData.key());
    }

}
