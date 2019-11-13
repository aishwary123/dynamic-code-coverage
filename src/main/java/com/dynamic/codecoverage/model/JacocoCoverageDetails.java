package com.dynamic.codecoverage.model;

/**
 * {@link JacocoCoverageDetails} maps {@link JacocoCoverageState} with the build
 * number that returned the information.
 * 
 * @author aishwaryt
 */
public class JacocoCoverageDetails {

    private JacocoCoverageState jacocoCoverageState;

    private int buildNumber;

    public JacocoCoverageState getJacocoCoverageState() {
        return jacocoCoverageState;
    }

    public void setJacocoCoverageState(JacocoCoverageState jacocoCoverageState) {
        this.jacocoCoverageState = jacocoCoverageState;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(int buildNumber) {
        this.buildNumber = buildNumber;
    }

}
