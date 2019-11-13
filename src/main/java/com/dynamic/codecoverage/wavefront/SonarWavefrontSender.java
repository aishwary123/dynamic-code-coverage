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
import com.dynamic.codecoverage.model.SonarCoverageState;
import com.dynamic.codecoverage.model.dto.ServiceObject;

public class SonarWavefrontSender extends WavefrontSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(
                SonarWavefrontSender.class);

    public SonarWavefrontSender(String wavefrontProxyUrl,
                                int wavefrontProxyPort)
        throws WavefrontClientException {
        super(wavefrontProxyUrl, wavefrontProxyPort);
    }

    @Override
    public void sendDataToWavefront(String reporterPrefix,
                                    ServiceObject serviceObject,
                                    ICodeCoverageState codeCoverageState)
        throws WavefrontClientException {
        if (!(codeCoverageState instanceof SonarCoverageState)) {
            throw new ClassCastException(CustomMessages.CLASS_CAST_EXCEPTION);
        }

        SonarCoverageState sonarCoverageState = (SonarCoverageState) codeCoverageState;
        Map<String, String> pointTags = new HashMap<>();
        pointTags.put("serviceName", serviceObject.getServiceName());
        pointTags.put("jobname", serviceObject.getJobName());
        pointTags.put("type", serviceObject.getType().toString());
        pointTags.put("testType", serviceObject.getTestType().toString());
        pointTags.put("product", serviceObject.getProduct());
        final String customPrefix = Strings.isNotBlank(reporterPrefix)
                    ? reporterPrefix + "." + PREFIX
                    : PREFIX;

        try {

            wfClient.saveStats(customPrefix + "line.percentage",
                        sonarCoverageState.getLineCoverage(),
                        InetAddress.getLocalHost().getHostName(), pointTags);

            wfClient.saveStats(customPrefix + "branch.percentage",
                        sonarCoverageState.getBranchCoverage(),
                        InetAddress.getLocalHost().getHostName(), pointTags);

            wfClient.saveStats(customPrefix + "line.totalLines",
                        sonarCoverageState.getLinesToCover(),
                        InetAddress.getLocalHost().getHostName(), pointTags);
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

}
