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
import com.axreng.backend.WebCrawler;
import com.google.gson.Gson;

public class AxurAPITest {

	String baseURL;
	int port;

	@BeforeEach
    public void init() {
        // Set the BASE_URL environment variable for testing
        this.baseURL = "http://example.com/";
        this.port = 4567;
    }

    @Test
    public void testAxurAPIConstructor() {
        // Arrange
        int port = 4567;
        RouteHandler routeHandler = mock(RouteHandler.class);
        // Act
        AxurAPI axurAPI = new AxurAPI(this.port, routeHandler,this.baseURL);
        
        // Assert
        assertNotNull(axurAPI);
        assertEquals(port, axurAPI.getPort());
        assertNotNull(axurAPI.getGson());
        assertNotNull(axurAPI.getWebCrawler());
    }

    @Test
    public void testSetupRoutes() {
        // Arrange
        RouteHandler routeHandler = mock(RouteHandler.class);
        AxurAPI axurAPI = spy(new AxurAPI(this.port, routeHandler, this.baseURL));
        
        // Act
        axurAPI.startAPI();
        
        // Assert
        // Verify that setupRoutes() calls setupPostRoute() and setupGetRoute() on the mocked RouteHandler
        verify(routeHandler).setupPostRoute(eq(axurAPI), any(Gson.class), any(WebCrawler.class));
        verify(routeHandler).setupGetRoute(eq(axurAPI), any(Gson.class), any(WebCrawler.class));
    }
    
    @Test
    public void testExceptionOnClassInstance() {
        
        RouteHandler routeHandler = mock(RouteHandler.class);
        assertThrows(IllegalArgumentException.class, () -> new AxurAPI(port, routeHandler));
    }
}