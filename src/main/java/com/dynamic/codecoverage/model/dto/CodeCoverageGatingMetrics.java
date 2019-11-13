package com.dynamic.codecoverage.model.dto;

public abstract class CodeCoverageGatingMetrics {

    boolean enabled;

    int minimumCoveragePercentageDefault;

    int maximumCoveragePercentageDefault;

    int minimumCoveragePercentageDynamic;

    int maximumCoveragePercentageDynamic;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnbaled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMinimumCoveragePercentageDefault() {
        return minimumCoveragePercentageDefault;
    }

    public void setMinimumCoveragePercentageDefault(int minimumCoveragePercentageDefault) {
        this.minimumCoveragePercentageDefault = minimumCoveragePercentageDefault;
    }

    public int getMaximumCoveragePercentageDefault() {
        return maximumCoveragePercentageDefault;
    }

    public void setMaximumCoveragePercentageDefault(int maximumCoveragePercentageDefault) {
        this.maximumCoveragePercentageDefault = maximumCoveragePercentageDefault;
    }

    public int getMinimumCoveragePercentageDynamic() {
        return minimumCoveragePercentageDynamic;
    }

    public void setMinimumCoveragePercentageDynamic(int minimumCoveragePercentageDynamic) {
        this.minimumCoveragePercentageDynamic = minimumCoveragePercentageDynamic;
    }

    public int getMaximumCoveragePercentageDynamic() {
        return maximumCoveragePercentageDynamic;
    }

    public void setMaximumCoveragePercentageDynamic(int maximumCoveragePercentageDynamic) {
        this.maximumCoveragePercentageDynamic = maximumCoveragePercentageDynamic;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + maximumCoveragePercentageDefault;
        result = prime * result + maximumCoveragePercentageDynamic;
        result = prime * result + minimumCoveragePercentageDefault;
        result = prime * result + minimumCoveragePercentageDynamic;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CodeCoverageGatingMetrics other = (CodeCoverageGatingMetrics) obj;
        if (enabled != other.enabled)
            return false;
        if (maximumCoveragePercentageDefault != other.maximumCoveragePercentageDefault)
            return false;
        if (maximumCoveragePercentageDynamic != other.maximumCoveragePercentageDynamic)
            return false;
        if (minimumCoveragePercentageDefault != other.minimumCoveragePercentageDefault)
            return false;
        if (minimumCoveragePercentageDynamic != other.minimumCoveragePercentageDynamic)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CodeCoverageState [enbaled=" + enabled
                    + ", minimumCoveragePercentageDefault="
                    + minimumCoveragePercentageDefault
                    + ", maximumCoveragePercentageDefault="
                    + maximumCoveragePercentageDefault
                    + ", minimumCoveragePercentageDynamic="
                    + minimumCoveragePercentageDynamic
                    + ", maximumCoveragePercentageDynamic="
                    + maximumCoveragePercentageDynamic + "]";
    }

}
