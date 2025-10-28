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

package org.eclipse.digitaltwin.aas4j.v3.model.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.xml.datatype.Duration;

import org.eclipse.digitaltwin.aas4j.v3.model.OperationRequestValue;
import org.eclipse.digitaltwin.aas4j.v3.model.annotations.IRI;
import org.eclipse.digitaltwin.aas4j.v3.model.builder.OperationRequestValueBuilder;

import at.srfg.iasset.repository.model.helper.value.SubmodelElementValue;

/** Default implementation of package org.eclipse.digitaltwin.aas4j.v3.model.OperationRequest */
@IRI("aas:OperationRequest")
public class DefaultOperationRequestValue implements OperationRequestValue {

  @IRI("https://admin-shell.io/aas/3/0/OperationRequest/clientTimeoutDuration")
  protected Duration clientTimeoutDuration;

  @IRI("https://admin-shell.io/aas/3/0/OperationRequest/inoutputArguments")
  protected Map<String, Object> inoutputArguments = new HashMap<>();

  @IRI("https://admin-shell.io/aas/3/0/OperationRequest/inputArguments")
  protected Map<String, Object> inputArguments = new HashMap<>();

  public DefaultOperationRequestValue() {}

  @Override
  public int hashCode() {
    return Objects.hash(this.inoutputArguments, this.inputArguments, this.clientTimeoutDuration);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null) {
      return false;
    } else if (this.getClass() != obj.getClass()) {
      return false;
    } else {
      DefaultOperationRequestValue other = (DefaultOperationRequestValue) obj;
      return Objects.equals(this.inoutputArguments, other.inoutputArguments)
          && Objects.equals(this.inputArguments, other.inputArguments)
          && Objects.equals(this.clientTimeoutDuration, other.clientTimeoutDuration);
    }
  }

  @Override
  public Map<String, Object> getInoutputArguments() {
    return inoutputArguments;
  }

  @Override
  public void setInoutputArguments(Map<String, Object> inoutputArguments) {
    this.inoutputArguments = inoutputArguments;
  }

  @Override
  public Map<String, Object> getInputArguments() {
    return inputArguments;
  }

  @Override
  public void setInputArguments(Map<String, Object> inputArguments) {
    this.inputArguments = inputArguments;
  }

  @Override
  public Duration getClientTimeoutDuration() {
    return clientTimeoutDuration;
  }

  @Override
  public void setClientTimeoutDuration(Duration clientTimeoutDuration) {
    this.clientTimeoutDuration = clientTimeoutDuration;
  }

  public String toString() {
    return String.format(
        "DefaultOperationRequestValue ("
            + "inoutputArguments=%s,"
            + "inputArguments=%s,"
            + "clientTimeoutDuration=%s,"
            + ")",
        this.inoutputArguments, this.inputArguments, this.clientTimeoutDuration);
  }

  /** This builder class can be used to construct a DefaultOperationRequest bean. */
  public static class Builder extends OperationRequestValueBuilder<DefaultOperationRequestValue, Builder> {

    @Override
    protected Builder getSelf() {
      return this;
    }

    @Override
    protected DefaultOperationRequestValue newBuildingInstance() {
      return new DefaultOperationRequestValue();
    }
  }
}
