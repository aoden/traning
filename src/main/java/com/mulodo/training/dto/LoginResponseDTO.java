package com.mulodo.training.dto;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
public class LoginResponseDTO {

	
	private ErrorDTO errorDTO;
	
	protected String token;
	
	public ErrorDTO getErrorDTO() {
		return errorDTO;
	}
	@XmlElement(name="error")
	public void setErrorDTO(ErrorDTO errorDTO) {
		this.errorDTO = errorDTO;
	}
	@XmlElement
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public LoginResponseDTO(ErrorDTO errorDTO, String token) {
		//super();
		this.errorDTO = errorDTO;
		this.token = token;
	}
	public LoginResponseDTO() {
		super();
	}
	
	
}
