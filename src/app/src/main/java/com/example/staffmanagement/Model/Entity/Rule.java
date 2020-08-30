package com.example.staffmanagement.Model.Entity;

public class Rule {
    public enum TYPE_PERIOD{
        DAY,MONTH,YEAR;
    };
    private int MaxNumberRequestOfRule;
    private int Period;
    private TYPE_PERIOD TypePeriod;

    public Rule(int maxNumberRequestOfRule, int period) {
        MaxNumberRequestOfRule = maxNumberRequestOfRule;
        Period = period;
        TypePeriod = TYPE_PERIOD.DAY;
    }

    public int getMaxNumberRequestOfRule() {
        return MaxNumberRequestOfRule;
    }

    public void setMaxNumberRequestOfRule(int maxNumberRequestOfRule) {
        MaxNumberRequestOfRule = maxNumberRequestOfRule;
    }

    public int getPeriod() {
        return Period;
    }

    public void setPeriod(int period) {
        Period = period;
    }

    public TYPE_PERIOD getTypePeriod() {
        return TypePeriod;
    }

    public void setTypePeriod(TYPE_PERIOD typePeriod) {
        TypePeriod = typePeriod;
    }
}
