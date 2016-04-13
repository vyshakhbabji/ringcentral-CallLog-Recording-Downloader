
import java.io.IOException;

import core.SDK;
import http.Client;
import platform.Platform;


public class Authorize {
	
	Client client ;
	
	public Platform  authenticate() {
		SDK sdk = new SDK(<apikey>,
				<apisecret>,
				Platform.Server.SANDBOX);
		Platform platform = sdk.platform();
		try {
			platform.login("<userphone - main number >", "<extension>", "<password>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return platform;
	}

}
