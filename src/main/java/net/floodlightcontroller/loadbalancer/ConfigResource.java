/**
 *    Copyright 2013, Big Switch Networks, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License"); you may
 *    not use this file except in compliance with the License. You may obtain
 *    a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *    License for the specific language governing permissions and limitations
 *    under the License.
 **/

package net.floodlightcontroller.loadbalancer;

import java.util.Collections;

import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

public class ConfigResource extends ServerResource{

	@Post
	@Put
	public Object config() {
		ILoadBalancerService lbs = (ILoadBalancerService) getContext().getAttributes().get(ILoadBalancerService.class.getCanonicalName());

		if (getReference().getPath().contains(LoadBalancerWebRoutable.ENABLE_STR)) {
			lbs.healthMonitoring(true);
			return Collections.singletonMap("health monitors", "enabled");
		}

		if (getReference().getPath().contains(LoadBalancerWebRoutable.DISABLE_STR)) {
			lbs.healthMonitoring(false);
			return Collections.singletonMap("health monitors", "disabled");
		}

		if (getReference().getPath().contains(LoadBalancerWebRoutable.MONITORS_STR)) {
			String period = (String) getRequestAttributes().get("period");
			try{
				int val = Integer.valueOf(period);
				return lbs.setMonitorsPeriod(val);
			}catch(Exception e) {
				return "{\"status\" : \"Failed! " + e.getMessage() + "\"}";

			}	
		}
		return Collections.singletonMap("ERROR", "Unimplemented configuration option");
	}
}