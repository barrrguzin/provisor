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
 * <p>Java class for remoteCtrIndicationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="remoteCtrIndicationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="pai"/>
 *     &lt;enumeration value="rpi"/>
 *     &lt;enumeration value="none"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "remoteCtrIndicationType")
@XmlEnum
public enum RemoteCtrIndicationType {

    @XmlEnumValue("pai")
    PAI("pai"),
    @XmlEnumValue("rpi")
    RPI("rpi"),
    @XmlEnumValue("none")
    NONE("none");
    private final String value;

    RemoteCtrIndicationType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RemoteCtrIndicationType fromValue(String v) {
        for (RemoteCtrIndicationType c: RemoteCtrIndicationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
