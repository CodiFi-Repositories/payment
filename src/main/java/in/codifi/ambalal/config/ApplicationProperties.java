package in.codifi.ambalal.config;

import javax.inject.Singleton;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.Setter;

@Singleton
@Getter
@Setter
public class ApplicationProperties {

	@ConfigProperty(name = "appconfig.log.db.name")
	String logDBName;
	
	@ConfigProperty(name = "appconfig.file.basepath")
	String fileBasePath;
	
}
