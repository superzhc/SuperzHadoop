/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.data.json.generator.config;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andrewserff
 */
public class WorkflowConfig {
    private String workflowName;
    private String workflowFilename;
    private int instances = 1;
    private List<String> customTypeHandlers = new ArrayList<>();
    /**
     * @return the workflowName
     */
    public String getWorkflowName() {
        return workflowName;
    }

    /**
     * @param workflowName the workflowName to set
     */
    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    /**
     * @return the workflowFilename
     */
    public String getWorkflowFilename() {
        return workflowFilename;
    }

    /**
     * @param workflowFilename the workflowFilename to set
     */
    public void setWorkflowFilename(String workflowFilename) {
        this.workflowFilename = workflowFilename;
    }

    public int getInstances() {
        return instances;
    }

    public void setInstances(int instances) {
        this.instances = instances;
    }

    public List<String> getCustomTypeHandlers() {
        return customTypeHandlers;
    }

    public void setCustomTypeHandlers(List<String> customTypeHandlers) {
        this.customTypeHandlers = customTypeHandlers;
    }

}
