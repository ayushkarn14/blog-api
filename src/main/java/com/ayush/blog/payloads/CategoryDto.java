package com.ayush.blog.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {

	private int categoryId;
	
	@NotBlank
	@Size(min=4, max=10)
	private String categoryTitle;
	
	@NotBlank
	@Size(min=4, max=50)
	private String categoryDescription;
}
