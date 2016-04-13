package com.hammam.business.om.controller;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.hammam.business.om.jpa.Sorder;

@FacesConverter("sorderConverter")
public class SorderConverter implements Converter {
 
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
        	
            try {
                Sorder sorder = (Sorder) SorderHandler.sordersMap.get(value);
                if(sorder == null)
                {
                	Sorder so = new Sorder();
                	so.setCustomerName(value);
                    return so;
                }
                return sorder;
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid customer name."));
            }
        }
        else {
        	Sorder so = new Sorder();
        	so.setCustomerName(value);
            return so;
        }
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((Sorder) object).getCustomerName());
        }
        else {
            return null;
        }
    }   
} 