/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wipro.ats.bdre.md.api.airflow;

import com.wipro.ats.bdre.exception.MetadataException;
import com.wipro.ats.bdre.md.api.GetProperties;
import com.wipro.ats.bdre.md.beans.GetPropertiesInfo;
import com.wipro.ats.bdre.util.AirflowUtil;
import com.wipro.ats.bdre.util.OozieUtil;
import org.apache.log4j.Logger;

import java.util.List;


/**
 * Created by pushpak on 18/07/2016.
 */
public class AirflowGetProperties {
    private static final Logger LOGGER = Logger.getLogger(AirflowGetProperties.class);

    /**
     * default constructor
     */
    private AirflowGetProperties() {

    }

    /**
     * This method calls execute method and persists the output till runtime.
     *
     * @param args String array having configuration group, environment and process-id with their
     *             command line notations.
     */

    public static void main(String[] args) {
        GetProperties getProperties = new GetProperties();
        List<GetPropertiesInfo> getPropertiesInfos = getProperties.execute(args);
        AirflowUtil airflowUtil = new AirflowUtil();
        try {
            airflowUtil.persistBeanList(getPropertiesInfos, false);
        } catch (Exception e) {
            LOGGER.error(e);
            throw new MetadataException(e);
        }
    }
}