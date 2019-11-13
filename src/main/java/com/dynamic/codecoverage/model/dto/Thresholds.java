package com.dynamic.codecoverage.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * It holds the different types of threshold information.<br>
 * This class is composed of different types of coverage types.
 * 
 * @author aishwaryt
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Thresholds {

    private LineCoverageGatingMetrics lineCoverageGatingMetrics;

    private BranchCoverageGatingMetrics branchCoverageGatingMetrics;

    private ClassCoverageGatingMetrics classCoverageGatingMetrics;

    private MethodCoverageGatingMetrics methodCoverageGatingMetrics;

    private InstructionCoverageGatingMetrics instructionCoverageGatingMetrics;

    private ComplexityCoverageGatingMetrics complexityCoverageGatingMetrics;

    @JsonCreator
    public Thresholds() {
        lineCoverageGatingMetrics = new LineCoverageGatingMetrics();
        branchCoverageGatingMetrics = new BranchCoverageGatingMetrics();
        classCoverageGatingMetrics = new ClassCoverageGatingMetrics();
        methodCoverageGatingMetrics = new MethodCoverageGatingMetrics();
        instructionCoverageGatingMetrics = new InstructionCoverageGatingMetrics();
        complexityCoverageGatingMetrics = new ComplexityCoverageGatingMetrics();
    }

    public LineCoverageGatingMetrics getLineCoverageGatingMetrics() {
        return lineCoverageGatingMetrics;
    }

    public void setLineCoverageGatingMetrics(LineCoverageGatingMetrics lineCoverageGatingMetrics) {
        this.lineCoverageGatingMetrics = lineCoverageGatingMetrics;
    }

    public BranchCoverageGatingMetrics getBranchCoverageGatingMetrics() {
        return branchCoverageGatingMetrics;
    }

    public void setBranchCoverageGatingMetrics(BranchCoverageGatingMetrics branchCoverageGatingMetrics) {
        this.branchCoverageGatingMetrics = branchCoverageGatingMetrics;
    }

    public ClassCoverageGatingMetrics getClassCoverageGatingMetrics() {
        return classCoverageGatingMetrics;
    }

    public void setClassCoverageGatingMetrics(ClassCoverageGatingMetrics classCoverageGatingMetrics) {
        this.classCoverageGatingMetrics = classCoverageGatingMetrics;
    }

    public MethodCoverageGatingMetrics getMethodCoverageGatingMetrics() {
        return methodCoverageGatingMetrics;
    }

    public void setMethodCoverageGatingMetrics(MethodCoverageGatingMetrics methodCoverageGatingMetrics) {
        this.methodCoverageGatingMetrics = methodCoverageGatingMetrics;
    }

    public InstructionCoverageGatingMetrics getInstructionCoverageGatingMetrics() {
        return instructionCoverageGatingMetrics;
    }

    public void setInstructionCoverageGatingMetrics(InstructionCoverageGatingMetrics instructionCoverageGatingMetrics) {
        this.instructionCoverageGatingMetrics = instructionCoverageGatingMetrics;
    }

    public ComplexityCoverageGatingMetrics getComplexityCoverageGatingMetrics() {
        return complexityCoverageGatingMetrics;
    }

    public void setComplexityCoverageGatingMetrics(ComplexityCoverageGatingMetrics complexityCoverageGatingMetrics) {
        this.complexityCoverageGatingMetrics = complexityCoverageGatingMetrics;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((branchCoverageGatingMetrics == null) ? 0
                    : branchCoverageGatingMetrics.hashCode());
        result = prime * result + ((classCoverageGatingMetrics == null) ? 0
                    : classCoverageGatingMetrics.hashCode());
        result = prime * result + ((complexityCoverageGatingMetrics == null) ? 0
                    : complexityCoverageGatingMetrics.hashCode());
        result = prime * result
                    + ((instructionCoverageGatingMetrics == null) ? 0
                                : instructionCoverageGatingMetrics.hashCode());
        result = prime * result + ((lineCoverageGatingMetrics == null) ? 0
                    : lineCoverageGatingMetrics.hashCode());
        result = prime * result + ((methodCoverageGatingMetrics == null) ? 0
                    : methodCoverageGatingMetrics.hashCode());
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
        Thresholds other = (Thresholds) obj;
        if (branchCoverageGatingMetrics == null) {
            if (other.branchCoverageGatingMetrics != null)
                return false;
        } else if (!branchCoverageGatingMetrics.equals(
                    other.branchCoverageGatingMetrics))
            return false;
        if (classCoverageGatingMetrics == null) {
            if (other.classCoverageGatingMetrics != null)
                return false;
        } else if (!classCoverageGatingMetrics.equals(
                    other.classCoverageGatingMetrics))
            return false;
        if (complexityCoverageGatingMetrics == null) {
            if (other.complexityCoverageGatingMetrics != null)
                return false;
        } else if (!complexityCoverageGatingMetrics.equals(
                    other.complexityCoverageGatingMetrics))
            return false;
        if (instructionCoverageGatingMetrics == null) {
            if (other.instructionCoverageGatingMetrics != null)
                return false;
        } else if (!instructionCoverageGatingMetrics.equals(
                    other.instructionCoverageGatingMetrics))
            return false;
        if (lineCoverageGatingMetrics == null) {
            if (other.lineCoverageGatingMetrics != null)
                return false;
        } else if (!lineCoverageGatingMetrics.equals(
                    other.lineCoverageGatingMetrics))
            return false;
        if (methodCoverageGatingMetrics == null) {
            if (other.methodCoverageGatingMetrics != null)
                return false;
        } else if (!methodCoverageGatingMetrics.equals(
                    other.methodCoverageGatingMetrics))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Thresholds [lineCoverageGatingMetrics="
                    + lineCoverageGatingMetrics
                    + ", branchCoverageGatingMetrics="
                    + branchCoverageGatingMetrics
                    + ", classCoverageGatingMetrics="
                    + classCoverageGatingMetrics
                    + ", methodCoverageGatingMetrics="
                    + methodCoverageGatingMetrics
                    + ", instructionCoverageGatingMetrics="
                    + instructionCoverageGatingMetrics
                    + ", complexityCoverageGatingMetrics="
                    + complexityCoverageGatingMetrics + "]";
    }

}
