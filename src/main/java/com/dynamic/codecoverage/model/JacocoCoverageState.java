package com.dynamic.codecoverage.model;

import com.dynamic.codecoverage.model.metrics.BranchCoverageMetrics;
import com.dynamic.codecoverage.model.metrics.ClassCoverageMetrics;
import com.dynamic.codecoverage.model.metrics.ComplexityCoverageMetrics;
import com.dynamic.codecoverage.model.metrics.InstructionCoverageMetrics;
import com.dynamic.codecoverage.model.metrics.LineCoverageMetrics;
import com.dynamic.codecoverage.model.metrics.MethodCoverageMetrics;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * It stores the Java Code Coverage details information for any specific
 * build.<br>
 * This value is returned by JaCoCo plugin.
 * 
 * @author aishwaryt
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JacocoCoverageState implements ICodeCoverageState {

    private BranchCoverageMetrics branchCoverage;

    private ClassCoverageMetrics classCoverage;

    private ComplexityCoverageMetrics complexityScore;

    private InstructionCoverageMetrics instructionCoverage;

    private LineCoverageMetrics lineCoverage;

    private MethodCoverageMetrics methodCoverage;

    public BranchCoverageMetrics getBranchCoverage() {
        return branchCoverage;
    }

    public void setBranchCoverage(BranchCoverageMetrics branchCoverage) {
        this.branchCoverage = branchCoverage;
    }

    public ClassCoverageMetrics getClassCoverage() {
        return classCoverage;
    }

    public void setClassCoverage(ClassCoverageMetrics classCoverage) {
        this.classCoverage = classCoverage;
    }

    public ComplexityCoverageMetrics getComplexityScore() {
        return complexityScore;
    }

    public void setComplexityScore(ComplexityCoverageMetrics complexityScore) {
        this.complexityScore = complexityScore;
    }

    public InstructionCoverageMetrics getInstructionCoverage() {
        return instructionCoverage;
    }

    public void setInstructionCoverage(InstructionCoverageMetrics instructionCoverage) {
        this.instructionCoverage = instructionCoverage;
    }

    public LineCoverageMetrics getLineCoverage() {
        return lineCoverage;
    }

    public void setLineCoverage(LineCoverageMetrics lineCoverage) {
        this.lineCoverage = lineCoverage;
    }

    public MethodCoverageMetrics getMethodCoverage() {
        return methodCoverage;
    }

    public void setMethodCoverage(MethodCoverageMetrics methodCoverage) {
        this.methodCoverage = methodCoverage;
    }

}
