package at.srfg.iasset.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public class ZenonAlarm {
	private String variable;
	private String alarmText;
	private String alarmGroup;
	private String alarmClass;
	private Instant timeComes;
	private Instant timeGoes;

	/**
	 * @return the variable
	 */
	public String getVariable() {
		return variable;
	}

	/**
	 * @param variable the variable to set
	 */
	public void setVariable(String variable) {
		this.variable = variable;
	}
	/**
	 * @return the alarmText
	 */
	public String getAlarmText() {
		return alarmText;
	}
	/**
	 * @param alarmText the alarmText to set
	 */
	public void setAlarmText(String alarmText) {
		this.alarmText = alarmText;
	}
	/**
	 * @return the alarmGroup
	 */
	public String getAlarmGroup() {
		return alarmGroup;
	}
	/**
	 * @param alarmGroup the alarmGroup to set
	 */
	public void setAlarmGroup(String alarmGroup) {
		this.alarmGroup = alarmGroup;
	}
	/**
	 * @return the alarmClass
	 */
	public String getAlarmClass() {
		return alarmClass;
	}
	/**
	 * @param alarmClass the alarmClass to set
	 */
	public void setAlarmClass(String alarmClass) {
		this.alarmClass = alarmClass;
	}
	/**
	 * @return the timeComes
	 */
	public Instant getTimeComes() {
		return timeComes;
	}
	/**
	 * @param timeComes the timeComes to set
	 */
	public void setTimeComes(Instant timeComes) {
		this.timeComes = timeComes;
	}
	/**
	 * @return the timeGoes
	 */
	public Instant getTimeGoes() {
		return timeGoes;
	}
	/**
	 * @param timeGoes the timeGoes to set
	 */
	public void setTimeGoes(Instant timeGoes) {
		this.timeGoes = timeGoes;
	}

	// Inner Variable class
	public static class Variable {
		@JsonProperty("variableName")
		private String variableName;

		// Getters and setters
		public String getVariableName() {
			return variableName;
		}

		public void setVariableName(String variableName) {
			this.variableName = variableName;
		}
	}

	// Inner AlarmClass class
	public static class AlarmClass {
		private String name;

		// Getters and setters
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	// Inner AlarmGroup class
	public static class AlarmGroup {
		private String name;

		// Getters and setters
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}