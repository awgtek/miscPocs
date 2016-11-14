package com.awgtek.dynamicspringmodel.entities;

import org.hibernate.validator.constraints.Email;

public class BasicPojo {

    private String email;
    private SomeType someType = SomeType.TYPE2;
    
    
    public BasicPojo() {
        super();
    }

    @Email
    public String getEmail() {
        return this.email;
    }


    public void setEmail(final String email) {
        this.email = email;
    }

	public SomeType getSomeType() {
		return someType;
	}

	public void setSomeType(SomeType someType) {
		this.someType = someType;
	}

	@Override
	public String toString() {
		return "BasicPojo [email=" + email + ", someType=" + someType + "]";
	}


    
}
