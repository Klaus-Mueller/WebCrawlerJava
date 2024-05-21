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
        // Set the BASE_URL environment variable for testing
        this.baseURL = "http://example.com/";
        this.port = 4567;
        this.routeHandler = mock(RouteHandler.class);
    }

    @Test
    public void testAxurAPIConstructor() {
        // Act
        AxurAPI axurAPI = new AxurAPI(this.port, this.routeHandler,this.baseURL);
        
        // Assert
        assertNotNull(axurAPI);
        assertEquals(port, axurAPI.getPort());
        assertNotNull(axurAPI.getGson());
        assertNotNull(axurAPI.getWebCrawler());
    }

    @Test
    public void testSetupRoutes() {
        AxurAPI axurAPI = spy(new AxurAPI(this.port, this.routeHandler, this.baseURL));
        
        // Act
        axurAPI.startAPI();
        
        // Assert
        // Verify that setupRoutes() calls setupPostRoute() and setupGetRoute() on the mocked RouteHandler
        verify(this.routeHandler).setupPostRoute(eq(axurAPI), any(Gson.class), any(SearchTaskManager.class));
        verify(this.routeHandler).setupGetRoute(eq(axurAPI), any(Gson.class), any(SearchTaskManager.class));
    }
   
}