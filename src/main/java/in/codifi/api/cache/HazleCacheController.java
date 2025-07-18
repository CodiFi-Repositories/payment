package in.codifi.api.cache;

import java.util.Map;

import org.eclipse.microprofile.config.ConfigProvider;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class HazleCacheController {

	
	public static HazleCacheController HazleCacheController = null;
	private HazelcastInstance hz = null;
	String keyFor = ConfigProvider.getConfig().getValue("config.app.hazel.for", String.class);

	public static HazleCacheController getInstance() {
		if (HazleCacheController == null) {
			HazleCacheController = new HazleCacheController();

		}
		return HazleCacheController;
	}

	public HazelcastInstance getHz() {
		if (hz == null) {
			ClientConfig clientConfig = new ClientConfig();
			clientConfig.setClusterName(ConfigProvider.getConfig().getValue("config.app.hazel.cluster", String.class));
			clientConfig.getNetworkConfig()
					.addAddress(ConfigProvider.getConfig().getValue("config.app.hazel.address", String.class));
			hz = HazelcastClient.newHazelcastClient(clientConfig);
		}
		return hz;
	}

	IMap<String, String> authToken = getHz().getMap("authToken" + keyFor);
	private Map<String, String> kraKeyValue = getHz().getMap("kraKeyValue" + keyFor);
	
	
	public Map<String, String> getKraKeyValue() {
		return kraKeyValue;
	}

	public void setKraKeyValue(Map<String, String> kraKeyValue) {
		this.kraKeyValue = kraKeyValue;
	}
	
	
	
	public IMap<String, String> getAuthToken() {
		return authToken;
	}

	public void setAuthToken(IMap<String, String> authToken) {
		this.authToken = authToken;
	}
}
