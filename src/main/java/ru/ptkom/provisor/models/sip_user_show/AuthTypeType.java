//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.05.07 at 12:50:14 AM VLAT 
//


package ru.ptkom.provisor.models.sip_user_show;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for authTypeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="authTypeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="none"/>
 *     &lt;enumeration value="always"/>
 *     &lt;enumeration value="register"/>
 *     &lt;enumeration value="ldap"/>
 *     &lt;enumeration value="ds"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "authTypeType")
@XmlEnum
public enum AuthTypeType {

    @XmlEnumValue("none")
    NONE("none"),
    @XmlEnumValue("always")
    ALWAYS("always"),
    @XmlEnumValue("register")
    REGISTER("register"),
    @XmlEnumValue("ldap")
    LDAP("ldap"),
    @XmlEnumValue("ds")
    DS("ds");
    private final String value;

    AuthTypeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AuthTypeType fromValue(String v) {
        for (AuthTypeType c: AuthTypeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}