package ru.scoring.camunda_app.process.main.variable;

import ru.scoring.camunda_app.engine.variables.MetaData;
import ru.scoring.camunda_app.engine.variables.ProcessVariable;

import java.util.UUID;

public interface MainProcessVariables {

    /**
     * Контекст
     */
    MetaData<Context> CONTEXT = new ProcessVariable<>("context", Context.class);

    /**
     * Идентификатор процесса camunda
     */
    MetaData<UUID> PROCESS_ID = new ProcessVariable<>("process_id", UUID.class);

    /**
     * Признак того, что компания является ИП
     */
    MetaData<Boolean> IS_INDIVIDUAL_UNDERTAKER = new ProcessVariable<>("is_individual_undertaker", Boolean.class);

    /**
     * Признак того, что компания находится в запрещенном регионе
     */
    MetaData<Boolean> IS_COMPANY_IN_PROHIBITED_REGION = new ProcessVariable<>("is_company_in_prohibited_region", Boolean.class);

    /**
     * Признак того, что капитал компании не соответствует скорингу
     */
    MetaData<Boolean> IS_CAPITAL_LOW = new ProcessVariable<>("is_capital_low", Boolean.class);

    /**
     * Признак того, что компания является резидентом РФ
     */
    MetaData<Boolean> IS_RESIDENT_COMPANY = new ProcessVariable<>("is_resident_company", Boolean.class);

    /**
     * Признак скоринг компании, false - успешный скоринг
     */
    MetaData<Boolean> IS_SCORING_FAILED = new ProcessVariable<>("is_scoring_failed", Boolean.class);
    
}
