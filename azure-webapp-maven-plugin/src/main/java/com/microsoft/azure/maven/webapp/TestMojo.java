/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.maven.webapp;

import com.microsoft.applicationinsights.core.dependencies.apachecommons.lang3.exception.ExceptionUtils;
import com.microsoft.applicationinsights.internal.config.ApplicationInsightsXmlConfiguration;
import com.microsoft.applicationinsights.internal.logger.InternalLogger;
import org.apache.maven.plugins.annotations.Mojo;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Mojo(name = "test")
public class TestMojo extends AbstractWebAppMojo {
    @Override
    protected void doExecute() throws Exception {
        final JAXBContext jaxbContext = JAXBContext.newInstance(ApplicationInsightsXmlConfiguration.class);
        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        //IOUtils.copy(this.getClass().getClassLoader().getResourceAsStream("ApplicationInsights.xml"),new
        // FileOutputStream("D:\\java8.txt"))
        final XMLStreamReader resourceFileReader = getXmlStreamReader(new FileInputStream(new File("D:\\GitRepo" +
            "\\azure-maven-plugins\\azure-webapp-maven-plugin\\src\\main\\resources\\ApplicationInsights.xml")));
//        final XMLStreamReader resourceFileReader =
//            getXmlStreamReader(this.getClass().getClassLoader().getResourceAsStream(
//            "ApplicationInsights.xml"));
        final ApplicationInsightsXmlConfiguration applicationInsights =
            (ApplicationInsightsXmlConfiguration) unmarshaller.unmarshal(resourceFileReader);
        this.warning(applicationInsights.getInstrumentationKey());
    }

    private XMLStreamReader getXmlStreamReader(InputStream input) {
        try {
            final XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty("javax.xml.stream.isSupportingExternalEntities", false);
            factory.setProperty("javax.xml.stream.supportDTD", false);
            return factory.createXMLStreamReader(input);
        } catch (Throwable var3) {
            InternalLogger.INSTANCE.error("Failed to create stream reader for configuration file: '%s'",
                new Object[]{ExceptionUtils.getStackTrace(var3)});
            return null;
        }
    }
}
