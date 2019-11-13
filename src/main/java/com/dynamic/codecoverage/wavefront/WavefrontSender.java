package com.dynamic.codecoverage.wavefront;

import com.dynamic.codecoverage.exceptions.WavefrontClientException;
import com.dynamic.codecoverage.model.ICodeCoverageState;
import com.dynamic.codecoverage.model.dto.ServiceObject;
import com.dynamic.codecoverage.utils.WFClient;

public abstract class WavefrontSender {

    public static final String PREFIX = "code-coverage.";
    WFClient wfClient;

    public WavefrontSender(final String wavefrontProxyUrl,
                           final int wavefrontProxyPort)
        throws WavefrontClientException {
        wfClient = new WFClient(wavefrontProxyUrl, wavefrontProxyPort);
    }

    public abstract void sendDataToWavefront(String reporterPrefix,
                                             ServiceObject serviceObject,
                                             ICodeCoverageState codeCoverageState)
        throws WavefrontClientException;

}
