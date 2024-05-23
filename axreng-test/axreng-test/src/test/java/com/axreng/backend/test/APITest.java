package com.axreng.backend.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axreng.backend.API;
import com.axreng.backend.RouteHandler;
import com.axreng.backend.SearchTaskManager;
import com.google.gson.Gson;

public class APITest {

	String baseURL;
	int port;
	RouteHandler routeHandler;

	@BeforeEach
	public void init() {
		// Set up necessary fields before each test
		this.baseURL = "http://example.com/";
		this.port = 4567;
		this.routeHandler = mock(RouteHandler.class);
	}

	@Test
	public void testAPIConstructorWithBaseURL() {
		// Act
		API api = new API(this.port, this.routeHandler, this.baseURL);

		// Assert
		assertNotNull(api);
		assertEquals(port, api.getPort());
		assertNotNull(api.getGson());
	}


	@Test
	public void testapiConstructorWithInvalidBaseURL() {
		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> {
			new API(this.port, this.routeHandler, "");
		});
	}

	@Test
	public void testSetupRoutes() {
		API api = spy(new API(this.port, this.routeHandler, this.baseURL));
		api.startAPI();
		verify(this.routeHandler).setupSearchTaskPostRoute(eq(api), any(Gson.class), any(SearchTaskManager.class));
		verify(this.routeHandler).setupSearchTaskGetRoute(eq(api), any(Gson.class), any(SearchTaskManager.class));
		verify(this.routeHandler).setupGetActiveTasks(eq(api), any(Gson.class), any(SearchTaskManager.class));
		verify(this.routeHandler).setupGetCompletedTasks(eq(api), any(Gson.class), any(SearchTaskManager.class));
	}
}
