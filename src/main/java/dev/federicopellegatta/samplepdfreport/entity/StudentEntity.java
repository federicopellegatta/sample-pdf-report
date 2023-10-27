package dev.federicopellegatta.samplepdfreport.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "t_student")
@Getter
@Setter
public class StudentEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "surname", nullable = false)
	private String surname;
	@Column(name = "birth_date", nullable = false)
	private LocalDate birthDate;
	@Column(name = "email")
	private String email;
	@Column(name = "phone_number")
	private String phoneNumber;
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "address", nullable = false)
	private AddressEntity address;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "marks")
	private Collection<MarkEntity> marks;
}