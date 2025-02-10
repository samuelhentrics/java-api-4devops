package com.api.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "bid")
public class Bid {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotNull(message = "bid cannot be null.")
	@Column(name = "bid")
	private double bid;
	@NotNull(message = "id_product cannot be null.")
	@Column(name = "id_product")
	private int idProduct;
	@NotNull(message = "id_buyer cannot be null.")
	@Column(name = "id_buyer")
	private int idBuyer;
	@NotNull(message = "id_seller cannot be null.")
	@Column(name = "id_seller")
	private int idSeller;
	@Column(name = "message")
	private String message;
	@NotNull(message = "date_create cannot be null.")
	@Column(name = "date_create")
	private Date dateCreate;
	@NotNull(message = "date_update cannot be null.")
	@Column(name = "date_update")
	private Date dateUpdate;
	@NotNull(message = "active cannot be null.")
	@Column(name = "active")
	private boolean active;
}
