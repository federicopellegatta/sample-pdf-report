package dev.federicopellegatta.samplepdfreport.dto;

import dev.federicopellegatta.samplepdfreport.entity.AddressEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AddressResponse {
	private String street;
	private int number;
	private String city;
	private String zipCode;
	private String country;
	
	public AddressResponse(AddressEntity address) {
		this.street = address.getStreet();
		this.number = address.getNumber();
		this.city = address.getCity();
		this.zipCode = address.getZipCode();
		this.country = address.getCountry();
	}
	
	@Override
	public String toString() {
		return street + ", " + number + ", " + zipCode + ", " + city + ", " + country;
	}
}
