package com.sap.jsonmodels;

import com.google.gson.annotations.Expose;

public class Properties {

	@Expose
	private String qualifier = "";
	@Expose
	private String dataType = "";
	@Expose
	private String persistence = "";
	@Expose
	private boolean redeclare = false;
	@Expose
	private String modifiers[];
	@Expose
	private String twinFeild = "";

	@Expose
	private String dataBeanCanonicalName = "";
	@Expose
	private String extensionName = "";

	@Expose
	private String modelPackage = "";

	public String getModelPackage() {
		return modelPackage;
	}

	public void setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
	}

	@Expose
	private String aliasName = "";

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	/**
	 * @return the extensionName
	 */
	public String getExtensionName() {
		return extensionName;
	}

	/**
	 * @param extensionName
	 *            the extensionName to set
	 */
	public void setExtensionName(String extensionName) {
		this.extensionName = extensionName;
	}

	/**
	 * @return the dataBeanCanonicalName
	 */
	public String getDataBeanCanonicalName() {
		return dataBeanCanonicalName;
	}

	/**
	 * @param dataBeanCanonicalName
	 *            the dataBeanCanonicalName to set
	 */
	public void setDataBeanCanonicalName(String dataBeanCanonicalName) {
		this.dataBeanCanonicalName = dataBeanCanonicalName;
	}

	public String getTwinFeild() {
		return twinFeild;
	}

	public void setTwinFeild(String twinFeild) {
		this.twinFeild = twinFeild;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getPersistence() {
		return persistence;
	}

	public void setPersistence(String persistence) {
		this.persistence = persistence;
	}

	public boolean getRedeclare() {
		return redeclare;
	}

	public void setRedeclare(boolean redeclare) {
		this.redeclare = redeclare;
	}

	public String[] getModifiers() {
		return modifiers;
	}

	public void setModifiers(String[] modifiers) {
		this.modifiers = modifiers;
	}

}
