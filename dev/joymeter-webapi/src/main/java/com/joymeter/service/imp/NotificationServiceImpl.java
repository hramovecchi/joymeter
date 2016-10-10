package com.joymeter.service.imp;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.joymeter.api.client.ClientFactory;
import com.joymeter.api.client.dto.GCMAdviceContainer;
import com.joymeter.api.client.dto.GCMToAndroidClientDTO;
import com.joymeter.entity.Advice;
import com.joymeter.service.NotificationService;
import com.sun.jersey.api.client.Client;

@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {

	public static final String autorizationHeader = "Authorization";

	@Value("${gcm.notification.uri}")
	private String baseUri;

	@Value("${gcm.notification.path}")
	private String path;

	@Value("${gcm.api.key}")
	private String apiKey;

	public void sendNotificationMessage(String userGCMID, Advice advice) {
		if (advice != null && !StringUtils.isEmpty(apiKey)){
			Client client = ClientFactory.create();
			URI resourceUri = UriBuilder.fromUri(baseUri).path(path).build();
			GCMToAndroidClientDTO dto = new GCMToAndroidClientDTO();
			dto.setTo(userGCMID);
			dto.setData(new GCMAdviceContainer(advice));

			client.resource(resourceUri)
				.header(autorizationHeader, "key=" + apiKey)
				.type(MediaType.APPLICATION_JSON)
				.post(dto);
		}
	}
}
