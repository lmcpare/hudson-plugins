/* 
 * Copyright 2008 Tom Huybrechts and hudson.dev.java.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.  
 * 
 */
package hudson.stagingworkflow;

import hudson.jbpm.model.Form;

import java.io.IOException;

import javax.servlet.ServletException;

import org.jbpm.JbpmConfiguration;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class EndVoteForm extends Form {

	public EndVoteForm(TaskInstance taskInstance) {
		super(taskInstance);
	}

	@Override
	public void handle(StaplerRequest request, StaplerResponse response)
			throws ServletException, IOException {
		TaskInstance taskInstance = getTaskInstance();
		Token token = taskInstance.getToken();

		taskInstance.end();
		token.signal("completed");
		
		JbpmConfiguration.getInstance().getCurrentJbpmContext().save(token);

	}

}
