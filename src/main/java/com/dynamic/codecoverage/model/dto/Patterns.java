package com.dynamic.codecoverage.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Patterns {

    private String inclusionPattern;

    private String exclusionPattern;

    private String execPattern;

    private String classPattern;

    private String sourcePattern;

    private String sourceInclusionPattern;

    private String sourceExclusionPattern;

    @JsonCreator
    public Patterns() {
        inclusionPattern = "";
        exclusionPattern = "";
        execPattern = "**/**.exec";
        classPattern = "**/classes";
        sourcePattern = "**/src/main/java";
        sourceInclusionPattern = "**/*.java";
        sourceExclusionPattern = "";
    }

    public String getInclusionPattern() {
        return inclusionPattern;
    }

    public void setInclusionPattern(String inclusionPattern) {
        this.inclusionPattern = inclusionPattern;
    }

    public String getExclusionPattern() {
        return exclusionPattern;
    }

    public void setExclusionPattern(String exclusionPattern) {
        this.exclusionPattern = exclusionPattern;
    }

    public String getExecPattern() {
        return execPattern;
    }

    public void setExecPattern(String execPattern) {
        this.execPattern = execPattern;
    }

    public String getClassPattern() {
        return classPattern;
    }

    public void setClassPattern(String classPattern) {
        this.classPattern = classPattern;
    }

    public String getSourcePattern() {
        return sourcePattern;
    }

    public void setSourcePattern(String sourcePattern) {
        this.sourcePattern = sourcePattern;
    }

    public String getSourceInclusionPattern() {
        return sourceInclusionPattern;
    }

    public void setSourceInclusionPattern(String sourceInclusionPattern) {
        this.sourceInclusionPattern = sourceInclusionPattern;
    }

    public String getSourceExclusionPattern() {
        return sourceExclusionPattern;
    }

    public void setSourceExclusionPattern(String sourceExclusionPattern) {
        this.sourceExclusionPattern = sourceExclusionPattern;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                    + ((classPattern == null) ? 0 : classPattern.hashCode());
        result = prime * result + ((exclusionPattern == null) ? 0
                    : exclusionPattern.hashCode());
        result = prime * result
                    + ((execPattern == null) ? 0 : execPattern.hashCode());
        result = prime * result + ((inclusionPattern == null) ? 0
                    : inclusionPattern.hashCode());
        result = prime * result + ((sourceExclusionPattern == null) ? 0
                    : sourceExclusionPattern.hashCode());
        result = prime * result + ((sourceInclusionPattern == null) ? 0
                    : sourceInclusionPattern.hashCode());
        result = prime * result
                    + ((sourcePattern == null) ? 0 : sourcePattern.hashCode());
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
        Patterns other = (Patterns) obj;
        if (classPattern == null) {
            if (other.classPattern != null)
                return false;
        } else if (!classPattern.equals(other.classPattern))
            return false;
        if (exclusionPattern == null) {
            if (other.exclusionPattern != null)
                return false;
        } else if (!exclusionPattern.equals(other.exclusionPattern))
            return false;
        if (execPattern == null) {
            if (other.execPattern != null)
                return false;
        } else if (!execPattern.equals(other.execPattern))
            return false;
        if (inclusionPattern == null) {
            if (other.inclusionPattern != null)
                return false;
        } else if (!inclusionPattern.equals(other.inclusionPattern))
            return false;
        if (sourceExclusionPattern == null) {
            if (other.sourceExclusionPattern != null)
                return false;
        } else if (!sourceExclusionPattern.equals(other.sourceExclusionPattern))
            return false;
        if (sourceInclusionPattern == null) {
            if (other.sourceInclusionPattern != null)
                return false;
        } else if (!sourceInclusionPattern.equals(other.sourceInclusionPattern))
            return false;
        if (sourcePattern == null) {
            if (other.sourcePattern != null)
                return false;
        } else if (!sourcePattern.equals(other.sourcePattern))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Patterns [inclusionPattern=" + inclusionPattern
                    + ", exclusionPattern=" + exclusionPattern
                    + ", execPattern=" + execPattern + ", classPattern="
                    + classPattern + ", sourcePattern=" + sourcePattern
                    + ", sourceInclusionPattern=" + sourceInclusionPattern
                    + ", sourceExclusionPattern=" + sourceExclusionPattern
                    + "]";
    }

}
