//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.05.07 at 12:50:14 AM VLAT 
//


package ru.ptkom.provisor.models.sip_user_show;

import ru.ptkom.provisor.models.Marshallable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="sip" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="domain" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="group" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="complete" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                 &lt;attribute name="auth" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "in")
public class In extends Marshallable {

    protected In.Sip sip;


    public In(String number, String domain, String group, boolean complete, boolean auth){
        Sip sip = new Sip(number, domain, group, complete, auth);
        this.sip = sip;
    }

    public In(){};

    /**
     * Gets the value of the sip property.
     * 
     * @return
     *     possible object is
     *     {@link In.Sip }
     *     
     */
    public In.Sip getSip() {
        return sip;
    }

    /**
     * Sets the value of the sip property.
     * 
     * @param value
     *     allowed object is
     *     {@link In.Sip }
     *     
     */
    public void setSip(In.Sip value) {
        this.sip = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="domain" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="group" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="complete" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *       &lt;attribute name="auth" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Sip {

        @XmlAttribute(name = "domain", required = true)
        protected String domain;
        @XmlAttribute(name = "group", required = true)
        protected String group;
        @XmlAttribute(name = "id")
        protected String id;
        @XmlAttribute(name = "complete")
        protected Boolean complete;
        @XmlAttribute(name = "auth")
        protected Boolean auth;

        /**
         * Gets the value of the domain property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */


        public Sip(String number, String domain, String group, boolean complete, boolean auth){

            if (!number.contains("@")) {
                number += ("@" + domain);
            }

            this.id = number;
            this.domain = domain;
            this.group = group;
            this.complete = complete;
            this.auth = auth;
        }

        public Sip(){}


        public String getDomain() {
            return domain;
        }

        /**
         * Sets the value of the domain property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDomain(String value) {
            this.domain = value;
        }

        /**
         * Gets the value of the group property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGroup() {
            return group;
        }

        /**
         * Sets the value of the group property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGroup(String value) {
            this.group = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setId(String value) {
            this.id = value;
        }

        /**
         * Gets the value of the complete property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isComplete() {
            return complete;
        }

        /**
         * Sets the value of the complete property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setComplete(Boolean value) {
            this.complete = value;
        }

        /**
         * Gets the value of the auth property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isAuth() {
            return auth;
        }

        /**
         * Sets the value of the auth property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setAuth(Boolean value) {
            this.auth = value;
        }

    }

}
