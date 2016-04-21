/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.wandrell.example.mule.wss.security.callback;

import java.io.IOException;
import java.util.Collections;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.saml.ext.SAMLCallback;
import org.apache.ws.security.saml.ext.bean.AuthenticationStatementBean;
import org.apache.ws.security.saml.ext.bean.SubjectBean;
import org.apache.ws.security.saml.ext.builder.SAML2Constants;
import org.opensaml.common.SAMLVersion;

/**
 * Callback handler for handling SAML 2.0 messages. It will use a SAML
 * properties file to set up this message.
 * <p>
 * This is used by the the SAML user-based authentications.
 *
 * @author Bernardo Martínez Garrido
 */
public final class SamlCallbackHandler implements CallbackHandler {

    /**
     * SAML 2.0 confirmation method.
     */
    private String confirmationMethod;
    /**
     * Name of the subject.
     */
    private String subjectName;
    /**
     * Qualifier for the subject.
     */
    private String subjectQualifier;

    /**
     * Default constructor.
     */
    public SamlCallbackHandler() {
        super();

        // TODO: Set these values through the constructor
        subjectName = "EntityServices";
        subjectQualifier = "www.wandrell.com";
        confirmationMethod = SAML2Constants.CONF_SENDER_VOUCHES;
    }

    @Override
    public final void handle(final Callback[] callbacks) throws IOException,
            UnsupportedCallbackException {
        SAMLCallback samlCallback;            // SAML callback
        SubjectBean subject;                  // Subject data
        AuthenticationStatementBean authBean; // Auth statement

        for (final Callback callback : callbacks) {
            if (callback instanceof SAMLCallback) {
                samlCallback = (SAMLCallback) callback;

                subject = new SubjectBean(subjectName, subjectQualifier,
                        confirmationMethod);

                samlCallback.setSamlVersion(SAMLVersion.VERSION_20);
                samlCallback.setSubject(subject);

                authBean = new AuthenticationStatementBean();
                authBean.setSubject(subject);
                authBean.setAuthenticationMethod("Password");

                samlCallback.setAuthenticationStatementData(Collections
                        .singletonList(authBean));
            } else {
                throw new UnsupportedCallbackException(callback,
                        "Unrecognized Callback");
            }
        }
    }

}
