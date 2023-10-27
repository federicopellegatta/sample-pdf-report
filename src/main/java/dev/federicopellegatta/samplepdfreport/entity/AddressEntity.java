package dev.federicopellegatta.samplepdfreport.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "t_address")
public class AddressEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;
	@Column(name = "street", nullable = false)
	private String street;
	@Column(name = "number", nullable = false)
	private int number;
	@Column(name = "city", nullable = false)
	private String city;
	@Column(name = "zip_code", nullable = false)
	private String zipCode;
	@Column(name = "country", nullable = false)
	private String country;
	
	@Override
	public String toString() {
		return street + ", " + number + ", " + zipCode + ", " + city + ", " + country;
	}
}