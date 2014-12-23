package me.zwap1233.launcher.Authentication;

import java.io.IOException;
import java.util.UUID;

import org.json.JSONObject;

public class Refresh extends Yggdrasil{
	
	private String accesstoken;
	private String clienttoken;

	public Refresh(UUID uuid, String accesstoken, String clienttoken) {
		super(uuid, "/refresh");
		
		this.accesstoken = accesstoken;
		this.clienttoken = clienttoken;
		
		payload = createPayload();
		
		try {
			execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		accesstoken = null;
		clienttoken = null;
	}

	@Override
	protected JSONObject createPayload() {
		JSONObject obj = new JSONObject();
		
		obj.put("accessToken", accesstoken);
		obj.put("clientToken", clienttoken);
		
		return obj;
	}
}
