package com.dynamic.codecoverage.model.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This model is used to hold the configuration details related to any specific
 * service.<br>
 * {@code product, type, serviceName, jobName, testType} are mandatory
 * fields.<br>
 * {@code isDynamicGatingEnabled} is used to enable/disable dynamic gating
 * feature.<br>
 * {@link Thresholds} is used to keep different types of threshold information.
 * 
 * @author aishwaryt
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceObject {

    @NotNull
    private String product;

    @NotNull
    private ReportType type;

    @NotNull
    private String serviceName;

    @NotNull
    private String jobName;

    @NotNull
    private TestType testType;

    private boolean isDynamicGatingEnabled;

    private CodeCoverageType codeCoverageType;

    private int lastReportedBuildNumber;

    private boolean changeBuildStatus;

    private boolean failBuildOnCoverageFail;

    private Thresholds thresholds;

    private Patterns patterns;

    @JsonCreator
    public ServiceObject() {
        thresholds = new Thresholds();
        patterns = new Patterns();
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public TestType getTestType() {
        return testType;
    }

    public void setTestType(TestType testType) {
        this.testType = testType;
    }

    public boolean isDynamicGatingEnabled() {
        return isDynamicGatingEnabled;
    }

    public void setDynamicGatingEnabled(boolean isDynamicGatingEnabled) {
        this.isDynamicGatingEnabled = isDynamicGatingEnabled;
    }

    public CodeCoverageType getCodeCoverageType() {
        return codeCoverageType;
    }

    public void setCodeCoverageType(CodeCoverageType codeCoverageType) {
        this.codeCoverageType = codeCoverageType;
    }

    public int getLastReportedBuildNumber() {
        return lastReportedBuildNumber;
    }

    public void setLastReportedBuildNumber(int lastReportedBuildNumber) {
        this.lastReportedBuildNumber = lastReportedBuildNumber;
    }

    public boolean isChangeBuildStatus() {
        return changeBuildStatus;
    }

    public void setChangeBuildStatus(boolean changeBuildStatus) {
        this.changeBuildStatus = changeBuildStatus;
    }

    public boolean isFailBuildOnCoverageFail() {
        return failBuildOnCoverageFail;
    }

    public void setFailBuildOnCoverageFail(boolean failBuildOnCoverageFail) {
        this.failBuildOnCoverageFail = failBuildOnCoverageFail;
    }

    public Thresholds getThresholds() {
        return thresholds;
    }

    public void setThresholds(Thresholds thresholds) {
        this.thresholds = thresholds;
    }

    public Patterns getPatterns() {
        return patterns;
    }

    public void setPatterns(Patterns patterns) {
        this.patterns = patterns;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (changeBuildStatus ? 1231 : 1237);
        result = prime * result + ((codeCoverageType == null) ? 0
                    : codeCoverageType.hashCode());
        result = prime * result + (failBuildOnCoverageFail ? 1231 : 1237);
        result = prime * result + (isDynamicGatingEnabled ? 1231 : 1237);
        result = prime * result + ((jobName == null) ? 0 : jobName.hashCode());
        result = prime * result + lastReportedBuildNumber;
        result = prime * result
                    + ((patterns == null) ? 0 : patterns.hashCode());
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        result = prime * result
                    + ((serviceName == null) ? 0 : serviceName.hashCode());
        result = prime * result
                    + ((testType == null) ? 0 : testType.hashCode());
        result = prime * result
                    + ((thresholds == null) ? 0 : thresholds.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        ServiceObject other = (ServiceObject) obj;
        if (changeBuildStatus != other.changeBuildStatus)
            return false;
        if (codeCoverageType != other.codeCoverageType)
            return false;
        if (failBuildOnCoverageFail != other.failBuildOnCoverageFail)
            return false;
        if (isDynamicGatingEnabled != other.isDynamicGatingEnabled)
            return false;
        if (jobName == null) {
            if (other.jobName != null)
                return false;
        } else if (!jobName.equals(other.jobName))
            return false;
        if (lastReportedBuildNumber != other.lastReportedBuildNumber)
            return false;
        if (patterns == null) {
            if (other.patterns != null)
                return false;
        } else if (!patterns.equals(other.patterns))
            return false;
        if (product == null) {
            if (other.product != null)
                return false;
        } else if (!product.equals(other.product))
            return false;
        if (serviceName == null) {
            if (other.serviceName != null)
                return false;
        } else if (!serviceName.equals(other.serviceName))
            return false;
        if (testType != other.testType)
            return false;
        if (thresholds == null) {
            if (other.thresholds != null)
                return false;
        } else if (!thresholds.equals(other.thresholds))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ServiceObject [product=" + product + ", type=" + type
                    + ", serviceName=" + serviceName + ", jobName=" + jobName
                    + ", testType=" + testType + ", isDynamicGatingEnabled="
                    + isDynamicGatingEnabled + ", codeCoverageType="
                    + codeCoverageType + ", lastReportedBuildNumber="
                    + lastReportedBuildNumber + ", changeBuildStatus="
                    + changeBuildStatus + ", failBuildOnCoverageFail="
                    + failBuildOnCoverageFail + ", thresholds=" + thresholds
                    + ", patterns=" + patterns + "]";
    }

}
