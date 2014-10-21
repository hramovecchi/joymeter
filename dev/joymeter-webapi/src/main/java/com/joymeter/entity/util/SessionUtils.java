package com.joymeter.entity.util;

import java.util.UUID;

public class SessionUtils {

	public static String generateSessionId() {
		String uuid = Long.toHexString(UUID.randomUUID()
				.getLeastSignificantBits());
		String sessionId = String.format("q-%s", uuid);
		return sessionId;
	}

}
