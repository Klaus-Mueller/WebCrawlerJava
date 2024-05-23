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

import com.axreng.backend.AxurAPI;
import com.axreng.backend.RouteHandler;
import com.axreng.backend.SearchTaskManager;
import com.google.gson.Gson;

public class AxurAPITest {

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
	public void testAxurAPIConstructorWithBaseURL() {
		// Act
		AxurAPI axurAPI = new AxurAPI(this.port, this.routeHandler, this.baseURL);

		// Assert
		assertNotNull(axurAPI);
		assertEquals(port, axurAPI.getPort());
		assertNotNull(axurAPI.getGson());
	}


	@Test
	public void testAxurAPIConstructorWithInvalidBaseURL() {
		// Act & Assert
		assertThrows(IllegalArgumentException.class, () -> {
			new AxurAPI(this.port, this.routeHandler, "");
		});
	}

	@Test
	public void testSetupRoutes() {
		AxurAPI axurAPI = spy(new AxurAPI(this.port, this.routeHandler, this.baseURL));
		axurAPI.startAPI();
		verify(this.routeHandler).setupSearchTaskPostRoute(eq(axurAPI), any(Gson.class), any(SearchTaskManager.class));
		verify(this.routeHandler).setupSearchTaskGetRoute(eq(axurAPI), any(Gson.class), any(SearchTaskManager.class));
		verify(this.routeHandler).setupGetActiveTasks(eq(axurAPI), any(Gson.class), any(SearchTaskManager.class));
		verify(this.routeHandler).setupGetCompletedTasks(eq(axurAPI), any(Gson.class), any(SearchTaskManager.class));
	}
}
