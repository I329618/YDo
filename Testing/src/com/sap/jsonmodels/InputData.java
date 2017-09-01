package com.sap.jsonmodels;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
public class InputData {

                @Expose
                private String projectName="";
                @Expose
                private String itemsName="";
                @Expose
                private String extendedItem="";
                @Expose
                private String deploymentTable="";
                @Expose
                private List<Properties> properties=new ArrayList<Properties>();
                @Expose
                private boolean autoCreate=false;
                @Expose
                private boolean autoGenerate=false;
                @Expose
                private boolean addAttribute=false;
                @Expose
                private boolean abstractItem=false;
                @Expose
                private String jaloPackage="";
                
                public String getProjectName() {
                                return projectName;
                }
                public void setProjectName(String projectName) {
                                this.projectName = projectName;
                }
                public String getItemsName() {
                                return itemsName;
                }
                public void setItemsName(String itemsName) {
                                this.itemsName = itemsName;
                }
                public String getExtendedItem() {
                                return extendedItem;
                }
                public void setExtendedItem(String extendedItem) {
                                this.extendedItem = extendedItem;
                }
                public String getDeploymentTable() {
                                return deploymentTable;
                }
                public void setDeploymentTable(String deploymentTable) {
                                this.deploymentTable = deploymentTable;
                }
                public List<Properties> getProperties() {
                                return properties;
                }
                public void setProperties(List<Properties> properties) {
                                this.properties = properties;
                }
                public boolean isAutoCreate() {
                                return autoCreate;
                }
                public void setAutoCreate(boolean autoCreate) {
                                this.autoCreate = autoCreate;
                }
                public boolean isAutoGenerate() {
                                return autoGenerate;
                }
                public void setAutoGenerate(boolean autoGenerate) {
                                this.autoGenerate = autoGenerate;
                }
                public boolean isAddAttribute() {
                                return addAttribute;
                }
                public void setAddAttribute(boolean addAttribute) {
                                this.addAttribute = addAttribute;
                }
                public boolean isAbstractItem() {
                                return abstractItem;
                }
                public void setAbstractItem(boolean abstractItem) {
                                this.abstractItem = abstractItem;
                }
				public String getJalo() {
					return jaloPackage;
				}
				public void setJalo(String jalo) {
					this.jaloPackage = jaloPackage;
				}
                
}
