package com.axreng.backend.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axreng.backend.API;
import com.axreng.backend.DefaultRouteHandler;
import com.axreng.backend.SearchTask;
import com.axreng.backend.SearchTaskManager;
import com.axreng.backend.TaskResponse;
import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;

public class DefaultRouteHandlerTest {

	private DefaultRouteHandler routeHandler;
	private Gson gson;

	@BeforeEach
	public void setUp() {
		routeHandler = new DefaultRouteHandler();
		gson = new Gson();
	}

	@Test
	public void testSetupPostRoute() throws Exception {
	    API api = mock(API.class);
	    SearchTaskManager searchTaskManager = mock(SearchTaskManager.class);
	    Request request = mock(Request.class);
	    Response response = mock(Response.class);
	    when(request.body()).thenReturn("{\"keyword\":\"test keyword\"}");
	    String taskId = "task123";
	    when(searchTaskManager.startSearch("test keyword")).thenReturn(taskId);
	    Route postRoute = routeHandler.createSearcTaskPostRoute(api, gson, searchTaskManager);
	    Object result = postRoute.handle(request, response);
	    verify(response).type("application/json");
	    verify(response).status(200); 
	    verify(response).body(gson.toJson(new TaskResponse(taskId)));
	    assertEquals(gson.toJson(new TaskResponse(taskId)), result);
	}
	
	 @Test
	    public void testSetupGetRoute() throws Exception {
	        API api = mock(API.class);
	        SearchTaskManager searchTaskManager = mock(SearchTaskManager.class);
	        Request request = mock(Request.class);
	        Response response = mock(Response.class);
	        when(request.params(":id")).thenReturn("task123");
	        SearchTask task = new SearchTask("Test Task");
	        when(searchTaskManager.getTask("task123")).thenReturn(task);
	        Route getRoute = routeHandler.createSearchTaskGetRoute(api, gson, searchTaskManager);
	        Object result = getRoute.handle(request, response);
	        verify(response).type("application/json");
	        verify(response).body(gson.toJson(task));
	        assertEquals(gson.toJson(task), result);
	    }
	 
	 @Test
	    public void testSetupGetActiveTasks() throws Exception {
	        API api = mock(API.class);
	        SearchTaskManager searchTaskManager = mock(SearchTaskManager.class);
	        Request request = mock(Request.class);
	        Response response = mock(Response.class);
	        List<SearchTask> activeTasks = Arrays.asList(
	                new SearchTask("Active Task 1"),
	                new SearchTask("Active Task 2")
	        );
	        
	        when(searchTaskManager.getActiveSearchTaks()).thenReturn(activeTasks);
	        Route getRoute = routeHandler.createGetActiveTasksRoute(api, gson, searchTaskManager);
	        Object result = getRoute.handle(request, response);
	        verify(response).type("application/json");
	        verify(response).status(200);
	        verify(response).body(gson.toJson(activeTasks));
	        verify(searchTaskManager).getActiveSearchTaks();
	        assertEquals(gson.toJson(activeTasks), result);
	    }
	 
	 @Test
	    public void testSetupGetCompletedTasks() throws Exception {
	        API api = mock(API.class);
	        SearchTaskManager searchTaskManager = mock(SearchTaskManager.class);
	        Request request = mock(Request.class);
	        Response response = mock(Response.class);
	        List<SearchTask> completedTasks = Arrays.asList(
	                new SearchTask("Completed Task 1"),
	                new SearchTask("Completed Task 2")
	        );
	        when(searchTaskManager.getCompletedSearchTaks()).thenReturn(completedTasks);
	        Route getRoute = routeHandler.createGetCompletedTasksRoute(api, gson, searchTaskManager);
	        Object result = getRoute.handle(request, response);
	        verify(response).status(200);
	        verify(response).body(gson.toJson(completedTasks));
	        verify(response).type("application/json");
	        verify(searchTaskManager).getCompletedSearchTaks();
	        assertEquals(gson.toJson(completedTasks), result);
	    }
}
