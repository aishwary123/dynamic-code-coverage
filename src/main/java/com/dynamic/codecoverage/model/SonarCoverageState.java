package com.dynamic.codecoverage.model;

/**
 * It stores the Coverage details information for any specific build.<br>
 * This value is returned by Sonar plugin.
 * 
 * @author aishwaryt
 */
public class SonarCoverageState implements ICodeCoverageState {

    private float lineCoverage;

    private float branchCoverage;

    private int linesToCover;

    public float getLineCoverage() {
        return lineCoverage;
    }

    public int getLinesToCover() {
        return linesToCover;
    }

    public void setLinesToCover(int linesToCover) {
        this.linesToCover = linesToCover;
    }

    public void setLineCoverage(float lineCoverage) {
        this.lineCoverage = lineCoverage;
    }

    public float getBranchCoverage() {
        return branchCoverage;
    }

    public void setBranchCoverage(float branchCoverage) {
        this.branchCoverage = branchCoverage;
    }

}
