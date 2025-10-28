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

import java.util.Map;

import javax.xml.datatype.Duration;

import org.eclipse.digitaltwin.aas4j.v3.model.OperationRequestValue;

public abstract class OperationRequestValueBuilder<
        T extends OperationRequestValue, B extends OperationRequestValueBuilder<T, B>>
    extends ExtendableBuilder<T, B> {

  /**
   * This function allows setting a value for inoutputArguments
   *
   * @param inoutputArguments desired value to be set
   * @return Builder object with new value for inoutputArguments
   */
  public B inoutputArguments(Map<String, Object> inoutputArguments) {
    getBuildingInstance().setInoutputArguments(inoutputArguments);
    return getSelf();
  }

  /**
   * This function allows adding a value to the List inoutputArguments
   *
   * @param inoutputArguments desired value to be added
   * @return Builder object with new value for inoutputArguments
   */
  public B inoutputArguments(String key, Object value) {
    getBuildingInstance().getInoutputArguments().put(key, value);
    return getSelf();
  }

  /**
   * This function allows setting a value for inputArguments
   *
   * @param inputArguments desired value to be set
   * @return Builder object with new value for inputArguments
   */
  public B inputArguments(Map<String, Object> inputArguments) {
    getBuildingInstance().setInputArguments(inputArguments);
    return getSelf();
  }

  /**
   * This function allows adding a value to the List inputArguments
   *
   * @param inputArguments desired value to be added
   * @return Builder object with new value for inputArguments
   */
  public B inputArguments(String key, Object inputArguments) {
    getBuildingInstance().getInputArguments().put(key, inputArguments);
    return getSelf();
  }

  /**
   * This function allows setting a value for clientTimeoutDuration
   *
   * @param clientTimeoutDuration desired value to be set
   * @return Builder object with new value for clientTimeoutDuration
   */
  public B clientTimeoutDuration(Duration clientTimeoutDuration) {
    getBuildingInstance().setClientTimeoutDuration(clientTimeoutDuration);
    return getSelf();
  }
}
