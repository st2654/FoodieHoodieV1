package edu.nyu.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import edu.nyu.Recipe;
import edu.nyu.RecipeDAO;


@Path("/recipe")

public class RecipeResource {


	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	// Method to get the REST response for the given query
	@GET
	@Path("{recipe}")
	@Produces({MediaType.APPLICATION_JSON})
	public ArrayList<Recipe> getTodo(@PathParam("recipe") String id,@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit) {	
		RecipeDAO rd=new RecipeDAO();
		ArrayList<Recipe> n=rd.getRecipeFromQuery(id);
		return n;
	}
	
	// Method to get the REST response for the given query
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public List<Recipe> getTodo(@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit) {
		RecipeDAO rd=new RecipeDAO();
		ArrayList<Recipe> n=rd.getRecipeFromQuery("potato");
		return n;
	}
}
