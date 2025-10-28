/*
 * Copyright (c) 2021 Fraunhofer-Gesellschaft zur Foerderung der angewandten Forschung e. V.
 * Copyright (c) 2023, SAP SE or an SAP affiliate company
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.eclipse.digitaltwin.aas4j.v3.model.builder;

import java.util.List;
import java.util.Map;

import org.eclipse.digitaltwin.aas4j.v3.model.ExecutionState;
import org.eclipse.digitaltwin.aas4j.v3.model.Message;
import org.eclipse.digitaltwin.aas4j.v3.model.OperationResultValue;

public abstract class OperationResultValueBuilder<
        T extends OperationResultValue, B extends OperationResultValueBuilder<T, B>>
    extends ExtendableBuilder<T, B> {

  /**
   * This function allows setting a value for executionState
   *
   * @param executionState desired value to be set
   * @return Builder object with new value for executionState
   */
  public B executionState(ExecutionState executionState) {
    getBuildingInstance().setExecutionState(executionState);
    return getSelf();
  }

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
  public B inoutputArguments(String key, Object inoutputArguments) {
    getBuildingInstance().getInoutputArguments().put(key, inoutputArguments);
    return getSelf();
  }

  /**
   * This function allows setting a value for messages
   *
   * @param messages desired value to be set
   * @return Builder object with new value for messages
   */
  public B messages(List<Message> messages) {
    getBuildingInstance().setMessages(messages);
    return getSelf();
  }

  /**
   * This function allows adding a value to the List messages
   *
   * @param messages desired value to be added
   * @return Builder object with new value for messages
   */
  public B messages(Message messages) {
    getBuildingInstance().getMessages().add(messages);
    return getSelf();
  }

  /**
   * This function allows setting a value for outputArguments
   *
   * @param outputArguments desired value to be set
   * @return Builder object with new value for outputArguments
   */
  public B outputArguments(Map<String, Object> outputArguments) {
    getBuildingInstance().setOutputArguments(outputArguments);
    return getSelf();
  }

  /**
   * This function allows adding a value to the List outputArguments
   *
   * @param outputArguments desired value to be added
   * @return Builder object with new value for outputArguments
   */
  public B outputArguments(String key, Object outputArguments) {
    getBuildingInstance().getOutputArguments().put(key, outputArguments);
    return getSelf();
  }

  /**
   * This function allows setting a value for success
   *
   * @param success desired value to be set
   * @return Builder object with new value for success
   */
  public B success(boolean success) {
    getBuildingInstance().setSuccess(success);
    return getSelf();
  }
}
