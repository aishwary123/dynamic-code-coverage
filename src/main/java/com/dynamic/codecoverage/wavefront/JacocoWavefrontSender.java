package com.dynamic.codecoverage.wavefront;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dynamic.codecoverage.exceptions.DataReportingException;
import com.dynamic.codecoverage.exceptions.WavefrontClientException;
import com.dynamic.codecoverage.messages.CustomMessages;
import com.dynamic.codecoverage.model.ICodeCoverageState;
import com.dynamic.codecoverage.model.JacocoCoverageState;
import com.dynamic.codecoverage.model.dto.CodeCoverageGatingMetrics;
import com.dynamic.codecoverage.model.dto.ServiceObject;
import com.dynamic.codecoverage.model.dto.Thresholds;

public class JacocoWavefrontSender extends WavefrontSender {

    public static final String PREFIX_THRESHOLD = "code-coverage.threshold.";
    public static final String MIN_DEFAULT = "min.default";
    public static final String MAX_DEFAULT = "max.default";
    public static final String MIN_DYNAMIC = "min.dynamic";
    public static final String MAX_DYNAMIC = "max.dynamic";
    public static final String PERCENTAGE_MIN_DEFAULT = "percentage."
                + MIN_DEFAULT;
    public static final String PERCENTAGE_MAX_DEFAULT = "percentage."
                + MAX_DEFAULT;
    public static final String PERCENTAGE_MIN_DYNAMIC = "percentage."
                + MIN_DYNAMIC;
    public static final String PERCENTAGE_MAX_DYNAMIC = "percentage."
                + MAX_DYNAMIC;

    private static final Logger LOGGER = LoggerFactory.getLogger(
                JacocoWavefrontSender.class);

    public JacocoWavefrontSender(String wavefrontProxyUrl,
                                 int wavefrontProxyPort)
        throws WavefrontClientException {
        super(wavefrontProxyUrl, wavefrontProxyPort);
    }

    @Override
    public void sendDataToWavefront(String reporterPrefix,
                                    ServiceObject serviceObject,
                                    ICodeCoverageState codeCoverageState)
        throws WavefrontClientException {
        if (!(codeCoverageState instanceof JacocoCoverageState)) {
            throw new ClassCastException(CustomMessages.CLASS_CAST_EXCEPTION);
        }

        JacocoCoverageState jacocoCoverageState = (JacocoCoverageState) codeCoverageState;
        Map<String, String> pointTags = new HashMap<>();
        pointTags.put("serviceName", serviceObject.getServiceName());
        pointTags.put("jobname", serviceObject.getJobName());
        pointTags.put("type", serviceObject.getType().toString());
        pointTags.put("testType", serviceObject.getTestType().toString());
        pointTags.put("product", serviceObject.getProduct());
        try {
            reportCodeCoverage(reporterPrefix, pointTags, serviceObject,
                        jacocoCoverageState);
            reportCodeCoverageThreshold(reporterPrefix, pointTags,
                        serviceObject);
        } catch (UnknownHostException unknownHostException) {
            LOGGER.error(
                        CustomMessages.EXCEPTION_DETAILS.concat(
                                    CustomMessages.PARAMETER_PLACEHOLDER),
                        unknownHostException.getMessage());
            throw new DataReportingException(
                        CustomMessages.SENDING_DATA_TO_WAVEFRONT_SERVER_FAILED);
        } finally {
            wfClient.flushClient();
        }

    }

    // For comparison between current code-coverage value and threshold, we have
    // added point tags.
    // Using these point tags we can plot graph and make a comparison.
    private void reportCodeCoverage(final String reporterPrefix,
                                    final Map<String, String> pointTags,
                                    final ServiceObject serviceObject,
                                    final JacocoCoverageState jacocoCoverageState)
        throws WavefrontClientException, UnknownHostException {
        final String customPrefix = Strings.isNotBlank(reporterPrefix)
                    ? reporterPrefix + "." + PREFIX
                    : PREFIX;

        // Line code coverage
        if (null != jacocoCoverageState.getLineCoverage()) {

            addThresholdInPointTags(pointTags,
                        serviceObject.getThresholds().getLineCoverageGatingMetrics());
            wfClient.saveStats(customPrefix + "line.percentage",
                        jacocoCoverageState.getLineCoverage().getPercentage(),
                        InetAddress.getLocalHost().getHostName(), pointTags);
            removeThresholdFromPointTags(pointTags);
        }
        // Branch code coverage
        if (null != jacocoCoverageState.getBranchCoverage()) {
            addThresholdInPointTags(pointTags,
                        serviceObject.getThresholds().getBranchCoverageGatingMetrics());
            wfClient.saveStats(customPrefix + "branch.percentage",
                        jacocoCoverageState.getBranchCoverage().getPercentage(),
                        InetAddress.getLocalHost().getHostName(), pointTags);
            removeThresholdFromPointTags(pointTags);
        }
        // Method code coverage
        if (null != jacocoCoverageState.getMethodCoverage()) {
            addThresholdInPointTags(pointTags,
                        serviceObject.getThresholds().getMethodCoverageGatingMetrics());
            wfClient.saveStats(customPrefix + "method.percentage",
                        jacocoCoverageState.getMethodCoverage().getPercentage(),
                        InetAddress.getLocalHost().getHostName(), pointTags);
            removeThresholdFromPointTags(pointTags);
        }
        // Class code coverage
        if (null != jacocoCoverageState.getClassCoverage()) {
            addThresholdInPointTags(pointTags,
                        serviceObject.getThresholds().getClassCoverageGatingMetrics());
            wfClient.saveStats(customPrefix + "class.percentage",
                        jacocoCoverageState.getClassCoverage().getPercentage(),
                        InetAddress.getLocalHost().getHostName(), pointTags);
            removeThresholdFromPointTags(pointTags);
        }
        // Instruction code coverage
        if (null != jacocoCoverageState.getInstructionCoverage()) {
            addThresholdInPointTags(pointTags,
                        serviceObject.getThresholds().getInstructionCoverageGatingMetrics());
            wfClient.saveStats(customPrefix + "instruction.percentage",
                        jacocoCoverageState.getInstructionCoverage().getPercentage(),
                        InetAddress.getLocalHost().getHostName(), pointTags);
            removeThresholdFromPointTags(pointTags);
        }
        // Complexity code coverage
        if (null != jacocoCoverageState.getComplexityScore()) {
            addThresholdInPointTags(pointTags,
                        serviceObject.getThresholds().getComplexityCoverageGatingMetrics());
            wfClient.saveStats(customPrefix + "complexity.percentage",
                        jacocoCoverageState.getComplexityScore().getPercentage(),
                        InetAddress.getLocalHost().getHostName(), pointTags);
            removeThresholdFromPointTags(pointTags);
        }
        // Lines total code coverage
        if (null != jacocoCoverageState.getLineCoverage()) {
            wfClient.saveStats(customPrefix + "line.totalLines",
                        jacocoCoverageState.getLineCoverage().getTotal(),
                        InetAddress.getLocalHost().getHostName(), pointTags);

        }

    }

    // It will report threshold value as a separate metric.
    private void reportCodeCoverageThreshold(final String reporterPrefix,
                                             final Map<String, String> pointTags,
                                             final ServiceObject serviceObject)
        throws UnknownHostException, WavefrontClientException {
        final String customPrefix = Strings.isNotBlank(reporterPrefix)
                    ? reporterPrefix + "." + PREFIX_THRESHOLD
                    : PREFIX_THRESHOLD;
        if (null != serviceObject.getThresholds()) {
            Thresholds thresholds = serviceObject.getThresholds();
            // Line coverage threshold value reporting
            if (null != thresholds.getLineCoverageGatingMetrics()
                        && thresholds.getLineCoverageGatingMetrics().isEnabled()) {
                savePercentageThreshold(customPrefix + "line.", pointTags,
                            thresholds.getLineCoverageGatingMetrics());
            }
            // Branch coverage threshold value reporting
            if (null != thresholds.getBranchCoverageGatingMetrics()
                        && thresholds.getBranchCoverageGatingMetrics().isEnabled()) {
                savePercentageThreshold(customPrefix + "branch.", pointTags,
                            thresholds.getBranchCoverageGatingMetrics());
            }
            // Method coverage threshold value reporting
            if (null != thresholds.getMethodCoverageGatingMetrics()
                        && thresholds.getMethodCoverageGatingMetrics().isEnabled()) {
                savePercentageThreshold(customPrefix + "method.", pointTags,
                            thresholds.getMethodCoverageGatingMetrics());
            }
            // Class coverage threshold value reporting
            if (null != thresholds.getClassCoverageGatingMetrics()
                        && thresholds.getClassCoverageGatingMetrics().isEnabled()) {
                savePercentageThreshold(customPrefix + "class.", pointTags,
                            thresholds.getClassCoverageGatingMetrics());
            }
            // Instruction coverage threshold value reporting
            if (null != thresholds.getInstructionCoverageGatingMetrics()
                        && thresholds.getInstructionCoverageGatingMetrics().isEnabled()) {
                savePercentageThreshold(customPrefix + "instruction.",
                            pointTags,
                            thresholds.getInstructionCoverageGatingMetrics());
            }
            // Complexity coverage threshold value reporting
            if (null != thresholds.getComplexityCoverageGatingMetrics()
                        && thresholds.getComplexityCoverageGatingMetrics().isEnabled()) {
                savePercentageThreshold(customPrefix + "complexity.", pointTags,
                            thresholds.getComplexityCoverageGatingMetrics());
            }
        }
    }

    private void savePercentageThreshold(final String prefix,
                                         final Map<String, String> pointTags,
                                         final CodeCoverageGatingMetrics codeCoverageGatingMetrics)
        throws UnknownHostException, WavefrontClientException {
        wfClient.saveStats(prefix + PERCENTAGE_MIN_DEFAULT,
                    codeCoverageGatingMetrics.getMinimumCoveragePercentageDefault(),
                    InetAddress.getLocalHost().getHostName(), pointTags);
        wfClient.saveStats(prefix + PERCENTAGE_MAX_DEFAULT,
                    codeCoverageGatingMetrics.getMaximumCoveragePercentageDefault(),
                    InetAddress.getLocalHost().getHostName(), pointTags);
        wfClient.saveStats(prefix + PERCENTAGE_MIN_DYNAMIC,
                    codeCoverageGatingMetrics.getMinimumCoveragePercentageDynamic(),
                    InetAddress.getLocalHost().getHostName(), pointTags);
        wfClient.saveStats(prefix + PERCENTAGE_MAX_DYNAMIC,
                    codeCoverageGatingMetrics.getMaximumCoveragePercentageDynamic(),
                    InetAddress.getLocalHost().getHostName(), pointTags);
    }

    private void addThresholdInPointTags(final Map<String, String> pointTags,
                                         final CodeCoverageGatingMetrics codeCoverageGatingMetrics) {

        pointTags.put(MIN_DEFAULT, String.valueOf(
                    codeCoverageGatingMetrics.getMinimumCoveragePercentageDefault()));
        pointTags.put(MAX_DEFAULT, String.valueOf(
                    codeCoverageGatingMetrics.getMaximumCoveragePercentageDefault()));
        pointTags.put(MIN_DYNAMIC, String.valueOf(
                    codeCoverageGatingMetrics.getMinimumCoveragePercentageDynamic()));
        pointTags.put(MAX_DYNAMIC, String.valueOf(
                    codeCoverageGatingMetrics.getMaximumCoveragePercentageDynamic()));

    }

    private void removeThresholdFromPointTags(final Map<String, String> pointTags) {

        pointTags.remove(MIN_DEFAULT);
        pointTags.remove(MAX_DEFAULT);
        pointTags.remove(MIN_DYNAMIC);
        pointTags.remove(MAX_DYNAMIC);

    }

}
