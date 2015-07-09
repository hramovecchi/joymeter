package com.joymeter.entity.util;

import java.util.UUID;

public class SessionUtils {

	public static String randomSessionToken() {
		String part1 = Long.toHexString(UUID.randomUUID()
				.getLeastSignificantBits());
		String part2 = Long.toHexString(UUID.randomUUID()
				.getLeastSignificantBits());
		
		return String.format("%s-%s", part1, part2);
	}


}
