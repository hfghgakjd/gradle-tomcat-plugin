/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.plugins.tomcat

import org.apache.catalina.loader.WebappLoader
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.InputFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Deploys a WAR to an embedded Tomcat web container.
 *
 * @author Benjamin Muschko
 */
class TomcatRunWar extends AbstractTomcatRunTask {
    static Logger logger = LoggerFactory.getLogger(TomcatRunWar.class)
    private File webApp

    @Override
    void validateConfiguration() {
        super.validateConfiguration()

        if(!getWebApp() || !getWebApp().exists()) {
            throw new InvalidUserDataException("Web application WAR "
                    + (getWebApp() == null ? "null" : getWebApp().getCanonicalPath())
                    + " does not exist")
        }
        else {
            logger.info "Web application WAR = ${getWebApp().getCanonicalPath()}"
        }
    }

    @Override
    void configureWebApplication() {
        WebappLoader loader = new WebappLoader(getClass().getClassLoader())

        setContext(getServer().createContext("/" + getContextPath(), getWebApp().getCanonicalPath()))
        getContext().setLoader(loader)
        getContext().setReloadable(reloadable)
        configureDefaultWebXml()
    }

    @InputFile
    public File getWebApp() {
        webApp
    }

    public void setWebApp(File webApp) {
        this.webApp = webApp
    }
}
