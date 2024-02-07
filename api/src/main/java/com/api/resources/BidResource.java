package com.api.resources;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.api.business.BidBusiness;
import com.api.data.entity.Bid;
import com.api.exception.business.InvalidElementException;
import com.api.exception.business.NotFoundException;
import com.api.exception.business.ResourceStateConflictException;
import com.api.exception.technical.DAOException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Path("Bids")
@Tag(name = "Bids")
public class BidResource {
	@Inject
	private BidBusiness BidBusiness;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get all Bids", description = "Get all Bids", responses = {
			@ApiResponse(responseCode = "200", description = "Succes", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Bid.class)))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	@SecurityRequirement(name = "bearer-auth")
	public Response getBids() throws DAOException {
		return Response.ok(BidBusiness.getAll()).build();
//		return Response.ok("Easy changes").build();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get a Bid", description = "Get a Bid by id", responses = {
			@ApiResponse(responseCode = "200", description = "Succes", content = @Content( mediaType = "application/json",schema = @Schema(implementation = Bid.class))),
			@ApiResponse(responseCode = "404", description = "Bid not found"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	@SecurityRequirement(name = "bearer-auth")
	public Response getBid(@PathParam("id") long id) throws NotFoundException, DAOException, InvalidElementException {
		return Response.ok(BidBusiness.get(id)).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Add Bid", description = "Create a Bid in database", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Bid.class))),
			@ApiResponse(responseCode = "400", description = "Invalid Bid"),
			@ApiResponse(responseCode = "404", description = "Right not found"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	@SecurityRequirement(name = "bearer-auth")
	public Response addBid(Bid Bid) throws InvalidElementException, DAOException, NotFoundException {
		return Response.ok(BidBusiness.add(Bid)).build();
	}
	
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Update Bid", description = "Update a Bid in database", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Bid.class))),
			@ApiResponse(responseCode = "400", description = "Invalid Bid"),
			@ApiResponse(responseCode = "404", description = "Bid or right not found"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	@SecurityRequirement(name = "bearer-auth")
	public Response updateBid(@PathParam("id") long id, Bid Bid) throws InvalidElementException, NotFoundException, DAOException {
		return Response.ok(BidBusiness.update(id, Bid)).build();
	}
	
	@DELETE
	@Path("{id}")
	@Operation(summary = "Delete Bid", description = "Delete a Bid by id.", responses = {
			@ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "409", description = "Bid still used"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	@SecurityRequirement(name = "bearer-auth")
	public Response deleteBid(@PathParam("id") long id) throws ResourceStateConflictException, DAOException {
		BidBusiness.delete(id);
		return Response.noContent().build();
	}
}
