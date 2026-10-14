/*
 * Copyright (c) 2021 Fraunhofer-Gesellschaft zur Foerderung der angewandten Forschung e. V.
 * Copyright (c) 2023, SAP SE or an SAP affiliate company
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.eclipse.digitaltwin.aas4j.v3.model.builder;

import java.util.List;

import javax.xml.datatype.Duration;

import org.eclipse.digitaltwin.aas4j.v3.model.DataSpecificationContentRDF;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringNameType;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringTextType;

public abstract class DataSpecificationContentRDFBuilder<
        T extends DataSpecificationContentRDF, B extends DataSpecificationContentRDFBuilder<T, B>>
    extends ExtendableBuilder<T, B> {

  /**
   * This function allows setting a value for iri
   *
   * @param inoutputArguments desired value to be set
   * @return Builder object with new value for inoutputArguments
   */
  public B iri(String iri) {
    getBuildingInstance().setIri(iri);
    return getSelf();
  }

  /**
   * This function allows adding a value to the List resourceType
   *
   * @param inoutputArguments desired value to be added
   * @return Builder object with new value for inoutputArguments
   */
  public B resourceType(String resourceType) {
    getBuildingInstance().setResourceType(resourceType);
    return getSelf();
  }

  /**
   * This function allows setting a value for inputArguments
   *
   * @param inputArguments desired value to be set
   * @return Builder object with new value for inputArguments
   */
  public B preferredName(List<LangStringNameType> preferredNames) {
    getBuildingInstance().setPreferredName(preferredNames);
    return getSelf();
  }
  public B preferredName(LangStringNameType preferredName) {
	  getBuildingInstance().getPreferredName().add(preferredName);
	  return getSelf();
  }

  /**
   * This function allows adding a value to the List inputArguments
   *
   * @param inputArguments desired value to be added
   * @return Builder object with new value for inputArguments
   */
  public B description(List<LangStringTextType> descriptions) {
    getBuildingInstance().setDescription(descriptions);
    return getSelf();
  }
  public B description(LangStringTextType description) {
	    getBuildingInstance().getDescription().add(description);
	    return getSelf();
	  }

  /**
   * This function allows setting a value for clientTimeoutDuration
   *
   * @param clientTimeoutDuration desired value to be set
   * @return Builder object with new value for clientTimeoutDuration
   */
  public B sameAs(List<String> sameAs) {
    getBuildingInstance().setSameAs(sameAs);
    return getSelf();
  }
  public B sameAs(String sameAs) {
	    getBuildingInstance().getSameAs().add(sameAs);
	    return getSelf();
	  }
}
