package com.dynamic.codecoverage.model.metrics;

public abstract class CodeCoverageMetrics {

    int covered;

    int missed;

    int percentage;

    float percentageFloat;

    int total;

    public int getCovered() {
        return covered;
    }

    public void setCovered(int covered) {
        this.covered = covered;
    }

    public int getMissed() {
        return missed;
    }

    public void setMissed(int missed) {
        this.missed = missed;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public float getPercentageFloat() {
        return percentageFloat;
    }

    public void setPercentageFloat(float percentageFloat) {
        this.percentageFloat = percentageFloat;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
