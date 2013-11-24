package com.lewuathe.plugins.jenkinsjubatus;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.util.FormValidation;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.util.*;
import java.io.InputStream;
import java.io.IOException;

public class JubatusResultPublisher extends Recorder {

	private final String jubaHostname;
	private final int    jubaPort;
	private final String jubaName;

	@DataBoundConstructor 
	public JubatusResultPublisher(String jubaHostname, String jubaPort, String jubaName) {
		this.jubaHostname = jubaHostname;
		this.jubaPort     = Integer.parseInt(jubaPort);
		this.jubaName     = jubaName;
	}

	public String getJubaHostname() {
		return this.jubaHostname;
	}

	public String getJubaPort() {
		return String.valueOf(this.jubaPort);
	}

	public String getJubaName() {
		return this.jubaName;
	}

	@Override 
	public boolean perform(@SuppressWarnings("rawtypes")  AbstractBuild<?, ?> build, Launcher launder, BuildListener listener)  {
		try {
			JubatusClient client = new JubatusClient(this.jubaHostname, this.jubaPort, this.jubaName);

			listener.getLogger().println("Juba hostname: " + jubaHostname);
			listener.getLogger().println("Juba port number: " + jubaPort);
			listener.getLogger().println("Juba name: " + jubaName);
			
			listener.getLogger().println(build.getWorkspace());
			listener.getLogger().println(build.getHudsonVersion());
			listener.getLogger().println(build.getEnvironments());
			listener.getLogger().println(build.getBuiltOnStr());
			listener.getLogger().println(build.getResult());

			Map m = new HashMap<String, String>();
			
			m.put("workspace", build.getWorkspace());
			m.put("hudsonVersion", build.getHudsonVersion());
			m.put("builtOnStr", build.getBuiltOnStr());
			client.train("Success", m);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl)super.getDescriptor();
	}

	@Extension
	public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

		public DescriptorImpl() {
			load();
		}

		public FormValidation doCheckJubaHostname(@QueryParameter String value) throws IOException {
			if(value.length() == 0) {
				return FormValidation.error("Please set hostname");
			}
			return FormValidation.ok();
		}

		public FormValidation doCheckJubaPort(@QueryParameter String value) throws IOException {
			if(value.length() == 0) {
				return FormValidation.error("Please set port number");
			}
			try {
				int i = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				return FormValidation.error("Please set port integer number");
			}
			return FormValidation.ok();
		}

		public FormValidation doCheckJubaName(@QueryParameter String value) throws IOException {
			if(value.length() == 0) {
				return FormValidation.error("Please set Jubatus instance name");
			}
			return FormValidation.ok();
		}
		
		@Override
		public boolean isApplicable(@SuppressWarnings("rawtypes") Class<? extends AbstractProject> jobType) {
			return true;
		}
		
		
        @Override
			public String getDisplayName() {
            return "Jubatus Publisher";
        }
	}
}
