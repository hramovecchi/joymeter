package com.joymeter.entity.util;

import java.util.UUID;

public class SessionUtils {

	
	public static String generateSessionId() {
		String uuid = Long.toHexString(UUID.randomUUID()
				.getLeastSignificantBits());
		String onlineId = String.format("s-%s", uuid);
		return onlineId;
	}


}
