/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2016 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.hk2.configuration.hub.test;

import javax.inject.Inject;

import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.configuration.hub.api.Hub;
import org.glassfish.hk2.configuration.hub.api.Instance;
import org.glassfish.hk2.configuration.hub.api.ManagerUtilities;
import org.glassfish.hk2.configuration.hub.api.WriteableBeanDatabase;
import org.glassfish.hk2.configuration.hub.api.WriteableType;
import org.junit.Assert;
import org.junit.Before;
import org.jvnet.hk2.testing.junit.HK2Runner;

/**
 * @author jwells
 *
 */
public class HubTestBase extends HK2Runner {
    @Inject
    protected DynamicConfigurationService dcs;
    
    protected Hub hub;
    
    @Before
    public void before() {
        super.before();
        
        // This is necessary to make running in an IDE easier
        ManagerUtilities.enableConfigurationHub(testLocator);
        
        this.hub = testLocator.getService(Hub.class);
    }
    
    protected void addType(String typeName) {
        addType(typeName, false);
    }
    
    protected void addType(String typeName, boolean asResource) {
        WriteableBeanDatabase wbd = hub.getWriteableDatabaseCopy();
        
        wbd.addType(typeName);
        
        if (asResource) {
            DynamicConfiguration dc = dcs.createDynamicConfiguration();
            
            dc.registerTwoPhaseResources(wbd.getTwoPhaseResource());
            
            dc.commit();
        }
        else {
            wbd.commit();
        }
    }
    
    protected void addTypeAndInstance(String typeName, String instanceKey, Object instanceValue) {
        addTypeAndInstance(typeName, instanceKey, instanceValue, false);
 
    }
    
    protected void addTypeAndInstance(String typeName, String instanceKey, Object instanceValue, boolean asResource) {
        WriteableBeanDatabase wbd = hub.getWriteableDatabaseCopy();
        
        WriteableType wt = wbd.findOrAddWriteableType(typeName);
        
        Instance added = wt.addInstance(instanceKey, instanceValue);
        Assert.assertNotNull(added);
        
        if (asResource) {
            DynamicConfiguration dc = dcs.createDynamicConfiguration();
            
            dc.registerTwoPhaseResources(wbd.getTwoPhaseResource());
            
            dc.commit();
        }
        else {
            wbd.commit();
        }
    }
    
    protected void addTypeAndInstance(String typeName, String instanceKey, Object instanceValue, Object metadata) {
        addTypeAndInstance(typeName, instanceKey, instanceValue, metadata, false);
    }
    
    protected void addTypeAndInstance(String typeName, String instanceKey, Object instanceValue, Object metadata, boolean asResource) {
        WriteableBeanDatabase wbd = hub.getWriteableDatabaseCopy();
        
        WriteableType wt = wbd.findOrAddWriteableType(typeName);
        
        wt.addInstance(instanceKey, instanceValue, metadata);
        
        if (asResource) {
            DynamicConfiguration dc = dcs.createDynamicConfiguration();
            
            dc.registerTwoPhaseResources(wbd.getTwoPhaseResource());
            
            dc.commit();
        }
        else {
            wbd.commit();
        }
    }
    
    protected void removeType(String typeName) {
        removeType(typeName, false);
    }
    
    protected void removeType(String typeName, boolean asResource) {
        WriteableBeanDatabase wbd = hub.getWriteableDatabaseCopy();
        
        wbd.removeType(typeName);
        
        if (asResource) {
            DynamicConfiguration dc = dcs.createDynamicConfiguration();
            
            dc.registerTwoPhaseResources(wbd.getTwoPhaseResource());
            
            dc.commit();
        }
        else {
            wbd.commit();
        }
    }

}
