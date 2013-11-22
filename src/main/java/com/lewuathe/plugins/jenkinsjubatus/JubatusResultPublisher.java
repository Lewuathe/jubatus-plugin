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
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.io.InputStream;
import java.util.Properties;
import java.util.Map;
import java.io.IOException;

public class JubatusResultPublisher extends Publisher {

	private final String jubaHostname;
	private final int    jubaPort;

	@DataBoundConstructor 
	public JubatusResultPublisher(String jubaHostname, String jubaPort) {
		this.jubaHostname = jubaHostname;
		this.jubaPort     = Integer.parseInt(jubaPort);
	}

	public String getJubaHostname() {
		return this.jubaHostname;
	}

	public String getJubaPort() {
		return String.valueOf(this.jubaPort);
	}

	@Override 
	public boolean perform(@SuppressWarnings("rawtypes")  AbstractBuild<?, ?> build, Launcher launder, BuildListener listener)  {
		try {
			listener.getLogger().println("Juba hostname: " + jubaHostname);
			listener.getLogger().println("Juba port number: " + jubaPort);
			
			Map variables = build.getBuildVariables();
			listener.getLogger().println(variables);

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
